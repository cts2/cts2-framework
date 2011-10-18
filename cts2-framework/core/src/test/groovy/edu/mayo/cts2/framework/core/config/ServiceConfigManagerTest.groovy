package edu.mayo.cts2.framework.core.config;

import static org.junit.Assert.*

import org.junit.Before
import org.junit.Test

class ServiceConfigManagerTest {

	ServiceConfigManager manager;
	
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
		
		manager = new ServiceConfigManager()
		
		manager.configInitializer = ConfigInitializer.instance()
		
		manager.afterPropertiesSet();
	}
	
	@Test()
	void "Test Setup"(){
		assertNotNull manager;
	}
	
	
	@Test
	void "Check context config default properties"(){
		def contextConfigFile = new File(configDir + File.separator +
			contextDir + File.separator + ConfigConstants.CONTEXT_PROPERTIES_FILE)
		
		def props = new Properties();
		props.load(new FileReader(contextConfigFile))
		
		def values = [
			"service.plugin.name", 
			"service.plugin.version", 
			"admin.username",
			"admin.password",
			"server.root",
			"app.name"]
		
		values.each { assertNotNull it+" is NULL", props.getProperty(it) }
	}
	
	@Test
	void "Check context config default property values"(){
		def contextConfigFile = new File(configDir + File.separator +
			contextDir + File.separator + ConfigConstants.CONTEXT_PROPERTIES_FILE)
		
		def props = new Properties();
		props.load(new FileReader(contextConfigFile))
		
		assertEquals "", props.getProperty("service.plugin.name")
		assertEquals "", props.getProperty("service.plugin.version")
		assertEquals "admin", props.getProperty("admin.username")
		assertEquals "admin", props.getProperty("admin.password")
		assertEquals "http://localhost:8080", props.getProperty("server.root")
		assertEquals "webapp-rest", props.getProperty("app.name")
	}


}
