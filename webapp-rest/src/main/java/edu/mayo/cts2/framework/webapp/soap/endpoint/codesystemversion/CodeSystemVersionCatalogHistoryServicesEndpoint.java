package edu.mayo.cts2.framework.webapp.soap.endpoint.codesystemversion;

import edu.mayo.cts2.framework.model.codesystemversion.CodeSystemVersionCatalogEntry;
import edu.mayo.cts2.framework.model.codesystemversion.CodeSystemVersionCatalogEntryList;
import edu.mayo.cts2.framework.model.service.core.NameOrURI;
import edu.mayo.cts2.framework.model.wsdl.basehistoryservice.*;
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
import edu.mayo.cts2.framework.model.wsdl.codesystemversionhistory.Count;
import edu.mayo.cts2.framework.model.wsdl.codesystemversionhistory.CountResponse;
import edu.mayo.cts2.framework.model.wsdl.codesystemversionhistory.Difference;
import edu.mayo.cts2.framework.model.wsdl.codesystemversionhistory.DifferenceResponse;
import edu.mayo.cts2.framework.model.wsdl.codesystemversionhistory.GetChangeHistoryFor;
import edu.mayo.cts2.framework.model.wsdl.codesystemversionhistory.GetChangeHistoryForResponse;
import edu.mayo.cts2.framework.model.wsdl.codesystemversionhistory.GetEarliestChangeFor;
import edu.mayo.cts2.framework.model.wsdl.codesystemversionhistory.GetEarliestChangeForResponse;
import edu.mayo.cts2.framework.model.wsdl.codesystemversionhistory.GetLastChangeFor;
import edu.mayo.cts2.framework.model.wsdl.codesystemversionhistory.GetLastChangeForResponse;
import edu.mayo.cts2.framework.model.wsdl.codesystemversionhistory.Intersect;
import edu.mayo.cts2.framework.model.wsdl.codesystemversionhistory.IntersectResponse;
import edu.mayo.cts2.framework.model.wsdl.codesystemversionhistory.ReadChangeSet;
import edu.mayo.cts2.framework.model.wsdl.codesystemversionhistory.ReadChangeSetResponse;
import edu.mayo.cts2.framework.model.wsdl.codesystemversionhistory.Restrict;
import edu.mayo.cts2.framework.model.wsdl.codesystemversionhistory.RestrictResponse;
import edu.mayo.cts2.framework.model.wsdl.codesystemversionhistory.Union;
import edu.mayo.cts2.framework.model.wsdl.codesystemversionhistory.UnionResponse;
import edu.mayo.cts2.framework.model.wsdl.codesystemversionquery.Resolve;
import edu.mayo.cts2.framework.model.wsdl.codesystemversionquery.ResolveResponse;
import edu.mayo.cts2.framework.service.profile.codesystemversion.CodeSystemVersionHistoryService;
import edu.mayo.cts2.framework.webapp.service.AbstractServiceAwareBean.Cts2Service;
import edu.mayo.cts2.framework.webapp.soap.endpoint.AbstractHistoryServiceEndpoint;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint("CodeSystemVersionCatalogHistoryServicesEndpoint")
public class CodeSystemVersionCatalogHistoryServicesEndpoint extends AbstractHistoryServiceEndpoint {

  @Cts2Service
  private CodeSystemVersionHistoryService codeSystemVersionHistoryService;

  @PayloadRoot(localPart = "getEarliestChangeFor", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/CodeSystemVersionCatalogHistoryServices")
  @ResponsePayload
  public GetEarliestChangeForResponse getEarliestChangeFor(@RequestPayload GetEarliestChangeFor request) {
    GetEarliestChangeForResponse response = new GetEarliestChangeForResponse();
    response.setReturn(this.codeSystemVersionHistoryService.getEarliestChangeFor(request.getCodeSystemVersion()));
    return response;
  }

  @PayloadRoot(localPart = "getLastChangeFor", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/CodeSystemVersionCatalogHistoryServices")
  @ResponsePayload
  public GetLastChangeForResponse getLastChangeFor(@RequestPayload GetLastChangeFor request) {
    GetLastChangeForResponse response = new GetLastChangeForResponse();
    response.setReturn(this.codeSystemVersionHistoryService.getLastChangeFor(request.getCodeSystem()));
    return response;
  }

  /* TODO: Implement Method: getChangeHistoryFor */
  @PayloadRoot(localPart = "getChangeHistoryFor", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/CodeSystemVersionCatalogHistoryServices")
  @ResponsePayload
  public GetChangeHistoryForResponse getChangeHistoryFor(@RequestPayload GetChangeHistoryFor request) {
    throw new UnsupportedOperationException("Method not implemented");
//    GetChangeHistoryForResponse response = new GetChangeHistoryForResponse();
//    CodeSystemVersionCatalogEntryList entryList = this.codeSystemVersionHistoryService.getChangeHistoryFor(request.getCodeSystemVersion(), request.getFromDate(), request.getToDate());
//    response.setCodeSystemVersionCatalogEntryList(entryList);
//    return response;
  }

  /*******************************************************/
  /*              BaseHistoryVersionService              */
  /*******************************************************/
  @PayloadRoot(localPart = "getEarliestChange", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/CodeSystemVersionCatalogHistoryServices")
  @ResponsePayload
  public GetEarliestChangeResponse getEarliestChange(@RequestPayload GetEarliestChange request) {
    GetEarliestChangeResponse response = new GetEarliestChangeResponse();
    response.setReturn(this.codeSystemVersionHistoryService.getEarliestChange());
    return response;
  }

  @PayloadRoot(localPart = "getLatestChange", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/CodeSystemVersionCatalogHistoryServices")
  @ResponsePayload
  public GetLatestChangeResponse getLatestChange(@RequestPayload GetLatestChange request) {
    GetLatestChangeResponse response = new GetLatestChangeResponse();
    response.setReturn(this.codeSystemVersionHistoryService.getLatestChange());
    return response;
  }

  /* TODO: Implement Method: getChangeHistory */
  @PayloadRoot(localPart = "getChangeHistory", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/CodeSystemVersionCatalogHistoryServices")
  @ResponsePayload
  public GetChangeHistoryResponse getChangeHistory(@RequestPayload GetChangeHistory request) {
    throw new UnsupportedOperationException("Method not implemented");
//    GetChangeHistoryResponse response = new GetChangeHistoryResponse();
//    response.setReturn(this.codeSystemVersionHistoryService.getChangeHistory());
//    return response;

  }

  @PayloadRoot(localPart = "readChangeSet", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/CodeSystemVersionCatalogHistoryServices")
  @ResponsePayload
  public ReadChangeSetResponse readChangeSet(@RequestPayload ReadChangeSet request) {
    ReadChangeSetResponse response = new ReadChangeSetResponse();
    return response;
  }

  @PayloadRoot(localPart = "resolve", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/CodeSystemVersionCatalogHistoryServices")
  @ResponsePayload
  public ResolveResponse readChangeSet(@RequestPayload Resolve request) {
    ResolveResponse response = new ResolveResponse();
    return response;
  }

  /*******************************************************/
  /*                   BaseQueryService                  */
  /*******************************************************/
  @PayloadRoot(localPart = "count", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/CodeSystemVersionCatalogQueryServices")
  @ResponsePayload
  public CountResponse count(@RequestPayload Count request) {
    CountResponse response = new CountResponse();
    return response;
  }

  @PayloadRoot(localPart = "difference", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/CodeSystemVersionCatalogQueryServices")
  @ResponsePayload
  public DifferenceResponse difference(@RequestPayload Difference request) {
    DifferenceResponse response = new DifferenceResponse();
    return response;
  }

  @PayloadRoot(localPart = "intersect", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/CodeSystemVersionCatalogQueryServices")
  @ResponsePayload
  public IntersectResponse intersect(@RequestPayload Intersect request) {
    IntersectResponse response = new IntersectResponse();
    return response;
  }

  @PayloadRoot(localPart = "restrict", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/CodeSystemVersionCatalogQueryServices")
  @ResponsePayload
  public RestrictResponse restrict(@RequestPayload Restrict request) {
    RestrictResponse response = new RestrictResponse();
    return response;
  }

  @PayloadRoot(localPart = "union", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/CodeSystemVersionCatalogQueryServices")
  @ResponsePayload
  public UnionResponse union(@RequestPayload Union request) {
    UnionResponse response = new UnionResponse();
    return response;
  }

  /*******************************************************/
  /*                   BaseServices                      */
  /*******************************************************/
  @PayloadRoot(localPart = "getDefaultFormat", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/BaseServiceTypes")
  @ResponsePayload
  public GetDefaultFormatResponse getDefaultFormat(@RequestPayload GetDefaultFormat request) {
    GetDefaultFormatResponse response = new GetDefaultFormatResponse();
    response.setReturn(this.codeSystemVersionHistoryService.getDefaultFormat());
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
    response.setReturn(this.codeSystemVersionHistoryService.getKnownNamespaceList());
    return response;
  }

  @PayloadRoot(localPart = "getServiceDescription", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/BaseServiceTypes")
  @ResponsePayload
  public GetServiceDescriptionResponse getServiceDescription(@RequestPayload GetServiceDescription request) {
    GetServiceDescriptionResponse response = new GetServiceDescriptionResponse();
    response.setReturn(this.codeSystemVersionHistoryService.getServiceDescription());
    return response;
  }

  @PayloadRoot(localPart = "getServiceName", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/BaseServiceTypes")
  @ResponsePayload
  public GetServiceNameResponse getServiceName(@RequestPayload GetServiceName request) {
    GetServiceNameResponse response = new GetServiceNameResponse();
    response.setReturn(this.codeSystemVersionHistoryService.getServiceName());
    return response;
  }

  @PayloadRoot(localPart = "getServiceProvider", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/BaseServiceTypes")
  @ResponsePayload
  public GetServiceProviderResponse getServiceProvider(@RequestPayload GetServiceProvider request) {
    GetServiceProviderResponse response = new GetServiceProviderResponse();
    response.setReturn(this.codeSystemVersionHistoryService.getServiceProvider());
    return response;
  }

  @PayloadRoot(localPart = "getServiceVersion", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/BaseServiceTypes")
  @ResponsePayload
  public GetServiceVersionResponse getServiceVersion(@RequestPayload GetServiceVersion request) {
    GetServiceVersionResponse response = new GetServiceVersionResponse();
    response.setReturn(this.codeSystemVersionHistoryService.getServiceVersion());
    return response;
  }

  @PayloadRoot(localPart = "getSupportedFormat", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/BaseServiceTypes")
  @ResponsePayload
  public GetSupportedFormatResponse getSupportedFormat(@RequestPayload GetSupportedFormat request) {
    GetSupportedFormatResponse response = new GetSupportedFormatResponse();
    response.setReturn(this.codeSystemVersionHistoryService.getSupportedFormatList());
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
