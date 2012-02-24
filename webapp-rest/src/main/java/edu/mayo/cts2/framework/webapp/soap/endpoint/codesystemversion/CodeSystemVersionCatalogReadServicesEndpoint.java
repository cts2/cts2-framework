package edu.mayo.cts2.framework.webapp.soap.endpoint.codesystemversion;

import edu.mayo.cts2.framework.model.codesystemversion.CodeSystemVersionCatalogEntry;
import edu.mayo.cts2.framework.model.wsdl.baseservice.*;
import edu.mayo.cts2.framework.model.wsdl.codesystemversionread.*;
import edu.mayo.cts2.framework.service.profile.codesystemversion.CodeSystemVersionReadService;
import edu.mayo.cts2.framework.webapp.service.AbstractServiceAwareBean;
import edu.mayo.cts2.framework.webapp.soap.endpoint.AbstractReadServiceEndpoint;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint("CodeSystemVersionCatalogReadServicesEndpoint")
public class CodeSystemVersionCatalogReadServicesEndpoint extends AbstractReadServiceEndpoint {

  @AbstractServiceAwareBean.Cts2Service
  private CodeSystemVersionReadService codeSystemVersionReadService;

  @PayloadRoot(localPart = "read", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/CodeSystemVersionCatalogReadServices")
  @ResponsePayload
  public ReadResponse read(@RequestPayload Read request) {
    CodeSystemVersionCatalogEntry entry = this.doRead(
        this.codeSystemVersionReadService,
        request.getCodeSystemVersion(),
        request.getQueryControl(),
        request.getContext());

    ReadResponse readResponse = new ReadResponse();
    readResponse.setReturn(entry);

    return readResponse;
  }

  @PayloadRoot(localPart = "exists", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/CodeSystemVersionCatalogReadServices")
  @ResponsePayload
  public ExistsResponse exists(@RequestPayload Exists request) {
    boolean exists = this.codeSystemVersionReadService.exists(
        request.getCodeSystemVersion(),
        request.getContext());
    ExistsResponse existsResponse = new ExistsResponse();
    existsResponse.setReturn(exists);
    return existsResponse;
  }

  @PayloadRoot(localPart = "existsCodeSystemVersionForCodeSystem", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/CodeSystemVersionCatalogReadServices")
  @ResponsePayload
  public ExistsCodeSystemVersionForCodeSystemResponse existsCodeSystemVersionForCodeSystem(
      @RequestPayload ExistsCodeSystemVersionForCodeSystem request) {
    boolean exists = this.codeSystemVersionReadService.existsCodeSystemVersionForCodeSystem(
        request.getCodeSystem(),
        request.getTag().getName());
    ExistsCodeSystemVersionForCodeSystemResponse response = new ExistsCodeSystemVersionForCodeSystemResponse();
    response.setReturn(exists);
    return response;
  }

  @PayloadRoot(localPart = "getCodeSystemVersionForCodeSystem", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/CodeSystemVersionCatalogReadServices")
  @ResponsePayload
  public GetCodeSystemVersionForCodeSystemResponse getCodeSystemVersionForCodeSystem(
      @RequestPayload GetCodeSystemVersionForCodeSystem request) {
    GetCodeSystemVersionForCodeSystemResponse response = new GetCodeSystemVersionForCodeSystemResponse();
    response.setReturn(this.codeSystemVersionReadService.getCodeSystemVersionForCodeSystem(
        request.getCodeSystem(),
        request.getTag().getName()));
    return response;
  }

  /****************/
  /* BaseServices */
  /****************/
  @PayloadRoot(localPart = "getDefaultFormat", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/BaseServiceTypes")
  @ResponsePayload
  public GetDefaultFormatResponse getDefaultFormat(@RequestPayload GetDefaultFormat request) {
    GetDefaultFormatResponse response = new GetDefaultFormatResponse();
    response.setReturn(this.codeSystemVersionReadService.getDefaultFormat());
    return response;
  }

  @PayloadRoot(localPart = "getImplementationType", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/BaseServiceTypes")
  @ResponsePayload
  public GetImplementationTypeResponse getImplementationType(@RequestPayload GetImplementationType request) {
    GetImplementationTypeResponse response = new GetImplementationTypeResponse();
//    response.setReturn(ImplementationProfile.IP_SOAP);
    return response;
  }

  @PayloadRoot(localPart = "getKnownNamespace", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/BaseServiceTypes")
  @ResponsePayload
  public GetKnownNamespaceResponse getKnownNamespace(@RequestPayload GetKnownNamespace request) {
    GetKnownNamespaceResponse response = new GetKnownNamespaceResponse();
    response.setReturn(this.codeSystemVersionReadService.getKnownNamespaceList());
    return response;
  }

  @PayloadRoot(localPart = "getServiceDescription", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/BaseServiceTypes")
  @ResponsePayload
  public GetServiceDescriptionResponse getServiceDescription(@RequestPayload GetServiceDescription request) {
    GetServiceDescriptionResponse response = new GetServiceDescriptionResponse();
    response.setReturn(this.codeSystemVersionReadService.getServiceDescription());
    return response;
  }

  @PayloadRoot(localPart = "getServiceName", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/BaseServiceTypes")
  @ResponsePayload
  public GetServiceNameResponse getServiceName(@RequestPayload GetServiceName request) {
    GetServiceNameResponse response = new GetServiceNameResponse();
    response.setReturn(this.codeSystemVersionReadService.getServiceName());
    return response;
  }

  @PayloadRoot(localPart = "getServiceProvider", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/BaseServiceTypes")
  @ResponsePayload
  public GetServiceProviderResponse getServiceProvider(@RequestPayload GetServiceProvider request) {
    GetServiceProviderResponse response = new GetServiceProviderResponse();
    response.setReturn(this.codeSystemVersionReadService.getServiceProvider());
    return response;
  }

  @PayloadRoot(localPart = "getServiceVersion", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/BaseServiceTypes")
  @ResponsePayload
  public GetServiceVersionResponse getServiceVersion(@RequestPayload GetServiceVersion request) {
    GetServiceVersionResponse response = new GetServiceVersionResponse();
    response.setReturn(this.codeSystemVersionReadService.getServiceVersion());
    return response;
  }

  @PayloadRoot(localPart = "getSupportedFormat", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/BaseServiceTypes")
  @ResponsePayload
  public GetSupportedFormatResponse getSupportedFormat(@RequestPayload GetSupportedFormat request) {
    GetSupportedFormatResponse response = new GetSupportedFormatResponse();
    response.setReturn(this.codeSystemVersionReadService.getSupportedFormatList());
    return response;
  }

  @PayloadRoot(localPart = "getSupportedProfile", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/BaseServiceTypes")
  @ResponsePayload
  public GetSupportedProfileResponse getSupportedProfile(@RequestPayload GetSupportedProfile request) {
    GetSupportedProfileResponse response = new GetSupportedProfileResponse();
//    response.setReturn(this.baseService.getSupportedProfile());
    return response;
  }

}
