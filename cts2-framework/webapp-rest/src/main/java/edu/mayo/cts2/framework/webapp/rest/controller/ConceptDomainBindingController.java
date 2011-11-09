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
import org.springframework.web.bind.annotation.ResponseBody;

import edu.mayo.cts2.framework.model.command.Page;
import edu.mayo.cts2.framework.model.command.ResolvedFilter;
import edu.mayo.cts2.framework.model.conceptdomainbinding.ConceptDomainBinding;
import edu.mayo.cts2.framework.model.conceptdomainbinding.ConceptDomainBindingDirectory;
import edu.mayo.cts2.framework.model.conceptdomainbinding.ConceptDomainBindingDirectoryEntry;
import edu.mayo.cts2.framework.model.conceptdomainbinding.ConceptDomainBindingMsg;
import edu.mayo.cts2.framework.model.core.Message;
import edu.mayo.cts2.framework.model.directory.DirectoryResult;
import edu.mayo.cts2.framework.model.service.core.Query;
import edu.mayo.cts2.framework.model.service.exception.UnknownConceptDomainBinding;
import edu.mayo.cts2.framework.model.updates.ChangeableResourceChoice;
import edu.mayo.cts2.framework.service.command.restriction.ConceptDomainBindingQueryServiceRestrictions;
import edu.mayo.cts2.framework.service.profile.conceptdomainbinding.ConceptDomainBindingMaintenanceService;
import edu.mayo.cts2.framework.service.profile.conceptdomainbinding.ConceptDomainBindingQueryService;
import edu.mayo.cts2.framework.service.profile.conceptdomainbinding.ConceptDomainBindingReadService;
import edu.mayo.cts2.framework.webapp.rest.command.RestFilter;
import edu.mayo.cts2.framework.webapp.rest.command.RestReadContext;

/**
 * The Class ConceptDomainBindingController.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Controller
public class ConceptDomainBindingController extends AbstractServiceAwareController {

	@Cts2Service
	private ConceptDomainBindingReadService conceptDomainBindingReadService;
	
	@Cts2Service
	private ConceptDomainBindingQueryService conceptDomainBindingQueryService;
	
	@Cts2Service
	private ConceptDomainBindingMaintenanceService conceptDomainBindingMaintenanceService;
	
	private final static UrlTemplateBinder<ConceptDomainBinding> URL_BINDER =
			new UrlTemplateBinder<ConceptDomainBinding>(){

		@Override
		public Map<String,String> getPathValues(ConceptDomainBinding resource) {
			Map<String,String> returnMap = new HashMap<String,String>();
			//TODO
			return null;
		}

	};
	
	private final static MessageFactory<ConceptDomainBinding> MESSAGE_FACTORY = 
			new MessageFactory<ConceptDomainBinding>() {

		@Override
		public Message createMessage(ConceptDomainBinding resource) {
			ConceptDomainBindingMsg msg = new ConceptDomainBindingMsg();
			msg.setConceptDomainBinding(resource);

			return msg;
		}
	};
	/**
	 * Creates the concept domain binding.
	 *
	 * @param httpServletRequest the http servlet request
	 * @param conceptDomainBinding the concept domain binding
	 * @param changeseturi the changeseturi
	 * @param codeSystemName the code system name
	 * @param conceptDomainBindingName the concept domain binding name
	 */
	@RequestMapping(value=PATH_CONCEPTDOMAINBINDING, method=RequestMethod.POST)
	@ResponseBody
	public void createConceptDomainBinding(
			HttpServletRequest httpServletRequest,
			@RequestBody ConceptDomainBinding conceptDomainBinding,
			@RequestParam(required=false) String changeseturi) {
			
		ChangeableResourceChoice choice = new ChangeableResourceChoice();
		choice.setConceptDomainBinding(conceptDomainBinding);
		
		this.getCreateHandler().create(
				choice, 
				changeseturi,
				PATH_CONCEPTDOMAINBINDING_OF_CONCEPTDOMAIN_BYID, 
				URL_BINDER, 
				this.conceptDomainBindingMaintenanceService);
	}
	
	@RequestMapping(value=PATH_CONCEPTDOMAINBINDING_OF_CONCEPTDOMAIN_BYID, method=RequestMethod.PUT)
	@ResponseBody
	public void modifyConceptDomainBinding(
			HttpServletRequest httpServletRequest,
			@RequestBody ConceptDomainBinding conceptDomainBinding,
			@RequestParam(required=false) String changeseturi,
			@PathVariable(VAR_CONCEPTDOMAINID) String codeSystemName,
			@PathVariable(VAR_CONCEPTDOMAINBINDINGID) String conceptDomainBindingUri) {
			
		ChangeableResourceChoice choice = new ChangeableResourceChoice();
		choice.setConceptDomainBinding(conceptDomainBinding);
		
		this.getCreateHandler().create(
				choice, 
				changeseturi,
				PATH_CONCEPTDOMAINBINDING_OF_CONCEPTDOMAIN_BYID, 
				URL_BINDER, 
				this.conceptDomainBindingMaintenanceService);
	}
	/**
	 * Gets the concept domain bindings of concept domain.
	 *
	 * @param httpServletRequest the http servlet request
	 * @param restrictions the restrictions
	 * @param resolvedFilter the filter
	 * @param page the page
	 * @param conceptDomainName the concept domain name
	 * @return the concept domain bindings of concept domain
	 */
	@RequestMapping(value={
			PATH_CONCEPTDOMAINBINDINGS_OF_CONCEPTDOMAIN}, method=RequestMethod.GET)
	@ResponseBody
	public ConceptDomainBindingDirectory getConceptDomainBindingsOfConceptDomain(
			HttpServletRequest httpServletRequest,
			ConceptDomainBindingQueryServiceRestrictions restrictions,
			RestFilter restFilter,
			Page page,
			@PathVariable(VAR_CONCEPTDOMAINID) String conceptDomainName) {
	
		return this.getConceptDomainBindingsOfConceptDomain(
				httpServletRequest, 
				null, 
				restrictions, 
				restFilter, 
				page, 
				conceptDomainName);
	}
	
	/**
	 * Gets the concept domain bindings of concept domain.
	 *
	 * @param httpServletRequest the http servlet request
	 * @param query the query
	 * @param restrictions the restrictions
	 * @param resolvedFilter the filter
	 * @param page the page
	 * @param conceptDomainName the concept domain name
	 * @return the concept domain bindings of concept domain
	 */
	@RequestMapping(value={
			PATH_CONCEPTDOMAINBINDINGS_OF_CONCEPTDOMAIN}, method=RequestMethod.GET)
	@ResponseBody
	public ConceptDomainBindingDirectory getConceptDomainBindingsOfConceptDomain(
			HttpServletRequest httpServletRequest,
			@RequestBody Query query,
			ConceptDomainBindingQueryServiceRestrictions restrictions,
			RestFilter restFilter,
			Page page,
			@PathVariable(VAR_CONCEPTDOMAINID) String conceptDomainName) {
		
		restrictions.setConceptdomain(conceptDomainName);
		
		return this.getConceptDomainBindings(
				httpServletRequest,
				query,
				restrictions,
				restFilter,
				page);
	}
	
	/**
	 * Does concept domain binding exist.
	 *
	 * @param httpServletResponse the http servlet response
	 * @param conceptDomainName the concept domain name
	 * @param conceptDomainBindingName the concept domain binding name
	 */
	@RequestMapping(value=PATH_CONCEPTDOMAINBINDING_OF_CONCEPTDOMAIN_BYID, method=RequestMethod.HEAD)
	@ResponseBody
	public void doesConceptDomainBindingExist(
			HttpServletResponse httpServletResponse,
			@PathVariable(VAR_CONCEPTDOMAINID) String conceptDomainName,
			@PathVariable(VAR_CONCEPTDOMAINBINDINGID) String conceptDomainBindingName) {
		
		//TODO:
	}
	
	/**
	 * Gets the concept domain bindings of code system count.
	 *
	 * @param httpServletResponse the http servlet response
	 * @param query the query
	 * @param restrictions the restrictions
	 * @param resolvedFilter the filter
	 * @param conceptDomainName the concept domain name
	 * @return the concept domain bindings of code system count
	 */
	@RequestMapping(value={
			PATH_CONCEPTDOMAINBINDINGS_OF_CONCEPTDOMAIN}, method=RequestMethod.HEAD)
	@ResponseBody
	public void getConceptDomainBindingsOfConceptDomainCount(
			HttpServletResponse httpServletResponse,
			@RequestBody Query query,
			ConceptDomainBindingQueryServiceRestrictions restrictions,
			RestFilter restFilter,
			@PathVariable(VAR_CONCEPTDOMAINID) String conceptDomainName) {
		
		restrictions.setConceptdomain(conceptDomainName);
		
		this.getConceptDomainBindingsCount(
				httpServletResponse, 
				query, 
				restrictions,
				restFilter);
	}

	/**
	 * Gets the concept domain bindings.
	 *
	 * @param httpServletRequest the http servlet request
	 * @param restrictions the restrictions
	 * @param resolvedFilter the filter
	 * @param page the page
	 * @return the concept domain bindings
	 */
	@RequestMapping(value={
			PATH_CONCEPTDOMAINBINDINGS}, method=RequestMethod.POST)
	@ResponseBody
	public ConceptDomainBindingDirectory getConceptDomainBindings(
			HttpServletRequest httpServletRequest,
			ConceptDomainBindingQueryServiceRestrictions restrictions,
			RestFilter restFilter,
			Page page) {
		
		return this.getConceptDomainBindings(
				httpServletRequest, 
				null, 
				restrictions, 
				restFilter, 
				page);
	}
	
	/**
	 * Gets the concept domain bindings.
	 *
	 * @param httpServletRequest the http servlet request
	 * @param query the query
	 * @param restrictions the restrictions
	 * @param resolvedFilter the filter
	 * @param page the page
	 * @return the concept domain bindings
	 */
	@RequestMapping(value={
			PATH_CONCEPTDOMAINBINDINGS}, method=RequestMethod.POST)
	@ResponseBody
	public ConceptDomainBindingDirectory getConceptDomainBindings(
			HttpServletRequest httpServletRequest,
			Query query,
			ConceptDomainBindingQueryServiceRestrictions restrictions,
			RestFilter restFilter,
			Page page) {
		
		ResolvedFilter filterComponent = this.processFilter(restFilter, this.conceptDomainBindingQueryService);
		
		DirectoryResult<ConceptDomainBindingDirectoryEntry> directoryResult = 
			this.conceptDomainBindingQueryService.getResourceSummaries(query, createSet(filterComponent), restrictions, null, page);
		
		ConceptDomainBindingDirectory directory = this.populateDirectory(
				directoryResult, 
				page, 
				httpServletRequest, 
				ConceptDomainBindingDirectory.class);
		
		return directory;
	}
	
	/**
	 * Gets the concept domain bindings count.
	 *
	 * @param httpServletResponse the http servlet response
	 * @param query the query
	 * @param restrictions the restrictions
	 * @param resolvedFilter the filter
	 * @return the concept domain bindings count
	 */
	@RequestMapping(value={
			PATH_CONCEPTDOMAINBINDINGS}, method=RequestMethod.HEAD)
	@ResponseBody
	public void getConceptDomainBindingsCount(
			HttpServletResponse httpServletResponse,
			Query query,
			ConceptDomainBindingQueryServiceRestrictions restrictions,
			RestFilter restFilter) {
	
		ResolvedFilter filterComponent = this.processFilter(restFilter, this.conceptDomainBindingQueryService);
		
		int count = this.conceptDomainBindingQueryService.count(query, createSet(filterComponent), restrictions);
		
		this.setCount(count, httpServletResponse);
	}
	
	/**
	 * Gets the concept domain binding by name.
	 *
	 * @param httpServletRequest the http servlet request
	 * @param conceptDomainName the concept domain name
	 * @param conceptDomainBindingName the concept domain binding name
	 * @return 
	 * @return the concept domain binding by name
	 */
	@RequestMapping(value={	
			PATH_CONCEPTDOMAINBINDING_OF_CONCEPTDOMAIN_BYID
			},
		method=RequestMethod.GET)
	@ResponseBody
	public Message getConceptDomainBindingByUri(
			HttpServletRequest httpServletRequest,
			RestReadContext restReadContext,
			@PathVariable(VAR_CONCEPTDOMAINID) String conceptDomainName,
			@PathVariable(VAR_CONCEPTDOMAINBINDINGID) String conceptDomainBindingUri) {
		
		return this.doRead(
				httpServletRequest, 
				MESSAGE_FACTORY, 
				this.conceptDomainBindingReadService, 
				restReadContext,
				UnknownConceptDomainBinding.class,
				conceptDomainBindingUri);
	}
}