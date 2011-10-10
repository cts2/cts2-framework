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
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.core.config.ConfigChangeObserver;
import edu.mayo.cts2.framework.core.config.PluginConfig;
import edu.mayo.cts2.framework.core.config.PluginManager;
import edu.mayo.cts2.framework.core.config.PluginReference;
import edu.mayo.cts2.framework.core.config.ServiceConfigManager;
import edu.mayo.cts2.framework.core.config.option.OptionHolder;
import edu.mayo.cts2.framework.service.profile.Cts2Profile;

/**
 * A factory for creating ServiceProvider objects.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Component
public class ServiceProviderFactory implements InitializingBean,
		ConfigChangeObserver, ServiceProviderChangeObservable {

	private final Log log = LogFactory.getLog(getClass().getName());

	@Resource
	private PluginManager pluginManager;
	
	@Resource
	private ServiceConfigManager serviceConfigManager;

	private ServiceProvider serviceProvider;

	private Set<ServiceProviderChangeObserver> observers = new HashSet<ServiceProviderChangeObserver>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		this.serviceProvider = this.createServiceProvider();
		this.serviceConfigManager.registerListener(this);
	}

	/**
	 * Instantiates a new service provider factory.
	 */
	public ServiceProviderFactory() {
		super();
	}

	public ServiceProvider getServiceProvider() {
		synchronized(this.serviceProvider){
			return this.serviceProvider;
		}
	}

	private void refresh() {
		synchronized(this.serviceProvider){
			this.serviceProvider = this.createServiceProvider();
		}
	}

	/**
	 * Creates a new ServiceProvider object.
	 * 
	 * @return the service provider
	 */
	protected ServiceProvider createServiceProvider() {
		
		PluginReference activePlugin = pluginManager.getActivePlugin();

		if (activePlugin == null) {
			log.warn("No Service Plugin declared.");

			return new EmptyServiceProvider();
		}

		String providerClassName = pluginManager
				.getPluginServiceProviderClassName(
						activePlugin.getPluginName(), 
						activePlugin.getPluginVersion());

		final ClassLoader pluginClassLoader = 
				pluginManager.getPluginClassLoader(
						activePlugin.getPluginName(), 
						activePlugin.getPluginVersion());
		
		try {
			final ServiceProvider provider = this.loadServiceProviderClass(
					providerClassName, pluginClassLoader);

			this.serviceProvider = this.decorateServiceProvider(provider, pluginClassLoader);
			
			serviceProvider.initialize(
					this.pluginManager.getPluginConfig());
			
			return serviceProvider;

		} catch (ClassNotFoundException e) {
			log.warn("Service Provider Class: " + providerClassName
					+ " not found!");

			return new EmptyServiceProvider();
		}
	}
	
	private RuntimeException handleException(ExecutionException executionException)  {
		Throwable e = executionException.getCause();
		
		if(e instanceof RuntimeException){
			return (RuntimeException) e;
		} else {
			return new RuntimeException(e);
		}
	}
	
	protected ExecutorService createExecutorService(final ClassLoader pluginClassLoader){
		return Executors.newFixedThreadPool(1,
				new ThreadFactory() {

					public Thread newThread(Runnable runnable) {
						Thread t = new Thread(runnable);
						t.setContextClassLoader(pluginClassLoader);

						return t;
					}

				});
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
	
	protected ServiceProvider decorateServiceProvider(final ServiceProvider delegate, ClassLoader pluginClassLoader){
		final ExecutorService ex = this.createExecutorService(pluginClassLoader);
		
		ServiceProvider serviceProvider = new ServiceProvider() {

			public <T extends Cts2Profile> T getService(
					final Class<T> serviceClass) {
				try {
					return ex.submit(new Callable<T>() {

						public T call() throws Exception {
							return delegate.getService(serviceClass);
						}

					}).get();
				} catch (ExecutionException e) {
					throw handleException(e);
				} catch (InterruptedException e) {
					throw new IllegalStateException(e);
				}
			}
			
			public void initialize(
					final PluginConfig pluginConfig) {
				try {
					ex.submit(new Callable<Void>() {

						public Void call() throws Exception {
							delegate.initialize(pluginConfig);
							
							return null;
						}

					}).get();
				} catch (ExecutionException e) {
					throw handleException(e);
				} catch (InterruptedException e) {
					throw new IllegalStateException(e);
				}
			}
			
			public void destroy() {
				try {
					ex.submit(new Callable<Void>() {

						public Void call() throws Exception {
							delegate.destroy();
							
							return null;
						}

					}).get();
				} catch (ExecutionException e) {
					throw handleException(e);
				} catch (InterruptedException e) {
					throw new IllegalStateException(e);
				}
			}

		};
		
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
	public void onPluginRemoved(PluginReference ref) {
		if(this.pluginManager.isActivePlugin(ref)){
			this.refresh();
			this.fireServiceProviderChangeEvent();
		}
	}

	@Override
	public void onPluginAdded(PluginReference ref) {
		//no-op
	}

	@Override
	public void onContextConfigPropertiesChange() {
		this.refresh();
		this.fireServiceProviderChangeEvent();
	}

	@Override
	public void onGlobalConfigPropertiesChange(OptionHolder newOptions) {
		this.refresh();
		this.fireServiceProviderChangeEvent();
	}

	@Override
	public void onContextConfigPropertiesChange(OptionHolder newOptions) {
		this.refresh();
		this.fireServiceProviderChangeEvent();
	}

	@Override
	public void onPluginSpecificConfigPropertiesChange(OptionHolder newOptions) {
		this.refresh();
		this.fireServiceProviderChangeEvent();
	}
}
