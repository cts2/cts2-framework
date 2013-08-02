package edu.mayo.cts2.framework.util.spring;

import static org.junit.Assert.*;

import org.junit.Before
import org.junit.Test
import org.springframework.context.ApplicationContext

import edu.mayo.cts2.framework.service.profile.Cts2Profile
import edu.mayo.cts2.framework.service.profile.codesystem.CodeSystemQueryService
import edu.mayo.cts2.framework.service.profile.codesystem.CodeSystemReadService

class AbstractSpringServiceProviderTest {
	
	def provider
	
	@Before
	void setup(){
		provider = new AbstractSpringServiceProvider(){}
	}
	
	@Test
	void testBeanNotPresent(){
		provider.applicationContext = {
			getBeansOfType : { null as Map }
		} as ApplicationContext
	
		assertNull provider.getService(CodeSystemReadService.class)
	
	}
	
	@Test
	void testBeanPresent(){
		provider.applicationContext = {
			getBeansOfType : { 
		
				if(it.name.equals(CodeSystemReadService.class.name)){
					//this is good
				 } else {
				 	fail()
				 }
			
				 [ "test":{} as CodeSystemReadService ]
			}
		} as ApplicationContext
	
		assertNotNull provider.getService(CodeSystemReadService.class)
	
	}
	
	@Test
	void testBeanPresentWrongName(){
		provider.applicationContext = {
			getBeansOfType : {
		
				if(it.name.equals(CodeSystemReadService.class.name)){
					[ "test":{} as CodeSystemReadService ]
				 } else {
					null as Map
				 }
 
			}
		} as ApplicationContext
	
		assertNull provider.getService(CodeSystemQueryService.class)
	
	}

}
