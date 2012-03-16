package edu.mayo.cts2.framework.webapp.soap.endpoint.conceptdomain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang.ArrayUtils;
import org.junit.Test;

import edu.mayo.cts2.framework.model.command.ResolvedReadContext;
import edu.mayo.cts2.framework.model.conceptdomain.ConceptDomainCatalogEntry;
import edu.mayo.cts2.framework.model.core.NamespaceReference;
import edu.mayo.cts2.framework.model.core.OpaqueData;
import edu.mayo.cts2.framework.model.core.ScopedEntityName;
import edu.mayo.cts2.framework.model.core.SourceReference;
import edu.mayo.cts2.framework.model.service.core.DocumentedNamespaceReference;
import edu.mayo.cts2.framework.model.service.core.EntityNameOrURI;
import edu.mayo.cts2.framework.model.service.core.NameOrURI;
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
import edu.mayo.cts2.framework.model.wsdl.conceptdomainread.Exists;
import edu.mayo.cts2.framework.model.wsdl.conceptdomainread.ExistsDefiningEntity;
import edu.mayo.cts2.framework.model.wsdl.conceptdomainread.ExistsDefiningEntityResponse;
import edu.mayo.cts2.framework.model.wsdl.conceptdomainread.ExistsResponse;
import edu.mayo.cts2.framework.model.wsdl.conceptdomainread.Read;
import edu.mayo.cts2.framework.model.wsdl.conceptdomainread.ReadByDefiningEntity;
import edu.mayo.cts2.framework.model.wsdl.conceptdomainread.ReadByDefiningEntityResponse;
import edu.mayo.cts2.framework.model.wsdl.conceptdomainread.ReadResponse;
import edu.mayo.cts2.framework.service.profile.conceptdomain.ConceptDomainReadService;
import edu.mayo.cts2.framework.webapp.service.MockServiceProvider;
import edu.mayo.cts2.framework.webapp.soap.endpoint.MockBaseService;
import edu.mayo.cts2.framework.webapp.soap.endpoint.SoapEndpointTestBase;

public class ConceptDomainCatalogReadServicesEndpointTestIT extends SoapEndpointTestBase {

  String uri = "http://localhost:8081/webapp-rest/soap/service/ConceptDomainCatalogReadService";

  @Test
  public void TestRead() throws Exception {
    MockServiceProvider.cts2Service = new MockService();
    Read readRequest = new Read();
    readRequest.setConceptDomainId(ModelUtils.nameOrUriFromName("test"));
    readRequest.setContext(new ReadContext());
    ReadResponse response = (ReadResponse) this.doSoapCall(uri, readRequest);
    assertEquals("success", response.getReturn().getConceptDomainName());
  }

  @Test
  public void TestExists() throws Exception {
    MockServiceProvider.cts2Service = new MockService();
    Exists existsRequest = new Exists();
    existsRequest.setConceptDomainId(ModelUtils.nameOrUriFromName("test"));
    existsRequest.setContext(new ReadContext());
    ExistsResponse response = (ExistsResponse) this.doSoapCall(uri, existsRequest);
    assertTrue(response.getReturn());
  }

  @Test
  public void TestExistsDefiningEntity() throws Exception {
    MockServiceProvider.cts2Service = new MockService();
    ExistsDefiningEntity request = new ExistsDefiningEntity();
    ScopedEntityName name = new ScopedEntityName();
    name.setName("test");
    name.setNamespace("testNamespace");
    request.setEntity(ModelUtils.entityNameOrUriFromName(name));
    request.setContext(new ReadContext());
    ExistsDefiningEntityResponse response = (ExistsDefiningEntityResponse) this.doSoapCall(uri, request);
    assertTrue(response.getReturn());
  }

  @Test
  public void TestReadByDefiningEntity() throws Exception {
    MockServiceProvider.cts2Service = new MockService();
    ReadByDefiningEntity request = new ReadByDefiningEntity();
    ScopedEntityName name = new ScopedEntityName();
    name.setName("test");
    name.setNamespace("testNamespace");
    request.setEntity(ModelUtils.entityNameOrUriFromName(name));
    QueryControl queryControl = new QueryControl();
    queryControl.setTimeLimit(10000L);
    request.setQueryControl(queryControl);
    request.setContext(new ReadContext());
    ReadByDefiningEntityResponse response = (ReadByDefiningEntityResponse) this.doSoapCall(uri, request);
    assertEquals("success", response.getReturn().getConceptDomainName());
  }
  
  /*******************************************************/
  /*                    Base Services                    */
  /*******************************************************/
  @Test
  public void TestGetServiceName() throws Exception {
    MockServiceProvider.cts2Service = new MockService();
    GetServiceName request = new GetServiceName();
    GetServiceNameResponse response = (GetServiceNameResponse) this.doSoapCall(uri, request);
    assertEquals(MockBaseService.class.getName(), response.getReturn());
  }

  @Test
  public void TestGetServiceDescription() throws Exception {
    MockServiceProvider.cts2Service = new MockService();
    GetServiceDescription request = new GetServiceDescription();
    GetServiceDescriptionResponse response = (GetServiceDescriptionResponse) this.doSoapCall(uri, request);
    OpaqueData expected = new OpaqueData();
    expected.setValue(ModelUtils.toTsAnyType("test desc"));
    assertEquals(expected, response.getReturn());
  }

  @Test
  public void TestGetServiceProvider() throws Exception {
    MockServiceProvider.cts2Service = new MockService();
    GetServiceProvider request = new GetServiceProvider();
    GetServiceProviderResponse response = (GetServiceProviderResponse) this.doSoapCall(uri, request);
    assertEquals(new SourceReference("test ref"), response.getReturn());
  }

  @Test
  public void TestGetServiceVersion() throws Exception {
    MockServiceProvider.cts2Service = new MockService();
    GetServiceVersion request = new GetServiceVersion();
    GetServiceVersionResponse response = (GetServiceVersionResponse) this.doSoapCall(uri, request);
    assertEquals("1.23.4", response.getReturn());
  }

  @Test
  public void TestGetSupportedFormat() throws Exception {
    MockServiceProvider.cts2Service = new MockService();
    GetSupportedFormat request = new GetSupportedFormat();
    GetSupportedFormatResponse response = (GetSupportedFormatResponse) this.doSoapCall(uri, request);
    assertEquals("SOAP", response.getReturn(0).getContent());
  }

  @Test
  public void TestGetDefaultFormat() throws Exception {
    MockServiceProvider.cts2Service = new MockService();
    GetDefaultFormat request = new GetDefaultFormat();
    GetDefaultFormatResponse response = (GetDefaultFormatResponse) this.doSoapCall(uri, request);
    assertEquals("SOAP", response.getReturn().getContent());
  }

  @Test
  public void TestGetSupportedProfile() throws Exception {
    MockServiceProvider.cts2Service = new MockService();
    GetSupportedProfile request = new GetSupportedProfile();
    GetSupportedProfileResponse response = (GetSupportedProfileResponse) this.doSoapCall(uri, request);
    ProfileElement profile = response.getReturn()[0];
    assertEquals(StructuralProfile.SP_CONCEPT_DOMAIN, profile.getStructuralProfile());
    assertEquals(FunctionalProfile.FP_READ, profile.getFunctionalProfile(0));
  }

  @Test
  public void TestGetImplementationType() throws Exception {
    MockServiceProvider.cts2Service = new MockService();
    GetImplementationType request = new GetImplementationType();
    GetImplementationTypeResponse response = (GetImplementationTypeResponse) this.doSoapCall(uri, request);
    assertEquals(ImplementationProfile.IP_SOAP, response.getReturn(0));
  }

  @Test
  public void TestGetKnownNamespace() throws Exception {
    MockServiceProvider.cts2Service = new MockService();
    GetKnownNamespace request = new GetKnownNamespace();
    GetKnownNamespaceResponse response = (GetKnownNamespaceResponse) this.doSoapCall(uri, request);
    NamespaceReference[] namespaceReferences = response.getReturn();
    assertEquals(3, namespaceReferences.length);
    assertTrue(ArrayUtils.contains(namespaceReferences, new DocumentedNamespaceReference("ns1")));
    assertTrue(ArrayUtils.contains(namespaceReferences, new DocumentedNamespaceReference("ns2")));
    assertTrue(ArrayUtils.contains(namespaceReferences, new DocumentedNamespaceReference("ns3")));
  }

  /********************************************************************************************************************/
  /*                                                                                                                  */
  /*                                  Mock Code System Catalog Read Service Class                                     */
  /*                                                                                                                  */
  /********************************************************************************************************************/
  private class MockService extends MockBaseService implements ConceptDomainReadService {

    public boolean existsDefiningEntity(EntityNameOrURI entity, ResolvedReadContext context) {
      return entity.getEntityName().getName().equals("test") && context != null;
    }
    
    public ConceptDomainCatalogEntry readByDefiningEntity(
        EntityNameOrURI entity,
        ResolvedReadContext context) {
      ConceptDomainCatalogEntry entry = new ConceptDomainCatalogEntry();
      entry.setAbout("test.about");

      if (entity.getEntityName().getName().equals("test") && context != null) {
        entry.setConceptDomainName("success");
      }
      else {
        entry.setConceptDomainName("fail");
      }

      return entry;
    }
    
    public ConceptDomainCatalogEntry read(NameOrURI identifier, ResolvedReadContext readContext) {
      ConceptDomainCatalogEntry entry = new ConceptDomainCatalogEntry();
      entry.setAbout("test about");

      if (identifier.getName().equals("test") && readContext != null) {
        entry.setConceptDomainName("success");
      }
      else {
        entry.setConceptDomainName("fail");
      }

      return entry;
    }

    public boolean exists(NameOrURI identifier, ResolvedReadContext readContext) {
      return identifier.getName().equals("test") && readContext != null;
    }
  }
}
