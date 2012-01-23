package edu.mayo.cts2.framework.core.plugin;

import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

public interface ExtensionPoint {
	
	public Class<?> getServiceClass();
	
	public void setServiceTracker(ServiceTracker serviceTracker);
	
	public ServiceTrackerCustomizer addServiceTrackerCustomizer();

}
