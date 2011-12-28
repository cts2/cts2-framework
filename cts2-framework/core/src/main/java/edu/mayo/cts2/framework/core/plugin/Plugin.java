package edu.mayo.cts2.framework.core.plugin;

import java.util.Set;

import org.osgi.framework.BundleActivator;
import org.osgi.service.cm.ManagedService;

import edu.mayo.cts2.framework.core.config.option.Option;

public interface Plugin extends BundleActivator, ManagedService {

	public void initialize(PluginConfig config);

	public void destroy();
	
	public void updatePluginOptions(Set<Option> newOptions);

	public Set<Option> getPluginOptions();
	
}
