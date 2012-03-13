package edu.mayo.cts2.framework.webapp.soap.endpoint.association;

import edu.mayo.cts2.framework.model.association.Association;
import edu.mayo.cts2.framework.model.command.ResolvedReadContext;
import edu.mayo.cts2.framework.model.core.FormatReference;
import edu.mayo.cts2.framework.model.core.NamespaceReference;
import edu.mayo.cts2.framework.model.core.OpaqueData;
import edu.mayo.cts2.framework.model.core.SourceReference;
import edu.mayo.cts2.framework.model.service.core.DocumentedNamespaceReference;
import edu.mayo.cts2.framework.model.service.core.ProfileElement;
import edu.mayo.cts2.framework.model.service.core.ReadContext;
import edu.mayo.cts2.framework.model.service.core.types.FunctionalProfile;
import edu.mayo.cts2.framework.model.service.core.types.ImplementationProfile;
import edu.mayo.cts2.framework.model.service.core.types.StructuralProfile;
import edu.mayo.cts2.framework.model.util.ModelUtils;
import edu.mayo.cts2.framework.model.wsdl.associationread.*;
import edu.mayo.cts2.framework.model.wsdl.baseservice.*;
import edu.mayo.cts2.framework.service.profile.association.AssociationReadService;
import edu.mayo.cts2.framework.service.profile.association.name.AssociationReadId;
import edu.mayo.cts2.framework.webapp.service.MockServiceProvider;
import edu.mayo.cts2.framework.webapp.soap.endpoint.MockBaseService;
import edu.mayo.cts2.framework.webapp.soap.endpoint.SoapEndpointTestBase;
import org.apache.commons.lang.ArrayUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class AssociationReadServicesEndpointTestIT extends SoapEndpointTestBase {

  String uri = "http://localhost:8081/webapp-rest/soap/service/AssociationReadService";

  @Test
  public void TestRead() throws Exception {
    fail("Method not implemented");
    MockServiceProvider.cts2Service = new MockService();
    Read readRequest = new Read();
    readRequest.setAssociationID("test");
    readRequest.setContext(new ReadContext());
    ReadResponse response = (ReadResponse) this.doSoapCall(uri, readRequest);
    assertEquals("success", response.getReturn().getAssociationID());
  }

  @Test
  public void TestExists() throws Exception {
    fail("Method not implemented");
    MockServiceProvider.cts2Service = new MockService();
    Exists existsRequest = new Exists();
    existsRequest.setAssociationID("test");
    existsRequest.setContext(new ReadContext());
    ExistsResponse response = (ExistsResponse) this.doSoapCall(uri, existsRequest);
    assertTrue(response.getReturn());
  }

  @Test
  public void TestReadByExternalStatementId() throws Exception {
    fail("Method not implemented");
    MockServiceProvider.cts2Service = new MockService();
    ReadByExternalStatementId readRequest = new ReadByExternalStatementId();
    readRequest.setExternalStatementId("test");
    readRequest.setScopingNamespace(ModelUtils.nameOrUriFromName("namespaceTest"));
    readRequest.setContext(new ReadContext());
    ReadByExternalStatementIdResponse response = (ReadByExternalStatementIdResponse) this.doSoapCall(uri, readRequest);
    assertEquals("success", response.getReturn().getAssociationID());
  }

  @Test
  public void TestExistsByExternalStatementId() throws Exception {
    fail("Method not implemented");
    MockServiceProvider.cts2Service = new MockService();
    ExistsByExternalStatementId existsRequest = new ExistsByExternalStatementId();
    existsRequest.setExternalStatementId("test");
    existsRequest.setScopingNamespace(ModelUtils.nameOrUriFromName("namespaceTest"));
    existsRequest.setContext(new ReadContext());
    ExistsByExternalStatementIdResponse response = (ExistsByExternalStatementIdResponse) this.doSoapCall(uri, existsRequest);
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
    assertEquals(StructuralProfile.SP_ASSOCIATION, profile.getStructuralProfile());
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
  /*                                      Mock Association Read Service Class                                         */
  /*                                                                                                                  */
  /********************************************************************************************************************/
  private class MockService extends MockBaseService implements AssociationReadService {

    public Association readByExternalStatementId(String externalStatementId, String scopingNamespaceName, ResolvedReadContext readContext) {
      Association association = new Association();
      if (externalStatementId.equals("test") && scopingNamespaceName.equals("namespaceTest") && readContext != null) {
        association.setAssociationID("success");
      }
      else {
        association.setAssociationID("fail");
      }
      return association;
    }

    public boolean existsByExternalStatementId(String externalStatementId, String scopingNamespaceName, ResolvedReadContext readContext) {
      return externalStatementId.equals("test") && scopingNamespaceName.equals("namespaceTest") && readContext != null;
    }

    public Association read(AssociationReadId identifier, ResolvedReadContext readContext) {
      Association association = new Association();
      if (identifier.getName().equals("test") && readContext != null) {
        association.setAssociationID("success");
      }
      else {
        association.setAssociationID("fail");
      }
      return association;
    }

    public boolean exists(AssociationReadId identifier, ReadContext readContext) {
      return identifier.getName().equals("test") && readContext != null;
    }
  }

}
