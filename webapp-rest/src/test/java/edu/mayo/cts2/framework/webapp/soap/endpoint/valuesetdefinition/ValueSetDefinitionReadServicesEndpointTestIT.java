package edu.mayo.cts2.framework.webapp.soap.endpoint.valuesetdefinition;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.junit.Ignore;
import org.junit.Test;

import edu.mayo.cts2.framework.model.command.ResolvedReadContext;
import edu.mayo.cts2.framework.model.core.CodeSystemReference;
import edu.mayo.cts2.framework.model.core.CodeSystemVersionReference;
import edu.mayo.cts2.framework.model.core.NameAndMeaningReference;
import edu.mayo.cts2.framework.model.core.NamespaceReference;
import edu.mayo.cts2.framework.model.core.OpaqueData;
import edu.mayo.cts2.framework.model.core.SourceAndNotation;
import edu.mayo.cts2.framework.model.core.SourceReference;
import edu.mayo.cts2.framework.model.core.ValueSetReference;
import edu.mayo.cts2.framework.model.core.VersionTagReference;
import edu.mayo.cts2.framework.model.core.types.SetOperator;
import edu.mayo.cts2.framework.model.extension.LocalIdValueSetDefinition;
import edu.mayo.cts2.framework.model.service.core.DocumentedNamespaceReference;
import edu.mayo.cts2.framework.model.service.core.NameOrURI;
import edu.mayo.cts2.framework.model.service.core.ProfileElement;
import edu.mayo.cts2.framework.model.service.core.QueryControl;
import edu.mayo.cts2.framework.model.service.core.ReadContext;
import edu.mayo.cts2.framework.model.service.core.types.FunctionalProfile;
import edu.mayo.cts2.framework.model.service.core.types.ImplementationProfile;
import edu.mayo.cts2.framework.model.service.core.types.StructuralProfile;
import edu.mayo.cts2.framework.model.util.ModelUtils;
import edu.mayo.cts2.framework.model.valuesetdefinition.CompleteCodeSystemReference;
import edu.mayo.cts2.framework.model.valuesetdefinition.ValueSetDefinition;
import edu.mayo.cts2.framework.model.valuesetdefinition.ValueSetDefinitionEntry;
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
import edu.mayo.cts2.framework.model.wsdl.valuesetdefinitionread.Exists;
import edu.mayo.cts2.framework.model.wsdl.valuesetdefinitionread.ExistsDefinitionForValueSet;
import edu.mayo.cts2.framework.model.wsdl.valuesetdefinitionread.ExistsDefinitionForValueSetResponse;
import edu.mayo.cts2.framework.model.wsdl.valuesetdefinitionread.ExistsResponse;
import edu.mayo.cts2.framework.model.wsdl.valuesetdefinitionread.Read;
import edu.mayo.cts2.framework.model.wsdl.valuesetdefinitionread.ReadDefinitionForValueSet;
import edu.mayo.cts2.framework.model.wsdl.valuesetdefinitionread.ReadDefinitionForValueSetResponse;
import edu.mayo.cts2.framework.model.wsdl.valuesetdefinitionread.ReadResponse;
import edu.mayo.cts2.framework.service.profile.valuesetdefinition.ValueSetDefinitionReadService;
import edu.mayo.cts2.framework.service.profile.valuesetdefinition.name.ValueSetDefinitionReadId;
import edu.mayo.cts2.framework.webapp.service.MockServiceProvider;
import edu.mayo.cts2.framework.webapp.soap.endpoint.MockBaseService;
import edu.mayo.cts2.framework.webapp.soap.endpoint.SoapEndpointTestBase;

public class ValueSetDefinitionReadServicesEndpointTestIT extends SoapEndpointTestBase {

  String uri = "http://localhost:8081/webapp-rest/soap/service/ValueSetDefinitionReadService";

  @Test
  @Ignore
  public void TestRead() throws Exception {
    fail("Method not implemented");
    MockServiceProvider.cts2Service = new MockService();
    Read request = new Read();
    request.setValueSetDefinitionURI("test");
    request.setContext(new ReadContext());
    request.setQueryControl(new QueryControl());
    ReadResponse response = (ReadResponse) this.doSoapCall(uri, request);
    assertEquals("success", response.getReturn().getDocumentURI());
  }

  @Test
  public void TestReadDefinitionForValueSet() throws Exception {
    MockServiceProvider.cts2Service = new MockService();
    ReadDefinitionForValueSet request = new ReadDefinitionForValueSet();
    request.setValueSet(ModelUtils.nameOrUriFromName("test"));
    request.setTag(ModelUtils.nameOrUriFromName("tag1"));
    request.setContext(new ReadContext());
    ReadDefinitionForValueSetResponse response = (ReadDefinitionForValueSetResponse) this.doSoapCall(uri, request);
    assertEquals("success", response.getReturn().getDocumentURI());
  }

  @Test
  @Ignore
  public void TestExists() throws Exception {
    fail("Method not implemented");
    MockServiceProvider.cts2Service = new MockService();
    Exists request = new Exists();
    request.setValueSetDefinitionURI("test");
    request.setContext(new ReadContext());
    ExistsResponse response = (ExistsResponse) this.doSoapCall(uri, request);
    assertTrue(response.getReturn());
  }

  @Test
  public void TestExistsDefinitionForValueSet() throws Exception {
    MockServiceProvider.cts2Service = new MockService();
    ExistsDefinitionForValueSet request = new ExistsDefinitionForValueSet();
    request.setValueSet(ModelUtils.nameOrUriFromName("test"));
    request.setTag(ModelUtils.nameOrUriFromName("tag1"));
    request.setContext(new ReadContext());
    ExistsDefinitionForValueSetResponse response = (ExistsDefinitionForValueSetResponse) this.doSoapCall(uri, request);
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
    assertEquals(StructuralProfile.SP_VALUE_SET_DEFINITION, profile.getStructuralProfile());
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
  /*                                        Mock ValueSet Read Service Class                                          */
  /*                                                                                                                  */
  /********************************************************************************************************************/
  private class MockService extends MockBaseService implements ValueSetDefinitionReadService {

    @Override
    public boolean existsByTag(NameOrURI valueSet, VersionTagReference tag, ResolvedReadContext readContext) {
      return valueSet.getName().equals("test") && tag.getContent().equals("tag1") && readContext != null;
    }

    @Override
    public LocalIdValueSetDefinition readByTag(NameOrURI valueSet, VersionTagReference tag, ResolvedReadContext readContext) {
      ValueSetDefinition definition = createValueSetDefinition();

      if (valueSet.getName().equals("test") && tag.getContent().equals("tag1") && readContext != null) {
        definition.setDocumentURI("success");
      }
      else {
        definition.setDocumentURI("fail");
      }

      return new LocalIdValueSetDefinition("test", definition);
    }

    @Override
    public LocalIdValueSetDefinition read(ValueSetDefinitionReadId identifier, ResolvedReadContext readContext) {
      LocalIdValueSetDefinition localIdValueSetDefinition = new LocalIdValueSetDefinition(createValueSetDefinition());

      if (identifier.getValueSet().getName().equals("test") && readContext != null) {
        localIdValueSetDefinition.setLocalID("success");
      }
      else {
        localIdValueSetDefinition.setLocalID("fail");
      }

      return localIdValueSetDefinition;
    }

    @Override
    public boolean exists(ValueSetDefinitionReadId identifier, ResolvedReadContext readContext) {
      return identifier.getName().equals("test") && readContext != null;
    }

    private ValueSetDefinition createValueSetDefinition() {
      ValueSetDefinition definition = new ValueSetDefinition();
      definition.setAbout("test.about");

      SourceAndNotation sourceAndNotation = new SourceAndNotation();
      sourceAndNotation.setSourceDocument("test.sourceandnotation.document");
      definition.setSourceAndNotation(sourceAndNotation);

      ValueSetReference valueSetReference = new ValueSetReference("test.valuesetref");
      definition.setDefinedValueSet(valueSetReference);

      ValueSetDefinitionEntry entry = new ValueSetDefinitionEntry();
      CompleteCodeSystemReference codeSystemReference = new CompleteCodeSystemReference();
      CodeSystemReference ref = new CodeSystemReference("test.codesystem");
      codeSystemReference.setCodeSystem(ref);
      CodeSystemVersionReference codeSystemVersionReference = new CodeSystemVersionReference();
      codeSystemVersionReference.setCodeSystem(ref);
      codeSystemVersionReference.setVersion(new NameAndMeaningReference("test.codesystemref.version"));
      codeSystemReference.setCodeSystemVersion(codeSystemVersionReference);
      entry.setCompleteCodeSystem(codeSystemReference);
      entry.setOperator(SetOperator.INTERSECT);
      entry.setEntryOrder(1L);
      ValueSetDefinitionEntry entries[] = new ValueSetDefinitionEntry[1];
      entries[0] = entry;
      definition.setEntry(entries);

      return definition;
    }

	@Override
	public List<VersionTagReference> getSupportedTags() {
		 List<VersionTagReference> tags = new ArrayList<VersionTagReference>(3);
	      tags.add(new VersionTagReference("tag1"));
	      tags.add(new VersionTagReference("tag2"));
	      tags.add(new VersionTagReference("tag3"));
	      return tags;
	}
  }

}
