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

import javax.annotation.Resource;
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

import edu.mayo.cts2.framework.model.association.Association;
import edu.mayo.cts2.framework.model.association.AssociationDirectory;
import edu.mayo.cts2.framework.model.association.AssociationDirectoryEntry;
import edu.mayo.cts2.framework.model.association.AssociationGraph;
import edu.mayo.cts2.framework.model.association.AssociationMsg;
import edu.mayo.cts2.framework.model.association.GraphNode;
import edu.mayo.cts2.framework.model.association.types.GraphDirection;
import edu.mayo.cts2.framework.model.association.types.GraphFocus;
import edu.mayo.cts2.framework.model.command.Page;
import edu.mayo.cts2.framework.model.command.ResolvedFilter;
import edu.mayo.cts2.framework.model.core.Message;
import edu.mayo.cts2.framework.model.directory.DirectoryResult;
import edu.mayo.cts2.framework.model.entity.EntityDirectory;
import edu.mayo.cts2.framework.model.entity.EntityDirectoryEntry;
import edu.mayo.cts2.framework.model.service.association.AdvancedAssociationQueryService;
import edu.mayo.cts2.framework.model.service.core.Query;
import edu.mayo.cts2.framework.model.service.core.QueryControl;
import edu.mayo.cts2.framework.model.service.exception.UnknownAssociation;
import edu.mayo.cts2.framework.model.updates.ChangeableResourceChoice;
import edu.mayo.cts2.framework.model.util.ModelUtils;
import edu.mayo.cts2.framework.service.command.restriction.AssociationQueryServiceRestrictions;
import edu.mayo.cts2.framework.service.profile.association.AssociationMaintenanceService;
import edu.mayo.cts2.framework.service.profile.association.AssociationQueryService;
import edu.mayo.cts2.framework.service.profile.association.AssociationReadService;
import edu.mayo.cts2.framework.service.profile.association.name.AssociationReadId;
import edu.mayo.cts2.framework.service.profile.codesystemversion.CodeSystemVersionReadService;
import edu.mayo.cts2.framework.service.profile.entitydescription.name.EntityDescriptionReadId;
import edu.mayo.cts2.framework.webapp.rest.command.RestFilter;
import edu.mayo.cts2.framework.webapp.rest.command.RestReadContext;
import edu.mayo.cts2.framework.webapp.rest.naming.CodeSystemVersionNameResolver;

/**
 * The Class AssociationController.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Controller
public class AssociationController extends AbstractServiceAwareController {
	
	@Cts2Service
	private AssociationReadService associationReadService;
	
	@Cts2Service
	private AssociationMaintenanceService associationMaintenanceService;
	
	@Cts2Service
	private AssociationQueryService associationQueryService;

	@Resource
	private CodeSystemVersionNameResolver codeSystemVersionNameResolver;

	private AdvancedAssociationQueryService advancedAssociationQueryService;
	
	@Cts2Service
	private CodeSystemVersionReadService codeSystemVersionReadService;
	
	private static UrlTemplateBinder<Association> URL_BINDER = new 
			UrlTemplateBinder<Association>(){

		@Override
		public Map<String,String> getPathValues(Association resource) {
			Map<String,String> returnMap = new HashMap<String,String>();
			returnMap.put(VAR_ASSOCIATIONID,resource.getAssociationID());
			
			return returnMap;
		}

	};
	
	private final static MessageFactory<Association> MESSAGE_FACTORY = 
			new MessageFactory<Association>() {

		@Override
		public Message createMessage(Association resource) {
			AssociationMsg msg = new AssociationMsg();
			msg.setAssociation(resource);

			return msg;
		}
	};
	
	/**
	 * Gets the children associations of entity.
	 *
	 * @param httpServletRequest the http servlet request
	 * @param resolvedFilter the filter
	 * @param page the page
	 * @param codeSystemName the code system name
	 * @param codeSystemVersionName the code system version name
	 * @param entityName the entity name
	 * @return the children associations of entity
	 */
	@RequestMapping(value=PATH_CHILDREN_ASSOCIATIONS_OF_ENTITY, method=RequestMethod.GET)
	@ResponseBody
	public EntityDirectory getChildrenAssociationsOfEntity(
			HttpServletRequest httpServletRequest,
			QueryControl queryControl,
			RestFilter restFilter,
			Page page,
			@PathVariable(VAR_CODESYSTEMID) String codeSystemName,
			@PathVariable(VAR_CODESYSTEMVERSIONID) String codeSystemVersionId,
			@PathVariable(VAR_ENTITYID) String entityName) {
		
		return this.getChildrenAssociationsOfEntity(
				httpServletRequest, 
				queryControl,
				null, 
				restFilter, 
				page, 
				codeSystemName, 
				codeSystemVersionId,
				entityName);
	}
	
	/**
	 * Gets the children associations of entity.
	 *
	 * @param httpServletRequest the http servlet request
	 * @param query the query
	 * @param resolvedFilter the filter
	 * @param page the page
	 * @param codeSystemName the code system name
	 * @param codeSystemVersionName the code system version name
	 * @param entityName the entity name
	 * @return the children associations of entity
	 */
	@RequestMapping(value=PATH_CHILDREN_ASSOCIATIONS_OF_ENTITY, method=RequestMethod.POST)
	@ResponseBody
	public EntityDirectory getChildrenAssociationsOfEntity(
			HttpServletRequest httpServletRequest,
			QueryControl queryControl,
			Query query,
			RestFilter resolvedFilter,
			Page page,
			@PathVariable(VAR_CODESYSTEMID) String codeSystemName,
			@PathVariable(VAR_CODESYSTEMVERSIONID) String codeSystemVersionId,
			@PathVariable(VAR_ENTITYID) String entityName) {
		String codeSystemVersionName = this.codeSystemVersionNameResolver.getCodeSystemVersionNameFromVersionId(
				codeSystemVersionReadService, codeSystemName, codeSystemVersionId);
		ResolvedFilter filterComponent = this.processFilter(resolvedFilter, this.associationQueryService);
		
		EntityDescriptionReadId name = 
				new EntityDescriptionReadId(
						this.getScopedEntityName(entityName, codeSystemName), 
						ModelUtils.nameOrUriFromName(codeSystemVersionName));
		
		DirectoryResult<EntityDirectoryEntry> directoryResult = 
			this.associationQueryService.getChildrenAssociationsOfEntity(
					query, 
					createSet(filterComponent),
					name,
					null,//TODO
					null,//TODO
					page);
		
		EntityDirectory directory = this.populateDirectory(
				directoryResult, 
				page, 
				httpServletRequest, 
				EntityDirectory.class);
		
		return directory;
	}
	
	/**
	 * Gets the associations of code system version.
	 *
	 * @param httpServletRequest the http servlet request
	 * @param resolvedFilter the filter
	 * @param associationRestrictions the association restrictions
	 * @param page the page
	 * @param codeSystemName the code system name
	 * @param codeSystemVersionName the code system version name
	 * @return the associations of code system version
	 */
	@RequestMapping(value=PATH_ASSOCIATIONS_OF_CODESYSTEMVERSION, method=RequestMethod.GET)
	@ResponseBody
	public AssociationDirectory getAssociationsOfCodeSystemVersion(
			HttpServletRequest httpServletRequest,
			ResolvedFilter resolvedFilter,
			AssociationQueryServiceRestrictions associationRestrictions,
			Page page,
			@PathVariable(VAR_CODESYSTEMID) String codeSystemName,
			@PathVariable(VAR_CODESYSTEMVERSIONID) String versionId) {
		
		String codeSystemVersionName = 
				codeSystemVersionNameResolver.getCodeSystemVersionNameFromVersionId(
						this.codeSystemVersionReadService,
						codeSystemName, 
						versionId);
		
		associationRestrictions.setCodeSystemVersion(ModelUtils.nameOrUriFromName(codeSystemVersionName));
		
		return 
				this.getAssociations(
						httpServletRequest, 
						null, 
						associationRestrictions, 
						page);
	}
	
	/**
	 * Gets the associations.
	 *
	 * @param httpServletRequest the http servlet request
	 * @param query the query
	 * @param resolvedFilter the filter
	 * @param associationRestrictions the association restrictions
	 * @param page the page
	 * @return the associations
	 */
	@RequestMapping(value=PATH_ASSOCIATIONS, method=RequestMethod.POST)
	@ResponseBody
	public AssociationDirectory getAssociations(
			HttpServletRequest httpServletRequest,
			Query query,
			RestFilter resolvedFilter,
			AssociationQueryServiceRestrictions associationRestrictions,
			Page page) {
	
		ResolvedFilter filterComponent = this.processFilter(resolvedFilter, this.associationQueryService);
		
		DirectoryResult<AssociationDirectoryEntry> directoryResult = 
			this.associationQueryService.
				getResourceSummaries(query, createSet(filterComponent), associationRestrictions, null, page);
		
		AssociationDirectory directory = this.populateDirectory(
				directoryResult, 
				page, 
				httpServletRequest, 
				AssociationDirectory.class);
		
		return directory;
	}

	/**
	 * Gets the associations.
	 *
	 * @param httpServletRequest the http servlet request
	 * @param resolvedFilter the filter
	 * @param associationRestrictions the association restrictions
	 * @param page the page
	 * @return the associations
	 */
	@RequestMapping(value=PATH_ASSOCIATIONS, method=RequestMethod.GET)
	@ResponseBody
	public AssociationDirectory getAssociations(
			HttpServletRequest httpServletRequest,
			RestFilter resolvedFilter,
			AssociationQueryServiceRestrictions associationRestrictions,
			Page page) {
		
		return 
				this.getAssociations(
						httpServletRequest, 
						null, 
						resolvedFilter,
						associationRestrictions, 
						page);
	}
	
	@RequestMapping(value=PATH_ASSOCIATION_OF_CODESYSTEMVERSION_BY_URI, method=RequestMethod.GET)
	@ResponseBody
	public Message getAssociationOfCodeSystemVersionByUri(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse,
			RestReadContext restReadContext,
			QueryControl queryControl,
			@PathVariable(VAR_CODESYSTEMID) String codeSystemName,
			@PathVariable(VAR_CODESYSTEMVERSIONID) String codeSystemVersionName,
			@RequestParam(VAR_URI) String associationUri) {
	
		return this.doRead(
				httpServletRequest,
				MESSAGE_FACTORY, 
				this.associationReadService,
				restReadContext,
				UnknownAssociation.class,
				//TODO:
				new AssociationReadId(associationUri, null));
	}

	@RequestMapping(value=PATH_ASSOCIATIONBYURI, method=RequestMethod.GET)
	public ModelAndView getAssociationByUri(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse,
			QueryControl queryControl,
			@RequestParam(VAR_URI) String uri,
			@RequestParam(value="redirect", defaultValue="false") boolean redirect) {
	
		return this.doReadByUri(
				httpServletRequest, 
				MESSAGE_FACTORY, 
				PATH_ASSOCIATIONBYURI,
				PATH_ASSOCIATIONBYID, 
				URL_BINDER, 
				this.associationReadService,
				//TODO:
				new AssociationReadId(uri, null), 
				redirect);
	}
	
	/**
	 * Gets the graph code system version.
	 *
	 * @param httpServletRequest the http servlet request
	 * @param associationRestrictions the association restrictions
	 * @param page the page
	 * @param codeSystemName the code system name
	 * @param codeSystemVersionName the code system version name
	 * @param focus the focus
	 * @param direction the direction
	 * @param depth the depth
	 * @return the graph code system version
	 */
	@RequestMapping(value=PATH_GRAPH_OF_CODESYSTEMVERSION, method=RequestMethod.GET)
	@ResponseBody
	//TODO: Not complete
	public AssociationGraph getGraphCodeSystemVersion(
			HttpServletRequest httpServletRequest,
			AssociationQueryServiceRestrictions associationRestrictions,
			Page page,
			@PathVariable(VAR_CODESYSTEMID) String codeSystemName,
			@PathVariable(VAR_CODESYSTEMVERSIONID) String codeSystemVersionId,
			@RequestParam(required=true, defaultValue="TOP_NODE") String focus,
			@RequestParam(required=true, defaultValue="FORWARD") GraphDirection direction,
			@RequestParam(required=true, defaultValue="1") int depth) {
		
		String codeSystemVersionName = this.codeSystemVersionNameResolver.getCodeSystemVersionNameFromVersionId(
				codeSystemVersionReadService, codeSystemName, codeSystemVersionId);
		
		GraphFocus graphFocus;
		
		if(focus.equals(GraphFocus.TOP_NODE.toString())){
			graphFocus = GraphFocus.TOP_NODE;
		} else if(focus.equals(GraphFocus.BOTTOM_NODE.toString())){
			graphFocus = GraphFocus.BOTTOM_NODE;
		} else {
			graphFocus = GraphFocus.SPECIFIC_ENTITY;
		}
		
		DirectoryResult<GraphNode> directoryResult = 
			this.associationQueryService.getAssociationGraph(
					graphFocus, 
					new EntityDescriptionReadId(
							this.getScopedEntityName(focus, codeSystemName), 
							ModelUtils.nameOrUriFromName(codeSystemVersionName)),
					direction,
					depth);

		AssociationGraph graph = this.populateDirectory(
				directoryResult, 
				page, 
				httpServletRequest, 
				AssociationGraph.class);
		
		graph.setExpansionDirection(direction);
		graph.setExpansionDepth((long)depth);
		graph.setGraphFocus(graphFocus);
		
		//TODO: need to populate (resolve) focus entity.
		//we can populate based on what the user has passed
		//in, but we need to resolve the URI/name.
		//graph.setFocusEntity(focusEntity)
		
		return graph;
	}
	
	@RequestMapping(value=PATH_SOURCEOF_ASSOCIATIONS_OF_ENTITY, method=RequestMethod.GET)
	@ResponseBody
	public AssociationDirectory getSourceOfAssociationsOfEntity(
			HttpServletRequest httpServletRequest,
			AssociationQueryServiceRestrictions associationRestrictions,
			ResolvedFilter resolvedFilter,
			Page page,
			@PathVariable(VAR_CODESYSTEMID) String codeSystemName,
			@PathVariable(VAR_CODESYSTEMVERSIONID) String codeSystemVersionId,
			@PathVariable(VAR_ENTITYID) String entityName) {
		
		return this.getSourceOfAssociationsOfEntity(
				httpServletRequest, 
				null, 
				associationRestrictions, 
				resolvedFilter, 
				page, 
				codeSystemName, 
				codeSystemVersionId, 
				entityName);
	}
	
	/**
	 * Gets the source of associations of entity.
	 *
	 * @param httpServletRequest the http servlet request
	 * @param query the query
	 * @param associationRestrictions the association restrictions
	 * @param resolvedFilter the filter
	 * @param page the page
	 * @param codeSystemName the code system name
	 * @param codeSystemVersionName the code system version name
	 * @param entityName the entity name
	 * @return the source of associations of entity
	 */
	@RequestMapping(value=PATH_SOURCEOF_ASSOCIATIONS_OF_ENTITY, method=RequestMethod.POST)
	@ResponseBody
	public AssociationDirectory getSourceOfAssociationsOfEntity(
			HttpServletRequest httpServletRequest,
			Query query,
			AssociationQueryServiceRestrictions associationRestrictions,
			ResolvedFilter resolvedFilter,
			Page page,
			@PathVariable(VAR_CODESYSTEMID) String codeSystemName,
			@PathVariable(VAR_CODESYSTEMVERSIONID) String codeSystemVersionId,
			@PathVariable(VAR_ENTITYID) String entityName) {

		String codeSystemVersionName = this.codeSystemVersionNameResolver.getCodeSystemVersionNameFromVersionId(
				codeSystemVersionReadService, codeSystemName, codeSystemVersionId);
		
		associationRestrictions.setSourceEntity(
				ModelUtils.entityNameOrUriFromName(
						this.getScopedEntityName(entityName, codeSystemName)));
		
		return this.getAssociationsOfCodeSystemVersion(
				httpServletRequest, 
				resolvedFilter,
				associationRestrictions, 
				page, 
				codeSystemName,
				codeSystemVersionName);
	}
	
	/**
	 * Creates the association.
	 *
	 * @param httpServletRequest the http servlet request
	 * @param association the association
	 * @param changeseturi the changeseturi
	 * @param codeSystemName the code system name
	 * @param codeSystemVersionName the code system version name
	 * @param associationName the association name
	 */
	@RequestMapping(value=PATH_ASSOCIATION, method=RequestMethod.POST)
	@ResponseBody
	public void createAssociation(
			HttpServletRequest httpServletRequest,
			@RequestBody Association association,
			@RequestParam(required=false) String changeseturi) {
			
		ChangeableResourceChoice choice = new ChangeableResourceChoice();
		choice.setAssociation(association);
		
		this.getCreateHandler().create(
				choice,
				changeseturi,
				PATH_ASSOCIATIONBYID, 
				URL_BINDER, 
				this.associationMaintenanceService);
	}
	
	@RequestMapping(value=PATH_ASSOCIATIONBYID, method=RequestMethod.PUT)
	@ResponseBody
	public void updateAssociation(
			HttpServletRequest httpServletRequest,
			@RequestBody Association association,
			@RequestParam(required=false) String changeseturi,
			@PathVariable(VAR_CODESYSTEMID) String codeSystemName,
			@PathVariable(VAR_CODESYSTEMVERSIONID) String codeSystemVersionName,
			@PathVariable(VAR_ASSOCIATIONID) String associationUri) {
			
		ChangeableResourceChoice choice = new ChangeableResourceChoice();
		choice.setAssociation(association);
		
		this.getUpdateHandler().update(
				choice, 
				changeseturi,
				new AssociationReadId(associationUri, codeSystemVersionName), 
				this.associationMaintenanceService);
	}

	public AssociationReadService getAssociationReadService() {
		return associationReadService;
	}

	public void setAssociationReadService(
			AssociationReadService associationReadService) {
		this.associationReadService = associationReadService;
	}

	public AssociationMaintenanceService getAssociationMaintenanceService() {
		return associationMaintenanceService;
	}

	public void setAssociationMaintenanceService(
			AssociationMaintenanceService associationMaintenanceService) {
		this.associationMaintenanceService = associationMaintenanceService;
	}

	public AssociationQueryService getAssociationQueryService() {
		return associationQueryService;
	}

	public void setAssociationQueryService(
			AssociationQueryService associationQueryService) {
		this.associationQueryService = associationQueryService;
	}

	public CodeSystemVersionNameResolver getCodeSystemVersionNameResolver() {
		return codeSystemVersionNameResolver;
	}

	public void setCodeSystemVersionNameResolver(
			CodeSystemVersionNameResolver codeSystemVersionNameResolver) {
		this.codeSystemVersionNameResolver = codeSystemVersionNameResolver;
	}

	public AdvancedAssociationQueryService getAdvancedAssociationQueryService() {
		return advancedAssociationQueryService;
	}

	public void setAdvancedAssociationQueryService(
			AdvancedAssociationQueryService advancedAssociationQueryService) {
		this.advancedAssociationQueryService = advancedAssociationQueryService;
	}

	public CodeSystemVersionReadService getCodeSystemVersionReadService() {
		return codeSystemVersionReadService;
	}

	public void setCodeSystemVersionReadService(
			CodeSystemVersionReadService codeSystemVersionReadService) {
		this.codeSystemVersionReadService = codeSystemVersionReadService;
	}
}
