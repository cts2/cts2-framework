package edu.mayo.cts2.framework.webapp.soap.endpoint.codesystem;

import edu.mayo.cts2.framework.model.service.core.DocumentedNamespaceReference;
import edu.mayo.cts2.framework.model.service.core.types.ImplementationProfile;
import edu.mayo.cts2.framework.model.wsdl.baseservice.*;
import edu.mayo.cts2.framework.model.wsdl.codesystemread.Exists;
import edu.mayo.cts2.framework.model.wsdl.codesystemread.ExistsResponse;
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

  @PayloadRoot(localPart = "exists", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/CodeSystemCatalogReadServices")
  @ResponsePayload
  public ExistsResponse exists(@RequestPayload Exists request) {
    boolean exists = this.codeSystemReadService.exists(request.getCodeSystemId(), request.getContext());

    ExistsResponse existsResponse = new ExistsResponse();
    existsResponse.setReturn(exists);

    return existsResponse;
  }

  @PayloadRoot(localPart = "getDefaultFormat", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/BaseServiceTypes")
  @ResponsePayload
  public GetDefaultFormatResponse getDefaultFormat(@RequestPayload GetDefaultFormat request) {
    GetDefaultFormatResponse response = new GetDefaultFormatResponse();
    response.setReturn(this.codeSystemReadService.getDefaultFormat());
    return response;
  }

  @PayloadRoot(localPart = "getImplementationType", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/BaseServiceTypes")
  @ResponsePayload
  public GetImplementationTypeResponse getImplementationType(@RequestPayload GetImplementationType request) {
    GetImplementationTypeResponse response = new GetImplementationTypeResponse();
    response.setReturn(0, ImplementationProfile.IP_SOAP);
    return response;
  }

  @PayloadRoot(localPart = "getKnownNamespace", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/BaseServiceTypes")
  @ResponsePayload
  public GetKnownNamespaceResponse getKnownNamespace(@RequestPayload GetKnownNamespace request) {
    GetKnownNamespaceResponse response = new GetKnownNamespaceResponse();
    response.setReturn(this.codeSystemReadService.getKnownNamespaceList());
    return response;
  }

  @PayloadRoot(localPart = "getServiceDescription", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/BaseServiceTypes")
  @ResponsePayload
  public GetServiceDescriptionResponse getServiceDescription(@RequestPayload GetServiceDescription request) {
    GetServiceDescriptionResponse response = new GetServiceDescriptionResponse();
    response.setReturn(this.codeSystemReadService.getServiceDescription());
    return response;
  }

  @PayloadRoot(localPart = "getServiceName", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/BaseServiceTypes")
  @ResponsePayload
  public GetServiceNameResponse getServiceName(@RequestPayload GetServiceName request) {
    GetServiceNameResponse response = new GetServiceNameResponse();
    response.setReturn(this.codeSystemReadService.getServiceName());
    return response;
  }

  @PayloadRoot(localPart = "getServiceProvider", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/BaseServiceTypes")
  @ResponsePayload
  public GetServiceProviderResponse getServiceProvider(@RequestPayload GetServiceProvider request) {
    GetServiceProviderResponse response = new GetServiceProviderResponse();
    response.setReturn(this.codeSystemReadService.getServiceProvider());
    return response;
  }

  @PayloadRoot(localPart = "getServiceVersion", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/BaseServiceTypes")
  @ResponsePayload
  public GetServiceVersionResponse getServiceVersion(@RequestPayload GetServiceVersion request) {
    GetServiceVersionResponse response = new GetServiceVersionResponse();
    response.setReturn(this.codeSystemReadService.getServiceVersion());
    return response;
  }

  @PayloadRoot(localPart = "getSupportedFormat", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/BaseServiceTypes")
  @ResponsePayload
  public GetSupportedFormatResponse getSupportedFormat(@RequestPayload GetSupportedFormat request) {
    GetSupportedFormatResponse response = new GetSupportedFormatResponse();
    response.setReturn(this.codeSystemReadService.getSupportedFormatList());
    return response;
  }

  @PayloadRoot(localPart = "getSupportedProfile", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/BaseServiceTypes")
  @ResponsePayload
  public GetSupportedProfileResponse getSupportedProfile(@RequestPayload GetSupportedProfile request) {
    throw new UnsupportedOperationException("Method not implemented");
  }

}
