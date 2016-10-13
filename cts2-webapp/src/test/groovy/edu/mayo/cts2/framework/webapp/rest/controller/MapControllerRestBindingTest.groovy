package edu.mayo.cts2.framework.webapp.rest.controller;

import static org.junit.Assert.*
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.server.result.MockMvcResultActions.*
import static org.springframework.test.web.server.setup.MockMvcBuilders.standaloneSetup

import javax.annotation.Resource

import edu.mayo.cts2.framework.model.map.MapCatalogEntry
import edu.mayo.cts2.framework.service.profile.map.MapReadService

 class MapControllerRestBindingTest extends ControllerRestBindingTestBase {
	
	@Resource
	MapController controller
	
	@Override
	public getByUriUrl() {
		"/mapbyuri"
	}
	
	@Override
	public getByNameUrl() {
		"/map/mapname"
	}

	@Override
	public getUriToTest() {
		"http://some/uri/to/test.org"
	}
	
	@Override
	public initController() {
		def map = new MapCatalogEntry(mapName:"mapname")
		
		def rs = [
			read:{id,readcontext -> map }
		] as MapReadService;
	
		controller.setMapReadService(rs);
		
		controller
	}
}
