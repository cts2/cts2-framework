package edu.mayo.cts2.framework.webapp.rest.controller;

import static org.junit.Assert.*
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.server.result.MockMvcResultActions.*
import static org.springframework.test.web.server.setup.MockMvcBuilders.standaloneSetup

import javax.annotation.Resource

import org.junit.Before;

import edu.mayo.cts2.framework.model.codesystem.CodeSystemCatalogEntry
import edu.mayo.cts2.framework.service.profile.codesystem.CodeSystemReadService

 class CodeSystemControllerRestBindingTest extends ControllerRestBindingTestBase {
	
	@Resource
	CodeSystemController controller
	
	@Override
	public getByUriUrl() {
		"/codesystembyuri"
	}
	
	@Override
	public getByNameUrl() {
		"/codesystem/csn"
	}

	@Override
	public getUriToTest() {
		"http://some/uri/to/test.org"
	}
	
	@Override
	public initController() {
		def cs = new CodeSystemCatalogEntry(codeSystemName:"csn")
		
		def rs = [
			read:{id,readcontext -> cs }
		] as CodeSystemReadService;
	
		controller.setCodeSystemReadService(rs);
		
		controller
	}
}
