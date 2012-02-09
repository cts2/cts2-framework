package edu.mayo.cts2.framework.webapp.soap.endpoint.codesystem;

import javax.annotation.Resource;

import org.osgi.framework.BundleContext;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.osgi.context.BundleContextAware;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import edu.mayo.cts2.framework.core.xml.Cts2Marshaller;
import edu.mayo.cts2.framework.model.codesystem.CodeSystemCatalogEntry;
import edu.mayo.cts2.framework.model.wsdl.codesystemcatalogread.Read;
import edu.mayo.cts2.framework.model.wsdl.codesystemcatalogread.ReadResponse;

@Endpoint
public class CodeSystemCatalogReadServicesEndpoint {

	@PayloadRoot(localPart = "read", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/CodeSystemCatalogReadServices")
	@ResponsePayload
	public ReadResponse getCS(@RequestPayload Read holidayRequest) {
		CodeSystemCatalogEntry e = new CodeSystemCatalogEntry();
		e.setAbout("asdfb");
		e.setCodeSystemName("asdf");
		ReadResponse response = new ReadResponse();
		response.setReturn(e);
		
		return response;
	}

}
