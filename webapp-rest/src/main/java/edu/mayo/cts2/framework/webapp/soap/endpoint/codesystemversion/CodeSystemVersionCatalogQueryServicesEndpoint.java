package edu.mayo.cts2.framework.webapp.soap.endpoint.codesystemversion;

import edu.mayo.cts2.framework.service.profile.codesystemversion.CodeSystemVersionQueryService;
import edu.mayo.cts2.framework.webapp.service.AbstractServiceAwareBean.Cts2Service;
import edu.mayo.cts2.framework.webapp.soap.endpoint.AbstractQueryServiceEndpoint;
import org.springframework.ws.server.endpoint.annotation.Endpoint;

@Endpoint("CodeSystemVersionCatalogQueryServicesEndpoint")
public class CodeSystemVersionCatalogQueryServicesEndpoint extends AbstractQueryServiceEndpoint {
  
  @Cts2Service
  private CodeSystemVersionQueryService codeSystemVersionQueryService;


  
}
