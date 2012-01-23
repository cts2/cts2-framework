package edu.mayo.cts2.framework.core.config;

import static org.junit.Assert.*

import org.junit.Before
import org.junit.Test


class ConfigInitializerTest {
	
	ConfigInitializer initializer = null;
	
	def configDir
	def pluginsDir
	def contextDir
	
	@Before
	void setup(){
		def tmpDir = System.getProperty("java.io.tmpdir")
		
		configDir = tmpDir + File.separator + UUID.randomUUID().toString()
		pluginsDir = configDir + File.separator + "plugins"

		System.setProperty(ConfigConstants.CTS2_CONFIG_DIRECTORY_ENV_VARIABLE,
			configDir)
		
		System.setProperty(ConfigConstants.CTS2_PLUGINS_DIRECTORY_ENV_VARIABLE,
			pluginsDir)
		
		new File(configDir).delete()
		new File(configDir).deleteDir()
		new File(configDir).deleteOnExit()
		
		new File(pluginsDir).delete()
		new File(pluginsDir).deleteDir()
		new File(pluginsDir).deleteOnExit()
		
		contextDir = UUID.randomUUID().toString()
		
		ConfigInitializer.instance = null
		
		ConfigInitializer.initialize(contextDir)
		
		initializer = ConfigInitializer.instance()
	}
	
	@Test
	void testInit(){
		assertNotNull initializer
	}
	
	@Test
	void "Check config dir created"(){
		assertTrue new File(configDir).exists()
	}
	
	@Test
	void "Check plugins dir created"(){
		assertTrue new File(pluginsDir).exists()
	}
	
	@Test
	void "Check context dir created"(){
		assertTrue new File(configDir + File.separator + contextDir).exists()
	}
	
	@Test
	void "Check global config props file created"(){
		assertTrue new File(configDir + File.separator + "global.properties").exists()
	}
	
	@Test
	void "Check context config props file created"(){
		assertTrue new File(configDir + File.separator + 
			contextDir + File.separator + ConfigConstants.CONTEXT_PROPERTIES_FILE).exists()
	}
}
