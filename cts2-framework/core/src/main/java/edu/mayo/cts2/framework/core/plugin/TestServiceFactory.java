package edu.mayo.cts2.framework.core.plugin;

import java.io.File;

import javax.annotation.Resource;

import org.osgi.framework.Bundle;
import org.osgi.framework.ServiceFactory;
import org.osgi.framework.ServiceRegistration;
import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.core.config.ServiceConfigManager;

@Component
public class TestServiceFactory implements ServiceFactory {

	@Resource
	private ServiceConfigManager serviceConfigManager;
	
	@Override
	public Object getService(Bundle arg0, ServiceRegistration arg1) {
		return new File(arg0.getSymbolicName());
	}

	@Override
	public void ungetService(Bundle arg0, ServiceRegistration arg1, Object arg2) {
		// TODO Auto-generated method stub
		
	}
	


}
