package edu.mayo.cts2.framework.webapp.soap.endpoint.codesystemversion;

import edu.mayo.cts2.framework.model.codesystemversion.CodeSystemVersionCatalogEntry;
import edu.mayo.cts2.framework.model.command.ResolvedReadContext;
import edu.mayo.cts2.framework.model.core.FormatReference;
import edu.mayo.cts2.framework.model.core.NamespaceReference;
import edu.mayo.cts2.framework.model.core.OpaqueData;
import edu.mayo.cts2.framework.model.core.SourceReference;
import edu.mayo.cts2.framework.model.service.core.DocumentedNamespaceReference;
import edu.mayo.cts2.framework.model.service.core.NameOrURI;
import edu.mayo.cts2.framework.model.service.core.QueryControl;
import edu.mayo.cts2.framework.model.service.core.ReadContext;
import edu.mayo.cts2.framework.model.util.ModelUtils;
import edu.mayo.cts2.framework.model.wsdl.baseservice.*;
import edu.mayo.cts2.framework.model.wsdl.codesystemversionread.*;
import edu.mayo.cts2.framework.service.profile.codesystemversion.CodeSystemVersionReadService;
import edu.mayo.cts2.framework.webapp.service.MockServiceProvider;
import edu.mayo.cts2.framework.webapp.soap.endpoint.MockBaseService;
import edu.mayo.cts2.framework.webapp.soap.endpoint.SoapEndpointTestBase;
import org.apache.commons.lang.ArrayUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CodeSystemVersionCatalogReadServicesEndpointTestIT extends SoapEndpointTestBase {

  String uri = "http://localhost:8081/webapp-rest/soap/service/CodeSystemVersionCatalogReadService";

  @Test
  public void TestRead() throws Exception {
    MockServiceProvider.cts2Service = new MockService();
    Read readRequest = new Read();
    readRequest.setCodeSystemVersion(ModelUtils.nameOrUriFromName("test"));
    readRequest.setQueryControl(new QueryControl());
    readRequest.setContext(new ReadContext());
    ReadResponse response = (ReadResponse) this.doSoapCall(uri, readRequest);
    assertEquals(response.getReturn().getCodeSystemVersionName(), "test");
  }

  @Test
  public void TestExists() throws Exception {
    MockServiceProvider.cts2Service = new MockService();
    Exists existsRequest = new Exists();
    existsRequest.setCodeSystemVersion(ModelUtils.nameOrUriFromName("test"));
    existsRequest.setContext(new ReadContext());
    ExistsResponse response = (ExistsResponse) this.doSoapCall(uri, existsRequest);
    assertTrue(response.getReturn());
  }

  @Test
  public void TestExistsCodeSystemVersionForCodeSystem() throws Exception {
    MockServiceProvider.cts2Service = new MockService();
    ExistsCodeSystemVersionForCodeSystem request = new ExistsCodeSystemVersionForCodeSystem();
    request.setCodeSystem(ModelUtils.nameOrUriFromName("test"));
    request.setTag(ModelUtils.nameOrUriFromName("testTag"));
    ExistsCodeSystemVersionForCodeSystemResponse response = (ExistsCodeSystemVersionForCodeSystemResponse) this.doSoapCall(uri, request);
    assertTrue(response.getReturn());
  }

  @Test
  public void TestGetCodeSystemVersionForCodeSystem() throws Exception {
    MockServiceProvider.cts2Service = new MockService();
    GetCodeSystemVersionForCodeSystem request = new GetCodeSystemVersionForCodeSystem();
    request.setCodeSystem(ModelUtils.nameOrUriFromName("test"));
    request.setQueryControl(new QueryControl());
    request.setContext(new ReadContext());
    CodeSystemVersionCatalogEntry expected = new CodeSystemVersionCatalogEntry();
    expected.setCodeSystemVersionName("testName");
    GetCodeSystemVersionForCodeSystemResponse response = (GetCodeSystemVersionForCodeSystemResponse) this.doSoapCall(uri, request);
    assertEquals(expected, response.getReturn());
  }

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

  private class MockService extends MockBaseService implements CodeSystemVersionReadService {

    public CodeSystemVersionCatalogEntry read(NameOrURI identifier, ResolvedReadContext readContext) {
      CodeSystemVersionCatalogEntry entry = new CodeSystemVersionCatalogEntry();
      entry.setAbout("testAbout");
      if (identifier.getName().equals("test")) {
        entry.setCodeSystemVersionName("success");
      }
      else {
        entry.setCodeSystemVersionName("fail");
      }
      return entry;
    }

    public boolean exists(NameOrURI identifier, ReadContext readContext) {
      return identifier.getName().equals("test");
    }

    @Override
    public boolean existsCodeSystemVersionForCodeSystem(NameOrURI codeSystem, String tagName) {
      return codeSystem.getName().equals("test") && codeSystem.getName().equals("testTag");
    }

    @Override
    public CodeSystemVersionCatalogEntry getCodeSystemVersionForCodeSystem(NameOrURI codeSystem, String tagName) {
      CodeSystemVersionCatalogEntry entry = new CodeSystemVersionCatalogEntry();
      if (codeSystem.getName().equals("test") && tagName.equals("testTag")) {
        entry.setCodeSystemVersionName("success");
      }
      else {
        entry.setCodeSystemVersionName("fail");
      }
      return entry;
    }

    @Override
    public boolean existsVersionId(NameOrURI codeSystem, String officialResourceVersionId) {
      return codeSystem.getName().equals("test") && officialResourceVersionId.equals("TEST_VERSION");
    }

    @Override
    public CodeSystemVersionCatalogEntry getCodeSystemVersionForCodeSystem(NameOrURI codeSystem, String tagName, ResolvedReadContext readContext) {
      CodeSystemVersionCatalogEntry entry = new CodeSystemVersionCatalogEntry();
      if (codeSystem.getName().equals("test") && tagName.equals("testTag") && readContext != null) {
        entry.setCodeSystemVersionName("success");
      }
      else {
        entry.setCodeSystemVersionName("fail");
      }
      return entry;
    }

    @Override
    public CodeSystemVersionCatalogEntry getCodeSystemByVersionId(NameOrURI codeSystem, String officialResourceVersionId, ResolvedReadContext readContext) {
      CodeSystemVersionCatalogEntry entry = new CodeSystemVersionCatalogEntry();
      if (codeSystem.getName().equals("test") && officialResourceVersionId.equals("TEST_VERSION") && readContext != null) {
        entry.setCodeSystemVersionName("success");
      }
      else {
        entry.setCodeSystemVersionName("fail");
      }
      return entry;
    }
  }

}
