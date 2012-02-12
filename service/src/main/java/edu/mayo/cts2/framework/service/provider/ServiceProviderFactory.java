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

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.util.tracker.ServiceTrackerCustomizer;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.core.config.option.OptionHolder;
import edu.mayo.cts2.framework.core.plugin.AbstractExtensionPoint;
import edu.mayo.cts2.framework.core.plugin.ExtensionPoint;
import edu.mayo.cts2.framework.core.plugin.PluginConfigChangeObserver;
import edu.mayo.cts2.framework.core.plugin.PluginManager;
import edu.mayo.cts2.framework.core.plugin.PluginReference;

/**
 * A factory for creating ServiceProvider objects.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Component
public class ServiceProviderFactory extends AbstractExtensionPoint<ServiceProvider> 
	implements PluginConfigChangeObserver, 
	ServiceProviderChangeObservable, 
	ExtensionPoint,
	ApplicationContextAware {
	
	public static final String USE_CLASSPATH_PROVIDER_PROP = "useClasspathProvider";
	
	protected Log log = LogFactory.getLog(getClass());

	@Resource
	private PluginManager pluginManager;
	
	private ServiceProvider classPathServiceProvider;

	private Set<ServiceProviderChangeObserver> observers = new HashSet<ServiceProviderChangeObserver>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		this.pluginManager.registerExtensionPoint(this);
	}

	/**
	 * Instantiates a new service provider factory.
	 */
	public ServiceProviderFactory() {
		super();
	}

	public ServiceProvider getServiceProvider() {
		ServiceProvider serviceProvider;
		
		if(this.classPathServiceProvider != null){
			serviceProvider = this.classPathServiceProvider;
		} else {
			serviceProvider = (ServiceProvider)
				this.getServiceTracker().getService();
		}
		
		return serviceProvider;
	}

	private void fireServiceProviderChangeEvent() {
		for (ServiceProviderChangeObserver observer : this.observers) {
			observer.onServiceProviderChange();
		}
	}

	public void registerListener(ServiceProviderChangeObserver observer) {
		this.observers.add(observer);
	}

	public void unregisterListener(ServiceProviderChangeObserver observer) {
		this.observers.remove(observer);
	}
	
	@Override
	public void onPluginActivated(PluginReference ref) {
	
		this.fireServiceProviderChangeEvent();
	}

	@Override
	public void onPluginRemoved(PluginReference ref) {
		//
	}

	@Override
	public void onPluginAdded(PluginReference ref) {
		//no-op
	}

	@Override
	public void onPluginSpecificConfigPropertiesChange(OptionHolder newOptions) {

		this.fireServiceProviderChangeEvent();
	}

	@Override
	public Class<?> getServiceClass() {
		return ServiceProvider.class;
	}

	@Override
	public ServiceTrackerCustomizer addServiceTrackerCustomizer() {
		return null;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
		if(BooleanUtils.toBoolean(System.getProperty(USE_CLASSPATH_PROVIDER_PROP))){
			ServiceProvider classpathServiceProvider = null;
			try {
				classpathServiceProvider = 
						applicationContext.getBean(ServiceProvider.class);
				
				log.warn("NOTICE: Found a ServiceProvider on the Classpath: " + classpathServiceProvider +
						". This Service Provider will be set as active cannot be disabled.");
			} catch (NoSuchBeanDefinitionException  e) {
				log.info("No ServiceProvider found on the Classpath... waiting for a Service Plugin.");
			}
			
			this.classPathServiceProvider = classpathServiceProvider;
		}
	}

}
