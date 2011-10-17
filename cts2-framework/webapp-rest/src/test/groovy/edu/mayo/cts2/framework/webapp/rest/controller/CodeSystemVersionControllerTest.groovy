package edu.mayo.cts2.framework.webapp.rest.controller;

import static org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import org.springframework.mock.web.MockHttpServletRequest

import edu.mayo.cts2.framework.core.config.ServerContext
import edu.mayo.cts2.framework.core.config.ServiceConfigManager
import edu.mayo.cts2.framework.model.codesystemversion.CodeSystemVersionCatalogEntry
import edu.mayo.cts2.framework.model.core.Message
import edu.mayo.cts2.framework.service.command.QueryControl
import edu.mayo.cts2.framework.service.profile.codesystemversion.CodeSystemVersionReadService

 class CodeSystemVersionControllerTest {
	
	CodeSystemVersionController controller
	
	def codeSystemVersionReadService = [
		read: {csvId -> return new CodeSystemVersionCatalogEntry() }
	] as CodeSystemVersionReadService

	def httpServletRequest = new MockHttpServletRequest()

	def serverContext = [
		getServerRootWithAppName: { "http://test/webapp" }
	] as ServerContext

	def serviceConfigManager = [
		getServerContext: { serverContext }
	] as ServiceConfigManager

	
	@Before
	void setup(){
		controller = new CodeSystemVersionController()
		controller.codeSystemVersionReadService = codeSystemVersionReadService
		controller.serviceConfigManager = serviceConfigManager
	}
	
	@Test
	void testGetCodeSystemVersionByNameInstanceOfMessage(){
		def result = controller.getCodeSystemVersionByName(httpServletRequest, new QueryControl(), "csname", "csvname")
		
		assert result instanceof Message	
	}
	
	@Test
	void testGetCodeSystemVersionByNameHasEverything(){
		def result = controller.getCodeSystemVersionByName(httpServletRequest, new QueryControl(), "csname", "csvname")
	
		assertNotNull result.heading
		assertNotNull result.heading.resourceRoot
		assertNotNull result.heading.resourceURI
		assertNotNull result.heading.accessDate
		
	}
}
