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

import edu.mayo.cts2.framework.model.service.core.EntityNameOrURI;
import edu.mayo.cts2.framework.model.service.core.NameOrURI;

/**
 * The Class AssociationQueryServiceRestrictions.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class AssociationQueryServiceRestrictions {

	private NameOrURI codeSystemVersion;
	
	private EntityNameOrURI sourceEntity;
	
	private EntityNameOrURI targetEntity;
	
	private EntityNameOrURI sourceOrTargetEntity;
	
	private EntityNameOrURI targetLiteral;

	private EntityNameOrURI predicate;
	
	private EntityNameOrURI targetExpression;

	public NameOrURI getCodeSystemVersion() {
		return codeSystemVersion;
	}

	public void setCodeSystemVersion(NameOrURI codeSystemVersion) {
		this.codeSystemVersion = codeSystemVersion;
	}

	public EntityNameOrURI getSourceEntity() {
		return sourceEntity;
	}

	public void setSourceEntity(EntityNameOrURI sourceEntity) {
		this.sourceEntity = sourceEntity;
	}

	public EntityNameOrURI getTargetEntity() {
		return targetEntity;
	}

	public void setTargetEntity(EntityNameOrURI targetEntity) {
		this.targetEntity = targetEntity;
	}

	public EntityNameOrURI getSourceOrTargetEntity() {
		return sourceOrTargetEntity;
	}

	public void setSourceOrTargetEntity(EntityNameOrURI sourceOrTargetEntity) {
		this.sourceOrTargetEntity = sourceOrTargetEntity;
	}

	public EntityNameOrURI getTargetLiteral() {
		return targetLiteral;
	}

	public void setTargetLiteral(EntityNameOrURI targetLiteral) {
		this.targetLiteral = targetLiteral;
	}

	public EntityNameOrURI getPredicate() {
		return predicate;
	}

	public void setPredicate(EntityNameOrURI predicate) {
		this.predicate = predicate;
	}

	public EntityNameOrURI getTargetExpression() {
		return targetExpression;
	}

	public void setTargetExpression(EntityNameOrURI targetExpression) {
		this.targetExpression = targetExpression;
	}
}
