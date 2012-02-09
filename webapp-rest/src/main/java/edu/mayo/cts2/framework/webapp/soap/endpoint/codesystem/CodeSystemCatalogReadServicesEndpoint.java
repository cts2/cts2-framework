package edu.mayo.cts2.framework.webapp.soap.endpoint.codesystem;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import edu.mayo.cts2.framework.model.codesystem.CodeSystemCatalogEntry;
import edu.mayo.cts2.framework.model.wsdl.codesystemread.Read;
import edu.mayo.cts2.framework.model.wsdl.codesystemread.ReadResponse;
import edu.mayo.cts2.framework.service.profile.codesystem.CodeSystemReadService;
import edu.mayo.cts2.framework.webapp.soap.endpoint.AbstractReadServiceEndpoint;

@Endpoint("CodeSystemCatalogReadServicesEndpoint")
public class CodeSystemCatalogReadServicesEndpoint extends AbstractReadServiceEndpoint {
	
	@Cts2Service 
	private CodeSystemReadService codeSystemReadService;

	@PayloadRoot(localPart = "read", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/CodeSystemCatalogReadServices")
	@ResponsePayload
	public ReadResponse read(@RequestPayload Read request) {
		CodeSystemCatalogEntry entry = this.doRead(
				this.codeSystemReadService,
				request.getCodeSystemId(), 
				request.getQueryControl(), 
				request.getContext());
		
		ReadResponse readResponse = new ReadResponse();
		readResponse.setReturn(entry);
		
		return readResponse;
		
	}

}
