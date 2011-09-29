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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.mayo.cts2.framework.model.codesystem.CodeSystemCatalogEntry;
import edu.mayo.cts2.framework.model.codesystem.CodeSystemCatalogEntryDirectory;
import edu.mayo.cts2.framework.model.codesystem.CodeSystemCatalogEntryMsg;
import edu.mayo.cts2.framework.model.codesystem.CodeSystemCatalogEntrySummary;
import edu.mayo.cts2.framework.model.core.FilterComponent;
import edu.mayo.cts2.framework.model.directory.DirectoryResult;
import edu.mayo.cts2.framework.model.service.core.Query;
import edu.mayo.cts2.framework.model.service.exception.UnknownCodeSystem;
import edu.mayo.cts2.framework.service.command.Filter;
import edu.mayo.cts2.framework.service.command.Page;
import edu.mayo.cts2.framework.service.command.QueryControl;
import edu.mayo.cts2.framework.service.profile.codesystem.CodeSystemMaintenanceService;
import edu.mayo.cts2.framework.service.profile.codesystem.CodeSystemQueryService;
import edu.mayo.cts2.framework.service.profile.codesystem.CodeSystemReadService;

/**
 * The Class CodeSystemController.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Controller
public class CodeSystemController extends AbstractServiceAwareController {
	
	@Cts2Service
	private CodeSystemReadService codeSystemReadService;
	
	@Cts2Service
	private CodeSystemQueryService codeSystemQueryService;
	
	@Cts2Service
	private CodeSystemMaintenanceService codeSystemMaintenanceService;

	/**
	 * Gets the code systems.
	 *
	 * @param httpServletRequest the http servlet request
	 * @param filter the filter
	 * @param page the page
	 * @return the code systems
	 */
	@RequestMapping(value=PATH_CODESYSTEMS, method=RequestMethod.GET)
	@ResponseBody
	public CodeSystemCatalogEntryDirectory getCodeSystems(
			HttpServletRequest httpServletRequest,
			QueryControl queryControl,
			Filter filter,
			Page page) {
		
		return this.getCodeSystems(httpServletRequest, queryControl, null, filter, page);
	}
	
	/**
	 * Gets the code systems.
	 *
	 * @param httpServletRequest the http servlet request
	 * @param query the query
	 * @param filter the filter
	 * @param page the page
	 * @return the code systems
	 */
	@RequestMapping(value=PATH_CODESYSTEMS, method=RequestMethod.POST)
	@ResponseBody
	public CodeSystemCatalogEntryDirectory getCodeSystems(
			HttpServletRequest httpServletRequest,
			QueryControl queryControl,
			@RequestBody Query query,
			Filter filter,
			Page page) {
		
		FilterComponent filterComponent = this.processFilter(filter, codeSystemQueryService);

		DirectoryResult<CodeSystemCatalogEntrySummary> directoryResult = 
			this.codeSystemQueryService.getResourceSummaries(
				query,
				filterComponent, 
				null, 
				page);

		CodeSystemCatalogEntryDirectory directory = this.populateDirectory(
				directoryResult, 
				page, 
				httpServletRequest, 
				CodeSystemCatalogEntryDirectory.class);

		return directory;
	}

	/**
	 * Does code system exist.
	 *
	 * @param httpServletResponse the http servlet response
	 * @param codeSystemName the code system name
	 */
	@RequestMapping(value=PATH_CODESYSTEMBYID, method=RequestMethod.HEAD)
	@ResponseBody
	public void doesCodeSystemExist(
			HttpServletResponse httpServletResponse,
			@PathVariable(VAR_CODESYSTEMID) String codeSystemName) {
		
		boolean exists = this.codeSystemReadService.exists(codeSystemName);
		
		this.handleExists(codeSystemName, UnknownCodeSystem.class, httpServletResponse, exists);
	}
	
	/**
	 * Gets the code systems count.
	 *
	 * @param httpServletResponse the http servlet response
	 * @param query the query
	 * @param filter the filter
	 * @return the code systems count
	 */
	@RequestMapping(value=PATH_CODESYSTEMS, method=RequestMethod.HEAD)
	@ResponseBody
	public void getCodeSystemsCount(
			HttpServletResponse httpServletResponse,
			@RequestBody Query query,
			Filter filter) {
		FilterComponent filterComponent = this.processFilter(filter, this.codeSystemQueryService);
		
		int count = this.codeSystemQueryService.count(
				query,
				filterComponent, null);
		
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
	@ResponseBody
	public CodeSystemCatalogEntryMsg getCodeSystemByName(
			HttpServletRequest httpServletRequest,
			@PathVariable(VAR_CODESYSTEMID) String codeSystemName) {
			
		CodeSystemCatalogEntry codeSystem = this.codeSystemReadService.read(codeSystemName);
		
		CodeSystemCatalogEntryMsg msg = new CodeSystemCatalogEntryMsg();
		msg.setCodeSystemCatalogEntry(codeSystem);
		
		msg = this.wrapMessage(msg, httpServletRequest);
		
		return msg;
	}
	
	/**
	 * Creates the code system.
	 *
	 * @param httpServletRequest the http servlet request
	 * @param codeSystem the code system
	 * @param changeseturi the changeseturi
	 * @param codeSystemName the code system name
	 */
	@RequestMapping(value=PATH_CODESYSTEMBYID, method=RequestMethod.PUT)
	@ResponseBody
	public void createCodeSystem(
			HttpServletRequest httpServletRequest,
			@RequestBody CodeSystemCatalogEntry codeSystem,
			@RequestParam(required=false) String changeseturi,
			@PathVariable(VAR_CODESYSTEMID) String codeSystemName) {
			
		this.codeSystemMaintenanceService.createResource(changeseturi, codeSystem);
	}
	
	/**
	 * Gets the code system by uri.
	 *
	 * @param httpServletRequest the http servlet request
	 * @param uri the uri
	 * @return the code system by uri
	 */
	@RequestMapping(value=PATH_CODESYSTEMBYURI, method=RequestMethod.GET)
	public CodeSystemCatalogEntry getCodeSystemByUri(
			HttpServletRequest httpServletRequest,
			@PathVariable(VAR_URI) String uri) {
		/*
		uri = this.decodeUri(uri);
		
		String name = this.codeSystemService.getCodeSystemNameFromUri(uri);
			
		String path = "../../" + CODESYSTEM + "/" + name;

		return this.redirect(path, httpServletRequest, VAR_URI);
		*/
		//TODO
		return null;
	}
	
	@RequestMapping(value=PATH_CODESYSTEMQUERYSERVICE, method=RequestMethod.GET)
	@ResponseBody
	public edu.mayo.cts2.framework.model.service.codesystem.CodeSystemQueryService getCodeSystemCatalogQueryService() {
		return null;
		//
	}
	
	@RequestMapping(value=PATH_CODESYSTEMREADSERVICE, method=RequestMethod.GET)
	@ResponseBody
	public edu.mayo.cts2.framework.model.service.codesystem.CodeSystemReadService getCodeSystemCatalogReadService() {
		return null;
		//
	}
}