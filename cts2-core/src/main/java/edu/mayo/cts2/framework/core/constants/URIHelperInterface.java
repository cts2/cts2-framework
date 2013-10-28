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
	
	public static final String PATH_CODESYSTEM_CHANGEHISTORY = PATH_CODESYSTEMBYID + "/" + CHANGEHISTORY;
	
	public static final String PATH_CODESYSTEM_EARLIESTCHANGE = PATH_CODESYSTEMBYID + "/" + EARLIESTCHANGE;
	
	public static final String PATH_CODESYSTEM_LATESTCHANGE = PATH_CODESYSTEMBYID + "/" + LATESTCHANGE;
	
	public static final String PATH_CODESYSTEMBYURI = "/" + CODESYSTEM_BY_URI + "/" + ALL_WILDCARD;

	public static final String PATH_CODESYSTEMVERSION_OF_CODESYSTEM_BYID = 
			"/" + CODESYSTEM + "/{" + VAR_CODESYSTEMID + "}/" 
			+ VERSION + "/{"+ VAR_CODESYSTEMVERSIONID + "}";
	
	public static final String PATH_CODESYSTEMVERSION_CHANGEHISTORY = PATH_CODESYSTEMVERSION_OF_CODESYSTEM_BYID + "/" + CHANGEHISTORY;
	
	public static final String PATH_CODESYSTEMVERSION_EARLIESTCHANGE = PATH_CODESYSTEMVERSION_OF_CODESYSTEM_BYID + "/" + EARLIESTCHANGE;
	
	public static final String PATH_CODESYSTEMVERSION_LATESTCHANGE = PATH_CODESYSTEMVERSION_OF_CODESYSTEM_BYID + "/" + LATESTCHANGE;
	
	public static final String PATH_CODESYSTEMVERSIONBYURI = "/" + CODESYSTEMVERSION_BY_URI + "/" + ALL_WILDCARD;
	
	public static final String PATH_CODESYSTEMVERSIONBYTAG = "/" + CODESYSTEM + "/{"
			+ VAR_CODESYSTEMID + "}/" + VERSION;
	
	public static final String PATH_CODESYSTEMVERSIONS = "/" + CODESYSTEMVERSIONS;
	
	public static final String PATH_CODESYSTEMVERSION = "/" + CODESYSTEMVERSION;
	
	public static final String PATH_CODESYSTEMVERSIONBYID = "/" + CODESYSTEMVERSION + "/{" + VAR_CODESYSTEMVERSIONID + "}";

	public static final String PATH_CODESYSTEMVERSIONS_OF_CODESYSTEM = 
		"/" + CODESYSTEM + "/{" + VAR_CODESYSTEMID + "}/" + VERSIONS;

	/* Entities */
	public static final String PATH_ENTITIES = "/" + ENTITIES;
	public static final String PATH_ENTITY = "/" + ENTITY;
	public static final String PATH_ENTITIES_OF_CODESYSTEM_VERSION = "/" + CODESYSTEM + "/{"
			+ VAR_CODESYSTEMID + "}/" + VERSION 
			+ "/{" + VAR_CODESYSTEMVERSIONID + "}/" + ENTITIES;
	
	public static final String PATH_ENTITY_OF_CODESYSTEM_VERSION_BYID = "/" + CODESYSTEM + "/{"
			+ VAR_CODESYSTEMID + "}/" + VERSION
			+ "/{" + VAR_CODESYSTEMVERSIONID + "}/"
			+ ENTITY + "/{" + VAR_ENTITYID + "}";
	
	public static final String PATH_ENTITY_OF_CODESYSTEM_VERSION_BYURI = "/" + CODESYSTEM + "/{"
			+ VAR_CODESYSTEMID + "}/" + VERSION
			+ "/{" + VAR_CODESYSTEMVERSIONID + "}/"
			+ ENTITY_BY_URI;
	
	public static final String PATH_ALL_DESCRIPTIONS_OF_ENTITYBYID = "/"
			+ ENTITY + "/{" + VAR_ENTITYID + "}";
	
	public static final String PATH_ENTITYBYURI = "/" + ENTITY_BY_URI + "/" + ALL_WILDCARD;
			
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
	public static final String PATH_ASSOCIATIONBYURI = "/" + ASSOCIATION_BY_URI + "/" + ALL_WILDCARD;
	public static final String PATH_ASSOCIATIONS_OF_CODESYSTEMVERSION = 
		PATH_CODESYSTEMVERSION_OF_CODESYSTEM_BYID + "/" + ASSOCIATIONS;
	public static final String PATH_GRAPH_OF_CODESYSTEMVERSION = 
		PATH_CODESYSTEMVERSION_OF_CODESYSTEM_BYID + "/" + GRAPH;
	
	public static final String PATH_CHILDREN_ASSOCIATIONS_OF_ENTITY = 
		PATH_ENTITY_OF_CODESYSTEM_VERSION_BYID + "/" + CHILDREN;
	public static final String PATH_ANCESTOR_ASSOCIATIONS_OF_ENTITY = 
		PATH_ENTITY_OF_CODESYSTEM_VERSION_BYID + "/" + ANCESTORS;
	public static final String PATH_DECENDANT_ASSOCIATIONS_OF_ENTITY = 
		PATH_ENTITY_OF_CODESYSTEM_VERSION_BYID + "/" + DESCENDANTS;
	
	public static final String PATH_SUBJECTOF_ASSOCIATIONS_OF_ENTITY = 
		PATH_ENTITY_OF_CODESYSTEM_VERSION_BYID + "/" + SUBJECTOF;
	public static final String PATH_TARGETOF_ASSOCIATIONS_OF_ENTITY = 
		PATH_ENTITY_OF_CODESYSTEM_VERSION_BYID + "/" + TARGETOF;
	public static final String PATH_PREDICATEOF_ASSOCIATIONS_OF_ENTITY = 
		PATH_ENTITY_OF_CODESYSTEM_VERSION_BYID + "/" + PREDICATEOF;
	
	/* Statement */
	public static final String PATH_STATEMENTS = "/" + STATEMENTS;
	public static final String PATH_STATEMENT = "/" + STATEMENT;
	public static final String PATH_STATEMENT_OF_CODESYSTEMVERSION_BYID = 
			PATH_CODESYSTEMVERSION_OF_CODESYSTEM_BYID + "/{" + VAR_STATEMENTID + "}";
	
	public static final String PATH_STATEMENTBYURI = "/" + STATEMENT_BY_URI + "/" + ALL_WILDCARD;
	
	/* Maps */
	public static final String PATH_MAP_BYID = "/" + MAP + "/{" + VAR_MAPID + "}";
	public static final String PATH_MAP_CHANGEHISTORY = PATH_MAP_BYID + "/" + CHANGEHISTORY;
	public static final String PATH_MAP_EARLIESTCHANGE = PATH_MAP_BYID + "/" + EARLIESTCHANGE;
	public static final String PATH_MAP_LATESTCHANGE = PATH_MAP_BYID + "/" + LATESTCHANGE;
	public static final String PATH_MAPVERSION_OF_MAP_BYID = PATH_MAP_BYID + "/" + VERSION + "/{" + VAR_MAPVERSIONID + "}";
	public static final String PATH_MAPVERSION_CHANGEHISTORY = PATH_MAPVERSION_OF_MAP_BYID + "/" + CHANGEHISTORY;
	public static final String PATH_MAPVERSION_EARLIESTCHANGE = PATH_MAPVERSION_OF_MAP_BYID + "/" + EARLIESTCHANGE;
	public static final String PATH_MAPVERSION_LATESTCHANGE = PATH_MAPVERSION_OF_MAP_BYID + "/" + LATESTCHANGE;
	public static final String PATH_MAPS = "/" + MAPS;
	public static final String PATH_MAP = "/" + MAP;
	public static final String PATH_MAPVERSIONS = "/" + MAPVERSIONS;
	public static final String PATH_MAPVERSION = "/" + MAPVERSION;
	public static final String PATH_MAPVERSIONS_OF_MAP = PATH_MAP_BYID + "/" + VERSIONS;
	public static final String PATH_MAPVERSION_BYURI = "/" + MAPVERSION_BY_URI + "/" + ALL_WILDCARD;
	public static final String PATH_MAP_BYURI = "/" + MAP_BY_URI + "/" + ALL_WILDCARD;
	public static final String PATH_MAPENTRY =  "/" + MAPENTRY ;
	public static final String PATH_MAPENTRY_OF_MAPVERSION_BYID = PATH_MAPVERSION_OF_MAP_BYID + "/" + ENTRY + "/{" + VAR_MAPENTRYID + "}";
	public static final String PATH_MAPENTRIES = PATH_MAPVERSION_OF_MAP_BYID + "/" + ENTRIES;
	
	/* ConceptDomain */
	public static final String PATH_CONCEPTDOMAINS = "/" + CONCEPTDOMAINS;
	public static final String PATH_CONCEPTDOMAIN = "/" + CONCEPTDOMAIN;
	public static final String PATH_CONCEPTDOMAIN_BYID = "/" + CONCEPTDOMAIN + "/{" + VAR_CONCEPTDOMAINID + "}";
	public static final String PATH_CONCEPTDOMAIN_CHANGEHISTORY = PATH_CONCEPTDOMAIN_BYID + "/" + CHANGEHISTORY;
	public static final String PATH_CONCEPTDOMAIN_EARLIESTCHANGE = PATH_CONCEPTDOMAIN_BYID + "/" + EARLIESTCHANGE;
	public static final String PATH_CONCEPTDOMAIN_LATESTCHANGE = PATH_CONCEPTDOMAIN_BYID + "/" + LATESTCHANGE;
	public static final String PATH_CONCEPTDOMAIN_BYURI = "/" + CONCEPTDOMAIN_BY_URI + "/" + ALL_WILDCARD;
	public static final String PATH_CONCEPTDOMAINBINDING_OF_CONCEPTDOMAIN_BYID = PATH_CONCEPTDOMAIN_BYID + "/" + BINDING + "/{" + VAR_CONCEPTDOMAINBINDINGID + "}"; 
	public static final String PATH_CONCEPTDOMAINBINDING = "/" + CONCEPTDOMAINBINDING;
	public static final String PATH_CONCEPTDOMAINBINDINGS = "/" + CONCEPTDOMAINBINDINGS;
	public static final String PATH_CONCEPTDOMAINBINDINGS_OF_CONCEPTDOMAIN = PATH_CONCEPTDOMAIN_BYID + "/" + VERSIONS;
	public static final String PATH_CONCEPTDOMAINBINDING_BYURI = "/" + CONCEPTDOMAINBINDING_BY_URI;



	/* Valueset */
	public static final String PATH_VALUESET = "/" + VALUESET;
	public static final String PATH_VALUESETBYID = "/" + VALUESET + "/{"
			+ VAR_VALUESETID + "}";	
	public static final String PATH_VALUESET_CHANGEHISTORY = PATH_VALUESETBYID + "/" + CHANGEHISTORY;
	public static final String PATH_VALUESET_EARLIESTCHANGE = PATH_VALUESETBYID + "/" + EARLIESTCHANGE;
	public static final String PATH_VALUESET_LATESTCHANGE = PATH_VALUESETBYID + "/" + LATESTCHANGE;
	public static final String PATH_VALUESET_BYURI = "/" + VALUESET_BY_URI + "/" + ALL_WILDCARD;

	public static final String PATH_ENTITIESINVALUESET = "/" + VALUESET + "/{"
			+ VAR_VALUESETID + "}/" + ENTITIES;

	public static final String PATH_VALUESETS = "/" + VALUESETS;
	
	public static final String PATH_VALUESETDEFINITION = "/" + VALUESETDEFINITION_LONG;
	
	public static final String PATH_VALUESETDEFINITIONS = "/" + VALUESETDEFINITIONS_LONG;
	
	public static final String PATH_VALUESETDEFINITION_OF_VALUESET = PATH_VALUESETBYID + "/"
			+ VALUESETDEFINITION_SHORT;
	
	public static final String PATH_VALUESETDEFINITION_OF_VALUESET_BYID = PATH_VALUESETBYID + "/"
			+ VALUESETDEFINITION_SHORT + "/{" + VAR_VALUESETDEFINITIONID + "}";
	
	public static final String PATH_RESOLUTION_OF_VALUESETDEFINITION =
			PATH_VALUESETDEFINITION_OF_VALUESET_BYID + "/" + VALUE_SET_RESOLUTION_SHORT;

    public static final String PATH_RESOLUTION_OF_VALUESETDEFINITION_ENTITIES =
            PATH_VALUESETDEFINITION_OF_VALUESET_BYID + "/" + ENTITIES;
	
	public static final String PATH_RESOLVED_VALUESET = "/" + RESOLVED_VALUE_SET_LONG;
	
	public static final String PATH_RESOLVED_VALUESETS = "/" + RESOLVED_VALUE_SETS_LONG;
	
	public static final String PATH_RESOLVED_VALUESETS_OF_VALUESETDEFINITION =
			PATH_VALUESETDEFINITION_OF_VALUESET_BYID + "/" + VALUE_SET_RESOLUTIONS_SHORT;
	
	public static final String PATH_RESOLVED_VALUESET_OF_VALUESETDEFINITION_BYID =
			PATH_VALUESETDEFINITION_OF_VALUESET_BYID + "/" + VALUE_SET_RESOLUTION_SHORT + "/{" + VAR_RESOLVEDVALUESETID + "}";

	public static final String PATH_RESOLVED_VALUESET_OF_VALUESETDEFINITION_BYID_ENTITIES =
			PATH_VALUESETDEFINITION_OF_VALUESET_BYID + "/" + VALUE_SET_RESOLUTION_SHORT + "/{" + VAR_RESOLVEDVALUESETID + "}/" + ENTITIES;
	
	public static final String PATH_VALUESETDEFINITION_BYURI = "/" + VALUESETDEFINITION_BY_URI + "/" + ALL_WILDCARD;
	
	public static final String PATH_VALUESETDEFINITIONS_OF_VALUESET = PATH_VALUESETBYID
			+ "/" + VALUESETDEFINITIONS_SHORT;

	public static final String PATH_VALIDATEENTITYINVALUESET = "/" + VALUESET
			+ "/{" + VAR_VALUESETID + "}/" + ENTITIES + "/" + EXISTINGENTITYIDS;

	/* Services */
	public static final String PATH_SERVICE = SERVICE + "/{" + VAR_SERVICEID + "}";
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
	public static final String STATEMENT_BY_URI = "statementbyuri";

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
	public static final String CONCEPTDOMAINBINDING_BY_URI = "conceptdomainbindingbyuri";
	public static final String BINDINGS = "bindings";
	public static final String BINDING = "binding";
	
	public static final String PREFERREDDISPLAY = "preferreddisplay";
	public static final String EXISTINGENTITYIDS = "existingentityids";

	public static final String PROPERTIES = "properties";
	public static final String EXISTINGPROPERTYIDS = "existingpropertyids";
	public static final String ENTITYEFFECTIVE = "entityeffective";

	public static final String DELIMITEDFILE = "delimitedfile";

	public static final String VALUESET = "valueset";
	public static final String COMPLETE = "complete";
	public static final String VALUESET_BY_URI = "valuesetbyuri";
	public static final String VALUESETS = "valuesets";
	public static final String VALUESETDEFINITION_SHORT = "definition";
	public static final String VALUESETDEFINITION_LONG = "valuesetdefinition";
	public static final String VALUESETDEFINITIONS_SHORT = "definitions";
	public static final String VALUESETDEFINITIONS_LONG = "valuesetdefinitions";
	public static final String VALUESETDEFINITION_BY_URI = "valuesetdefinitionbyuri";
	public static final String VALUE_SET_RESOLUTION_SHORT = "resolution";
	public static final String VALUE_SET_RESOLUTIONS_SHORT = "resolutions";
	public static final String RESOLVED_VALUE_SET_LONG = "resolvedvalueset";
	public static final String RESOLVED_VALUE_SETS_LONG = "resolvedvaluesets";

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
	public static final String SUBJECTOF = "subjectof";
	public static final String PREDICATEOF = "predicateof";
	public static final String TARGETOF = "targetof";
	public static final String CHILDREN = "children";
	public static final String ANCESTORS = "ancestors";
	public static final String DESCENDANTS = "descendants";
	
	public static final String SERVICE = "service";
	public static final String URI = "uri";
	
	public static final String CHANGEHISTORY = "changehistory";
	public static final String EARLIESTCHANGE = "earliestchange";
	public static final String LATESTCHANGE = "latestchange";
	
	public static final String ALL_WILDCARD = "**";
	
	public static final String CODE_SYSTEM_READ = "codesystemread";
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
	public static final String VAR_RESOLVEDVALUESETID = "resolvedValueSetID";
	public static final String VAR_SOURCEENTITYID = "sourceEntityID";
	public static final String VAR_TARGETENTITYID = "targetEntityID";
	public static final String VAR_CONCEPTDOMAINID = "conceptDomainID";
	public static final String VAR_CONCEPTDOMAINBINDINGID = "conceptDomainBindingID";
	public static final String VAR_RESOLUTIONTYPE = "resolutiontype";
	public static final String VAR_SERVICEID = "serviceID";

}

interface PathParameters {
	public static final String PARAM_CODESYSTEMVERSION = "codesystemversion";
	public static final String PARAM_CODESYSTEM = "codesystem";
	
	public static final String PARAM_CONCEPTDOMAIN = "conceptdomain";

	public static final String PARAM_ENTITY = "entity";
	public static final String PARAM_ENTITYNAMESPACE = "entitynamespace";
	public static final String PARAM_ENTITYNAME = "entityname";
	
	public static final String PARAM_VALUESET = "valueset";
	public static final String PARAM_DEFINITION = "definition";
	
	public static final String PARAM_ENTITIES_MAPROLE = "entitiesmaprole";
	public static final String PARAM_VALUESETS_MAPROLE = "valuesetsmaprole";
	public static final String PARAM_CODESYSTEMS_MAPROLE = "codesystemsmaprole";
	
	public static final String PARAM_ALL_OR_SOME = "allorsome";
	public static final String PARAM_ENTITIES_MAPSTATUS = "mapstatus";

	public static final String PARAM_STATEMENTID = "statementid";
	public static final String PARAM_STATEMENTNAME = "statementname";
	public static final String PARAM_STATEMENTVALUE = "statementvalue";

	public static final String PARAM_VALUESETID = "valuesetid";

	public static final String PARAM_FROMDATE = "fromdate";
	public static final String PARAM_TODATE = "todate";

	public static final String PARAM_SOURCEENTITYID = "sourceentity";
	public static final String PARAM_TARGETENTITYID = "targetentity";	
	public static final String PARAM_SOURCEORTARGETENTITYID = "sourceortargetentity";
	public static final String PARAM_PREDICATEID = "predicate";
    public static final String PARAM_TARGETLITERALID = "targetliteral";
    public static final String PARAM_TARGETEXPRESSIONID = "targetexpression";

	public static final String PARAM_MATCHALGORITHM = "matchalgorithm";
	public static final String PARAM_MATCHVALUE = "matchvalue";
	
	public static final String PARAM_PAGE = "page";
	public static final String PARAM_MAXTORETURN = "maxtoreturn";
	
	public static final String PARAM_FILTERCOMPONENT = "filtercomponent";
	
	public static final String PARAM_CHANGESETCONTEXT = "changesetcontext";
	
	public static final String PARAM_URI = "uri";
	public static final String PARAM_TAG = "tag";
	
	public static final String PARAM_LIST = "list";
    public static final String PARAM_COMPLETE = "complete";
    
    public static final String PARAM_COMMIT = "commit";

    public static final String PARAM_REDIRECT = "redirect";

	public static enum ValueSetDefinitionResolutionTypes {
		iterable,
		entitydirectory,
		complete
	}

	public static enum ResolvedValueSetResolutionTypes {
		iterable,
		complete
	}

	public static final String DEFAULT_VALUESETDEFINITION_RESOLUTION = "iterable";
	public static final String DEFAULT_TAG = "CURRENT";
	
	public static final String PARAM_SEPARATOR = ":";

}

interface HeaderParameters {
	public static final String HEADER_COUNT = "count";
}
