package edu.mayo.cts2.framework.core.config;

import java.util.HashSet;
import java.util.Set;

import edu.mayo.cts2.framework.core.config.option.OptionHolder;

public class BaseServiceConfigChangeObservable {
	
	private Set<ConfigChangeObserver> observers = 
			new HashSet<ConfigChangeObserver>();

	public void registerListener(ConfigChangeObserver observer) {
		this.observers.add(observer);
	}

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

}
