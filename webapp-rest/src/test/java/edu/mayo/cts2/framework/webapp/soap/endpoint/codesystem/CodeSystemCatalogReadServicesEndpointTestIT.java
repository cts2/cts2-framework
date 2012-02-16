package edu.mayo.cts2.framework.webapp.soap.endpoint.codesystem;

import edu.mayo.cts2.framework.model.codesystem.CodeSystemCatalogEntry;
import edu.mayo.cts2.framework.model.command.ResolvedReadContext;
import edu.mayo.cts2.framework.model.core.FormatReference;
import edu.mayo.cts2.framework.model.core.NamespaceReference;
import edu.mayo.cts2.framework.model.core.OpaqueData;
import edu.mayo.cts2.framework.model.core.SourceReference;
import edu.mayo.cts2.framework.model.service.core.DocumentedNamespaceReference;
import edu.mayo.cts2.framework.model.service.core.NameOrURI;
import edu.mayo.cts2.framework.model.service.core.ReadContext;
import edu.mayo.cts2.framework.model.util.ModelUtils;
import edu.mayo.cts2.framework.model.wsdl.baseservice.*;
import edu.mayo.cts2.framework.model.wsdl.codesystemread.Exists;
import edu.mayo.cts2.framework.model.wsdl.codesystemread.ExistsResponse;
import edu.mayo.cts2.framework.model.wsdl.codesystemread.Read;
import edu.mayo.cts2.framework.model.wsdl.codesystemread.ReadResponse;
import edu.mayo.cts2.framework.service.profile.codesystem.CodeSystemReadService;
import edu.mayo.cts2.framework.webapp.service.MockServiceProvider;
import edu.mayo.cts2.framework.webapp.soap.endpoint.SoapEndpointTestBase;
import org.apache.commons.lang.ArrayUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CodeSystemCatalogReadServicesEndpointTestIT extends SoapEndpointTestBase {

  String uri = "http://localhost:8081/webapp-rest/soap/service/CodeSystemCatalogReadService";

  @Test
  public void TestRead() throws Exception {
    MockServiceProvider.cts2Service = new MockCodeSystemReadService();
    Read readRequest = new Read();
    readRequest.setCodeSystemId(ModelUtils.nameOrUriFromName("test"));
    ReadResponse response = (ReadResponse) this.doSoapCall(uri, readRequest);
    assertEquals(response.getReturn().getCodeSystemName(), "test");
  }

  @Test
  public void TestExists() throws Exception {
    MockServiceProvider.cts2Service = new MockCodeSystemReadService();
    Exists existsRequest = new Exists();
    existsRequest.setCodeSystemId(ModelUtils.nameOrUriFromName("test"));
    ExistsResponse response = (ExistsResponse) this.doSoapCall(uri, existsRequest);
    assertTrue(response.getReturn());
  }

  @Test
  public void TestGetServiceName() throws Exception {
    MockServiceProvider.cts2Service = new MockCodeSystemReadService();
    GetServiceName request = new GetServiceName();
    GetServiceNameResponse response = (GetServiceNameResponse) this.doSoapCall(uri, request);
    assertEquals(CodeSystemCatalogReadServicesEndpointTestIT.class.getName(), response.getReturn());
  }

  @Test
  public void TestGetServiceVersion() throws Exception {
    MockServiceProvider.cts2Service = new MockCodeSystemReadService();
    GetServiceVersion request = new GetServiceVersion();
    GetServiceVersionResponse response = (GetServiceVersionResponse) this.doSoapCall(uri, request);
    assertEquals("1.23.4", response.getReturn());
  }

  @Test
  public void TestGetDefaultFormat() throws Exception {
    MockServiceProvider.cts2Service = new MockCodeSystemReadService();
    GetDefaultFormat request = new GetDefaultFormat();
    GetDefaultFormatResponse response = (GetDefaultFormatResponse) this.doSoapCall(uri, request);
    assertEquals(new FormatReference("testRef"), response.getReturn());
  }

  @Test
  public void TestGetServiceProvider() throws Exception {
    MockServiceProvider.cts2Service = new MockCodeSystemReadService();
    GetServiceProvider request = new GetServiceProvider();
    GetServiceProviderResponse response = (GetServiceProviderResponse) this.doSoapCall(uri, request);
    assertEquals(new SourceReference("test ref"), response.getReturn());
  }

  @Test
  public void TestGetServiceDescription() throws Exception {
    MockServiceProvider.cts2Service = new MockCodeSystemReadService();
    GetServiceDescription request = new GetServiceDescription();
    GetServiceDescriptionResponse response = (GetServiceDescriptionResponse) this.doSoapCall(uri, request);
    OpaqueData expected = new OpaqueData();
    expected.setValue(ModelUtils.toTsAnyType("test desc"));
    assertEquals(expected, response.getReturn());
  }

  @Test
  public void TestGetKnownNamespaceList() throws Exception {
    MockServiceProvider.cts2Service = new MockCodeSystemReadService();
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
    MockServiceProvider.cts2Service = new MockCodeSystemReadService();
    GetSupportedFormat request = new GetSupportedFormat();
    GetSupportedFormatResponse response = (GetSupportedFormatResponse) this.doSoapCall(uri, request);
    FormatReference[] formatReferences = response.getReturn();
    assertEquals(3, formatReferences.length);
    assertTrue(ArrayUtils.contains(formatReferences, new FormatReference("fr1")));
    assertTrue(ArrayUtils.contains(formatReferences, new FormatReference("fr2")));
    assertTrue(ArrayUtils.contains(formatReferences, new FormatReference("fr3")));
  }

  protected class MockCodeSystemReadService implements CodeSystemReadService {

    public CodeSystemCatalogEntry read(NameOrURI identifier,
                                       ResolvedReadContext readContext) {
      CodeSystemCatalogEntry entry = new CodeSystemCatalogEntry();
      entry.setCodeSystemName("test");
      entry.setAbout("testAbout");
      return entry;
    }

    public boolean exists(NameOrURI identifier, ReadContext readContext) {
      return identifier.getName().equals("test");
    }

    public String getServiceName() {
      return CodeSystemCatalogReadServicesEndpointTestIT.class.getName();
    }

    public OpaqueData getServiceDescription() {
      OpaqueData opaqueData = new OpaqueData();
      opaqueData.setValue(ModelUtils.toTsAnyType("test desc"));
      return opaqueData;
    }

    public String getServiceVersion() {
      return "1.23.4";
    }

    public SourceReference getServiceProvider() {
      return new SourceReference("test ref");
    }

    public List<FormatReference> getSupportedFormatList() {
      List<FormatReference> formatReferences = new ArrayList<FormatReference>(3);
      formatReferences.add(new FormatReference("fr1"));
      formatReferences.add(new FormatReference("fr2"));
      formatReferences.add(new FormatReference("fr3"));
      return formatReferences;
    }

    public FormatReference getDefaultFormat() {
      return new FormatReference("testRef");
    }

    public List<DocumentedNamespaceReference> getKnownNamespaceList() {
      List<DocumentedNamespaceReference> namespaceReferences = new ArrayList<DocumentedNamespaceReference>(3);
      namespaceReferences.add(new DocumentedNamespaceReference("ns1"));
      namespaceReferences.add(new DocumentedNamespaceReference("ns2"));
      namespaceReferences.add(new DocumentedNamespaceReference("ns3"));
      return namespaceReferences;
    }
  }

}

