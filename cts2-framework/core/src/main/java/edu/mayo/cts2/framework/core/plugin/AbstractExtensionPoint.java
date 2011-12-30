package edu.mayo.cts2.framework.core.plugin;

import com.atlassian.plugin.ModuleDescriptor;
import com.atlassian.plugin.main.AtlassianPlugins;
import com.atlassian.plugin.tracker.DefaultPluginModuleTracker;
import com.atlassian.plugin.tracker.PluginModuleTracker;

public abstract class AbstractExtensionPoint<T> implements ExtensionPoint<T> {
	
	private PluginModuleTracker<T,ModuleDescriptor<T>> tracker;
	
	public T getService(){
		return tracker.getModules().iterator().next();
	}
	
	public void init(AtlassianPlugins plugins){
		@SuppressWarnings("unchecked")
		PluginModuleTracker<T,ModuleDescriptor<T>> tracker = 
				new DefaultPluginModuleTracker<T,ModuleDescriptor<T>>(
						plugins.getPluginAccessor(),
						plugins.getPluginEventManager(),
						(Class<ModuleDescriptor<T>>) 
						this.getClass().getAnnotation(ExtensionPointDescriptor.class).descriptor());
		
		this.tracker = tracker;
	}
}