package edu.mayo.cts2.framework.webapp.rest.controller;

import static org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockServletContext

import edu.mayo.cts2.framework.core.config.ServerContext
import edu.mayo.cts2.framework.core.config.ServiceConfigManager
import edu.mayo.cts2.framework.model.codesystem.CodeSystemCatalogEntry
import edu.mayo.cts2.framework.model.codesystem.CodeSystemCatalogEntryDirectory
import edu.mayo.cts2.framework.model.directory.DirectoryResult
import edu.mayo.cts2.framework.service.command.Page


class AbstractMessageWrappingControllerTest {

	def serverContext = [
		getServerRootWithAppName: { "http://test/webapp" }
	] as ServerContext

	def serviceConfigManager = [ 
		getServerContext: { serverContext } 
	] as ServiceConfigManager

	def controller = new AbstractMessageWrappingController(){}

	MockHttpServletRequest httpServletRequest
	
	@Before
	void init(){
		controller.serviceConfigManager = serviceConfigManager
		httpServletRequest = new MockHttpServletRequest("GET", "test/codesystems")
	}

	@Test
	void testGetHeadingAccessDate(){
			
		def restResource = controller.getHeadingForNameRequest( httpServletRequest )

		assertNotNull restResource.accessDate	
	}
	
	@Test
	void "Test for parameter count"(){
		httpServletRequest.addParameter("test1", "val1")
		httpServletRequest.addParameter("test2", "val2")

		def restResource = controller.getHeadingForNameRequest( httpServletRequest )

		assertEquals 2, restResource.parameter.size()
	}
	
	@Test
	void testParameterValues(){

		def restResource = controller.getHeadingForNameRequest( httpServletRequest )
		
		restResource.parameter.each { 
			
			if(it.arg == "arg1"){
				assertEquals it.val, "value1"
			} else if(it.arg == "arg2"){
				assertEquals it.val, "value2"
			} else {
				fail()
			}
		}
	}
	
	@Test
	void "Test 'numEntries' on 'populateDirectory' Directory"(){

		def dirResult = new DirectoryResult(["one","two"], true, true)
		
		
		def restResource = controller.
			populateDirectory( 
				dirResult, 
				new Page(), 
				httpServletRequest, 
				CodeSystemCatalogEntryDirectory.class )

		assertEquals 2, restResource.numEntries
	}
	
	@Test
	void "Test getUrlTemplateVariables() with one"(){
		def params = controller.getUrlTemplateVariables("/this/{is}")
			
		assertEquals 1, params.size()
		assertTrue params.contains("is")
	}
	
	@Test
	void "Test getUrlTemplateVariables() with one and slash"(){
		def params = controller.getUrlTemplateVariables("/this/{is}/")
			
		assertEquals 1, params.size()
		assertTrue params.contains("is")
	}
	
	@Test
	void "Test getUrlTemplateVariables() at beginning and end"(){
		def params = controller.getUrlTemplateVariables("{this}/{is}/")
			
		assertEquals 2, params.size()
		assertTrue params.contains("this")
		assertTrue params.contains("is")
	}
	
	@Test
	void "Test getUrlTemplateVariables() with multiple"(){
		def params = controller.getUrlTemplateVariables("/this/{is}/a/{test}/of/this/{method}")
			
		assertEquals 3, params.size()
		assertTrue params.contains("is")
		assertTrue params.contains("test")
		assertTrue params.contains("method")
	}
	
	@Test
	void "bindResourceToUrlTemplate"() {
		def binder = [
			getValueForPathAttribute : {varName,resource -> varName + "CHANGED"}
		] as UrlTemplateBinder
		
		def urlPath = controller.bindResourceToUrlTemplate(binder, new CodeSystemCatalogEntry(), "/this/{is}/a/{test}/of/this/{method}")
	
		assertEquals "/this/isCHANGED/a/testCHANGED/of/this/methodCHANGED", urlPath
	}
}
