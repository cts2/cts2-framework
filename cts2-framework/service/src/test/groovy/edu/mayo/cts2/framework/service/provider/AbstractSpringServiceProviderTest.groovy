package edu.mayo.cts2.framework.service.provider;

import static org.junit.Assert.*

import java.util.Set;

import javax.annotation.Resource

import org.junit.Test
import org.springframework.context.ApplicationContext
import org.springframework.context.support.ClassPathXmlApplicationContext

import edu.mayo.cts2.framework.core.config.PluginConfig
import edu.mayo.cts2.framework.core.config.option.Option;

class AbstractSpringServiceProviderTest {
	
	def provider = new TestAbstractSpringServiceProvider()
	
	@Test
	void "Test config not null"(){
		def config = new PluginConfig(null,null,null)
		
		provider.initialize(config)
		
		assertNotNull provider.getApplicationContext()
	}
	
	@Test
	void "Test config get PluginConfig bean"(){
		def config = new PluginConfig(null,null,null)
		
		def context = provider.buildParentApplicationContext(config)
		
		def returnedConfig = context.getBean("pluginConfig")
		
		assertNotNull returnedConfig
	}

	@Test
	void "Test Autowire PluginConfig bean"(){
		def config = new PluginConfig(null,null,null)
		
		def context = provider.buildParentApplicationContext(config)
		
		def returnedConfig = context.getBean("pluginConfig")
		
		assertNotNull returnedConfig
	}
	
	@Test
	void "Test Autowire bean"(){
		def config = new PluginConfig(null,null,null)
		
		provider.initialize(config)
		
		def context = provider.getApplicationContext()
	
		def returnedTestAutowire = context.getBean(TestAutowire.class)
		
		assertNotNull returnedTestAutowire.getPluginConfig()
	}
}

class TestAutowire {
	
	@Resource
	PluginConfig pluginConfig
	
}

class TestAbstractSpringServiceProvider extends AbstractSpringServiceProvider {

	@Override
	protected ApplicationContext getApplicationContext(ApplicationContext parent) {
		new ClassPathXmlApplicationContext(
			["edu/mayo/cts2/framework/service/provider/AbstractSpringServiceProviderTest.xml"] as String[],
			parent)
	}

	@Override
	protected ApplicationContext getIntegrationTestApplicationContext(
			ApplicationContext parent) {
			new ClassPathXmlApplicationContext(
			["edu/mayo/cts2/framework/service/provider/AbstractSpringServiceProviderTest.xml"] as String[],
			parent)
	}

	@Override
	public Set<Option> getPluginOptions() {
		return null;
	}
			
			
	
}
