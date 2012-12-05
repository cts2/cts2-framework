package edu.mayo.cts2.framework.webapp.rest.controller;

import static org.junit.Assert.*

import javax.xml.transform.stream.StreamResult

import org.junit.Before
import org.junit.Test
import org.springframework.mock.web.MockHttpServletRequest

import edu.mayo.cts2.framework.core.config.ServerContext
import edu.mayo.cts2.framework.core.xml.DelegatingMarshaller
import edu.mayo.cts2.framework.model.core.SourceReference
import edu.mayo.cts2.framework.service.profile.codesystem.CodeSystemReadService
import edu.mayo.cts2.framework.service.provider.ServiceProvider
import edu.mayo.cts2.framework.service.provider.ServiceProviderFactory
import edu.mayo.cts2.framework.webapp.rest.controller.ServiceController.InvalidServiceRequest
import edu.mayo.cts2.framework.webapp.service.ServiceBuilder

 class ServiceControllerTest {
	 
	def marshaller = new DelegatingMarshaller()
	
	ServiceController controller

	def serverContext = [
		getServerRootWithAppName: { "http://test/webapp" }
	] as ServerContext
	
	@Before
	void setup(){
		controller = new ServiceController()
		def serviceBuilder = new ServiceBuilder()
		 
		serviceBuilder.serverContext = serverContext

		serviceBuilder.afterPropertiesSet()
		
		controller.serviceBuilder = serviceBuilder
	
		controller.serviceProviderFactory = [
			getServiceProvider : {
				[
					getService: {
						[
							getServiceName : { -> null },
							getServiceProvider : { -> null },
							getServiceVersion : { -> null },
							getServiceDescription : { -> null },
							getKnownNamespaceList : { -> null }
						] as CodeSystemReadService
					}
				] as ServiceProvider
			} 
		] as ServiceProviderFactory
	}
	
	@Test(expected=InvalidServiceRequest)
	void TestGetBadServiceStructure(){
		def httpServletRequest = new MockHttpServletRequest()
		
		def result =
			controller.getService(httpServletRequest, "watermelonread")
	}
	
	@Test(expected=InvalidServiceRequest)
	void TestGetBadServiceProfile(){
		def httpServletRequest = new MockHttpServletRequest()
		
		def result =
			controller.getService(httpServletRequest, "codesystemdanceapolka")
	}
	
	@Test
	void TestGetService(){
		def httpServletRequest = new MockHttpServletRequest()
		
		def result = 
			controller.getService(httpServletRequest, "codesystemread").body

	   assertNotNull result
	}
	
	@Test
	void TestGetServiceValidXml(){
		def httpServletRequest = new MockHttpServletRequest()
		
		def result =
			controller.getService(httpServletRequest, "codesystemread").body

	   assertNotNull result
	   
	   def sw = new StringWriter();
	   
	   marshaller.marshal(result, new StreamResult(sw))
	   
	   println sw
	}
}
