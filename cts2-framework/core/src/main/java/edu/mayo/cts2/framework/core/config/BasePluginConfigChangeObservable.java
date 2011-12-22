package edu.mayo.cts2.framework.core.config;

import java.util.HashSet;
import java.util.Set;

import edu.mayo.cts2.framework.core.config.option.OptionHolder;
import edu.mayo.cts2.framework.core.plugin.PluginConfigChangeObservable;
import edu.mayo.cts2.framework.core.plugin.PluginConfigChangeObserver;
import edu.mayo.cts2.framework.core.plugin.PluginReference;

public class BasePluginConfigChangeObservable 
	implements PluginConfigChangeObservable {
	
	private Set<PluginConfigChangeObserver> observers = 
			new HashSet<PluginConfigChangeObserver>();

	protected void firePluginSpecificConfigPropertiesChangeEvent(OptionHolder newOptions){
		for(PluginConfigChangeObserver observer : this.observers) {
			observer.onPluginSpecificConfigPropertiesChange(newOptions);
		}
	}
	
	public void firePluginRemovedEvent(PluginReference plugin) {
		for(PluginConfigChangeObserver observer : this.observers) {
			observer.onPluginRemoved(plugin);
		}
	}
	
	protected void firePluginActivatedEvent(PluginReference plugin){
		for(PluginConfigChangeObserver observer : this.observers) {
			observer.onPluginActivated(plugin);
		}
	}
	
	public void firePluginAddedEvent(PluginReference plugin) {
		for(PluginConfigChangeObserver observer : this.observers) {
			observer.onPluginAdded(plugin);
		}
	}

	@Override
	public void registerListener(PluginConfigChangeObserver observer) {
		observers.add(observer);
	}

	@Override
	public void unregisterListener(PluginConfigChangeObserver observer) {
		this.observers.remove(observer);
	}
}
