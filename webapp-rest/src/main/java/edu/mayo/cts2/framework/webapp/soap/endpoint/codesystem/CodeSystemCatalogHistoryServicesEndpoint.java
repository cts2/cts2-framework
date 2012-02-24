package edu.mayo.cts2.framework.webapp.soap.endpoint.codesystem;

import edu.mayo.cts2.framework.model.codesystem.CodeSystemCatalogEntry;
import edu.mayo.cts2.framework.model.codesystem.CodeSystemCatalogEntryList;
import edu.mayo.cts2.framework.model.service.core.NameOrURI;
import edu.mayo.cts2.framework.model.wsdl.basehistoryservice.*;
import edu.mayo.cts2.framework.model.wsdl.basehistoryservice.ReadChangeSet;
import edu.mayo.cts2.framework.model.wsdl.basehistoryservice.ReadChangeSetResponse;
import edu.mayo.cts2.framework.model.wsdl.baseservice.*;
import edu.mayo.cts2.framework.model.wsdl.codesystemhistory.*;
import edu.mayo.cts2.framework.model.wsdl.codesystemquery.*;
import edu.mayo.cts2.framework.model.wsdl.codesystemquery.Count;
import edu.mayo.cts2.framework.model.wsdl.codesystemquery.CountResponse;
import edu.mayo.cts2.framework.model.wsdl.codesystemquery.Difference;
import edu.mayo.cts2.framework.model.wsdl.codesystemquery.DifferenceResponse;
import edu.mayo.cts2.framework.model.wsdl.codesystemquery.Intersect;
import edu.mayo.cts2.framework.model.wsdl.codesystemquery.IntersectResponse;
import edu.mayo.cts2.framework.model.wsdl.codesystemquery.Restrict;
import edu.mayo.cts2.framework.model.wsdl.codesystemquery.RestrictResponse;
import edu.mayo.cts2.framework.model.wsdl.codesystemquery.Union;
import edu.mayo.cts2.framework.model.wsdl.codesystemquery.UnionResponse;
import edu.mayo.cts2.framework.service.profile.codesystem.CodeSystemHistoryService;
import edu.mayo.cts2.framework.webapp.service.AbstractServiceAwareBean;
import edu.mayo.cts2.framework.webapp.soap.endpoint.AbstractHistoryServiceEndpoint;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint("CodeSystemCatalogHistoryServicesEndpoint")
public class CodeSystemCatalogHistoryServicesEndpoint extends AbstractHistoryServiceEndpoint {

  @AbstractServiceAwareBean.Cts2Service
  private CodeSystemHistoryService codeSystemHistoryService;

  @PayloadRoot(localPart = "getEarliestChangeFor", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/CodeSystemCatalogHistoryServices")
  @ResponsePayload
  public GetEarliestChangeForResponse getEarliestChangeFor(@RequestPayload GetEarliestChangeFor request) {
    GetEarliestChangeForResponse response = new GetEarliestChangeForResponse();
    NameOrURI nameOrURI = request.getCodeSystem();
    CodeSystemCatalogEntry entry = this.codeSystemHistoryService.getEarliestChangeFor(nameOrURI);
    response.setReturn(entry);
    return response;
  }

  @PayloadRoot(localPart = "getLastChangeFor", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/CodeSystemCatalogHistoryServices")
  @ResponsePayload
  public GetLastChangeForResponse getLastChangeFor(@RequestPayload GetLastChangeFor request) {
    GetLastChangeForResponse response = new GetLastChangeForResponse();
    response.setReturn(this.codeSystemHistoryService.getLastChangeFor(request.getCodeSystem()));
    return response;
  }

  /* TODO: Implement Method: getChangeHistoryFor */
  @PayloadRoot(localPart = "getChangeHistoryFor", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/CodeSystemCatalogHistoryServices")
  @ResponsePayload
  public GetChangeHistoryForResponse getChangeHistoryFor(@RequestPayload GetChangeHistoryFor request) {
    throw new UnsupportedOperationException("Method not implemented");
//    GetChangeHistoryForResponse response = new GetChangeHistoryForResponse();
//    CodeSystemCatalogEntryList entryList = this.codeSystemHistoryService.getChangeHistoryFor(request.getCodeSystem(), request.getFromDate(), request.getToDate());
//    response.setResponse(entryList);
//    return response;
  }

  /**********************/
  /* BaseHistoryService */
  /**********************/
  @PayloadRoot(localPart = "getEarliestChange", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/CodeSystemCatalogHistoryServices")
  @ResponsePayload
  public GetEarliestChangeResponse getEarliestChange(@RequestPayload GetEarliestChange request) {
    GetEarliestChangeResponse response = new GetEarliestChangeResponse();
    response.setReturn(this.codeSystemHistoryService.getEarliestChange());
    return response;
  }

  @PayloadRoot(localPart = "getLatestChange", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/CodeSystemCatalogHistoryServices")
  @ResponsePayload
  public GetLatestChangeResponse getLatestChange(@RequestPayload GetLatestChange request) {
    GetLatestChangeResponse response = new GetLatestChangeResponse();
    response.setReturn(this.codeSystemHistoryService.getLatestChange());
    return response;
  }

  /* TODO: Implement Method: getChangeHistory */
  @PayloadRoot(localPart = "getChangeHistory", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/CodeSystemCatalogHistoryServices")
  @ResponsePayload
  public GetChangeHistoryResponse getChangeHistory(@RequestPayload GetChangeHistory request) {
    throw new UnsupportedOperationException("Method not implemented");
//    GetChangeHistoryResponse response = new GetChangeHistoryResponse();
//    response.setReturn(this.codeSystemHistoryService.getChangeHistory());
//    return response;

  }

  @PayloadRoot(localPart = "readChangeSet", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/CodeSystemCatalogHistoryServices")
  @ResponsePayload
  public ReadChangeSetResponse readChangeSet(@RequestPayload ReadChangeSet request) {
    ReadChangeSetResponse response = new ReadChangeSetResponse();
    return response;
  }

  @PayloadRoot(localPart = "resolve", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/CodeSystemCatalogHistoryServices")
  @ResponsePayload
  public ResolveResponse readChangeSet(@RequestPayload Resolve request) {
    ResolveResponse response = new ResolveResponse();
    return response;
  }

  /********************/
  /* BaseQueryService */
  /********************/
  @PayloadRoot(localPart = "count", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/CodeSystemCatalogQueryServices")
  @ResponsePayload
  public CountResponse count(@RequestPayload Count request) {
    CountResponse response = new CountResponse();
    return response;
  }

  @PayloadRoot(localPart = "difference", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/CodeSystemCatalogQueryServices")
  @ResponsePayload
  public DifferenceResponse difference(@RequestPayload Difference request) {
    DifferenceResponse response = new DifferenceResponse();
    return response;
  }

  @PayloadRoot(localPart = "intersect", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/CodeSystemCatalogQueryServices")
  @ResponsePayload
  public IntersectResponse intersect(@RequestPayload Intersect request) {
    IntersectResponse response = new IntersectResponse();
    return response;
  }

  @PayloadRoot(localPart = "restrict", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/CodeSystemCatalogQueryServices")
  @ResponsePayload
  public RestrictResponse restrict(@RequestPayload Restrict request) {
    RestrictResponse response = new RestrictResponse();
    return response;
  }

  @PayloadRoot(localPart = "union", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/CodeSystemCatalogQueryServices")
  @ResponsePayload
  public UnionResponse union(@RequestPayload Union request) {
    UnionResponse response = new UnionResponse();
    return response;
  }

  /***************/
  /* BaseService */
  /***************/
  @PayloadRoot(localPart = "getDefaultFormat", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/BaseServiceTypes")
  @ResponsePayload
  public GetDefaultFormatResponse getDefaultFormat(@RequestPayload GetDefaultFormat request) {
    GetDefaultFormatResponse response = new GetDefaultFormatResponse();
    response.setReturn(this.codeSystemHistoryService.getDefaultFormat());
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
    response.setReturn(this.codeSystemHistoryService.getKnownNamespaceList());
    return response;
  }

  @PayloadRoot(localPart = "getServiceDescription", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/BaseServiceTypes")
  @ResponsePayload
  public GetServiceDescriptionResponse getServiceDescription(@RequestPayload GetServiceDescription request) {
    GetServiceDescriptionResponse response = new GetServiceDescriptionResponse();
    response.setReturn(this.codeSystemHistoryService.getServiceDescription());
    return response;
  }

  @PayloadRoot(localPart = "getServiceName", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/BaseServiceTypes")
  @ResponsePayload
  public GetServiceNameResponse getServiceName(@RequestPayload GetServiceName request) {
    GetServiceNameResponse response = new GetServiceNameResponse();
    response.setReturn(this.codeSystemHistoryService.getServiceName());
    return response;
  }

  @PayloadRoot(localPart = "getServiceProvider", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/BaseServiceTypes")
  @ResponsePayload
  public GetServiceProviderResponse getServiceProvider(@RequestPayload GetServiceProvider request) {
    GetServiceProviderResponse response = new GetServiceProviderResponse();
    response.setReturn(this.codeSystemHistoryService.getServiceProvider());
    return response;
  }

  @PayloadRoot(localPart = "getServiceVersion", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/BaseServiceTypes")
  @ResponsePayload
  public GetServiceVersionResponse getServiceVersion(@RequestPayload GetServiceVersion request) {
    GetServiceVersionResponse response = new GetServiceVersionResponse();
    response.setReturn(this.codeSystemHistoryService.getServiceVersion());
    return response;
  }

  @PayloadRoot(localPart = "getSupportedFormat", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/BaseServiceTypes")
  @ResponsePayload
  public GetSupportedFormatResponse getSupportedFormat(@RequestPayload GetSupportedFormat request) {
    GetSupportedFormatResponse response = new GetSupportedFormatResponse();
    response.setReturn(this.codeSystemHistoryService.getSupportedFormatList());
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
