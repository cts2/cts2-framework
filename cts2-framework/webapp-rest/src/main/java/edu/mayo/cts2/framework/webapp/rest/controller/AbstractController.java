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

import java.beans.PropertyEditorSupport;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.oxm.XmlMappingException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import edu.mayo.cts2.framework.core.constants.ModelAndViewInterface;
import edu.mayo.cts2.framework.core.constants.URIHelperInterface;
import edu.mayo.cts2.framework.model.core.FilterComponent;
import edu.mayo.cts2.framework.model.core.MatchAlgorithmReference;
import edu.mayo.cts2.framework.model.core.ScopedEntityName;
import edu.mayo.cts2.framework.model.core.URIAndEntityName;
import edu.mayo.cts2.framework.model.core.types.TargetReferenceType;
import edu.mayo.cts2.framework.model.exception.Cts2RestException;
import edu.mayo.cts2.framework.model.exception.ExceptionFactory;
import edu.mayo.cts2.framework.model.exception.UnspecifiedCts2RestException;
import edu.mayo.cts2.framework.model.service.core.QueryControl;
import edu.mayo.cts2.framework.model.service.exception.CTS2Exception;
import edu.mayo.cts2.framework.model.service.exception.UnknownResourceReference;
import edu.mayo.cts2.framework.service.command.Filter;
import edu.mayo.cts2.framework.service.command.Page;
import edu.mayo.cts2.framework.service.profile.QueryService;
import edu.mayo.cts2.framework.webapp.rest.exception.Cts2RestExceptionCodeMapper;
import edu.mayo.cts2.framework.webapp.rest.view.NoPathParamRedirectView;

/**
 * The Class AbstractController.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractController implements URIHelperInterface, ModelAndViewInterface {
	
	protected Log log = LogFactory.getLog(getClass());

	@Resource
	private Cts2RestExceptionCodeMapper cts2RestExceptionCodeMapper;
	
	/**
	 * Decode uri.
	 *
	 * @param uri the uri
	 * @return the string
	 */
	protected String decodeUri(String uri){
		try {
			return URLDecoder.decode(uri, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException(e);
		}	
	}

	/**
	 * Handle exception.
	 *
	 * @param response the response
	 * @param ex the ex
	 * @return the model and view
	 */
	@ExceptionHandler(Cts2RestException.class)
	@ResponseBody
	public CTS2Exception handleException(HttpServletResponse response, Cts2RestException ex) {
		int status = this.cts2RestExceptionCodeMapper.getErrorCode(ex);
		
		response.setStatus(status);
		
		return ex.getCts2Exception();
	}
	
	/**
	 * Handle exception.
	 *
	 * @param response the response
	 * @param request the request
	 * @param ex the ex
	 * @return the model and view
	 */
	@ExceptionHandler(RuntimeException.class)
	@ResponseBody
	public CTS2Exception handleException(
			HttpServletResponse response, 
			HttpServletRequest request, 
			RuntimeException ex) {
		log.error(ex);
		
		int status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
		
		response.setStatus(status);
		
		return 
				ExceptionFactory.createUnknownException(ex, 
						getUrlString(request),getParameterString(request));
	}
	
	/**
	 * Handle exception.
	 *
	 * @param response the response
	 * @param request the request
	 * @param ex the ex
	 * @return the model and view
	 */
	@ExceptionHandler(UnspecifiedCts2RestException.class)
	@ResponseBody
	public CTS2Exception handleException(
			HttpServletResponse response, 
			HttpServletRequest request, 
			UnspecifiedCts2RestException ex) {
		log.error(ex);
		
		int status = ex.getStatusCode();
		
		response.setStatus(status);
		
		return 
				ExceptionFactory.createUnknownException(ex.getMessage(), 
						getUrlString(request),getParameterString(request));
	}
	
	/**
	 * Handle exception.
	 *
	 * @param response the response
	 * @param request the request
	 * @param ex the ex
	 * @return the model and view
	 */
	@ExceptionHandler(UnsupportedOperationException.class)
	@ResponseBody
	public CTS2Exception handleException(
			HttpServletResponse response, 
			HttpServletRequest request, 
			UnsupportedOperationException ex) {
		log.error(ex);
		
		int status = HttpServletResponse.SC_NOT_IMPLEMENTED;
		
		response.setStatus(status);
		
		return ExceptionFactory.createUnknownException(
						"Method not implemented. " + ex.getMessage(), getUrlString(request),getParameterString(request));
	}
	
	/**
	 * Handle exception.
	 *
	 * @param response the response
	 * @param request the request
	 * @param ex the ex
	 * @return the model and view
	 */
	@ExceptionHandler(XmlMappingException.class)
	@ResponseBody
	public CTS2Exception handleException(
			HttpServletResponse response, 
			HttpServletRequest request, 
			XmlMappingException ex) {
		log.error(ex);
		
		int status = HttpServletResponse.SC_BAD_REQUEST;

		response.setStatus(status);
		
		return ExceptionFactory.createUnknownException(
						ex.getMessage(), getUrlString(request),getParameterString(request));
	}
	
	/**
	 * Gets the url string.
	 *
	 * @param request the request
	 * @return the url string
	 */
	private String getUrlString(HttpServletRequest request){
		return request.getContextPath();
	}
	
	/**
	 * Gets the parameter string.
	 *
	 * @param request the request
	 * @return the parameter string
	 */
	private String getParameterString(HttpServletRequest request){
		return request.getQueryString();
	}

	/**
	 * Handle exists.
	 *
	 * @param resourceName the resource name
	 * @param exceptionClass the exception class
	 * @param httpServletResponse the http servlet response
	 * @param exists the exists
	 */
	protected void handleExists(String resourceName, 
			Class<? extends UnknownResourceReference> exceptionClass, 
			HttpServletResponse httpServletResponse,
			boolean exists){
		
		if(exists){
			httpServletResponse.setStatus(HttpServletResponse.SC_OK);
		} else {
			throw ExceptionFactory.createUnknownResourceException(resourceName, exceptionClass);
		}
	}
	
	/**
	 * Sets the count.
	 *
	 * @param count the count
	 * @param httpServletResponse the http servlet response
	 */
	protected void setCount(int count, HttpServletResponse httpServletResponse) {
		httpServletResponse.setHeader(HEADER_COUNT, Integer.toString(count));
	}
	
	/**
	 * Redirect.
	 *
	 * @param redirectUrl the redirect url
	 * @param httpServletRequest the http servlet request
	 * @param pathParametersToRemove the path parameters to remove
	 * @return the model and view
	 */
	@SuppressWarnings("unchecked")
	protected ModelAndView redirect(String redirectUrl, HttpServletRequest httpServletRequest, String... pathParametersToRemove){
		RedirectView rmv = new NoPathParamRedirectView(redirectUrl, Arrays.asList(pathParametersToRemove));

		ModelAndView mav = new ModelAndView(rmv);

		mav.addAllObjects(httpServletRequest.getParameterMap());
		
		return mav;
	}
	
	/**
	 * Gets the end.
	 *
	 * @param page the page
	 * @return the end
	 */
	protected int getEnd(Page page){
		return ( page.getPage() + 1 ) * page.getMaxtoreturn();
	}
	
	/**
	 * Checks if is last page.
	 *
	 * @param page the page
	 * @param pageSize the page size
	 * @return true, if is last page
	 */
	protected boolean isLastPage(Page page, int pageSize){
		return pageSize < page.getMaxtoreturn();
	}
	
	/**
	 * Gets the start.
	 *
	 * @param page the page
	 * @return the start
	 */
	protected int getStart(Page page){
		return page.getPage() * page.getMaxtoreturn();
	}

	/**
	 * Process filter.
	 *
	 * @param filter the filter
	 * @param service the service
	 * @return the filter component
	 */
	protected FilterComponent processFilter(Filter filter, QueryService<?,?,?> service){
		if(filter == null ||
				StringUtils.isBlank(filter.getMatchValue())){
			return null;
		}
		
		String matchAlgorithmReference = filter.getMatchalgorithm();
		
		MatchAlgorithmReference matchRef = 
			service.getMatchAlgorithmReference(matchAlgorithmReference);
		
		FilterComponent filterComponent = new FilterComponent();
		filterComponent.setMatchAlgorithm(matchRef);
		filterComponent.setMatchValue(filter.getMatchValue());
		
		String nameOrUri = filter.getFiltercomponent();
		
		String name;
		
		switch(filter.getReferencetype()){
			case ATTRIBUTE: {
				name = service.getModelAttributeReference(nameOrUri).getContent();
				break;
			}
			case PROPERTY: {
				name = service.getPropertyReference(nameOrUri).getName();
				break;
			}
			case SPECIAL: {
				throw new UnsupportedOperationException("SPECIAL not supported yet.");
			}
			default : {
				throw new IllegalStateException();
			}
		}
		
		URIAndEntityName uriEntityName = new URIAndEntityName();
		uriEntityName.setName(name);
		
		filterComponent.setReferenceTarget(uriEntityName);
		
		filterComponent.setReferenceType(filter.getReferencetype());
		
		return filterComponent;
	}
	
	/**
	 * Inits the binder.
	 *
	 * @param binder the binder
	 */
	@InitBinder
	 public void initBinder(WebDataBinder binder) {

	  binder.registerCustomEditor(TargetReferenceType.class,
	    new TargetReferenceTypeEditor());
	 }
	
	@InitBinder
	 public void initotherBinder(WebDataBinder binder) {

		
		binder.registerCustomEditor(QueryControl.class,
			    new QueryControlTypeEditor());
	 }
	
	/**
	 * The Class TargetReferenceTypeEditor.
	 *
	 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
	 */
	private class TargetReferenceTypeEditor extends PropertyEditorSupport{

	    @Override
	    public void setAsText(final String text){
	        this.setValue(TargetReferenceType.valueOf(text.toUpperCase()));
	    }
	}
	
	private class QueryControlTypeEditor extends PropertyEditorSupport{

	    @Override
	    public void setAsText(final String text){
	        this.setValue(TargetReferenceType.valueOf(text.toUpperCase()));
	    }
	}
	
	/**
	 * Gets the scoped entity name.
	 *
	 * @param entityName the entity name
	 * @param codeSystemName the code system name
	 * @return the scoped entity name
	 */
	protected ScopedEntityName getScopedEntityName(String entityName, String codeSystemName){
		ScopedEntityName scopedName = new ScopedEntityName();
		
		String[] nameParts = entityName.split(":");
		if(nameParts.length == 2){
			scopedName.setNamespace(nameParts[0]);
			scopedName.setName(nameParts[1]);
		} else if(nameParts.length == 1){
			scopedName.setNamespace(codeSystemName);
			scopedName.setName(nameParts[0]);
		} else {
			throw new IllegalStateException();
		}
		
		return scopedName;
	}
}