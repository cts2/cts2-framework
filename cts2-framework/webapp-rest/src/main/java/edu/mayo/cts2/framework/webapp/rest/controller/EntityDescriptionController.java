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

import edu.mayo.cts2.framework.model.core.FilterComponent;
import edu.mayo.cts2.framework.model.directory.DirectoryResult;
import edu.mayo.cts2.framework.model.entity.EntityDescription;
import edu.mayo.cts2.framework.model.entity.EntityDescriptionMsg;
import edu.mayo.cts2.framework.model.entity.EntityDirectory;
import edu.mayo.cts2.framework.model.entity.EntityDirectoryEntry;
import edu.mayo.cts2.framework.model.service.core.Query;
import edu.mayo.cts2.framework.service.command.Filter;
import edu.mayo.cts2.framework.service.command.Page;
import edu.mayo.cts2.framework.service.command.restriction.EntityDescriptionQueryServiceRestrictions;
import edu.mayo.cts2.framework.service.profile.entitydescription.EntityDescriptionMaintenanceService;
import edu.mayo.cts2.framework.service.profile.entitydescription.EntityDescriptionQueryService;
import edu.mayo.cts2.framework.service.profile.entitydescription.EntityDescriptionReadService;
import edu.mayo.cts2.framework.service.profile.entitydescription.name.EntityDescriptionName;

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
	public void createEntityDescription(
			@RequestParam(required=false) String changeseturi,
			@RequestBody EntityDescription entity,
			@PathVariable(VAR_CODESYSTEMID) String codeSystemName,
			@PathVariable(VAR_CODESYSTEMVERSIONID) String codeSystemVersionName,
			@PathVariable(VAR_ENTITYID) String entityName) {

			this.entityDescriptionMaintenanceService.createResource(
					changeseturi, 
					entity);
	}
	
	/**
	 * Gets the entity descriptions of code system version.
	 *
	 * @param httpServletRequest the http servlet request
	 * @param restrictions the restrictions
	 * @param filter the filter
	 * @param page the page
	 * @param codeSystemName the code system name
	 * @param codeSystemVersionName the code system version name
	 * @return the entity descriptions of code system version
	 */
	@RequestMapping(value=PATH_ENTITIES_OF_CODESYSTEM_VERSION, method=RequestMethod.GET)
	@ResponseBody
	public EntityDirectory getEntityDescriptionsOfCodeSystemVersion(
			HttpServletRequest httpServletRequest,
			EntityDescriptionQueryServiceRestrictions restrictions,
			Filter filter,
			Page page,
			@PathVariable(VAR_CODESYSTEMID) String codeSystemName,
			@PathVariable(VAR_CODESYSTEMVERSIONID) String codeSystemVersionName) {
		
		return this.getEntityDescriptionsOfCodeSystemVersion(
				httpServletRequest, 
				null, 
				restrictions, 
				filter, 
				page, 
				codeSystemName, 
				codeSystemVersionName);
	}
	
	/**
	 * Gets the entity descriptions of code system version.
	 *
	 * @param httpServletRequest the http servlet request
	 * @param query the query
	 * @param restrictions the restrictions
	 * @param filter the filter
	 * @param page the page
	 * @param codeSystemName the code system name
	 * @param codeSystemVersionName the code system version name
	 * @return the entity descriptions of code system version
	 */
	@RequestMapping(value=PATH_ENTITIES_OF_CODESYSTEM_VERSION, method=RequestMethod.POST)
	@ResponseBody
	public EntityDirectory getEntityDescriptionsOfCodeSystemVersion(
			HttpServletRequest httpServletRequest,
			@RequestBody Query query,
			EntityDescriptionQueryServiceRestrictions restrictions,
			Filter filter,
			Page page,
			@PathVariable(VAR_CODESYSTEMID) String codeSystemName,
			@PathVariable(VAR_CODESYSTEMVERSIONID) String codeSystemVersionName) {
		
		FilterComponent filterComponent = this.processFilter(filter, this.entityDescriptionQueryService);
		
		restrictions.setCodesystem(codeSystemName);
		restrictions.setCodesystemversion(codeSystemVersionName);
		
		DirectoryResult<EntityDirectoryEntry> directoryResult = 
			this.entityDescriptionQueryService.getResourceSummaries(
					query, 
					filterComponent, 
					restrictions, 
					page);

		EntityDirectory directory = this.populateDirectory(
				directoryResult, 
				page, 
				httpServletRequest, 
				EntityDirectory.class);
		
		return directory;
	}
	
	/**
	 * Gets the entity descriptions of code system version count.
	 *
	 * @param httpServletResponse the http servlet response
	 * @param query the query
	 * @param restrictions the restrictions
	 * @param filter the filter
	 * @param page the page
	 * @param codeSystemName the code system name
	 * @param codeSystemVersionName the code system version name
	 * @return the entity descriptions of code system version count
	 */
	@RequestMapping(value=PATH_ENTITIES_OF_CODESYSTEM_VERSION, method=RequestMethod.HEAD)
	@ResponseBody
	public void getEntityDescriptionsOfCodeSystemVersionCount(
			HttpServletResponse httpServletResponse,
			@RequestBody Query query,
			EntityDescriptionQueryServiceRestrictions restrictions,
			Filter filter,
			Page page,
			@PathVariable(VAR_CODESYSTEMID) String codeSystemName,
			@PathVariable(VAR_CODESYSTEMVERSIONID) String codeSystemVersionName) {
		
		FilterComponent filterComponent = this.processFilter(filter, this.entityDescriptionQueryService);
		
		restrictions.setCodesystem(codeSystemName);
		restrictions.setCodesystemversion(codeSystemVersionName);
		
		int count = 
			this.entityDescriptionQueryService.count(query, filterComponent, restrictions);

		this.setCount(count, httpServletResponse);
	}
	

	/**
	 * Gets the entity descriptions.
	 *
	 * @param httpServletRequest the http servlet request
	 * @param restrictions the restrictions
	 * @param filter the filter
	 * @param page the page
	 * @return the entity descriptions
	 */
	@RequestMapping(value=PATH_ENTITIES, method=RequestMethod.GET)
	@ResponseBody
	public EntityDirectory getEntityDescriptions(
			HttpServletRequest httpServletRequest,
			EntityDescriptionQueryServiceRestrictions restrictions,
			Filter filter,
			Page page) {
		
		return this.getEntityDescriptions(
				httpServletRequest, 
				null, 
				restrictions, 
				filter, 
				page);
	}
	
	/**
	 * Gets the entity descriptions.
	 *
	 * @param httpServletRequest the http servlet request
	 * @param query the query
	 * @param restrictions the restrictions
	 * @param filter the filter
	 * @param page the page
	 * @return the entity descriptions
	 */
	@RequestMapping(value=PATH_ENTITIES, method=RequestMethod.POST)
	@ResponseBody
	public EntityDirectory getEntityDescriptions(
			HttpServletRequest httpServletRequest,
			@RequestBody Query query,
			EntityDescriptionQueryServiceRestrictions restrictions,
			Filter filter,
			Page page) {
		
		FilterComponent filterComponent = this.processFilter(filter, this.entityDescriptionQueryService);
		
		DirectoryResult<EntityDirectoryEntry> directoryResult = 
			this.entityDescriptionQueryService.
				getResourceSummaries(
						query,
						filterComponent,
						restrictions, 
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
			@RequestBody Query query,
			EntityDescriptionQueryServiceRestrictions restrictions,
			FilterComponent filterComponent) {
		
		int count = 
			this.entityDescriptionQueryService.
				count(query,
						filterComponent,
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
	public EntityDescriptionMsg getEntityDescriptionByName(
			HttpServletRequest httpServletRequest,
			@PathVariable(VAR_CODESYSTEMID) String codeSystemName,
			@PathVariable(VAR_CODESYSTEMVERSIONID) String codeSystemVersionName,
			@PathVariable(VAR_ENTITYID) String entityName) {

		
		EntityDescription entity = 
			this.entityDescriptionReadService.
				read(new EntityDescriptionName(
						getScopedEntityName(entityName, codeSystemName),
						codeSystemVersionName));
		
		EntityDescriptionMsg msg = new EntityDescriptionMsg();
		msg.setEntityDescription(entity);
		
		msg = this.wrapMessage(msg, httpServletRequest);
		
		return msg;
	}
	
	/**
	 * Gets the entity description by uri.
	 *
	 * @param httpServletRequest the http servlet request
	 * @param uri the uri
	 * @return the entity description by uri
	 */
	@RequestMapping(value=PATH_ENTITYBYURI, method=RequestMethod.GET)
	@ResponseBody
	public EntityDescriptionMsg getEntityDescriptionByUri(
			HttpServletRequest httpServletRequest,
			@PathVariable(VAR_URI) String uri) {
		/*
		uri = this.decodeUri(uri);
		
		String name = 
			this.entityDescriptionService.
				getEntityDescriptionNameFromUri(uri);
		
		String path = "../../" + ENTITY +"/" + name;
		
		return this.redirect(path, httpServletRequest, VAR_URI);
		*/
		//TODO!!
		return null;
	}
}