package edu.mayo.cts2.framework.webapp.rest.controller;

import static org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse

import edu.mayo.cts2.framework.core.config.ServerContext
import edu.mayo.cts2.framework.core.config.ServiceConfigManager
import edu.mayo.cts2.framework.core.constants.URIHelperInterface
import edu.mayo.cts2.framework.model.codesystem.CodeSystemCatalogEntry
import edu.mayo.cts2.framework.model.core.Message
import edu.mayo.cts2.framework.model.exception.Cts2RestException
import edu.mayo.cts2.framework.model.service.exception.UnknownCodeSystem
import edu.mayo.cts2.framework.service.command.QueryControl
import edu.mayo.cts2.framework.service.profile.codesystem.CodeSystemQueryService
import edu.mayo.cts2.framework.service.profile.codesystem.CodeSystemReadService

 class CodeSystemControllerTest {
	
	CodeSystemController controller
	
	def codeSystemReadService = [
		read: {name -> return new CodeSystemCatalogEntry() }
	] as CodeSystemReadService

	def httpServletRequest = new MockHttpServletRequest()
	
	def serverContext = [
		getServerRootWithAppName: { "http://test/webapp" }
	] as ServerContext

	def serviceConfigManager = [
		getServerContext: { serverContext }
	] as ServiceConfigManager
	
	@Before
	void setup(){
		controller = new CodeSystemController()
		controller.codeSystemReadService = codeSystemReadService
		controller.serviceConfigManager = serviceConfigManager
	}
	
	@Test
	void testGetCodeSystemByNameInstanceOfMessage(){
		def result = controller.getCodeSystemByName(httpServletRequest, new QueryControl(), "id")
		
		assert result instanceof Message	
	}
	
	@Test
	void testGetCodeSystemByNameHasEverything(){
		def result = controller.getCodeSystemByName(httpServletRequest, new QueryControl(), "id")
	
		assertNotNull result.heading
		assertNotNull result.heading.resourceRoot
		assertNotNull result.heading.resourceURI
		assertNotNull result.heading.accessDate
		
	}
	
	@Test
	void testGetCodeSystemsCount(){
		MockHttpServletResponse response = new MockHttpServletResponse()
		
		def codeSystemQueryService = [ count: {query,restrictions,filter -> 1}] as CodeSystemQueryService
		controller.codeSystemQueryService = codeSystemQueryService
			
		def result = controller.getCodeSystemsCount(response, null, null)
	
		assertEquals "1", response.getHeader(URIHelperInterface.HEADER_COUNT)
		
	}
	
	@Test
	void testDoesCodeSystemExistFalse(){
		MockHttpServletResponse response = new MockHttpServletResponse()
		
		def codeSystemReadService = [ exists: {query -> false}] as CodeSystemReadService
		controller.codeSystemReadService = codeSystemReadService
			
		try{
			def result = controller.doesCodeSystemExist(response, "test")
		} catch (Cts2RestException e){
		assert e.cts2Exception instanceof UnknownCodeSystem
		return
		}
		
		fail
	}
	
	@Test
	void testDoesCodeSystemExistTrue(){
		MockHttpServletResponse response = new MockHttpServletResponse()
		
		def codeSystemReadService = [ exists: {query -> true}] as CodeSystemReadService
		
		controller.codeSystemReadService = codeSystemReadService
		def result = controller.doesCodeSystemExist(response, null)
		
		assertEquals 200, response.getStatus()
	}

}
