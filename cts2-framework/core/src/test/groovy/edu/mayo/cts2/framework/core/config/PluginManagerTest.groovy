package edu.mayo.cts2.framework.core.config;

import static org.junit.Assert.*

import java.io.File

import org.apache.tools.ant.AntClassLoader
import org.junit.Test

import edu.mayo.cts2.framework.core.config.option.OptionHolder

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
		def manager = new TestPluginManager();
		
		def classloader1 = manager.getPluginClassLoader("test", "1")
		
		def classloader2 = manager.getPluginClassLoader("test", "2")
		
		assertFalse classloader1.equals(classloader2)
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
