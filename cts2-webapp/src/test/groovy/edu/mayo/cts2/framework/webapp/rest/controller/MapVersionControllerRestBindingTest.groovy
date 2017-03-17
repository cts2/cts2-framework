package edu.mayo.cts2.framework.webapp.rest.controller;

import static org.junit.Assert.*
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.server.result.MockMvcResultActions.*
import static org.springframework.test.web.server.setup.MockMvcBuilders.standaloneSetup

import javax.annotation.Resource

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.web.server.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

import edu.mayo.cts2.framework.core.config.ServerContext
import edu.mayo.cts2.framework.model.core.MapReference
import edu.mayo.cts2.framework.model.directory.DirectoryResult
import edu.mayo.cts2.framework.model.mapversion.MapVersion
import edu.mayo.cts2.framework.service.profile.mapversion.MapVersionQueryService
import edu.mayo.cts2.framework.service.profile.mapversion.MapVersionReadService

class MapVersionControllerRestBindingTest extends ControllerRestBindingTestBase {

	@Resource
	WebApplicationContext context
	
	@Resource
	MapVersionController controller

	@Override
	public getByUriUrl() {
		"/mapversionbyuri"
	}
	
	@Override
	public getByNameUrl() {
		"/map/mapname/version/mvn"
	}

	@Override
	public getUriToTest() {
		"http://some/uri/to/test.org"
	}
	
	@Override
	public initController() {
		def mapversion = new MapVersion(mapVersionName:"mvn",versionOf:new MapReference(content:"mapname"))
		
		def rs = [
			read:{id,readcontext -> mapversion }
		] as MapVersionReadService;
	
		controller.setMapVersionReadService(rs);
		
		controller
	}


	@Test
	void testGetMapVersions(){
		
		def qs = [
			getResourceSummaries:{
				query,sort,page -> 
				new DirectoryResult([],true)}
		] as MapVersionQueryService;
	
		controller.setMapVersionQueryService(qs);
		
		MockMvcBuilders
			.webApplicationContextSetup(context).build()
			.perform(get("/mapversions").param("format","xml"))
			.andExpect(response().status().isOk())
	}
	
	@Test
	void testGetMapVersionsOfMap(){
		
		def qs = [
			getResourceSummaries:{
				query,sort,page ->
				new DirectoryResult([],true)}
		] as MapVersionQueryService;
	
		controller.setMapVersionQueryService(qs);
		
		MockMvcBuilders
			.webApplicationContextSetup(context).build()
			.perform(get("/map/mname/versions"))
			.andExpect(response().status().isOk())
	}
}

