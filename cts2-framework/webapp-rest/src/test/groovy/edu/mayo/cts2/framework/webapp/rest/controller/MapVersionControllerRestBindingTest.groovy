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
import edu.mayo.cts2.framework.core.config.ServiceConfigManager
import edu.mayo.cts2.framework.model.directory.DirectoryResult
import edu.mayo.cts2.framework.service.profile.mapversion.MapVersionQueryService

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
	loader=TestGenericWebXmlContextLoader,
	locations=["file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml","classpath:test-web-context.xml"])
class MapVersionControllerRestBindingTest {

	@Resource
	WebApplicationContext context
	
	@Resource
	MapVersionController controller
	
	def serverContext = [
		getServerRootWithAppName: { "http://test/webapp" }
	] as ServerContext

	def serviceConfigManager = [
		getServerContext: { serverContext }
	] as ServiceConfigManager
	
	@Before
	void setup(){
		controller.serviceConfigManager = serviceConfigManager
	}
	
	@Test
	void testGetMapVersions(){
		
		def qs = [
			getResourceSummaries:{
				query,filter,restrictions,readcontext,page -> 
				new DirectoryResult([],true,true)}
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
				query,filter,restrictions,readcontext,page ->
				new DirectoryResult([],true,true)}
		] as MapVersionQueryService;
	
		controller.setMapVersionQueryService(qs);
		
		MockMvcBuilders
			.webApplicationContextSetup(context).build()
			.perform(get("/map/mname/versions"))
			.andExpect(response().status().isOk())
	}

}

class TestGenericWebXmlContextLoader extends GenericWebXmlContextLoader {
	
	public TestGenericWebXmlContextLoader() {
	super("src/test/resources/META-INF/web-resources", false);
	}
	
	}

