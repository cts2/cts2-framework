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

import org.apache.commons.lang.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import edu.mayo.cts2.framework.model.command.Page;
import edu.mayo.cts2.framework.model.conceptdomainbinding.ConceptDomainBinding;
import edu.mayo.cts2.framework.model.conceptdomainbinding.ConceptDomainBindingDirectory;
import edu.mayo.cts2.framework.model.conceptdomainbinding.ConceptDomainBindingList;
import edu.mayo.cts2.framework.model.conceptdomainbinding.ConceptDomainBindingMsg;
import edu.mayo.cts2.framework.model.core.Directory;
import edu.mayo.cts2.framework.model.core.Message;
import edu.mayo.cts2.framework.model.extension.LocalIdConceptDomainBinding;
import edu.mayo.cts2.framework.model.service.core.Query;
import edu.mayo.cts2.framework.model.service.exception.UnknownConceptDomainBinding;
import edu.mayo.cts2.framework.model.util.ModelUtils;
import edu.mayo.cts2.framework.service.command.restriction.ConceptDomainBindingQueryServiceRestrictions;
import edu.mayo.cts2.framework.service.profile.conceptdomainbinding.ConceptDomainBindingMaintenanceService;
import edu.mayo.cts2.framework.service.profile.conceptdomainbinding.ConceptDomainBindingQuery;
import edu.mayo.cts2.framework.service.profile.conceptdomainbinding.ConceptDomainBindingQueryService;
import edu.mayo.cts2.framework.service.profile.conceptdomainbinding.ConceptDomainBindingReadService;
import edu.mayo.cts2.framework.service.profile.conceptdomainbinding.name.ConceptDomainBindingReadId;
import edu.mayo.cts2.framework.webapp.rest.command.RestFilter;
import edu.mayo.cts2.framework.webapp.rest.command.RestReadContext;
import edu.mayo.cts2.framework.webapp.rest.query.ConceptDomainBindingQueryBuilder;

/**
 * The Class ConceptDomainBindingController.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Controller
public class ConceptDomainBindingController extends AbstractMessageWrappingController {

	@Cts2Service
	private ConceptDomainBindingReadService conceptDomainBindingReadService;
	
	@Cts2Service
	private ConceptDomainBindingQueryService conceptDomainBindingQueryService;
	
	@Cts2Service
	private ConceptDomainBindingMaintenanceService conceptDomainBindingMaintenanceService;

	private final static UrlTemplateBinder<LocalIdConceptDomainBinding> URL_BINDER =
			new UrlTemplateBinder<LocalIdConceptDomainBinding>(){

		@Override
		public Map<String,String> getPathValues(LocalIdConceptDomainBinding resource) {
			Map<String,String> returnMap = new HashMap<String,String>();
			
			returnMap.put(VAR_CONCEPTDOMAINBINDINGID, resource.getLocalID());
			returnMap.put(VAR_CONCEPTDOMAINID, resource.getResource().getBindingFor().getContent());
			
			return returnMap;
		}

	};
	
	private final static MessageFactory<LocalIdConceptDomainBinding> MESSAGE_FACTORY = 
			new MessageFactory<LocalIdConceptDomainBinding>() {

		@Override
		public Message createMessage(LocalIdConceptDomainBinding resource) {
			ConceptDomainBindingMsg msg = new ConceptDomainBindingMsg();
			msg.setConceptDomainBinding(resource.getResource());

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
	 * @return 
	 */
	@RequestMapping(value=PATH_CONCEPTDOMAINBINDING, method=RequestMethod.POST)
	public ResponseEntity<Void> createConceptDomainBinding(
			HttpServletRequest httpServletRequest,
			@RequestBody ConceptDomainBinding conceptDomainBinding,
			@RequestParam(value=PARAM_CHANGESETCONTEXT, required=false) String changeseturi) {
				
		return this.getCreateHandler().create(
				conceptDomainBinding, 
				changeseturi,
				PATH_CONCEPTDOMAINBINDING_OF_CONCEPTDOMAIN_BYID, 
				URL_BINDER, 
				this.conceptDomainBindingMaintenanceService);
	}
	
	@RequestMapping(value=PATH_CONCEPTDOMAINBINDING_OF_CONCEPTDOMAIN_BYID, method=RequestMethod.PUT)
	@ResponseBody
	public void updateConceptDomainBinding(
			HttpServletRequest httpServletRequest,
			@RequestBody ConceptDomainBinding conceptDomainBinding,
			@PathVariable(VAR_CONCEPTDOMAINID) String conceptDomainName,
			@PathVariable(VAR_CONCEPTDOMAINBINDINGID) String conceptDomainBindingLocalId,
			@RequestParam(value=PARAM_CHANGESETCONTEXT, required=false) String changeseturi) {

		this.getUpdateHandler().update(
				new LocalIdConceptDomainBinding(conceptDomainBindingLocalId, conceptDomainBinding),
				changeseturi, 
				new ConceptDomainBindingReadId(
						conceptDomainBindingLocalId, 
						ModelUtils.nameOrUriFromName(conceptDomainName)),
				this.conceptDomainBindingMaintenanceService);
	}
	
	@RequestMapping(value=PATH_CONCEPTDOMAINBINDING_OF_CONCEPTDOMAIN_BYID, method=RequestMethod.DELETE)
	@ResponseBody
	public void deleteCodeSystem(
			HttpServletRequest httpServletRequest,
			@PathVariable(VAR_CONCEPTDOMAINID) String conceptDomainName,
			@PathVariable(VAR_CONCEPTDOMAINBINDINGID) String conceptDomainBindingLocalId,
			@RequestParam(PARAM_CHANGESETCONTEXT) String changeseturi) {
		
		ConceptDomainBindingReadId id = 
				new ConceptDomainBindingReadId(
						conceptDomainBindingLocalId,
						ModelUtils.nameOrUriFromName(conceptDomainName));

		this.conceptDomainBindingMaintenanceService.
			deleteResource(id, changeseturi);
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
	public Directory getConceptDomainBindingsOfConceptDomain(
			HttpServletRequest httpServletRequest,
			RestReadContext restReadContext,
			ConceptDomainBindingQueryServiceRestrictions restrictions,
			RestFilter restFilter,
			Page page,
			boolean list,
			@PathVariable(VAR_CONCEPTDOMAINID) String conceptDomain) {
	
		return this.getConceptDomainBindingsOfConceptDomain(
				httpServletRequest, 
				restReadContext,
				null, 
				restrictions, 
				restFilter, 
				page, 
				list,
				conceptDomain);
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
			PATH_CONCEPTDOMAINBINDINGS_OF_CONCEPTDOMAIN}, method=RequestMethod.POST)
	@ResponseBody
	public Directory getConceptDomainBindingsOfConceptDomain(
			HttpServletRequest httpServletRequest,
			RestReadContext restReadContext,
			@RequestBody Query query,
			ConceptDomainBindingQueryServiceRestrictions restrictions,
			RestFilter restFilter,
			Page page,
			boolean list,
			@PathVariable(VAR_CONCEPTDOMAINID) String conceptDomain) {
		
		restrictions.setConceptDomain(ModelUtils.nameOrUriFromEither(conceptDomain));
		
		return this.getConceptDomainBindings(
				httpServletRequest,
				restReadContext,
				query,
				restrictions,
				restFilter,
				page,
				list);
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
			RestReadContext restReadContext,
			ConceptDomainBindingQueryServiceRestrictions restrictions,
			RestFilter restFilter,
			@PathVariable(VAR_CONCEPTDOMAINID) String conceptDomain) {
		
		restrictions.setConceptDomain(ModelUtils.nameOrUriFromEither(conceptDomain));
		
		this.getConceptDomainBindingsCount(
				httpServletResponse, 
				restReadContext, 
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
			PATH_CONCEPTDOMAINBINDINGS}, method=RequestMethod.GET)
	@ResponseBody
	public Directory getConceptDomainBindings(
			HttpServletRequest httpServletRequest,
			RestReadContext restReadContext,
			ConceptDomainBindingQueryServiceRestrictions restrictions,
			RestFilter restFilter,
			Page page,
			boolean list) {
		
		return this.getConceptDomainBindings(
				httpServletRequest, 
				restReadContext,
				null, 
				restrictions, 
				restFilter, 
				page,
				list);
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
	public Directory getConceptDomainBindings(
			HttpServletRequest httpServletRequest,
			RestReadContext restReadContext,
			Query query,
			ConceptDomainBindingQueryServiceRestrictions restrictions,
			RestFilter restFilter,
			Page page,
			boolean list) {
		
		ConceptDomainBindingQueryBuilder builder = this.getNewResourceQueryBuilder();
		
		ConceptDomainBindingQuery resourceQuery = builder.
				addQuery(query).
				addRestFilter(restFilter).
				addRestReadContext(restReadContext).
				build();
		
		return this.doQuery(
				httpServletRequest,
				list, 
				this.conceptDomainBindingQueryService,
				resourceQuery,
				page, 
				null,//TODO: Sort not yet supported 
				ConceptDomainBindingDirectory.class, 
				ConceptDomainBindingList.class);
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
			RestReadContext restReadContext,
			ConceptDomainBindingQueryServiceRestrictions restrictions,
			RestFilter restFilter) {
	
		ConceptDomainBindingQueryBuilder builder = this.getNewResourceQueryBuilder();
		
		ConceptDomainBindingQuery resourceQuery = builder.
				addRestFilter(restFilter).
				addRestReadContext(restReadContext).
				build();
		
		int count = this.conceptDomainBindingQueryService.count(resourceQuery);
		
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
			PATH_CONCEPTDOMAINBINDING_BYURI
			},
		method=RequestMethod.GET)
	public ModelAndView getConceptDomainBindingByUri(
			HttpServletRequest httpServletRequest,
			RestReadContext restReadContext,
			@RequestParam(PARAM_URI) String uri,
			@RequestParam(value="redirect", defaultValue="false") boolean redirect) {
		
		ConceptDomainBindingReadId id = new ConceptDomainBindingReadId(uri);
		
		return this.doReadByUri(
				httpServletRequest, 
				MESSAGE_FACTORY, 
				PATH_CONCEPTDOMAINBINDING_BYURI, 
				PATH_CONCEPTDOMAINBINDING_OF_CONCEPTDOMAIN_BYID, 
				URL_BINDER, 
				this.conceptDomainBindingReadService, 
				restReadContext,
				UnknownConceptDomainBinding.class, 
				id,
				redirect);
	}
	
	@RequestMapping(value={	
			PATH_CONCEPTDOMAINBINDING_OF_CONCEPTDOMAIN_BYID
			},
		method=RequestMethod.GET)
	public Object getConceptDomainBindingByLocalId(
			HttpServletRequest httpServletRequest,
			RestReadContext restReadContext,
			@PathVariable(VAR_CONCEPTDOMAINID) String conceptDomainName,
			@PathVariable(VAR_CONCEPTDOMAINBINDINGID) String bindingLocalId) {
		
		ConceptDomainBindingReadId id = 
				new ConceptDomainBindingReadId(
						bindingLocalId,
						ModelUtils.nameOrUriFromName(conceptDomainName));
		
		return this.doRead(
				httpServletRequest, 
				MESSAGE_FACTORY, 
				this.conceptDomainBindingReadService, 
				restReadContext, 
				UnknownConceptDomainBinding.class, 
				id);
	}
	
	@InitBinder
	public void initConceptDomainBindingRestrictionBinder(
			 WebDataBinder binder,
			 @RequestParam(value=PARAM_CONCEPTDOMAIN, required=false) String conceptdomain) {
		
		if(binder.getTarget() instanceof ConceptDomainBindingQueryServiceRestrictions){
			ConceptDomainBindingQueryServiceRestrictions restrictions = 
					(ConceptDomainBindingQueryServiceRestrictions) binder.getTarget();

			if(StringUtils.isNotBlank(conceptdomain)){
				restrictions.setConceptDomain(ModelUtils.nameOrUriFromEither(conceptdomain));
			}			
		}
	}
	
	private ConceptDomainBindingQueryBuilder getNewResourceQueryBuilder(){
		return new ConceptDomainBindingQueryBuilder(
			this.conceptDomainBindingQueryService, 
			this.getFilterResolver(),
			this.getReadContextResolver());
	}
}