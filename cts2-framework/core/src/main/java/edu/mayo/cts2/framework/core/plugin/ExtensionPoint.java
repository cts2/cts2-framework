package edu.mayo.cts2.framework.core.plugin;

import org.osgi.util.tracker.ServiceTracker;

public interface ExtensionPoint {
	
	public Class<?> getServiceClass();
	
	public void setServiceTracker(ServiceTracker serviceTracker);

}
