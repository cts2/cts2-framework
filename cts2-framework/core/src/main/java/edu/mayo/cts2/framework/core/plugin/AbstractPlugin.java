package edu.mayo.cts2.framework.core.plugin;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.util.tracker.ServiceTracker;

public abstract class AbstractPlugin<T> implements Plugin<T>, BundleActivator {

	@Override
	public void start(BundleContext context) throws Exception {
		BundleContext bundleContext = FrameworkUtil.getBundle(this.getClass())
                .getBundleContext();
		
		ServiceTracker tracker = 
				new ServiceTracker(bundleContext, bundleContext.getServiceReference(PluginConfig.class.getName()), null);
		
		PluginConfig config = (PluginConfig)tracker.getService();
		
		this.initialize(config);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		this.destroy();
	}

}
