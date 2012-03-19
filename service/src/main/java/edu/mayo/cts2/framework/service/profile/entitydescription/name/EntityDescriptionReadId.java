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
package edu.mayo.cts2.framework.service.profile.entitydescription.name;

import edu.mayo.cts2.framework.model.core.ScopedEntityName;
import edu.mayo.cts2.framework.model.service.core.EntityNameOrURI;
import edu.mayo.cts2.framework.model.service.core.NameOrURI;
import edu.mayo.cts2.framework.model.util.ModelUtils;

/**
 * The Class EntityDescriptionId.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class EntityDescriptionReadId extends EntityNameOrURI {

	private static final long serialVersionUID = 6314775768311853179L;
	
	private NameOrURI codeSystemVersion;
	
	public EntityDescriptionReadId(ScopedEntityName scopedEntityName, NameOrURI codeSystemVersion) {
		super();
		this.setEntityName(scopedEntityName);
		this.codeSystemVersion = codeSystemVersion;
	}
	
	public EntityDescriptionReadId(String uri, NameOrURI codeSystemVersion) {
		super();
		this.setUri(uri);
		this.codeSystemVersion = codeSystemVersion;
	}

	public EntityDescriptionReadId(EntityNameOrURI entity, NameOrURI codeSystemVersion) {
		super();
		this.setEntityName(entity.getEntityName());
		this.setUri(entity.getUri());
		this.codeSystemVersion = codeSystemVersion;
	}
	
	public EntityDescriptionReadId(String entityName, String entityNamespace, NameOrURI codeSystemVersion) {
		super();
		this.setEntityName(ModelUtils.createScopedEntityName(entityName, entityNamespace));
		this.codeSystemVersion = codeSystemVersion;
	}

	public NameOrURI getCodeSystemVersion() {
		return codeSystemVersion;
	}

}
