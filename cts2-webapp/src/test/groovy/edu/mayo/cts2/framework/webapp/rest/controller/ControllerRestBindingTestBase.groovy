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

@RunWith(SpringJUnit4ClassRunner)
@ContextConfiguration(
	loader=TestGenericWebXmlContextLoader,
	locations=["file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml","classpath:test-web-context.xml"])
abstract class ControllerRestBindingTestBase {

	@Resource
	WebApplicationContext context
	
	def serverContext = [
		getServerRootWithAppName: { "http://test/webapp" }
	] as ServerContext

	@Before
	void setup(){
		getController().setServerContext(serverContext)
	}
	
	abstract getByUriUrl()
	abstract getByNameUrl()
	abstract getUriToTest()
	abstract getController()

	
	@Test
	void testGetByUriWithForward(){

		MockMvcBuilders
			.webApplicationContextSetup(context).build()
			.perform(get(getByUriUrl()).param("uri",getUriToTest()).param("redirect","false"))
			.andExpect(response().status().isOk())
			.andExpect(response().forwardedUrl(UriResolutionController.FORWARDING_URL))
			.andExpect(model().hasAttributes(UriResolutionController.ATTRIBUTE_NAME))

	}
	
	@Test
	void testGetByUriWithRedirect(){

		MockMvcBuilders
			.webApplicationContextSetup(context).build()
			.perform(get(getByUriUrl()).param("uri",getUriToTest()).param("redirect","true"))
			.andExpect(response().status().isOk())
			.andExpect(response().redirectedUrl(getByNameUrl()))
	}

}

class TestGenericWebXmlContextLoader extends GenericWebXmlContextLoader {
	
	public TestGenericWebXmlContextLoader() {
	super("src/main/webapp/WEB-INF", false);
	}
	
}

