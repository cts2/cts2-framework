package edu.mayo.cts2.framework.webapp.rest.controller;

import static org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockServletContext

import edu.mayo.cts2.framework.core.config.ServerContext
import edu.mayo.cts2.framework.model.codesystem.CodeSystemCatalogEntry
import edu.mayo.cts2.framework.model.codesystem.CodeSystemCatalogEntryDirectory
import edu.mayo.cts2.framework.model.command.Page;
import edu.mayo.cts2.framework.model.directory.DirectoryResult


class AbstractMessageWrappingControllerTest {

	def serverContext = [
		getServerRootWithAppName: { "http://test/webapp" }
	] as ServerContext

	def controller = new AbstractMessageWrappingController(){}

	MockHttpServletRequest httpServletRequest
	
	@Before
	void init(){
		controller.serverContext = serverContext
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

		def dirResult = new DirectoryResult(["one","two"], true)
		
		
		def restResource = controller.
			populateDirectory( 
				dirResult, 
				new Page(), 
				httpServletRequest, 
				CodeSystemCatalogEntryDirectory.class )

		assertEquals 2, restResource.numEntries
	}
	

}
