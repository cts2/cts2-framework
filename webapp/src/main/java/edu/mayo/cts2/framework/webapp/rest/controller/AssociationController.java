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
import org.springframework.web.servlet.ModelAndView;

import edu.mayo.cts2.framework.model.association.Association;
import edu.mayo.cts2.framework.model.association.AssociationDirectory;
import edu.mayo.cts2.framework.model.association.AssociationGraph;
import edu.mayo.cts2.framework.model.association.AssociationList;
import edu.mayo.cts2.framework.model.association.AssociationMsg;
import edu.mayo.cts2.framework.model.association.GraphNode;
import edu.mayo.cts2.framework.model.association.types.GraphDirection;
import edu.mayo.cts2.framework.model.association.types.GraphFocus;
import edu.mayo.cts2.framework.model.command.Page;
import edu.mayo.cts2.framework.model.command.ResolvedReadContext;
import edu.mayo.cts2.framework.model.core.CodeSystemVersionReference;
import edu.mayo.cts2.framework.model.core.Message;
import edu.mayo.cts2.framework.model.directory.DirectoryResult;
import edu.mayo.cts2.framework.model.entity.EntityDirectory;
import edu.mayo.cts2.framework.model.entity.EntityList;
import edu.mayo.cts2.framework.model.service.core.NameOrURI;
import edu.mayo.cts2.framework.model.service.core.Query;
import edu.mayo.cts2.framework.model.service.exception.UnknownAssociation;
import edu.mayo.cts2.framework.model.util.ModelUtils;
import edu.mayo.cts2.framework.service.command.restriction.AssociationQueryServiceRestrictions;
import edu.mayo.cts2.framework.service.command.restriction.EntityDescriptionQueryServiceRestrictions;
import edu.mayo.cts2.framework.service.command.restriction.EntityDescriptionQueryServiceRestrictions.HierarchyRestriction;
import edu.mayo.cts2.framework.service.command.restriction.EntityDescriptionQueryServiceRestrictions.HierarchyRestriction.HierarchyType;
import edu.mayo.cts2.framework.service.profile.association.AssociationMaintenanceService;
import edu.mayo.cts2.framework.service.profile.association.AssociationQuery;
import edu.mayo.cts2.framework.service.profile.association.AssociationQueryService;
import edu.mayo.cts2.framework.service.profile.association.AssociationReadService;
import edu.mayo.cts2.framework.service.profile.association.name.AssociationReadId;
import edu.mayo.cts2.framework.service.profile.codesystemversion.CodeSystemVersionReadService;
import edu.mayo.cts2.framework.service.profile.entitydescription.EntityDescriptionQuery;
import edu.mayo.cts2.framework.service.profile.entitydescription.EntityDescriptionQueryService;
import edu.mayo.cts2.framework.service.profile.entitydescription.name.EntityDescriptionReadId;
import edu.mayo.cts2.framework.webapp.naming.CodeSystemVersionNameResolver;
import edu.mayo.cts2.framework.webapp.rest.command.QueryControl;
import edu.mayo.cts2.framework.webapp.rest.command.RestFilter;
import edu.mayo.cts2.framework.webapp.rest.command.RestReadContext;
import edu.mayo.cts2.framework.webapp.rest.query.AssociationQueryBuilder;
import edu.mayo.cts2.framework.webapp.rest.query.EntityQueryBuilder;

/**
 * The Class AssociationController.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Controller
public class AssociationController extends AbstractMessageWrappingController {
	
	@Cts2Service
	private AssociationReadService associationReadService;
	
	@Cts2Service
	private AssociationMaintenanceService associationMaintenanceService;
	
	@Cts2Service
	private AssociationQueryService associationQueryService;

	@Resource
	private CodeSystemVersionNameResolver codeSystemVersionNameResolver;
	
	@Cts2Service
	private EntityDescriptionQueryService entityDescriptionQueryService;

	@Cts2Service
	private CodeSystemVersionReadService codeSystemVersionReadService;
	
	private static UrlTemplateBinder<Association> URL_BINDER = new 
			UrlTemplateBinder<Association>(){

		@Override
		public Map<String,String> getPathValues(Association resource) {
			Map<String,String> returnMap = new HashMap<String,String>();
			
			CodeSystemVersionReference ref = getAssertedIn(resource);
			
			returnMap.put(VAR_CODESYSTEMID,
					ref.getCodeSystem().getContent());
			
			returnMap.put(VAR_CODESYSTEMVERSIONID,
					ref.getVersion().getContent());
			
			returnMap.put(VAR_ASSOCIATIONID,
					resource.getLocalID());
			
			return returnMap;
		}

	};
	
	private static CodeSystemVersionReference getAssertedIn(Association resource){
		if(resource.getAssertedIn() != null){
			return resource.getAssertedIn();
		} else {
			return resource.getAssertedBy();
		}
	}
	
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
	public Object getChildrenAssociationsOfEntity(
			HttpServletRequest httpServletRequest,
			RestReadContext restReadContext,
			QueryControl queryControl,
			EntityDescriptionQueryServiceRestrictions restrictions,
			RestFilter restFilter,
			Page page,
			boolean list,
			@PathVariable(VAR_CODESYSTEMID) String codeSystemName,
			@PathVariable(VAR_CODESYSTEMVERSIONID) String codeSystemVersionId,
			@PathVariable(VAR_ENTITYID) String entityName) {
		
		return this.getChildrenAssociationsOfEntity(
				httpServletRequest, 
				restReadContext,
				queryControl,
				null, 
				restrictions,
				restFilter, 
				page, 
				list, 
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
	public Object getChildrenAssociationsOfEntity(
			HttpServletRequest httpServletRequest,
			RestReadContext restReadContext,
			QueryControl queryControl,
			@RequestBody Query query,
			EntityDescriptionQueryServiceRestrictions restrictions,
			RestFilter restFilter,
			Page page,
			boolean list,
			@PathVariable(VAR_CODESYSTEMID) String codeSystemName,
			@PathVariable(VAR_CODESYSTEMVERSIONID) String codeSystemVersionId,
			@PathVariable(VAR_ENTITYID) String entityName) {

		ResolvedReadContext readContext = this.resolveRestReadContext(restReadContext);
		
		String codeSystemVersionName = this.codeSystemVersionNameResolver.getCodeSystemVersionNameFromVersionId(
				codeSystemVersionReadService, 
				codeSystemName, 
				codeSystemVersionId,
				readContext);
			
		NameOrURI codeSystemVersionNameOrUri = ModelUtils.nameOrUriFromName(codeSystemVersionName);
		
		EntityDescriptionReadId entity = 
				new EntityDescriptionReadId(
						this.getScopedEntityName(entityName, codeSystemName), 
						codeSystemVersionNameOrUri);
		
		HierarchyRestriction hierarchyRestriction = new HierarchyRestriction();
		hierarchyRestriction.setEntity(entity);
		hierarchyRestriction.setHierarchyType(HierarchyType.CHILDREN);
	
		restrictions.setHierarchyRestriction(hierarchyRestriction);
		restrictions.getCodeSystemVersions().add(codeSystemVersionNameOrUri);
		
		EntityQueryBuilder builder = 
				this.getNewEntityQueryBuilder();
		
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
	public Object getAssociationsOfCodeSystemVersion(
			HttpServletRequest httpServletRequest,
			RestReadContext restReadContext,
			QueryControl queryControl,
			RestFilter restFilter,
			AssociationQueryServiceRestrictions associationRestrictions,
			Page page,
			boolean list,
			@PathVariable(VAR_CODESYSTEMID) String codeSystemName,
			@PathVariable(VAR_CODESYSTEMVERSIONID) String versionId) {
		
		ResolvedReadContext readContext = this.resolveRestReadContext(restReadContext);
		
		String codeSystemVersionName = 
				codeSystemVersionNameResolver.getCodeSystemVersionNameFromVersionId(
						this.codeSystemVersionReadService,
						codeSystemName, 
						versionId,
						readContext);
		
		associationRestrictions.setCodeSystemVersion(ModelUtils.nameOrUriFromName(codeSystemVersionName));
		
		return this.getAssociations(
				httpServletRequest, 
				restReadContext,
				queryControl,
				restFilter, 
				associationRestrictions, 
				page,
				list);
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
	public Object getAssociations(
			HttpServletRequest httpServletRequest,
			RestReadContext restReadContext,
			QueryControl queryControl,
			@RequestBody Query query,
			RestFilter restFilter,
			AssociationQueryServiceRestrictions restrictions,
			Page page,
			boolean list) {
	
		AssociationQueryBuilder builder = this.getNewResourceQueryBuilder();
		
		AssociationQuery resourceQuery = builder.
				addQuery(query).
				addRestFilter(restFilter).
				addRestrictions(restrictions).
				addRestReadContext(restReadContext).
				build();
		
		return this.doQuery(
				httpServletRequest,
				list, 
				this.associationQueryService,
				resourceQuery,
				page, 
				queryControl,
				AssociationDirectory.class, 
				AssociationList.class);
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
	public Object getAssociations(
			HttpServletRequest httpServletRequest,
			RestReadContext restReadContext,
			QueryControl queryControl,
			RestFilter restFilter,
			AssociationQueryServiceRestrictions associationRestrictions,
			Page page, 
			boolean list) {
		
		return 
				this.getAssociations(
						httpServletRequest, 
						restReadContext,
						queryControl,
						null,
						restFilter,
						associationRestrictions, 
						page,
						list);
	}
	
	@RequestMapping(value=PATH_ASSOCIATIONBYID, method=RequestMethod.GET)
	public Object getAssociationOfCodeSystemVersionByLocalName(
			HttpServletRequest httpServletRequest,
			RestReadContext restReadContext,
			QueryControl queryControl,
			@PathVariable(VAR_CODESYSTEMID) String codeSystemName,
			@PathVariable(VAR_CODESYSTEMVERSIONID) String codeSystemVersionName,
			@PathVariable(VAR_ASSOCIATIONID) String associationLocalName) {
	
		return this.doRead(
				httpServletRequest,
				MESSAGE_FACTORY, 
				this.associationReadService,
				restReadContext,
				UnknownAssociation.class,
				new AssociationReadId(
						associationLocalName, 
						ModelUtils.nameOrUriFromName(codeSystemVersionName)));
	}
	
	@RequestMapping(value=PATH_ASSOCIATION_OF_CODESYSTEMVERSION_BY_URI, method=RequestMethod.GET)
	public Object getAssociationOfCodeSystemVersionByUri(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse,
			RestReadContext restReadContext,
			QueryControl queryControl,
			@PathVariable(VAR_CODESYSTEMID) String codeSystemName,
			@PathVariable(VAR_CODESYSTEMVERSIONID) String codeSystemVersionName,
			@RequestParam(PARAM_URI) String associationUri) {
	
		return this.doRead(
				httpServletRequest,
				MESSAGE_FACTORY, 
				this.associationReadService,
				restReadContext,
				UnknownAssociation.class,
				new AssociationReadId(associationUri));
	}

	@RequestMapping(value=PATH_ASSOCIATIONBYURI, method=RequestMethod.GET)
	public ModelAndView getAssociationByUri(
			HttpServletRequest httpServletRequest,
			RestReadContext restReadContext,
			QueryControl queryControl,
			@RequestParam(PARAM_URI) String uri,
			@RequestParam(value="redirect", defaultValue="false") boolean redirect) {
	
		return this.doReadByUri(
				httpServletRequest, 
				MESSAGE_FACTORY, 
				PATH_ASSOCIATIONBYURI,
				PATH_ASSOCIATIONBYID, 
				URL_BINDER, 
				this.associationReadService,
				restReadContext,
				UnknownAssociation.class,
				new AssociationReadId(uri), 
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
	//TODO: Not complete
	public Object getGraphCodeSystemVersion(
			HttpServletRequest httpServletRequest,
			RestReadContext restReadContext,
			AssociationQueryServiceRestrictions associationRestrictions,
			Page page,
			@PathVariable(VAR_CODESYSTEMID) String codeSystemName,
			@PathVariable(VAR_CODESYSTEMVERSIONID) String codeSystemVersionId,
			@RequestParam(required=true, defaultValue="TOP_NODE") String focus,
			@RequestParam(required=true, defaultValue="FORWARD") GraphDirection direction,
			@RequestParam(required=true, defaultValue="1") int depth) {
		
		ResolvedReadContext readContext = this.resolveRestReadContext(restReadContext);
		
		String codeSystemVersionName = this.codeSystemVersionNameResolver.getCodeSystemVersionNameFromVersionId(
				codeSystemVersionReadService, 
				codeSystemName, 
				codeSystemVersionId, 
				readContext);
		
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
		
		return this.buildResponse(httpServletRequest, graph);
	}
	
	@RequestMapping(value=PATH_SUBJECTOF_ASSOCIATIONS_OF_ENTITY, method=RequestMethod.GET)
	public Object getSubjectOfAssociationsOfEntity(
			HttpServletRequest httpServletRequest,
			RestReadContext restReadContext,
			QueryControl queryControl,
			AssociationQueryServiceRestrictions associationRestrictions,
			RestFilter resolvedFilter,
			Page page,
			boolean list,
			@PathVariable(VAR_CODESYSTEMID) String codeSystemName,
			@PathVariable(VAR_CODESYSTEMVERSIONID) String codeSystemVersionId,
			@PathVariable(VAR_ENTITYID) String entityName) {
		
		return this.getSubjectOfAssociationsOfEntity(
				httpServletRequest, 
				restReadContext,
				queryControl,
				null, 
				associationRestrictions, 
				resolvedFilter, 
				page, 
				list,
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
	@RequestMapping(value=PATH_SUBJECTOF_ASSOCIATIONS_OF_ENTITY, method=RequestMethod.POST)
	public Object getSubjectOfAssociationsOfEntity(
			HttpServletRequest httpServletRequest,
			RestReadContext restReadContext,
			QueryControl queryControl,
			@RequestBody Query query,
			AssociationQueryServiceRestrictions associationRestrictions,
			RestFilter resolvedFilter,
			Page page,
			boolean list,
			@PathVariable(VAR_CODESYSTEMID) String codeSystemName,
			@PathVariable(VAR_CODESYSTEMVERSIONID) String codeSystemVersionId,
			@PathVariable(VAR_ENTITYID) String entityName) {
		
		ResolvedReadContext readContext = this.resolveRestReadContext(restReadContext);

		String codeSystemVersionName = this.codeSystemVersionNameResolver.getCodeSystemVersionNameFromVersionId(
				codeSystemVersionReadService, 
				codeSystemName, 
				codeSystemVersionId, 
				readContext);
		
		associationRestrictions.setSourceEntity(
				ModelUtils.entityNameOrUriFromName(
						this.getScopedEntityName(entityName, codeSystemName)));
		
		return this.getAssociationsOfCodeSystemVersion(
				httpServletRequest, 
				restReadContext, 
				queryControl,
				resolvedFilter,
				associationRestrictions, 
				page, 
				list,
				codeSystemName,
				codeSystemVersionName);
	}
	
	@RequestMapping(value=PATH_TARGETOF_ASSOCIATIONS_OF_ENTITY, method=RequestMethod.GET)
	public Object getTargetOfAssociationsOfEntity(
			HttpServletRequest httpServletRequest,
			RestReadContext restReadContext,
			QueryControl queryControl,
			AssociationQueryServiceRestrictions associationRestrictions,
			RestFilter resolvedFilter,
			Page page,
			boolean list,
			@PathVariable(VAR_CODESYSTEMID) String codeSystemName,
			@PathVariable(VAR_CODESYSTEMVERSIONID) String codeSystemVersionId,
			@PathVariable(VAR_ENTITYID) String entityName) {
		
		return this.getTargetOfAssociationsOfEntity(
				httpServletRequest, 
				restReadContext, 
				queryControl,
				null,
				associationRestrictions,
				resolvedFilter,
				page,
				list, 
				codeSystemName, 
				codeSystemVersionId, 
				entityName);
	}
	
	@RequestMapping(value=PATH_TARGETOF_ASSOCIATIONS_OF_ENTITY, method=RequestMethod.POST)
	public Object getTargetOfAssociationsOfEntity(
			HttpServletRequest httpServletRequest,
			RestReadContext restReadContext,
			QueryControl queryControl,
			@RequestBody Query query,
			AssociationQueryServiceRestrictions associationRestrictions,
			RestFilter resolvedFilter,
			Page page,
			boolean list,
			@PathVariable(VAR_CODESYSTEMID) String codeSystemName,
			@PathVariable(VAR_CODESYSTEMVERSIONID) String codeSystemVersionId,
			@PathVariable(VAR_ENTITYID) String entityName) {
		
		ResolvedReadContext readContext = this.resolveRestReadContext(restReadContext);

		String codeSystemVersionName = this.codeSystemVersionNameResolver.getCodeSystemVersionNameFromVersionId(
				codeSystemVersionReadService, 
				codeSystemName, 
				codeSystemVersionId, 
				readContext);
		
		associationRestrictions.setTargetEntity(
				ModelUtils.entityNameOrUriFromName(
						this.getScopedEntityName(entityName, codeSystemName)));
		
		return this.getAssociationsOfCodeSystemVersion(
				httpServletRequest, 
				restReadContext, 
				queryControl,
				resolvedFilter,
				associationRestrictions, 
				page, 
				list,
				codeSystemName,
				codeSystemVersionName);
	}
	
	@RequestMapping(value=PATH_PREDICATEOF_ASSOCIATIONS_OF_ENTITY, method=RequestMethod.GET)
	public Object getPredicateOfAssociationsOfEntity(
			HttpServletRequest httpServletRequest,
			RestReadContext restReadContext,
			QueryControl queryControl,
			AssociationQueryServiceRestrictions associationRestrictions,
			RestFilter resolvedFilter,
			Page page,
			boolean list,
			@PathVariable(VAR_CODESYSTEMID) String codeSystemName,
			@PathVariable(VAR_CODESYSTEMVERSIONID) String codeSystemVersionId,
			@PathVariable(VAR_ENTITYID) String entityName) {
		
		return this.getPredicateOfAssociationsOfEntity(
				httpServletRequest, 
				restReadContext, 
				queryControl,
				null,
				associationRestrictions,
				resolvedFilter,
				page,
				list, 
				codeSystemName, 
				codeSystemVersionId, 
				entityName);
	}
	
	@RequestMapping(value=PATH_PREDICATEOF_ASSOCIATIONS_OF_ENTITY, method=RequestMethod.POST)
	public Object getPredicateOfAssociationsOfEntity(
			HttpServletRequest httpServletRequest,
			RestReadContext restReadContext,
			QueryControl queryControl,
			@RequestBody Query query,
			AssociationQueryServiceRestrictions associationRestrictions,
			RestFilter resolvedFilter,
			Page page,
			boolean list,
			@PathVariable(VAR_CODESYSTEMID) String codeSystemName,
			@PathVariable(VAR_CODESYSTEMVERSIONID) String codeSystemVersionId,
			@PathVariable(VAR_ENTITYID) String entityName) {
		
		ResolvedReadContext readContext = this.resolveRestReadContext(restReadContext);

		String codeSystemVersionName = this.codeSystemVersionNameResolver.getCodeSystemVersionNameFromVersionId(
				codeSystemVersionReadService, 
				codeSystemName, 
				codeSystemVersionId, 
				readContext);
		
		associationRestrictions.setPredicate(
				ModelUtils.entityNameOrUriFromName(
						this.getScopedEntityName(entityName, codeSystemName)));
		
		return this.getAssociationsOfCodeSystemVersion(
				httpServletRequest, 
				restReadContext, 
				queryControl,
				resolvedFilter,
				associationRestrictions, 
				page, 
				list,
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
	public Object createAssociation(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse,
			@RequestBody Association association,
			@RequestParam(value=PARAM_CHANGESETCONTEXT, required=false) String changeseturi) {
		
		return this.doCreate(
				httpServletResponse,
				association,
				changeseturi,
				PATH_ASSOCIATIONBYID, 
				URL_BINDER, 
				this.associationMaintenanceService);
	}
	
	@RequestMapping(value=PATH_ASSOCIATIONBYID, method=RequestMethod.PUT)
	public Object updateAssociation(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse,
			@RequestBody Association association,
			@RequestParam(value=PARAM_CHANGESETCONTEXT, required=false) String changeseturi,
			@PathVariable(VAR_CODESYSTEMID) String codeSystemName,
			@PathVariable(VAR_CODESYSTEMVERSIONID) String codeSystemVersionName,
			@PathVariable(VAR_ASSOCIATIONID) String associationLocalName) {
			
		return this.doUpdate(
				httpServletResponse,
				association, 
				changeseturi,
				new AssociationReadId(
						associationLocalName, 
						ModelUtils.nameOrUriFromName(codeSystemVersionName)), 		
				this.associationMaintenanceService);
	}
	
	@RequestMapping(value=PATH_ASSOCIATIONBYID, method=RequestMethod.DELETE)
	public Object deleteAssociation(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse,
			RestReadContext restReadContext,
			QueryControl queryControl,
			@PathVariable(VAR_CODESYSTEMID) String codeSystemName,
			@PathVariable(VAR_CODESYSTEMVERSIONID) String codeSystemVersionName,
			@PathVariable(VAR_ASSOCIATIONID) String associationLocalName,
			@RequestParam(PARAM_CHANGESETCONTEXT) String changeseturi) {
		
		AssociationReadId id = new AssociationReadId(
				associationLocalName, 
				ModelUtils.nameOrUriFromName(codeSystemVersionName));
	
		return 
				this.doDelete(
						httpServletResponse, 
						id, 
						changeseturi, 
						this.associationMaintenanceService);
	
	}
	
	private AssociationQueryBuilder getNewResourceQueryBuilder(){
		return new AssociationQueryBuilder(
			this.associationQueryService, 
			this.getFilterResolver(),
			this.getReadContextResolver());
	}
	
	private EntityQueryBuilder getNewEntityQueryBuilder(){
		return new EntityQueryBuilder(
			this.entityDescriptionQueryService, 
			this.getFilterResolver(),
			this.getReadContextResolver());
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
	
	public CodeSystemVersionReadService getCodeSystemVersionReadService() {
		return codeSystemVersionReadService;
	}

	public void setCodeSystemVersionReadService(
			CodeSystemVersionReadService codeSystemVersionReadService) {
		this.codeSystemVersionReadService = codeSystemVersionReadService;
	}
}
