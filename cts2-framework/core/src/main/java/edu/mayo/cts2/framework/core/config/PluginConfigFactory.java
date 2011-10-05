package edu.mayo.cts2.framework.core.config;

import java.io.File;

import edu.mayo.cts2.framework.core.config.option.OptionHolder;

public class PluginConfigFactory {
	
	private static PluginConfigFactory instance;
	
	private PluginConfig pluginConfig;
	
	protected PluginConfigFactory(
			OptionHolder options, 
			File workDirectory, 
			ServerContext serverContext){
		instance = this;
		
		pluginConfig = new PluginConfig(
				options, 
				workDirectory,
				serverContext);
	}

	public static PluginConfigFactory instance(){
		if(instance == null){
			throw new IllegalStateException("The PluginConfigFactory has not been initialized.");
		}
		return instance;
	}

	public PluginConfig getPluginConfig() {
		return pluginConfig;
	}
}
