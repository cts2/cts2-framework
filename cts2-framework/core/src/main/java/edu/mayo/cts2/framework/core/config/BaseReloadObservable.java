package edu.mayo.cts2.framework.core.config;

import java.util.HashSet;
import java.util.Set;

public class BaseReloadObservable implements ReloadObservable {
	
	private Set<ReloadObserver> observers = 
			new HashSet<ReloadObserver>();

	@Override
	public void registerListener(ReloadObserver observer) {
		this.observers.add(observer);
	}

	@Override
	public void unregisterListener(ReloadObserver observer) {
		this.observers.remove(observer);
	}
	
	protected void fireReloadEvent(){
		for(ReloadObserver observer : observers) {
			observer.onReload();
		}
	}
}
