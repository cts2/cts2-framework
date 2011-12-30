package edu.mayo.cts2.framework.core.plugin;

import java.util.Map;

import edu.mayo.cts2.framework.core.config.option.OptionHolder;

public interface PluginConfigManager {

	public PluginConfig getPluginConfig(String pluginName);
	

	public void updatePluginSpecificConfigProperties(
			String pluginName, Map<String, String> properties);

	public void updatePluginSpecificConfigProperty(String pluginName,
			String propertyName, String propertyValue);

	public OptionHolder getPluginSpecificConfigProperties(
			String pluginName);
}
