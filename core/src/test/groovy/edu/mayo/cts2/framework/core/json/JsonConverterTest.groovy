package edu.mayo.cts2.framework.core.json;

import static org.junit.Assert.*;

import org.junit.Test

import edu.mayo.cts2.framework.model.codesystem.CodeSystemCatalogEntry
import edu.mayo.cts2.framework.model.core.ScopedEntityName
import edu.mayo.cts2.framework.model.entity.EntityDescription
import edu.mayo.cts2.framework.model.entity.NamedEntityDescription

class JsonConverterTest {
	
	@Test
	void TestGetJsonClass(){
		def converter = new JsonConverter()
		
		def json = """
		{"CodeSystemCatalogEntry":{"codeSystemName":"csname","designedForOntologyTaskList":[],"usedOntologyEngineeringToolList":[],"releaseFormatList":[],"about":"urn:oid:about","keywordList":[],"resourceTypeList":[],"additionalDocumentationList":[],"sourceAndRoleList":[],"noteList":[],"propertyList":[],"alternateIDList":[],"entryState":"ACTIVE"}}
		"""	
		
		assertEquals "CodeSystemCatalogEntry", converter.getJsonClass(json).getSimpleName()
	}
	
	@Test
	void TestJsonToObject(){
		def converter = new JsonConverter()
		
		def json = """
		{"CodeSystemCatalogEntry":{"codeSystemName":"csname","designedForOntologyTaskList":[],"usedOntologyEngineeringToolList":[],"releaseFormatList":[],"about":"urn:oid:about","keywordList":[],"resourceTypeList":[],"additionalDocumentationList":[],"sourceAndRoleList":[],"noteList":[],"propertyList":[],"alternateIDList":[],"entryState":"ACTIVE"}}
		"""
		
		assertEquals "csname", converter.fromJson(json, CodeSystemCatalogEntry).codeSystemName
	}
	
	
	@Test
	void TestSetChoiceValue(){
		def converter = new JsonConverter()
		
		def ed = new NamedEntityDescription(entityID:new ScopedEntityName(name:"n",namespace:"ns"))
		
		def des = new EntityDescription()
		des.setNamedEntity(ed)
		
		converter.setChoiceValue(des);
		
		assertNotNull des.namedEntity;
		assertNotNull des.choiceValue;

	}
	
	@Test
	void TestGetJsonFromObject(){
		def converter = new JsonConverter()
		
		def cs = new CodeSystemCatalogEntry()
		
		print converter.toJson(cs)
	}

}
