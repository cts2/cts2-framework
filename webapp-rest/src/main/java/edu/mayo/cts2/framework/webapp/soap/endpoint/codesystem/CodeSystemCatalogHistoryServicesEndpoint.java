package edu.mayo.cts2.framework.webapp.soap.endpoint.codesystem;

import edu.mayo.cts2.framework.service.profile.codesystem.CodeSystemHistoryService;
import edu.mayo.cts2.framework.webapp.service.AbstractServiceAwareBean;
import edu.mayo.cts2.framework.webapp.soap.endpoint.AbstractHistoryServiceEndpoint;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint("CodeSystemCatalogHistoryServiceEndpoint")
public class CodeSystemCatalogHistoryServicesEndpoint extends AbstractHistoryServiceEndpoint {

  @AbstractServiceAwareBean.Cts2Service
  private CodeSystemHistoryService codeSystemHistoryService;

  @PayloadRoot(localPart = "getEarliestChange", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/CodeSystemCatalogHistoryServices")
  @ResponsePayload
  public GetEarliestChangeResponse getEarliestChange(@RequestPayload GetEarliestChange request) {

  }

}
