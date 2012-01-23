package edu.mayo.cts2.framework.core.plugin;

import java.util.Map;

import edu.mayo.cts2.framework.core.config.ServerContext;
import edu.mayo.cts2.framework.core.config.option.OptionHolder;

public interface PluginConfigManager {

	public PluginConfig getPluginConfig(String namespace);
	
	public void updatePluginConfigProperties(
			String namespace, Map<String, String> properties);

	public void updatePluginConfigProperty(String pluginName,
			String namespace, String propertyValue);

	public OptionHolder getPluginConfigProperties(
			String namespace);
	
	public ServerContext getServerContext();
}
