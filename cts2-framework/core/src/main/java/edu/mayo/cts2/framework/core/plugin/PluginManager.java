package edu.mayo.cts2.framework.core.plugin;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Set;

import edu.mayo.cts2.framework.core.config.option.OptionHolder;

public interface PluginManager {

	public void updatePluginSpecificConfigProperties(
			String pluginName, Map<String, String> properties);

	public void updatePluginSpecificConfigProperty(String pluginName,
			String propertyName, String propertyValue);

	public OptionHolder getPluginSpecificConfigProperties(
			String pluginName);

	public void removePlugin(String pluginName, String pluginVersion);

	public <T extends Plugin> Iterable<T> getPlugins(Class<T> clazz);

	public PluginDescription getPluginDescription(
			String pluginName,
			String pluginVersion);

	public Set<PluginDescription> getPluginDescriptions();
	
	public Set<ServiceDescription> getServiceDescriptions();

	public void activatePlugin(String name, String version);
	
	public void dectivatePlugin(String name, String version);
	
	public void installPlugin(InputStream source) throws IOException;

	public PluginConfig getPluginConfig(String pluginName);

	public boolean isPluginActive(PluginReference ref);

}