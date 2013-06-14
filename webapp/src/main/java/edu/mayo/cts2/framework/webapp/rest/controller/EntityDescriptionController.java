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
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
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

import edu.mayo.cts2.framework.core.util.EncodingUtils;
import edu.mayo.cts2.framework.model.command.Page;
import edu.mayo.cts2.framework.model.command.ResolvedReadContext;
import edu.mayo.cts2.framework.model.core.ChangeableElementGroup;
import edu.mayo.cts2.framework.model.core.EntityReference;
import edu.mayo.cts2.framework.model.core.Message;
import edu.mayo.cts2.framework.model.core.ScopedEntityName;
import edu.mayo.cts2.framework.model.directory.DirectoryResult;
import edu.mayo.cts2.framework.model.entity.EntityDescription;
import edu.mayo.cts2.framework.model.entity.EntityDescriptionBase;
import edu.mayo.cts2.framework.model.entity.EntityDescriptionMsg;
import edu.mayo.cts2.framework.model.entity.EntityDirectory;
import edu.mayo.cts2.framework.model.entity.EntityList;
import edu.mayo.cts2.framework.model.entity.EntityListEntry;
import edu.mayo.cts2.framework.model.entity.EntityReferenceMsg;
import edu.mayo.cts2.framework.model.exception.ExceptionFactory;
import edu.mayo.cts2.framework.model.service.core.EntityNameOrURI;
import edu.mayo.cts2.framework.model.service.core.Query;
import edu.mayo.cts2.framework.model.service.exception.UnknownEntity;
import edu.mayo.cts2.framework.model.util.ModelUtils;
import edu.mayo.cts2.framework.service.command.restriction.EntityDescriptionQueryServiceRestrictions;
import edu.mayo.cts2.framework.service.command.restriction.TaggedCodeSystemRestriction;
import edu.mayo.cts2.framework.service.profile.codesystemversion.CodeSystemVersionReadService;
import edu.mayo.cts2.framework.service.profile.entitydescription.EntityDescriptionMaintenanceService;
import edu.mayo.cts2.framework.service.profile.entitydescription.EntityDescriptionQuery;
import edu.mayo.cts2.framework.service.profile.entitydescription.EntityDescriptionQueryService;
import edu.mayo.cts2.framework.service.profile.entitydescription.EntityDescriptionReadService;
import edu.mayo.cts2.framework.service.profile.entitydescription.name.EntityDescriptionReadId;
import edu.mayo.cts2.framework.webapp.naming.CodeSystemVersionNameResolver;
import edu.mayo.cts2.framework.webapp.rest.command.QueryControl;
import edu.mayo.cts2.framework.webapp.rest.command.RestFilter;
import edu.mayo.cts2.framework.webapp.rest.command.RestReadContext;
import edu.mayo.cts2.framework.webapp.rest.query.EntityQueryBuilder;
import edu.mayo.cts2.framework.webapp.rest.util.ControllerUtils;
import edu.mayo.cts2.framework.webapp.rest.validator.EntityDescriptionValidator;

/**
 * The Class EntityDescriptionController.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Controller
public class EntityDescriptionController extends AbstractMessageWrappingController {
	
	@Cts2Service
	private EntityDescriptionQueryService entityDescriptionQueryService;
	
	@Cts2Service
	private EntityDescriptionReadService entityDescriptionReadService;
	
	@Cts2Service
	private EntityDescriptionMaintenanceService entityDescriptionMaintenanceService;
	
	@Cts2Service
	private CodeSystemVersionReadService codeSystemVersionReadService;
	
	@Resource
	private CodeSystemVersionNameResolver codeSystemVersionNameResolver;
		
	@Resource
	private EntityDescriptionValidator entityDescriptionValidator;

	private final UrlTemplateBinder<EntityDescription> URL_BINDER =
			new UrlTemplateBinder<EntityDescription>(){

		@Override
		public Map<String,String> getPathValues(EntityDescription resource) {
			Map<String,String> returnMap = new HashMap<String,String>();
			
			EntityDescriptionBase base = ModelUtils.getEntity(resource);
			
			String codeSystemName = base.getDescribingCodeSystemVersion().getCodeSystem().getContent();
			
			returnMap.put(VAR_CODESYSTEMID, codeSystemName);
			
			ResolvedReadContext readContext = null;
			
			ChangeableElementGroup group = 
					resource.getChangeableElementGroup();
			
			if(group != null && group.getChangeDescription() != null){
				String changeSetUri = group.getChangeDescription().getContainingChangeSet();
				
				readContext = new ResolvedReadContext();
				readContext.setChangeSetContextUri(changeSetUri);
			}
			
			returnMap.put(VAR_CODESYSTEMVERSIONID, 
					codeSystemVersionNameResolver.getVersionIdFromCodeSystemVersionName(
							codeSystemVersionReadService, 
							base.getDescribingCodeSystemVersion().getVersion().getContent(),
							readContext));
			
			ScopedEntityName id = base.getEntityID();
			returnMap.put(VAR_ENTITYID, EncodingUtils.encodeScopedEntityName(id));

			return returnMap;
		}

	};
	
	private final static MessageFactory<EntityDescription> MESSAGE_FACTORY = 
			new MessageFactory<EntityDescription>() {

		@Override
		public Message createMessage(EntityDescription resource) {
			EntityDescriptionMsg msg = new EntityDescriptionMsg();
			msg.setEntityDescription(resource);

			return msg;
		}
	};
	
	private final static MessageFactory<EntityList> ENTITY_LIST_MESSAGE_FACTORY = 
		new MessageFactory<EntityList>(){

		@Override
		public Message createMessage(EntityList resource) {
			return resource;
		}	
	};
	
	private final static MessageFactory<EntityReference> ENTITYREFERENCE_MESSAGE_FACTORY = 
			new MessageFactory<EntityReference>(){

			@Override
			public Message createMessage(EntityReference resource) {
				EntityReferenceMsg msg = new EntityReferenceMsg();
				msg.setEntityReference(resource);
				
				return msg;
			}	
		};
	
	/**
	 * Creates the entity description.
	 *
	 * @param changeseturi the changeseturi
	 * @param entity the entity
	 * @param codeSystemName the code system name
	 * @param codeSystemVersionName the code system version name
	 * @param entityName the entity name
	 */
	@RequestMapping(value=PATH_ENTITY_OF_CODESYSTEM_VERSION_BYID, method=RequestMethod.PUT)
	public Object updateEntityDescription(
			HttpServletResponse httpServletResponse,
			@RequestParam(value=PARAM_CHANGESETCONTEXT, required=false) String changeseturi,
			@RequestBody EntityDescription entity,
			@PathVariable(VAR_CODESYSTEMID) String codeSystemName,
			@PathVariable(VAR_CODESYSTEMVERSIONID) String codeSytemVersionName,
			@PathVariable(VAR_ENTITYID) String entityName) {
		
		this.entityDescriptionValidator.validateUpdateEntityDescription(
				this.codeSystemVersionReadService,
				codeSystemName, 
				codeSytemVersionName, 
				entity);
	
		ScopedEntityName name = getScopedEntityName(entityName, codeSystemName);
		
		return this.doUpdate(
				httpServletResponse,
				entity, 
				changeseturi, 
				new EntityDescriptionReadId(name, ModelUtils.nameOrUriFromName(codeSytemVersionName)),
				this.entityDescriptionMaintenanceService);
	}
	
	@RequestMapping(value=PATH_ENTITY, method=RequestMethod.POST)
	public Object createEntityDescription(
			HttpServletResponse httpServletResponse,
			@RequestParam(value=PARAM_CHANGESETCONTEXT, required=false) String changeseturi,
			@RequestBody EntityDescription entity) {

		return this.doCreate(
				httpServletResponse,
				entity,
				changeseturi, 
				PATH_ENTITY_OF_CODESYSTEM_VERSION_BYID, 
				URL_BINDER, 
				this.entityDescriptionMaintenanceService);
	}
	
	@RequestMapping(value=PATH_ENTITY_OF_CODESYSTEM_VERSION_BYID, method=RequestMethod.DELETE)
	public Object deleteCodeSystemVersion(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse,
			@PathVariable(VAR_CODESYSTEMID) String codeSystemName,
			@PathVariable(VAR_CODESYSTEMVERSIONID) String codeSystemVersionId,
			@PathVariable(VAR_ENTITYID) String entityName,
			@RequestParam(PARAM_CHANGESETCONTEXT) String changeseturi) {
		
			ResolvedReadContext readContext = new ResolvedReadContext();
			readContext.setChangeSetContextUri(changeseturi);
		
			String codeSystemVersionName = this.codeSystemVersionNameResolver.getCodeSystemVersionNameFromVersionId(
				codeSystemVersionReadService, 
				codeSystemName, 
				codeSystemVersionId,
				readContext);
		
			EntityDescriptionReadId id = new EntityDescriptionReadId(
				getScopedEntityName(entityName, codeSystemName),
				ModelUtils.nameOrUriFromName(codeSystemVersionName));

			
			return this.doDelete(
					httpServletResponse, 
					id, 
					changeseturi, 
					this.entityDescriptionMaintenanceService);
	}

	/**
	 * Gets the entity descriptions of code system version.
	 *
	 * @param httpServletRequest the http servlet request
	 * @param restrictions the restrictions
	 * @param resolvedFilter the filter
	 * @param page the page
	 * @param codeSystemName the code system name
	 * @param codeSystemVersionName the code system version name
	 * @return the entity descriptions of code system version
	 */
	@RequestMapping(value=PATH_ENTITIES_OF_CODESYSTEM_VERSION, method=RequestMethod.GET)
	public Object getEntityDescriptionsOfCodeSystemVersion(
			HttpServletRequest httpServletRequest,
			QueryControl queryControl,
			RestReadContext restReadContext,
			EntityDescriptionQueryServiceRestrictions restrictions,
			RestFilter restFilter,
			Page page,
			boolean list,
			@PathVariable(VAR_CODESYSTEMID) String codeSystemName,
			@PathVariable(VAR_CODESYSTEMVERSIONID) String codeSystemVersionId) {
		
		return this.getEntityDescriptionsOfCodeSystemVersion(
				httpServletRequest, 
				restReadContext,
				queryControl,
				null, 
				restrictions, 
				restFilter, 
				page, 
				list,
				codeSystemName, 
				codeSystemVersionId);
	}
	
	/**
	 * Gets the entity descriptions of code system version.
	 *
	 * @param httpServletRequest the http servlet request
	 * @param query the query
	 * @param restrictions the restrictions
	 * @param resolvedFilter the filter
	 * @param page the page
	 * @param codeSystemName the code system name
	 * @param codeSystemVersionName the code system version name
	 * @return the entity descriptions of code system version
	 */
	@RequestMapping(value=PATH_ENTITIES_OF_CODESYSTEM_VERSION, method=RequestMethod.POST)
	public Object getEntityDescriptionsOfCodeSystemVersion(
			HttpServletRequest httpServletRequest,
			RestReadContext restReadContext,
			QueryControl queryControl,
			@RequestBody Query query,
			EntityDescriptionQueryServiceRestrictions restrictions,
			RestFilter restFilter,
			Page page,
			boolean list,
			@PathVariable(VAR_CODESYSTEMID) String codeSystemName,
			@PathVariable(VAR_CODESYSTEMVERSIONID) String codeSystemVersionId) {
		
		ResolvedReadContext readContext = this.resolveRestReadContext(restReadContext);
		
		String codeSystemVersionName = this.getCodeSystemVersionNameResolver().
					getCodeSystemVersionNameFromVersionId(
							codeSystemVersionReadService, 
							codeSystemName, 
							codeSystemVersionId,
							readContext);
		
		restrictions.getCodeSystemVersions().add(ModelUtils.nameOrUriFromName(codeSystemVersionName));
		
		return this.getEntityDescriptions(
				httpServletRequest, 
				restReadContext,
				queryControl,
				query, 
				restrictions, 
				restFilter, 
				page,
				list);
	}
	
	/**
	 * Gets the entity descriptions of code system version count.
	 *
	 * @param httpServletResponse the http servlet response
	 * @param query the query
	 * @param restrictions the restrictions
	 * @param resolvedFilter the filter
	 * @param page the page
	 * @param codeSystemName the code system name
	 * @param codeSystemVersionName the code system version name
	 * @return the entity descriptions of code system version count
	 */
	@RequestMapping(value=PATH_ENTITIES_OF_CODESYSTEM_VERSION, method=RequestMethod.HEAD)
	@ResponseBody
	public void getEntityDescriptionsOfCodeSystemVersionCount(
			HttpServletResponse httpServletResponse,
			RestReadContext restReadContext,
			@RequestBody Query query,
			EntityDescriptionQueryServiceRestrictions restrictions,
			RestFilter restFilter,
			Page page,
			@PathVariable(VAR_CODESYSTEMID) String codeSystemName,
			@PathVariable(VAR_CODESYSTEMVERSIONID) String codeSystemVersionId) {
		
		ResolvedReadContext readContext = this.resolveRestReadContext(restReadContext);
		
		String codeSystemVersionName = this.codeSystemVersionNameResolver.getCodeSystemVersionNameFromVersionId(
				codeSystemVersionReadService, 
				codeSystemName, 
				codeSystemVersionId,
				readContext);
		
		restrictions.getCodeSystemVersions().add(ModelUtils.nameOrUriFromName(codeSystemVersionName));
		
		this.getEntityDescriptionsCount(
				httpServletResponse,
				restReadContext,
				query, 
				restrictions, 
				restFilter);
	}
	

	/**
	 * Gets the entity descriptions.
	 *
	 * @param httpServletRequest the http servlet request
	 * @param restrictions the restrictions
	 * @param resolvedFilter the filter
	 * @param page the page
	 * @return the entity descriptions
	 */
	@RequestMapping(value=PATH_ENTITIES, method=RequestMethod.GET)
	public Object getEntityDescriptions(
			HttpServletRequest httpServletRequest,
			QueryControl queryControl,
			RestReadContext restReadContext,
			EntityDescriptionQueryServiceRestrictions restrictions,
			RestFilter restFilter,
			Page page,
			boolean list) {
		
		return this.getEntityDescriptions(
				httpServletRequest, 
				restReadContext,
				queryControl,
				null, 
				restrictions, 
				restFilter, 
				page,
				list);
	}
	
	/**
	 * Gets the entity descriptions.
	 *
	 * @param httpServletRequest the http servlet request
	 * @param query the query
	 * @param restrictions the restrictions
	 * @param resolvedFilter the filter
	 * @param page the page
	 * @return the entity descriptions
	 */
	@RequestMapping(value=PATH_ENTITIES, method=RequestMethod.POST)
	public Object getEntityDescriptions(
			HttpServletRequest httpServletRequest,
			RestReadContext restReadContext,
			QueryControl queryControl,
			@RequestBody Query query,
			EntityDescriptionQueryServiceRestrictions restrictions,
			RestFilter restFilter,
			Page page,
			boolean list) {
		
		EntityQueryBuilder builder = this.getNewResourceQueryBuilder();
		
		EntityDescriptionQuery resourceQuery = builder.
				addQuery(query).
				addRestrictions(restrictions).
				addRestFilter(restFilter).
				addRestReadContext(restReadContext).
				build();
	
		return this.doQuery(
				httpServletRequest,
				list, 
				this.entityDescriptionQueryService,
				resourceQuery,
				page, 
				queryControl,
				EntityDirectory.class, 
				EntityList.class);
	}
	
	/**
	 * Gets the entity descriptions count.
	 *
	 * @param httpServletResponse the http servlet response
	 * @param query the query
	 * @param restrictions the restrictions
	 * @param filterComponent the filter component
	 * @return the entity descriptions count
	 */
	@RequestMapping(value=PATH_ENTITIES, method=RequestMethod.HEAD)
	@ResponseBody
	public void getEntityDescriptionsCount(
			HttpServletResponse httpServletResponse,
			RestReadContext restReadContext,
			@RequestBody Query query,
			EntityDescriptionQueryServiceRestrictions restrictions,
			RestFilter restFilter) {
		
		EntityQueryBuilder builder = this.getNewResourceQueryBuilder();
		
		EntityDescriptionQuery resourceQuery = builder.
				addQuery(query).
				addRestrictions(restrictions).
				addRestFilter(restFilter).
				addRestReadContext(restReadContext).
				build();
		
		int count = 
			this.entityDescriptionQueryService.count(resourceQuery);

		this.setCount(count, httpServletResponse);
	}
	
	@RequestMapping(value=PATH_ENTITY_OF_CODESYSTEM_VERSION_BYURI, method=RequestMethod.GET)
	public ModelAndView getEntityDescriptionOfCodeSystemVersionByUri(
			HttpServletRequest httpServletRequest,
			RestReadContext restReadContext,
			@PathVariable(VAR_CODESYSTEMID) String codeSystemName,
			@PathVariable(VAR_CODESYSTEMVERSIONID) String codeSystemVersionId,
			@RequestParam(PARAM_URI) String uri,
			@RequestParam(value="redirect", defaultValue=DEFAULT_REDIRECT) boolean redirect) {
		
		ResolvedReadContext readContext = this.resolveRestReadContext(restReadContext);
		
		String codeSystemVersionName = this.codeSystemVersionNameResolver.getCodeSystemVersionNameFromVersionId(
				codeSystemVersionReadService, 
				codeSystemName,
				codeSystemVersionId,
				readContext);
		
		EntityDescriptionReadId id = new EntityDescriptionReadId(
				uri,
				ModelUtils.nameOrUriFromName(codeSystemVersionName));
		
		EntityDescription resource = 
				this.entityDescriptionReadService.read(
						id, 
						readContext);
		
		return this.doForward(
				resource, 
				id.toString(), 
				httpServletRequest, 
				MESSAGE_FACTORY, 
				PATH_ENTITY_OF_CODESYSTEM_VERSION_BYURI, 
				PATH_ENTITY_OF_CODESYSTEM_VERSION_BYID, 
				URL_BINDER, 
				restReadContext, 
				UnknownEntity.class, 
				redirect);
	}

	/**
	 * Gets the entity description by name.
	 *
	 * @param httpServletRequest the http servlet request
	 * @param codeSystemName the code system name
	 * @param codeSystemVersionName the code system version name
	 * @param entityName the entity name
	 * @return the entity description by name
	 */
	@RequestMapping(value=PATH_ENTITY_OF_CODESYSTEM_VERSION_BYID, method=RequestMethod.GET)
	public Object getEntityDescriptionOfCodeSystemVersionByName(
			HttpServletRequest httpServletRequest,
			RestReadContext restReadContext,
			@PathVariable(VAR_CODESYSTEMID) String codeSystemName,
			@PathVariable(VAR_CODESYSTEMVERSIONID) String codeSystemVersionId,
			@PathVariable(VAR_ENTITYID) String entityName) {
		
		ResolvedReadContext readContext = this.resolveRestReadContext(restReadContext);
		
		String codeSystemVersionName = this.codeSystemVersionNameResolver.getCodeSystemVersionNameFromVersionId(
				codeSystemVersionReadService, 
				codeSystemName,
				codeSystemVersionId,
				readContext);

		EntityDescriptionReadId id = new EntityDescriptionReadId(
				getScopedEntityName(entityName, codeSystemName),
				ModelUtils.nameOrUriFromName(codeSystemVersionName));
		
		return this.doRead(
				httpServletRequest, 
				MESSAGE_FACTORY, 
				this.entityDescriptionReadService, 
				restReadContext,
				UnknownEntity.class,
				id);
	}
	
	@RequestMapping(value=PATH_ALL_DESCRIPTIONS_OF_ENTITYBYID, method=RequestMethod.GET)
	public Object getAllEntityDescriptionsByName(
			HttpServletRequest httpServletRequest,
			RestReadContext restReadContext,
			QueryControl queryControl,
			@PathVariable(VAR_ENTITYID) String entityName,
			Page page,
			boolean list) {
		
		ResolvedReadContext readContext = this.resolveRestReadContext(restReadContext);

		EntityNameOrURI entityId = 
				ModelUtils.entityNameOrUriFromName(this.getScopedEntityName(entityName));
	
		Object response = null;
		
		if(list){
			DirectoryResult<EntityListEntry> entityList = 
					this.entityDescriptionReadService.readEntityDescriptions(
							entityId, 
							this.resolveSort(queryControl, this.entityDescriptionQueryService),
							readContext, 
							page);
			response = this.populateDirectory(entityList, page, httpServletRequest, EntityList.class);
		} else {
			EntityReference entityReference = this.entityDescriptionReadService.availableDescriptions(entityId, readContext);
			
			if(entityReference != null){
				response = this.wrapMessage(
					ENTITYREFERENCE_MESSAGE_FACTORY.createMessage(entityReference), 
					httpServletRequest);
			}
		}
		
		if(response == null) {
			throw ExceptionFactory.createUnknownResourceException(entityId.toString(), UnknownEntity.class);
		} else {
			return this.buildResponse(httpServletRequest, response);
		}
	}
	
	/**
	 * Gets the entity description by uri.
	 *
	 * @param httpServletRequest the http servlet request
	 * @param uri the uri
	 * @return the entity description by uri
	 */
	@RequestMapping(value=PATH_ENTITYBYURI, method=RequestMethod.GET)
	public Object getAllEntityDescriptionsByUri(
			HttpServletRequest httpServletRequest,
			RestReadContext restReadContext,
			QueryControl queryControl,
			@RequestParam(PARAM_URI) String uri,
			Page page,
			boolean list,
			@RequestParam(value="redirect", defaultValue=DEFAULT_REDIRECT) boolean redirect) {

		ResolvedReadContext readContext = this.resolveRestReadContext(restReadContext);
		
		EntityNameOrURI entityId = 
				ModelUtils.entityNameOrUriFromUri(uri);
		
		if(list){
			DirectoryResult<EntityListEntry> entityList = 
					this.entityDescriptionReadService.readEntityDescriptions(
							entityId, 
							this.resolveSort(queryControl, this.entityDescriptionQueryService),
							readContext, 
							page);
			
			if(entityList == null || CollectionUtils.isEmpty(entityList.getEntries())){
				throw ExceptionFactory.createUnknownResourceException(entityId.toString(), UnknownEntity.class);
			}
			
			ScopedEntityName name = ModelUtils.getEntity(entityList.getEntries().get(0).getEntry()).getEntityID();
			
			EntityList descriptions = 
				this.populateDirectory(entityList, page, httpServletRequest, EntityList.class);
			
			final String id = EncodingUtils.encodeScopedEntityName(name);
			
			return this.doForward(
				descriptions, 
				id, 
				httpServletRequest, 
				ENTITY_LIST_MESSAGE_FACTORY,
				PATH_ENTITYBYURI, 
				PATH_ALL_DESCRIPTIONS_OF_ENTITYBYID, 
				new UrlTemplateBinder<EntityList>(){
					@Override
					public Map<String, String> getPathValues(
							EntityList resource) {
						Map<String, String> map = new HashMap<String,String>();
						map.put(VAR_ENTITYID, id);
						
						return map;
					}
				}, 
				restReadContext, 
				UnknownEntity.class, 
				redirect);
			
		} else {
			EntityReference descriptions = this.entityDescriptionReadService.availableDescriptions(entityId, readContext);
			
			if(descriptions == null){
				throw ExceptionFactory.createUnknownResourceException(entityId.toString(), UnknownEntity.class);
			}
			
			final String id = EncodingUtils.encodeScopedEntityName(descriptions.getName());
			
			return this.doForward(
				descriptions, 
				id, 
				httpServletRequest, 
				ENTITYREFERENCE_MESSAGE_FACTORY,
				PATH_ENTITYBYURI, 
				PATH_ALL_DESCRIPTIONS_OF_ENTITYBYID, 
				new UrlTemplateBinder<EntityReference>(){

					@Override
					public Map<String, String> getPathValues(
							EntityReference resource) {
						Map<String, String> map = new HashMap<String,String>();
						map.put(VAR_ENTITYID, id);
						
						return map;
					}
					
				}, 
				restReadContext, 
				UnknownEntity.class, 
				redirect);
		}

	}
	
	@InitBinder
	public void initEntityDescriptionRestrictionBinder(
			 WebDataBinder binder,
			 @RequestParam(value=PARAM_CODESYSTEM, required=false) List<String> codesystem,
			 @RequestParam(value=PARAM_CODESYSTEMVERSION, required=false) List<String> codesystemversion,
			 @RequestParam(value=PARAM_ENTITY, required=false) List<String> entity) {
		
		if(binder.getTarget() instanceof EntityDescriptionQueryServiceRestrictions){
			EntityDescriptionQueryServiceRestrictions restrictions = 
					(EntityDescriptionQueryServiceRestrictions) binder.getTarget();

			if(CollectionUtils.isNotEmpty(codesystemversion)){
				for(String csv : codesystemversion){
					restrictions.getCodeSystemVersions().add(
						ModelUtils.nameOrUriFromEither(csv));
				}
			}		
			
			if(CollectionUtils.isNotEmpty(entity)){
				restrictions.setEntities(
						ControllerUtils.idsToEntityNameOrUriSet(entity));
			}		
			
			if(CollectionUtils.isNotEmpty(codesystem)){
				for(String cs : codesystem){
					String[] parts = StringUtils.split(cs, PARAM_SEPARATOR);
					TaggedCodeSystemRestriction	restriction = 
						new TaggedCodeSystemRestriction();

					restriction.setCodeSystem(ModelUtils.nameOrUriFromEither(parts[0]));
					
					String tagName;
					if(parts.length == 2){
						tagName = ControllerUtils.getReference(
							parts[1], 
							this.entityDescriptionQueryService.getSupportedTags()).getContent();
					} else {
						tagName = DEFAULT_TAG;
					}
				
					restriction.setTag(tagName);
					
					restrictions.getTaggedCodeSystems().add(restriction);
				}
			}
		}
	}
	
	private EntityQueryBuilder getNewResourceQueryBuilder(){
		return new EntityQueryBuilder(
			this.entityDescriptionQueryService, 
			this.getFilterResolver(),
			this.getReadContextResolver());
	}

	public EntityDescriptionQueryService getEntityDescriptionQueryService() {
		return entityDescriptionQueryService;
	}

	public void setEntityDescriptionQueryService(
			EntityDescriptionQueryService entityDescriptionQueryService) {
		this.entityDescriptionQueryService = entityDescriptionQueryService;
	}

	public EntityDescriptionReadService getEntityDescriptionReadService() {
		return entityDescriptionReadService;
	}

	public void setEntityDescriptionReadService(
			EntityDescriptionReadService entityDescriptionReadService) {
		this.entityDescriptionReadService = entityDescriptionReadService;
	}

	public EntityDescriptionMaintenanceService getEntityDescriptionMaintenanceService() {
		return entityDescriptionMaintenanceService;
	}

	public void setEntityDescriptionMaintenanceService(
			EntityDescriptionMaintenanceService entityDescriptionMaintenanceService) {
		this.entityDescriptionMaintenanceService = entityDescriptionMaintenanceService;
	}

	public CodeSystemVersionReadService getCodeSystemVersionReadService() {
		return codeSystemVersionReadService;
	}

	public void setCodeSystemVersionReadService(
			CodeSystemVersionReadService codeSystemVersionReadService) {
		this.codeSystemVersionReadService = codeSystemVersionReadService;
	}

	public CodeSystemVersionNameResolver getCodeSystemVersionNameResolver() {
		return codeSystemVersionNameResolver;
	}

	public void setCodeSystemVersionNameResolver(
			CodeSystemVersionNameResolver codeSystemVersionNameResolver) {
		this.codeSystemVersionNameResolver = codeSystemVersionNameResolver;
	}

	public EntityDescriptionValidator getEntityDescriptionValidator() {
		return entityDescriptionValidator;
	}

	public void setEntityDescriptionValidator(
			EntityDescriptionValidator entityDescriptionValidator) {
		this.entityDescriptionValidator = entityDescriptionValidator;
	}
}