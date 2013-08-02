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

import edu.mayo.cts2.framework.core.constants.ModelAndViewInterface;
import edu.mayo.cts2.framework.core.constants.URIHelperInterface;
import edu.mayo.cts2.framework.core.util.EncodingUtils;
import edu.mayo.cts2.framework.model.command.Page;
import edu.mayo.cts2.framework.model.core.ScopedEntityName;
import edu.mayo.cts2.framework.model.core.URIAndEntityName;
import edu.mayo.cts2.framework.model.exception.ExceptionFactory;
import edu.mayo.cts2.framework.model.exception.UnspecifiedCts2Exception;
import edu.mayo.cts2.framework.model.service.core.types.LoggingLevel;
import edu.mayo.cts2.framework.model.service.exception.CTS2Exception;
import edu.mayo.cts2.framework.webapp.rest.command.RestFilter;
import edu.mayo.cts2.framework.webapp.rest.command.RestFilters;
import edu.mayo.cts2.framework.webapp.rest.config.RestConfig;
import edu.mayo.cts2.framework.webapp.rest.exception.Cts2RestExceptionCodeMapper;
import edu.mayo.cts2.framework.webapp.rest.exception.StatusSettingCts2RestException;
import edu.mayo.cts2.framework.webapp.service.AbstractServiceAwareBean;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UrlPathHelper;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * The base abstract Spring MVC Controller.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractController extends AbstractServiceAwareBean implements URIHelperInterface, ModelAndViewInterface {
	
	protected Log log = LogFactory.getLog(getClass());
	
	protected static final String DEFAULT_REDIRECT = "true";
	
	@Resource
	private RestConfig restConfig;

	@Resource
	private Cts2RestExceptionCodeMapper cts2RestExceptionCodeMapper;
	
	private UrlPathHelper urlPathHelper = new UrlPathHelper();
	
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
	@ExceptionHandler(CTS2Exception.class)
	@ResponseBody
	public CTS2Exception handleException(HttpServletResponse response, CTS2Exception ex) {
		int status = this.cts2RestExceptionCodeMapper.getErrorCode(ex);

		if(ex.getSeverity() == null){
			ex.setSeverity(LoggingLevel.ERROR);
		}
		
		response.setStatus(status);
		
		return ex;
	}
	
	@ExceptionHandler(StatusSettingCts2RestException.class)
	@ResponseBody
	public CTS2Exception handleException(HttpServletResponse response, StatusSettingCts2RestException ex) {
		response.setStatus(ex.getStatusCode());
		
		return ex;
	}
	
	/**
	 * Handle exception.
	 *
	 * @param response the response
	 * @param request the request
	 * @param ex the ex
	 * @return the model and view
	 */
	@ExceptionHandler(Throwable.class)
	@ResponseBody
	public CTS2Exception handleException(
			HttpServletResponse response, 
			HttpServletRequest request, 
			RuntimeException ex) {
        String errorId = Long.toString(new Date().getTime());

		log.error("Unexpected Error: " + errorId, ex);

		int status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
		
		response.setStatus(status);

        boolean showStackTrace = this.getRestConfig().getShowStackTraceOnError();

        if(showStackTrace){
		    return
				ExceptionFactory.createUnknownException(
						ex,
						getUrlString(request),
						getParameterString(request),
                        true);
        } else {
            return
                ExceptionFactory.createUnknownException(
                        this.getErrorSupportMessage(errorId),
                        getUrlString(request),
                        getParameterString(request));
        }
	}

    protected String getErrorSupportMessage(String errorId){
        String preamble = "An Unexpected error has occurred.";

        String supportEmail = this.getRestConfig().getSupportEmail();

        if(StringUtils.isNotBlank(supportEmail)){
            preamble =
                preamble + "\nPlease contact " + supportEmail + " and reference Log ID: " + errorId;
        }

        return preamble;
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
		int status = HttpServletResponse.SC_NOT_IMPLEMENTED;
		
		response.setStatus(status);
		
		return ExceptionFactory.createUnknownException(
						"Method not implemented. " + ex.getMessage() != null ? ex.getMessage() : "", 
								getUrlString(request),
								getParameterString(request));
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
	 * Sets the count.
	 *
	 * @param count the count
	 * @param httpServletResponse the http servlet response
	 */
	protected void setCount(int count, HttpServletResponse httpServletResponse) {
		httpServletResponse.setHeader(HEADER_COUNT, Integer.toString(count));
	}
	
	protected boolean isPartialRedirect(HttpServletRequest request, String urlTemplatePath){
		String adjustedTemplate = StringUtils.removeEnd(urlTemplatePath, ALL_WILDCARD);
		
		String contextPath = this.getUrlPathHelper().getContextPath(request);
		
		String requestUri = StringUtils.removeStart(request.getRequestURI(),contextPath);
		
		return ! (StringUtils.removeStart(StringUtils.removeEnd(requestUri, "/"), "/").equals(
				StringUtils.removeStart(
						StringUtils.removeEnd(adjustedTemplate, "/"),"/")));
	}
	
	@ExceptionHandler(ConversionNotSupportedException.class)
	@ResponseBody
	public UnspecifiedCts2Exception handleException(
			HttpServletResponse response, 
			HttpServletRequest request, 
			ConversionNotSupportedException ex) {	
		int status = HttpServletResponse.SC_BAD_REQUEST;
		response.setStatus(status);
		
		Class<?> requiredType = ex.getRequiredType();
		String typeName = requiredType.getSimpleName();
		String value = ex.getValue().toString();
		
		String possibleValues = "";
		
		if(requiredType.isEnum()){
			StringBuilder sb = new StringBuilder();
			sb.append(" Possible values include: ");
			
			Object[] values = requiredType.getEnumConstants();
			
			sb.append(StringUtils.join(values, ", "));
			
			possibleValues = sb.toString();
		}
		
		return ExceptionFactory.createUnknownException(
			"Cannot convert value " + value + " to the type " + typeName + "." + possibleValues, 
					getUrlString(request),
					getParameterString(request));
	}
	
	/**
	 * Gets the end.
	 *
	 * @param page the page
	 * @return the end
	 */
	protected int getEnd(Page page){
		return ( page.getPage() + 1 ) * page.getMaxToReturn();
	}
	
	/**
	 * Checks if is last page.
	 *
	 * @param page the page
	 * @param pageSize the page size
	 * @return true, if is last page
	 */
	protected boolean isLastPage(Page page, int pageSize){
		return pageSize < page.getMaxToReturn();
	}
	
	/**
	 * Gets the start.
	 *
	 * @param page the page
	 * @return the start
	 */
	protected int getStart(Page page){
		return page.getPage() * page.getMaxToReturn();
	}
	
	@InitBinder
	 public void initPageBinder(
			 WebDataBinder binder,
			 @RequestParam(value=PARAM_MAXTORETURN, required=false) Integer maxToReturn,
			 @RequestParam(value=PARAM_PAGE, required=false) Integer pageNumber) {
		
		if(binder.getTarget() instanceof Page){
			Page page = 
					(Page) binder.getTarget();
			
			if(maxToReturn != null){
				page.setMaxToReturn(maxToReturn);
			}
			
			if(pageNumber != null){
				page.setPage(pageNumber);
			}
			
		}
	 }
	
	@InitBinder
	public void initEntityDescriptionRestrictionBinder(
			 WebDataBinder binder,
			 @RequestParam(value=PARAM_FILTERCOMPONENT, required=false) String filterComponent,
			 @RequestParam(value=PARAM_MATCHALGORITHM, required=false) String matchAlgorithm,
			 @RequestParam(value=PARAM_MATCHVALUE, required=false) String matchValue) {
		
		if(binder.getTarget() instanceof RestFilters){
			RestFilters filters = 
					(RestFilters) binder.getTarget();

			if(StringUtils.isNotBlank(matchValue)){
				RestFilter filter = new RestFilter();
				if(StringUtils.isNotBlank(filterComponent)){
					filter.setFiltercomponent(filterComponent);
				}
				if(StringUtils.isNotBlank(matchAlgorithm)){
					filter.setMatchalgorithm(matchAlgorithm);
				}
				filter.setMatchvalue(matchValue);
				
				filters.getRestFilters().add(filter);
			}
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
	
	protected <T> Set<T> createSet(T element){
		Set<T> set = new HashSet<T>();
		set.add(element);
		return set;
	}
	
	protected ScopedEntityName getScopedEntityName(String encodedEntityName){
		return EncodingUtils.decodeEntityName(encodedEntityName);
	}
	
	protected String getScopedEntityName(ScopedEntityName scopedEntityName){
		return EncodingUtils.encodeScopedEntityName(scopedEntityName);
	}
	
	protected String getScopedEntityName(URIAndEntityName uriAndEntityName){
		return EncodingUtils.encodeScopedEntityName(uriAndEntityName);
	}

	protected UrlPathHelper getUrlPathHelper() {
		return urlPathHelper;
	}

	protected RestConfig getRestConfig() {
		return restConfig;
	}

	protected void setRestConfig(RestConfig restConfig) {
		this.restConfig = restConfig;
	}
}