package edu.mayo.cts2.framework.webapp.soap.endpoint;

import org.springframework.beans.BeansException;
import org.springframework.ws.server.endpoint.mapping.PayloadRootAnnotationMethodEndpointMapping;

public class PerUrlPayloadRootAnnotationMethodEndpointMapping extends PayloadRootAnnotationMethodEndpointMapping {

	private String service;
	
	protected void initApplicationContext() throws BeansException {
		this.initInterceptors();
		
		this.registerMethods(service);
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}
}
