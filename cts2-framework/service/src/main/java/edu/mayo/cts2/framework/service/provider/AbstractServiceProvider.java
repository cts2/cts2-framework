package edu.mayo.cts2.framework.service.provider;

import edu.mayo.cts2.framework.core.plugin.AbstractPlugin;

public abstract class AbstractServiceProvider extends AbstractPlugin implements ServiceProvider {
	
	protected Class<?> getPluginServiceInterface(){
		return ServiceProvider.class;
	}

}
