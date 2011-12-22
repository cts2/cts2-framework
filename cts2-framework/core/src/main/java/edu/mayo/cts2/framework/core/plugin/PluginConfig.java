package edu.mayo.cts2.framework.core.plugin;

import java.io.File;

import edu.mayo.cts2.framework.core.config.ServerContext;
import edu.mayo.cts2.framework.core.config.option.OptionHolder;
import edu.mayo.cts2.framework.core.config.option.StringOption;

public class PluginConfig {

	private OptionHolder options;
	
	private File workDirectory;
	
	private ServerContext serverContext;

	public PluginConfig(
			OptionHolder options, 
			File workDirectory,
			ServerContext serverContext){
		super();
		this.options = options;
		this.workDirectory = workDirectory;
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
