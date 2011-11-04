package edu.mayo.cts2.framework.webapp.rest.converter;

import org.junit.Test
import static org.junit.Assert.*

import edu.mayo.cts2.framework.model.codesystem.CodeSystemCatalogEntry

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


}
