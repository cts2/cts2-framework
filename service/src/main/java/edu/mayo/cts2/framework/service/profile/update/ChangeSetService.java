/*
 * Copyright: (c) 2004-2012 Mayo Foundation for Medical Education and 
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
package edu.mayo.cts2.framework.service.profile.update;

import java.net.URI;
import java.util.Date;

import edu.mayo.cts2.framework.model.core.OpaqueData;
import edu.mayo.cts2.framework.model.core.SourceReference;
import edu.mayo.cts2.framework.model.service.core.types.FunctionalProfile;
import edu.mayo.cts2.framework.model.updates.ChangeSet;
import edu.mayo.cts2.framework.service.profile.Cts2Profile;
import edu.mayo.cts2.framework.service.profile.FunctionalConformance;

/**
 * The Interface ChangeSetService.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@FunctionalConformance(FunctionalProfile.FP_UPDATE)
public interface ChangeSetService extends Cts2Profile {
	
	/**
	 * Read change set.
	 *
	 * @param changeSetUri the change set uri
	 * @return the change set
	 */
	public ChangeSet readChangeSet(String changeSetUri);
	
	/**
	 * Creates the change set.
	 *
	 * @return the change set
	 */
	public ChangeSet createChangeSet();
	
	/**
	 * Update change set metadata.
	 *
	 * @param changeSetUri the change set uri
	 * @param creator the creator
	 * @param changeInstructions the change instructions
	 * @param officialEffectiveDate the official effective date
	 */
	public void updateChangeSetMetadata(
			String changeSetUri, 
			SourceReference creator, 
			OpaqueData changeInstructions,
			Date officialEffectiveDate);
	
	/**
	 * Rollback change set.
	 *
	 * @param changeSetUri the change set uri
	 */
	public void rollbackChangeSet(String changeSetUri);
	
	/**
	 * Commit change set.
	 *
	 * @param changeSetUri the change set uri
	 */
	public void commitChangeSet(String changeSetUri);

	/**
	 * Import change set.
	 *
	 * @param changeSetUri the change set uri
	 * @return the string
	 */
	public String importChangeSet(URI changeSetUri);

}
