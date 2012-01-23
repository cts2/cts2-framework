package edu.mayo.cts2.framework.core.plugin;

import org.osgi.util.tracker.ServiceTracker;

public abstract class AbstractExtensionPoint<T> implements ExtensionPoint {
	
	private ServiceTracker serviceTracker;

	@Override
	public void setServiceTracker(ServiceTracker serviceTracker) {
		this.serviceTracker = serviceTracker;
	}

	protected ServiceTracker getServiceTracker() {
		return serviceTracker;
	}

}