package edu.mayo.cts2.framework.core.url;

import static org.junit.Assert.*

import org.junit.Before
import org.junit.Test

import edu.mayo.cts2.framework.core.config.ServerContext

class UrlConstructorTest {
	
	UrlConstructor urlConstructor = new UrlConstructor()
	
	@Before
	void setup(){
		ServerContext serverContext = new ServerContext()
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

}
