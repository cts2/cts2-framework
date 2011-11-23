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

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import edu.mayo.cts2.framework.model.command.Page;
import edu.mayo.cts2.framework.model.command.ResolvedFilter;
import edu.mayo.cts2.framework.model.core.Message;
import edu.mayo.cts2.framework.model.directory.DirectoryResult;
import edu.mayo.cts2.framework.model.service.core.Query;
import edu.mayo.cts2.framework.model.service.exception.UnknownValueSet;
import edu.mayo.cts2.framework.model.updates.ChangeableResourceChoice;
import edu.mayo.cts2.framework.model.util.ModelUtils;
import edu.mayo.cts2.framework.model.valueset.ValueSetCatalogEntry;
import edu.mayo.cts2.framework.model.valueset.ValueSetCatalogEntryDirectory;
import edu.mayo.cts2.framework.model.valueset.ValueSetCatalogEntryList;
import edu.mayo.cts2.framework.model.valueset.ValueSetCatalogEntryMsg;
import edu.mayo.cts2.framework.model.valueset.ValueSetCatalogEntrySummary;
import edu.mayo.cts2.framework.service.command.restriction.ValueSetQueryServiceRestrictions;
import edu.mayo.cts2.framework.service.profile.valueset.ValueSetHistoryService;
import edu.mayo.cts2.framework.service.profile.valueset.ValueSetMaintenanceService;
import edu.mayo.cts2.framework.service.profile.valueset.ValueSetQueryService;
import edu.mayo.cts2.framework.service.profile.valueset.ValueSetReadService;
import edu.mayo.cts2.framework.webapp.rest.command.QueryControl;
import edu.mayo.cts2.framework.webapp.rest.command.RestFilter;
import edu.mayo.cts2.framework.webapp.rest.command.RestReadContext;

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
	
	@Cts2Service
	private ValueSetHistoryService valueSetHistoryService;
	
	private final static UrlTemplateBinder<ValueSetCatalogEntry> URL_BINDER =
			new UrlTemplateBinder<ValueSetCatalogEntry>(){

		@Override
		public Map<String,String> getPathValues(ValueSetCatalogEntry resource) {
			Map<String,String> returnMap = new HashMap<String,String>();

			returnMap.put(VAR_VALUESETID,resource.getValueSetName());
			
			return returnMap;
		}

	};
	
	private final static MessageFactory<ValueSetCatalogEntry> MESSAGE_FACTORY = 
			new MessageFactory<ValueSetCatalogEntry>() {

		@Override
		public Message createMessage(ValueSetCatalogEntry resource) {
			ValueSetCatalogEntryMsg msg = new ValueSetCatalogEntryMsg();
			msg.setValueSetCatalogEntry(resource);

			return msg;
		}
	};
	
	@RequestMapping(value=PATH_VALUESET_CHANGEHISTORY, method=RequestMethod.GET)
	@ResponseBody
	public ValueSetCatalogEntryList getChangeHistory(
			HttpServletRequest httpServletRequest,
			@PathVariable(VAR_VALUESETID) String valueSetName,
			@RequestParam(required=false) Date PARAM_FROMDATE,
			@RequestParam(required=false) Date PARAM_TODATE,
			Page page) {
	
		DirectoryResult<ValueSetCatalogEntry> result = 
				this.valueSetHistoryService.getChangeHistory(
						ModelUtils.nameOrUriFromName(valueSetName),
						PARAM_FROMDATE,
						PARAM_TODATE);
		
		return this.populateDirectory(
				result, 
				page, 
				httpServletRequest, 
				ValueSetCatalogEntryList.class);	
	}
	
	@RequestMapping(value=PATH_VALUESET_EARLIESTCHANGE, method=RequestMethod.GET)
	@ResponseBody
	public ValueSetCatalogEntryMsg getEarliesChange(
			HttpServletRequest httpServletRequest,
			@PathVariable(VAR_VALUESETID) String valueSetName) {
	
		ValueSetCatalogEntry result = 
				this.valueSetHistoryService.getEarliestChangeFor(
						ModelUtils.nameOrUriFromName(valueSetName));
		
		ValueSetCatalogEntryMsg msg = new ValueSetCatalogEntryMsg();
		msg.setValueSetCatalogEntry(result);
		
		return this.wrapMessage(msg, httpServletRequest);
	}
	
	@RequestMapping(value=PATH_VALUESET_LATESTCHANGE, method=RequestMethod.GET)
	@ResponseBody
	public ValueSetCatalogEntryMsg getLastChange(
			HttpServletRequest httpServletRequest,
			@PathVariable(VAR_VALUESETID) String valueSetName) {
	
		ValueSetCatalogEntry result = 
				this.valueSetHistoryService.getLastChangeFor(
						ModelUtils.nameOrUriFromName(valueSetName));
	
		ValueSetCatalogEntryMsg msg = new ValueSetCatalogEntryMsg();
		msg.setValueSetCatalogEntry(result);
		
		return this.wrapMessage(msg, httpServletRequest);
	}
	
	/**
	 * Gets the value sets.
	 *
	 * @param httpServletRequest the http servlet request
	 * @param query the query
	 * @param restrictions the restrictions
	 * @param resolvedFilter the filter
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
			RestFilter restFilter,
			Page page,
			@RequestParam(value=PARAM_CODESYSTEM, required=false) List<String> codeSystems) {
		
		ResolvedFilter filterComponent = this.processFilter(restFilter, this.valueSetQueryService);

		DirectoryResult<ValueSetCatalogEntrySummary> directoryResult = this.valueSetQueryService.getResourceSummaries(
				query,
				createSet(filterComponent), 
				restrictions,
				null, page);

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
		
		this.doExists(
				httpServletResponse, 
				this.valueSetReadService, 
				UnknownValueSet.class,
				ModelUtils.nameOrUriFromName(valueSetName));
	}
	
	/**
	 * Gets the value sets count.
	 *
	 * @param httpServletResponse the http servlet response
	 * @param query the query
	 * @param restrictions the restrictions
	 * @param resolvedFilter the filter
	 * @return the value sets count
	 */
	@RequestMapping(value=PATH_VALUESETS, method=RequestMethod.HEAD)
	@ResponseBody
	public void getValueSetsCount(
			HttpServletResponse httpServletResponse,
			@RequestBody Query query,
			ValueSetQueryServiceRestrictions restrictions,
			RestFilter restFilter) {
		ResolvedFilter filterComponent = this.processFilter(restFilter, this.valueSetQueryService);
		
		int count = this.valueSetQueryService.count(
				query,
				createSet(filterComponent),
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
	public Message getValueSetByName(
			HttpServletRequest httpServletRequest,
			RestReadContext restReadContext,
			QueryControl queryControl,
			@PathVariable(VAR_VALUESETID) String valueSetName) {
			
		return this.doRead(
				httpServletRequest, 
				MESSAGE_FACTORY, 
				this.valueSetReadService,
				restReadContext,
				UnknownValueSet.class,
				ModelUtils.nameOrUriFromName(valueSetName));
	}
	
	@RequestMapping(value=PATH_VALUESET_BYURI, method=RequestMethod.GET)
	@ResponseBody
	public ModelAndView getValueSetByUri(
			HttpServletRequest httpServletRequest,
			QueryControl queryControl,
			@PathVariable(VAR_URI) String uri,
			@RequestParam(value="redirect", defaultValue="false") boolean redirect) {
		
		return this.doReadByUri(
				httpServletRequest, 
				MESSAGE_FACTORY, 
				PATH_MAP_BYURI, 
				PATH_MAP_BYID, 
				URL_BINDER, 
				this.valueSetReadService,
				ModelUtils.nameOrUriFromUri(uri),
				redirect);
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
	public void updateValueSet(
			HttpServletRequest httpServletRequest,
			@RequestBody ValueSetCatalogEntry valueSet,
			@RequestParam(required=false) String changeseturi,
			@PathVariable(VAR_VALUESETID) String valueSetName) {
			
		ChangeableResourceChoice choice = new ChangeableResourceChoice();
		choice.setValueSet(valueSet);
		
		this.getUpdateHandler().update(
				choice, 
				changeseturi, 
				ModelUtils.nameOrUriFromName(valueSetName), 
				this.valueSetMaintenanceService);
	}
	
	@RequestMapping(value=PATH_VALUESET, method=RequestMethod.POST)
	@ResponseBody
	public void createValueSet(
			HttpServletRequest httpServletRequest,
			@RequestBody ValueSetCatalogEntry valueSet,
			@RequestParam(value=PARAM_CHANGESETCONTEXT, required=false) String changeseturi) {
			
		ChangeableResourceChoice choice = new ChangeableResourceChoice();
		choice.setValueSet(valueSet);
		
		this.getCreateHandler().create(
				choice, 
				changeseturi, 
				PATH_VALUESETBYID, 
				URL_BINDER, 
				this.valueSetMaintenanceService);
	}
	
	@RequestMapping(value=PATH_VALUESETBYID, method=RequestMethod.DELETE)
	@ResponseBody
	public void deleteCodeSystem(
			HttpServletRequest httpServletRequest,
			@PathVariable(VAR_VALUESETID) String valueSetName,
			@RequestParam String changeseturi) {

		this.valueSetMaintenanceService.
			deleteResource(
					ModelUtils.nameOrUriFromName(
							valueSetName), 
							changeseturi);
	}
}