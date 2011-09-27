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

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.mayo.cts2.framework.model.core.FilterComponent;
import edu.mayo.cts2.framework.model.directory.DirectoryResult;
import edu.mayo.cts2.framework.model.service.core.Query;
import edu.mayo.cts2.framework.model.service.exception.UnknownResourceReference;
import edu.mayo.cts2.framework.model.valueset.ValueSetCatalogEntry;
import edu.mayo.cts2.framework.model.valueset.ValueSetCatalogEntryDirectory;
import edu.mayo.cts2.framework.model.valueset.ValueSetCatalogEntryMsg;
import edu.mayo.cts2.framework.model.valueset.ValueSetCatalogEntrySummary;
import edu.mayo.cts2.framework.service.command.Filter;
import edu.mayo.cts2.framework.service.command.Page;
import edu.mayo.cts2.framework.service.command.restriction.ValueSetQueryServiceRestrictions;
import edu.mayo.cts2.framework.service.profile.valueset.ValueSetMaintenanceService;
import edu.mayo.cts2.framework.service.profile.valueset.ValueSetQueryService;
import edu.mayo.cts2.framework.service.profile.valueset.ValueSetReadService;

/**
 * The Class ValueSetController.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Controller
public class ValueSetController extends AbstractServiceAwareController {
	
	@Cts2Service
	private ValueSetReadService valueSetReadService;
	
	@Cts2Service
	private ValueSetQueryService valueSetQueryService;
	
	@Cts2Service
	private ValueSetMaintenanceService valueSetMaintenanceService;
	
	/**
	 * Gets the value sets.
	 *
	 * @param httpServletRequest the http servlet request
	 * @param query the query
	 * @param restrictions the restrictions
	 * @param filter the filter
	 * @param page the page
	 * @param codeSystems the code systems
	 * @return the value sets
	 */
	@RequestMapping(value=PATH_VALUESETS, method=RequestMethod.POST)
	@ResponseBody
	public ValueSetCatalogEntryDirectory getValueSets(
			HttpServletRequest httpServletRequest,
			@RequestBody Query query,
			ValueSetQueryServiceRestrictions restrictions,
			Filter filter,
			Page page,
			@RequestParam(value=PARAM_CODESYSTEM, required=false) List<String> codeSystems) {
		
		FilterComponent filterComponent = this.processFilter(filter, this.valueSetQueryService);

		DirectoryResult<ValueSetCatalogEntrySummary> directoryResult = this.valueSetQueryService.getResourceSummaries(
				query,
				filterComponent, 
				restrictions,
				page);

		ValueSetCatalogEntryDirectory directory = this.populateDirectory(
				directoryResult, 
				page, 
				httpServletRequest, 
				ValueSetCatalogEntryDirectory.class);

		return directory;
	}
	
	/**
	 * Does value set exist.
	 *
	 * @param httpServletResponse the http servlet response
	 * @param valueSetName the value set name
	 */
	@RequestMapping(value=PATH_VALUESETBYID, method=RequestMethod.HEAD)
	@ResponseBody
	public void doesValueSetExist(
			HttpServletResponse httpServletResponse,
			@PathVariable(VAR_CODESYSTEMID) String valueSetName) {
		
		boolean exists = this.valueSetReadService.exists(valueSetName);
		
		this.handleExists(valueSetName, UnknownResourceReference.class, httpServletResponse, exists);
	}
	
	/**
	 * Gets the value sets count.
	 *
	 * @param httpServletResponse the http servlet response
	 * @param query the query
	 * @param restrictions the restrictions
	 * @param filter the filter
	 * @return the value sets count
	 */
	@RequestMapping(value=PATH_VALUESETS, method=RequestMethod.HEAD)
	@ResponseBody
	public void getValueSetsCount(
			HttpServletResponse httpServletResponse,
			@RequestBody Query query,
			ValueSetQueryServiceRestrictions restrictions,
			Filter filter) {
		FilterComponent filterComponent = this.processFilter(filter, this.valueSetQueryService);
		
		int count = this.valueSetQueryService.count(
				query,
				filterComponent,
				restrictions);
		
		this.setCount(count, httpServletResponse);
	}

	/**
	 * Gets the value set by name.
	 *
	 * @param httpServletRequest the http servlet request
	 * @param valueSetName the value set name
	 * @return the value set by name
	 */
	@RequestMapping(value=PATH_VALUESETBYID, method=RequestMethod.GET)
	@ResponseBody
	public ValueSetCatalogEntryMsg getValueSetByName(
			HttpServletRequest httpServletRequest,
			@PathVariable(VAR_VALUESETID) String valueSetName) {
			
		ValueSetCatalogEntry valueSet = this.valueSetReadService.read(valueSetName);
		
		ValueSetCatalogEntryMsg msg = new ValueSetCatalogEntryMsg();
		msg.setValueSetCatalogEntry(valueSet);
		
		msg = this.wrapMessage(msg, httpServletRequest);
		
		return msg;
	}
	
	/**
	 * Creates the value set.
	 *
	 * @param httpServletRequest the http servlet request
	 * @param valueSet the value set
	 * @param changeseturi the changeseturi
	 * @param valueSetName the value set name
	 */
	@RequestMapping(value=PATH_VALUESETBYID, method=RequestMethod.PUT)
	@ResponseBody
	public void createValueSet(
			HttpServletRequest httpServletRequest,
			@RequestBody ValueSetCatalogEntry valueSet,
			@RequestParam(required=false) String changeseturi,
			@PathVariable(VAR_VALUESETID) String valueSetName) {
			
		this.valueSetMaintenanceService.createResource(changeseturi, valueSet);
	}
}