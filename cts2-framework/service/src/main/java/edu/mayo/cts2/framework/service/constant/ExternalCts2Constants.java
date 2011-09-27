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
package edu.mayo.cts2.framework.service.constant;

/**
 * The Class ExternalCts2Constants.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class ExternalCts2Constants {

	public static final String CONCAT_STRING = ":";

	public static final String SLASH = "/";

	public static final String CTS2_URI = "http://schema.omg.org/spec/CTS2/1.0";

	public static final String MODEL = "model";
	
	public static final String MATCH = "match";

	public static final String ATTRIBUTE = "attribute";
	
	private static final String MODEL_ATTRIBUTE_ROOT = CTS2_URI + SLASH + MODEL
		+ SLASH + ATTRIBUTE;
	
	private static final String MATCH_ALGORITHM_ROOT = CTS2_URI + SLASH + MATCH
		+ SLASH + ATTRIBUTE;
	
	/* Match Algorithms */	
	public static final String MATCH_CONTAINS_NAME = "contains";
	
	public static final String MATCH_CONTAINS_URI = MATCH_ALGORITHM_ROOT
		+ CONCAT_STRING + MATCH_CONTAINS_NAME;
	
	/**
	 * Builds the match algorithm uri.
	 *
	 * @param matchAlgorithmName the match algorithm name
	 * @return the string
	 */
	public static String buildMatchAlgorithmUri(String matchAlgorithmName){
		return MATCH_ALGORITHM_ROOT + CONCAT_STRING + matchAlgorithmName;
	}
	/* End Match Algorithms */

	/* Core */
	public static final String MA_ABOUT_NAME = "about";
	
	public static final String MA_ABOUT_URI = MODEL_ATTRIBUTE_ROOT
		+ CONCAT_STRING + MA_ABOUT_NAME;

	public static final String MA_RESOURCE_NAME_NAME = "resourceName";
	
	public static final String MA_RESOURCE_URI = MODEL_ATTRIBUTE_ROOT
		+ CONCAT_STRING + MA_RESOURCE_NAME_NAME;
	
	public static final String MA_KEYWORD_NAME = "keyword";
	
	public static final String MA_KEYWORD_URI = MODEL_ATTRIBUTE_ROOT
		+ CONCAT_STRING + MA_KEYWORD_NAME;
	
	public static final String MA_RESOURCE_SYNOPSIS_NAME = "resourceSynopsis";
	
	public static final String MA_RESOURCE_SYNOPSIS_URI = MODEL_ATTRIBUTE_ROOT
		+ CONCAT_STRING + MA_RESOURCE_SYNOPSIS_NAME;
	
	/* Model Attributes */
	public static final String MA_ENTITY_DESCRIPTION_DESIGNATION_NAME = "entityDescriptionDesignation";
	
	public static final String MA_ENTITY_DESCRIPTION_DESIGNATION_URI = MODEL_ATTRIBUTE_ROOT
		+ CONCAT_STRING + MA_ENTITY_DESCRIPTION_DESIGNATION_NAME;
	
	/**
	 * Builds the model attribute uri.
	 *
	 * @param modelAttributeName the model attribute name
	 * @return the string
	 */
	public static String buildModelAttributeUri(String modelAttributeName){
		return MODEL_ATTRIBUTE_ROOT + CONCAT_STRING + modelAttributeName;
	}
	/* End Model Attributes */
}
