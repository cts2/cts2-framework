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
package edu.mayo.cts2.framework.util.spring;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import edu.mayo.cts2.framework.service.profile.Cts2Profile;
import edu.mayo.cts2.framework.service.provider.ServiceProvider;

/**
 * The Class AbstractSpringServiceProvider.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractSpringServiceProvider implements ServiceProvider, ApplicationContextAware {
		
	private final Log log = LogFactory.getLog(getClass().getName());
	
	private ApplicationContext applicationContext;

	/* (non-Javadoc)
	 * @see edu.mayo.cts2.framework.service.provider.ServiceProvider#getService(java.lang.Class)
	 */
	public <T extends Cts2Profile> T getService(Class<T> serviceClass) {

		T bean = null;
		try {
			Map<String, T> beans = applicationContext.getBeansOfType(serviceClass);
			if(beans != null && !beans.isEmpty()){
				if(beans.size() == 1){
					bean = beans.values().iterator().next();
				} else {
					List<T> candidates = new ArrayList<T>();
					
					for(T service : beans.values()){
						if(service.getClass().isAnnotationPresent(AggregateService.class)){
							candidates.add(service);
						}
					}
					
					if(candidates.size() != 1){
						throw new RuntimeException(
								"Cannot have more than one Service Implementation without" +
								" annotating one as an 'AggreateService.'");
					} else {
						bean = candidates.get(0);
					}
				}
			}
			
		} catch (BeansException e) {
			log.warn(e);
		}
		
		return bean;
		
	}
	
	protected ApplicationContext decorateApplicationContext(ApplicationContext context) {
		return context;
	}
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = this.decorateApplicationContext(applicationContext);
	}
}
