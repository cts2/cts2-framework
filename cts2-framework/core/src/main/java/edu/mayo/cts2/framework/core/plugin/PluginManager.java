package edu.mayo.cts2.framework.core.plugin;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

public interface PluginManager {


	public void removePlugin(String pluginName, String pluginVersion);

	public PluginDescription getPluginDescription(
			String pluginName,
			String pluginVersion);

	public Set<PluginDescription> getPluginDescriptions();

	public void activatePlugin(String name, String version);
	
	public void dectivatePlugin(String name, String version);
	
	public void installPlugin(InputStream source) throws IOException;

	public boolean isPluginActive(PluginReference ref);

	public void registerExtensionPoint(ExtensionPoint extensionPoint);

}