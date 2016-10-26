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
package edu.mayo.cts2.framework.webapp.service;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.annotation.Resource;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.proxy.Invoker;
import org.apache.commons.proxy.ProxyFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.MethodInvoker;

import edu.mayo.cts2.framework.service.profile.Cts2Profile;
import edu.mayo.cts2.framework.service.provider.ServiceProvider;
import edu.mayo.cts2.framework.service.provider.ServiceProviderChangeObserver;
import edu.mayo.cts2.framework.service.provider.ServiceProviderFactory;

/**
 * The Class AbstractServiceAwareController.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class AbstractServiceAwareBean implements InitializingBean, ServiceProviderChangeObserver {

	private static Log log = LogFactory
			.getLog(AbstractServiceAwareBean.class);

	@Resource
	private ServiceProviderFactory serviceProviderFactory;

	/**
	 * The Interface Cts2Service.
	 * 
	 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
	 */
	@Target({ ElementType.FIELD })
	@Retention(RetentionPolicy.RUNTIME)
	public @interface Cts2Service {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public final void afterPropertiesSet() throws Exception {
		this.serviceProviderFactory.registerListener(this);

		this.loadServices();
		
		this.doInitialize();
	}
	
	protected void doInitialize(){
		//no-op
	}
	
	protected <T extends Cts2Profile> T getCts2Service(Class<T> clazz){
		ServiceProvider provider = this.serviceProviderFactory.getServiceProvider();
		
		return provider.getService(clazz);
	}

	/**
	 * Load services.
	 */
	protected void loadServices() {
		
				for (Field field : this.getClass().getDeclaredFields()) {
					if (field.isAnnotationPresent(Cts2Service.class)) {

						@SuppressWarnings("unchecked")
						final Class<? extends Cts2Profile> clazz = (Class<? extends Cts2Profile>) field
								.getType();
						ProxyFactory factory = new ProxyFactory();
						Cts2Profile service = (Cts2Profile)factory.createInvokerProxy( new Invoker(){

							@Override
							public Object invoke(Object o, Method method, Object[] olist) throws Throwable {
								ServiceProvider retrievedServiceProvider = 
										serviceProviderFactory.getServiceProvider();

								Cts2Profile retrievedService;
								if (retrievedServiceProvider == null) {
									throw new UnsupportedOperationException("This service is not implemented.");
								} else {
									retrievedService = retrievedServiceProvider.getService(clazz);
									
									if(retrievedService == null){
										throw new UnsupportedOperationException("This service is not implemented.");
									}
								}

								try {
									return method
											.invoke(retrievedService,
													(Object[])method.getParameters());
								} catch (InvocationTargetException e) {
									throw e.getCause();
								}

							}
							
						}, new Class<?>[]{clazz});
						
						if(service == null){
							service = proxyNullService(clazz);
						}

						field.setAccessible(true);

						try {
							field.set(this, service);
						} catch (Exception e) {
							throw new IllegalStateException(e);
						}

						log.info("Setting service: " + field.getType()
								+ " on: " + this.getClass().getName());
					}
				}
			}
	
	@SuppressWarnings("unchecked")
	protected <T extends Cts2Profile> T proxyNullService(Class<T> serviceClass){
		
		ProxyFactory factory =
				new ProxyFactory();
		
		return (T) factory.createInvokerProxy( nullServiceMethodInterceptor, new Class<?>[]{serviceClass});
	}
	
	protected Invoker nullServiceMethodInterceptor = new  Invoker() {

		@Override
		public Object invoke(Object arg0, Method arg1, Object[] arg2) throws Throwable {
			throw new UnsupportedOperationException("This method is not yet supported");
		}
		
	};

	@Override
	public void onServiceProviderChange() {
		this.loadServices();
	}

	protected ServiceProviderFactory getServiceProviderFactory() {
		return serviceProviderFactory;
	}

	protected void setServiceProviderFactory(
			ServiceProviderFactory serviceProviderFactory) {
		this.serviceProviderFactory = serviceProviderFactory;
	}
	
}
