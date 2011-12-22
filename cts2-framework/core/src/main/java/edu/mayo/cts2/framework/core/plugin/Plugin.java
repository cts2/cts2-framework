package edu.mayo.cts2.framework.core.plugin;

import java.util.Set;

import edu.mayo.cts2.framework.core.config.option.Option;

public interface Plugin<T> {

	public void initialize(PluginConfig config);

	public void destroy();

	public Set<Option> getPluginOptions();
	
	public T getPlugin();

}
