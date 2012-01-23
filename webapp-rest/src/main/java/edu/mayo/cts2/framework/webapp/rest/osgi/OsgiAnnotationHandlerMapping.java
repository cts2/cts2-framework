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
package edu.mayo.cts2.framework.webapp.rest.osgi;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.handler.AbstractUrlHandlerMapping;
import org.springframework.web.servlet.mvc.annotation.DefaultAnnotationHandlerMapping;

import edu.mayo.cts2.framework.core.plugin.ExtensionPoint;
import edu.mayo.cts2.framework.core.plugin.PluginManager;
import edu.mayo.cts2.framework.webapp.rest.extensions.controller.ControllerProvider;

public class OsgiAnnotationHandlerMapping extends DefaultAnnotationHandlerMapping 
	implements ExtensionPoint, InitializingBean {
	
	private final Map<Class<?>, RequestMapping> cachedMappings = new HashMap<Class<?>, RequestMapping>();
	
	@Resource
	private PluginManager pluginManager;

	protected String[] determineUrlsForOsgiService(Object osgiController) {

		Class<?> handlerType = osgiController.getClass();
		RequestMapping mapping = osgiController.getClass().getAnnotation(RequestMapping.class);
		if (mapping != null) {
			// @RequestMapping found at type level
			this.cachedMappings.put(handlerType, mapping);
			Set<String> urls = new LinkedHashSet<String>();
			String[] typeLevelPatterns = mapping.value();
			if (typeLevelPatterns.length > 0) {
				// @RequestMapping specifies paths at type level
				String[] methodLevelPatterns = determineUrlsForHandlerMethods(handlerType, true);
				for (String typeLevelPattern : typeLevelPatterns) {
					if (!typeLevelPattern.startsWith("/")) {
						typeLevelPattern = "/" + typeLevelPattern;
					}
					boolean hasEmptyMethodLevelMappings = false;
					for (String methodLevelPattern : methodLevelPatterns) {
						if (methodLevelPattern == null) {
							hasEmptyMethodLevelMappings = true;
						}
						else {
							String combinedPattern = getPathMatcher().combine(typeLevelPattern, methodLevelPattern);
							addUrlsForPath(urls, combinedPattern);
						}
					}
					if (hasEmptyMethodLevelMappings ||
							org.springframework.web.servlet.mvc.Controller.class.isAssignableFrom(handlerType)) {
						addUrlsForPath(urls, typeLevelPattern);
					}
				}
				return StringUtils.toStringArray(urls);
			}
			else {
				// actual paths specified by @RequestMapping at method level
				return determineUrlsForHandlerMethods(handlerType, false);
			}
		}
		else if (AnnotationUtils.findAnnotation(handlerType, Controller.class) != null) {
			// @RequestMapping to be introspected at method level
			return determineUrlsForHandlerMethods(handlerType, false);
		}
		else {
			return null;
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		this.pluginManager.registerExtensionPoint(this);
	}

	@Override
	public Class<?> getServiceClass() {
		return ControllerProvider.class;
	}

	@Override
	public void setServiceTracker(ServiceTracker serviceTracker) {
		//
	}

	@Override
	public ServiceTrackerCustomizer addServiceTrackerCustomizer() {
		return new ServiceTrackerCustomizer(){

			/* (non-Javadoc)
			 * @see org.osgi.util.tracker.ServiceTrackerCustomizer#addingService(org.osgi.framework.ServiceReference)
			 */
			@Override
			public Object addingService(ServiceReference reference) {
				Object service = 
						reference.getBundle().getBundleContext().getService(reference);
				
				String[] urls = OsgiAnnotationHandlerMapping.this.determineUrlsForOsgiService(service);
				
				for(String url : urls){
					OsgiAnnotationHandlerMapping.this.registerHandler(url, service);
				}
				
				return service;
			}

			@Override
			public void modifiedService(ServiceReference reference,
					Object service) {
				//
			}

			@Override
			public void removedService(ServiceReference reference,
					Object service) {	
				OsgiAnnotationHandlerMapping.this.clearHandlerMappings();
				OsgiAnnotationHandlerMapping.this.detectHandlers();
			}
			
		};
	}
	
	/**
	 * Clear all handler mappings. Spring made the 'handlerMap' unmodifiable,
	 * so we must resort to reflection.
	 */
	protected void clearHandlerMappings(){
		Field field;
		try {
			field = AbstractUrlHandlerMapping.class.getDeclaredField("handlerMap");
			field.setAccessible(true);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		ReflectionUtils.setField(
				field, 
				OsgiAnnotationHandlerMapping.this, 
				new LinkedHashMap<String, Object>());
		
	}
}
