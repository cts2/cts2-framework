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
package edu.mayo.cts2.framework.core.constants;

/**
 * The Interface ModelAndViewInterface.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface ModelAndViewInterface {

	public static final String VIEW_HOMEPAGE = "homepage";
	public static final String VIEW_CODESYSTEM = "codeSystem";
	public static final String VIEW_CODESYSTEMDIRECTORY = "codeSystemDirectory";
	public static final String VIEW_CODESYSTEMVERSIONDIRECTORY = "codeSystemVersionDirectory";
	public static final String VIEW_CODESYSTEMVERSION = "codeSystemVersion";
	public static final String VIEW_CODESYSTEMVERSIONLIST = "codeSystemVersionList";
	
	public static final String VIEW_ENTITYDIRECTORY = "entityDirectory";
	public static final String VIEW_ENTITYDESCRIPTION = "entityDescription";
	public static final String VIEW_ENTITYLIST = "entityList";

	public static final String VIEW_ASSOCIATION = "association";
	public static final String VIEW_ASSOCIATIONDIRECTORY = "associationDirectory";
	
	public static final String VIEW_MAP = "map";
	public static final String VIEW_MAPVERSION = "mapVersion";
	public static final String VIEW_MAPDIRECTORY = "mapDirectory";
	public static final String VIEW_MAPVERSIONDIRECTORY = "mapVersionDirectory";
	public static final String VIEW_MAPENTRYDIRECTORY = "mapEntryDirectory";
	
	public static final String VIEW_STATEMENT = "statement";
	public static final String VIEW_STATEMENTDIRECTORY = "statementDirectory";
	
	public static final String VIEW_VALUESET = "valueSet";
	public static final String VIEW_VALUESETDIRECTORY = "valueSetDirectory";
	public static final String VIEW_VALUESETDEFINITION = "valueSetDefinition";
	public static final String VIEW_VALUESETDEFINITIONDIRECTORY = "valueSetDefinitionDirectory";
	
	public static final String VIEW_CONCEPTDOMAIN = "conceptDomain";
	public static final String VIEW_CONCEPTDOMAINDIRECTORY = "conceptDomainDirectory";
	public static final String VIEW_CONCEPTDOMAINBINDING = "conceptDomainBinding";
	public static final String VIEW_CONCEPTDOMAINBINDINGDIRECTORY = "conceptDomainBindingDirectory";
	
	public static final String VIEW_ERROR = "error";
	public static final String VIEW_SERVICE = "service";
	
	public static final String CTS2_MODEL_OBJECT = "cts2ModelObject";
	
	public static final String MODEL_CODESYSTEMDIRECTORY = CTS2_MODEL_OBJECT;
	
	public static final String MODEL_CODESYSTEMMSG = CTS2_MODEL_OBJECT;
	public static final String MODEL_CODESYSTEMVERSIONDIRECTORY = CTS2_MODEL_OBJECT;
	public static final String MODEL_CODESYSTEMVERSIONMSG = CTS2_MODEL_OBJECT;
	public static final String MODEL_CODESYSTEMVERSIONLIST = CTS2_MODEL_OBJECT;
	public static final String MODEL_VALIDCODESYSTEMIDS = CTS2_MODEL_OBJECT;
	public static final String MODEL_NUMBEROFCODESINCODESYSTEM = CTS2_MODEL_OBJECT;
	public static final String MODEL_PREFERREDDISPLAY = CTS2_MODEL_OBJECT;

	public static final String MODEL_ENTITYDIRECTORY = CTS2_MODEL_OBJECT;
	public static final String MODEL_ENTITYDESCRIPTIONMSG = CTS2_MODEL_OBJECT;
	public static final String MODEL_ENTITYLIST = CTS2_MODEL_OBJECT;
	public static final String MODEL_ENTITYIDS = CTS2_MODEL_OBJECT;
	public static final String MODEL_VALIDENTITYINCODESYSTEM = CTS2_MODEL_OBJECT;
	public static final String MODEL_VALIDPROPERTYINENTITY = CTS2_MODEL_OBJECT;
	public static final String MODEL_ENTITYEFFECTIVE = CTS2_MODEL_OBJECT;
	
	public static final String MODEL_ASSOCIATIONDIRECTORY = CTS2_MODEL_OBJECT;

	public static final String MODEL_VALIDMAPPINGSINCODESYSTEM = CTS2_MODEL_OBJECT;
	public static final String MODEL_INVERSE_MAPPING_EXISTS = CTS2_MODEL_OBJECT;
	public static final String MODEL_GET_INVERSE_MAPPING = CTS2_MODEL_OBJECT;
	public static final String MODEL_ENTITY_MAPPING_TARGETS_FOR_SOURCE = CTS2_MODEL_OBJECT;
	public static final String MODEL_ENTITY_MAPPING_SOURCES_FOR_TARGET = CTS2_MODEL_OBJECT;
	
	public static final String MODEL_VALUESETMSG = CTS2_MODEL_OBJECT;
	public static final String MODEL_VALUESETDIRECTORY = CTS2_MODEL_OBJECT;
	public static final String MODEL_VALUESETDEFINITIONMSG = CTS2_MODEL_OBJECT;
	public static final String MODEL_VALUESETDEFINITIONDIRECTORY = CTS2_MODEL_OBJECT;
	
	public static final String MODEL_MAPMSG = CTS2_MODEL_OBJECT;
	public static final String MODEL_MAPDIRECTORY = CTS2_MODEL_OBJECT;
	public static final String MODEL_MAPVERSIONMSG = CTS2_MODEL_OBJECT;
	public static final String MODEL_MAPVERSIONDIRECTORY = CTS2_MODEL_OBJECT;
	public static final String MODEL_MAPENTRYMSG = CTS2_MODEL_OBJECT;
	public static final String MODEL_MAPENTRYDIRECTORY = CTS2_MODEL_OBJECT;
	
	public static final String MODEL_CONCEPTDOMAINMSG = CTS2_MODEL_OBJECT;
	public static final String MODEL_CONCEPTDOMAINDIRECTORY = CTS2_MODEL_OBJECT;
	public static final String MODEL_CONCEPTDOMAINBINDINGMSG = CTS2_MODEL_OBJECT;
	public static final String MODEL_CONCEPTDOMAINBINDINGDIRECTORY = CTS2_MODEL_OBJECT;
}


