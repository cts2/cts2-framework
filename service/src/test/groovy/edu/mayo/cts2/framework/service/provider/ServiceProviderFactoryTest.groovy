package edu.mayo.cts2.framework.service.provider;

import static org.junit.Assert.*


import org.junit.Before;
import org.junit.Test
import org.springframework.context.ApplicationContext

import edu.mayo.cts2.framework.service.profile.codesystem.CodeSystemReadService

class ServiceProviderFactoryTest {
	
	def factory
	
	@Before
	void setup(){
		factory = new ServiceProviderFactory()
	}
	
	@Test
	void testClassPathApplicationContext(){
		System.setProperty(ServiceProviderFactory.USE_CLASSPATH_PROVIDER_PROP, "true")
		def ac = {
			getBean : { [] as ServiceProvider }
		} as ApplicationContext
	
		factory.setApplicationContext(ac)
		
		assertNotNull factory.getServiceProvider()
	
	}

}
