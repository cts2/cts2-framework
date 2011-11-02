package edu.mayo.cts2.framework.webapp.rest.controller;

import static org.junit.Assert.*

import org.junit.Test

import edu.mayo.cts2.framework.model.codesystem.CodeSystemCatalogEntry

class UrlTemplateBindingCreatorTest {

	UrlTemplateBindingCreator binder = new UrlTemplateBindingCreator()
	
	@Test
	void "Test getUrlTemplateVariables() with one"(){
		def params = binder.getUrlTemplateVariables("/this/{is}")
			
		assertEquals 1, params.size()
		assertTrue params.contains("is")
	}
	
	@Test
	void "Test getUrlTemplateVariables() with one and slash"(){
		def params = binder.getUrlTemplateVariables("/this/{is}/")
			
		assertEquals 1, params.size()
		assertTrue params.contains("is")
	}
	
	@Test
	void "Test getUrlTemplateVariables() at beginning and end"(){
		def params = binder.getUrlTemplateVariables("{this}/{is}/")
			
		assertEquals 2, params.size()
		assertTrue params.contains("this")
		assertTrue params.contains("is")
	}
	
	@Test
	void "Test getUrlTemplateVariables() with multiple"(){
		def params = binder.getUrlTemplateVariables("/this/{is}/a/{test}/of/this/{method}")
			
		assertEquals 3, params.size()
		assertTrue params.contains("is")
		assertTrue params.contains("test")
		assertTrue params.contains("method")
	}
	
	@Test
	void "bindResourceToUrlTemplate"() {
		def urlBinder = [
			getValueForPathAttribute : {varName,resource -> varName + "CHANGED"}
		] as UrlTemplateBinder
		
		def urlPath = binder.bindResourceToUrlTemplate(urlBinder, new CodeSystemCatalogEntry(), "/this/{is}/a/{test}/of/this/{method}")
	
		assertEquals "/this/isCHANGED/a/testCHANGED/of/this/methodCHANGED", urlPath
	}
}
