package edu.mayo.cts2.framework.webapp.rest.controller;

import static org.junit.Assert.*

import org.junit.Test

import edu.mayo.cts2.framework.webapp.rest.util.ControllerUtils;

class ControllerUtilsTest {
	
	@Test
	void IdsToEntityNameOrUriSetOneName(){
		def result = ControllerUtils.idsToEntityNameOrUriSet(["ns:name"])
			
		assertEquals 1,result.size()
		assertEquals "ns", result.iterator().next().getEntityName().getNamespace();
		assertEquals "name", result.iterator().next().getEntityName().getName();

	}
	
	@Test
	void IdsToEntityNameOrUriSetOneNameNoNamespace(){
		def result = ControllerUtils.idsToEntityNameOrUriSet(["name"])
			
		assertEquals 1,result.size()
		assertNull result.iterator().next().getEntityName().getNamespace();
		assertEquals "name", result.iterator().next().getEntityName().getName();

	}
	
	
	@Test
	void IdsToEntityNameOrUriSetOneUri(){
		def result = ControllerUtils.idsToEntityNameOrUriSet(["urn:oid:134321"])
			
		assertEquals 1,result.size()
		assertEquals "urn:oid:134321", result.iterator().next().getUri()

	}
	
	@Test void idsToEntityNameOrUriSetNull(){
		def result = ControllerUtils.idsToEntityNameOrUriSet(null)
			
		assertEquals 0,result.size()
	
	}
	
	@Test void idsToNameOrUriSetNull(){
		def result = ControllerUtils.idsToNameOrUriSet(null)
			
		assertEquals 0,result.size()
	
	}

}
