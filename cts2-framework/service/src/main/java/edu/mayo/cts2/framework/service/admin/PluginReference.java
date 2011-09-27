package edu.mayo.cts2.framework.service.admin;

public class PluginReference {

	private String _pluginName;
	private String _pluginVersion;

	public String getPluginName() {
		return _pluginName;
	}

	public void setPluginName(String pluginName) {
		this._pluginName = pluginName;
	}

	public String getPluginVersion() {
		return _pluginVersion;
	}

	public void setPluginVersion(String pluginVersion) {
		this._pluginVersion = pluginVersion;
	}
}
