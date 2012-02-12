package edu.mayo.cts2.framework.webapp.service


import org.springframework.stereotype.Component

import edu.mayo.cts2.framework.service.profile.Cts2Profile;
import edu.mayo.cts2.framework.service.provider.ServiceProvider
import edu.mayo.cts2.framework.webapp.service.AbstractServiceAwareBean.Cts2Service

@Component
class MockServiceProvider implements ServiceProvider {
	
	public static Cts2Profile cts2Service
	
	@Override
	public <T extends Cts2Profile> T getService(Class<T> serviceClass) {
		return cts2Service
	}



}
