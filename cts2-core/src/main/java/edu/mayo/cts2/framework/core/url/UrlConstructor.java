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
package edu.mayo.cts2.framework.core.url;

import edu.mayo.cts2.framework.core.config.ServerContext;
import edu.mayo.cts2.framework.core.constants.URIHelperInterface;
import edu.mayo.cts2.framework.core.util.EncodingUtils;
import edu.mayo.cts2.framework.model.core.ScopedEntityName;
import edu.mayo.cts2.framework.model.service.core.types.FunctionalProfile;
import edu.mayo.cts2.framework.model.service.core.types.StructuralProfile;

/**
 * A helper class for producing CTS2 compliant URLs.
 * 
 * Service plugins should use this class instead of constructing resource
 * 'hrefs' by hand, if possible.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class UrlConstructor {

	private ServerContext serverContext;
	
	/**
	 * Instantiates a new url constructor.
	 */
	public UrlConstructor(){
		super();
	}
	
	/**
	 * Instantiates a new url constructor.
	 *
	 * @param serverContext the server context
	 */
	public UrlConstructor(ServerContext serverContext){
		this.serverContext = serverContext;
	}

	/**
	 * Creates the code system url.
	 *
	 * @param codeSystemName the code system name
	 * @return the string
	 */
	public String createCodeSystemUrl(String codeSystemName) {

		return addServerContext(
			URIHelperInterface.CODESYSTEM + "/" + codeSystemName);
	}
	
	/**
	 * Creates the code system version url.
	 *
	 * @param codeSystemName the code system name
	 * @param codeSystemVersionName the code system version name
	 * @return the string
	 */
	public String createCodeSystemVersionUrl(String codeSystemName, String codeSystemVersionName) {

		return this.createCodeSystemUrl(codeSystemName) + "/" +
					URIHelperInterface.VERSION + "/" + codeSystemVersionName;
	}
	
	/**
	 * Creates the entity url.
	 *
	 * @param codeSystemName the code system name
	 * @param codeSystemVersionName the code system version name
	 * @param entityName the entity name
	 * @return the string
	 */
	public String createEntityUrl(String codeSystemName, String codeSystemVersionName, String entityName){
		return this.createCodeSystemVersionUrl(codeSystemName, codeSystemVersionName) + "/" +
			URIHelperInterface.ENTITY + "/" + EncodingUtils.encodeEntityName(entityName);
	}
	
	public String createEntityUrl(String codeSystemName, String codeSystemVersionName, ScopedEntityName entityName){
		return this.createCodeSystemVersionUrl(codeSystemName, codeSystemVersionName) + "/" +
			URIHelperInterface.ENTITY + "/" + EncodingUtils.encodeScopedEntityName(entityName);
	}
	
	public String createEntityUrl(ScopedEntityName entityName){
		return this.addServerContext(
				URIHelperInterface.ENTITY  + "/" + EncodingUtils.encodeScopedEntityName(entityName));
	}

	/**
	 * This should never really be used... this should always be a scoped entity name.
	 */
	@Deprecated
	public String createEntityUrl(String entityName){
		return this.addServerContext(
				URIHelperInterface.ENTITY + "/" + EncodingUtils.encodeEntityName(entityName));
	}

	/**
	 * Creates the source url.
	 *
	 * @param codeSystemName the code system name
	 * @param codeSystemVersionName the code system version name
	 * @param entityName the entity name
	 * @return the string
	 * 
	 * @deprecated use 'createSubjectOfUrl'
	 */
	@Deprecated
	public String createSourceUrl(String codeSystemName, String codeSystemVersionName, String entityName){
		return this.createSubjectOfUrl(codeSystemName, codeSystemVersionName, entityName);
	}
	
	/**
	 * Creates the sourceof url.
	 *
	 * @param codeSystemName the code system name
	 * @param codeSystemVersionName the code system version name
	 * @param entityName the entity name
	 * @return the string
	 */
	public String createSubjectOfUrl(String codeSystemName, String codeSystemVersionName, String entityName){
		return this.createEntityUrl(codeSystemName, codeSystemVersionName, entityName) + "/" + URIHelperInterface.SUBJECTOF;
	}
	
	/**
	 * Creates the targetof url.
	 *
	 * @param codeSystemName the code system name
	 * @param codeSystemVersionName the code system version name
	 * @param entityName the entity name
	 * @return the string
	 */
	public String createTargetOfUrl(String codeSystemName, String codeSystemVersionName, String entityName){
		return this.createEntityUrl(codeSystemName, codeSystemVersionName, entityName) + "/" + URIHelperInterface.TARGETOF;
	}
	
	/**
	 * Creates the children url.
	 *
	 * @param codeSystemName the code system name
	 * @param codeSystemVersionName the code system version name
	 * @param entityName the entity name
	 * @return the string
	 */
	public String createChildrenUrl(String codeSystemName, String codeSystemVersionName, String entityName){
		return this.createEntityUrl(codeSystemName, codeSystemVersionName, entityName) + "/" + URIHelperInterface.CHILDREN;
	}

	/**
	 * Creates the entities of code system version url.
	 *
	 * @param codeSystemName the code system name
	 * @param codeSystemVersionName the code system version name
	 * @return the string
	 */
	public String createEntitiesOfCodeSystemVersionUrl(String codeSystemName, String codeSystemVersionName){
		return 
				createCodeSystemVersionUrl(codeSystemName, codeSystemVersionName)
					+ "/" + URIHelperInterface.ENTITIES ;
	}
	
	/**
	 * Creates the associations of code system version url.
	 *
	 * @param codeSystemName the code system name
	 * @param codeSystemVersionName the code system version name
	 * @return the string
	 */
	public String createAssociationsOfCodeSystemVersionUrl(String codeSystemName, String codeSystemVersionName){
		return
			this.createCodeSystemVersionUrl(
					codeSystemName, 
					codeSystemVersionName) + "/" + URIHelperInterface.ASSOCIATIONS;
	}
	
	public String createAssociationOfCodeSystemVersionUrl(
			String codeSystemName, 
			String codeSystemVersionName,
			String associationLocalName){
		return this.createCodeSystemVersionUrl(codeSystemName, codeSystemVersionName) + "/" +
				URIHelperInterface.ASSOCIATION + "/" + associationLocalName;
	}
	
	
	/**
	 * Creates the versions of code system url.
	 *
	 * @param codeSystemName the code system name
	 * @return the string
	 */
	public String createVersionsOfCodeSystemUrl(String codeSystemName){
		return this.createCodeSystemUrl(codeSystemName) + "/" + URIHelperInterface.VERSIONS;
	}
	
	/**
	 * Creates the value set url.
	 *
	 * @param valueSetName the value set name
	 * @return the string
	 */
	public String createValueSetUrl(String valueSetName) {

		return addServerContext(
			URIHelperInterface.VALUESET + "/" + valueSetName);
	}
	
	/**
	 * Creates the definitions of value set url.
	 *
	 * @param valueSetName the value set name
	 * @return the string
	 */
	public String createDefinitionsOfValueSetUrl(String valueSetName) {
		return this.createValueSetUrl(valueSetName) + "/" + URIHelperInterface.VALUESETDEFINITIONS_SHORT;
	}
	
	public String createResolvedValueSetUrl(String valueSetName, String definitionLocalId, String resolutionLocalId){
		return this.createValueSetDefinitionUrl(valueSetName, definitionLocalId) + "/" + 
				URIHelperInterface.VALUE_SET_RESOLUTION_SHORT + "/" + resolutionLocalId;
	}
	
	/**
	 * Creates the value set definition url.
	 *
	 * @param valueSetName the value set name
	 * @param definitionLocalId the value set definition document uri
	 * @return the string
	 */
	public String createValueSetDefinitionUrl(String valueSetName, String definitionLocalId) {
		return this.createValueSetUrl(valueSetName) + "/" +
					URIHelperInterface.VALUESETDEFINITION_SHORT + "/" + definitionLocalId;
	}
	
	/**
	 * Creates the code systems url.
	 *
	 * @return the string
	 */
	public String createCodeSystemsUrl() {
		return addServerContext(
			URIHelperInterface.CODESYSTEMS);
	}
	
	
	public String createServiceUrl(StructuralProfile structural, FunctionalProfile functional) {
		String serviceName = structural.name().replaceFirst("^SP", "").replaceAll("_", "").toLowerCase();
		String serviceType = functional.name().replaceFirst("^FP", "").replaceAll("_", "").toLowerCase();
		
		return addServerContext(
			URIHelperInterface.SERVICE + "/" + serviceName + serviceType);
	}
	
	/**
	 * Adds the server context.
	 *
	 * @param url the url
	 * @return the string
	 */
	private String addServerContext(String url){
		return this.serverContext.getServerRootWithAppName() + "/" + url;
	}
	
	/**
	 * Gets the server root with app name.
	 *
	 * @return the server root with app name
	 */
	public String getServerRootWithAppName(){
		return this.serverContext.getServerRootWithAppName();
	}
	
	public String getAppName(){
		return this.serverContext.getAppName();
	}

	/**
	 * Creates the map url.
	 *
	 * @param mapName the map name
	 * @return the string
	 */
	public String createMapUrl(String mapName) {
		return addServerContext(
				URIHelperInterface.MAP + "/" + mapName);
	}
	
	/**
	 * Creates the map version url.
	 *
	 * @param mapName the map name
	 * @param mapVersionName the map version name
	 * @return the string
	 */
	public String createMapVersionUrl(String mapName, String mapVersionName) {
		return this.createMapUrl(mapName) + "/" + URIHelperInterface.VERSION + "/" + mapVersionName;
	}

	/**
	 * Creates the map entry url.
	 *
	 * @param mapName the map name
	 * @param mapVersionName the map version name
	 * @param mapEntryName the map entry name
	 * @return the string
	 */
	public String createMapEntryUrl(String mapName, String mapVersionName,
			String mapEntryName) {
		return this.createMapVersionUrl(mapName, mapVersionName) + "/" + URIHelperInterface.ENTRY + "/" + mapEntryName;
	}
}
