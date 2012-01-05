package edu.mayo.cts2.framework.core.plugin;

import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.atlassian.plugin.Plugin;
import com.atlassian.plugin.PluginArtifact;
import com.atlassian.plugin.PluginController;
import com.atlassian.plugin.PluginException;
import com.atlassian.plugin.PluginParseException;
import com.atlassian.plugin.spring.AvailableToPlugins;

@Component
@AvailableToPlugins
public class AtlassianExportedPluginController implements PluginController {

	@Resource
	private AtlassianPluginManager atlassianPluginManager;
	
	@Override
	public void enablePlugin(String key) {
		this.atlassianPluginManager.plugins.getPluginController().enablePlugin(key);
	}

	@Override
	public void enablePlugins(String... keys) {
		this.atlassianPluginManager.plugins.getPluginController().enablePlugins(keys);
	}

	@Override
	public void disablePlugin(String key) {
		this.atlassianPluginManager.plugins.getPluginController().disablePlugin(key);
	}

	@Override
	public void disablePluginWithoutPersisting(String key) {
		this.atlassianPluginManager.plugins.getPluginController().disablePluginWithoutPersisting(key);
	}

	@Override
	public void enablePluginModule(String completeKey) {
		this.atlassianPluginManager.plugins.getPluginController().enablePluginModule(completeKey);
	}

	@Override
	public void disablePluginModule(String completeKey) {
		this.atlassianPluginManager.plugins.getPluginController().disablePluginModule(completeKey);
	}

	@Override
	public String installPlugin(PluginArtifact pluginArtifact)
			throws PluginParseException {
		return 
				this.atlassianPluginManager.plugins.getPluginController().installPlugin(pluginArtifact);
	}

	@Override
	public Set<String> installPlugins(PluginArtifact... pluginArtifacts)
			throws PluginParseException {
		return 
				this.atlassianPluginManager.plugins.getPluginController().installPlugins(pluginArtifacts);
	}

	@Override
	public void uninstall(Plugin plugin) throws PluginException {
		this.atlassianPluginManager.plugins.getPluginController().uninstall(plugin);
	}

	@Override
	public void revertRestartRequiredChange(String pluginKey)
			throws PluginException {
		this.atlassianPluginManager.plugins.getPluginController().revertRestartRequiredChange(pluginKey);
	}

	@Override
	public int scanForNewPlugins() throws PluginParseException {
		return this.atlassianPluginManager.plugins.getPluginController().scanForNewPlugins();
	}

}
