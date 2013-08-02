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

import java.util.List;

import edu.mayo.cts2.framework.model.command.Page;
import edu.mayo.cts2.framework.model.command.ResolvedReadContext;
import edu.mayo.cts2.framework.model.core.CodeSystemReference;
import edu.mayo.cts2.framework.model.core.CodeSystemVersionReference;
import edu.mayo.cts2.framework.model.core.EntityReference;
import edu.mayo.cts2.framework.model.core.SortCriteria;
import edu.mayo.cts2.framework.model.core.VersionTagReference;
import edu.mayo.cts2.framework.model.directory.DirectoryResult;
import edu.mayo.cts2.framework.model.entity.EntityDescription;
import edu.mayo.cts2.framework.model.entity.EntityListEntry;
import edu.mayo.cts2.framework.model.service.core.EntityNameOrURI;
import edu.mayo.cts2.framework.model.service.core.types.StructuralProfile;
import edu.mayo.cts2.framework.service.profile.Cts2Profile;
import edu.mayo.cts2.framework.service.profile.ReadService;
import edu.mayo.cts2.framework.service.profile.StructuralConformance;
import edu.mayo.cts2.framework.service.profile.entitydescription.name.EntityDescriptionReadId;

/**
 * The CTS2 EntityDescription Read Service interface.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@StructuralConformance(StructuralProfile.SP_ENTITY_DESCRIPTION)
public interface EntityDescriptionReadService extends
		ReadService<EntityDescription, EntityDescriptionReadId>, Cts2Profile {

	/**
	 * Read entity descriptions.
	 *
	 * @param entityId the entity id
	 * @param sortCriteria the sort criteria
	 * @param readContext the read context
	 * @param page the page
	 * @return the directory result
	 */
	public DirectoryResult<EntityListEntry> readEntityDescriptions(
			EntityNameOrURI entityId, 
			SortCriteria sortCriteria,
			ResolvedReadContext readContext, 
			Page page);

	/**
	 * Available descriptions.
	 *
	 * @param entityId the entity id
	 * @param readContext the read context
	 * @return the entity reference
	 */
	public EntityReference availableDescriptions(
			EntityNameOrURI entityId,
			ResolvedReadContext readContext);

	/**
	 * Read entity descriptions.
	 *
	 * @param entityId the entity id
	 * @param readContext the read context
	 * @return the entity list
	 */
	public List<EntityListEntry> readEntityDescriptions(
			EntityNameOrURI entityId,
			ResolvedReadContext readContext);

	/**
	 * Gets the known code systems.
	 *
	 * @return the known code systems
	 */
	public List<CodeSystemReference> getKnownCodeSystems();

	/**
	 * Gets the known code system versions.
	 *
	 * @return the known code system versions
	 */
	public List<CodeSystemVersionReference> getKnownCodeSystemVersions();

	/**
	 * Gets the supported version tags.
	 *
	 * @return the supported version tags
	 */
	public List<VersionTagReference> getSupportedVersionTags();
}
