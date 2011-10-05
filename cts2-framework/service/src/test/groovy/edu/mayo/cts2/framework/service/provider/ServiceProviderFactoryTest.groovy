package edu.mayo.cts2.framework.service.provider;

import static org.junit.Assert.*

import org.junit.Test

import edu.mayo.cts2.framework.core.config.PluginConfig
import edu.mayo.cts2.framework.core.config.PluginManager
import edu.mayo.cts2.framework.core.config.PluginReference
import edu.mayo.cts2.framework.service.profile.Cts2Profile

class ServiceProviderFactoryTest {
	
	def classLoader = new URLClassLoader()

	def pluginManager = [
		getActivePlugin : { new PluginReference("test","test") },
		getPluginServiceProviderClassName : { pluginName,pluginVersion -> TestServiceProvider.class.getName() },
		createClassLoaderForPlugin : { pluginName,pluginVersion -> classLoader }
	] as PluginManager

	@Test
	void "Test service provider not null"(){
		def factory = new ServiceProviderFactory()
		factory.pluginManager = pluginManager
		
		def serviceProvider = factory.createServiceProvider();
		
		assertNotNull serviceProvider
	}
	
	@Test( expected = InfoException.class)
	void "Test service provider is right one"(){
		def factory = new ServiceProviderFactory()
		factory.pluginManager = pluginManager
		
		def serviceProvider = factory.createServiceProvider();

		def returnClazz = serviceProvider.getService(Cts2Profile.class)
	}
	
	@Test
	void "Test service provider has right class loader for getService"(){
		def factory = new ServiceProviderFactory()
		factory.pluginManager = pluginManager
		
		def serviceProvider = factory.createServiceProvider();

		try {
			serviceProvider.getService(Cts2Profile.class)
		} catch (e) {
			assertEquals e.classLoader, classLoader
			return
		}
		
		fail
	}
}

class InfoException extends RuntimeException {
	def classLoader	
}

class TestServiceProvider implements ServiceProvider {

	@Override
	public <T extends Cts2Profile> T getService(Class<T> serviceClass) {
		throw new InfoException(classLoader: Thread.currentThread().contextClassLoader)
	}

	@Override
	public void initialize(PluginConfig config) {
		//
	}

	@Override
	public void destroy() {
		//
	}

}
