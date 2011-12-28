package edu.mayo.cts2.framework.core.plugin;

public class ServiceDescription extends PluginReference{

	private String _description;
	private boolean _isActive;

	public ServiceDescription(
			String name, 
			String version, 
			String description,
			boolean isActive) {
		super(name, version);
		this._description = description;
		this.setActive(isActive);
	}

	public String getDescription() {
		return _description;
	}
	
	public void setDescription(String description) {
		this._description = description;
	}

	public boolean isActive() {
		return _isActive;
	}

	public void setActive(boolean isActive) {
		this._isActive = isActive;
	}	
}
