package edu.mayo.cts2.framework.webapp.soap.wsdl;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.ws.transport.http.WsdlDefinitionHandlerAdapter;

import edu.mayo.cts2.framework.core.config.ServerContext;

public class LocationAdjustingWsdlDefinitionHandlerAdapter extends WsdlDefinitionHandlerAdapter {
	
	private static final String SOAP_URL_PREFIX = "/soap/service/";
	
	@Resource
	private ServerContext serverContext;
	
	@Override
	protected String transformLocation(String location, HttpServletRequest request) {
		return this.serverContext.getServerRootWithAppName() + 
				SOAP_URL_PREFIX +
				this.getServiceName(location);
	}
	
	private String getServiceName(String location){
		return StringUtils.substringAfterLast(location, "/");
	}

}
