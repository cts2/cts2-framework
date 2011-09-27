package edu.mayo.cts2.framework.webapp.rest.controller;

import static org.junit.Assert.*

import javax.servlet.http.HttpServletRequest

import org.junit.Before
import org.junit.Test

import edu.mayo.cts2.framework.core.constants.ModelAndViewInterface
import edu.mayo.cts2.framework.core.url.UrlConstructor
import edu.mayo.cts2.framework.model.codesystemversion.CodeSystemVersionCatalogEntry
import edu.mayo.cts2.framework.model.core.Message
import edu.mayo.cts2.framework.service.profile.codesystemversion.CodeSystemVersionReadService
import edu.mayo.cts2.framework.webapp.rest.controller.CodeSystemVersionController

 class CodeSystemVersionControllerTest {
	
	CodeSystemVersionController controller
	
	def codeSystemVersionReadService = [
		read: {csvId -> return new CodeSystemVersionCatalogEntry() }
	] as CodeSystemVersionReadService

	def httpServletRequest = [
		getServletPath: { '/codesystem/ID/version/V_ID' },
		getRequestURL:  { 'http://test/webapp/codesystem/ID/version/V_ID'<<'' },
		getParameterMap:  { ["format":"xml"] } ] as HttpServletRequest

	def urlConstructor = [
		getServerRootWithAppName: { "http://test/webapp" }
	] as UrlConstructor

	
	@Before
	void setup(){
		controller = new CodeSystemVersionController()
		controller.codeSystemVersionReadService = codeSystemVersionReadService
		controller.setUrlConstructor(urlConstructor)
	}
	
	@Test
	void testGetCodeSystemVersionByNameInstanceOfMessage(){
		def result = controller.getCodeSystemVersionByName(httpServletRequest, "csname", "csvname")
		
		assert result instanceof Message	
	}
	
	@Test
	void testGetCodeSystemVersionByNameHasEverything(){
		def result = controller.getCodeSystemVersionByName(httpServletRequest, "csname", "csvname")
	
		assertNotNull result.heading
		assertNotNull result.heading.resourceRoot
		assertNotNull result.heading.resourceURI
		assertNotNull result.heading.accessDate
		
	}
}
