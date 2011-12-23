package edu.mayo.cts2.framework.core.plugin;

import java.util.Map;

public class PluginService<T> {
	
	private T service;
	private Map<String, ?> properties;

	public PluginService(T service, Map<String, ?> properties) {
		super();
		this.service = service;
		this.properties = properties;
	}

	public T getService() {
		return service;
	}
	
	public Map<String, ?> getProperties() {
		return properties;
	}
	
}
