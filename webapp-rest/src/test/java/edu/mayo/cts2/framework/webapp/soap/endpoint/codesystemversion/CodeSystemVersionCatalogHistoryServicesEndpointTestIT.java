package edu.mayo.cts2.framework.webapp.soap.endpoint.codesystemversion;

import edu.mayo.cts2.framework.model.codesystemversion.CodeSystemVersionCatalogEntry;
import edu.mayo.cts2.framework.model.core.CodeSystemReference;
import edu.mayo.cts2.framework.model.core.FormatReference;
import edu.mayo.cts2.framework.model.core.NamespaceReference;
import edu.mayo.cts2.framework.model.core.OpaqueData;
import edu.mayo.cts2.framework.model.core.SourceAndNotation;
import edu.mayo.cts2.framework.model.core.SourceReference;
import edu.mayo.cts2.framework.model.directory.DirectoryResult;
import edu.mayo.cts2.framework.model.service.core.DocumentedNamespaceReference;
import edu.mayo.cts2.framework.model.service.core.NameOrURI;
import edu.mayo.cts2.framework.model.util.ModelUtils;
import edu.mayo.cts2.framework.model.wsdl.baseservice.GetDefaultFormat;
import edu.mayo.cts2.framework.model.wsdl.baseservice.GetDefaultFormatResponse;
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
import edu.mayo.cts2.framework.model.wsdl.codesystemversionhistory.GetEarliestChangeFor;
import edu.mayo.cts2.framework.model.wsdl.codesystemversionhistory.GetEarliestChangeForResponse;
import edu.mayo.cts2.framework.model.wsdl.codesystemversionhistory.GetLastChangeFor;
import edu.mayo.cts2.framework.model.wsdl.codesystemversionhistory.GetLastChangeForResponse;
import edu.mayo.cts2.framework.webapp.service.MockServiceProvider;
import edu.mayo.cts2.framework.webapp.soap.endpoint.MockBaseHistoryService;
import edu.mayo.cts2.framework.webapp.soap.endpoint.MockBaseService;
import edu.mayo.cts2.framework.webapp.soap.endpoint.SoapEndpointTestBase;
import org.apache.commons.lang.ArrayUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class CodeSystemVersionCatalogHistoryServicesEndpointTestIT extends SoapEndpointTestBase {

  String uri = "http://localhost:8081/webapp-rest/soap/service/CodeSystemVersionCatalogHistoryService";

  @Test
  public void TestGetEarliestChangeFor() {
    MockServiceProvider.cts2Service = new MockService();
    GetEarliestChangeFor request = new GetEarliestChangeFor();
    request.setCodeSystemVersion(ModelUtils.nameOrUriFromName("test"));
    GetEarliestChangeForResponse response = (GetEarliestChangeForResponse) this.doSoapCall(uri, request);
    assertEquals("success", response.getReturn().getCodeSystemVersionName());
  }

  @Test
  public void TestGetLastChangeFor() {
    MockServiceProvider.cts2Service = new MockService();
    GetLastChangeFor request = new GetLastChangeFor();
    request.setCodeSystem(ModelUtils.nameOrUriFromName("test"));
    GetLastChangeForResponse response = (GetLastChangeForResponse) this.doSoapCall(uri, request);
    assertEquals("success", response.getReturn().getCodeSystemVersionName());
  }

  @Test
  public void TestGetChangeHistoryFor() {
    fail("not implemented");
  }

  /*******************************************************/
  /*                Base History Services                */
  /*******************************************************/
  /* TODO: Write Tests For: BaseHistoryServices */
  @Test
  public void TestReadChangeSet() throws Exception {
    fail("not implemented");
  }

  @Test
  public void TestResolve() throws Exception {
    fail("not implemented");
  }

  /*******************************************************/
  /*                 Base Query Services                 */
  /*******************************************************/
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

  /********************************************************************************************************************/
  /*                                                                                                                  */
  /*                             Mock Code System Version Catalog History Service Class                               */
  /*                                                                                                                  */
  /********************************************************************************************************************/
  private class MockService extends MockBaseHistoryService {

    @Override
    public Object getEarliestChangeFor(Object identifier) {
      NameOrURI nameOrURI = (NameOrURI) identifier;
      CodeSystemVersionCatalogEntry entry = new CodeSystemVersionCatalogEntry();
      entry.setAbout("This is a test entry");
      entry.setSourceAndNotation(new SourceAndNotation());
      entry.setDocumentURI("testURI");
      entry.setVersionOf(new CodeSystemReference("default"));
      if (nameOrURI.getName().equals("test")) {
        entry.setCodeSystemVersionName("success");
      }
      else {
        entry.setCodeSystemVersionName("fail");
      }
      return entry;
    }

    @Override
    public Object getLastChangeFor(Object identifier) {
      NameOrURI nameOrURI = (NameOrURI) identifier;
      CodeSystemVersionCatalogEntry entry = new CodeSystemVersionCatalogEntry();
      entry.setAbout("This is a test entry");
      entry.setSourceAndNotation(new SourceAndNotation());
      entry.setDocumentURI("testURI");
      entry.setVersionOf(new CodeSystemReference("default"));
      if (nameOrURI.getName().equals("test")) {
        entry.setCodeSystemVersionName("success");
      }
      else {
        entry.setCodeSystemVersionName("fail");
      }
      return entry;
    }

    @Override
    /* TODO: Implement Mock Method: getChangeHistoryFor */
    public DirectoryResult getChangeHistoryFor(Object identifier) {
      throw new UnsupportedOperationException();
    }

  }
}
