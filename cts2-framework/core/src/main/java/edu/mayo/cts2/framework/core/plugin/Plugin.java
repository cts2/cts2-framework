package edu.mayo.cts2.framework.core.plugin;

import java.util.Set;

import edu.mayo.cts2.framework.core.config.option.Option;

public interface Plugin  {

	public void initialize(PluginConfig config);

	public void destroy();
	
	public void updatePluginOptions(Set<Option> newOptions);

	public Set<Option> getPluginOptions();
	
}
