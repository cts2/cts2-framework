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
package edu.mayo.cts2.framework.service.profile.association;

import edu.mayo.cts2.framework.model.association.Association;
import edu.mayo.cts2.framework.model.association.AssociationDirectoryEntry;
import edu.mayo.cts2.framework.model.association.GraphNode;
import edu.mayo.cts2.framework.model.association.types.GraphDirection;
import edu.mayo.cts2.framework.model.association.types.GraphFocus;
import edu.mayo.cts2.framework.model.command.Page;
import edu.mayo.cts2.framework.model.command.ResolvedReadContext;
import edu.mayo.cts2.framework.model.directory.DirectoryResult;
import edu.mayo.cts2.framework.model.entity.EntityDirectoryEntry;
import edu.mayo.cts2.framework.model.entity.EntityList;
import edu.mayo.cts2.framework.service.command.restriction.AssociationQueryServiceRestrictions;
import edu.mayo.cts2.framework.service.command.restriction.EntityDescriptionQueryServiceRestrictions;
import edu.mayo.cts2.framework.service.profile.QueryService;
import edu.mayo.cts2.framework.service.profile.entitydescription.EntityDescriptionQuery;
import edu.mayo.cts2.framework.service.profile.entitydescription.name.EntityDescriptionReadId;

/**
 * The Interface AssociationQueryService.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface AssociationQueryService extends 
	QueryService<Association, AssociationDirectoryEntry, AssociationQuery>{
	
	/**
	 * Gets the children associations of entity.
	 *
	 * @param query the query
	 * @param filterComponent the filter component
	 * @param page the page
	 * @param id the id
	 * @return the children associations of entity
	 */
	public DirectoryResult<EntityDirectoryEntry> getChildrenAssociationsOfEntity(
			EntityDescriptionReadId entity,
			EntityDescriptionQuery query,		
			ResolvedReadContext readContext,
			Page page);
	
	public DirectoryResult<EntityDirectoryEntry> getChildrenAssociationsOfEntityList(
			EntityDescriptionReadId entity,
			EntityDescriptionQuery query,		
			ResolvedReadContext readContext,
			Page page);
	
	public DirectoryResult<EntityDirectoryEntry> getSourceEntities(
			AssociationQueryServiceRestrictions associationRestrictions,
			EntityDescriptionQueryServiceRestrictions entityRestrictions,	
			ResolvedReadContext readContext,
			Page page);
	
	public DirectoryResult<EntityList> getSourceEntitiesList(
			AssociationQueryServiceRestrictions associationRestrictions,
			EntityDescriptionQueryServiceRestrictions entityRestrictions,	
			ResolvedReadContext readContext,
			Page page);
	
	public DirectoryResult<EntityDirectoryEntry> getTargetEntities(
			AssociationQuery associationQuery,
			EntityDescriptionQuery entityDescriptionQuery,	
			ResolvedReadContext readContext,
			Page page);
	
	public DirectoryResult<EntityList> getTargetEntitiesList(
			AssociationQuery associationQuery,
			EntityDescriptionQuery entityDescriptionQuery,	
			ResolvedReadContext readContext,
			Page page);
	
	public DirectoryResult<EntityDirectoryEntry> getAllSourceAndTargetEntities(
			AssociationQuery associationQuery,
			EntityDescriptionQuery entityDescriptionQuery,	
			ResolvedReadContext readContext,
			Page page);
	
	public DirectoryResult<EntityList> getAllSourceAndTargetEntitiesList(
			AssociationQuery associationQuery,
			EntityDescriptionQuery entityDescriptionQuery,	
			ResolvedReadContext readContext,
			Page page);
	
	public DirectoryResult<EntityDirectoryEntry> getPredicates(
			AssociationQuery associationQuery,
			EntityDescriptionQuery entityDescriptionQuery,	
			ResolvedReadContext readContext,
			Page page);
	
	public DirectoryResult<EntityList> getPredicatesList(
			AssociationQuery associationQuery,
			EntityDescriptionQuery entityDescriptionQuery,	
			ResolvedReadContext readContext,
			Page page);

	
	/**
	 * Gets the Association Graph result.
	 * 
	 * NOTE: If the 'focusType' is "SPECIFIC_ENTITY", 'focusEntity' must be populated,
	 * otherwise, it must be null
	 *
	 * @param focusType the focus type
	 * @param focus the focus
	 * @param direction the direction
	 * @param depth the depth
	 * @return the association graph
	 */
	public DirectoryResult<GraphNode> getAssociationGraph(
			GraphFocus focusType,
			EntityDescriptionReadId focusEntity, 
			GraphDirection direction,
			long depth);
}
