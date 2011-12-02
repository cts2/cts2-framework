package edu.mayo.cts2.framework.webapp.rest.controller;

import static org.junit.Assert.*
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.server.result.MockMvcResultActions.*
import static org.springframework.test.web.server.setup.MockMvcBuilders.standaloneSetup

import javax.annotation.Resource

import org.junit.Test
import org.springframework.test.web.server.setup.MockMvcBuilders

import edu.mayo.cts2.framework.model.core.CodeSystemReference
import edu.mayo.cts2.framework.model.core.NameAndMeaningReference
import edu.mayo.cts2.framework.model.core.SourceAndNotation
import edu.mayo.cts2.framework.model.core.ValueSetDefinitionReference
import edu.mayo.cts2.framework.model.core.ValueSetReference
import edu.mayo.cts2.framework.model.core.types.SetOperator
import edu.mayo.cts2.framework.model.directory.DirectoryResult
import edu.mayo.cts2.framework.model.extension.LocalIdValueSetDefinition
import edu.mayo.cts2.framework.model.valuesetdefinition.CompleteCodeSystemReference
import edu.mayo.cts2.framework.model.valuesetdefinition.ResolvedValueSet
import edu.mayo.cts2.framework.model.valuesetdefinition.ResolvedValueSetHeader
import edu.mayo.cts2.framework.model.valuesetdefinition.ValueSetDefinition
import edu.mayo.cts2.framework.model.valuesetdefinition.ValueSetDefinitionEntry
import edu.mayo.cts2.framework.service.profile.valuesetdefinition.ValueSetDefinitionReadService
import edu.mayo.cts2.framework.service.profile.valuesetdefinition.ValueSetDefinitionResolutionService

 class ValueSetDefinitionResolutionControllerRestBindingTest extends ControllerRestBindingTestBase {
	
	@Resource
	ValueSetDefinitionResolutionController controller
	
	@Override
	public getByUriUrl() {
		"/valuesetresolutionbyuri"
	}
	
	@Override
	public getByNameUrl() {
		"/valueset/vs/definition/1/resolution/1"
	}

	@Override
	public getUriToTest() {
		"http://some/uri/to/resolve/test.org"
	}
	
	@Override
	public getController() {
		def vsdef = createEntry(getUriToTest())
		
		def rs = [
			read:{id,readcontext -> vsdef }
		] as ValueSetDefinitionReadService;
	
		controller.setValueSetDefinitionReadService(rs);
		
		controller
	}
	
	@Test void TestResolutionDefaultByUri(){
	
		MockMvcBuilders
			.webApplicationContextSetup(context).build()
			.perform(get("/valuesetdefinitionbyuri/resolution")
				.param("uri","http://some/uri/to/test.org")
				.param("redirect","true")
			)
			.andExpect(response().status().isOk())
			.andExpect(response().redirectedUrl(getByNameUrl()+"/resolution"))

	}
	
	@Test void TestResolutionDefault(){
		
		def isCalled = false;
		
		def rs = [
			resolveDefinitionAsDirectory:{ p1,p2,p3,p4,p5,p6,p7-> 
				isCalled = true
				new DirectoryResult([],true,true)
			}
		] as ValueSetDefinitionResolutionService;
	
		controller.setValueSetDefinitionResolutionService(rs);
		
		MockMvcBuilders
			.webApplicationContextSetup(context).build()
			.perform(get("/valueset/vs/definition/1/resolution"))
			.andExpect(response().status().isOk())
			
		assertTrue isCalled
	}
	
	@Test void TestResolutionDefaultWithParam(){
		
		def isCalled = false;
		
		def rs = [
			resolveDefinitionAsDirectory:{ p1,p2,p3,p4,p5,p6,p7->
				isCalled = true
				new DirectoryResult([],true,true)
			}
		] as ValueSetDefinitionResolutionService;
	
		controller.setValueSetDefinitionResolutionService(rs);
		
		MockMvcBuilders
			.webApplicationContextSetup(context).build()
			.perform(get("/valueset/vs/definition/1/resolution").param("resolutiontype","directory"))
			.andExpect(response().status().isOk())
			
		assertTrue isCalled
	}
	
	@Test void TestResolutionCompleteSet(){
		
		def isCalled = false;
		
		def rs = [
			resolveDefinitionAsCompleteSet:{ p1,p2,p3,p4->
				isCalled = true
				new ResolvedValueSet(resolutionInfo:new ResolvedValueSetHeader(
					resolutionOf:new ValueSetDefinitionReference(
						valueSetDefinition: new NameAndMeaningReference(),
						valueSet: new ValueSetReference())))
			}
		] as ValueSetDefinitionResolutionService;
	
		controller.setValueSetDefinitionResolutionService(rs);
		
		MockMvcBuilders
			.webApplicationContextSetup(context).build()
			.perform(get("/valueset/vs/definition/1/resolution").param("resolutiontype","complete"))
			.andExpect(response().status().isOk())
			
		assertTrue isCalled
	}
	
	@Test void TestResolutionEntityDirectory(){
		
		def isCalled = false;
		
		def rs = [
			resolveDefinitionAsEntityDirectory:{ p1,p2,p3,p4,p5,p6,p7,p8-> 
				isCalled = true
				new DirectoryResult([],true,true)
			}
		] as ValueSetDefinitionResolutionService;
	
		controller.setValueSetDefinitionResolutionService(rs);
		
		MockMvcBuilders
			.webApplicationContextSetup(context).build()
			.perform(get("/valueset/vs/definition/1/resolution").param("resolutiontype","entitydirectory"))
			.andExpect(response().status().isOk())
			
		assertTrue isCalled
	}
	
	def createEntry(uri) {
		def entry = new ValueSetDefinition(about:"http://testAbout", documentURI:uri)
		entry.setSourceAndNotation(new SourceAndNotation())
		entry.setDefinedValueSet(new ValueSetReference(content:"vs"))
		entry.addEntry(new ValueSetDefinitionEntry())
		entry.getEntry(0).setCompleteCodeSystem(new CompleteCodeSystemReference())
		entry.getEntry(0).getCompleteCodeSystem().setCodeSystem(new CodeSystemReference())
		entry.getEntry(0).setOperator(SetOperator.UNION)
		entry.getEntry(0).setEntryOrder(1l);
		
		new LocalIdValueSetDefinition("1",entry)
	}
}
