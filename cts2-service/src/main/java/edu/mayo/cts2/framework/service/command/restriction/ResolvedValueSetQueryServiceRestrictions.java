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
package edu.mayo.cts2.framework.service.command.restriction;

import java.util.HashSet;
import java.util.Set;

import edu.mayo.cts2.framework.model.service.core.EntityNameOrURI;
import edu.mayo.cts2.framework.model.service.core.NameOrURI;


/**
 * The Class ResolvedValueSetQueryServiceRestrictions.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class ResolvedValueSetQueryServiceRestrictions {
	
	private Set<NameOrURI> valueSets = new HashSet<NameOrURI>();

	private Set<NameOrURI> valueSetDefinitions = new HashSet<NameOrURI>();

	private Set<NameOrURI> codeSystems = new HashSet<NameOrURI>();

	private Set<NameOrURI> codeSystemVersions = new HashSet<NameOrURI>();
	
	private Set<EntityNameOrURI> entities = new HashSet<EntityNameOrURI>();

	public Set<NameOrURI> getValueSets() {
		return valueSets;
	}

	public void setValueSets(Set<NameOrURI> valueSets) {
		this.valueSets = valueSets;
	}

	public Set<NameOrURI> getValueSetDefinitions() {
		return valueSetDefinitions;
	}

	public void setValueSetDefinitions(Set<NameOrURI> valueSetDefinitions) {
		this.valueSetDefinitions = valueSetDefinitions;
	}

	public Set<NameOrURI> getCodeSystems() {
		return codeSystems;
	}

	public void setCodeSystems(Set<NameOrURI> codeSystems) {
		this.codeSystems = codeSystems;
	}

	public Set<NameOrURI> getCodeSystemVersions() {
		return codeSystemVersions;
	}

	public void setCodeSystemVersions(Set<NameOrURI> codeSystemVersions) {
		this.codeSystemVersions = codeSystemVersions;
	}

	public Set<EntityNameOrURI> getEntities() {
		return entities;
	}

	public void setEntities(Set<EntityNameOrURI> entities) {
		this.entities = entities;
	}
}
