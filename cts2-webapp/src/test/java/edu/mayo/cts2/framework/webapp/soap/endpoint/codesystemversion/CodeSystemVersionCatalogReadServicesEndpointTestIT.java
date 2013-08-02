package edu.mayo.cts2.framework.webapp.soap.endpoint.codesystemversion;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.junit.Test;

import edu.mayo.cts2.framework.model.codesystemversion.CodeSystemVersionCatalogEntry;
import edu.mayo.cts2.framework.model.command.ResolvedReadContext;
import edu.mayo.cts2.framework.model.core.CodeSystemReference;
import edu.mayo.cts2.framework.model.core.NamespaceReference;
import edu.mayo.cts2.framework.model.core.OpaqueData;
import edu.mayo.cts2.framework.model.core.SourceAndNotation;
import edu.mayo.cts2.framework.model.core.SourceReference;
import edu.mayo.cts2.framework.model.core.VersionTagReference;
import edu.mayo.cts2.framework.model.service.core.DocumentedNamespaceReference;
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
import edu.mayo.cts2.framework.model.wsdl.codesystemversionread.Exists;
import edu.mayo.cts2.framework.model.wsdl.codesystemversionread.ExistsCodeSystemVersionForCodeSystem;
import edu.mayo.cts2.framework.model.wsdl.codesystemversionread.ExistsCodeSystemVersionForCodeSystemResponse;
import edu.mayo.cts2.framework.model.wsdl.codesystemversionread.ExistsResponse;
import edu.mayo.cts2.framework.model.wsdl.codesystemversionread.GetCodeSystemByVersionId;
import edu.mayo.cts2.framework.model.wsdl.codesystemversionread.GetCodeSystemByVersionIdResponse;
import edu.mayo.cts2.framework.model.wsdl.codesystemversionread.GetCodeSystemVersionForCodeSystem;
import edu.mayo.cts2.framework.model.wsdl.codesystemversionread.GetCodeSystemVersionForCodeSystemResponse;
import edu.mayo.cts2.framework.model.wsdl.codesystemversionread.GetSupportedTag;
import edu.mayo.cts2.framework.model.wsdl.codesystemversionread.GetSupportedTagResponse;
import edu.mayo.cts2.framework.model.wsdl.codesystemversionread.Read;
import edu.mayo.cts2.framework.model.wsdl.codesystemversionread.ReadResponse;
import edu.mayo.cts2.framework.service.profile.codesystemversion.CodeSystemVersionReadService;
import edu.mayo.cts2.framework.webapp.service.MockServiceProvider;
import edu.mayo.cts2.framework.webapp.soap.endpoint.MockBaseService;
import edu.mayo.cts2.framework.webapp.soap.endpoint.SoapEndpointTestBase;

public class CodeSystemVersionCatalogReadServicesEndpointTestIT extends SoapEndpointTestBase {

  String uri = "http://localhost:8081/webapp-rest/soap/service/CodeSystemVersionCatalogReadService";

  @Test
  public void TestRead() throws Exception {
    MockServiceProvider.cts2Service = new MockService();
    Read readRequest = new Read();
    readRequest.setCodeSystemVersion(ModelUtils.nameOrUriFromName("test"));
    readRequest.setContext(new ReadContext());
    ReadResponse response = (ReadResponse) this.doSoapCall(uri, readRequest);
    assertEquals("success", response.getReturn().getCodeSystemVersionName());
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
    request.setTag(ModelUtils.nameOrUriFromName("tag1"));
    ExistsCodeSystemVersionForCodeSystemResponse
        response = (ExistsCodeSystemVersionForCodeSystemResponse) this.doSoapCall(uri, request);
    assertTrue(response.getReturn());
  }

  @Test
  public void TestGetCodeSystemVersionForCodeSystem() throws Exception {
    MockServiceProvider.cts2Service = new MockService();
    GetCodeSystemVersionForCodeSystem request = new GetCodeSystemVersionForCodeSystem();
    request.setCodeSystem(ModelUtils.nameOrUriFromName("test"));
    request.setQueryControl(new QueryControl());
    request.setContext(new ReadContext());
    request.setTag(ModelUtils.nameOrUriFromName("tag1"));
    GetCodeSystemVersionForCodeSystemResponse
        response = (GetCodeSystemVersionForCodeSystemResponse) this.doSoapCall(uri, request);
    assertEquals(
    		"success", response.getReturn().getCodeSystemVersionName());
  }

  @Test
  public void TestGetCodeSystemByVersionId() throws Exception {
    MockServiceProvider.cts2Service = new MockService();
    GetCodeSystemByVersionId request = new GetCodeSystemByVersionId();
    request.setCodeSystem(ModelUtils.nameOrUriFromName("test"));
    request.setOfficialResourceVersionId("TEST_VERSION");
    request.setReadContext(new ReadContext());
    GetCodeSystemByVersionIdResponse response = (GetCodeSystemByVersionIdResponse) this.doSoapCall(uri, request);
    assertEquals("success", response.getReturn().getCodeSystemVersionName());
  }
  
  @Test
  public void TestGetSupportedTag() throws Exception {
    MockServiceProvider.cts2Service = new MockService();
    GetSupportedTag request = new GetSupportedTag();
    GetSupportedTagResponse response = (GetSupportedTagResponse) this.doSoapCall(uri, request);
    VersionTagReference[] versionTagReferences = response.getReturn();
    assertEquals(3, versionTagReferences.length);
    assertTrue(ArrayUtils.contains(versionTagReferences, new VersionTagReference("tag1")));
    assertTrue(ArrayUtils.contains(versionTagReferences, new VersionTagReference("tag2")));
    assertTrue(ArrayUtils.contains(versionTagReferences, new VersionTagReference("tag3")));
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
    assertEquals(StructuralProfile.SP_CODE_SYSTEM_VERSION, profile.getStructuralProfile());
    assertEquals(FunctionalProfile.FP_READ.name(), profile.getFunctionalProfile()[0].getContent());
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
  /*                                   Mock Code System Version Read Service Class                                    */
  /*                                                                                                                  */
  /********************************************************************************************************************/
  private class MockService extends MockBaseService implements CodeSystemVersionReadService {

    public CodeSystemVersionCatalogEntry read(NameOrURI identifier, ResolvedReadContext readContext) {
      CodeSystemVersionCatalogEntry entry = getMockEntry();

      if (identifier.getName().equals("test")) {
        entry.setCodeSystemVersionName("success");
      }
      else {
        entry.setCodeSystemVersionName("fail");
      }
      return entry;
    }

    public boolean exists(NameOrURI identifier, ResolvedReadContext readContext) {
      boolean exists = identifier.getName().equals("test") && readContext != null;
      return exists;
    }

    @Override
    public boolean existsByTag(NameOrURI codeSystem, VersionTagReference tag, ResolvedReadContext readContext) {
      boolean exists = codeSystem.getName().equals("test") && tag.getContent().equals("tag1");
      return exists;
    }

    @Override
    public CodeSystemVersionCatalogEntry readByTag(NameOrURI codeSystem, VersionTagReference tag, ResolvedReadContext readContext) {
      CodeSystemVersionCatalogEntry entry = getMockEntry();

      if (codeSystem.getName().equals("test") && tag.getContent().equals("tag1")) {
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
    public CodeSystemVersionCatalogEntry getCodeSystemByVersionId(NameOrURI codeSystem, String officialResourceVersionId, ResolvedReadContext readContext) {
      CodeSystemVersionCatalogEntry entry = getMockEntry();

      if (codeSystem.getName().equals("test") && officialResourceVersionId.equals("TEST_VERSION") && readContext != null) {
        entry.setCodeSystemVersionName("success");
      }
      else {
        entry.setCodeSystemVersionName("fail");
      }
      return entry;
    }

    public List<VersionTagReference> getSupportedTags() {
      List<VersionTagReference> tags = new ArrayList<VersionTagReference>(3);
      tags.add(new VersionTagReference("tag1"));
      tags.add(new VersionTagReference("tag2"));
      tags.add(new VersionTagReference("tag3"));
      return tags;
    }

    private CodeSystemVersionCatalogEntry getMockEntry() {
      CodeSystemVersionCatalogEntry entry = new CodeSystemVersionCatalogEntry();
      entry.setOfficialResourceVersionId("TEST_VERSION");
      entry.setAbout("aboutTest");
      entry.setSourceAndNotation(new SourceAndNotation());
      entry.setDocumentURI("testURI");
      entry.setVersionOf(new CodeSystemReference("testRef"));
      return entry;
    }

  }

}
