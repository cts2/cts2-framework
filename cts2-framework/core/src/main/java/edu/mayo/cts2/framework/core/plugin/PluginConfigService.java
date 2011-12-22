package edu.mayo.cts2.framework.core.plugin;

import javax.annotation.Resource;

import org.osgi.framework.Bundle;
import org.osgi.framework.ServiceFactory;
import org.osgi.framework.ServiceRegistration;
import org.springframework.stereotype.Component;

@Component
public class PluginConfigService implements ServiceFactory {

	@Resource
	private PluginManager pluginManager;
	
	@Override
	public PluginConfig getService(Bundle bundle, ServiceRegistration registration) {
		return pluginManager.getPluginConfig(bundle.getSymbolicName());
	}

	@Override
	public void ungetService(Bundle bundle, ServiceRegistration registration, Object obj) {
		// TODO Auto-generated method stub
		
	}
	
}
