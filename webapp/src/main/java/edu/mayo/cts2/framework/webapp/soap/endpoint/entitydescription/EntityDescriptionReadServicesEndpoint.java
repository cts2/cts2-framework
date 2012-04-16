package edu.mayo.cts2.framework.webapp.soap.endpoint.entitydescription;

import java.util.List;
import java.util.concurrent.Callable;

import javax.annotation.Resource;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import edu.mayo.cts2.framework.model.command.ResolvedReadContext;
import edu.mayo.cts2.framework.model.core.CodeSystemReference;
import edu.mayo.cts2.framework.model.core.CodeSystemVersionReference;
import edu.mayo.cts2.framework.model.core.EntityReference;
import edu.mayo.cts2.framework.model.core.FormatReference;
import edu.mayo.cts2.framework.model.core.VersionTagReference;
import edu.mayo.cts2.framework.model.entity.EntityDescription;
import edu.mayo.cts2.framework.model.entity.EntityList;
import edu.mayo.cts2.framework.model.service.core.EntityNameOrURI;
import edu.mayo.cts2.framework.model.service.core.FunctionalProfileEntry;
import edu.mayo.cts2.framework.model.service.core.ProfileElement;
import edu.mayo.cts2.framework.model.service.core.QueryControl;
import edu.mayo.cts2.framework.model.service.core.ReadContext;
import edu.mayo.cts2.framework.model.service.core.types.FunctionalProfile;
import edu.mayo.cts2.framework.model.service.core.types.ImplementationProfile;
import edu.mayo.cts2.framework.model.service.core.types.StructuralProfile;
import edu.mayo.cts2.framework.model.util.ModelUtils;
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
import edu.mayo.cts2.framework.model.wsdl.entitydescriptionread.AvailableDescriptions;
import edu.mayo.cts2.framework.model.wsdl.entitydescriptionread.AvailableDescriptionsResponse;
import edu.mayo.cts2.framework.model.wsdl.entitydescriptionread.Exists;
import edu.mayo.cts2.framework.model.wsdl.entitydescriptionread.ExistsInCodeSystem;
import edu.mayo.cts2.framework.model.wsdl.entitydescriptionread.ExistsInCodeSystemResponse;
import edu.mayo.cts2.framework.model.wsdl.entitydescriptionread.ExistsResponse;
import edu.mayo.cts2.framework.model.wsdl.entitydescriptionread.GetKnownCodeSystem;
import edu.mayo.cts2.framework.model.wsdl.entitydescriptionread.GetKnownCodeSystemResponse;
import edu.mayo.cts2.framework.model.wsdl.entitydescriptionread.GetKnownCodeSystemVersion;
import edu.mayo.cts2.framework.model.wsdl.entitydescriptionread.GetKnownCodeSystemVersionResponse;
import edu.mayo.cts2.framework.model.wsdl.entitydescriptionread.GetSupportedVersionTag;
import edu.mayo.cts2.framework.model.wsdl.entitydescriptionread.GetSupportedVersionTagResponse;
import edu.mayo.cts2.framework.model.wsdl.entitydescriptionread.Read;
import edu.mayo.cts2.framework.model.wsdl.entitydescriptionread.ReadByCodeSystem;
import edu.mayo.cts2.framework.model.wsdl.entitydescriptionread.ReadByCodeSystemResponse;
import edu.mayo.cts2.framework.model.wsdl.entitydescriptionread.ReadEntityDescriptions;
import edu.mayo.cts2.framework.model.wsdl.entitydescriptionread.ReadEntityDescriptionsResponse;
import edu.mayo.cts2.framework.model.wsdl.entitydescriptionread.ReadResponse;
import edu.mayo.cts2.framework.service.profile.codesystemversion.CodeSystemVersionReadService;
import edu.mayo.cts2.framework.service.profile.entitydescription.EntityDescriptionReadService;
import edu.mayo.cts2.framework.service.profile.entitydescription.name.EntityDescriptionReadId;
import edu.mayo.cts2.framework.webapp.naming.CodeSystemVersionNameResolver;
import edu.mayo.cts2.framework.webapp.soap.endpoint.AbstractReadServiceEndpoint;

@Endpoint("EntityDescriptionReadServicesEndpoint")
public class EntityDescriptionReadServicesEndpoint extends AbstractReadServiceEndpoint {
  
  @Cts2Service
  private EntityDescriptionReadService entityDescriptionReadService;
  
  @Cts2Service
  private CodeSystemVersionReadService codeSystemVersionReadService;
  
  @Resource
  private CodeSystemVersionNameResolver codeSystemVersionNameResolver;

  @PayloadRoot(localPart = "read", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/EntityDescriptionReadServices")
  @ResponsePayload
  public ReadResponse read(@RequestPayload Read request) {
	EntityDescription base = this.doRead(
			this.entityDescriptionReadService, 
			new EntityDescriptionReadId(
					request.getEntityId(), 
					request.getCodeSystemVersion()),
			request.getQueryControl(),
			request.getContext());
    
    ReadResponse response = new ReadResponse();
    response.setReturn(base);
    return response;
  }

  @PayloadRoot(localPart = "exists", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/EntityDescriptionReadServices")
  @ResponsePayload
  public ExistsResponse exists(@RequestPayload Exists request) {
    ResolvedReadContext resolvedReadContext = this.resolveReadContext(request.getContext());
    boolean exists = this.entityDescriptionReadService.exists(
        new EntityDescriptionReadId(
        		request.getEntityId(),
        		request.getCodeSystemVersion()),
        resolvedReadContext);

    ExistsResponse response = new ExistsResponse();
    response.setReturn(exists);
    return response;
  }

  @PayloadRoot(localPart = "existsInCodeSystem", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/EntityDescriptionReadServices")
  @ResponsePayload
  public ExistsInCodeSystemResponse existsInCodeSystem(@RequestPayload ExistsInCodeSystem request) {
    ResolvedReadContext resolvedReadContext = this.resolveReadContext(request.getContext());
   
	String codeSystemVersionName = codeSystemVersionNameResolver.getCodeSystemVersionNameFromTag(
			codeSystemVersionReadService, 
			request.getCodeSystem(), 
			this.resolveTag(request.getTag(), codeSystemVersionReadService), 
			this.resolveReadContext(request.getContext()));
	
	EntityDescriptionReadId id = new EntityDescriptionReadId(
			request.getEntityId(), 
			ModelUtils.nameOrUriFromName(codeSystemVersionName));
    
	boolean exists = this.entityDescriptionReadService.exists(
				id,
		        resolvedReadContext);

    ExistsInCodeSystemResponse response = new ExistsInCodeSystemResponse();
    response.setReturn(exists);
    return response;
  }


  @PayloadRoot(localPart = "readByCodeSystem", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/EntityDescriptionReadServices")
  @ResponsePayload
  public ReadByCodeSystemResponse readByCodeSystem(@RequestPayload ReadByCodeSystem request) {
    
	String codeSystemVersionName = codeSystemVersionNameResolver.getCodeSystemVersionNameFromTag(
			codeSystemVersionReadService, 
			request.getCodeSystem(), 
			this.resolveTag(request.getTag(), codeSystemVersionReadService), 
			this.resolveReadContext(request.getContext()));
	  
	EntityDescription base = this.doRead(
			this.entityDescriptionReadService, 
			new EntityDescriptionReadId(
					request.getEntityId(), 
					ModelUtils.nameOrUriFromName(codeSystemVersionName)),
			request.getQueryControl(),
			request.getContext());

    ReadByCodeSystemResponse response = new ReadByCodeSystemResponse();
    response.setReturn(base);
    return response;
  }

  @PayloadRoot(localPart = "readEntityDescriptions", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/EntityDescriptionReadServices")
  @ResponsePayload
  public ReadEntityDescriptionsResponse readEntityDescriptions(@RequestPayload ReadEntityDescriptions request) {
    EntityList entities = this.doReadEntityDescriptions(
        this.entityDescriptionReadService,
        request.getEntityId(),
        request.getQueryControl(),
        request.getContext());

    ReadEntityDescriptionsResponse response = new ReadEntityDescriptionsResponse();
    response.setEntityList(entities);
    return response;
  }

  @PayloadRoot(localPart = "availableDescriptions", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/EntityDescriptionReadServices")
  @ResponsePayload
  public AvailableDescriptionsResponse availableDescriptions(@RequestPayload AvailableDescriptions request) {
    ResolvedReadContext resolvedReadContext = this.resolveReadContext(request.getContext());
    EntityReference entityReference = this.entityDescriptionReadService.availableDescriptions(request.getEntityId(), resolvedReadContext);

    AvailableDescriptionsResponse response = new AvailableDescriptionsResponse();
    response.setReturn(entityReference);
    return response;
  }

  @PayloadRoot(localPart = "getKnownCodeSystem", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/EntityDescriptionReadServices")
  @ResponsePayload
  public GetKnownCodeSystemResponse getKnownCodeSystem(@RequestPayload GetKnownCodeSystem request) {
    List<CodeSystemReference> ref = this.entityDescriptionReadService.getKnownCodeSystems();

    GetKnownCodeSystemResponse response = new GetKnownCodeSystemResponse();
    response.setReturn(ref);
    return response;
  }

  @PayloadRoot(localPart = "getKnownCodeSystemVersion", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/EntityDescriptionReadServices")
  @ResponsePayload
  public GetKnownCodeSystemVersionResponse getKnownCodeSystemVersion(@RequestPayload GetKnownCodeSystemVersion request) {
    List<CodeSystemVersionReference> refs = this.entityDescriptionReadService.getKnownCodeSystemVersions();

    GetKnownCodeSystemVersionResponse response = new GetKnownCodeSystemVersionResponse();
    response.setReturn(refs);
    return response;
  }

  @PayloadRoot(localPart = "getSupportedVersionTag", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/EntityDescriptionReadServices")
  @ResponsePayload
  public GetSupportedVersionTagResponse getSupportedVersionTag(@RequestPayload GetSupportedVersionTag request) {
    List<VersionTagReference> refs = this.entityDescriptionReadService.getSupportedVersionTags();

    GetSupportedVersionTagResponse response = new GetSupportedVersionTagResponse();
    response.setReturn(refs);
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
    response.setReturn(this.entityDescriptionReadService.getKnownNamespaceList());

    return response;
  }

  @PayloadRoot(localPart = "getServiceDescription", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/BaseServiceTypes")
  @ResponsePayload
  public GetServiceDescriptionResponse getServiceDescription(@RequestPayload GetServiceDescription request) {
    GetServiceDescriptionResponse response = new GetServiceDescriptionResponse();
    response.setReturn(this.entityDescriptionReadService.getServiceDescription());

    return response;
  }

  @PayloadRoot(localPart = "getServiceName", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/BaseServiceTypes")
  @ResponsePayload
  public GetServiceNameResponse getServiceName(@RequestPayload GetServiceName request) {
    GetServiceNameResponse response = new GetServiceNameResponse();
    response.setReturn(this.entityDescriptionReadService.getServiceName());

    return response;
  }

  @PayloadRoot(localPart = "getServiceProvider", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/BaseServiceTypes")
  @ResponsePayload
  public GetServiceProviderResponse getServiceProvider(@RequestPayload GetServiceProvider request) {
    GetServiceProviderResponse response = new GetServiceProviderResponse();
    response.setReturn(this.entityDescriptionReadService.getServiceProvider());

    return response;
  }

  @PayloadRoot(localPart = "getServiceVersion", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/BaseServiceTypes")
  @ResponsePayload
  public GetServiceVersionResponse getServiceVersion(@RequestPayload GetServiceVersion request) {
    GetServiceVersionResponse response = new GetServiceVersionResponse();
    response.setReturn(this.entityDescriptionReadService.getServiceVersion());

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
    profile.setStructuralProfile(StructuralProfile.SP_ENTITY_DESCRIPTION);

    FunctionalProfileEntry entry = new FunctionalProfileEntry();
	entry.setContent(FunctionalProfile.FP_READ.name());
    profile.addFunctionalProfile(entry);

    ProfileElement profiles[] = new ProfileElement[1];
    profiles[0] = profile;

    GetSupportedProfileResponse response = new GetSupportedProfileResponse();
    response.setReturn(profiles);

    return response;
  }


  private EntityList doReadEntityDescriptions(
      final EntityDescriptionReadService readService,
      final EntityNameOrURI entityId,
      final QueryControl queryControl,
      final ReadContext context) {
    final ResolvedReadContext resolvedReadContext = this.resolveReadContext(context);

    return this.doTimedCall(new Callable<EntityList>() {

      @Override
      public EntityList call() throws Exception {
        return readService.readEntityDescriptions(entityId, resolvedReadContext);
      }
    }, queryControl);
  }

	public CodeSystemVersionNameResolver getCodeSystemVersionNameResolver() {
		return codeSystemVersionNameResolver;
	}
	
	public void setCodeSystemVersionNameResolver(
			CodeSystemVersionNameResolver codeSystemVersionNameResolver) {
		this.codeSystemVersionNameResolver = codeSystemVersionNameResolver;
	}

	public CodeSystemVersionReadService getCodeSystemVersionReadService() {
		return codeSystemVersionReadService;
	}

	public void setCodeSystemVersionReadService(
			CodeSystemVersionReadService codeSystemVersionReadService) {
		this.codeSystemVersionReadService = codeSystemVersionReadService;
	}
	  
  
  
}
