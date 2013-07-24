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

import edu.mayo.cts2.framework.model.command.Page;
import edu.mayo.cts2.framework.model.command.ResolvedFilter;
import edu.mayo.cts2.framework.model.command.ResolvedReadContext;
import edu.mayo.cts2.framework.model.core.Directory;
import edu.mayo.cts2.framework.model.core.EntitySynopsis;
import edu.mayo.cts2.framework.model.core.Message;
import edu.mayo.cts2.framework.model.core.SortCriteria;
import edu.mayo.cts2.framework.model.directory.DirectoryResult;
import edu.mayo.cts2.framework.model.entity.EntityDirectory;
import edu.mayo.cts2.framework.model.entity.EntityDirectoryEntry;
import edu.mayo.cts2.framework.model.exception.ExceptionFactory;
import edu.mayo.cts2.framework.model.extension.LocalIdValueSetResolution;
import edu.mayo.cts2.framework.model.service.core.NameOrURI;
import edu.mayo.cts2.framework.model.service.core.Query;
import edu.mayo.cts2.framework.model.service.exception.UnknownResourceReference;
import edu.mayo.cts2.framework.model.service.exception.UnknownValueSetDefinition;
import edu.mayo.cts2.framework.model.util.ModelUtils;
import edu.mayo.cts2.framework.model.valuesetdefinition.*;
import edu.mayo.cts2.framework.service.command.restriction.ResolvedValueSetQueryServiceRestrictions;
import edu.mayo.cts2.framework.service.command.restriction.ResolvedValueSetResolutionEntityRestrictions;
import edu.mayo.cts2.framework.service.profile.resolvedvalueset.*;
import edu.mayo.cts2.framework.service.profile.resolvedvalueset.name.ResolvedValueSetReadId;
import edu.mayo.cts2.framework.service.profile.valuesetdefinition.ResolvedValueSetResolutionEntityQuery;
import edu.mayo.cts2.framework.service.profile.valuesetdefinition.ResolvedValueSetResult;
import edu.mayo.cts2.framework.service.profile.valuesetdefinition.ValueSetDefinitionResolutionService;
import edu.mayo.cts2.framework.service.profile.valuesetdefinition.name.ValueSetDefinitionReadId;
import edu.mayo.cts2.framework.webapp.rest.command.QueryControl;
import edu.mayo.cts2.framework.webapp.rest.command.RestFilter;
import edu.mayo.cts2.framework.webapp.rest.command.RestReadContext;
import edu.mayo.cts2.framework.webapp.rest.util.ControllerUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * An MVC Controller responsible for the resolution, retrieval, and maintenance of
 * {@link ValueSetDefinition}s and {@link ResolvedValueSet}s.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Controller
public class ValueSetDefinitionResolutionController extends AbstractMessageWrappingController {
	
	@Resource
	private UrlTemplateBindingCreator urlTemplateBindingCreator;

	@Cts2Service
	private ResolvedValueSetLoaderService resolvedValueSetLoaderService;
	
	@Cts2Service
	private ResolvedValueSetQueryService resolvedValueSetQueryService;
	
	@Cts2Service
	private ResolvedValueSetResolutionService resolvedValueSetResolutionService;
	
	@Cts2Service
	private ValueSetDefinitionResolutionService valueSetDefinitionResolutionService;
	
	final static MessageFactory<LocalIdValueSetResolution> MESSAGE_FACTORY = 
			new MessageFactory<LocalIdValueSetResolution>() {

		@Override
		public Message createMessage(LocalIdValueSetResolution resource) {
			ResolvedValueSetMsg msg = new ResolvedValueSetMsg();
			msg.setResolvedValueSet(resource.getResource());

			return msg;
		}
	};
	
	private final static UrlTemplateBinder<ResolvedValueSetReference> URL_BINDER =
			new UrlTemplateBinder<ResolvedValueSetReference>(){

		@Override
		public Map<String,String> getPathValues(ResolvedValueSetReference resource) {
			Map<String,String> returnMap = new HashMap<String,String>();
			
			returnMap.put(VAR_VALUESETID, 
					resource.getValueSetDefinitionReference().getValueSet().getContent());
			returnMap.put(VAR_VALUESETDEFINITIONID, 
					resource.getValueSetDefinitionReference().getValueSetDefinition().getContent());
			returnMap.put(VAR_RESOLVEDVALUESETID, 
					resource.getLocalID());

			return returnMap;
		}

	};

    /**
     * Gets the Entities resulting from the Resolution of a {$lnk ValueSetDefinition}.
     *
     * Example URL: {@code http://server.root/valueset/{id}/definition/entities}
     */
    @RequestMapping(value=PATH_RESOLUTION_OF_VALUESETDEFINITION_ENTITIES, method=RequestMethod.GET)
    @ResponseBody
    public Object getValueSetDefinitionResolutionEntities(
            HttpServletRequest httpServletRequest,
            QueryControl queryControl,
            RestReadContext restReadContext,
            ResolvedValueSetResolutionEntityRestrictions restrictions,
            RestFilter restFilter,
            @PathVariable(VAR_VALUESETID) String valueSetName,
            @PathVariable(VAR_VALUESETDEFINITIONID) String definitionLocalId,
            @RequestParam(value=PARAM_CODESYSTEMVERSION, required=false) List<String> codeSystemVersionIds,
            @RequestParam(value=PARAM_TAG, required=false) String tagName,
            Page page) {

        return this.doGetValueSetDefinitionResolution(
                httpServletRequest,
                queryControl,
                restReadContext,
                null,
                restrictions,
                restFilter,
                valueSetName,
                definitionLocalId,
                ValueSetDefinitionResolutionTypes.entitydirectory,
                codeSystemVersionIds,
                tagName,
                page);
    }

    /**
     * Dynamically resolves a {@link ValueSetDefinition}.
     *
     * NOTE: The 'complete' parameter dictates whether or not the ValueSetDefinition is resolved
     * to a {@link IteratableResolvedValueSet} (when false or omitted)
     * or a {@link ResolvedValueSet} (when true)
     *
     * Example URL (iterable): {@code http://server.root/valueset/{id}/definition/{defId}/resolution}
     * Example URL (complete): {@code http://server.root/valueset/{id}/definition/{defId}/resolution?complete=true}
     *
     * @see #getValueSetDefinitionResolutionEntities for resolving a ValueSetDefinition to Entities
     */
	@RequestMapping(value=PATH_RESOLUTION_OF_VALUESETDEFINITION, method=RequestMethod.GET)
	@ResponseBody
	public Object getValueSetDefinitionResolution(
			HttpServletRequest httpServletRequest,
			QueryControl queryControl,
			RestReadContext restReadContext,
			ResolvedValueSetResolutionEntityRestrictions restrictions,
			RestFilter restFilter,
			@PathVariable(VAR_VALUESETID) String valueSetName,
			@PathVariable(VAR_VALUESETDEFINITIONID) String definitionLocalId,
			@RequestParam(value=PARAM_CODESYSTEMVERSION, required=false) List<String> codeSystemVersionIds,
			@RequestParam(value=PARAM_TAG, required=false) String tagName,
            @RequestParam(value=PARAM_COMPLETE, required=false) boolean complete,
            Page page) {

        ValueSetDefinitionResolutionTypes resolution;
        if(complete){
            resolution = ValueSetDefinitionResolutionTypes.complete;
        } else {
            resolution = ValueSetDefinitionResolutionTypes.iterable;
        }

		return this.doGetValueSetDefinitionResolution(
				httpServletRequest,
				queryControl,
				restReadContext,
				null,
				restrictions, 
				restFilter,
				valueSetName, 
				definitionLocalId,
				resolution,
				codeSystemVersionIds, 
				tagName,
                page);
	}

    /**
     * Base method for resolving a {$link ValueSetDefinition} to either a {@link IteratableResolvedValueSet},
     * a {@link ResolvedValueSet}, or an {@link EntityDirectory}l
     */
	protected Object doGetValueSetDefinitionResolution(
			HttpServletRequest httpServletRequest,
			QueryControl queryControl,
			RestReadContext restReadContext,
			Query query,
			ResolvedValueSetResolutionEntityRestrictions restrictions,
			RestFilter restFilter,
		    String valueSetName,
			String definitionLocalId,
			ValueSetDefinitionResolutionTypes resolution,
			List<String> codeSystemVersionIds,
			String tagName,
			Page page) {
		
		ValueSetDefinitionReadId definitionId = 
				new ValueSetDefinitionReadId(
						definitionLocalId,
						ModelUtils.nameOrUriFromName(valueSetName));

		ResolvedReadContext readContext = this.resolveRestReadContext(restReadContext);
		
		Set<NameOrURI> codeSystemVersions = 
				ControllerUtils.idsToNameOrUriSet(codeSystemVersionIds);
		
		NameOrURI tag = ModelUtils.nameOrUriFromEither(tagName);
		
		switch (resolution) {
			case iterable : {
				ResolvedValueSetResolutionEntityQuery entityQuery = 
						this.getResolvedValueSetResolutionEntityQuery(query, restFilter, restrictions);
				
				ResolvedValueSetResult<EntitySynopsis> directory = this.valueSetDefinitionResolutionService.
						resolveDefinition(
								definitionId, 
								codeSystemVersions, 
								tag, 
								entityQuery,
								this.resolveSort(queryControl, this.valueSetDefinitionResolutionService), 
								readContext,
								page);

				if(directory == null){
					throw ExceptionFactory.createUnknownResourceException(
							definitionLocalId, UnknownValueSetDefinition.class);
				} else {
                    IteratableResolvedValueSet iterable = this.populateDirectory(
                            directory,
                            page,
                            httpServletRequest,
                            IteratableResolvedValueSet.class);

					iterable.setResolutionInfo(directory.getResolvedValueSetHeader());
					
					return iterable;
				}
			}
			case entitydirectory : {
				ResolvedValueSetResolutionEntityQuery entityQuery = 
						this.getResolvedValueSetResolutionEntityQuery(query, restFilter, restrictions);
				
				DirectoryResult<EntityDirectoryEntry> entityDirectory = this.valueSetDefinitionResolutionService.
						resolveDefinitionAsEntityDirectory(
								definitionId, 
								codeSystemVersions, 
								tag,
								entityQuery,
								this.resolveSort(queryControl, this.valueSetDefinitionResolutionService), 
								readContext, 
								page);
				
				return this.populateDirectory(
						entityDirectory, 
						page, 
						httpServletRequest, 
						EntityDirectory.class);
			}
			case complete : {
				ResolvedValueSet completeSet = this.valueSetDefinitionResolutionService.
					resolveDefinitionAsCompleteSet(
							definitionId, 
							codeSystemVersions, 
							tag, 
							readContext);
				
				ResolvedValueSetMsg msg = new ResolvedValueSetMsg();
				msg.setResolvedValueSet(completeSet);
				
				return this.wrapMessage(msg, httpServletRequest);
			}
			default : {
				throw new IllegalStateException();
			}
		}	
	}

    /**
     * Gets the Entities of a pre-resolved {@link ResolvedValueSet}.
     *
     * Example URL: {@code http://server.root/valueset/{id}/definition/{defId}/resolution/{resId}/entities}
     */
	@RequestMapping(value=PATH_RESOLVED_VALUESET_OF_VALUESETDEFINITION_BYID_ENTITIES, method=RequestMethod.GET)
	public Object getResolvedValueSetResolutionByLocalIdEntities(
			HttpServletRequest httpServletRequest,
			RestReadContext restReadContext,
			Query query,
			QueryControl queryControl,
			@PathVariable(VAR_VALUESETID) String valueSetName,
			@PathVariable(VAR_VALUESETDEFINITIONID) String definitionLocalId,
			@PathVariable(VAR_RESOLVEDVALUESETID) String resolvedValueSetLocalId,
			ResolvedValueSetResolutionEntityRestrictions restrictions,
			RestFilter restFilter,
			Page page) {
		
		ResolvedValueSetReadId id = new ResolvedValueSetReadId(
				resolvedValueSetLocalId,
				ModelUtils.nameOrUriFromName(valueSetName),
				ModelUtils.nameOrUriFromName(definitionLocalId));

		ResolvedFilter filter = this.getFilterResolver().resolveRestFilter(
				restFilter, this.resolvedValueSetResolutionService);

		Set<ResolvedFilter> filterSet = new HashSet<ResolvedFilter>();
		if (filter != null) {
			filterSet.add(filter);
		}

		SortCriteria sortCriteria = 
			this.resolveSort(queryControl, this.resolvedValueSetResolutionService);
		
		ResolvedValueSetResolutionEntityQuery entityQuery = 
			this.getResolvedValueSetResolutionEntityQuery(
					query, 
					filterSet, 
					restrictions);
		
		ResolvedValueSetResult<EntityDirectoryEntry> directory =
				this.resolvedValueSetResolutionService.getEntities(
						id, 
						entityQuery,
						sortCriteria, 
						page);

		if (directory == null) {
			throw ExceptionFactory.createUnknownResourceException(
					id.toString(), UnknownResourceReference.class);
		}

		Directory dir = 
				this.populateDirectory(
						directory, 
						page, 
						httpServletRequest, 
						EntityDirectory.class);
			
		return this.buildResponse(httpServletRequest, dir);
	}

    /**
     * Gets a {@link ResolvedValueSet} by its local identifier.
     *
     * Example URL: {@code http://server.root/valueset/{id}/definition/{defId}/resolution/{resId}}
     */
	@RequestMapping(value=PATH_RESOLVED_VALUESET_OF_VALUESETDEFINITION_BYID, method=RequestMethod.GET)
	public Object getResolvedValueSetResolutionByLocalId(
			HttpServletRequest httpServletRequest,
			RestReadContext restReadContext,
			@PathVariable(VAR_VALUESETID) String valueSetName,
			@PathVariable(VAR_VALUESETDEFINITIONID) String definitionLocalId,
			@PathVariable(VAR_RESOLVEDVALUESETID) String resolvedValueSetLocalId,
            @RequestParam(value=PARAM_COMPLETE, required=false) boolean complete,
			RestFilter restFilter, 
			Page page) {

		ResolvedValueSetReadId id = new ResolvedValueSetReadId(
				resolvedValueSetLocalId,
				ModelUtils.nameOrUriFromName(valueSetName),
				ModelUtils.nameOrUriFromName(definitionLocalId));

        if(complete){
            ResolvedValueSet resolvedValueSet =
                this.resolvedValueSetResolutionService.getResolution(id);

            if (resolvedValueSet == null) {
                throw ExceptionFactory.createUnknownResourceException(
                        id.toString(), UnknownResourceReference.class);
            } else {
                return resolvedValueSet;
            }
        } else {
            ResolvedFilter filter = this.getFilterResolver().resolveRestFilter(
                    restFilter, this.resolvedValueSetResolutionService);

            Set<ResolvedFilter> filterSet = new HashSet<ResolvedFilter>();
            if (filter != null) {
                filterSet.add(filter);
            }

            ResolvedValueSetResult<EntitySynopsis> directory =
                    this.resolvedValueSetResolutionService.
                        getResolution(
                            id,
                            filterSet,
                            page);

            if (directory == null) {
                throw ExceptionFactory.createUnknownResourceException(
                        id.toString(), UnknownResourceReference.class);
            }

            IteratableResolvedValueSet iterable = this.populateDirectory(directory,
                    page, httpServletRequest, IteratableResolvedValueSet.class);

            iterable.setResolutionInfo(directory.getResolvedValueSetHeader());

            return this.buildResponse(httpServletRequest, iterable);
        }
	}

    /**
     * Lists all pre-resolved {@link ResolvedValueSet}s for a given {@link ValueSetDefinition}.
     *
     * Example URL: {@code http://server.root/valueset/{id}/definition/{defId}/resolutions}
     */
	@RequestMapping(value=PATH_RESOLVED_VALUESETS_OF_VALUESETDEFINITION, method=RequestMethod.GET)
	public Object getResolvedValueSetsOfValueSetDefinition(
			HttpServletRequest httpServletRequest,
			ResolvedValueSetQueryServiceRestrictions restrictions,
			@PathVariable(VAR_VALUESETID) String valueSetName,
			@PathVariable(VAR_VALUESETDEFINITIONID) String definitionLocalId,
			RestFilter restFilter,
			Page page) {

		restrictions.getValueSets().add(ModelUtils.nameOrUriFromName(valueSetName));
		restrictions.getValueSetDefinitions().add(ModelUtils.nameOrUriFromName(definitionLocalId));
		
		return this.getResolvedValueSets(
				httpServletRequest, 
				restrictions,
				null,  
				restFilter, 
				page);
	}

    /**
     * Lists all pre-resolved {@link ResolvedValueSet}s.
     *
     * Example URL: {@code http://server.root/resolvedvaluesets}
     */
	@RequestMapping(value=PATH_RESOLVED_VALUESETS, method=RequestMethod.GET)
	public Object getResolvedValueSets(
			HttpServletRequest httpServletRequest,
			ResolvedValueSetQueryServiceRestrictions restrictions,
			RestFilter restFilter,
			Page page) {
		
		return this.getResolvedValueSets(
				httpServletRequest, 
				restrictions,
				null,  
				restFilter, 
				page);
	}
	
	@RequestMapping(value=PATH_RESOLVED_VALUESETS, method=RequestMethod.POST)
	public Object getResolvedValueSets(
			HttpServletRequest httpServletRequest,
			ResolvedValueSetQueryServiceRestrictions restrictions,
			@RequestBody Query query,
			RestFilter restFilter,
			Page page) {

		DirectoryResult<ResolvedValueSetDirectoryEntry> result = 
				this.resolvedValueSetQueryService.getResourceSummaries(
						this.getResolvedValueSetQuery(
								query, 
								restFilter,
								restrictions),
					null,//TODO: add Sorting
				page);
		
		Directory directory = this.populateDirectory(
				result, 
				page, 
				httpServletRequest, 
				ResolvedValueSetDirectory.class);
		
		return this.buildResponse(httpServletRequest, directory);
	}

    /**
     * Removes a {@link ResolvedValueSet}.
     */
	@RequestMapping(value=PATH_RESOLVED_VALUESET_OF_VALUESETDEFINITION_BYID, method=RequestMethod.DELETE)
	@ResponseBody
	public void deleteValueSetResolutionByLocalId(
			HttpServletRequest httpServletRequest,
			RestReadContext restReadContext,
			@PathVariable(VAR_VALUESETID) String valueSetName,
			@PathVariable(VAR_VALUESETDEFINITIONID) String definitionLocalId,
			@PathVariable(VAR_RESOLVEDVALUESETID) String resolvedValueSetLocalId) {
		
		ResolvedValueSetReadId id = 
				new ResolvedValueSetReadId(
						resolvedValueSetLocalId,
						ModelUtils.nameOrUriFromName(valueSetName),
						ModelUtils.nameOrUriFromName(definitionLocalId));
		
		this.resolvedValueSetLoaderService.delete(id);
	}

    /**
     * Loads a {@link ResolvedValueSet}.
     */
	@RequestMapping(value=PATH_RESOLVED_VALUESET, method=RequestMethod.POST)
	public Object loadResolvedValueSet(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse,
			@RequestBody ResolvedValueSet resolvedValueSet) {
		
		ResolvedValueSetReference id = this.resolvedValueSetLoaderService.load(resolvedValueSet);

		String location = this.urlTemplateBindingCreator.bindResourceToUrlTemplate(
				URL_BINDER, 
				id, 
				PATH_RESOLVED_VALUESET_OF_VALUESETDEFINITION_BYID);
		
		this.setLocation(httpServletResponse, location);
		
		httpServletResponse.setStatus(HttpStatus.CREATED.value());
		
		//TODO: Add ModelAndView
		
		return null;
	}
	
	@InitBinder
	public void initResolvedValueSetQueryServiceRestrictionsBinder(
			 WebDataBinder binder,
			 @RequestParam(value=PARAM_VALUESET, required=false) List<String> valueset,
			 @RequestParam(value=PARAM_DEFINITION, required=false) List<String> definition,
			 @RequestParam(value=PARAM_CODESYSTEM, required=false) List<String>  codesystem,
			 @RequestParam(value=PARAM_CODESYSTEMVERSION, required=false) List<String>  codesystemversion,
			 @RequestParam(value=PARAM_ENTITY, required=false) List<String> entity) {
		
		if(binder.getTarget() instanceof ResolvedValueSetQueryServiceRestrictions){
			ResolvedValueSetQueryServiceRestrictions restrictions = 
					(ResolvedValueSetQueryServiceRestrictions) binder.getTarget();

			if(CollectionUtils.isNotEmpty(valueset)){
				restrictions.setValueSets(ControllerUtils.idsToNameOrUriSet(valueset));
			}		
			
			if(CollectionUtils.isNotEmpty(definition)){
				restrictions.setValueSetDefinitions(ControllerUtils.idsToNameOrUriSet(definition));
			}		
			
			if(CollectionUtils.isNotEmpty(codesystem)){
				restrictions.setCodeSystems(ControllerUtils.idsToNameOrUriSet(codesystem));
			}	
			
			if(CollectionUtils.isNotEmpty(codesystemversion)){
				restrictions.setCodeSystemVersions(ControllerUtils.idsToNameOrUriSet(codesystemversion));
			}
			
			if(CollectionUtils.isNotEmpty(entity)){
				restrictions.setEntities(ControllerUtils.idsToEntityNameOrUriSet(entity));
			}
		}
	}
	
	@InitBinder
	public void initEntityDescriptionRestrictionBinder(
			 WebDataBinder binder,
			 @RequestParam(value=PARAM_CODESYSTEM, required=false) String codesystem,
			 @RequestParam(value=PARAM_TAG, required=false) String tag,
			 @RequestParam(value=PARAM_CODESYSTEMVERSION, required=false) String codesystemversion,
			 @RequestParam(value=PARAM_ENTITY, required=false) List<String> entity) {
		
		if(binder.getTarget() instanceof ResolvedValueSetResolutionEntityRestrictions){
			ResolvedValueSetResolutionEntityRestrictions restrictions = 
					(ResolvedValueSetResolutionEntityRestrictions) binder.getTarget();

			if(StringUtils.isNotBlank(codesystemversion)){
				restrictions.setCodeSystemVersion(ModelUtils.nameOrUriFromEither(codesystemversion));
			}		
			
			if(CollectionUtils.isNotEmpty(entity)){
				restrictions.setEntities(
						ControllerUtils.idsToEntityNameOrUriSet(entity));
			}		
			
			//TODO: Allow for tags?
		}
	}
	
	private ResolvedValueSetResolutionEntityQuery getResolvedValueSetResolutionEntityQuery(
			final Query query, 
			final RestFilter restFilter,
			final ResolvedValueSetResolutionEntityRestrictions restrictions){
		
		final Set<ResolvedFilter> filters = new HashSet<ResolvedFilter>();
		
		ResolvedFilter filter = this.getFilterResolver().resolveRestFilter(
				restFilter,
				this.resolvedValueSetResolutionService);
		
		if(filter != null){
			filters.add(filter);
		}

		return new ResolvedValueSetResolutionEntityQuery() {
			
			@Override
			public Query getQuery() {
				return query;
			}

			@Override
			public Set<ResolvedFilter> getFilterComponent() {
				return filters;
			}

			@Override
			public ResolvedValueSetResolutionEntityRestrictions getResolvedValueSetResolutionEntityRestrictions() {
				return restrictions;
			}

		};
	}
	
	private ResolvedValueSetQuery getResolvedValueSetQuery(
			final Query query, 
			final RestFilter restFilter,
			final ResolvedValueSetQueryServiceRestrictions restrictions){
		
		final Set<ResolvedFilter> filters = new HashSet<ResolvedFilter>();
		
		ResolvedFilter filter = this.getFilterResolver().resolveRestFilter(
				restFilter,
				this.resolvedValueSetQueryService);
		
		if(filter != null){
			filters.add(filter);
		}

		return new ResolvedValueSetQuery() {
			
			@Override
			public Query getQuery() {
				return query;
			}

			@Override
			public Set<ResolvedFilter> getFilterComponent() {
				return filters;
			}

			@Override
			public ResolvedValueSetQueryServiceRestrictions getResolvedValueSetQueryServiceRestrictions() {
				return restrictions;
			}

		};
	}
	
	private ResolvedValueSetResolutionEntityQuery getResolvedValueSetResolutionEntityQuery(
			final Query query,
			final Set<ResolvedFilter> filterSet,
			final ResolvedValueSetResolutionEntityRestrictions restrictions){
		
		return new ResolvedValueSetResolutionEntityQuery(){

			@Override
			public Query getQuery() {
				return query;
			}

			@Override
			public Set<ResolvedFilter> getFilterComponent() {
				return filterSet;
			}

			@Override
			public ResolvedValueSetResolutionEntityRestrictions getResolvedValueSetResolutionEntityRestrictions() {
				return restrictions;
			}
			
		};
	}

	public ValueSetDefinitionResolutionService getValueSetDefinitionResolutionService() {
		return valueSetDefinitionResolutionService;
	}

	public void setValueSetDefinitionResolutionService(
			ValueSetDefinitionResolutionService valueSetDefinitionResolutionService) {
		this.valueSetDefinitionResolutionService = valueSetDefinitionResolutionService;
	}
}