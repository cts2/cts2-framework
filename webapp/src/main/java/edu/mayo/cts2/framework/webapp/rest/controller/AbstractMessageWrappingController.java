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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.servlet.ModelAndView;

import edu.mayo.cts2.framework.core.config.ServerContext;
import edu.mayo.cts2.framework.core.constants.URIHelperInterface;
import edu.mayo.cts2.framework.model.command.Page;
import edu.mayo.cts2.framework.model.command.ResolvedReadContext;
import edu.mayo.cts2.framework.model.core.Directory;
import edu.mayo.cts2.framework.model.core.IsChangeable;
import edu.mayo.cts2.framework.model.core.Message;
import edu.mayo.cts2.framework.model.core.Parameter;
import edu.mayo.cts2.framework.model.core.PropertyReference;
import edu.mayo.cts2.framework.model.core.RESTResource;
import edu.mayo.cts2.framework.model.core.SortCriteria;
import edu.mayo.cts2.framework.model.core.SortCriterion;
import edu.mayo.cts2.framework.model.core.types.CompleteDirectory;
import edu.mayo.cts2.framework.model.core.types.SortDirection;
import edu.mayo.cts2.framework.model.directory.DirectoryResult;
import edu.mayo.cts2.framework.model.exception.ExceptionFactory;
import edu.mayo.cts2.framework.model.service.exception.UnknownResourceReference;
import edu.mayo.cts2.framework.model.service.exception.types.ExceptionType;
import edu.mayo.cts2.framework.service.profile.BaseMaintenanceService;
import edu.mayo.cts2.framework.service.profile.BaseQueryService;
import edu.mayo.cts2.framework.service.profile.QueryService;
import edu.mayo.cts2.framework.service.profile.ReadService;
import edu.mayo.cts2.framework.service.profile.ResourceQuery;
import edu.mayo.cts2.framework.webapp.rest.command.QueryControl;
import edu.mayo.cts2.framework.webapp.rest.command.RestReadContext;
import edu.mayo.cts2.framework.webapp.rest.exception.StatusSettingCts2RestException;
import edu.mayo.cts2.framework.webapp.rest.resolver.FilterResolver;
import edu.mayo.cts2.framework.webapp.rest.resolver.ReadContextResolver;
import edu.mayo.cts2.framework.webapp.rest.util.ControllerUtils;

/**
 * The Class AbstractMessageWrappingController.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractMessageWrappingController extends
		AbstractController {

	@Resource
	private UrlTemplateBindingCreator urlTemplateBindingCreator;

	@Resource
	private CreateHandler createHandler;
	
	@Resource
	private UpdateHandler updateHandler;
	
	@Resource
	private DeleteHandler deleteHandler;
	
	@Resource 
	private FilterResolver filterResolver;
	
	@Resource
	private ServerContext serverContext;
	
	@Resource 
	private ReadContextResolver readContextResolver;
	
	private static String BEANS_VIEW = "beans";
	private static String BEANS_MODEL_OBJECT = "bean";
	private static String IS_DIRECTORY_MODEL_OBJECT = "isDirectory";
	private static String URLBASE_MODEL_OBJECT = "urlBase";

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

	protected <T extends Message, R> T wrapMessage(T message,
			String urlTemplate,
			UrlTemplateBinder<R> binder,
			R resource,
			HttpServletRequest httpServletRequest) {
		
		String resourceUrl = this.urlTemplateBindingCreator.bindResourceToUrlTemplate(binder, resource, urlTemplate);

		RESTResource heading = this
				.getHeadingWithKnownUrlRequest(httpServletRequest, resourceUrl);

		message.setHeading(heading);

		return message;
	}
	
	private void setDirectoryEntries(Directory directory, List<?> entries){
		try {
			final Field field = ReflectionUtils.findField(directory.getClass(),
					"_entryList");

			AccessController.doPrivileged(new PrivilegedAction<Void>() {
				public Void run() {
					field.setAccessible(true);

					return null;
				}
			});

			ReflectionUtils.setField(field, directory, entries);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
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
			DirectoryResult<?> result, 
			Page page,
			HttpServletRequest httpServletRequest, 
			Class<T> directoryClazz) {
		
		T directory;
		
		try {
			directory = directoryClazz.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}	

		if(result == null){
			result = new DirectoryResult<Void>(new ArrayList<Void>(), true);
		}
		
		this.setDirectoryEntries(directory, result.getEntries());
		
		boolean atEnd = result.isAtEnd();
		boolean isComplete = atEnd && ( page.getPage() == 0 );
		
		String urlRoot = this.serverContext.getServerRootWithAppName();

		if (!urlRoot.endsWith("/")) {
			urlRoot = urlRoot + "/";
		}

		String pathInfo = httpServletRequest.getServletPath();
	
		String url = urlRoot
				+ StringUtils.removeStart(pathInfo,
						"/");

		if (isComplete) {
			directory.setComplete(CompleteDirectory.COMPLETE);
		} else {
			directory.setComplete(CompleteDirectory.PARTIAL);

			if (!result.isAtEnd()) {
				directory.setNext(url
						+ getParametersString(
								httpServletRequest.getParameterMap(),
								page.getPage() + 1, page.getMaxToReturn()));
			}

			if (page.getPage() > 0) {
				directory.setPrev(url
						+ getParametersString(
								httpServletRequest.getParameterMap(),
								page.getPage() - 1, page.getMaxToReturn()));
			}
		}

		directory.setNumEntries((long) result.getEntries().size());

		return this.wrapMessage(directory, httpServletRequest);
	}

	@SuppressWarnings("unchecked")
	protected RESTResource getHeadingForNameRequest(HttpServletRequest request) {

		return this.getHeading(request.getParameterMap(),
				this.getUrlPathHelper().getServletPath(request));

	}

	@SuppressWarnings("unchecked")
	protected RESTResource getHeadingWithKnownUrlRequest(HttpServletRequest request,
			String resourceUrl) {

		return this.getHeading(request.getParameterMap(), resourceUrl);

	}

	protected <R> ModelAndView forward(
			Message message,
			R resource, 	
			UrlTemplateBinder<R> urlBinder,
			String urlTemplate, 
			boolean redirect){
		ModelAndView mav;
		
		if(!redirect){
			mav = new ModelAndView(
				"forward:"+ UriResolutionController.FORWARDING_URL, 
				UriResolutionController.ATTRIBUTE_NAME, 
				message);
		} else {
			mav = new ModelAndView(
				"redirect:"+ this.urlTemplateBindingCreator.bindResourceToUrlTemplate(urlBinder, resource, urlTemplate));
		}
		
		return mav;
	}
	
	protected <R,I> Object doRead(
			HttpServletRequest httpServletRequest,
			MessageFactory<R> messageFactory,
			ReadService<R,I> readService, 
			RestReadContext restReadContext,
			Class<? extends UnknownResourceReference > exceptionClazz,
			I id) {
		
		ResolvedReadContext resolvedContext = this.resolveRestReadContext(restReadContext);

		R resource = readService.read(id, resolvedContext);
		
		if(resource == null){
			throw ExceptionFactory.createUnknownResourceException(id.toString(), exceptionClazz);
		}
		
		Message msg = messageFactory.createMessage(resource);
		
		msg = this.wrapMessage(msg, httpServletRequest);
		
		return this.buildResponse(httpServletRequest, msg);
	}
	
	protected Object buildResponse(HttpServletRequest request, Object bean){
		String acceptHeader = request.getHeader("Accept");
		
		List<MediaType> types = MediaType.parseMediaTypes(acceptHeader);
		
		if(CollectionUtils.isEmpty(types)){
			return new ResponseEntity<Object>(bean, HttpStatus.OK);
		}
		
		MediaType.sortByQualityValue(types);
		
		MediaType type = types.get(0);
		
		if(this.getRestConfig().getAllowHtmlRendering() && 
				type.isCompatibleWith(MediaType.TEXT_HTML)){
			ModelAndView mav = new ModelAndView(BEANS_VIEW);
			mav.addObject(BEANS_MODEL_OBJECT, bean);
			mav.addObject(URLBASE_MODEL_OBJECT, this.serverContext.getServerRootWithAppName());
			mav.addObject(IS_DIRECTORY_MODEL_OBJECT, bean instanceof Directory);
			return mav;
		} else {
			return new ResponseEntity<Object>(bean, HttpStatus.OK);
		}
	}
	
	protected <R,S,Q extends ResourceQuery> Object doQuery(
			HttpServletRequest httpServletRequest,
			boolean isList,
			QueryService<R,S,Q> queryService, 
			Q query,
			Page page,
			QueryControl queryControl,
			Class<? extends Directory> summaryDirectory,
			Class<? extends Directory> listDirectory) {
		
		DirectoryResult<?> result;
		Class<? extends Directory> directoryClass;
		
		SortCriteria sortCriteria = this.resolveSort(queryControl, queryService);
		
		if(isList){
			result = 
				queryService.getResourceList(query, sortCriteria, page);
			directoryClass = listDirectory;
		} else {
			result = 
				queryService.getResourceSummaries(query, sortCriteria, page);
			directoryClass = summaryDirectory;
		}
		
		Directory dir = 
			this.populateDirectory(result, page, httpServletRequest, directoryClass);
		
		return this.buildResponse(httpServletRequest, dir);
	}
	
	protected <I> Object doDelete(
			HttpServletResponse response,
			I identifier,
			String changeSetUri,
			BaseMaintenanceService<?,?,I> service) {
		
		this.deleteHandler.delete(identifier, changeSetUri, service);
		
		response.setStatus(HttpStatus.NO_CONTENT.value());
		
		//TODO: Add a ModelAndView return type
		return null;
	}
	
	protected <T extends IsChangeable,R extends IsChangeable,I> Object doUpdate(
			HttpServletResponse response,
			T resource, 
			String changeSetUri, 
			I identifier,
			BaseMaintenanceService<T,R,I> service) {
		
		this.updateHandler.update(
				resource, 
				changeSetUri, 
				identifier, 
				service);
		
		response.setStatus(HttpStatus.NO_CONTENT.value());
		
		//TODO: Add a ModelAndView return type
		return null;
	}
	
	protected <T extends IsChangeable,R extends IsChangeable> Object doCreate(
			HttpServletResponse response,
			R resource, 
			String changeSetUri, 
			String urlTemplate,
			UrlTemplateBinder<T> template,
			BaseMaintenanceService<T,R,?> service){
		
		T returnedResource = this.createHandler.create(
				resource,
				changeSetUri, 
				urlTemplate, 
				template, 
				service);
		
		String location = this.urlTemplateBindingCreator.bindResourceToUrlTemplate(
				template,
				returnedResource, 
				urlTemplate);
		
		if(StringUtils.isNotBlank(changeSetUri)){
			location = location + ("?" + URIHelperInterface.PARAM_CHANGESETCONTEXT + "=" +  changeSetUri);
		}
		
		this.setLocation(response, location);
		
		//TODO: Add a ModelAndView return type
		return null;
	}
	
	protected void setLocation(HttpServletResponse response, String location){
		location = StringUtils.removeStart(location, "/");
		
		response.setHeader("Location", location);
	}

	protected SortCriteria resolveSort(QueryControl sort, BaseQueryService queryService) {
		if(sort == null || StringUtils.isBlank(sort.getSort())){
			return null;
		}
		Set<? extends PropertyReference> predicates = queryService.getSupportedSortReferences();
		PropertyReference ref = 
				ControllerUtils.getPropertyReference(sort.getSort(), predicates);

		SortCriterion sortCriterion = new SortCriterion();
		sortCriterion.setSortDirection(this.getSortDirection(sort.getSortdirection()));
		sortCriterion.setSortElement(ref);
		
		SortCriteria sortCriteria = new SortCriteria();
		sortCriteria.addEntry(sortCriterion);
		
		return sortCriteria;
	}
	
	private SortDirection getSortDirection(String direction){
		if(StringUtils.isBlank(direction) || StringUtils.equals(direction, "descending")){
			return SortDirection.DESCENDING;
		}
		if(StringUtils.equals(direction, "ascending")){
			return SortDirection.ASCENDING;
		}
		
		throw new StatusSettingCts2RestException(
				"Invalid 'sortdirection' parameter.", 
				ExceptionType.INVALID_SERVICE_INPUT, 
				400);
	}

	protected ResolvedReadContext resolveRestReadContext(RestReadContext context){
		if(context == null){
			return null;
		}
		
		ResolvedReadContext resolvedContext = new ResolvedReadContext();
		resolvedContext.setChangeSetContextUri(context.getChangesetcontext());
		//TODO: Finish this method
		return resolvedContext;
	}
	
	protected <I> void doExists(
			HttpServletResponse httpServletResponse,
			ReadService<?,I> readService,
			Class<? extends UnknownResourceReference > exceptionClazz,
			I id) {
		
		//TODO: ReadContext
		boolean exists = readService.exists(id, null);
		
		this.handleExists(id.toString(), exceptionClazz, httpServletResponse, exists);
	}
	

	/**
	 * Handle exists.
	 *
	 * @param resourceName the resource name
	 * @param exceptionClass the exception class
	 * @param httpServletResponse the http servlet response
	 * @param exists the exists
	 */
	private void handleExists(String resourceIdAsString, 
			Class<? extends UnknownResourceReference> exceptionClass, 
			HttpServletResponse httpServletResponse,
			boolean exists){
		
		if(exists){
			httpServletResponse.setStatus(HttpServletResponse.SC_OK);
		} else {
			throw ExceptionFactory.createUnknownResourceException(
					resourceIdAsString, 
					exceptionClass);
		}
	}
	
	protected <R extends IsChangeable, I> ModelAndView doReadByUri(
			HttpServletRequest httpServletRequest,
			MessageFactory<R> messageFactory,
			String byUriTemplate,
			String byNameTemaplate,
			UrlTemplateBinder<R> urlBinder,
			ReadService<R,I> readService,
			RestReadContext restReadContext,
			Class<? extends UnknownResourceReference > exceptionClazz,
			I identifier,
			boolean redirect) {
		
		ResolvedReadContext resolvedContext = this.resolveRestReadContext(restReadContext);
		
		R resource = 
				readService.read(identifier, resolvedContext);
		
		return this.doForward(
				resource, 
				identifier.toString(), 
				httpServletRequest, 
				messageFactory, 
				byUriTemplate, 
				byNameTemaplate, 
				urlBinder, 
				restReadContext, 
				exceptionClazz, 
				redirect);
	}
	
	protected <R extends IsChangeable> ModelAndView doForward(
			R resource,
			String identifier,
			HttpServletRequest httpServletRequest,
			MessageFactory<R> messageFactory,
			String byUriTemplate,
			String byNameTemaplate,
			UrlTemplateBinder<R> urlBinder,
			RestReadContext restReadContext,
			Class<? extends UnknownResourceReference > exceptionClazz,
			boolean redirect) {
		
		if(resource == null){
			throw ExceptionFactory.createUnknownResourceException(
					identifier, 
					exceptionClazz);
		}
		
		if(! this.isPartialRedirect(httpServletRequest, byUriTemplate)){

			Message msg = messageFactory.createMessage(resource);

			msg = this.wrapMessage(msg, byNameTemaplate, urlBinder, resource, httpServletRequest);
			
			return this.forward(msg, resource, urlBinder, byNameTemaplate, redirect);
		} else {

			return this.forward(httpServletRequest, urlBinder, byNameTemaplate, resource, byUriTemplate, redirect);
		}
		
	}
	
	private String getForwardOrRedirectString(boolean redirect){
		return redirect ? "redirect" : "forward";
	}
	
	protected <R> ModelAndView forward(
			HttpServletRequest httpServletRequest,
			UrlTemplateBinder<R> urlBinder,
			String urlTemplate, 
			R resource,
			String byUriTemplate,
			boolean redirect) {
		String forwardOrRedirect = getForwardOrRedirectString(redirect);
		
		String url = this.urlTemplateBindingCreator.bindResourceToUrlTemplate(urlBinder, resource, urlTemplate);
		
		String extraUrlPath = StringUtils.substringAfter(httpServletRequest.getRequestURI(), StringUtils.removeEnd(byUriTemplate, ALL_WILDCARD));
		
		if(StringUtils.isNotBlank(extraUrlPath)){
			url = url + "/" + extraUrlPath;
		}
		
		ModelAndView mav = new ModelAndView(
				forwardOrRedirect + ":" + url);
		
		return mav;
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

		String urlRoot = this.serverContext.getServerRootWithAppName();

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
	protected String getParametersString(Map<String, Object> parameters,
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

	protected FilterResolver getFilterResolver() {
		return filterResolver;
	}

	protected void setFilterResolver(FilterResolver filterResolver) {
		this.filterResolver = filterResolver;
	}

	protected ReadContextResolver getReadContextResolver() {
		return readContextResolver;
	}

	protected void setReadContextResolver(ReadContextResolver readContextResolver) {
		this.readContextResolver = readContextResolver;
	}

	public ServerContext getServerContext() {
		return serverContext;
	}

	public void setServerContext(ServerContext serverContext) {
		this.serverContext = serverContext;
	}
}
