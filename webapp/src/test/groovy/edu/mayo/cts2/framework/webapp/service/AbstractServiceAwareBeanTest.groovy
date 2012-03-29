package edu.mayo.cts2.framework.webapp.service;

import static org.junit.Assert.*

import org.junit.Before
import org.junit.Test

import edu.mayo.cts2.framework.service.profile.codesystem.CodeSystemReadService

 class AbstractServiceAwareBeanTest {
	
	private AbstractServiceAwareBean controller;
	
	@Before
	void setup(){
		this.controller = new AbstractServiceAwareBean(){}
	}
	
	@Test(expected=UnsupportedOperationException)
	void TestProxyNullService(){
		CodeSystemReadService service = controller.proxyNullService(CodeSystemReadService)
		
		service.read(null, null);
	}
	
}
