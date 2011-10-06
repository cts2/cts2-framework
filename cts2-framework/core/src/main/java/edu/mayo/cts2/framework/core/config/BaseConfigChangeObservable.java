package edu.mayo.cts2.framework.core.config;

import java.util.HashSet;
import java.util.Set;

import edu.mayo.cts2.framework.core.config.option.OptionHolder;

public class BaseConfigChangeObservable implements ConfigChangeObservable {
	
	private Set<ConfigChangeObserver> observers = 
			new HashSet<ConfigChangeObserver>();

	@Override
	public void registerListener(ConfigChangeObserver observer) {
		this.observers.add(observer);
	}

	@Override
	public void unregisterListener(ConfigChangeObserver observer) {
		this.observers.remove(observer);
	}
	
	protected void fireContextConfigPropertiesChangeEvent(OptionHolder newOptions){
		for(ConfigChangeObserver observer : observers) {
			observer.onContextConfigPropertiesChange(newOptions);
		}
	}
	
	protected void fireGlobalConfigPropertiesChangeEvent(OptionHolder newOptions){
		for(ConfigChangeObserver observer : observers) {
			observer.onGlobalConfigPropertiesChange(newOptions);
		}
	}
	
	protected void firePluginSpecificConfigPropertiesChangeEvent(OptionHolder newOptions){
		for(ConfigChangeObserver observer : observers) {
			observer.onPluginSpecificConfigPropertiesChange(newOptions);
		}
	}
	
	public void firePluginRemovedEvent(PluginReference plugin) {
		for(ConfigChangeObserver observer : observers) {
			observer.onPluginRemoved(plugin);
		}
	}
	
	public void firePluginAddedEvent(PluginReference plugin) {
		for(ConfigChangeObserver observer : observers) {
			observer.onPluginAdded(plugin);
		}
	}
}
