package edu.mayo.cts2.framework.webapp.soap.endpoint.conceptdomainbinding;

import edu.mayo.cts2.framework.model.core.FormatReference;
import edu.mayo.cts2.framework.model.service.core.ProfileElement;
import edu.mayo.cts2.framework.model.service.core.types.FunctionalProfile;
import edu.mayo.cts2.framework.model.service.core.types.ImplementationProfile;
import edu.mayo.cts2.framework.model.service.core.types.StructuralProfile;
import edu.mayo.cts2.framework.model.wsdl.baseservice.*;
import edu.mayo.cts2.framework.model.wsdl.conceptdomainbindingread.*;
import edu.mayo.cts2.framework.service.profile.conceptdomainbinding.ConceptDomainBindingReadService;
import edu.mayo.cts2.framework.webapp.soap.endpoint.AbstractReadServiceEndpoint;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint("ConceptDomainBindingReadServicesEndpoint")
public class ConceptDomainBindingReadServicesEndpoint extends AbstractReadServiceEndpoint {

  @Cts2Service
  private ConceptDomainBindingReadService conceptDomainBindingReadService;

  /* TODO: Implement Method: read */
  @PayloadRoot(localPart = "read", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/ConceptDomainBindingReadServiceTypes")
  @ResponsePayload
  public ReadResponse read(@RequestPayload Read request) {
    throw new UnsupportedOperationException("Method not implemented");
  }

  /* TODO: Implement Method: exists */
  @PayloadRoot(localPart = "exists", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/ConceptDomainBindingReadServiceTypes")
  @ResponsePayload
  public ExistsResponse exists(@RequestPayload Exists request) {
    throw new UnsupportedOperationException("Method not implemented");
  }

  /* TODO: Implement Method: readByURI */
  @PayloadRoot(localPart = "readByURI", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/ConceptDomainBindingReadServiceTypes")
  @ResponsePayload
  public ReadByURIResponse readByURI(@RequestPayload ReadByURI request) {
    throw new UnsupportedOperationException("Method not implemented");
  }

  /* TODO: Implement Method: existsURI */
  @PayloadRoot(localPart = "existsURI", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/ConceptDomainBindingReadServiceTypes")
  @ResponsePayload
  public ExistsURIResponse existsURI(@RequestPayload ExistsURI request) {
//    ExistsResponse response = new ExistsResponse();
//    response.setReturn(this.conceptDomainBindingReadService.);
    throw new UnsupportedOperationException("Method not implemented");
  }

  /* TODO: Implement Method: getSupportedTag */
  @PayloadRoot(localPart = "getSupportedTag", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/ConceptDomainBindingReadServiceTypes")
  @ResponsePayload
  public GetSupportedTagResponse getSupportedTag(@RequestPayload GetSupportedTag request) {
//    GetSupportedTagResponse response = new GetSupportedTagResponse();
//    response.setReturn(conceptDomainBindingReadService.exists());
//    return response;
    throw new UnsupportedOperationException("Method not implemented");
  }

  /*******************************************************/
  /*                   BaseServices                      */
  /*******************************************************/
  @PayloadRoot(localPart = "getDefaultFormat", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/BaseServiceTypes")
  @ResponsePayload
  public GetDefaultFormatResponse getDefaultFormat(@RequestPayload GetDefaultFormat request) {
    FormatReference format = new FormatReference("SOAP");

    GetDefaultFormatResponse response = new GetDefaultFormatResponse();
    response.setReturn(format);

    return response;
  }

  @PayloadRoot(localPart = "getImplementationType", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/BaseServiceTypes")
  @ResponsePayload
  public GetImplementationTypeResponse getImplementationType(@RequestPayload GetImplementationType request) {
    ImplementationProfile implementations[] = new ImplementationProfile[1];
    implementations[0] = ImplementationProfile.IP_SOAP;

    GetImplementationTypeResponse response = new GetImplementationTypeResponse();
    response.setReturn(implementations);

    return response;
  }

  @PayloadRoot(localPart = "getKnownNamespace", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/BaseServiceTypes")
  @ResponsePayload
  public GetKnownNamespaceResponse getKnownNamespace(@RequestPayload GetKnownNamespace request) {
    GetKnownNamespaceResponse response = new GetKnownNamespaceResponse();
    response.setReturn(this.conceptDomainBindingReadService.getKnownNamespaceList());

    return response;
  }

  @PayloadRoot(localPart = "getServiceDescription", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/BaseServiceTypes")
  @ResponsePayload
  public GetServiceDescriptionResponse getServiceDescription(@RequestPayload GetServiceDescription request) {
    GetServiceDescriptionResponse response = new GetServiceDescriptionResponse();
    response.setReturn(this.conceptDomainBindingReadService.getServiceDescription());

    return response;
  }

  @PayloadRoot(localPart = "getServiceName", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/BaseServiceTypes")
  @ResponsePayload
  public GetServiceNameResponse getServiceName(@RequestPayload GetServiceName request) {
    GetServiceNameResponse response = new GetServiceNameResponse();
    response.setReturn(this.conceptDomainBindingReadService.getServiceName());

    return response;
  }

  @PayloadRoot(localPart = "getServiceProvider", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/BaseServiceTypes")
  @ResponsePayload
  public GetServiceProviderResponse getServiceProvider(@RequestPayload GetServiceProvider request) {
    GetServiceProviderResponse response = new GetServiceProviderResponse();
    response.setReturn(this.conceptDomainBindingReadService.getServiceProvider());

    return response;
  }

  @PayloadRoot(localPart = "getServiceVersion", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/BaseServiceTypes")
  @ResponsePayload
  public GetServiceVersionResponse getServiceVersion(@RequestPayload GetServiceVersion request) {
    GetServiceVersionResponse response = new GetServiceVersionResponse();
    response.setReturn(this.conceptDomainBindingReadService.getServiceVersion());

    return response;
  }

  @PayloadRoot(localPart = "getSupportedFormat", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/BaseServiceTypes")
  @ResponsePayload
  public GetSupportedFormatResponse getSupportedFormat(@RequestPayload GetSupportedFormat request) {
    FormatReference format[] = new FormatReference[1];
    format[0] = new FormatReference("SOAP");

    GetSupportedFormatResponse response = new GetSupportedFormatResponse();
    response.setReturn(format);

    return response;
  }

  @PayloadRoot(localPart = "getSupportedProfile", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/BaseServiceTypes")
  @ResponsePayload
  public GetSupportedProfileResponse getSupportedProfile(@RequestPayload GetSupportedProfile request) {
    ProfileElement profile = new ProfileElement();
    profile.setStructuralProfile(StructuralProfile.SP_CODE_SYSTEM);

    FunctionalProfile functionalProfiles[] = new FunctionalProfile[1];
    functionalProfiles[0] = FunctionalProfile.FP_READ;
    profile.setFunctionalProfile(functionalProfiles);

    ProfileElement profiles[] = new ProfileElement[1];
    profiles[0] = profile;

    GetSupportedProfileResponse response = new GetSupportedProfileResponse();
    response.setReturn(profiles);

    return response;
  }

}
