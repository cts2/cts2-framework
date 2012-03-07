package edu.mayo.cts2.framework.webapp.soap.endpoint.conceptdomainbinding;

import edu.mayo.cts2.framework.model.command.ResolvedReadContext;
import edu.mayo.cts2.framework.model.conceptdomainbinding.ConceptDomainBinding;
import edu.mayo.cts2.framework.model.core.*;
import edu.mayo.cts2.framework.model.extension.LocalIdConceptDomainBinding;
import edu.mayo.cts2.framework.model.service.core.DocumentedNamespaceReference;
import edu.mayo.cts2.framework.model.service.core.NameOrURI;
import edu.mayo.cts2.framework.model.service.core.ReadContext;
import edu.mayo.cts2.framework.model.util.ModelUtils;
import edu.mayo.cts2.framework.model.wsdl.baseservice.*;
import edu.mayo.cts2.framework.model.wsdl.conceptdomainbindingread.GetSupportedTag;
import edu.mayo.cts2.framework.model.wsdl.conceptdomainbindingread.GetSupportedTagResponse;
import edu.mayo.cts2.framework.model.wsdl.conceptdomainbindingread.Read;
import edu.mayo.cts2.framework.model.wsdl.conceptdomainbindingread.ReadResponse;
import edu.mayo.cts2.framework.service.profile.conceptdomainbinding.ConceptDomainBindingReadService;
import edu.mayo.cts2.framework.service.profile.conceptdomainbinding.name.ConceptDomainBindingReadId;
import edu.mayo.cts2.framework.webapp.service.MockServiceProvider;
import edu.mayo.cts2.framework.webapp.soap.endpoint.MockBaseService;
import edu.mayo.cts2.framework.webapp.soap.endpoint.SoapEndpointTestBase;
import org.apache.commons.lang.ArrayUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class ConceptDomainBindingReadServicesEndpointTestIT extends SoapEndpointTestBase {

  String uri = "http://localhost:8081/webapp-rest/soap/service/ConceptDomainBindingReadService";

  @Test
  public void TestGetSupportedTag() throws Exception {
    GetSupportedTag request = new GetSupportedTag();
    GetSupportedTagResponse response = (GetSupportedTagResponse) this.doSoapCall(uri, request);
    assertEquals("success", response.getReturn(0).getContent());
  }
  
  @Test
  public void TestRead() throws Exception {
    Read request = new Read();
    request.setConceptDomain(ModelUtils.nameOrUriFromName("test"));
    request.setApplicableContext(ModelUtils.nameOrUriFromName("applicableContextTest"));
    request.setBindingQualifier(ModelUtils.nameOrUriFromName("bindingQualifierTest"));
    request.setValueSet(ModelUtils.nameOrUriFromName("valueSetTest"));
    ReadResponse response = (ReadResponse) this.doSoapCall(uri, request);
    assertEquals("success", response.getReturn().getBindingURI());
  }
  
  @Test
  public void TestExists() throws Exception {
    fail("Method not implemented.");
  }
  
  @Test
  public void TestReadByURI() throws Exception {
    fail("Method not implemented.");
  }
  
  @Test
  public void TestExistsURI() throws Exception {
    fail("Method not implemented.");
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
  /*                                 Mock Concept Domain Binding Read Service Class                                   */
  /*                                                                                                                  */
  /********************************************************************************************************************/
  private class MockService extends MockBaseService implements ConceptDomainBindingReadService {

//    @Override
//    public ConceptDomainBinding read(NameOrURI conceptDomain, NameOrURI valueSet, NameOrURI applicableContext, NameOrURI bindingQualifier) {
//
//    }
//
//    @Override
//    public ConceptDomainBinding readByURI(DocumentURI uri) {
//
//    }
//
//    @Override
//    public boolean exists(NameOrURI conceptDomain, NameOrURI valueSet, NameOrURI applicableContext, NameOrURI bindingQualifier) {
//
//    }
//
//    @Override
//    public boolean existsURI(DocumentURI uri) {
//
//    }

    @Override
    public LocalIdConceptDomainBinding read(ConceptDomainBindingReadId identifier, ResolvedReadContext readContext) {
      return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean exists(ConceptDomainBindingReadId identifier, ReadContext readContext) {
      return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public VersionTagReference[] getSupportedTag() {
      VersionTagReference ref = new VersionTagReference("success");
      VersionTagReference refs[] = new VersionTagReference[1];
      refs[0] = ref;
      return refs;
    }
    
  }

}
