package edu.mayo.cts2.framework.webapp.soap.endpoint.map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang.ArrayUtils;
import org.junit.Test;

import edu.mayo.cts2.framework.model.command.ResolvedReadContext;
import edu.mayo.cts2.framework.model.core.MapVersionReference;
import edu.mayo.cts2.framework.model.core.NameAndMeaningReference;
import edu.mayo.cts2.framework.model.core.NamespaceReference;
import edu.mayo.cts2.framework.model.core.OpaqueData;
import edu.mayo.cts2.framework.model.core.SourceAndRoleReference;
import edu.mayo.cts2.framework.model.core.SourceReference;
import edu.mayo.cts2.framework.model.core.URIAndEntityName;
import edu.mayo.cts2.framework.model.mapversion.MapEntry;
import edu.mayo.cts2.framework.model.mapversion.types.MapProcessingRule;
import edu.mayo.cts2.framework.model.service.core.DocumentedNamespaceReference;
import edu.mayo.cts2.framework.model.service.core.NameOrURI;
import edu.mayo.cts2.framework.model.service.core.ProfileElement;
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
import edu.mayo.cts2.framework.model.wsdl.mapentryread.Exists;
import edu.mayo.cts2.framework.model.wsdl.mapentryread.ExistsResponse;
import edu.mayo.cts2.framework.model.wsdl.mapentryread.Read;
import edu.mayo.cts2.framework.model.wsdl.mapentryread.ReadResponse;
import edu.mayo.cts2.framework.service.profile.mapentry.MapEntryReadService;
import edu.mayo.cts2.framework.service.profile.mapentry.name.MapEntryReadId;
import edu.mayo.cts2.framework.webapp.service.MockServiceProvider;
import edu.mayo.cts2.framework.webapp.soap.endpoint.MockBaseService;
import edu.mayo.cts2.framework.webapp.soap.endpoint.SoapEndpointTestBase;

public class MapEntryReadServicesEndpointTestIT extends SoapEndpointTestBase {
  
  String uri = "http://localhost:8081/webapp-rest/soap/service/MapEntryReadService";

  @Test
  public void TestRead() throws Exception {
    MockServiceProvider.cts2Service = new MockService();
    Read request = new Read();
    request.setMapVersion(ModelUtils.nameOrUriFromName("test"));
    request.setMapFrom(ModelUtils.entityNameOrUriFromUri("test.version.uri"));
    request.setContext(new ReadContext());
    ReadResponse response = (ReadResponse) this.doSoapCall(uri, request);
    assertEquals("success", response.getMapEntry().getSource(0).getContent());
  }

  @Test
  public void TestExists() throws Exception {
    MockServiceProvider.cts2Service = new MockService();
    Exists request = new Exists();request.setMapVersion(ModelUtils.nameOrUriFromName("test"));
    request.setMapFrom(ModelUtils.entityNameOrUriFromUri("test.version.uri"));
    request.setContext(new ReadContext());
    ExistsResponse response = (ExistsResponse) this.doSoapCall(uri, request);
    assertTrue(response.getReturn());
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
    assertEquals(StructuralProfile.SP_MAP, profile.getStructuralProfile());
    assertEquals(FunctionalProfile.FP_READ, profile.getFunctionalProfile());
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
  /*                                       Mock Map Entry Read Service Class                                          */
  /*                                                                                                                  */
  /********************************************************************************************************************/
  private class MockService extends MockBaseService implements MapEntryReadService {

    @Override
    public boolean exists(MapEntryReadId identifier, ResolvedReadContext resolvedReadContext) {
      return identifier.getMapVersion().getName().equals("test")
          && identifier.getUri().equals("test.version.uri")
          && resolvedReadContext != null;
    }

    @Override
    public MapEntry read(MapEntryReadId id, ResolvedReadContext resolvedReadContext) {
      NameOrURI mapVersion = id.getMapVersion();
     
      MapEntry entry = new MapEntry();
      SourceAndRoleReference sourceAndRoleReference = new SourceAndRoleReference();
      sourceAndRoleReference.setSource(new SourceReference("test.sourceRef"));

      if (mapVersion.getName().equals("test") && id.getUri().equals("test.version.uri")) {
        sourceAndRoleReference.setContent("success");
      }
      else {
        sourceAndRoleReference.setContent("fail");
      }

      SourceAndRoleReference refs[] = new SourceAndRoleReference[1];
      refs[0] = sourceAndRoleReference;
      entry.setSource(refs);

      MapVersionReference versionReference = new MapVersionReference();
      versionReference.setMapVersion(new NameAndMeaningReference("test.version"));
      entry.setAssertedBy(versionReference);

      URIAndEntityName uriAndEntityName = new URIAndEntityName();
      uriAndEntityName.setName("test.name");
      uriAndEntityName.setNamespace("test");
      entry.setMapFrom(uriAndEntityName);

      entry.setProcessingRule(MapProcessingRule.FIRST_MATCH);

      return entry;
    }
    
  }

}
