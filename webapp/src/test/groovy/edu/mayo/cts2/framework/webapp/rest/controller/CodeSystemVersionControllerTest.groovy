package edu.mayo.cts2.framework.webapp.rest.controller;

import static org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import org.springframework.mock.web.MockHttpServletRequest

import edu.mayo.cts2.framework.core.config.ServerContext
import edu.mayo.cts2.framework.core.config.ServiceConfigManager
import edu.mayo.cts2.framework.model.codesystemversion.CodeSystemVersionCatalogEntry
import edu.mayo.cts2.framework.model.core.Message
import edu.mayo.cts2.framework.service.profile.codesystemversion.CodeSystemVersionReadService
import edu.mayo.cts2.framework.webapp.rest.command.QueryControl;
import edu.mayo.cts2.framework.webapp.naming.CodeSystemVersionNameResolver

 class CodeSystemVersionControllerTest {
	
	CodeSystemVersionController controller
	
	def codeSystemVersionReadService = [
		read: {csvId,context -> return new CodeSystemVersionCatalogEntry() },
		getCodeSystemByVersionId: {csId,csvId,context -> return new CodeSystemVersionCatalogEntry() }
	] as CodeSystemVersionReadService

	def codeSystemVersionNameResolver = [
		getCodeSystemVersionNameFromVersionId: {service,csName,versionId,context -> return csName+"_"+versionId },
	] as CodeSystemVersionNameResolver

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
		controller.codeSystemVersionNameResolver = codeSystemVersionNameResolver
		controller.codeSystemVersionReadService = codeSystemVersionReadService
		controller.serviceConfigManager = serviceConfigManager
	}
	
	@Test
	void testGetCodeSystemVersionByNameOrOfficialResourceVersionIdInstanceOfMessage(){
		def result = controller.getCodeSystemVersionByNameOrOfficialResourceVersionId(httpServletRequest, null, new QueryControl(), "csname", "csvname")
		
		assert result instanceof Message	
	}
	
	@Test
	void testGetCodeSystemVersionByNameOrOfficialResourceVersionIdNameHasEverything(){
		def result = controller.getCodeSystemVersionByNameOrOfficialResourceVersionId(httpServletRequest, null, new QueryControl(), "csname", "csvname")
	
		assertNotNull result.heading
		assertNotNull result.heading.resourceRoot
		assertNotNull result.heading.resourceURI
		assertNotNull result.heading.accessDate
		
	}
}
