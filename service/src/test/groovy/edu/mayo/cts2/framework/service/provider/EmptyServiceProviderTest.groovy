package edu.mayo.cts2.framework.service.provider;

import static org.junit.Assert.*


import org.junit.Before;
import org.junit.Test

import edu.mayo.cts2.framework.service.profile.codesystem.CodeSystemReadService


class EmptyServiceProviderTest {
	
	def p
	
	@Before
	void setup(){
		p = new EmptyServiceProvider()
	}
	
	@Test
	void testReturnsNull(){
		assertNull p.getService(CodeSystemReadService.class)
	
	}

}
