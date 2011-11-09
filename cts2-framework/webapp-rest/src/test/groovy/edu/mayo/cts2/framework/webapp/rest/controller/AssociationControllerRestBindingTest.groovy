package edu.mayo.cts2.framework.webapp.rest.controller;

import static org.junit.Assert.*
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.server.result.MockMvcResultActions.*
import static org.springframework.test.web.server.setup.MockMvcBuilders.standaloneSetup

import javax.annotation.Resource

import org.junit.Ignore

import edu.mayo.cts2.framework.model.association.Association
import edu.mayo.cts2.framework.model.core.CodeSystemReference
import edu.mayo.cts2.framework.model.core.CodeSystemVersionReference
import edu.mayo.cts2.framework.model.core.NameAndMeaningReference
import edu.mayo.cts2.framework.service.profile.association.AssociationReadService

@Ignore("Need to clarify what the expected behavior is.")
class AssociationControllerRestBindingTest extends ControllerRestBindingTestBase {
	
	@Resource
	AssociationController controller
	
	@Override
	public getByUriUrl() {
		"/associationbyuri"
	}
	
	@Override
	public getByNameUrl() {
		"/association/http://some/association.org"
	}

	@Override
	public getUriToTest() {
		"http://some/association.org"
	}
	
	@Override
	public getController() {
		def a = new Association(
			assertedBy:new CodeSystemVersionReference(
				codeSystem:new CodeSystemReference(content:"csname"),
				version:new NameAndMeaningReference(content:"csvname")),
			associationID:"http://some/association.org")
		
		def rs = [
			read:{id,readcontext -> a }
		] as AssociationReadService;
	
		controller.setAssociationReadService(rs);
		
		controller
	}
}
