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
package edu.mayo.cts2.framework.service.profile.resolvedvalueset;

import edu.mayo.cts2.framework.model.command.Page;
import edu.mayo.cts2.framework.model.command.ResolvedFilter;
import edu.mayo.cts2.framework.model.core.EntityReferenceList;
import edu.mayo.cts2.framework.model.core.SortCriteria;
import edu.mayo.cts2.framework.model.core.URIAndEntityName;
import edu.mayo.cts2.framework.model.directory.DirectoryResult;
import edu.mayo.cts2.framework.model.entity.EntityDescription;
import edu.mayo.cts2.framework.model.entity.EntityDirectoryEntry;
import edu.mayo.cts2.framework.model.service.core.EntityNameOrURI;
import edu.mayo.cts2.framework.model.service.core.types.StructuralProfile;
import edu.mayo.cts2.framework.model.valuesetdefinition.ResolvedValueSet;
import edu.mayo.cts2.framework.model.valuesetdefinition.ValueSetDefinition;
import edu.mayo.cts2.framework.service.profile.BaseQueryService;
import edu.mayo.cts2.framework.service.profile.Cts2Profile;
import edu.mayo.cts2.framework.service.profile.StructuralConformance;
import edu.mayo.cts2.framework.service.profile.resolvedvalueset.name.ResolvedValueSetReadId;
import edu.mayo.cts2.framework.service.profile.valuesetdefinition.ResolvedValueSetResolutionEntityQuery;
import edu.mayo.cts2.framework.service.profile.valuesetdefinition.ResolvedValueSetResult;
import edu.mayo.cts2.framework.service.profile.valuesetdefinition.ValueSetDefinitionResolutionService;

import java.util.Set;

/**
 * A CTS2 Service that exposes pre-resolved {@link ResolvedValueSet} content in
 * various different formats.
 * 
 * NOTE: To resolve a {@link ValueSetDefinition} dynamically, see {@link ValueSetDefinitionResolutionService}.
 * 
 * @see ValueSetDefinitionResolutionService
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@StructuralConformance(StructuralProfile.SP_VALUE_SET_RESOLUTION)
public interface ResolvedValueSetResolutionService extends BaseQueryService,
		Cts2Profile {

	/**
	 * Gets the resolution.
	 * 
	 * @param identifier
	 *            the identifier
	 * @param filterComponent
	 *            the filter component
	 * @param page
	 *            the page
	 * @return the resolution
	 */
	public ResolvedValueSetResult<URIAndEntityName> getResolution(
			ResolvedValueSetReadId identifier,
			Set<ResolvedFilter> filterComponent, 
			Page page);

	/**
	 * Gets the resolution.
	 * 
	 * @param identifier
	 *            the identifier
	 * @return the resolution
	 */
	public ResolvedValueSet getResolution(ResolvedValueSetReadId identifier);

	public ResolvedValueSetResult<EntityDirectoryEntry> getEntities(
			ResolvedValueSetReadId identifier,
			ResolvedValueSetResolutionEntityQuery query,
			SortCriteria sortCriteria, 
			Page page);

	public DirectoryResult<EntityDescription> getEntityList(
			ResolvedValueSetReadId identifier,
			ResolvedValueSetResolutionEntityQuery query,
			SortCriteria sortCriteria, 
			Page page);

    /**
     * Contains.
     */
    public EntityReferenceList contains(
            ResolvedValueSetReadId identifier,
            Set<EntityNameOrURI> entities);
}
