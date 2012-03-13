package edu.mayo.cts2.framework.webapp.soap.endpoint.valuesetdefinition;

import edu.mayo.cts2.framework.model.core.FormatReference;
import edu.mayo.cts2.framework.model.service.core.ProfileElement;
import edu.mayo.cts2.framework.model.service.core.types.FunctionalProfile;
import edu.mayo.cts2.framework.model.service.core.types.ImplementationProfile;
import edu.mayo.cts2.framework.model.service.core.types.StructuralProfile;
import edu.mayo.cts2.framework.model.valuesetdefinition.ValueSetDefinition;
import edu.mayo.cts2.framework.model.wsdl.baseservice.GetDefaultFormat;
import edu.mayo.cts2.framework.model.wsdl.baseservice.GetDefaultFormatResponse;
import edu.mayo.cts2.framework.model.wsdl.baseservice.GetImplementationType;
import edu.mayo.cts2.framework.model.wsdl.baseservice.GetImplementationTypeResponse;
import edu.mayo.cts2.framework.model.wsdl.baseservice.GetKnownNamespace;
import edu.mayo.cts2.framework.model.wsdl.baseservice.GetKnownNamespaceResponse;
import edu.mayo.cts2.framework.model.wsdl.baseservice.GetServiceDescription;
import edu.mayo.cts2.framework.model.wsdl.baseservice.GetServiceDescriptionResponse;
import edu.mayo.cts2.framework.model.wsdl.baseservice.GetServiceName;
import edu.mayo.cts2.framework.model.wsdl.baseservice.GetServiceNameResponse;
import edu.mayo.cts2.framework.model.wsdl.baseservice.GetServiceProvider;
import edu.mayo.cts2.framework.model.wsdl.baseservice.GetServiceProviderResponse;
import edu.mayo.cts2.framework.model.wsdl.baseservice.GetServiceVersion;
import edu.mayo.cts2.framework.model.wsdl.baseservice.GetServiceVersionResponse;
import edu.mayo.cts2.framework.model.wsdl.baseservice.GetSupportedFormat;
import edu.mayo.cts2.framework.model.wsdl.baseservice.GetSupportedFormatResponse;
import edu.mayo.cts2.framework.model.wsdl.baseservice.GetSupportedProfile;
import edu.mayo.cts2.framework.model.wsdl.baseservice.GetSupportedProfileResponse;
import edu.mayo.cts2.framework.model.wsdl.valuesetdefinitionread.Exists;
import edu.mayo.cts2.framework.model.wsdl.valuesetdefinitionread.ExistsDefinitionForValueSet;
import edu.mayo.cts2.framework.model.wsdl.valuesetdefinitionread.ExistsDefinitionForValueSetResponse;
import edu.mayo.cts2.framework.model.wsdl.valuesetdefinitionread.ExistsResponse;
import edu.mayo.cts2.framework.model.wsdl.valuesetdefinitionread.Read;
import edu.mayo.cts2.framework.model.wsdl.valuesetdefinitionread.ReadDefinitionForValueSet;
import edu.mayo.cts2.framework.model.wsdl.valuesetdefinitionread.ReadDefinitionForValueSetResponse;
import edu.mayo.cts2.framework.model.wsdl.valuesetdefinitionread.ReadResponse;
import edu.mayo.cts2.framework.service.profile.valuesetdefinition.ValueSetDefinitionReadService;
import edu.mayo.cts2.framework.webapp.soap.endpoint.AbstractReadServiceEndpoint;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint("ValueSetDefinitionReadServicesEndpoint")
public class ValueSetDefinitionReadServicesEndpoint extends AbstractReadServiceEndpoint {

  @Cts2Service
  private ValueSetDefinitionReadService valueSetDefinitionReadService;

  /* TODO: implement read */
  @PayloadRoot(localPart = "read", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/ValueSetDefinitionReadServices")
  @ResponsePayload
  public ReadResponse read(@RequestPayload Read request) {
    throw new UnsupportedOperationException("Method not implemented");
//    ValueSetDefinition definition = this.doRead(
//        this.valueSetDefinitionReadService,
//        request.getValueSetDefinitionURI(),
//        request.getQueryControl(),
//        request.getContext());
//
//    ReadResponse response = new ReadResponse();
//    response.setReturn(definition);
//    return response;
  }

  @PayloadRoot(localPart = "readDefinitionForValueSet", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/ValueSetDefinitionReadServices")
  @ResponsePayload
  public ReadDefinitionForValueSetResponse readDefinitionForValueSet(@RequestPayload ReadDefinitionForValueSet request) {
    ValueSetDefinition definition =
        this.valueSetDefinitionReadService.readDefinitionForValueSet(
        request.getValueSet(),
        request.getTag(),
        request.getContext());

    ReadDefinitionForValueSetResponse response = new ReadDefinitionForValueSetResponse();
    response.setReturn(definition);
    return response;
  }

  /* TODO: implement exists */
  @PayloadRoot(localPart = "exists", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/ValueSetDefinitionReadServices")
  @ResponsePayload
  public ExistsResponse exists(@RequestPayload Exists request) {
    throw new UnsupportedOperationException("Method not implemented");
//    boolean exists = this.valueSetDefinitionReadService.exists(
//        request.getValueSetDefinitionURI(),
//        request.getContext());
//
//    ExistsResponse response = new ExistsResponse();
//    response.setReturn(exists);
//    return response;
  }

  @PayloadRoot(localPart = "existsDefinitionForValueSet", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/ValueSetDefinitionReadServices")
  @ResponsePayload
  public ExistsDefinitionForValueSetResponse existsDefinitionForValueSet(@RequestPayload ExistsDefinitionForValueSet request) {
    boolean exists = this.valueSetDefinitionReadService.existsDefinitionForValueSet(
        request.getValueSet(),
        request.getTag(),
        request.getContext());

    ExistsDefinitionForValueSetResponse response = new ExistsDefinitionForValueSetResponse();
    response.setReturn(exists);
    return response;
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
    response.setReturn(this.valueSetDefinitionReadService.getKnownNamespaceList());

    return response;
  }

  @PayloadRoot(localPart = "getServiceDescription", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/BaseServiceTypes")
  @ResponsePayload
  public GetServiceDescriptionResponse getServiceDescription(@RequestPayload GetServiceDescription request) {
    GetServiceDescriptionResponse response = new GetServiceDescriptionResponse();
    response.setReturn(this.valueSetDefinitionReadService.getServiceDescription());

    return response;
  }

  @PayloadRoot(localPart = "getServiceName", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/BaseServiceTypes")
  @ResponsePayload
  public GetServiceNameResponse getServiceName(@RequestPayload GetServiceName request) {
    GetServiceNameResponse response = new GetServiceNameResponse();
    response.setReturn(this.valueSetDefinitionReadService.getServiceName());

    return response;
  }

  @PayloadRoot(localPart = "getServiceProvider", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/BaseServiceTypes")
  @ResponsePayload
  public GetServiceProviderResponse getServiceProvider(@RequestPayload GetServiceProvider request) {
    GetServiceProviderResponse response = new GetServiceProviderResponse();
    response.setReturn(this.valueSetDefinitionReadService.getServiceProvider());

    return response;
  }

  @PayloadRoot(localPart = "getServiceVersion", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/BaseServiceTypes")
  @ResponsePayload
  public GetServiceVersionResponse getServiceVersion(@RequestPayload GetServiceVersion request) {
    GetServiceVersionResponse response = new GetServiceVersionResponse();
    response.setReturn(this.valueSetDefinitionReadService.getServiceVersion());

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
    profile.setStructuralProfile(StructuralProfile.SP_VALUE_SET_DEFINITION);

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
