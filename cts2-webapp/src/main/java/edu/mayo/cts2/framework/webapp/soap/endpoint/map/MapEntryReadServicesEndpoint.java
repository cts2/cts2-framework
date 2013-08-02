package edu.mayo.cts2.framework.webapp.soap.endpoint.map;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import edu.mayo.cts2.framework.model.core.FormatReference;
import edu.mayo.cts2.framework.model.mapversion.MapEntry;
import edu.mayo.cts2.framework.model.service.core.FunctionalProfileEntry;
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
import edu.mayo.cts2.framework.model.wsdl.mapentryread.Exists;
import edu.mayo.cts2.framework.model.wsdl.mapentryread.ExistsResponse;
import edu.mayo.cts2.framework.model.wsdl.mapentryread.Read;
import edu.mayo.cts2.framework.model.wsdl.mapentryread.ReadResponse;
import edu.mayo.cts2.framework.service.profile.mapentry.MapEntryReadService;
import edu.mayo.cts2.framework.service.profile.mapentry.name.MapEntryReadId;
import edu.mayo.cts2.framework.webapp.soap.endpoint.AbstractReadServiceEndpoint;

@Endpoint("MapEntryReadServicesEndpoint")
public class MapEntryReadServicesEndpoint extends AbstractReadServiceEndpoint {

  @Cts2Service
  private MapEntryReadService mapEntryReadService;

  @PayloadRoot(localPart = "read", namespace = CTS2_NAMESPACE_ROOT + "MapEntryReadServices")
  @ResponsePayload
  public ReadResponse read(@RequestPayload Read request) {

	MapEntryReadId id = new MapEntryReadId(
		     request.getMapFrom(), 
		     request.getMapVersion());
   
	MapEntry entry = this.mapEntryReadService.read(
        id,
        this.resolveReadContext(request.getContext()));

    ReadResponse response = new ReadResponse();
    response.setMapEntry(entry);
    return response;
  }

  @PayloadRoot(localPart = "exists", namespace = CTS2_NAMESPACE_ROOT + "MapEntryReadServices")
  @ResponsePayload
  public ExistsResponse exists(@RequestPayload Exists request) {
	  MapEntryReadId id = new MapEntryReadId(
			     request.getMapFrom(), 
			     request.getMapVersion());
	   
    boolean exists = this.mapEntryReadService.exists(
        id,
        this.resolveReadContext(request.getContext()));

    ExistsResponse response = new ExistsResponse();
    response.setReturn(exists);
    return response;
  }

  /*******************************************************/
  /*                   BaseServices                      */
  /*******************************************************/
  @PayloadRoot(localPart = "getDefaultFormat", namespace = CTS2_NAMESPACE_ROOT + "BaseServiceTypes")
  @ResponsePayload
  public GetDefaultFormatResponse getDefaultFormat(@RequestPayload GetDefaultFormat request) {
    FormatReference format = new FormatReference("SOAP");

    GetDefaultFormatResponse response = new GetDefaultFormatResponse();
    response.setReturn(format);

    return response;
  }

  @PayloadRoot(localPart = "getImplementationType", namespace = CTS2_NAMESPACE_ROOT + "BaseServiceTypes")
  @ResponsePayload
  public GetImplementationTypeResponse getImplementationType(@RequestPayload GetImplementationType request) {
    ImplementationProfile implementations[] = new ImplementationProfile[1];
    implementations[0] = ImplementationProfile.IP_SOAP;

    GetImplementationTypeResponse response = new GetImplementationTypeResponse();
    response.setReturn(implementations);

    return response;
  }

  @PayloadRoot(localPart = "getKnownNamespace", namespace = CTS2_NAMESPACE_ROOT + "BaseServiceTypes")
  @ResponsePayload
  public GetKnownNamespaceResponse getKnownNamespace(@RequestPayload GetKnownNamespace request) {
    GetKnownNamespaceResponse response = new GetKnownNamespaceResponse();
    response.setReturn(this.mapEntryReadService.getKnownNamespaceList());

    return response;
  }

  @PayloadRoot(localPart = "getServiceDescription", namespace = CTS2_NAMESPACE_ROOT + "BaseServiceTypes")
  @ResponsePayload
  public GetServiceDescriptionResponse getServiceDescription(@RequestPayload GetServiceDescription request) {
    GetServiceDescriptionResponse response = new GetServiceDescriptionResponse();
    response.setReturn(this.mapEntryReadService.getServiceDescription());

    return response;
  }

  @PayloadRoot(localPart = "getServiceName", namespace = CTS2_NAMESPACE_ROOT + "BaseServiceTypes")
  @ResponsePayload
  public GetServiceNameResponse getServiceName(@RequestPayload GetServiceName request) {
    GetServiceNameResponse response = new GetServiceNameResponse();
    response.setReturn(this.mapEntryReadService.getServiceName());

    return response;
  }

  @PayloadRoot(localPart = "getServiceProvider", namespace = CTS2_NAMESPACE_ROOT + "BaseServiceTypes")
  @ResponsePayload
  public GetServiceProviderResponse getServiceProvider(@RequestPayload GetServiceProvider request) {
    GetServiceProviderResponse response = new GetServiceProviderResponse();
    response.setReturn(this.mapEntryReadService.getServiceProvider());

    return response;
  }

  @PayloadRoot(localPart = "getServiceVersion", namespace = CTS2_NAMESPACE_ROOT + "BaseServiceTypes")
  @ResponsePayload
  public GetServiceVersionResponse getServiceVersion(@RequestPayload GetServiceVersion request) {
    GetServiceVersionResponse response = new GetServiceVersionResponse();
    response.setReturn(this.mapEntryReadService.getServiceVersion());

    return response;
  }

  @PayloadRoot(localPart = "getSupportedFormat", namespace = CTS2_NAMESPACE_ROOT + "BaseServiceTypes")
  @ResponsePayload
  public GetSupportedFormatResponse getSupportedFormat(@RequestPayload GetSupportedFormat request) {
    FormatReference format[] = new FormatReference[1];
    format[0] = new FormatReference("SOAP");

    GetSupportedFormatResponse response = new GetSupportedFormatResponse();
    response.setReturn(format);

    return response;
  }

  @PayloadRoot(localPart = "getSupportedProfile", namespace = CTS2_NAMESPACE_ROOT + "BaseServiceTypes")
  @ResponsePayload
  public GetSupportedProfileResponse getSupportedProfile(@RequestPayload GetSupportedProfile request) {
    ProfileElement profile = new ProfileElement();
    profile.setStructuralProfile(StructuralProfile.SP_MAP);

    FunctionalProfileEntry entry = new FunctionalProfileEntry();
	entry.setContent(FunctionalProfile.FP_READ.name());
    profile.addFunctionalProfile(entry);

    ProfileElement profiles[] = new ProfileElement[1];
    profiles[0] = profile;

    GetSupportedProfileResponse response = new GetSupportedProfileResponse();
    response.setReturn(profiles);

    return response;
  }

}
