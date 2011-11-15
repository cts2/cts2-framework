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
import edu.mayo.cts2.framework.model.command.ResolvedFilter;
import edu.mayo.cts2.framework.model.command.ResolvedReadContext;
import edu.mayo.cts2.framework.model.core.Message;
import edu.mayo.cts2.framework.model.core.ScopedEntityName;
import edu.mayo.cts2.framework.model.directory.DirectoryResult;
import edu.mayo.cts2.framework.model.entity.EntityDescription;
import edu.mayo.cts2.framework.model.entity.EntityDescriptionBase;
import edu.mayo.cts2.framework.model.entity.EntityDescriptionMsg;
import edu.mayo.cts2.framework.model.entity.EntityDirectory;
import edu.mayo.cts2.framework.model.entity.EntityDirectoryEntry;
import edu.mayo.cts2.framework.model.service.core.Query;
import edu.mayo.cts2.framework.model.service.exception.UnknownEntity;
import edu.mayo.cts2.framework.model.updates.ChangeableResourceChoice;
import edu.mayo.cts2.framework.model.util.ModelUtils;
import edu.mayo.cts2.framework.service.command.restriction.EntityDescriptionQueryServiceRestrictions;
import edu.mayo.cts2.framework.service.command.restriction.EntityDescriptionQueryServiceRestrictions.TaggedCodeSystemRestriction;
import edu.mayo.cts2.framework.service.profile.codesystemversion.CodeSystemVersionReadService;
import edu.mayo.cts2.framework.service.profile.entitydescription.EntityDescriptionMaintenanceService;
import edu.mayo.cts2.framework.service.profile.entitydescription.EntityDescriptionQueryService;
import edu.mayo.cts2.framework.service.profile.entitydescription.EntityDescriptionReadService;
import edu.mayo.cts2.framework.service.profile.entitydescription.name.EntityDescriptionReadId;
import edu.mayo.cts2.framework.webapp.rest.command.RestFilter;
import edu.mayo.cts2.framework.webapp.rest.command.RestReadContext;
import edu.mayo.cts2.framework.webapp.rest.naming.CodeSystemVersionNameResolver;
import edu.mayo.cts2.framework.webapp.rest.validator.EntityDescriptionValidator;

/**
 * The Class EntityDescriptionController.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Controller
public class EntityDescriptionController extends AbstractServiceAwareController {
	
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
			
			returnMap.put(VAR_CODESYSTEMVERSIONID, 
					codeSystemVersionNameResolver.getVersionIdFromCodeSystemVersionName(
							codeSystemVersionReadService, base.getDescribingCodeSystemVersion().getCodeSystem().getContent()));
			
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
	
	/**
	 * Creates the entity description.
	 *
	 * @param changeseturi the changeseturi
	 * @param entity the entity
	 * @param codeSystemName the code system name
	 * @param codeSystemVersionName the code system version name
	 * @param entityName the entity name
	 */
	@RequestMapping(value=PATH_ENTITYBYID, method=RequestMethod.PUT)
	@ResponseBody
	public void updateEntityDescription(
			@RequestParam(required=false) String changeseturi,
			@RequestBody EntityDescription entity,
			@PathVariable(VAR_CODESYSTEMID) String codeSystemName,
			@PathVariable(VAR_CODESYSTEMVERSIONID) String codeSytemVersionName,
			@PathVariable(VAR_ENTITYID) String entityName) {
		
		this.entityDescriptionValidator.validateCreateEntityDescription(
				this.codeSystemVersionReadService,
				codeSystemName, 
				codeSytemVersionName, 
				entity);
		
		ChangeableResourceChoice choice = new ChangeableResourceChoice();
		choice.setEntityDescription(entity);
		
		ScopedEntityName name = getScopedEntityName(entityName, codeSystemName);
		this.getUpdateHandler().update(
				choice, 
				changeseturi, 
				new EntityDescriptionReadId(name, ModelUtils.nameOrUriFromName(codeSytemVersionName)),
				this.entityDescriptionMaintenanceService);
	}
	
	@RequestMapping(value=PATH_ENTITY, method=RequestMethod.POST)
	@ResponseBody
	public void createEntityDescription(
			@RequestParam(required=false) String changeseturi,
			@RequestBody EntityDescription entity) {
		/*
		this.entityDescriptionValidator.validateCreateEntityDescription(
				this.codeSystemVersionReadService,
				codeSystemName, 
				versionId, 
				entity);
		*/
		
		ChangeableResourceChoice choice = new ChangeableResourceChoice();
		choice.setEntityDescription(entity);

		this.getCreateHandler().create(
				choice,
				changeseturi, 
				PATH_ENTITYBYID, 
				URL_BINDER, 
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
	@ResponseBody
	public EntityDirectory getEntityDescriptionsOfCodeSystemVersion(
			HttpServletRequest httpServletRequest,
			RestReadContext restReadContext,
			EntityDescriptionQueryServiceRestrictions restrictions,
			RestFilter restFilter,
			Page page,
			@PathVariable(VAR_CODESYSTEMID) String codeSystemName,
			@PathVariable(VAR_CODESYSTEMVERSIONID) String codeSystemVersionId) {
		
		return this.getEntityDescriptionsOfCodeSystemVersion(
				httpServletRequest, 
				restReadContext,
				null, 
				restrictions, 
				restFilter, 
				page, 
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
	@ResponseBody
	public EntityDirectory getEntityDescriptionsOfCodeSystemVersion(
			HttpServletRequest httpServletRequest,
			RestReadContext restReadContext,
			@RequestBody Query query,
			EntityDescriptionQueryServiceRestrictions restrictions,
			RestFilter restFilter,
			Page page,
			@PathVariable(VAR_CODESYSTEMID) String codeSystemName,
			@PathVariable(VAR_CODESYSTEMVERSIONID) String codeSystemVersionId) {
		
		String codeSystemVersionName = this.getCodeSystemVersionNameResolver().
					getCodeSystemVersionNameFromVersionId(
							codeSystemVersionReadService, 
							codeSystemName, 
							codeSystemVersionId);
		
		restrictions.setCodeSystemVersion(ModelUtils.nameOrUriFromName(codeSystemVersionName));
		
		return this.getEntityDescriptions(
				httpServletRequest, 
				restReadContext,
				query, 
				restrictions, 
				restFilter, 
				page);
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
		
		String codeSystemVersionName = this.codeSystemVersionNameResolver.getCodeSystemVersionNameFromVersionId(
				codeSystemVersionReadService, codeSystemName, codeSystemVersionId);
		
		restrictions.setCodeSystemVersion(ModelUtils.nameOrUriFromName(codeSystemVersionName));
		
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
	@ResponseBody
	public EntityDirectory getEntityDescriptions(
			HttpServletRequest httpServletRequest,
			RestReadContext restReadContext,
			EntityDescriptionQueryServiceRestrictions restrictions,
			RestFilter restFilter,
			Page page) {
		
		return this.getEntityDescriptions(
				httpServletRequest, 
				restReadContext,
				null, 
				restrictions, 
				restFilter, 
				page);
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
	@ResponseBody
	public EntityDirectory getEntityDescriptions(
			HttpServletRequest httpServletRequest,
			RestReadContext restReadContext,
			@RequestBody Query query,
			EntityDescriptionQueryServiceRestrictions restrictions,
			RestFilter restFilter,
			Page page) {
		
		ResolvedFilter filterComponent = this.processFilter(restFilter, this.entityDescriptionQueryService);
		
		ResolvedReadContext readContext = this.resolveRestReadContext(restReadContext);
		
		DirectoryResult<EntityDirectoryEntry> directoryResult = 
			this.entityDescriptionQueryService.
				getResourceSummaries(
						query,
						createSet(filterComponent),
						restrictions, 
						readContext, 
						page);

		
		EntityDirectory directory = this.populateDirectory(
				directoryResult, 
				page, 
				httpServletRequest, 
				EntityDirectory.class);

		
		return directory;
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
		
		ResolvedFilter filterComponent = this.processFilter(restFilter, this.entityDescriptionQueryService);
		
		int count = 
			this.entityDescriptionQueryService.
				count(query,
						createSet(filterComponent),
						restrictions);

		this.setCount(count, httpServletResponse);
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
	@RequestMapping(value=PATH_ENTITYBYID, method=RequestMethod.GET)
	@ResponseBody
	public Message getEntityDescriptionByName(
			HttpServletRequest httpServletRequest,
			RestReadContext restReadContext,
			@PathVariable(VAR_CODESYSTEMID) String codeSystemName,
			@PathVariable(VAR_CODESYSTEMVERSIONID) String codeSystemVersionId,
			@PathVariable(VAR_ENTITYID) String entityName) {
		
		String codeSystemVersionName = this.codeSystemVersionNameResolver.getCodeSystemVersionNameFromVersionId(
				codeSystemVersionReadService, codeSystemName, codeSystemVersionId);

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
	
	/**
	 * Gets the entity description by uri.
	 *
	 * @param httpServletRequest the http servlet request
	 * @param uri the uri
	 * @return the entity description by uri
	 */
	@RequestMapping(value=PATH_ENTITYBYURI, method=RequestMethod.GET)
	public ModelAndView getEntityDescriptionByUri(
			HttpServletRequest httpServletRequest,
			RestReadContext restReadContext,
			@RequestParam(VAR_URI) String uri,
			@RequestParam(value="redirect", defaultValue="false") boolean redirect) {
		
		return this.doReadByUri(
				httpServletRequest, 
				MESSAGE_FACTORY, 
				PATH_ENTITYBYURI, 
				PATH_ENTITYBYID, 
				URL_BINDER, 
				this.entityDescriptionReadService, 
				new EntityDescriptionReadId(uri,null),
				redirect);
	}
	
	@InitBinder
	public void initEntityDescriptionRestrictionBinder(
			 WebDataBinder binder,
			 @RequestParam(value=PARAM_CODESYSTEM, required=false) String codesystem,
			 @RequestParam(value=PARAM_CODESYSTEMTAG, required=false) String tag,
			 @RequestParam(value=PARAM_CODESYSTEMVERSION, required=false) String codesystemversion,
			 @RequestParam(value=PARAM_ENTITY, required=false) List<String> entity) {
		
		if(binder.getTarget() instanceof EntityDescriptionQueryServiceRestrictions){
			EntityDescriptionQueryServiceRestrictions restrictions = 
					(EntityDescriptionQueryServiceRestrictions) binder.getTarget();

			if(StringUtils.isNotBlank(codesystemversion)){
				restrictions.setCodeSystemVersion(ModelUtils.nameOrUriFromEither(codesystemversion));
			}		
			
			if(CollectionUtils.isNotEmpty(entity)){
				restrictions.setEntities(
						ControllerUtils.idsToEntityNameOrUriSet(entity));
			}		
			
			if(StringUtils.isNotBlank(codesystem)){

				restrictions.setTaggedCodeSystem(new TaggedCodeSystemRestriction());
				restrictions.getTaggedCodeSystem().setCodeSystem(ModelUtils.nameOrUriFromEither(codesystem));
				
				String tagName = ControllerUtils.getReference(tag, this.entityDescriptionQueryService.getSupportedTags()).getContent();
			
				restrictions.getTaggedCodeSystem().setTag(tagName);
			}
		}
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