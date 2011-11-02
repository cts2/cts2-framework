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
package edu.mayo.cts2.framework.model.command;

import edu.mayo.cts2.framework.model.core.MatchAlgorithmReference;
import edu.mayo.cts2.framework.model.core.ModelAttributeReference;
import edu.mayo.cts2.framework.model.core.URIAndEntityName;
import edu.mayo.cts2.framework.model.core.types.TargetReferenceType;

/**
 * The Class ResolvedFilter.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class ResolvedFilter {
	
	private MatchAlgorithmReference matchAlgorithmReference;
	private URIAndEntityName propertyReference;
	private ModelAttributeReference modelAttributeReference;
	private String matchValue;
	private TargetReferenceType referenceType = TargetReferenceType.ATTRIBUTE;
	public MatchAlgorithmReference getMatchAlgorithmReference() {
		return matchAlgorithmReference;
	}
	
	public void setMatchAlgorithmReference(
			MatchAlgorithmReference matchAlgorithmReference) {
		this.matchAlgorithmReference = matchAlgorithmReference;
	}

	public URIAndEntityName getPropertyReference() {
		return propertyReference;
	}

	public void setPropertyReference(URIAndEntityName propertyReference) {
		this.propertyReference = propertyReference;
	}

	public ModelAttributeReference getModelAttributeReference() {
		return modelAttributeReference;
	}

	public void setModelAttributeReference(
			ModelAttributeReference modelAttributeReference) {
		this.modelAttributeReference = modelAttributeReference;
	}

	public String getMatchValue() {
		return matchValue;
	}

	public void setMatchValue(String matchValue) {
		this.matchValue = matchValue;
	}

	public TargetReferenceType getReferenceType() {
		return referenceType;
	}

	public void setReferenceType(TargetReferenceType referenceType) {
		this.referenceType = referenceType;
	}
}
