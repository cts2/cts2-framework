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
import java.net.URL;
import java.net.URLClassLoader;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.core.plugin.PluginClassLoader;
import edu.mayo.cts2.framework.service.admin.AdminService;

/**
 * A factory for creating ServiceProvider objects.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Component
public class ServiceProviderFactory implements InitializingBean {

	private final Log log = LogFactory.getLog(getClass().getName());

	@Resource
	private AdminService adminService;

	private ServiceProvider serviceProvider;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
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

	public synchronized ServiceProvider getServiceProvider() {
		if (this.serviceProvider == null) {
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

		File inUsePluginDirectory = this.adminService.getInUsePluginDirectory();

		if (inUsePluginDirectory == null) {
			log.warn("No Service Plugin declared.");

			return new EmptyServiceProvider();
		}
		
		String providerClassName = this.adminService.getCurrentPluginServiceProviderClassName();

		PluginClassLoader pluginClassLoader = new PluginClassLoader(
				this.getClass().getClassLoader(),
				inUsePluginDirectory.getAbsolutePath());

		try {
			ServiceProvider provider = this.loadServiceProviderClass(
					providerClassName, 
					pluginClassLoader);
			

			Thread.currentThread().setContextClassLoader(
					new URLClassLoader(new URL[0], pluginClassLoader));

			return provider
					;
		} catch (ClassNotFoundException e) {
			log.warn("Service Provider Class: " + providerClassName + " not found!");

			return new EmptyServiceProvider();
		}	
		

	}

	/**
	 * Load service provider class.
	 * 
	 * @param name
	 *            the name
	 * @param classLoader
	 *            the class loader
	 * @return the service provider
	 * @throws ClassNotFoundException
	 *             the class not found exception
	 */
	private ServiceProvider loadServiceProviderClass(String name,
			ClassLoader classLoader) throws ClassNotFoundException {
		ServiceProvider serviceProvider;
		try {
			serviceProvider = (ServiceProvider) classLoader.loadClass(name)
					.newInstance();
		} catch (InstantiationException e) {
			throw new IllegalStateException(e);
		} catch (IllegalAccessException e) {
			throw new IllegalStateException(e);
		}

		return serviceProvider;
	}
}
