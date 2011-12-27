package edu.mayo.cts2.framework.core.plugin;

import java.util.Dictionary;

public interface PluginService<T> {

	public T getService();

	public void updateProperties(Dictionary<String,?> newProperties);
	
	public void unregister();
	
}
