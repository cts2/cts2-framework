package edu.mayo.cts2.framework.core.url;

import static org.junit.Assert.*

import org.junit.Before
import org.junit.Test

import edu.mayo.cts2.framework.core.config.ServerContext
import edu.mayo.cts2.framework.core.config.TestServerContext
import edu.mayo.cts2.framework.model.service.core.types.FunctionalProfile
import edu.mayo.cts2.framework.model.service.core.types.StructuralProfile

class UrlConstructorTest {
	
	UrlConstructor urlConstructor = new UrlConstructor()
	
	@Before
	void setup(){
		ServerContext serverContext = new TestServerContext()
		serverContext.appName = "testapp"
		serverContext.serverRoot = "http://serverRoot:8080"
		
		urlConstructor.serverContext = serverContext
	}
	
	@Test
	void "Test create CodeSystem URL"(){
		String url = urlConstructor.createCodeSystemUrl("CSN");
		
		assertEquals "http://serverRoot:8080/testapp/codesystem/CSN", url
	}
	
	@Test
	void "Test create CodeSystemVersion URL"(){
		String url = urlConstructor.createCodeSystemVersionUrl("CSN", "CSVN");
		
		assertEquals "http://serverRoot:8080/testapp/codesystem/CSN/version/CSVN", url
	}
	
	@Test
	void "Test create Entity URL"(){
		String url = urlConstructor.createEntityUrl("CSN", "CSVN", "ENTITY");
		
		assertEquals "http://serverRoot:8080/testapp/codesystem/CSN/version/CSVN/entity/ENTITY", url
	}
	
	@Test
	void "Test create Entities of CodeSystemVersion URL"(){
		String url = urlConstructor.createEntitiesOfCodeSystemVersionUrl("CSN", "CSVN");
		
		assertEquals "http://serverRoot:8080/testapp/codesystem/CSN/version/CSVN/entities", url
	}

	@Test
	void "Test create Service URL"(){
		String url = urlConstructor.createServiceUrl(StructuralProfile.SP_CODE_SYSTEM, FunctionalProfile.FP_READ);

		assertEquals "http://serverRoot:8080/testapp/service/codesystemread", url
	}
}
