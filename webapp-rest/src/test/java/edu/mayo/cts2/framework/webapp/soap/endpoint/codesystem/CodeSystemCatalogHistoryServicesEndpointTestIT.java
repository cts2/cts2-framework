package edu.mayo.cts2.framework.webapp.soap.endpoint.codesystem;

import edu.mayo.cts2.framework.model.core.FormatReference;
import edu.mayo.cts2.framework.model.core.NamespaceReference;
import edu.mayo.cts2.framework.model.core.OpaqueData;
import edu.mayo.cts2.framework.model.core.SourceReference;
import edu.mayo.cts2.framework.model.service.core.DocumentedNamespaceReference;
import edu.mayo.cts2.framework.model.util.ModelUtils;
import edu.mayo.cts2.framework.model.wsdl.baseservice.*;
import edu.mayo.cts2.framework.model.wsdl.codesystemhistory.*;
import edu.mayo.cts2.framework.webapp.service.MockServiceProvider;
import edu.mayo.cts2.framework.webapp.soap.endpoint.MockBaseHistoryService;
import edu.mayo.cts2.framework.webapp.soap.endpoint.MockBaseService;
import edu.mayo.cts2.framework.webapp.soap.endpoint.SoapEndpointTestBase;
import org.apache.commons.lang.ArrayUtils;
import org.junit.Test;

import static org.junit.Assert.*;

public class CodeSystemCatalogHistoryServicesEndpointTestIT extends SoapEndpointTestBase {

  String uri = "http://localhost:8081/webapp-rest/soap/service/CodeSystemCatalogHistoryService";

  @Test
  public void TestGetEarliestChangeFor() throws Exception {
    MockServiceProvider.cts2Service = new MockService();
    GetEarliestChangeFor request = new GetEarliestChangeFor();
    request.setCodeSystem(ModelUtils.nameOrUriFromName("test"));
    GetEarliestChangeForResponse response = (GetEarliestChangeForResponse) this.doSoapCall(uri, request);
    assertEquals("success", response.getReturn().getCodeSystemName());
  }

  @Test
  public void TestGetLastChangeFor() throws Exception {
    MockServiceProvider.cts2Service = new MockService();
    GetLastChangeFor request = new GetLastChangeFor();
    request.setCodeSystem(ModelUtils.nameOrUriFromName("test"));
    GetLastChangeForResponse response = (GetLastChangeForResponse) this.doSoapCall(uri, request);
    assertEquals("success", response.getReturn().getCodeSystemName());
  }

  @Test
  public void TestGetChangeHistoryFor() throws Exception {
//    MockServiceProvider.cts2Service = new MockService();
//    GetChangeHistoryFor request = new GetChangeHistoryFor();
//    request.setCodeSystem(ModelUtils.nameOrUriFromName("test"));
//    GetChangeHistoryForResponse response = (GetChangeHistoryForResponse) this.doSoapCall(uri, request);

    fail("not implemented");
  }

  /* BaseHistoryServices */
  @Test
  public void TestReadChangeSet() throws Exception {
//    MockServiceProvider.cts2Service = new MockService();
//    ReadChangeSet request = new ReadChangeSet();
//    request.setURI("test");
//    ReadChangeSetResponse response = (ReadChangeSetResponse) this.doSoapCall(uri, request);

    fail("not implemented");
  }

  @Test
  public void TestResolve() throws Exception {
    fail("not implemented");
  }

  /* BaseQueryServices */
  /* TODO: Write Tests For: BaseQueryServices */
  @Test
  public void TestUnion() throws Exception {
    fail("not implemented");
  }

  @Test
  public void TestRestrict() throws Exception {
    fail("not implemented");
  }

  @Test
  public void TestIntersect() throws Exception {
    fail("not implemented");
  }

  @Test
  public void TestDifference() throws Exception {
    fail("not implemented");
  }

  @Test
  public void TestCount() throws Exception {
    fail("not implemented");
  }

  @Test
  public void TestGetSupportedMatchAlgorithm() throws Exception {
    fail("not implemented");
  }

  @Test
  public void TestGetSupportedModelAttribute() throws Exception {
    fail("not implemented");
  }

  @Test
  public void TestGetKnownProperty() throws Exception {
    fail("not implemented");
  }

  /* BaseServices */
  @Test
  public void TestGetServiceName() throws Exception {
    MockServiceProvider.cts2Service = new MockService();
    GetServiceName request = new GetServiceName();
    GetServiceNameResponse response = (GetServiceNameResponse) this.doSoapCall(uri, request);
    assertEquals(MockBaseService.class.getName(), response.getReturn());
  }

  @Test
  public void TestGetServiceVersion() throws Exception {
    MockServiceProvider.cts2Service = new MockService();
    GetServiceVersion request = new GetServiceVersion();
    GetServiceVersionResponse response = (GetServiceVersionResponse) this.doSoapCall(uri, request);
    assertEquals("1.23.4", response.getReturn());
  }

  @Test
  public void TestGetDefaultFormat() throws Exception {
    MockServiceProvider.cts2Service = new MockService();
    GetDefaultFormat request = new GetDefaultFormat();
    GetDefaultFormatResponse response = (GetDefaultFormatResponse) this.doSoapCall(uri, request);
    assertEquals(new FormatReference("testRef"), response.getReturn());
  }

  @Test
  public void TestGetServiceProvider() throws Exception {
    MockServiceProvider.cts2Service = new MockService();
    GetServiceProvider request = new GetServiceProvider();
    GetServiceProviderResponse response = (GetServiceProviderResponse) this.doSoapCall(uri, request);
    assertEquals(new SourceReference("test ref"), response.getReturn());
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
  public void TestGetKnownNamespaceList() throws Exception {
    MockServiceProvider.cts2Service = new MockService();
    GetKnownNamespace request = new GetKnownNamespace();
    GetKnownNamespaceResponse response = (GetKnownNamespaceResponse) this.doSoapCall(uri, request);
    NamespaceReference[] namespaceReferences = response.getReturn();
    assertEquals(3, namespaceReferences.length);
    assertTrue(ArrayUtils.contains(namespaceReferences, new DocumentedNamespaceReference("ns1")));
    assertTrue(ArrayUtils.contains(namespaceReferences, new DocumentedNamespaceReference("ns2")));
    assertTrue(ArrayUtils.contains(namespaceReferences, new DocumentedNamespaceReference("ns3")));
  }

  @Test
  public void TestGetSupportedFormatList() throws Exception {
    MockServiceProvider.cts2Service = new MockService();
    GetSupportedFormat request = new GetSupportedFormat();
    GetSupportedFormatResponse response = (GetSupportedFormatResponse) this.doSoapCall(uri, request);
    FormatReference[] formatReferences = response.getReturn();
    assertEquals(3, formatReferences.length);
    assertTrue(ArrayUtils.contains(formatReferences, new FormatReference("fr1")));
    assertTrue(ArrayUtils.contains(formatReferences, new FormatReference("fr2")));
    assertTrue(ArrayUtils.contains(formatReferences, new FormatReference("fr3")));
  }

  private class MockService extends MockBaseHistoryService {

  }

}
