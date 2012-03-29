package edu.mayo.cts2.framework.webapp.rest.controller;

import static org.junit.Assert.*
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.server.result.MockMvcResultActions.*
import static org.springframework.test.web.server.setup.MockMvcBuilders.standaloneSetup

import javax.annotation.Resource

import org.junit.Ignore
import org.junit.Test;
import org.springframework.test.web.server.setup.MockMvcBuilders;

import edu.mayo.cts2.framework.model.codesystemversion.CodeSystemVersionCatalogEntry
import edu.mayo.cts2.framework.model.core.CodeSystemReference
import edu.mayo.cts2.framework.model.core.CodeSystemVersionReference
import edu.mayo.cts2.framework.model.core.NameAndMeaningReference
import edu.mayo.cts2.framework.model.core.ScopedEntityName
import edu.mayo.cts2.framework.model.entity.EntityDescription
import edu.mayo.cts2.framework.model.entity.NamedEntityDescription
import edu.mayo.cts2.framework.service.profile.codesystemversion.CodeSystemVersionReadService
import edu.mayo.cts2.framework.service.profile.entitydescription.EntityDescriptionReadService

@Ignore
class EntityDescriptionControllerRestBindingTest extends ControllerRestBindingTestBase {
	
	@Resource
	EntityDescriptionController controller
	
	@Resource
	CodeSystemVersionController csvcontroller
	
	@Override
	public getByUriUrl() {
		"/entitybyuri"
	}
	
	@Override
	public getByNameUrl() {
		"/codesystem/TESTCS/version/v1/entity/ns:entityName"
	}

	@Override
	public getUriToTest() {
		"http://some/uri/to/test.org"
	}
	
	@Override
	public getController() {
		def ed = new EntityDescription();
		
		def entry = new NamedEntityDescription(about:"about")
		entry.setEntityID(new ScopedEntityName(name:"entityName", namespace:"ns"))
		entry.setDescribingCodeSystemVersion(new CodeSystemVersionReference())
		entry.getDescribingCodeSystemVersion().setVersion(new NameAndMeaningReference())
		entry.getDescribingCodeSystemVersion().getVersion().setContent("TESTCSVERSION")
		entry.getDescribingCodeSystemVersion().setCodeSystem(new CodeSystemReference())
		entry.getDescribingCodeSystemVersion().getCodeSystem().setContent("TESTCS")
		
		ed.setNamedEntity(entry)
		
		def rs = [
			read:{id,readcontext -> ed }
		] as EntityDescriptionReadService;
	
		def csv = new CodeSystemVersionCatalogEntry(
			codeSystemVersionName:"TESTCSVERSION",
			officialResourceVersionId:"v1",
			versionOf:new CodeSystemReference(content:"TESTCS"))
	
		def csvrs = [
			getCodeSystemByVersionId:{csn,versionid,readcontext -> csv },
			read:{csvn,readcontext -> csv }
		] as CodeSystemVersionReadService;

		controller.setEntityDescriptionReadService(rs)
		controller.setCodeSystemVersionReadService(csvrs)
		
		csvcontroller.setCodeSystemVersionReadService(csvrs)
		
		controller
	}
	
	
	@Test
	void testGetByCodeSystemVersionUriWithRedirect(){

		MockMvcBuilders
			.webApplicationContextSetup(context).build()
			.perform(get("/codesystemversionbyuri/entity/ns:code").param("uri","http://some/uri.org").param("redirect","true"))
			.andExpect(response().status().isOk())
			.andExpect(response().redirectedUrl("/codesystem/TESTCS/version/v1/entity/ns:code"))
	}

}
