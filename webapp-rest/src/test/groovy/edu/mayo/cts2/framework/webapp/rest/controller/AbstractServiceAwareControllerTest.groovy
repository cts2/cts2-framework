package edu.mayo.cts2.framework.webapp.rest.controller;

import static org.junit.Assert.*

import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.springframework.mock.web.MockHttpServletRequest

import edu.mayo.cts2.framework.model.command.Page
import edu.mayo.cts2.framework.service.profile.codesystem.CodeSystemReadService

 class AbstractServiceAwareControllerTest {
	
	private AbstractServiceAwareController controller;
	
	@Before
	void setup(){
		this.controller = new AbstractServiceAwareController(){}
	}
	
	@Test(expected=UnsupportedOperationException)
	void TestProxyNullService(){
		CodeSystemReadService service = controller.proxyNullService(CodeSystemReadService)
		
		service.read(null, null);
	}
	
}
