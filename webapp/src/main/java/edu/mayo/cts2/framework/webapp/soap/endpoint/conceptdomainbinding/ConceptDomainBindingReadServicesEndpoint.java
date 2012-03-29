package edu.mayo.cts2.framework.webapp.soap.endpoint.conceptdomainbinding;

import java.util.concurrent.Callable;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import edu.mayo.cts2.framework.model.conceptdomainbinding.ConceptDomainBinding;
import edu.mayo.cts2.framework.model.core.FormatReference;
import edu.mayo.cts2.framework.model.extension.LocalIdConceptDomainBinding;
import edu.mayo.cts2.framework.model.service.core.NameOrURI;
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
import edu.mayo.cts2.framework.model.wsdl.conceptdomainbindingread.Exists;
import edu.mayo.cts2.framework.model.wsdl.conceptdomainbindingread.ExistsResponse;
import edu.mayo.cts2.framework.model.wsdl.conceptdomainbindingread.ExistsURI;
import edu.mayo.cts2.framework.model.wsdl.conceptdomainbindingread.ExistsURIResponse;
import edu.mayo.cts2.framework.model.wsdl.conceptdomainbindingread.GetSupportedTag;
import edu.mayo.cts2.framework.model.wsdl.conceptdomainbindingread.GetSupportedTagResponse;
import edu.mayo.cts2.framework.model.wsdl.conceptdomainbindingread.Read;
import edu.mayo.cts2.framework.model.wsdl.conceptdomainbindingread.ReadByURI;
import edu.mayo.cts2.framework.model.wsdl.conceptdomainbindingread.ReadByURIResponse;
import edu.mayo.cts2.framework.model.wsdl.conceptdomainbindingread.ReadResponse;
import edu.mayo.cts2.framework.service.profile.conceptdomainbinding.ConceptDomainBindingReadService;
import edu.mayo.cts2.framework.service.profile.conceptdomainbinding.name.ConceptDomainBindingReadId;
import edu.mayo.cts2.framework.webapp.soap.endpoint.AbstractReadServiceEndpoint;

@Endpoint("ConceptDomainBindingReadServicesEndpoint")
public class ConceptDomainBindingReadServicesEndpoint extends AbstractReadServiceEndpoint {

  @Cts2Service
  private ConceptDomainBindingReadService conceptDomainBindingReadService;

  @PayloadRoot(localPart = "read", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/ConceptDomainBindingReadServices")
  @ResponsePayload
  public ReadResponse read(@RequestPayload Read request) {
    ReadResponse response = new ReadResponse();
    NameOrURI conceptDomain = request.getConceptDomain();
    NameOrURI valueSet = request.getValueSet();
    NameOrURI applicableContext = request.getApplicableContext();
    NameOrURI bindingQualifier = request.getBindingQualifier();

    response.setReturn(
        this.doRead(
            this.conceptDomainBindingReadService,
            conceptDomain,
            valueSet,
            applicableContext,
            bindingQualifier));

    return response;
  }

  @PayloadRoot(localPart = "exists", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/ConceptDomainBindingReadServices")
  @ResponsePayload
  public ExistsResponse exists(@RequestPayload Exists request) {
    ExistsResponse response = new ExistsResponse();
    NameOrURI conceptDomain = request.getConceptDomain();
    NameOrURI valueSet = request.getValueSet();
    NameOrURI applicableContext = request.getApplicableContext();
    NameOrURI bindingQualifier = request.getBindingQualifier();

    boolean exists =
        this.conceptDomainBindingReadService.exists(
            conceptDomain,
            valueSet,
            applicableContext,
            bindingQualifier);
    response.setReturn(exists);

    return response;
  }

  @PayloadRoot(localPart = "readByURI", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/ConceptDomainBindingReadServices")
  @ResponsePayload
  public ReadByURIResponse readByURI(@RequestPayload ReadByURI request) {
    ReadByURIResponse response = new ReadByURIResponse();

    LocalIdConceptDomainBinding result = this.doReadByURI(
    					this.conceptDomainBindingReadService, 
    						new ConceptDomainBindingReadId(request.getUri()));

    if(result != null){
    	response.setReturn(result.getResource());
    }
    
    return response;
  }

  @PayloadRoot(localPart = "existsURI", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/ConceptDomainBindingReadServices")
  @ResponsePayload
  public ExistsURIResponse existsURI(@RequestPayload ExistsURI request) {
    ExistsURIResponse response = new ExistsURIResponse();
    
    ConceptDomainBindingReadId id = new ConceptDomainBindingReadId(request.getUri());
    
    response.setReturn(this.conceptDomainBindingReadService.exists(id, null));

    return response;
  }

  @PayloadRoot(localPart = "getSupportedTag", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/ConceptDomainBindingReadServices")
  @ResponsePayload
  public GetSupportedTagResponse getSupportedTag(@RequestPayload GetSupportedTag request) {
    GetSupportedTagResponse response = new GetSupportedTagResponse();
    response.setReturn(conceptDomainBindingReadService.getSupportedTag());

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
    profile.setStructuralProfile(StructuralProfile.SP_CONCEPT_DOMAIN_BINDING);

    profile.setFunctionalProfile(FunctionalProfile.FP_READ);

    ProfileElement profiles[] = new ProfileElement[1];
    profiles[0] = profile;

    GetSupportedProfileResponse response = new GetSupportedProfileResponse();
    response.setReturn(profiles);

    return response;
  }

  private ConceptDomainBinding doRead(
      final ConceptDomainBindingReadService readService,
      final NameOrURI conceptDomain,
      final NameOrURI valueSet,
      final NameOrURI applicableContext,
      final NameOrURI bindingQualifier) {
    return this.doTimedCall(new Callable<ConceptDomainBinding>() {

      @Override
      public ConceptDomainBinding call() throws Exception {
        return readService.read(conceptDomain, valueSet, applicableContext, bindingQualifier);
      }

    }, null);
  }
  
  private LocalIdConceptDomainBinding doReadByURI(
      final ConceptDomainBindingReadService readService,
      final ConceptDomainBindingReadId id) {
    return this.doTimedCall(new Callable<LocalIdConceptDomainBinding>() {

      @Override
      public LocalIdConceptDomainBinding call() throws Exception {
        return readService.read(id, null);
      }

    }, null);
  }

}
