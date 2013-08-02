package edu.mayo.cts2.framework.core.config

import java.io.File;

import org.junit.Before;
import org.springframework.beans.factory.FactoryBean

import edu.mayo.cts2.framework.core.config.option.OptionHolder;

class TestConfigInitializerSpringFactory implements FactoryBean {
	
	ConfigInitializer initializer = null;
	
	def configDir
	def pluginsDir
	def contextDir

	@Override
	public Object getObject() throws Exception {
		setup();
		
		initializer
	}

	@Override
	public Class getObjectType() {
		ConfigInitializer.class
	}

	@Override
	public boolean isSingleton() {
		true
	}		
	
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
}