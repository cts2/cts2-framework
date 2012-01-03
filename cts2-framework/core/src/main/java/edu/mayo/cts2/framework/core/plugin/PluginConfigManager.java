package edu.mayo.cts2.framework.core.plugin;

import java.util.Map;

import com.atlassian.plugin.spring.AvailableToPlugins;

import edu.mayo.cts2.framework.core.config.ServerContext;
import edu.mayo.cts2.framework.core.config.option.OptionHolder;

@AvailableToPlugins
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
