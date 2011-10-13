package edu.mayo.cts2.framework.webapp.rest.controller;

import static org.junit.Assert.*

import javax.servlet.http.HttpServletRequest

import org.junit.Test

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

	def ServiceConfigManager = [ 
		getServerContext: { serverContext } 
	] as ServiceConfigManager

	def controller = [
		getServiceConfigManager : { ServiceConfigManager }
	] as AbstractMessageWrappingController

	def httpServletRequest = [ 
		getServletPath: { '/codesystems' },
		getRequestURL:  { 'http://test/webapp/codesystems'<<'' },
		getParameterMap:  { ["arg1":"value1", "arg2":"value2"] } ] as HttpServletRequest 

	@Test
	void testGetHeadingAccessDate(){
			
		def restResource = controller.getHeading( httpServletRequest )

		assertNotNull restResource.accessDate	
	}
	
	@Test
	void testGetHeadingResourceRoot(){
			
		def restResource = controller.getHeading( httpServletRequest )

		assertEquals "codesystems", restResource.resourceRoot
	}
	
	@Test
	void "Test for parameter count"(){

		def restResource = controller.getHeading( httpServletRequest )

		assertEquals 2, restResource.parameter.size()
	}
	
	@Test
	void testParameterValues(){

		def restResource = controller.getHeading( httpServletRequest )
		
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
	void "bindResourceToUrlTemplate()"() {
		def binder = [
			getValueForPathAttribute : {varName,resource -> varName + "CHANGED"}
		] as UrlBinder
		
		def urlPath = controller.bindResourceToUrlTemplate(binder, new CodeSystemCatalogEntry(), "/this/{is}/a/{test}/of/this/{method}")
	
		assertEquals "/this/isCHANGED/a/testCHANGED/of/this/methodCHANGED", urlPath
	}
}
