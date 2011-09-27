/*
 * Copyright: (c) 2004-2011 Mayo Foundation for Medical Education and 
 * Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
 * triple-shield Mayo logo are trademarks and service marks of MFMER.
 *
 * Except as contained in the copyright notice above, or as used to identify 
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.mayo.cts2.framework.service.provider;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Properties;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.core.config.Cts2Config;
import edu.mayo.cts2.framework.core.plugin.PluginClassLoader;

/**
 * A factory for creating ServiceProvider objects.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Component
public class ServiceProviderFactory implements InitializingBean {
	
	private final Log log = LogFactory.getLog(getClass().getName());

	@Resource
	private Cts2Config cts2Config;

	private static final String PROVIDER_CLASS_PROP = "provider.class";
	
	private ServiceProvider serviceProvider;

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		this.serviceProvider = this.createServiceProvider();
	}
	
	/**
	 * Instantiates a new service provider factory.
	 */
	public ServiceProviderFactory() {
		super();
	}

	/**
	 * Instantiates a new service provider factory.
	 *
	 * @param cts2Config the cts2 config
	 */
	public ServiceProviderFactory(Cts2Config cts2Config) {
		super();
		this.cts2Config = cts2Config;
	}
	
	public synchronized ServiceProvider getServiceProvider() {
		if(this.serviceProvider == null){
			this.serviceProvider = this.createServiceProvider();
		}
		
		return this.serviceProvider;
	}

	/**
	 * Creates a new ServiceProvider object.
	 *
	 * @return the service provider
	 */
	private ServiceProvider createServiceProvider() {
		String requestedProviderName = cts2Config
			.getProperty("provider.name");
		
		if (StringUtils.isBlank(requestedProviderName)) {
			log.warn("No Provider declared.");
			
			return new EmptyServiceProvider();
		}
	
		File dir = new File(cts2Config.getPluginsDirectory());

		if (!dir.exists() || dir.listFiles().length == 0) {
			log.warn("Plugin Directory: "
					+ cts2Config.getPluginsDirectory()
					+ " has no available plugins.");
			
			return new EmptyServiceProvider();
		}

		for (File plugin : dir.listFiles()) {

			try {

				for (File f : plugin.listFiles()) {
					if (f.getName().endsWith("plugin.properties")) {
						Properties properites = new Properties();
						FileInputStream fis = new FileInputStream(f);

						try {
							properites.load(fis);

							String foundProviderName = properites
									.getProperty("provider.name");

							if (!StringUtils.equals(foundProviderName,
									requestedProviderName)) {
								continue;
							}

							String providerClassName = properites
								.getProperty(PROVIDER_CLASS_PROP);
			
							ServiceProvider serviceProvider;
	
							try {
								serviceProvider = this.loadServiceProviderClass(
										providerClassName, 
										this.getClass().getClassLoader());
								
								log.info("Getting Service Provider default classpath.");
							} catch (ClassNotFoundException e) {
								//class not on default classpath -- check plugins.
								log.info("Getting Service Provider from plugin at: " + plugin);
								
								PluginClassLoader pluginClassLoader = new PluginClassLoader(
										this.getClass().getClassLoader(),
										plugin.getAbsolutePath());
			
								serviceProvider = this.loadServiceProviderClass(
										providerClassName, 
										pluginClassLoader);	
								
								Thread.currentThread().setContextClassLoader(
										new URLClassLoader(new URL[0], pluginClassLoader));
							}
	
							return serviceProvider;
						} catch (IOException e) {
							throw new RuntimeException(e);
						} finally {
							fis.close();
						}
					}
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		try {
			return null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Load service provider class.
	 *
	 * @param name the name
	 * @param classLoader the class loader
	 * @return the service provider
	 * @throws ClassNotFoundException the class not found exception
	 */
	private ServiceProvider loadServiceProviderClass(String name, ClassLoader classLoader) throws ClassNotFoundException {
		ServiceProvider serviceProvider;
		try {
			serviceProvider = (ServiceProvider) classLoader
				.loadClass(name).newInstance();
		} catch (InstantiationException e) {
			throw new IllegalStateException(e);
		} catch (IllegalAccessException e) {
			throw new IllegalStateException(e);
		}
		
		return serviceProvider;
	}
}
