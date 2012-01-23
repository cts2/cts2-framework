package edu.mayo.cts2.framework.webapp.rest.converter;

import static org.junit.Assert.*

import org.junit.Test

import edu.mayo.cts2.framework.model.codesystem.CodeSystemCatalogEntry
import edu.mayo.cts2.framework.model.core.ScopedEntityName
import edu.mayo.cts2.framework.model.core.types.EntryState
import edu.mayo.cts2.framework.model.core.types.FinalizableState
import edu.mayo.cts2.framework.model.entity.EntityDescription
import edu.mayo.cts2.framework.model.entity.NamedEntityDescription
import edu.mayo.cts2.framework.model.service.core.UpdateChangeSetMetadataRequest
import edu.mayo.cts2.framework.model.service.core.UpdatedState

class MappingGsonHttpMessageConverterTest {
	
	def converter = new MappingGsonHttpMessageConverter()
	
	def gson = converter.gson;
	
	@Test
	void TestGetJson(){
		
		def cs = new CodeSystemCatalogEntry(about:"urn:oid:about", codeSystemName:"csname")
		
		def json = gson.toJson(cs)
		
		assertNotNull json
	}
	
	@Test
	void TestJsonRoundTrip(){
		
		def cs = new CodeSystemCatalogEntry(about:"urn:oid:about", codeSystemName:"csname")
		
		def json = gson.toJson(cs)
		
		println json
		
		def returned = gson.fromJson(json, CodeSystemCatalogEntry.class)
		
		assertEquals returned.codeSystemName, cs.codeSystemName
	}
	
	@Test
	void TestUpdateChangeSetMetadata(){
		
		def csm = new UpdateChangeSetMetadataRequest(updatedState:new UpdatedState(state:FinalizableState.FINAL))
		
		def json = gson.toJson(csm)
		
		println json
		
		def returned = gson.fromJson(json, UpdateChangeSetMetadataRequest.class)
		
		assertEquals returned.updatedState.state, csm.updatedState.state
	}
	
	@Test
	void TestNamedEntityDescriptionRoundTrip(){
		
		def ed = new NamedEntityDescription(entityID:new ScopedEntityName(name:"n",namespace:"ns"))
		
		def json = gson.toJson(ed)
		
		assertNotNull json
		
		def returned = gson.fromJson(json, NamedEntityDescription.class)
		
		assertEquals returned.entityID.name, "n"
		
		assertEquals returned.entityID.namespace, "ns"
	}
	
	@Test
	void TestEntityDescriptionRoundTrip(){
		
		def ed = new NamedEntityDescription(entityID:new ScopedEntityName(name:"n",namespace:"ns"))
		
		def des = new EntityDescription()
		des.setNamedEntity(ed)
		
		def json = gson.toJson(des)
		
		assertNotNull json
		
		println json
		
		def returned = gson.fromJson(json, EntityDescription.class)
		
		assertEquals returned.getNamedEntity().entityID.name, "n"
		
		assertEquals returned.getNamedEntity().entityID.namespace, "ns"
	}
	
	@Test
	void TestSetChoiceValue(){
		
		def ed = new NamedEntityDescription(entityID:new ScopedEntityName(name:"n",namespace:"ns"))
		
		def des = new EntityDescription()
		des.setNamedEntity(ed)
		
		converter.setChoiceValue(des);
		
		assertNotNull des.namedEntity;
		assertNotNull des.choiceValue;

	}
}
