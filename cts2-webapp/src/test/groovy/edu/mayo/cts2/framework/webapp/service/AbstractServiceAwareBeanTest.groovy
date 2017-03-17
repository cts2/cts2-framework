package edu.mayo.cts2.framework.webapp.service;

import static org.junit.Assert.*

import edu.mayo.cts2.framework.service.profile.Cts2Profile;
import edu.mayo.cts2.framework.service.provider.ServiceProvider;
import edu.mayo.cts2.framework.service.provider.ServiceProviderChangeObserver;
import edu.mayo.cts2.framework.service.provider.ServiceProviderFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.proxy.Invoker;
import org.apache.commons.proxy.ProxyFactory;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.Resource;
import org.junit.Before
import org.junit.Test

import edu.mayo.cts2.framework.service.profile.codesystem.CodeSystemReadService

class AbstractServiceAwareBeanTest {

	@Test(expected = UnsupportedOperationException)
	void TestProxyNullService() {
		def controller = new AbstractServiceAwareBean() {}
		CodeSystemReadService service = controller.proxyNullService(CodeSystemReadService)
		service.read(null, null);
	}

	class SomeTestTest extends AbstractServiceAwareBean {
		@edu.mayo.cts2.framework.webapp.service.AbstractServiceAwareBean.Cts2Service
		TestServiceInf testService
	}

	@Test

	void TestProxyInvoke() {

		def controller = new SomeTestTest();
		def serviceProvider = [getService: {new TestService()}] as ServiceProvider
		controller.setServiceProviderFactory([getServiceProvider: {serviceProvider}] as ServiceProviderFactory)
		controller.afterPropertiesSet()
		def service = controller.testService
		assertEquals "hi", service.testEcho("hi")
		}
	}

	class TestService implements TestServiceInf {
		@Override
		public testEcho(String msg) {
			msg
		}
	}
	
	interface TestServiceInf extends Cts2Profile {
		public testEcho(String msg)
	}
	
