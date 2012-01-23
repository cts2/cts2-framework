package edu.mayo.cts2.framework.webapp.rest.controller;

import static org.junit.Assert.*

import org.junit.Test

import edu.mayo.cts2.framework.model.codesystem.CodeSystemCatalogEntry

class UrlTemplateBindingCreatorTest {

	UrlTemplateBindingCreator binder = new UrlTemplateBindingCreator()
	
	
	@Test
	void "Test getUrlTemplateVariables() at beginning and end"(){
		def url = binder.bindResourceToUrlTemplate("{this}/{is}/", "first","second")
			
		assertEquals "first/second/", url
		
	}
	
	@Test
	void "Test getUrlTemplateVariables() with multiple"(){
		def url = binder.bindResourceToUrlTemplate("/this/{is}/a/{test}/of/this/{method}", "first","second","third")
		
	}
	
	@Test
	void "bindResourceToUrlTemplate"() {
		def urlBinder = [
			getPathValues : {["is":"isCHANGED","test":"testCHANGED","method":"methodCHANGED"]}
		] as UrlTemplateBinder
		
		def urlPath = binder.bindResourceToUrlTemplate(urlBinder, new CodeSystemCatalogEntry(), "/this/{is}/a/{test}/of/this/{method}")
	
		assertEquals "/this/isCHANGED/a/testCHANGED/of/this/methodCHANGED", urlPath
	}
}
