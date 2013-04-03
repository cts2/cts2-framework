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
package edu.mayo.cts2.framework.service.profile.entitydescription;

import java.util.Set;

import edu.mayo.cts2.framework.model.command.ResolvedReadContext;
import edu.mayo.cts2.framework.model.core.EntityReferenceList;
import edu.mayo.cts2.framework.model.core.VersionTagReference;
import edu.mayo.cts2.framework.model.entity.EntityDescription;
import edu.mayo.cts2.framework.model.entity.EntityDirectoryEntry;
import edu.mayo.cts2.framework.model.service.core.EntityNameOrURI;
import edu.mayo.cts2.framework.model.service.core.EntityNameOrURIList;
import edu.mayo.cts2.framework.model.service.core.types.StructuralProfile;
import edu.mayo.cts2.framework.service.profile.Cts2Profile;
import edu.mayo.cts2.framework.service.profile.QueryService;
import edu.mayo.cts2.framework.service.profile.StructuralConformance;

/**
 * The Interface EntityDescriptionQueryService.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@StructuralConformance(StructuralProfile.SP_ENTITY_DESCRIPTION)
public interface EntityDescriptionQueryService extends 
	QueryService<EntityDescription, EntityDirectoryEntry, EntityDescriptionQuery>, Cts2Profile {

	/**
	 * Checks if is entity in set.
	 *
	 * @param entity the entity
	 * @param restrictions the restrictions
	 * @param readContext the read context
	 * @return true, if is entity in set
	 */
	public boolean isEntityInSet(
			EntityNameOrURI entity,
			EntityDescriptionQuery restrictions,
			ResolvedReadContext readContext);
	
	/**
	 * Resolve as entity reference list.
	 *
	 * @param restrictions the restrictions
	 * @param readContext the read context
	 * @return the entity reference list
	 */
	public EntityReferenceList resolveAsEntityReferenceList(
			EntityDescriptionQuery restrictions,
			ResolvedReadContext readContext);
	
	/**
	 * Intersect entity list.
	 *
	 * @param entities the entities
	 * @param restrictions the restrictions
	 * @param readContext the read context
	 * @return the entity name or uri list
	 */
	public EntityNameOrURIList intersectEntityList(
			Set<EntityNameOrURI> entities,
			EntityDescriptionQuery restrictions,
			ResolvedReadContext readContext);

	/**
	 * Gets the supported tags.
	 *
	 * @return the supported tags
	 */
	public Set<? extends VersionTagReference> getSupportedTags();
}
