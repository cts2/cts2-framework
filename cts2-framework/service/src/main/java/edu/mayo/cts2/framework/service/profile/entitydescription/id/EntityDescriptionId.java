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
package edu.mayo.cts2.framework.service.profile.entitydescription.id;

import edu.mayo.cts2.framework.model.core.ScopedEntityName;

/**
 * The Class EntityDescriptionId.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class EntityDescriptionId {

	private String codeSystemVersion;
	
	private ScopedEntityName name;

	public String getCodeSystemVersion() {
		return codeSystemVersion;
	}

	public void setCodeSystemVersion(String codeSystemVersion) {
		this.codeSystemVersion = codeSystemVersion;
	}

	public ScopedEntityName getName() {
		return name;
	}

	public void setName(ScopedEntityName name) {
		this.name = name;
	}
	
	/**
	 * Builds the entity description id.
	 *
	 * @param codeSystem the code system
	 * @param codeSystemVersion the code system version
	 * @param name the name
	 * @return the entity description id
	 */
	public static EntityDescriptionId buildEntityDescriptionId(String codeSystemVersion, ScopedEntityName name){
		EntityDescriptionId id = new EntityDescriptionId();
	
		id.setCodeSystemVersion(codeSystemVersion);
		id.setName(name);
		
		return id;
	}
}
