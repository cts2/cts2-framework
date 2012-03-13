package edu.mayo.cts2.framework.webapp.soap.endpoint.codesystemversion;

import edu.mayo.cts2.framework.model.codesystemversion.CodeSystemVersionCatalogEntry;
import edu.mayo.cts2.framework.model.core.FormatReference;
import edu.mayo.cts2.framework.model.service.core.ProfileElement;
import edu.mayo.cts2.framework.model.service.core.types.FunctionalProfile;
import edu.mayo.cts2.framework.model.service.core.types.ImplementationProfile;
import edu.mayo.cts2.framework.model.service.core.types.StructuralProfile;
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
import edu.mayo.cts2.framework.model.wsdl.codesystemversionread.Exists;
import edu.mayo.cts2.framework.model.wsdl.codesystemversionread.ExistsCodeSystemVersionForCodeSystem;
import edu.mayo.cts2.framework.model.wsdl.codesystemversionread.ExistsCodeSystemVersionForCodeSystemResponse;
import edu.mayo.cts2.framework.model.wsdl.codesystemversionread.ExistsResponse;
import edu.mayo.cts2.framework.model.wsdl.codesystemversionread.ExistsVersionId;
import edu.mayo.cts2.framework.model.wsdl.codesystemversionread.ExistsVersionIdResponse;
import edu.mayo.cts2.framework.model.wsdl.codesystemversionread.GetCodeSystemByVersionId;
import edu.mayo.cts2.framework.model.wsdl.codesystemversionread.GetCodeSystemByVersionIdResponse;
import edu.mayo.cts2.framework.model.wsdl.codesystemversionread.GetCodeSystemVersionForCodeSystem;
import edu.mayo.cts2.framework.model.wsdl.codesystemversionread.GetCodeSystemVersionForCodeSystemResponse;
import edu.mayo.cts2.framework.model.wsdl.codesystemversionread.GetSupportedTag;
import edu.mayo.cts2.framework.model.wsdl.codesystemversionread.GetSupportedTagResponse;
import edu.mayo.cts2.framework.model.wsdl.codesystemversionread.Read;
import edu.mayo.cts2.framework.model.wsdl.codesystemversionread.ReadResponse;
import edu.mayo.cts2.framework.service.profile.codesystemversion.CodeSystemVersionReadService;
import edu.mayo.cts2.framework.webapp.soap.endpoint.AbstractReadServiceEndpoint;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint("CodeSystemVersionCatalogReadServicesEndpoint")
public class CodeSystemVersionCatalogReadServicesEndpoint extends AbstractReadServiceEndpoint {

  @Cts2Service
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

  @PayloadRoot(localPart = "existsVersionId", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/CodeSystemVersionCatalogReadServices")
  @ResponsePayload
  public ExistsVersionIdResponse existsVersionId(@RequestPayload ExistsVersionId request) {
    boolean exists = this.codeSystemVersionReadService.existsVersionId(
        request.getCodeSystem(),
        request.getOfficialResourceVersionID());

    ExistsVersionIdResponse response = new ExistsVersionIdResponse();
    response.setReturn(exists);

    return response;
  }

  @PayloadRoot(localPart = "getCodeSystemVersionForCodeSystem", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/CodeSystemVersionCatalogReadServices")
  @ResponsePayload
  public GetCodeSystemVersionForCodeSystemResponse getCodeSystemVersionForCodeSystem(
      @RequestPayload GetCodeSystemVersionForCodeSystem request) {
    GetCodeSystemVersionForCodeSystemResponse response = new GetCodeSystemVersionForCodeSystemResponse();
    response.setReturn(
        this.codeSystemVersionReadService.getCodeSystemVersionForCodeSystem(
            request.getCodeSystem(),
            request.getTag().getName()));

    return response;
  }

  @PayloadRoot(localPart = "getCodeSystemByVersionId", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/CodeSystemVersionCatalogReadServices")
  @ResponsePayload
  public GetCodeSystemByVersionIdResponse getCodeSystemByVersionId(
      @RequestPayload GetCodeSystemByVersionId request) {
    GetCodeSystemByVersionIdResponse response = new GetCodeSystemByVersionIdResponse();
    response.setReturn(
        this.codeSystemVersionReadService.getCodeSystemByVersionId(
            request.getCodeSystem(),
            request.getOfficialResourceVersionId(),
            this.resolveReadContext(request.getReadContext())));

    return response;
  }

  @PayloadRoot(localPart = "getSupportedTag", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/CodeSystemVersionCatalogReadServices")
  @ResponsePayload
  public GetSupportedTagResponse getSupportedTag(@RequestPayload GetSupportedTag request) {
    GetSupportedTagResponse response = new GetSupportedTagResponse();
    response.setReturn(this.codeSystemVersionReadService.getSupportedTag());

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
    profile.setStructuralProfile(StructuralProfile.SP_CODE_SYSTEM_VERSION);

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
