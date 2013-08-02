package edu.mayo.cts2.framework.webapp.soap.endpoint.conceptdomainbinding;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import edu.mayo.cts2.framework.model.command.ResolvedReadContext;
import edu.mayo.cts2.framework.model.conceptdomainbinding.ConceptDomainBinding;
import edu.mayo.cts2.framework.model.core.ConceptDomainReference;
import edu.mayo.cts2.framework.model.core.ContextReference;
import edu.mayo.cts2.framework.model.core.NamespaceReference;
import edu.mayo.cts2.framework.model.core.OpaqueData;
import edu.mayo.cts2.framework.model.core.SourceReference;
import edu.mayo.cts2.framework.model.core.ValueSetReference;
import edu.mayo.cts2.framework.model.core.VersionTagReference;
import edu.mayo.cts2.framework.model.extension.LocalIdConceptDomainBinding;
import edu.mayo.cts2.framework.model.service.core.DocumentedNamespaceReference;
import edu.mayo.cts2.framework.model.service.core.NameOrURI;
import edu.mayo.cts2.framework.model.service.core.ProfileElement;
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
import edu.mayo.cts2.framework.model.wsdl.conceptdomainbindingread.Exists;
import edu.mayo.cts2.framework.model.wsdl.conceptdomainbindingread.ExistsResponse;
import edu.mayo.cts2.framework.model.wsdl.conceptdomainbindingread.ExistsURI;
import edu.mayo.cts2.framework.model.wsdl.conceptdomainbindingread.ExistsURIResponse;
import edu.mayo.cts2.framework.model.wsdl.conceptdomainbindingread.GetSupportedTag;
import edu.mayo.cts2.framework.model.wsdl.conceptdomainbindingread.GetSupportedTagResponse;
import edu.mayo.cts2.framework.model.wsdl.conceptdomainbindingread.Read;
import edu.mayo.cts2.framework.model.wsdl.conceptdomainbindingread.ReadByURI;
import edu.mayo.cts2.framework.model.wsdl.conceptdomainbindingread.ReadByURIResponse;
import edu.mayo.cts2.framework.model.wsdl.conceptdomainbindingread.ReadResponse;
import edu.mayo.cts2.framework.service.profile.conceptdomainbinding.ConceptDomainBindingReadService;
import edu.mayo.cts2.framework.service.profile.conceptdomainbinding.name.ConceptDomainBindingReadId;
import edu.mayo.cts2.framework.webapp.service.MockServiceProvider;
import edu.mayo.cts2.framework.webapp.soap.endpoint.MockBaseService;
import edu.mayo.cts2.framework.webapp.soap.endpoint.SoapEndpointTestBase;

public class ConceptDomainBindingReadServicesEndpointTestIT extends SoapEndpointTestBase {

  String uri = "http://localhost:8081/webapp-rest/soap/service/ConceptDomainBindingReadService";

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

  @Test
  public void TestRead() throws Exception {
    MockServiceProvider.cts2Service = new MockService();
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
    MockServiceProvider.cts2Service = new MockService();
    Exists request = new Exists();
    request.setConceptDomain(ModelUtils.nameOrUriFromName("test"));
    request.setApplicableContext(ModelUtils.nameOrUriFromName("applicableContextTest"));
    request.setBindingQualifier(ModelUtils.nameOrUriFromName("bindingQualifierTest"));
    request.setValueSet(ModelUtils.nameOrUriFromName("valueSetTest"));
    ExistsResponse response = (ExistsResponse) this.doSoapCall(uri, request);
    assertTrue(response.getReturn());
  }

  @Test
  public void TestReadByURI() throws Exception {
    MockServiceProvider.cts2Service = new MockService();
    ReadByURI request = new ReadByURI();
    request.setUri("test.uri");
    ReadByURIResponse response = (ReadByURIResponse) this.doSoapCall(uri, request);
    assertEquals("success", response.getReturn().getBindingURI());
  }

  @Test
  public void TestExistsURI() throws Exception {
    MockServiceProvider.cts2Service = new MockService();
    ExistsURI request = new ExistsURI();
    request.setUri("test.uri");
    ExistsURIResponse response = (ExistsURIResponse) this.doSoapCall(uri, request);
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
    assertEquals(StructuralProfile.SP_CONCEPT_DOMAIN_BINDING, profile.getStructuralProfile());
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
  /*                                 Mock Concept Domain Binding Read Service Class                                   */
  /*                                                                                                                  */

  /**
   * ****************************************************************************************************************
   */
  private class MockService extends MockBaseService implements ConceptDomainBindingReadService {

    public LocalIdConceptDomainBinding read(ConceptDomainBindingReadId identifier, ResolvedReadContext readContext) {
      ConceptDomainBinding binding = createBinding();

      if (StringUtils.equals(identifier.getName(), "test") ) {
        binding.setBindingURI("success");
      } else if (StringUtils.equals(identifier.getUri(), "test.uri") ) {
          binding.setBindingURI("success");
      }
      else {
        binding.setBindingURI("fail");
      }

      LocalIdConceptDomainBinding entry = new LocalIdConceptDomainBinding(binding);
      return entry;
    }

    public boolean exists(ConceptDomainBindingReadId identifier, ResolvedReadContext readContext) {
    	 return (StringUtils.equals(identifier.getName(), "test") || StringUtils.equals(identifier.getUri(), "test.uri"));
    }

    public boolean exists(NameOrURI conceptDomain, NameOrURI valueSet, NameOrURI applicableContext, NameOrURI bindingQualifier) {
      return StringUtils.equals(conceptDomain.getName(), "test")
          && valueSet.getName().equals("valueSetTest")
          && applicableContext.getName().equals("applicableContextTest")
          && bindingQualifier.getName().equals("bindingQualifierTest");
    }

    public ConceptDomainBinding read(
        NameOrURI conceptDomain, NameOrURI valueSet,
        NameOrURI applicableContext, NameOrURI bindingQualifier) {
      ConceptDomainBinding binding = createBinding();

      if (conceptDomain.getName().equals("test")
          && valueSet.getName().equals("valueSetTest")
          && applicableContext.getName().equals("applicableContextTest")
          && bindingQualifier.getName().equals("bindingQualifierTest")) {
        binding.setBindingURI("success");
      }
      else {
        binding.setBindingURI("fail");
      }

      return binding;
    }

    public List<VersionTagReference> getSupportedTag() {
      List<VersionTagReference> tags = new ArrayList<VersionTagReference>(3);
      tags.add(new VersionTagReference("tag1"));
      tags.add(new VersionTagReference("tag2"));
      tags.add(new VersionTagReference("tag3"));
      return tags;
    }

    private ConceptDomainBinding createBinding() {
      ConceptDomainBinding binding = new ConceptDomainBinding();
      binding.setApplicableContext(new ContextReference("test_ref"));
      binding.setBindingFor(new ConceptDomainReference("test_ref"));
      binding.setBoundValueSet(new ValueSetReference("test_ref"));
      return binding;
    }
  }

}
