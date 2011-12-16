package edu.mayo.cts2.framework.webapp.rest.controller;

import static org.junit.Assert.*
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.server.result.MockMvcResultActions.*
import static org.springframework.test.web.server.setup.MockMvcBuilders.standaloneSetup

import org.junit.Before
import org.junit.Test
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse

import edu.mayo.cts2.framework.core.config.ServerContext
import edu.mayo.cts2.framework.core.config.ServiceConfigManager
import edu.mayo.cts2.framework.core.constants.URIHelperInterface
import edu.mayo.cts2.framework.model.codesystem.CodeSystemCatalogEntry
import edu.mayo.cts2.framework.model.command.ResolvedFilter
import edu.mayo.cts2.framework.model.command.ResolvedReadContext
import edu.mayo.cts2.framework.model.core.Message
import edu.mayo.cts2.framework.model.exception.Cts2RestException
import edu.mayo.cts2.framework.model.service.exception.UnknownCodeSystem
import edu.mayo.cts2.framework.service.profile.codesystem.CodeSystemQueryService
import edu.mayo.cts2.framework.service.profile.codesystem.CodeSystemReadService
import edu.mayo.cts2.framework.webapp.rest.command.QueryControl
import edu.mayo.cts2.framework.webapp.rest.resolver.FilterResolver
import edu.mayo.cts2.framework.webapp.rest.resolver.ReadContextResolver

 class CodeSystemControllerTest {
	
	CodeSystemController controller
	
	def codeSystemReadService = [
		read: {name, context -> return new CodeSystemCatalogEntry() }
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
		def result = controller.getCodeSystemByName(httpServletRequest, null, new QueryControl(), "id")
		
		assert result instanceof Message	
	}
	
	@Test
	void testGetCodeSystemByNameHasEverything(){
		def result = controller.getCodeSystemByName(httpServletRequest, null, new QueryControl(), "id")
	
		assertNotNull result.heading
		assertNotNull result.heading.resourceRoot
		assertNotNull result.heading.resourceURI
		assertNotNull result.heading.accessDate
		
	}
	
	@Test
	void testGetCodeSystemsCount(){
		MockHttpServletResponse response = new MockHttpServletResponse()
		
		def codeSystemQueryService = [ count: {query -> 1}] as CodeSystemQueryService
		controller.codeSystemQueryService = codeSystemQueryService
	
		def readContextResolver = [
			resolveRestReadContext: {var -> new ResolvedReadContext() }
		] as ReadContextResolver;
		controller.readContextResolver = readContextResolver
		
		def filterResolver = [
			resolveRestFilter: {o,t -> new ResolvedFilter() }
		] as FilterResolver;
		controller.filterResolver = filterResolver
			
		def result = controller.getCodeSystemsCount(response, null, null)
	
		assertEquals "1", response.getHeader(URIHelperInterface.HEADER_COUNT)
		
	}
	
	@Test
	void testDoesCodeSystemExistFalse(){
		MockHttpServletResponse response = new MockHttpServletResponse()
		
		def codeSystemReadService = [ exists: {query,context -> false}] as CodeSystemReadService
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
		
		def codeSystemReadService = [ exists: {query,context -> true}] as CodeSystemReadService
		
		controller.codeSystemReadService = codeSystemReadService
		def result = controller.doesCodeSystemExist(response, "testName")
		
		assertEquals 200, response.getStatus()
	}
}
