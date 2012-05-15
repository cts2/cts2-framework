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
package edu.mayo.cts2.framework.service.profile.valuesetdefinition;

import java.util.Set;

import edu.mayo.cts2.framework.model.command.Page;
import edu.mayo.cts2.framework.model.command.ResolvedReadContext;
import edu.mayo.cts2.framework.model.core.EntitySynopsis;
import edu.mayo.cts2.framework.model.core.SortCriteria;
import edu.mayo.cts2.framework.model.entity.EntityDirectoryEntry;
import edu.mayo.cts2.framework.model.service.core.NameOrURI;
import edu.mayo.cts2.framework.model.service.core.types.StructuralProfile;
import edu.mayo.cts2.framework.model.valuesetdefinition.ResolvedValueSet;
import edu.mayo.cts2.framework.service.profile.BaseQueryService;
import edu.mayo.cts2.framework.service.profile.Cts2Profile;
import edu.mayo.cts2.framework.service.profile.StructuralConformance;
import edu.mayo.cts2.framework.service.profile.valuesetdefinition.name.ValueSetDefinitionReadId;

/**
 * The Interface ValueSetDefinitionResolutionService.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@StructuralConformance(StructuralProfile.SP_VALUE_SET_DEFINITION)
public interface ValueSetDefinitionResolutionService extends BaseQueryService, Cts2Profile {

	/**
	 * Resolve definition.
	 *
	 * @param definitionId the definition id
	 * @param codeSystemVersions the code system versions
	 * @param tag the tag
	 * @param query the query
	 * @param filterComponent the filter component
	 * @param readContext the read context
	 * @param page the page
	 * @return the resolved value set result
	 */
	public ResolvedValueSetResult<EntitySynopsis> resolveDefinition(
			ValueSetDefinitionReadId definitionId,
			Set<NameOrURI> codeSystemVersions,
			NameOrURI tag,
			ResolvedValueSetResolutionEntityQuery query,
			SortCriteria sortCriteria,
			ResolvedReadContext readContext, 
			Page page);
	
	/**
	 * Resolve definition as entity directory.
	 *
	 * @param definitionId the definition id
	 * @param codeSystemVersions the code system versions
	 * @param tag the tag
	 * @param query the query
	 * @param filterComponent the filter component
	 * @param restrictions the restrictions
	 * @param readContext the read context
	 * @param page the page
	 * @return the directory result
	 */
	public ResolvedValueSetResult<EntityDirectoryEntry> resolveDefinitionAsEntityDirectory(
			ValueSetDefinitionReadId definitionId,
			Set<NameOrURI> codeSystemVersions,
			NameOrURI tag,
			ResolvedValueSetResolutionEntityQuery query,
			SortCriteria sortCriteria,
			ResolvedReadContext readContext, 
			Page page);
	
	/**
	 * Resolve definition as complete set.
	 *
	 * @param definitionId the definition id
	 * @param codeSystemVersions the code system versions
	 * @param tag the tag
	 * @param readContext the read context
	 * @return the resolved value set
	 */
	public ResolvedValueSet resolveDefinitionAsCompleteSet(
			ValueSetDefinitionReadId definitionId,
			Set<NameOrURI> codeSystemVersions,
			NameOrURI tag,
			ResolvedReadContext readContext);
	
}
