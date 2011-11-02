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
 * The Interface URIHelperInterface.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface URIHelperInterface extends PathKeywords, PathVariables,
		PathParameters, HeaderParameters {

	/* CodeSystem */

	public static final String PATH_CODESYSTEMS = "/" + CODESYSTEMS;
	public static final String PATH_CODESYSTEM = "/" + CODESYSTEM;
	public static final String PATH_CODESYSTEMBYID = "/" + CODESYSTEM + "/{"
			+ VAR_CODESYSTEMID + "}";
	
	public static final String PATH_CODESYSTEMBYURI = "/" + CODESYSTEM_BY_URI;
	
	public static final String PATH_CODESYSTEMVERSION_OF_CODESYSTEM_BYTAG = 
		"/" + CODESYSTEM + "/{" + VAR_CODESYSTEMID + "}/" 
		+ TAG + "/{"+ VAR_TAG + "}";
	
	public static final String PATH_CODESYSTEMVERSION_OF_CODESYSTEM_BYID = 
			"/" + CODESYSTEM + "/{" + VAR_CODESYSTEMID + "}/" 
			+ VERSION + "/{"+ VAR_CODESYSTEMVERSIONID + "}";
	
	public static final String PATH_CODESYSTEMVERSIONBYURI = "/" + CODESYSTEMVERSION_BY_URI;
	
	public static final String PATH_CODESYSTEMVERSIONS = "/" + CODESYSTEMVERSIONS;
	
	public static final String PATH_CODESYSTEMVERSION = "/" + CODESYSTEMVERSION;

	public static final String PATH_CODESYSTEMVERSIONS_OF_CODESYSTEM = 
		"/" + CODESYSTEM + "/{" + VAR_CODESYSTEMID + "}/" + VERSIONS;

	/* Entities */

	public static final String PATH_ENTITIES = "/" + ENTITIES;
	public static final String PATH_ENTITY = "/" + ENTITY;
	public static final String PATH_ENTITIES_OF_CODESYSTEM_VERSION = "/" + CODESYSTEM + "/{"
			+ VAR_CODESYSTEMID + "}/" + VERSION 
			+ "/{" + VAR_CODESYSTEMVERSIONID + "}/" + ENTITIES;
	
	public static final String PATH_ENTITYBYID = "/" + CODESYSTEM + "/{"
			+ VAR_CODESYSTEMID + "}/" + VERSION
			+ "/{" + VAR_CODESYSTEMVERSIONID + "}/"
			+ ENTITY + "/{" + VAR_ENTITYID + "}";
	
	public static final String PATH_ENTITYBYURI = PATH_CODESYSTEMVERSION_OF_CODESYSTEM_BYID + "/" + ENTITY_BY_URI;
			
	public static final String PATH_ENTITYIDS = "/" + CODESYSTEM + "/{"
			+ VAR_CODESYSTEMID + "}/" + ENTITYIDS;
	public static final String PATH_ENTITYPREFERREDDISPLAY = "/" + CODESYSTEM
			+ "/{" + VAR_CODESYSTEMID + "}/" + ENTITY + "/{" + VAR_ENTITYID
			+ "}/" + PREFERREDDISPLAY;
	public static final String PATH_VALIDATEENTITYINCODESYSTEM = "/"
			+ CODESYSTEM + "/{" + VAR_CODESYSTEMID + "}/" + ENTITIES + "/"
			+ EXISTINGENTITYIDS;
	public static final String PATH_VALIDATEENTITYPROPERTIES = "/" + CODESYSTEM
			+ "/{" + VAR_CODESYSTEMID + "}/" + ENTITY + "/{" + VAR_ENTITYID
			+ "}/" + PROPERTIES + "/" + EXISTINGPROPERTYIDS;
	public static final String PATH_VALIDATEENTITYEFFECTIVE = "/" + CODESYSTEM
			+ "/{" + VAR_CODESYSTEMID + "}/" + ENTITIES + "/" + ENTITYEFFECTIVE;
	
	/* Association */
	public static final String PATH_ASSOCIATIONS = "/" + ASSOCIATIONS;
	public static final String PATH_ASSOCIATION = "/" + ASSOCIATION;
	public static final String PATH_ASSOCIATIONBYID = 
		PATH_CODESYSTEMVERSION_OF_CODESYSTEM_BYID + "/" + ASSOCIATION + "/{" + VAR_ASSOCIATIONID + "}";
	public static final String PATH_ASSOCIATION_OF_CODESYSTEMVERSION_BY_URI = 
			PATH_CODESYSTEMVERSION_OF_CODESYSTEM_BYID + "/" + ASSOCIATION_BY_URI;
	public static final String PATH_ASSOCIATIONBYURI = "/" + ASSOCIATION_BY_URI;
	public static final String PATH_ASSOCIATIONS_OF_CODESYSTEMVERSION = 
		PATH_CODESYSTEMVERSION_OF_CODESYSTEM_BYID + "/" + ASSOCIATIONS;
	public static final String PATH_GRAPH_OF_CODESYSTEMVERSION = 
		PATH_CODESYSTEMVERSION_OF_CODESYSTEM_BYID + "/" + GRAPH;
	public static final String PATH_CHILDREN_ASSOCIATIONS_OF_ENTITY = 
		PATH_ENTITYBYID + "/" + CHILDREN;
	public static final String PATH_SOURCEOF_ASSOCIATIONS_OF_ENTITY = 
		PATH_ENTITYBYID + "/" + SOURCEOF;
	
	/* Statement */
	public static final String PATH_STATEMENTS = "/" + STATEMENTS;
	public static final String PATH_STATEMENT = "/" + STATEMENT;
	public static final String PATH_STATEMENTBYID = "/" + STATEMENT + "/{"
			+ VAR_STATEMENTID + "}";
	
	public static final String PATH_STATEMENTBYURI = "/" + STATEMENTS + "/" + URI
			+ "/{" + VAR_URI + "}";

	
	/* Maps */

	public static final String PATH_MAP_BYID = "/" + MAP + "/{" + VAR_MAPID + "}";
	public static final String PATH_MAPVERSION_OF_MAP_BYID = PATH_MAP_BYID + "/" + VERSION + "/{" + VAR_MAPVERSIONID + "}";
	public static final String PATH_MAPS = "/" + MAPS;
	public static final String PATH_MAP = "/" + MAP;
	public static final String PATH_MAPVERSIONS = "/" + MAPVERSIONS;
	public static final String PATH_MAPVERSION = "/" + MAPVERSION;
	public static final String PATH_MAPVERSIONS_OF_MAP = PATH_MAP_BYID + "/" + VERSIONS;
	public static final String PATH_MAPVERSION_BYURI = "/" + MAPVERSION_BY_URI;
	public static final String PATH_MAP_BYURI = "/" + MAP_BY_URI;
	public static final String PATH_MAPENTRY =  "/" + MAPENTRY ;
	public static final String PATH_MAPENTRY_OF_MAPVERSION_BYID = PATH_MAPVERSION_OF_MAP_BYID + "/" + ENTRY + "/{" + VAR_MAPENTRYID + "}";
	public static final String PATH_MAPENTRIES = PATH_MAPVERSION_OF_MAP_BYID + "/" + ENTRIES;
	
	/* ConceptDomain */
	public static final String PATH_CONCEPTDOMAINS = "/" + CONCEPTDOMAINS;
	public static final String PATH_CONCEPTDOMAIN = "/" + CONCEPTDOMAIN;
	public static final String PATH_CONCEPTDOMAIN_BYID = "/" + CONCEPTDOMAIN + "/{" + VAR_CONCEPTDOMAINID + "}";
	public static final String PATH_CONCEPTDOMAIN_BYURI = "/" + CONCEPTDOMAIN_BY_URI;
	public static final String PATH_CONCEPTDOMAINBINDING_OF_CONCEPTDOMAIN_BYID = PATH_CONCEPTDOMAIN_BYID + "/" + BINDING + "/{" + VAR_CONCEPTDOMAINBINDINGID + "}"; 
	public static final String PATH_CONCEPTDOMAINBINDING = "/" + CONCEPTDOMAINBINDING;
	public static final String PATH_CONCEPTDOMAINBINDINGS = "/" + CONCEPTDOMAINBINDINGS;
	public static final String PATH_CONCEPTDOMAINBINDINGS_OF_CONCEPTDOMAIN = PATH_CONCEPTDOMAIN_BYID + "/" + VERSIONS;
	public static final String PATH_CONCEPTDOMAINBINDING_BYURI = PATH_CONCEPTDOMAINS + "/" + URI + "/{" + VAR_URI + "}";



	/* Valueset */
	
	public static final String PATH_VALUESET = "/" + VALUESET;

	public static final String PATH_VALUESETBYID = "/" + VALUESET + "/{"
			+ VAR_VALUESETID + "}";
	
	public static final String PATH_VALUESET_BYURI = "/" + VALUESET_BY_URI;

	public static final String PATH_ENTITIESINVALUESET = "/" + VALUESET + "/{"
			+ VAR_VALUESETID + "}/" + ENTITIES;

	public static final String PATH_VALUESETS = "/" + VALUESETS;
	
	public static final String PATH_VALUESETDEFINITION = "/" + VALUESETDEFINITION_LONG;
	public static final String PATH_VALUESETDEFINITIONS = "/" + VALUESETDEFINITIONS_LONG;
	
	public static final String PATH_VALUESETDEFINITION_OF_VALUESET_BYID = PATH_VALUESETBYID + "/"
			+ VALUESETDEFINITION_SHORT + "/{" + VAR_VALUESETDEFINITIONID + "}";
	
	public static final String PATH_VALUESETDEFINITIONS_OF_VALUESET = PATH_VALUESETBYID
			+ "/" + VALUESETDEFINITIONS_SHORT;

	public static final String PATH_VALIDATEENTITYINVALUESET = "/" + VALUESET
			+ "/{" + VAR_VALUESETID + "}/" + ENTITIES + "/" + EXISTINGENTITYIDS;

	/* Services */
	public static final String PATH_CODESYSTEMQUERYSERVICE = 
		SERVICE + "/" + CODESYSTEMQUERYSERVICE;
	public static final String PATH_CODESYSTEMREADSERVICE = 
		SERVICE + "/" + CODESYSTEMREADSERVICE;
	public static final String PATH_CODESYSTEMVERSIONQUERYSERVICE =
		SERVICE + "/" + CODESYSTEMVERSIONQUERYSERVICE;
	public static final String PATH_CODESYSTEMVERSIONREADSERVICE =
		SERVICE + "/" + CODESYSTEMVERSIONREADSERVICE;
}

interface PathKeywords {
	public static final String VERSIONS = "versions";
	public static final String VERSION = "version";
	
	public static final String CODESYSTEM = "codesystem";
	public static final String CODESYSTEM_BY_URI = "codesystembyuri";
	public static final String CODESYSTEMS = "codesystems";

	public static final String CODESYSTEMVERSIONS = "codesystemversions";
	public static final String CODESYSTEMVERSION = "codesystemversion";
	public static final String CODESYSTEMVERSION_BY_URI = "codesystemversionbyuri";


	public static final String ENTITIES = "entities";
	public static final String ENTITY = "entity";
	public static final String ENTITY_BY_URI = "entitybyuri";
	public static final String ENTITYIDS = "entityids";

	public static final String STATEMENTS = "statements";
	public static final String STATEMENT = "statement";

	public static final String MAPS = "maps";
	public static final String MAP = "map";
	public static final String MAP_BY_URI = "mapbyuri";
	public static final String MAPVRESION = "mapversion";
	public static final String MAPVERSION_BY_URI = "mapversionbyuri";
	public static final String MAPVERSIONS = "mapversions";
	public static final String MAPVERSION = "mapversion";
	public static final String ENTRIES = "entries";
	public static final String ENTRY = "entry";
	public static final String MAPENTRY = "mapentry";

	public static final String CONCEPTDOMAINS = "conceptdomains";
	public static final String CONCEPTDOMAIN = "conceptdomain";
	public static final String CONCEPTDOMAIN_BY_URI = "conceptdomainbyuri";
	public static final String CONCEPTDOMAINBINDING = "conceptdomainbinding";
	public static final String CONCEPTDOMAINBINDINGS = "conceptdomainbindings";
	public static final String BINDINGS = "bindings";
	public static final String BINDING = "binding";
	
	public static final String PREFERREDDISPLAY = "preferreddisplay";
	public static final String EXISTINGENTITYIDS = "existingentityids";

	public static final String PROPERTIES = "properties";
	public static final String EXISTINGPROPERTYIDS = "existingpropertyids";
	public static final String ENTITYEFFECTIVE = "entityeffective";

	public static final String DELIMITEDFILE = "delimitedfile";

	public static final String VALUESET = "valueset";
	public static final String VALUESET_BY_URI = "valuesetbyuri";
	public static final String VALUESETS = "valuesets";
	public static final String VALUESETDEFINITION_SHORT = "definition";
	public static final String VALUESETDEFINITION_LONG = "valuesetdefinition";
	public static final String VALUESETDEFINITIONS_SHORT = "definitions";
	public static final String VALUESETDEFINITIONS_LONG = "valuesetdefinitions";


	public static final String SOURCEENTITY = "sourceentity";
	public static final String TARGETENTITIES = "targetentities";

	public static final String TARGETENTITY = "targetentity";
	public static final String SOURCEENTITIES = "sourceentities";
	
	public static final String CODESYSTEMQUERYSERVICE = "codesystemqueryservice";
	public static final String CODESYSTEMREADSERVICE = "codesystemreadservice";
	public static final String CODESYSTEMVERSIONQUERYSERVICE = "codesystemversionqueryservice";
	public static final String CODESYSTEMVERSIONREADSERVICE = "codesystemversionreadservice";


	public static final String ASSOCIATIONS = "associations";
	public static final String ASSOCIATION = "association";
	public static final String ASSOCIATION_BY_URI = "associationbyuri";
	public static final String GRAPH = "graph";
	public static final String SOURCEOF = "sourceof";
	public static final String CHILDREN = "children";
	
	public static final String SERVICE = "service";
	public static final String URI = "uri";
	public static final String TAG = "tag";
}

interface PathVariables {
	public static final String VAR_ASSOCIATIONID = "associationID";
	public static final String VAR_CODESYSTEMID = "codeSystemID";
	public static final String VAR_CODESYSTEMVERSIONID = "codeSystemVersionID";
	public static final String VAR_ENTITYID = "entityID";
	public static final String VAR_STATEMENTID = "statementID";
	public static final String VAR_VALUESETID = "valueSetID";
	public static final String VAR_MAPID = "mapID";
	public static final String VAR_MAPVERSIONID = "mapVersionID";
	public static final String VAR_MAPENTRYID = "mapEntryID";
	public static final String VAR_VALUESETDEFINITIONID = "valueSetDefinitionID";
	public static final String VAR_SOURCEENTITYID = "sourceEntityID";
	public static final String VAR_TARGETENTITYID = "targetEntityID";
	public static final String VAR_CONCEPTDOMAINID = "conceptDomainID";
	public static final String VAR_CONCEPTDOMAINBINDINGID = "conceptDomainBindingID";
	public static final String VAR_URI = "uri";
	public static final String VAR_TAG = "tag";
}

interface PathParameters {
	public static final String PARAM_CODESYSTEMVERSION = "codesystemversion";
	public static final String PARAM_CODESYSTEMTAG = "tag";
	public static final String PARAM_CODESYSTEM = "codesystem";

	public static final String PARAM_ENTITYID = "entityid";
	public static final String PARAM_ENTITYNAMESPACE = "entitynamespace";
	public static final String PARAM_ENTITYNAME = "entityname";

	public static final String PARAM_STATEMENTID = "statementid";
	public static final String PARAM_STATEMENTNAME = "statementname";
	public static final String PARAM_STATEMENTVALUE = "statementvalue";

	public static final String PARAM_VALUESETID = "valuesetid";

	public static final String PARAM_ADDSTARTDATE = "addstartdate";
	public static final String PARAM_ADDENDDATE = "addenddate";

	public static final String PARAM_ALTERSTARTDATE = "alterstartdate";
	public static final String PARAM_ALTERENDDATE = "alterenddate";

	public static final String PARAM_EFFECTIVESTARTDATE = "effectivestartdate";
	public static final String PARAM_EFFECTIVEENDDATE = "effectiveenddate";

	public static final String PARAM_EFFECTIVEDATE = "effectivedate";

	public static final String PARAM_EXPORTFORMAT = "exportformat";
	public static final String PARAM_FIELDDELIMITER = "fielddelimiter";
	public static final String PARAM_EOLDELIMITER = "eoldelimiter";
	public static final String PARAM_FIELDENCLOSURE = "fieldenclosure";

	public static final String PARAM_EXPORTFORMAT_OWLRDF = "exportformat=owl-rdf";

	public static final String PARAM_SOURCEENTITYID = "sourceentity";
	public static final String PARAM_TARGETENTITYID = "targetentity";	
	public static final String PARAM_SOURCEORTARGETENTITYID = "sourceortargetentity";
	public static final String PARAM_PREDICATEID = "predicate";

	public static final String PARAM_MATCHINGALGORITHM = "matchingalgorithm";
	public static final String PARAM_MATCHTEXT = "matchtext";
	
	public static final String PARAM_PAGE = "page";
	public static final String PARAM_MAXTORETURN = "maxtoreturn";
	
	public static final String PARAM_FILTERCOMPONENT = "filtercomponent";

}

interface HeaderParameters {
	public static final String HEADER_COUNT = "count";
}
