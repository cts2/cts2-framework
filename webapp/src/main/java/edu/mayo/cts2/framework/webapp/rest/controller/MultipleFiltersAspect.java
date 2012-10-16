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

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;

import edu.mayo.cts2.framework.core.constants.URIHelperInterface;
import edu.mayo.cts2.framework.webapp.rest.command.RestFilter;
import edu.mayo.cts2.framework.webapp.rest.command.RestFilters;

/**
 * The Class MethodTimingAspect.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Aspect
@Order(2)
public class MultipleFiltersAspect {

	@Before("execution(public *"
	        + " edu.mayo.cts2.framework.webapp.rest.controller.*.*(..,javax.servlet.http.HttpServletRequest,..,edu.mayo.cts2.framework.webapp.rest.command.RestFilters,..))")
	    public void addRestFilter(final JoinPoint jp) throws Throwable {

		RestFilters restFilters = null;
		HttpServletRequest httpServletRequest = null;
		
		//this should never happen
		if(ArrayUtils.isEmpty(jp.getArgs())){
			throw new IllegalStateException("Pointcut failure!");
		}
		
		boolean foundRestFilters = false;
		boolean foundHttpServletRequest = false;
		for(Object arg : jp.getArgs()){
			if(arg instanceof RestFilters){
				restFilters = (RestFilters) arg;
				foundRestFilters = true;
			}
			if(arg instanceof HttpServletRequest){
				httpServletRequest = (HttpServletRequest) arg;
				foundHttpServletRequest = true;
			}
			if(foundRestFilters && foundHttpServletRequest){
				break;
			}
		}
		
		//this also should never happen
		if(restFilters == null || httpServletRequest == null){
			throw new IllegalStateException("Pointcut failure!");
		}
		
		Map<String,FilterTuple> filters = new HashMap<String,FilterTuple>();
		
		@SuppressWarnings("unchecked")
		Enumeration<String> params = httpServletRequest.getParameterNames();
		while(params.hasMoreElements()){
			String param = params.nextElement();
			if(this.isGroupedParam(param, URIHelperInterface.PARAM_FILTERCOMPONENT)){
				String groupId = this.getGroupId(param, URIHelperInterface.PARAM_FILTERCOMPONENT);
				FilterTuple tuple = this.getFilterTuple(groupId, filters);
				tuple.filterComponent = httpServletRequest.getParameter(param);
			}
			if(this.isGroupedParam(param, URIHelperInterface.PARAM_MATCHALGORITHM)){
				String groupId = this.getGroupId(param, URIHelperInterface.PARAM_MATCHALGORITHM);
				FilterTuple tuple = this.getFilterTuple(groupId, filters);
				tuple.matchAlgorithm = httpServletRequest.getParameter(param);
			}
			if(this.isGroupedParam(param, URIHelperInterface.PARAM_MATCHVALUE)){
				String groupId = this.getGroupId(param, URIHelperInterface.PARAM_MATCHVALUE);
				FilterTuple tuple = this.getFilterTuple(groupId, filters);
				tuple.matchValue = httpServletRequest.getParameter(param);
			}
		}
		
		for(FilterTuple tuple : filters.values()){
			restFilters.getRestFilters().add(this.toRestFilter(tuple));
		}
	}
	
	private RestFilter toRestFilter(FilterTuple tuple){
		RestFilter filter = new RestFilter();
		if(StringUtils.isNotBlank(tuple.filterComponent)){
			filter.setFiltercomponent(tuple.filterComponent);
		}
		if(StringUtils.isNotBlank(tuple.matchAlgorithm)){
			filter.setMatchalgorithm(tuple.matchAlgorithm);
		}
		filter.setMatchvalue(tuple.matchValue);
		
		return filter;
	}
	
	private FilterTuple getFilterTuple(String groupId, Map<String,FilterTuple> filters){
		if(! filters.containsKey(groupId)){
			filters.put(groupId, new FilterTuple());
		}
		
		return filters.get(groupId);
	}
	
	private boolean isGroupedParam(String groupedParamName, String paramName){
		return groupedParamName.startsWith(paramName) && groupedParamName.length() > paramName.length();
	}
	
	private String getGroupId(String groupedParamName, String paramName){
		return groupedParamName.substring(paramName.length());
	}
	
	private static class FilterTuple {
		private String filterComponent;
		private String matchAlgorithm;
		private String matchValue;
	}
}
