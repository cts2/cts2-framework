package edu.mayo.cts2.framework.webapp.soap.endpoint.mapversion;

import java.util.concurrent.Callable;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import edu.mayo.cts2.framework.model.command.ResolvedReadContext;
import edu.mayo.cts2.framework.model.core.FormatReference;
import edu.mayo.cts2.framework.model.mapversion.MapEntry;
import edu.mayo.cts2.framework.model.mapversion.MapVersion;
import edu.mayo.cts2.framework.model.service.core.FunctionalProfileEntry;
import edu.mayo.cts2.framework.model.service.core.NameOrURI;
import edu.mayo.cts2.framework.model.service.core.ProfileElement;
import edu.mayo.cts2.framework.model.service.core.QueryControl;
import edu.mayo.cts2.framework.model.service.core.ReadContext;
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
import edu.mayo.cts2.framework.model.wsdl.mapversionread.EntryExists;
import edu.mayo.cts2.framework.model.wsdl.mapversionread.EntryExistsResponse;
import edu.mayo.cts2.framework.model.wsdl.mapversionread.Exists;
import edu.mayo.cts2.framework.model.wsdl.mapversionread.ExistsMapVersionForMap;
import edu.mayo.cts2.framework.model.wsdl.mapversionread.ExistsMapVersionForMapResponse;
import edu.mayo.cts2.framework.model.wsdl.mapversionread.ExistsResponse;
import edu.mayo.cts2.framework.model.wsdl.mapversionread.Read;
import edu.mayo.cts2.framework.model.wsdl.mapversionread.ReadEntry;
import edu.mayo.cts2.framework.model.wsdl.mapversionread.ReadEntryResponse;
import edu.mayo.cts2.framework.model.wsdl.mapversionread.ReadMapVersionForMap;
import edu.mayo.cts2.framework.model.wsdl.mapversionread.ReadMapVersionForMapResponse;
import edu.mayo.cts2.framework.model.wsdl.mapversionread.ReadResponse;
import edu.mayo.cts2.framework.service.profile.mapentry.MapEntryReadService;
import edu.mayo.cts2.framework.service.profile.mapentry.name.MapEntryReadId;
import edu.mayo.cts2.framework.service.profile.mapversion.MapVersionReadService;
import edu.mayo.cts2.framework.webapp.soap.endpoint.AbstractReadServiceEndpoint;

@Endpoint("MapVersionReadServicesEndpoint")
public class MapVersionReadServicesEndpoint extends AbstractReadServiceEndpoint {

  @Cts2Service
  private MapVersionReadService mapVersionReadService;
  
  @Cts2Service
  private MapEntryReadService mapEntryReadService;

  @PayloadRoot(localPart = "read", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/MapVersionReadServices")
  @ResponsePayload
  public ReadResponse read(@RequestPayload Read request) {
    MapVersion mapVersion = this.doRead(
        this.mapVersionReadService,
        request.getMapVersion(),
        request.getQueryControl(),
        request.getContext());

    ReadResponse response = new ReadResponse();
    response.setMapVersion(mapVersion);
    return response;
  }

  @PayloadRoot(localPart = "readMapVersionForMap", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/MapVersionReadServices")
  @ResponsePayload
  public ReadMapVersionForMapResponse readMapVersionForMap(@RequestPayload ReadMapVersionForMap request) {
    MapVersion mapVersion = this.doReadMapVersionForMap(
        this.mapVersionReadService,
        request.getMap(),
        request.getTag(),
        request.getQueryControl(),
        request.getContext());

    ReadMapVersionForMapResponse response = new ReadMapVersionForMapResponse();
    response.setMapVersion(mapVersion);
    return response;
  }

  @PayloadRoot(localPart = "readEntry", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/MapVersionReadServices")
  @ResponsePayload
  public ReadEntryResponse readEntry(@RequestPayload ReadEntry request) {
	  
	MapEntryReadId id = new MapEntryReadId(request.getSourceEntity(), request.getMapVersion());
	  
    MapEntry entry = this.mapEntryReadService.read(
    		id, null);

    ReadEntryResponse response = new ReadEntryResponse();
    response.setReturn(entry);
    return response;
  }

  @PayloadRoot(localPart = "exists", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/MapVersionReadServices")
  @ResponsePayload
  public ExistsResponse exists(@RequestPayload Exists request) {
    boolean exists = this.mapVersionReadService.exists(
    		request.getMapVersion(), 
    		this.resolveReadContext(request.getContext()));

    ExistsResponse response = new ExistsResponse();
    response.setReturn(exists);
    return response;
  }

  @PayloadRoot(localPart = "existsMapVersionForMap", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/MapVersionReadServices")
  @ResponsePayload
  public ExistsMapVersionForMapResponse existsMapVersionForMap(@RequestPayload ExistsMapVersionForMap request) {
    boolean exists = this.mapVersionReadService.existsByTag(
        request.getMap(),
        this.resolveTag(request.getTag(), this.mapVersionReadService),
        this.resolveReadContext(request.getContext()));

    ExistsMapVersionForMapResponse response = new ExistsMapVersionForMapResponse();
    response.setReturn(exists);
    return response;
  }

  @PayloadRoot(localPart = "entryExists", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/MapVersionReadServices")
  @ResponsePayload
  public EntryExistsResponse entryExists(@RequestPayload EntryExists request) {
	MapEntryReadId id = new MapEntryReadId(request.getSourceEntity(), request.getMapVersion());

    boolean exists = this.mapEntryReadService.exists(
        id, null);

    EntryExistsResponse response = new EntryExistsResponse();
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
    response.setReturn(this.mapVersionReadService.getKnownNamespaceList());

    return response;
  }

  @PayloadRoot(localPart = "getServiceDescription", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/BaseServiceTypes")
  @ResponsePayload
  public GetServiceDescriptionResponse getServiceDescription(@RequestPayload GetServiceDescription request) {
    GetServiceDescriptionResponse response = new GetServiceDescriptionResponse();
    response.setReturn(this.mapVersionReadService.getServiceDescription());

    return response;
  }

  @PayloadRoot(localPart = "getServiceName", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/BaseServiceTypes")
  @ResponsePayload
  public GetServiceNameResponse getServiceName(@RequestPayload GetServiceName request) {
    GetServiceNameResponse response = new GetServiceNameResponse();
    response.setReturn(this.mapVersionReadService.getServiceName());

    return response;
  }

  @PayloadRoot(localPart = "getServiceProvider", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/BaseServiceTypes")
  @ResponsePayload
  public GetServiceProviderResponse getServiceProvider(@RequestPayload GetServiceProvider request) {
    GetServiceProviderResponse response = new GetServiceProviderResponse();
    response.setReturn(this.mapVersionReadService.getServiceProvider());

    return response;
  }

  @PayloadRoot(localPart = "getServiceVersion", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/BaseServiceTypes")
  @ResponsePayload
  public GetServiceVersionResponse getServiceVersion(@RequestPayload GetServiceVersion request) {
    GetServiceVersionResponse response = new GetServiceVersionResponse();
    response.setReturn(this.mapVersionReadService.getServiceVersion());

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
    profile.setStructuralProfile(StructuralProfile.SP_MAP_VERSION);

    FunctionalProfileEntry entry = new FunctionalProfileEntry();
	entry.setContent(FunctionalProfile.FP_READ.name());
    profile.addFunctionalProfile(entry);

    ProfileElement profiles[] = new ProfileElement[1];
    profiles[0] = profile;

    GetSupportedProfileResponse response = new GetSupportedProfileResponse();
    response.setReturn(profiles);

    return response;
  }
  
  private MapVersion doReadMapVersionForMap(
      final MapVersionReadService readService,
      final NameOrURI map,
      final NameOrURI tag,
      final QueryControl queryControl,
      final ReadContext context) {
    final ResolvedReadContext resolvedReadContext = this.resolveReadContext(context);

    return this.doTimedCall(new Callable<MapVersion>() {
      
      @Override
      public MapVersion call() throws Exception {
        return readService.readByTag(
        		map, 
        		resolveTag(tag, readService),
        		resolvedReadContext);
      }
    }, queryControl);
  }

}
