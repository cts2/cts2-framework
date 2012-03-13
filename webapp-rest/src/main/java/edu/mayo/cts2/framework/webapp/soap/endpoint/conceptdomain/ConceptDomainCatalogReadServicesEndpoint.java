package edu.mayo.cts2.framework.webapp.soap.endpoint.conceptdomain;

import edu.mayo.cts2.framework.model.command.ResolvedReadContext;
import edu.mayo.cts2.framework.model.conceptdomain.ConceptDomainCatalogEntry;
import edu.mayo.cts2.framework.model.core.FormatReference;
import edu.mayo.cts2.framework.model.service.core.EntityNameOrURI;
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
import edu.mayo.cts2.framework.model.wsdl.conceptdomainread.Exists;
import edu.mayo.cts2.framework.model.wsdl.conceptdomainread.ExistsDefiningEntity;
import edu.mayo.cts2.framework.model.wsdl.conceptdomainread.ExistsDefiningEntityResponse;
import edu.mayo.cts2.framework.model.wsdl.conceptdomainread.ExistsResponse;
import edu.mayo.cts2.framework.model.wsdl.conceptdomainread.Read;
import edu.mayo.cts2.framework.model.wsdl.conceptdomainread.ReadByDefiningEntity;
import edu.mayo.cts2.framework.model.wsdl.conceptdomainread.ReadByDefiningEntityResponse;
import edu.mayo.cts2.framework.model.wsdl.conceptdomainread.ReadResponse;
import edu.mayo.cts2.framework.service.profile.conceptdomain.ConceptDomainReadService;
import edu.mayo.cts2.framework.webapp.soap.endpoint.AbstractReadServiceEndpoint;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.util.concurrent.Callable;

@Endpoint("ConceptDomainCatalogReadServicesEndpoint")
public class ConceptDomainCatalogReadServicesEndpoint extends AbstractReadServiceEndpoint {

  @Cts2Service
  private ConceptDomainReadService conceptDomainReadService;

  @PayloadRoot(localPart = "read", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/ConceptDomainCatalogReadServices")
  @ResponsePayload
  public ReadResponse read(@RequestPayload Read request) {
    ConceptDomainCatalogEntry entry = this.doRead(
        this.conceptDomainReadService,
        request.getConceptDomainId(),
        request.getQueryControl(),
        request.getContext());

    ReadResponse response = new ReadResponse();
    response.setReturn(entry);

    return response;
  }

  @PayloadRoot(localPart = "readByDefiningEntity", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/ConceptDomainCatalogReadServices")
  @ResponsePayload
  public ReadByDefiningEntityResponse readByDefiningEntity(@RequestPayload ReadByDefiningEntity request) {
    ConceptDomainCatalogEntry entry = this.doReadByDefiningEntity(
        this.conceptDomainReadService,
        request.getEntity(),
        request.getQueryControl(),
        request.getContext());
    ReadByDefiningEntityResponse response = new ReadByDefiningEntityResponse();
    response.setReturn(entry);
    return response;
  }

  @PayloadRoot(localPart = "exists", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/ConceptDomainCatalogReadServices")
  @ResponsePayload
  public ExistsResponse exists(@RequestPayload Exists request) {
    boolean exists = this.conceptDomainReadService.exists(request.getConceptDomainId(), request.getContext());

    ExistsResponse response = new ExistsResponse();
    response.setReturn(exists);

    return response;
  }

  @PayloadRoot(localPart = "existsDefiningEntity", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/ConceptDomainCatalogReadServices")
  @ResponsePayload
  public ExistsDefiningEntityResponse existsByDefiningEntity(@RequestPayload ExistsDefiningEntity request) {
    ExistsDefiningEntityResponse response = new ExistsDefiningEntityResponse();
    response.setReturn(
        this.conceptDomainReadService.existsDefiningEntity(
            request.getEntity(),
            this.resolveReadContext(request.getContext())));

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
    response.setReturn(this.conceptDomainReadService.getKnownNamespaceList());

    return response;
  }

  @PayloadRoot(localPart = "getServiceDescription", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/BaseServiceTypes")
  @ResponsePayload
  public GetServiceDescriptionResponse getServiceDescription(@RequestPayload GetServiceDescription request) {
    GetServiceDescriptionResponse response = new GetServiceDescriptionResponse();
    response.setReturn(this.conceptDomainReadService.getServiceDescription());

    return response;
  }

  @PayloadRoot(localPart = "getServiceName", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/BaseServiceTypes")
  @ResponsePayload
  public GetServiceNameResponse getServiceName(@RequestPayload GetServiceName request) {
    GetServiceNameResponse response = new GetServiceNameResponse();
    response.setReturn(this.conceptDomainReadService.getServiceName());

    return response;
  }

  @PayloadRoot(localPart = "getServiceProvider", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/BaseServiceTypes")
  @ResponsePayload
  public GetServiceProviderResponse getServiceProvider(@RequestPayload GetServiceProvider request) {
    GetServiceProviderResponse response = new GetServiceProviderResponse();
    response.setReturn(this.conceptDomainReadService.getServiceProvider());

    return response;
  }

  @PayloadRoot(localPart = "getServiceVersion", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/BaseServiceTypes")
  @ResponsePayload
  public GetServiceVersionResponse getServiceVersion(@RequestPayload GetServiceVersion request) {
    GetServiceVersionResponse response = new GetServiceVersionResponse();
    response.setReturn(this.conceptDomainReadService.getServiceVersion());

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
    profile.setStructuralProfile(StructuralProfile.SP_CONCEPT_DOMAIN);

    FunctionalProfile functionalProfiles[] = new FunctionalProfile[1];
    functionalProfiles[0] = FunctionalProfile.FP_READ;
    profile.setFunctionalProfile(functionalProfiles);

    ProfileElement profiles[] = new ProfileElement[1];
    profiles[0] = profile;

    GetSupportedProfileResponse response = new GetSupportedProfileResponse();
    response.setReturn(profiles);

    return response;
  }

  private ConceptDomainCatalogEntry doReadByDefiningEntity(
      final ConceptDomainReadService readService,
      final EntityNameOrURI entity,
      final QueryControl queryControl,
      final ReadContext context) {

    final ResolvedReadContext resolvedReadContext = this.resolveReadContext(context);

    return this.doTimedCall(new Callable<ConceptDomainCatalogEntry>() {

      @Override
      public ConceptDomainCatalogEntry call() throws Exception {
        return readService.readByDefiningEntity(entity, resolvedReadContext);
      }
    }, queryControl);
  }
  
}
