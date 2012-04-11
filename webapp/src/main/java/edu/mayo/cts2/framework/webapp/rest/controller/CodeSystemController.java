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
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import edu.mayo.cts2.framework.model.codesystem.CodeSystemCatalogEntry;
import edu.mayo.cts2.framework.model.codesystem.CodeSystemCatalogEntryDirectory;
import edu.mayo.cts2.framework.model.codesystem.CodeSystemCatalogEntryList;
import edu.mayo.cts2.framework.model.codesystem.CodeSystemCatalogEntryMsg;
import edu.mayo.cts2.framework.model.command.Page;
import edu.mayo.cts2.framework.model.core.Message;
import edu.mayo.cts2.framework.model.directory.DirectoryResult;
import edu.mayo.cts2.framework.model.service.core.NameOrURI;
import edu.mayo.cts2.framework.model.service.core.Query;
import edu.mayo.cts2.framework.model.service.exception.UnknownCodeSystem;
import edu.mayo.cts2.framework.model.util.ModelUtils;
import edu.mayo.cts2.framework.service.profile.ResourceQuery;
import edu.mayo.cts2.framework.service.profile.codesystem.CodeSystemHistoryService;
import edu.mayo.cts2.framework.service.profile.codesystem.CodeSystemMaintenanceService;
import edu.mayo.cts2.framework.service.profile.codesystem.CodeSystemQueryService;
import edu.mayo.cts2.framework.service.profile.codesystem.CodeSystemReadService;
import edu.mayo.cts2.framework.webapp.rest.command.QueryControl;
import edu.mayo.cts2.framework.webapp.rest.command.RestFilter;
import edu.mayo.cts2.framework.webapp.rest.command.RestReadContext;
import edu.mayo.cts2.framework.webapp.rest.query.ResourceQueryBuilder;

/**
 * The Class CodeSystemController.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Controller
public class CodeSystemController extends AbstractMessageWrappingController {
	
	@Cts2Service
	private CodeSystemReadService codeSystemReadService;
	
	@Cts2Service
	private CodeSystemQueryService codeSystemQueryService;
	
	@Cts2Service
	private CodeSystemMaintenanceService codeSystemMaintenanceService;
	
	@Cts2Service
	private CodeSystemHistoryService codeSystemHistoryService;

	private final static UrlTemplateBinder<CodeSystemCatalogEntry> URL_BINDER =
			new UrlTemplateBinder<CodeSystemCatalogEntry>(){

		@Override
		public Map<String,String> getPathValues(CodeSystemCatalogEntry resource) {
			Map<String,String> returnMap = new HashMap<String,String>();
			returnMap.put(VAR_CODESYSTEMID, resource.getCodeSystemName());
			
			return returnMap;
		}

	};
	
	private final static MessageFactory<CodeSystemCatalogEntry> MESSAGE_FACTORY = 
			new MessageFactory<CodeSystemCatalogEntry>() {

		@Override
		public Message createMessage(CodeSystemCatalogEntry resource) {
			CodeSystemCatalogEntryMsg msg = new CodeSystemCatalogEntryMsg();
			msg.setCodeSystemCatalogEntry(resource);

			return msg;
		}
	};
	
	/**
	 * Gets the code systems.
	 *
	 * @param httpServletRequest the http servlet request
	 * @param resolvedFilter the filter
	 * @param page the page
	 * @return the code systems
	 */
	@RequestMapping(value=PATH_CODESYSTEMS, method=RequestMethod.GET)
	//@ResponseBody
	public Object getCodeSystems(
			HttpServletRequest httpServletRequest,
			RestReadContext restReadContext,
			QueryControl queryControl,
			RestFilter resolvedFilter,
			Page page,
			boolean list) {
		
		return this.getCodeSystems(
				httpServletRequest, 
				restReadContext, 
				queryControl, 
				null, 
				resolvedFilter, 
				page,
				list);
	}
	
	@RequestMapping(value=PATH_CODESYSTEM_CHANGEHISTORY, method=RequestMethod.GET)
	public CodeSystemCatalogEntryList getChangeHistory(
			HttpServletRequest httpServletRequest,
			@PathVariable(VAR_CODESYSTEMID) String codeSystemName,
			@RequestParam(required=false) Date PARAM_FROMDATE,
			@RequestParam(required=false) Date PARAM_TODATE,
			Page page) {
	
		DirectoryResult<CodeSystemCatalogEntry> result = 
				this.codeSystemHistoryService.getChangeHistory(
						ModelUtils.nameOrUriFromName(codeSystemName),
						PARAM_FROMDATE,
						PARAM_TODATE);
		
		return this.populateDirectory(
				result, 
				page, 
				httpServletRequest, 
				CodeSystemCatalogEntryList.class);	
	}
	
	@RequestMapping(value=PATH_CODESYSTEM_EARLIESTCHANGE, method=RequestMethod.GET)
	public CodeSystemCatalogEntryMsg getEarliesChange(
			HttpServletRequest httpServletRequest,
			@PathVariable(VAR_CODESYSTEMID) String codeSystemName) {
	
		CodeSystemCatalogEntry result = 
				this.codeSystemHistoryService.getEarliestChangeFor(ModelUtils.nameOrUriFromName(codeSystemName));
		
		CodeSystemCatalogEntryMsg msg = new CodeSystemCatalogEntryMsg();
		msg.setCodeSystemCatalogEntry(result);
		
		return this.wrapMessage(msg, httpServletRequest);
	}
	
	@RequestMapping(value=PATH_CODESYSTEM_LATESTCHANGE, method=RequestMethod.GET)
	public CodeSystemCatalogEntryMsg getLastChange(
			HttpServletRequest httpServletRequest,
			@PathVariable(VAR_CODESYSTEMID) String codeSystemName) {
	
		CodeSystemCatalogEntry result = 
				this.codeSystemHistoryService.getLastChangeFor(ModelUtils.nameOrUriFromName(codeSystemName));
	
		CodeSystemCatalogEntryMsg msg = new CodeSystemCatalogEntryMsg();
		msg.setCodeSystemCatalogEntry(result);
		
		return this.wrapMessage(msg, httpServletRequest);
	}
	
	/**
	 * Gets the code systems.
	 *
	 * @param httpServletRequest the http servlet request
	 * @param query the query
	 * @param resolvedFilter the filter
	 * @param page the page
	 * @return the code systems
	 */
	@RequestMapping(value=PATH_CODESYSTEMS, method=RequestMethod.POST)
	public Object getCodeSystems(
			HttpServletRequest httpServletRequest,
			RestReadContext restReadContext,
			QueryControl queryControl,
			@RequestBody Query query,
			RestFilter restFilter,
			Page page,
			boolean list) {
		
		ResourceQueryBuilder builder = this.getNewResourceQueryBuilder();
		
		ResourceQuery resourceQuery = builder.addQuery(query).
				addRestFilter(restFilter).
				addRestReadContext(restReadContext).
				build();
		
		return this.doQuery(
				httpServletRequest,
				list, 
				this.codeSystemQueryService,
				resourceQuery,
				page, 
				queryControl,
				CodeSystemCatalogEntryDirectory.class, 
				CodeSystemCatalogEntryList.class);
	}

	/**
	 * Does code system exist.
	 *
	 * @param httpServletResponse the http servlet response
	 * @param codeSystemName the code system name
	 */
	@RequestMapping(value=PATH_CODESYSTEMBYID, method=RequestMethod.HEAD)
	public void doesCodeSystemExist(
			HttpServletResponse httpServletResponse,
			@PathVariable(VAR_CODESYSTEMID) String codeSystemName) {

		this.doExists(httpServletResponse, 
				this.codeSystemReadService, 
				UnknownCodeSystem.class,
				ModelUtils.nameOrUriFromName(codeSystemName));
	}
	
	/**
	 * Gets the code systems count.
	 *
	 * @param httpServletResponse the http servlet response
	 * @param query the query
	 * @param resolvedFilter the filter
	 * @return the code systems count
	 */
	@RequestMapping(value=PATH_CODESYSTEMS, method=RequestMethod.HEAD)
	public void getCodeSystemsCount(
			HttpServletResponse httpServletResponse,
			RestReadContext restReadContext,
			RestFilter restFilter) {
		
		ResourceQuery resourceQuery = this.getNewResourceQueryBuilder().
				addRestFilter(restFilter).
				addRestReadContext(restReadContext).
				build();
		
		int count = this.codeSystemQueryService.count(resourceQuery);
		
		this.setCount(count, httpServletResponse);
	}

	/**
	 * Gets the code system by name.
	 *
	 * @param httpServletRequest the http servlet request
	 * @param codeSystemName the code system name
	 * @return the code system by name
	 */
	@RequestMapping(value=PATH_CODESYSTEMBYID, method=RequestMethod.GET)
	public Object getCodeSystemByName(
			HttpServletRequest httpServletRequest,
			RestReadContext restReadContext,
			QueryControl queryControl,
			@PathVariable(VAR_CODESYSTEMID) String codeSystemName) {
			
		return this.doRead(
				httpServletRequest, 
				MESSAGE_FACTORY, 
				this.codeSystemReadService, 
				restReadContext,
				UnknownCodeSystem.class,
				ModelUtils.nameOrUriFromName(codeSystemName));
	}
	
	/**
	 * Creates the code system.
	 *
	 * @param httpServletRequest the http servlet request
	 * @param codeSystem the code system
	 * @param changeseturi the changeseturi
	 * @param codeSystemName the code system name
	 */
	@RequestMapping(value=PATH_CODESYSTEM, method=RequestMethod.POST)
	public Object createCodeSystem(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse,
			@RequestBody CodeSystemCatalogEntry codeSystem,
			@RequestParam(value=PARAM_CHANGESETCONTEXT, required=false) String changeseturi) {

		return this.doCreate(
				httpServletResponse,
				codeSystem, 
				changeseturi, 
				PATH_CODESYSTEMBYID,
				URL_BINDER,
				this.codeSystemMaintenanceService);
	}

	@RequestMapping(value=PATH_CODESYSTEMBYID, method=RequestMethod.PUT)
	public Object updateCodeSystem(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse,
			@RequestBody CodeSystemCatalogEntry codeSystem,
			@RequestParam(value=PARAM_CHANGESETCONTEXT, required=false) String changeseturi) {

		return this.doUpdate(
				httpServletResponse,
				codeSystem, 
				changeseturi, 
				ModelUtils.nameOrUriFromName(codeSystem.getCodeSystemName()),
				this.codeSystemMaintenanceService);
	}
	
	@RequestMapping(value=PATH_CODESYSTEMBYID, method=RequestMethod.DELETE)
	public Object deleteCodeSystem(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse,
			@PathVariable(VAR_CODESYSTEMID) String codeSystemName,
			@RequestParam(PARAM_CHANGESETCONTEXT) String changeseturi) {

		NameOrURI identifier = ModelUtils.nameOrUriFromName(codeSystemName);

		return this.doDelete(
				httpServletResponse,
				identifier, 
				changeseturi, 
				this.codeSystemMaintenanceService);
	}
	
	/**
	 * Gets the code system by uri.
	 *
	 * @param httpServletRequest the http servlet request
	 * @param uri the uri
	 * @return the code system by uri
	 */
	@RequestMapping(value=PATH_CODESYSTEMBYURI, method=RequestMethod.GET)
	public ModelAndView getCodeSystemByUri(
			HttpServletRequest httpServletRequest,
			RestReadContext restReadContext,
			QueryControl queryControl,
			@RequestParam(PARAM_URI) String uri,
			@RequestParam(value="redirect", defaultValue="false") boolean redirect) {
	
		return this.doReadByUri(
				httpServletRequest, 
				MESSAGE_FACTORY, 
				PATH_CODESYSTEMBYURI,
				PATH_CODESYSTEMBYID, 
				URL_BINDER, 
				this.codeSystemReadService, 
				restReadContext,
				UnknownCodeSystem.class,
				ModelUtils.nameOrUriFromUri(uri), 
				redirect);
	}

	@RequestMapping(value=PATH_CODESYSTEMQUERYSERVICE, method=RequestMethod.GET)
	public edu.mayo.cts2.framework.model.service.codesystem.CodeSystemQueryService getCodeSystemCatalogQueryService() {
		return null;
		//
	}
	
	@RequestMapping(value=PATH_CODESYSTEMREADSERVICE, method=RequestMethod.GET)
	public edu.mayo.cts2.framework.model.service.codesystem.CodeSystemReadService getCodeSystemCatalogReadService() {
		return null;
		//
	}
	
	private ResourceQueryBuilder getNewResourceQueryBuilder(){
		return new ResourceQueryBuilder(
			this.codeSystemQueryService, 
			this.getFilterResolver(),
			this.getReadContextResolver());
	}

	public CodeSystemReadService getCodeSystemReadService() {
		return codeSystemReadService;
	}

	public void setCodeSystemReadService(CodeSystemReadService codeSystemReadService) {
		this.codeSystemReadService = codeSystemReadService;
	}

	public CodeSystemQueryService getCodeSystemQueryService() {
		return codeSystemQueryService;
	}

	public void setCodeSystemQueryService(
			CodeSystemQueryService codeSystemQueryService) {
		this.codeSystemQueryService = codeSystemQueryService;
	}

	public CodeSystemMaintenanceService getCodeSystemMaintenanceService() {
		return codeSystemMaintenanceService;
	}

	public void setCodeSystemMaintenanceService(
			CodeSystemMaintenanceService codeSystemMaintenanceService) {
		this.codeSystemMaintenanceService = codeSystemMaintenanceService;
	}

	public CodeSystemHistoryService getCodeSystemHistoryService() {
		return codeSystemHistoryService;
	}

	public void setCodeSystemHistoryService(
			CodeSystemHistoryService codeSystemHistoryService) {
		this.codeSystemHistoryService = codeSystemHistoryService;
	}
}