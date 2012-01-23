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

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import edu.mayo.cts2.framework.model.command.Page;
import edu.mayo.cts2.framework.model.core.Directory;
import edu.mayo.cts2.framework.model.core.Message;
import edu.mayo.cts2.framework.model.extension.LocalIdValueSetDefinition;
import edu.mayo.cts2.framework.model.service.core.Query;
import edu.mayo.cts2.framework.model.service.exception.UnknownValueSetDefinition;
import edu.mayo.cts2.framework.model.util.ModelUtils;
import edu.mayo.cts2.framework.model.valuesetdefinition.ValueSetDefinition;
import edu.mayo.cts2.framework.model.valuesetdefinition.ValueSetDefinitionDirectory;
import edu.mayo.cts2.framework.model.valuesetdefinition.ValueSetDefinitionList;
import edu.mayo.cts2.framework.model.valuesetdefinition.ValueSetDefinitionMsg;
import edu.mayo.cts2.framework.service.command.restriction.ValueSetDefinitionQueryServiceRestrictions;
import edu.mayo.cts2.framework.service.profile.valuesetdefinition.ValueSetDefinitionMaintenanceService;
import edu.mayo.cts2.framework.service.profile.valuesetdefinition.ValueSetDefinitionQuery;
import edu.mayo.cts2.framework.service.profile.valuesetdefinition.ValueSetDefinitionQueryService;
import edu.mayo.cts2.framework.service.profile.valuesetdefinition.ValueSetDefinitionReadService;
import edu.mayo.cts2.framework.service.profile.valuesetdefinition.ValueSetDefinitionResolutionService;
import edu.mayo.cts2.framework.service.profile.valuesetdefinition.name.ValueSetDefinitionReadId;
import edu.mayo.cts2.framework.webapp.rest.command.RestFilter;
import edu.mayo.cts2.framework.webapp.rest.command.RestReadContext;
import edu.mayo.cts2.framework.webapp.rest.query.ValueSetDefinitionQueryBuilder;

/**
 * The Class ValueSetDefinitionController.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Controller
public class ValueSetDefinitionController extends AbstractServiceAwareController {

	@Cts2Service
	private ValueSetDefinitionQueryService valueSetDefinitionQueryService;
	
	@Cts2Service
	private ValueSetDefinitionReadService valueSetDefinitionReadService;
	
	@Cts2Service
	private ValueSetDefinitionMaintenanceService valueSetDefinitionMaintenanceService;
	
	@Cts2Service
	private ValueSetDefinitionResolutionService valueSetDefinitionResolutionService;

	private final static UrlTemplateBinder<LocalIdValueSetDefinition> URL_BINDER =
			new UrlTemplateBinder<LocalIdValueSetDefinition>(){

		@Override
		public Map<String,String> getPathValues(LocalIdValueSetDefinition resource) {
			Map<String,String> returnMap = new HashMap<String,String>();
			
			returnMap.put(VAR_VALUESETID, resource.getResource().getDefinedValueSet().getContent());
			returnMap.put(VAR_VALUESETDEFINITIONID, resource.getLocalID());

			return returnMap;
		}

	};

	final static MessageFactory<LocalIdValueSetDefinition> MESSAGE_FACTORY = 
			new MessageFactory<LocalIdValueSetDefinition>() {

		@Override
		public Message createMessage(LocalIdValueSetDefinition resource) {
			ValueSetDefinitionMsg msg = new ValueSetDefinitionMsg();
			msg.setValueSetDefinition(resource.getResource());

			return msg;
		}
	};

	/**
	 * Gets the value set definitions of value set.
	 *
	 * @param httpServletRequest the http servlet request
	 * @param restrictions the restrictions
	 * @param resolvedFilter the filter
	 * @param page the page
	 * @param valueSetName the value set name
	 * @return the value set definitions of value set
	 */
	@RequestMapping(value={
			PATH_VALUESETDEFINITIONS_OF_VALUESET}, method=RequestMethod.GET)
	@ResponseBody
	public Directory getValueSetDefinitionsOfValueSet(
			HttpServletRequest httpServletRequest,
			RestReadContext restReadContext,
			ValueSetDefinitionQueryServiceRestrictions restrictions,
			RestFilter restFilter,
			Page page,
			boolean list,
			@PathVariable(VAR_VALUESETID) String valueSet) {
		
		return this.getValueSetDefinitionsOfValueSet(
				httpServletRequest, 
				restReadContext,
				null,
				restrictions,
				restFilter,
				page, 
				list,
				valueSet);
	}
	
	/**
	 * Gets the value set definitions of value set.
	 *
	 * @param httpServletRequest the http servlet request
	 * @param query the query
	 * @param restrictions the restrictions
	 * @param resolvedFilter the filter
	 * @param page the page
	 * @param valueSetName the value set name
	 * @return the value set definitions of value set
	 */
	@RequestMapping(value={
			PATH_VALUESETDEFINITIONS_OF_VALUESET}, method=RequestMethod.POST)
	@ResponseBody
	public Directory getValueSetDefinitionsOfValueSet(
			HttpServletRequest httpServletRequest,
			RestReadContext restReadContext,
			@RequestBody Query query,
			ValueSetDefinitionQueryServiceRestrictions restrictions,
			RestFilter restFilter,
			Page page,
			boolean list,
			@PathVariable(VAR_VALUESETID) String valueSet) {
		
		restrictions.setValueSet(ModelUtils.nameOrUriFromEither(valueSet));
		
		return this.getValueSetDefinitions(
				httpServletRequest, 
				restReadContext,
				query, 
				restrictions, 
				restFilter,
				page,
				list);
	}
	
	/**
	 * Does value set definition exist.
	 *
	 * @param httpServletResponse the http servlet response
	 * @param valueSetName the value set name
	 * @param valueSetDefinitionDocumentUri the value set definition document uri
	 */
	@RequestMapping(value=PATH_VALUESETDEFINITION_OF_VALUESET_BYID, method=RequestMethod.HEAD)
	@ResponseBody
	public void doesValueSetDefinitionExist(
			HttpServletResponse httpServletResponse,
			@PathVariable(VAR_VALUESETID) String valueSetName,
			@PathVariable(VAR_VALUESETDEFINITIONID) String valueSetDefinitionDocumentUri) {
		
		//
	}
	
	/**
	 * Gets the value set definitions of value set count.
	 *
	 * @param httpServletResponse the http servlet response
	 * @param query the query
	 * @param restrictions the restrictions
	 * @param resolvedFilter the filter
	 * @param valueSetName the value set name
	 * @return the value set definitions of value set count
	 */
	@RequestMapping(value={
			PATH_VALUESETDEFINITIONS_OF_VALUESET}, method=RequestMethod.HEAD)
	@ResponseBody
	public void getValueSetDefinitionsOfValueSetCount(
			HttpServletResponse httpServletResponse,
			RestReadContext restReadContext,
			ValueSetDefinitionQueryServiceRestrictions restrictions,
			RestFilter restFilter,
			@PathVariable(VAR_VALUESETID) String valueSet) {
		
		restrictions.setValueSet(ModelUtils.nameOrUriFromEither(valueSet));
		
		this.getValueSetDefinitionsCount(
				httpServletResponse,
				restReadContext,
				restrictions, 
				restFilter);
	}
	
	@RequestMapping(value={
			PATH_VALUESETDEFINITIONS}, method=RequestMethod.GET)
	@ResponseBody
	public Directory getValueSetDefinitions(
			HttpServletRequest httpServletRequest,
			RestReadContext restReadContext,
			ValueSetDefinitionQueryServiceRestrictions restrictions,
			RestFilter restFilter,
			Page page,
			boolean list) {
		
		return this.getValueSetDefinitions(
				httpServletRequest,
				restReadContext,
				null,
				restrictions,
				restFilter,
				page,
				list);
	}
	
	/**
	 * Gets the value set definitions.
	 *
	 * @param httpServletRequest the http servlet request
	 * @param query the query
	 * @param restrictions the restrictions
	 * @param resolvedFilter the filter
	 * @param page the page
	 * @return the value set definitions
	 */
	@RequestMapping(value={
			PATH_VALUESETDEFINITIONS}, method=RequestMethod.POST)
	@ResponseBody
	public Directory getValueSetDefinitions(
			HttpServletRequest httpServletRequest,
			RestReadContext restReadContext,
			@RequestBody Query query,
			ValueSetDefinitionQueryServiceRestrictions restrictions,
			RestFilter restFilter,
			Page page,
			boolean list) {
		
		ValueSetDefinitionQueryBuilder builder = this.getNewResourceQueryBuilder();
		
		ValueSetDefinitionQuery resourceQuery = builder.
				addQuery(query).
				addRestFilter(restFilter).
				addRestrictions(restrictions).
				addRestReadContext(restReadContext).
				build();
		
		return this.doQuery(
				httpServletRequest,
				list, 
				this.valueSetDefinitionQueryService,
				resourceQuery,
				page, 
				null,//TODO: Sort not yet supported 
				ValueSetDefinitionDirectory.class, 
				ValueSetDefinitionList.class);
	}
	
	/**
	 * Gets the value set definitions count.
	 *
	 * @param httpServletResponse the http servlet response
	 * @param query the query
	 * @param restrictions the restrictions
	 * @param resolvedFilter the filter
	 * @return the value set definitions count
	 */
	@RequestMapping(value={
			PATH_VALUESETDEFINITIONS}, method=RequestMethod.HEAD)
	@ResponseBody
	public void getValueSetDefinitionsCount(
			HttpServletResponse httpServletResponse,
			RestReadContext restReadContext,
			ValueSetDefinitionQueryServiceRestrictions restrictions,
			RestFilter restFilter) {
		
		ValueSetDefinitionQueryBuilder builder = this.getNewResourceQueryBuilder();
		
		ValueSetDefinitionQuery resourceQuery = builder.
				addRestFilter(restFilter).
				addRestrictions(restrictions).
				addRestReadContext(restReadContext).
				build();
		
		int count =
			this.valueSetDefinitionQueryService.count(resourceQuery);
		
		this.setCount(count, httpServletResponse);
	}
	
	/**
	 * Gets the value set definition by name.
	 *
	 * @param httpServletRequest the http servlet request
	 * @param valueSetName the value set name
	 * @param valueSetDefinitionDocumentUri the value set definition document uri
	 * @return the value set definition by name
	 */
	@RequestMapping(value={	
			PATH_VALUESETDEFINITION_BYURI
			},
		method=RequestMethod.GET)
	public ModelAndView getValueSetDefinitionByUri(
			HttpServletRequest httpServletRequest,
			RestReadContext restReadContext,
			@RequestParam(PARAM_URI) String uri,
			@RequestParam(value="redirect", defaultValue="false") boolean redirect) {
		
		ValueSetDefinitionReadId id = new ValueSetDefinitionReadId(uri);
	
		return this.doReadByUri(
				httpServletRequest, 
				MESSAGE_FACTORY, 
				PATH_VALUESETDEFINITION_BYURI, 
				PATH_VALUESETDEFINITION_OF_VALUESET_BYID, 
				URL_BINDER, 
				this.valueSetDefinitionReadService,
				restReadContext,
				UnknownValueSetDefinition.class,
				id, 
				redirect);
	}
	
	@RequestMapping(value={	
			PATH_VALUESETDEFINITION_OF_VALUESET_BYID
			},
		method=RequestMethod.GET)
	@ResponseBody
	public Message getValueSetDefinitionByLocalId(
			HttpServletRequest httpServletRequest,
			RestReadContext restReadContext,
			@PathVariable(VAR_VALUESETID) String valueSetName,
			@PathVariable(VAR_VALUESETDEFINITIONID) String definitionLocalId) {
		
		ValueSetDefinitionReadId id = 
				new ValueSetDefinitionReadId(
						definitionLocalId,
						ModelUtils.nameOrUriFromName(valueSetName));
		
		return this.doRead(
				httpServletRequest, 
				MESSAGE_FACTORY, 
				this.valueSetDefinitionReadService, 
				restReadContext, 
				UnknownValueSetDefinition.class, 
				id);
	}
	
	/**
	 * Creates the value set definition.
	 *
	 * @param httpServletRequest the http servlet request
	 * @param valueSetDefinition the value set definition
	 * @param changeseturi the changeseturi
	 * @param valueSetName the value set name
	 * @param valueSetDefinitionDocumentUri the value set definition document uri
	 */
	@RequestMapping(value=PATH_VALUESETDEFINITION_OF_VALUESET_BYID, method=RequestMethod.PUT)
	@ResponseBody
	public void updateValueSetDefinition(
			HttpServletRequest httpServletRequest,
			@RequestBody ValueSetDefinition valueSetDefinition,
			@RequestParam(value=PARAM_CHANGESETCONTEXT, required=false) String changeseturi,
			@PathVariable(VAR_VALUESETID) String valueSetName,
			@PathVariable(VAR_VALUESETDEFINITIONID) String valueSetDefinitionLocalId) {
			
		this.getUpdateHandler().update(
				new LocalIdValueSetDefinition(valueSetDefinitionLocalId, valueSetDefinition),
				changeseturi, 
				new ValueSetDefinitionReadId(
						valueSetDefinitionLocalId, 
						ModelUtils.nameOrUriFromName(valueSetName)),
				this.valueSetDefinitionMaintenanceService);
	}
	
	@RequestMapping(value=PATH_VALUESETDEFINITION, method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Void> createValueSetDefinition(
			HttpServletRequest httpServletRequest,
			@RequestBody ValueSetDefinition valueSetDefinition,
			@RequestParam(value=PARAM_CHANGESETCONTEXT, required=false) String changeseturi) {
	
		return this.getCreateHandler().create(
				valueSetDefinition,
				changeseturi, 
				PATH_VALUESETDEFINITION_OF_VALUESET_BYID, 
				URL_BINDER, 
				this.valueSetDefinitionMaintenanceService);
	}
	
	@RequestMapping(value=PATH_VALUESETDEFINITION_OF_VALUESET_BYID, method=RequestMethod.DELETE)
	@ResponseBody
	public void deleteValueSetDefinition(
			HttpServletRequest httpServletRequest,
			RestReadContext restReadContext,
			@PathVariable(VAR_VALUESETID) String valueSetName,
			@PathVariable(VAR_VALUESETDEFINITIONID) String valueSetDefinitionLocalId,
			@RequestParam(PARAM_CHANGESETCONTEXT) String changeseturi) {
		
		ValueSetDefinitionReadId id = 
				new ValueSetDefinitionReadId(
						valueSetDefinitionLocalId,
						ModelUtils.nameOrUriFromName(valueSetName));
			
		this.valueSetDefinitionMaintenanceService.
			deleteResource(id, changeseturi);
	}
	
	private ValueSetDefinitionQueryBuilder getNewResourceQueryBuilder(){
		return new ValueSetDefinitionQueryBuilder(
			this.valueSetDefinitionQueryService, 
			this.getFilterResolver(),
			this.getReadContextResolver());
	}

	public ValueSetDefinitionQueryService getValueSetDefinitionQueryService() {
		return valueSetDefinitionQueryService;
	}

	public void setValueSetDefinitionQueryService(
			ValueSetDefinitionQueryService valueSetDefinitionQueryService) {
		this.valueSetDefinitionQueryService = valueSetDefinitionQueryService;
	}

	public ValueSetDefinitionReadService getValueSetDefinitionReadService() {
		return valueSetDefinitionReadService;
	}

	public void setValueSetDefinitionReadService(
			ValueSetDefinitionReadService valueSetDefinitionReadService) {
		this.valueSetDefinitionReadService = valueSetDefinitionReadService;
	}

	public ValueSetDefinitionMaintenanceService getValueSetDefinitionMaintenanceService() {
		return valueSetDefinitionMaintenanceService;
	}

	public void setValueSetDefinitionMaintenanceService(
			ValueSetDefinitionMaintenanceService valueSetDefinitionMaintenanceService) {
		this.valueSetDefinitionMaintenanceService = valueSetDefinitionMaintenanceService;
	}

	public ValueSetDefinitionResolutionService getValueSetDefinitionResolutionService() {
		return valueSetDefinitionResolutionService;
	}

	public void setValueSetDefinitionResolutionService(
			ValueSetDefinitionResolutionService valueSetDefinitionResolutionService) {
		this.valueSetDefinitionResolutionService = valueSetDefinitionResolutionService;
	}
}