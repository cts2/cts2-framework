package edu.mayo.cts2.framework.webapp.service


import org.apache.commons.lang.ClassUtils
import org.springframework.stereotype.Component

import edu.mayo.cts2.framework.service.profile.Cts2Profile;
import edu.mayo.cts2.framework.service.provider.ServiceProvider

@Component
class MockServiceProvider implements ServiceProvider {
	
	public static Cts2Profile cts2Service
	
	public static List<Cts2Profile> cts2Services = []; 
	
	@Override
	public <T extends Cts2Profile> T getService(Class<T> serviceClass) {
		if(ClassUtils.isAssignable(cts2Service.getClass(), serviceClass)){
			return cts2Service
		}
		
		def returnSvc = null
		
		cts2Services.each {
			if( ClassUtils.isAssignable(it.getClass(), serviceClass) ){
				returnSvc = it
			}
		}
		
		return returnSvc
	}

}
