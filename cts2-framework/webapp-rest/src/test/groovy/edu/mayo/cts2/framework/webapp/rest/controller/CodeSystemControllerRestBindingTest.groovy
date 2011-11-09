package edu.mayo.cts2.framework.webapp.rest.controller;

import static org.junit.Assert.*
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.server.result.MockMvcResultActions.*
import static org.springframework.test.web.server.setup.MockMvcBuilders.standaloneSetup

import org.junit.Before
import org.junit.Test
import org.springframework.http.MediaType
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.test.web.server.setup.MockMvcBuilders

import edu.mayo.cts2.framework.core.config.ServerContext
import edu.mayo.cts2.framework.core.config.ServiceConfigManager
import edu.mayo.cts2.framework.core.constants.URIHelperInterface
import edu.mayo.cts2.framework.model.codesystem.CodeSystemCatalogEntry
import edu.mayo.cts2.framework.model.core.Message
import edu.mayo.cts2.framework.model.directory.DirectoryResult
import edu.mayo.cts2.framework.model.exception.Cts2RestException
import edu.mayo.cts2.framework.model.service.exception.UnknownCodeSystem
import edu.mayo.cts2.framework.service.profile.codesystem.CodeSystemQueryService
import edu.mayo.cts2.framework.service.profile.codesystem.CodeSystemReadService
import edu.mayo.cts2.framework.webapp.rest.command.QueryControl

 class CodeSystemControllerRestBindingTest {
	
	CodeSystemController controller
	
	def serverContext = [
		getServerRootWithAppName: { "http://test/webapp" }
	] as ServerContext

	def serviceConfigManager = [
		getServerContext: { serverContext }
	] as ServiceConfigManager
	
	@Before
	void setup(){
		controller = new CodeSystemController()
		controller.serviceConfigManager = serviceConfigManager
	}
	
	@Test
	void testGetCodeSystems(){
		
		def qs = [
			getResourceSummaries:{
				query,filter,restrictions,readcontext,page -> 
				assertEquals page.getPage(), 1
				new DirectoryResult([],true,true)}
		] as CodeSystemQueryService;
	
		controller.codeSystemQueryService = qs;
		
		MockMvcBuilders
			.standaloneSetup(new CodeSystemController(
				serviceConfigManager:serviceConfigManager, 
				codeSystemQueryService:qs)).build()
			.perform(get("/codesystems").param("page","1"))
			.andExpect(response().status().isOk())
	}

}
