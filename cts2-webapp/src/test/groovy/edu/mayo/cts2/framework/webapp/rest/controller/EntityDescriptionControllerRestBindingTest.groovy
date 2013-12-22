package edu.mayo.cts2.framework.webapp.rest.controller
import edu.mayo.cts2.framework.model.codesystemversion.CodeSystemVersionCatalogEntry
import edu.mayo.cts2.framework.model.core.*
import edu.mayo.cts2.framework.model.directory.DirectoryResult
import edu.mayo.cts2.framework.model.entity.EntityDescription
import edu.mayo.cts2.framework.model.entity.EntityListEntry
import edu.mayo.cts2.framework.model.entity.NamedEntityDescription
import edu.mayo.cts2.framework.service.profile.codesystemversion.CodeSystemVersionReadService
import edu.mayo.cts2.framework.service.profile.entitydescription.EntityDescriptionReadService
import org.junit.Test
import org.springframework.test.web.server.setup.MockMvcBuilders

import javax.annotation.Resource

import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.server.result.MockMvcResultActions.response

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
		"/entity/ns:entityName"
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
			read:{id,readcontext -> ed },
			availableDescriptions:{id,cxt -> new EntityReference(
				about: "http://test/about",
				name: new ScopedEntityName(name:"entityName", namespace:"ns"),
                knownEntityDescription: [
                        new DescriptionInCodeSystem(
                                href: "http://server/codesystem/TESTCS/version/TESTCSVERSION/entity/entityName",
                                describingCodeSystemVersion: entry.getDescribingCodeSystemVersion()
                        )
                ]
            ) },
            readEntityDescriptions:{id,sort,ctx,page->
                new DirectoryResult([new EntityListEntry(
                      href: "http://server/codesystem/TESTCS/version/TESTCSVERSION/entity/entityName",
                      entry: ed
                )], true)
            }
		] as EntityDescriptionReadService;
	
		def csv = new CodeSystemVersionCatalogEntry(
			codeSystemVersionName:"TESTCSVERSION",
			officialResourceVersionId:"v1",
			versionOf:new CodeSystemReference(content:"TESTCS"))
	
		def csvrs = [
			getCodeSystemByVersionId:{csn,versionid,readcontext -> csv },
			read:{csvn,readcontext -> csv },
			readByTag:{o,t,th -> csv}
		] as CodeSystemVersionReadService;

		controller.setEntityDescriptionReadService(rs)
		controller.setCodeSystemVersionReadService(csvrs)
		
		csvcontroller.setCodeSystemVersionReadService(csvrs)
		
		controller
	}

    @Test
    void testGetOneEntityDescriptionWithRedirect(){
        MockMvcBuilders
                .webApplicationContextSetup(context).build()
                .perform(get("/entity/ns:entityName"))
                .andExpect(response().status().isOk())
                .andExpect(response().redirectedUrl("http://server/codesystem/TESTCS/version/TESTCSVERSION/entity/entityName"))
    }

    @Test
    void testGetOneEntityDescriptionWithRedirectList(){
        MockMvcBuilders
                .webApplicationContextSetup(context).build()
                .perform(get("/entity/ns:entityName").param("list","true"))
                .andExpect(response().status().isOk())
                .andExpect(response().redirectedUrl("http://server/codesystem/TESTCS/version/TESTCSVERSION/entity/entityName"))
    }

	@Test
	void testGetByCodeSystemVersionUriWithRedirect(){

		MockMvcBuilders
			.webApplicationContextSetup(context).build()
			.perform(get("/codesystemversionbyuri/entity/ns:code").param("uri","http://some/uri.org").param("redirect","true"))
			.andExpect(response().status().isOk())
			.andExpect(response().redirectedUrl("/codesystem/TESTCS/version/v1/entity/ns:code"))
	}
	
	@Test
	void testGetByCodeSystemVersionUriWithRedirectDefault(){

		MockMvcBuilders
			.webApplicationContextSetup(context).build()
			.perform(get("/codesystemversionbyuri/entity/ns:code").param("uri","http://some/uri.org"))
			.andExpect(response().status().isOk())
			.andExpect(response().redirectedUrl("/codesystem/TESTCS/version/v1/entity/ns:code"))
	}
	
	@Test
	void testGetByTag(){

		MockMvcBuilders
			.webApplicationContextSetup(context).build()
			.perform(get("/codesystem/TESTCS/entity/ns:code"))
			.andExpect(response().status().isOk())
	}
	
	@Test
	void testGetByTagJson(){

		MockMvcBuilders
			.webApplicationContextSetup(context).build()
			.perform(get("/codesystem/TESTCS/entity/ns:code").param("format", "json"))
			.andExpect(response().status().isOk())
	}

}
