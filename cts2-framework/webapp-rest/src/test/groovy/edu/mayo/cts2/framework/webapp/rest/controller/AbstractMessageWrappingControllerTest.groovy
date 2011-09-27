package edu.mayo.cts2.framework.webapp.rest.controller;

import static org.junit.Assert.*

import javax.servlet.http.HttpServletRequest

import org.junit.Test

import edu.mayo.cts2.framework.core.url.UrlConstructor
import edu.mayo.cts2.framework.model.codesystem.CodeSystemCatalogEntryDirectory
import edu.mayo.cts2.framework.model.directory.DirectoryResult
import edu.mayo.cts2.framework.service.command.Page
import edu.mayo.cts2.framework.webapp.rest.controller.AbstractMessageWrappingController


class AbstractMessageWrappingControllerTest {

	def urlConstructor = [
		getServerRootWithAppName: { "http://test/webapp" }
	] as UrlConstructor

	def controller = [
		getUrlConstructor: { urlConstructor }
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
}
