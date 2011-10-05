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
package edu.mayo.cts2.framework.webapp.rest.controller;

import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.ReflectionUtils;

import edu.mayo.cts2.framework.core.config.ServiceConfigManager;
import edu.mayo.cts2.framework.core.constants.URIHelperInterface;
import edu.mayo.cts2.framework.model.core.Directory;
import edu.mayo.cts2.framework.model.core.Message;
import edu.mayo.cts2.framework.model.core.Parameter;
import edu.mayo.cts2.framework.model.core.RESTResource;
import edu.mayo.cts2.framework.model.core.types.CompleteDirectory;
import edu.mayo.cts2.framework.model.directory.DirectoryResult;
import edu.mayo.cts2.framework.service.command.Page;

/**
 * The Class AbstractMessageWrappingController.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractMessageWrappingController extends
		AbstractController {
	
	@Resource
	private ServiceConfigManager serviceConfigManager;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.cts2.web.rest.controller.AbstractMessageWrappingController.MethodWrapper
	 * #wrapMessage(java.lang.Object, javax.servlet.http.HttpServletRequest)
	 */
	/**
	 * Wrap message.
	 *
	 * @param <T> the generic type
	 * @param message the message
	 * @param httpServletRequest the http servlet request
	 * @return the t
	 */
	protected <T extends Message> T wrapMessage(T message,
			HttpServletRequest httpServletRequest) {

		message.setHeading(this.getHeading(httpServletRequest));

		return message;
	}

	/**
	 * Populate directory.
	 *
	 * @param <T> the generic type
	 * @param result the result
	 * @param page the page
	 * @param httpServletRequest the http servlet request
	 * @param directoryClazz the directory clazz
	 * @return the t
	 */
	@SuppressWarnings("unchecked")
	protected <T extends Directory> T populateDirectory(
			DirectoryResult<?> result, 
			Page page,
			HttpServletRequest httpServletRequest, 
			Class<T> directoryClazz) {

		boolean isComplete = result.isComplete();

		T directory;
		try {
			directory = directoryClazz.newInstance();

			final Field field = ReflectionUtils.findField(directoryClazz,
					"_entryList");
			
			AccessController.doPrivileged(new PrivilegedAction<Void>() {
	            public Void run() {
	            	field.setAccessible(true);
	            	
	            	return null;
	            }
	        });

			ReflectionUtils.setField(field, directory, result.getEntries());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		String urlRoot = 
			this.serviceConfigManager.getServerContext().getServerRootWithAppName();
		
		if(! urlRoot.endsWith("/")){
			urlRoot = urlRoot + "/";
		}
			
		String url = urlRoot
				+ StringUtils.removeStart(httpServletRequest.getServletPath(), "/");
		
		if (isComplete) {
			directory.setComplete(CompleteDirectory.COMPLETE);
		} else {
			directory.setComplete(CompleteDirectory.PARTIAL);

			if (!result.isAtEnd()) {
				directory.setNext(url + getParametersString(
						httpServletRequest.getParameterMap(), 
						page.getPage() + 1, page.getMaxtoreturn()));
			}

			if (page.getPage() > 0) {
				directory.setPrev(url + getParametersString(
						httpServletRequest.getParameterMap(), 
						page.getPage() - 1, page.getMaxtoreturn()));
			}
		}
		
		directory.setNumEntries((long)
				result.getEntries().size());

		return this.wrapMessage(directory, httpServletRequest);
	}

	/**
	 * Gets the heading.
	 *
	 * @param httpServletRequest the http servlet request
	 * @return the heading
	 */
	protected RESTResource getHeading(HttpServletRequest httpServletRequest) {
		RESTResource resource = new RESTResource();

		@SuppressWarnings("unchecked")
		Map<Object, Object> parameterMap = httpServletRequest.getParameterMap();
		for (Entry<Object,Object> param : parameterMap.entrySet()) {
			Parameter headingParam = new Parameter();
			headingParam.setArg(param.getKey().toString());
			headingParam.setVal(getParamValue(param.getValue()));

			resource.addParameter(headingParam);
		}

		resource.setAccessDate(new Date());

		String urlRoot = 
				this.serviceConfigManager.getServerContext().getServerRootWithAppName();
		
		if(! urlRoot.endsWith("/")){
			urlRoot = urlRoot + "/";
		}
		
		String resourceRoot = StringUtils.removeStart(
					httpServletRequest.getServletPath(), "/");

		
		String resourceURI = urlRoot + resourceRoot;
		
		resource.setResourceURI(resourceURI);
		
		resource.setResourceRoot(resourceRoot);

		return resource;
	}
	
	/**
	 * Gets the parameters string.
	 *
	 * @param parameters the parameters
	 * @param page the page
	 * @param pageSize the page size
	 * @return the parameters string
	 */
	private String getParametersString(Map<String,Object> parameters, int page, int pageSize){
		StringBuffer sb = new StringBuffer();
		sb.append("?");
		
		parameters = new HashMap<String,Object>(parameters);

		parameters.put(URIHelperInterface.PARAM_PAGE, Integer.toString(page));
	
		parameters.put(URIHelperInterface.PARAM_MAXTORETURN, Integer.toString(pageSize));
		
		for(Entry<String, Object> entry : parameters.entrySet()){
			if(entry.getValue().getClass().isArray()){

				for(Object val : (Object[])entry.getValue()){
					sb.append(entry.getKey() + "=" + val);
					sb.append("&");
				}
			
			} else {
				sb.append(entry.getKey() + "=" + entry.getValue());
				sb.append("&");
			}
		}
		
		return StringUtils.removeEnd(sb.toString(), "&");
	}

	/**
	 * Parameter value to string.
	 *
	 * @param param the param
	 * @return the string
	 */
	private String parameterValueToString(Object param) {
		String paramString;
		
		if(param.getClass().isArray()){
			paramString =  ArrayUtils.toString(param);
		} else {
			paramString =  param.toString().trim();
		}
		
		if(paramString.startsWith("{")){
			paramString = paramString.substring(1);
		}
		
		if(paramString.endsWith("}")){
			paramString = paramString.substring(0, paramString.length() - 1);
		}
		
		return paramString;
	}

	/**
	 * Gets the param value.
	 *
	 * @param value the value
	 * @return the param value
	 */
	private String getParamValue(Object value) {
		if (value == null) {
			return null;
		}

		return parameterValueToString(value);
	}
}
