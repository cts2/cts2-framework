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
package edu.mayo.cts2.framework.model.service.core;

/**
 * The Class FilterComponent.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class FilterComponent {
	
	private NameOrURI matchAlgorithm;
	private String matchValue;
	private NameOrURIList filterComponents;
	
	/**
	 * Gets the match algorithm.
	 *
	 * @return the match algorithm
	 */
	public NameOrURI getMatchAlgorithm() {
		return matchAlgorithm;
	}
	
	/**
	 * Sets the match algorithm.
	 *
	 * @param matchAlgorithm the new match algorithm
	 */
	public void setMatchAlgorithm(NameOrURI matchAlgorithm) {
		this.matchAlgorithm = matchAlgorithm;
	}
	
	/**
	 * Gets the match value.
	 *
	 * @return the match value
	 */
	public String getMatchValue() {
		return matchValue;
	}
	
	/**
	 * Sets the match value.
	 *
	 * @param matchValue the new match value
	 */
	public void setMatchValue(String matchValue) {
		this.matchValue = matchValue;
	}
	
	/**
	 * Gets the filter components.
	 *
	 * @return the filter components
	 */
	public NameOrURIList getFilterComponents() {
		return filterComponents;
	}
	
	/**
	 * Sets the filter components.
	 *
	 * @param filterComponents the new filter components
	 */
	public void setFilterComponents(NameOrURIList filterComponents) {
		this.filterComponents = filterComponents;
	}
}
