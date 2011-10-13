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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.ReflectionUtils;

import edu.mayo.cts2.framework.core.config.ServiceConfigManager;
import edu.mayo.cts2.framework.core.constants.URIHelperInterface;
import edu.mayo.cts2.framework.model.core.AbstractResourceDescription;
import edu.mayo.cts2.framework.model.core.Directory;
import edu.mayo.cts2.framework.model.core.Message;
import edu.mayo.cts2.framework.model.core.Parameter;
import edu.mayo.cts2.framework.model.core.RESTResource;
import edu.mayo.cts2.framework.model.core.types.CompleteDirectory;
import edu.mayo.cts2.framework.model.directory.DirectoryResult;
import edu.mayo.cts2.framework.model.exception.UnspecifiedCts2RuntimeException;
import edu.mayo.cts2.framework.service.command.Page;
import edu.mayo.cts2.framework.webapp.rest.controller.UrlBinder.UrlVariableNotBoundException;

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
	 * @param <T>
	 *            the generic type
	 * @param message
	 *            the message
	 * @param httpServletRequest
	 *            the http servlet request
	 * @return the t
	 */
	protected <T extends Message> T wrapMessage(T message,
			HttpServletRequest httpServletRequest) {

		RESTResource heading = this
				.getHeadingForNameRequest(httpServletRequest);

		message.setHeading(heading);

		return message;
	}

	protected <T extends Message, R extends AbstractResourceDescription> T wrapMessage(T message,
			String urlTemplate,
			UrlBinder<R> binder,
			R resource,
			HttpServletRequest httpServletRequest) {
		
		String resourceUrl = this.bindResourceToUrlTemplate(binder, resource, urlTemplate);

		RESTResource heading = this
				.getHeadingWithKnownUrlRequest(httpServletRequest, resourceUrl);

		message.setHeading(heading);

		return message;
	}

	/**
	 * Populate directory.
	 * 
	 * @param <T>
	 *            the generic type
	 * @param result
	 *            the result
	 * @param page
	 *            the page
	 * @param httpServletRequest
	 *            the http servlet request
	 * @param directoryClazz
	 *            the directory clazz
	 * @return the t
	 */
	@SuppressWarnings("unchecked")
	protected <T extends Directory> T populateDirectory(
			DirectoryResult<?> result, Page page,
			HttpServletRequest httpServletRequest, Class<T> directoryClazz) {

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

		String urlRoot = this.getServiceConfigManager().getServerContext()
				.getServerRootWithAppName();

		if (!urlRoot.endsWith("/")) {
			urlRoot = urlRoot + "/";
		}

		String url = urlRoot
				+ StringUtils.removeStart(httpServletRequest.getServletPath(),
						"/");

		if (isComplete) {
			directory.setComplete(CompleteDirectory.COMPLETE);
		} else {
			directory.setComplete(CompleteDirectory.PARTIAL);

			if (!result.isAtEnd()) {
				directory.setNext(url
						+ getParametersString(
								httpServletRequest.getParameterMap(),
								page.getPage() + 1, page.getMaxtoreturn()));
			}

			if (page.getPage() > 0) {
				directory.setPrev(url
						+ getParametersString(
								httpServletRequest.getParameterMap(),
								page.getPage() - 1, page.getMaxtoreturn()));
			}
		}

		directory.setNumEntries((long) result.getEntries().size());

		return this.wrapMessage(directory, httpServletRequest);
	}

	@SuppressWarnings("unchecked")
	private RESTResource getHeadingForNameRequest(HttpServletRequest request) {

		return this.getHeading(request.getParameterMap(),
				request.getServletPath());

	}

	@SuppressWarnings("unchecked")
	private RESTResource getHeadingWithKnownUrlRequest(HttpServletRequest request,
			String resourceUrl) {

		return this.getHeading(request.getParameterMap(), resourceUrl);

	}
	
	protected <R extends AbstractResourceDescription> String bindResourceToUrlTemplate(
			UrlBinder<R> binder, 
			R resource,  
			String urlTemplate){
		Set<String> variables = this.getUrlTemplateVariables(urlTemplate);
		
		String[] matchArray = new String[variables.size()];
		String[] valuesArray = new String[variables.size()];
		
		{//scope limit
			int i=0;
			for(Iterator<String> itr = variables.iterator(); itr.hasNext(); i++){
				String variable = itr.next();
				String value;
				try {
					value = binder.getValueForPathAttribute(variable, resource);
				} catch (UrlVariableNotBoundException e) {
					throw new UnspecifiedCts2RuntimeException(e);
				}
				
				valuesArray[i] = value;			
				matchArray[i] = '{' + variable + '}';
			}
		}//end scope limit
		
		return StringUtils.replaceEach(urlTemplate, matchArray, valuesArray);
	}
	
	protected Set<String> getUrlTemplateVariables(String urlTemplate){
		Set<String> pathParamNames = new HashSet<String>();

		char[] chars = urlTemplate.toCharArray();
		
		for(int i=0;i<chars.length;i++){
			char c = chars[i];
			
			if(c == '{'){
				 String pathParam = "";
				 
				 while(chars[++i] != '}'){
					 pathParam += chars[i];
				 }
				 
				 pathParamNames.add(pathParam);	 
			}
		}
		
		return pathParamNames;
	}

	private RESTResource getHeading(Map<Object, Object> parameterMap,
			String resourceUrl) {
		RESTResource resource = new RESTResource();

		for (Entry<Object, Object> param : parameterMap.entrySet()) {
			Parameter headingParam = new Parameter();
			headingParam.setArg(param.getKey().toString());
			headingParam.setVal(getParamValue(param.getValue()));

			resource.addParameter(headingParam);
		}

		resource.setAccessDate(new Date());

		String urlRoot = this.getServiceConfigManager().getServerContext()
				.getServerRootWithAppName();

		if (!urlRoot.endsWith("/")) {
			urlRoot = urlRoot + "/";
		}

		String resourceRoot = StringUtils.removeStart(resourceUrl, "/");

		String resourceURI = urlRoot + resourceRoot;

		resource.setResourceURI(resourceURI);

		resource.setResourceRoot(resourceRoot);

		return resource;
	}

	/**
	 * Gets the parameters string.
	 * 
	 * @param parameters
	 *            the parameters
	 * @param page
	 *            the page
	 * @param pageSize
	 *            the page size
	 * @return the parameters string
	 */
	private String getParametersString(Map<String, Object> parameters,
			int page, int pageSize) {
		StringBuffer sb = new StringBuffer();
		sb.append("?");

		parameters = new HashMap<String, Object>(parameters);

		parameters.put(URIHelperInterface.PARAM_PAGE, Integer.toString(page));

		parameters.put(URIHelperInterface.PARAM_MAXTORETURN,
				Integer.toString(pageSize));

		for (Entry<String, Object> entry : parameters.entrySet()) {
			if (entry.getValue().getClass().isArray()) {

				for (Object val : (Object[]) entry.getValue()) {
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
	 * @param param
	 *            the param
	 * @return the string
	 */
	private String parameterValueToString(Object param) {
		String paramString;

		if (param.getClass().isArray()) {
			paramString = ArrayUtils.toString(param);
		} else {
			paramString = param.toString().trim();
		}

		if (paramString.startsWith("{")) {
			paramString = paramString.substring(1);
		}

		if (paramString.endsWith("}")) {
			paramString = paramString.substring(0, paramString.length() - 1);
		}

		return paramString;
	}

	/**
	 * Gets the param value.
	 * 
	 * @param value
	 *            the value
	 * @return the param value
	 */
	private String getParamValue(Object value) {
		if (value == null) {
			return null;
		}

		return parameterValueToString(value);
	}

	protected ServiceConfigManager getServiceConfigManager() {
		return serviceConfigManager;
	}

	protected void setServiceConfigManager(
			ServiceConfigManager serviceConfigManager) {
		this.serviceConfigManager = serviceConfigManager;
	}
}
