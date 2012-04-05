package edu.mayo.cts2.framework.core.plugin;

import java.io.File;

import edu.mayo.cts2.framework.core.config.ServerContext;

public interface PluginConfigManager {

	public File getPluginWorkDirectory(String namespace);

	public ServerContext getServerContext();
}
