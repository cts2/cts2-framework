package edu.mayo.cts2.framework.core.plugin;

import java.util.Set;

import org.osgi.framework.BundleActivator;

import edu.mayo.cts2.framework.core.config.option.Option;

public interface Plugin extends BundleActivator {

	public void initialize(PluginConfig config);

	public void destroy();

	public Set<Option> getPluginOptions();
	
	public Iterable<PluginService<?>> getPluginServices();

}
