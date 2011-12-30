package edu.mayo.cts2.framework.core.plugin;

import com.atlassian.plugin.main.AtlassianPlugins;

public interface ExtensionPoint<T> {
	
	public T getService();
	
	public void init(AtlassianPlugins plugins);

}
