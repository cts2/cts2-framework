package edu.mayo.cts2.framework.service.provider;


public abstract class AbstractServiceProvider implements ServiceProvider {
	
	protected Class<?> getPluginServiceInterface(){
		return ServiceProvider.class;
	}

}
