package edu.mayo.cts2.framework.core.json

import edu.mayo.cts2.framework.core.json.JsonUnmarshallingException;

import static org.junit.Assert.*

import javax.xml.transform.stream.StreamSource

import org.junit.Test
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.Resource

import edu.mayo.cts2.framework.core.xml.DelegatingMarshaller
import edu.mayo.cts2.framework.model.codesystem.CodeSystemCatalogEntry
import edu.mayo.cts2.framework.model.core.EntryDescription
import edu.mayo.cts2.framework.model.core.ScopedEntityName
import edu.mayo.cts2.framework.model.core.TsAnyType
import edu.mayo.cts2.framework.model.entity.Designation
import edu.mayo.cts2.framework.model.entity.EntityDescription
import edu.mayo.cts2.framework.model.entity.NamedEntityDescription

class JsonConverterTest {
	
	@Test
	void TestGetJsonClass(){
		def converter = new JsonConverter()
		
		def json = """
		{"codeSystemCatalogEntry":{"codeSystemName":"csname","designedForOntologyTaskList":[],"usedOntologyEngineeringToolList":[],"releaseFormatList":[],"about":"urn:oid:about","keywordList":[],"resourceTypeList":[],"additionalDocumentationList":[],"sourceAndRoleList":[],"noteList":[],"propertyList":[],"alternateIDList":[],"entryState":"ACTIVE"}}
		"""	
		
		assertEquals CodeSystemCatalogEntry, converter.getJsonClass(json)
	}
	
	@Test
	void TestFromObjectWithExtra(){
		def converter = new JsonConverter()
		
		def json = """
		{"codeSystemCatalogEntry":{"somethingElse" : "aDifferentThing", "codeSystemName":"csname","about":"urn:oid:about","entryState":"ACTIVE"}}
		"""
		
		def cs = converter.fromJson(json);
		assertEquals CodeSystemCatalogEntry, cs.class
		assertEquals "urn:oid:about", cs.about
		assertEquals "csname", cs.codeSystemName
		assertEquals "ACTIVE", cs.entryState.toString()
	}

    @Test(expected = JsonUnmarshallingException)
    void TestFromInvalidObject(){
        def converter = new JsonConverter()

        def json = """
		{"somethingElse" : "aDifferentThing", "codeSystemName":"csname","about":"urn:oid:about","entryState":"ACTIVE"}
		"""

        converter.fromJson(json);
    }

    @Test
	void TestJsonToObject(){
		def converter = new JsonConverter()
		
		def json = """
		{"codeSystemCatalogEntry":{"codeSystemName":"csname","designedForOntologyTaskList":[],"usedOntologyEngineeringToolList":[],"releaseFormatList":[],"about":"urn:oid:about","keywordList":[],"resourceTypeList":[],"additionalDocumentationList":[],"sourceAndRoleList":[],"noteList":[],"propertyList":[],"alternateIDList":[],"entryState":"ACTIVE"}}
		"""
		
		assertEquals "csname", converter.fromJson(json, CodeSystemCatalogEntry).codeSystemName
	}
	
	@Test
	void TestObjectToJsonSpecialChars(){
		def converter = new JsonConverter()
		def cs = new CodeSystemCatalogEntry(resourceSynopsis: new EntryDescription(value: new TsAnyType()));
		cs.resourceSynopsis.value.content = '&lt;a href="http://obo.cvs.sourceforge.net/*checkout*/song/ontology/sofa.obo"&gt;SOFA&lt;/a&gt; '
		
		def json = converter.toJson(cs);
		
		def cs_return = converter.fromJson(json)
		
		assertEquals cs, cs_return
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
		cs.addKeyword("test1");
		cs.addKeyword("test2");

		assertTrue converter.toJson(cs).contains("\"keywordList\":[\"test1\",\"test2\"]");
	}

    @Test
    void TestGetJsonNoSynopsisValue(){
        def converter = new JsonConverter()

        def cs = new CodeSystemCatalogEntry()
        cs.setResourceSynopsis(new EntryDescription())

        converter.toJson(cs)
    }

	@Test
	void TestGetJsonNullSynopsisValue(){
		def converter = new JsonConverter()
		
		def cs = new CodeSystemCatalogEntry()
		cs.setResourceSynopsis(new EntryDescription())
		cs.getResourceSynopsis().setValue(new TsAnyType())
		cs.getResourceSynopsis().getValue().setContent(null)
		
		converter.toJson(cs)
	}
	
	
	@Test
	void TestGetJsonBlankSynopsisValue(){
		def converter = new JsonConverter()
		
		def cs = new CodeSystemCatalogEntry()
		cs.setResourceSynopsis(new EntryDescription())
		cs.getResourceSynopsis().setValue(new TsAnyType())
		cs.getResourceSynopsis().getValue().setContent("    ")
		
		converter.toJson(cs)
	}
	
	@Test
	void TestGetJsonFromComplexObject(){
		def converter = new JsonConverter()
		
		DelegatingMarshaller marshaller = new DelegatingMarshaller()
		
		Resource xml = new ClassPathResource("xml/breakingJson.xml")
		
		def stream = xml.getInputStream()
		
		def cs = marshaller.unmarshal(new StreamSource(stream))
		
		println converter.toJson(cs)
	}
	
	@Test
	void TestGetJsonFromXMLBreaking(){
		def converter = new JsonConverter()
		
		DelegatingMarshaller marshaller = new DelegatingMarshaller()
		
		Resource xml = new ClassPathResource("xml/breaksJsonCodeSystems.xml")
		
		def stream = xml.getInputStream()
		
		def cs = marshaller.unmarshal(new StreamSource(stream))
		
		converter.toJson(cs)
	}
	
	@Test
	void TestGetJsonFromDesignation(){
		def converter = new JsonConverter()
		
		def d = new Designation()
		d.value = new TsAnyType(content: "test")

		assertTrue ! converter.toJson(d).contains("anyObject")
		assertTrue converter.toJson(d).contains("\"value\":\"test\"")
	}
	
	@Test
	void TestGetJsonFromDesignationRoundTrip(){
		def converter = new JsonConverter()
		
		def d = new Designation()
		d.value = new TsAnyType(content: "test")
		
		def json = converter.toJson(d)
		
		def retD = converter.fromJson(json)
		
		assertEquals d, retD
	}

}
