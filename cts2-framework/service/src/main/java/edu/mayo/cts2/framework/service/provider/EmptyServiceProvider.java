package edu.mayo.cts2.framework.service.provider;

import java.util.Set;

import edu.mayo.cts2.framework.core.config.option.Option;
import edu.mayo.cts2.framework.core.plugin.PluginConfig;
import edu.mayo.cts2.framework.service.profile.Cts2Profile;

public class EmptyServiceProvider implements ServiceProvider {

	public <T extends Cts2Profile> T getService(Class<T> serviceClass) {
		return null;
	}

	public void initialize(PluginConfig config) {
		//
	}

	public void destroy() {
		//
	}

	@Override
	public Set<Option> getPluginOptions() {
		//
		return null;
	}
		
}
