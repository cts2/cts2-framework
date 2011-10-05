package edu.mayo.cts2.framework.core.config;

import java.io.File;

import edu.mayo.cts2.framework.core.config.option.OptionHolder;
import edu.mayo.cts2.framework.core.config.option.StringOption;

public class PluginConfig {

	private OptionHolder options;
	
	private File workDirectory;
	
	private ServerContext serverContext;

	protected PluginConfig(
			OptionHolder options, 
			File workDirectory,
			ServerContext serverContext){
		super();
		this.options = options;
		this.serverContext = serverContext;
	}

	public StringOption getStringOption(String optionName) {
		return options.getStringOption(optionName);
	}

	public File getWorkDirectory() {
		return workDirectory;
	}

	public ServerContext getServerContext() {
		return serverContext;
	}
}
