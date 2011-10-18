package edu.mayo.cts2.framework.core.config;

import static org.junit.Assert.*

import java.io.File

import org.easymock.EasyMock
import org.gmock.WithGMock
import org.junit.Test

import edu.mayo.cts2.framework.core.config.option.OptionHolder

@WithGMock
class PluginManagerTest {

	@Test
	void "Test getPluginClassLoader"(){
		def manager = new TestPluginManager();
		
		def classloader = manager.getPluginClassLoader("test", "test")
		
		assertNotNull classloader	
	}
	
	@Test
	void "Test getPluginClassLoader with same plugin"(){
		def manager = new TestPluginManager();
		
		def classloader1 = manager.getPluginClassLoader("test", "test")
		
		def classloader2 = manager.getPluginClassLoader("test", "test")
		
		assertTrue classloader1.equals(classloader2)
	}
	
	@Test
	void "Test getPluginClassLoader with different plugin"(){
		def manager = new TestPluginManager()
		
		def classloader1 = manager.getPluginClassLoader("test", "1")
		
		def classloader2 = manager.getPluginClassLoader("test", "2")
		
		assertFalse classloader1.equals(classloader2)
	}
	
	@Test
	void "Test work directory path"(){

		def manager = new PluginManager();
		
		ConfigInitializer init = EasyMock.createMock(ConfigInitializer)
		
		EasyMock.expect(init.getContextConfigDirectory()).andReturn(new File("test"))
		
		EasyMock.replay(init)
		
		manager.setConfigInitializer(init)
		
		def workDir = manager.getPluginWorkDirectory("test-plugin");
			
		assertEquals "test/.work/test-plugin", workDir.getPath()
	}
	
	@Test
	void "Test activatePlugin"(){
		def manager = new PluginManager();
		
		ServiceConfigManager scm = EasyMock.createMock(ServiceConfigManager)
		
		manager.serviceConfigManager = scm
		
		EasyMock.expect(scm.updateContextConfigProperties([
			"service.plugin.name" : "test",
			"service.plugin.version" : "testversion"
			
		])).once()
		
		EasyMock.replay(scm)
		
		manager.activatePlugin("test", "testversion")

	}
	
}

class TestPluginManager extends PluginManager {
	
	@Override
	protected ClassLoader  createClassLoaderForPlugin(String pluginName, String pluginVersion){
		new URLClassLoader();
	}
	
	@Override
	public OptionHolder getPluginSpecificConfigProperties(String pluginName){
		new OptionHolder(null);
	}
	
	protected File getPluginWorkDirectory(String pluginName){
		new File("file")
	}
	
}
