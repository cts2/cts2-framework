package edu.mayo.cts2.framework.core.plugin;

import org.osgi.framework.BundleContext;

public interface OsgiPluginManager extends PluginManager {

	public BundleContext getBundleContext();
}
