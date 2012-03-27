package edu.mayo.cts2.framework.webapp.soap.endpoint.entitydescription;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;

import edu.mayo.cts2.framework.model.codesystemversion.CodeSystemVersionCatalogEntry;
import edu.mayo.cts2.framework.model.command.Page;
import edu.mayo.cts2.framework.model.command.ResolvedReadContext;
import edu.mayo.cts2.framework.model.core.CodeSystemReference;
import edu.mayo.cts2.framework.model.core.CodeSystemVersionReference;
import edu.mayo.cts2.framework.model.core.EntityReference;
import edu.mayo.cts2.framework.model.core.FormatReference;
import edu.mayo.cts2.framework.model.core.NameAndMeaningReference;
import edu.mayo.cts2.framework.model.core.NamespaceReference;
import edu.mayo.cts2.framework.model.core.OpaqueData;
import edu.mayo.cts2.framework.model.core.RESTResource;
import edu.mayo.cts2.framework.model.core.ScopedEntityName;
import edu.mayo.cts2.framework.model.core.SortCriteria;
import edu.mayo.cts2.framework.model.core.SourceReference;
import edu.mayo.cts2.framework.model.core.URIAndEntityName;
import edu.mayo.cts2.framework.model.core.VersionTagReference;
import edu.mayo.cts2.framework.model.core.types.CompleteDirectory;
import edu.mayo.cts2.framework.model.directory.DirectoryResult;
import edu.mayo.cts2.framework.model.entity.EntityDescription;
import edu.mayo.cts2.framework.model.entity.EntityDescriptionBase;
import edu.mayo.cts2.framework.model.entity.EntityList;
import edu.mayo.cts2.framework.model.entity.EntityListEntry;
import edu.mayo.cts2.framework.model.entity.NamedEntityDescription;
import edu.mayo.cts2.framework.model.service.core.DocumentedNamespaceReference;
import edu.mayo.cts2.framework.model.service.core.EntityNameOrURI;
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
import edu.mayo.cts2.framework.model.wsdl.entitydescriptionread.AvailableDescriptions;
import edu.mayo.cts2.framework.model.wsdl.entitydescriptionread.AvailableDescriptionsResponse;
import edu.mayo.cts2.framework.model.wsdl.entitydescriptionread.Exists;
import edu.mayo.cts2.framework.model.wsdl.entitydescriptionread.ExistsInCodeSystem;
import edu.mayo.cts2.framework.model.wsdl.entitydescriptionread.ExistsInCodeSystemResponse;
import edu.mayo.cts2.framework.model.wsdl.entitydescriptionread.ExistsResponse;
import edu.mayo.cts2.framework.model.wsdl.entitydescriptionread.GetKnownCodeSystem;
import edu.mayo.cts2.framework.model.wsdl.entitydescriptionread.GetKnownCodeSystemResponse;
import edu.mayo.cts2.framework.model.wsdl.entitydescriptionread.GetKnownCodeSystemVersion;
import edu.mayo.cts2.framework.model.wsdl.entitydescriptionread.GetKnownCodeSystemVersionResponse;
import edu.mayo.cts2.framework.model.wsdl.entitydescriptionread.GetSupportedVersionTag;
import edu.mayo.cts2.framework.model.wsdl.entitydescriptionread.GetSupportedVersionTagResponse;
import edu.mayo.cts2.framework.model.wsdl.entitydescriptionread.Read;
import edu.mayo.cts2.framework.model.wsdl.entitydescriptionread.ReadByCodeSystem;
import edu.mayo.cts2.framework.model.wsdl.entitydescriptionread.ReadByCodeSystemResponse;
import edu.mayo.cts2.framework.model.wsdl.entitydescriptionread.ReadEntityDescriptions;
import edu.mayo.cts2.framework.model.wsdl.entitydescriptionread.ReadEntityDescriptionsResponse;
import edu.mayo.cts2.framework.model.wsdl.entitydescriptionread.ReadResponse;
import edu.mayo.cts2.framework.service.profile.codesystemversion.CodeSystemVersionReadService;
import edu.mayo.cts2.framework.service.profile.entitydescription.EntityDescriptionReadService;
import edu.mayo.cts2.framework.service.profile.entitydescription.name.EntityDescriptionReadId;
import edu.mayo.cts2.framework.webapp.service.MockServiceProvider;
import edu.mayo.cts2.framework.webapp.soap.endpoint.MockBaseService;
import edu.mayo.cts2.framework.webapp.soap.endpoint.SoapEndpointTestBase;

public class EntityDescriptionReadServicesEndpointTestIT extends SoapEndpointTestBase {
  
  String uri = "http://localhost:8081/webapp-rest/soap/service/EntityDescriptionReadService";

  @Before
  public void setup(){	  
	  MockServiceProvider.cts2Services.add(new MockCsvReadService());
  }
  
  private static class MockCsvReadService implements CodeSystemVersionReadService {

		@Override
		public CodeSystemVersionCatalogEntry readByTag(
				NameOrURI parentIdentifier, 
				VersionTagReference tag,
				ResolvedReadContext readContext) {
			CodeSystemVersionCatalogEntry entry = new CodeSystemVersionCatalogEntry();
			entry.setCodeSystemVersionName("test.version");
			
			return entry;
		}

		@Override
		public boolean existsByTag(NameOrURI parentIdentifier,
				VersionTagReference tag, ResolvedReadContext readContext) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public List<VersionTagReference> getSupportedTags() {
			  VersionTagReference ref = new VersionTagReference("success");
		      VersionTagReference refs[] = new VersionTagReference[1];
		      refs[0] = ref;
		      return Arrays.asList(refs);
		}

		@Override
		public CodeSystemVersionCatalogEntry read(NameOrURI identifier,
				ResolvedReadContext readContext) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean exists(NameOrURI identifier,
				ResolvedReadContext readContext) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public String getServiceName() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public OpaqueData getServiceDescription() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getServiceVersion() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public SourceReference getServiceProvider() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public List<DocumentedNamespaceReference> getKnownNamespaceList() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean existsVersionId(NameOrURI codeSystem,
				String officialResourceVersionId) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public CodeSystemVersionCatalogEntry getCodeSystemByVersionId(
				NameOrURI codeSystem, String officialResourceVersionId,
				ResolvedReadContext readContext) {
			// TODO Auto-generated method stub
			return null;
		}
		  
	  }
  
  @Test
  public void TestRead() throws Exception {
    MockServiceProvider.cts2Service = new MockService();
    Read request = new Read();
    ScopedEntityName name = new ScopedEntityName();
    name.setName("test");
    name.setNamespace("test");
    request.setEntityId(ModelUtils.entityNameOrUriFromName(name));
    request.setCodeSystemVersion(ModelUtils.nameOrUriFromName("test.version"));
    request.setContext(new ReadContext());
    QueryControl queryControl = new QueryControl();
    queryControl.setTimeLimit(10000L);
    request.setQueryControl(queryControl);
    ReadResponse response = (ReadResponse) this.doSoapCall(uri, request);
    assertEquals("success", ModelUtils.getEntity(response.getReturn()).getEntityID().getName());
  }

  @Test
  public void TestExists() throws Exception {
    MockServiceProvider.cts2Service = new MockService();
    Exists request = new Exists();
    ScopedEntityName name = new ScopedEntityName();
    name.setName("test");
    name.setNamespace("test");
    request.setEntityId(ModelUtils.entityNameOrUriFromName(name));
    request.setCodeSystemVersion(ModelUtils.nameOrUriFromName("test.version"));
    request.setContext(new ReadContext());
    ExistsResponse response = (ExistsResponse) this.doSoapCall(uri, request);
    assertTrue(response.getReturn());
  }

  @Test
  public void TestExistsInCodeSystem() throws Exception {
    MockServiceProvider.cts2Service = new MockService();
    ExistsInCodeSystem request = new ExistsInCodeSystem();
    request.setCodeSystem(ModelUtils.nameOrUriFromName("test"));
    request.setContext(new ReadContext());
    request.setEntityId(ModelUtils.entityNameOrUriFromUri("test.uri"));
    request.setTag(ModelUtils.nameOrUriFromName("test.tag"));
    ExistsInCodeSystemResponse response = (ExistsInCodeSystemResponse) this.doSoapCall(uri, request);
    assertTrue(response.getReturn());
  }

  @Test
  public void TestReadByCodeSystem() throws Exception {
    MockServiceProvider.cts2Service = new MockService();
    ReadByCodeSystem request = new ReadByCodeSystem();
    request.setCodeSystem(ModelUtils.nameOrUriFromName("test"));
    ScopedEntityName name = new ScopedEntityName();
    name.setName("test");
    name.setNamespace("test");
    request.setEntityId(ModelUtils.entityNameOrUriFromName(name));
    request.setContext(new ReadContext());
    QueryControl queryControl = new QueryControl();
    queryControl.setTimeLimit(1000000L);
    request.setQueryControl(queryControl);
    request.setTag(ModelUtils.nameOrUriFromName("test.tag"));
    ReadByCodeSystemResponse response = (ReadByCodeSystemResponse) this.doSoapCall(uri, request);
    assertEquals("success", ModelUtils.getEntity(response.getReturn()).getEntityID().getName());
  }

  @Test
  public void TestReadEntityDescriptions() throws Exception {
    MockServiceProvider.cts2Service = new MockService();
    ReadEntityDescriptions request = new ReadEntityDescriptions();
    ScopedEntityName name = new ScopedEntityName();
    name.setName("test.name");
    name.setNamespace("test");
    request.setEntityId(ModelUtils.entityNameOrUriFromName(name));
    request.setContext(new ReadContext());
    QueryControl queryControl = new QueryControl();
    queryControl.setTimeLimit(10000L);
    request.setQueryControl(queryControl);
    ReadEntityDescriptionsResponse response = (ReadEntityDescriptionsResponse) this.doSoapCall(uri, request);
    assertEquals("success", response.getEntityList().getEntry(0).getResourceName());
  }

  @Test
  public void TestAvailableDescriptions() throws Exception {
    MockServiceProvider.cts2Service = new MockService();
    AvailableDescriptions request = new AvailableDescriptions();
    request.setEntityId(ModelUtils.entityNameOrUriFromUri("test.uri"));
    request.setContext(new ReadContext());
    AvailableDescriptionsResponse response = (AvailableDescriptionsResponse) this.doSoapCall(uri, request);
    assertEquals("success", response.getReturn().getName().getName());
  }

  @Test
  public void TestGetKnownCodeSystem() throws Exception {
    MockServiceProvider.cts2Service = new MockService();
    GetKnownCodeSystem request = new GetKnownCodeSystem();
    GetKnownCodeSystemResponse response = (GetKnownCodeSystemResponse) this.doSoapCall(uri, request);
    assertEquals("success", response.getReturn()[0].getContent());
  }

  @Test
  public void TestGetKnownCodeSystemVersion() throws Exception {
    MockServiceProvider.cts2Service = new MockService();
    GetKnownCodeSystemVersion request = new GetKnownCodeSystemVersion();
    GetKnownCodeSystemVersionResponse response = (GetKnownCodeSystemVersionResponse) this.doSoapCall(uri, request);
    assertEquals("success", response.getReturn()[0].getCodeSystem().getContent());
  }

  @Test
  public void TestGetSupportedVersionTag() throws Exception {
    MockServiceProvider.cts2Service = new MockService();
    GetSupportedVersionTag request = new GetSupportedVersionTag();
    GetSupportedVersionTagResponse response = (GetSupportedVersionTagResponse) this.doSoapCall(uri, request);
    assertEquals("success", response.getReturn(0).getContent());
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
    assertEquals(StructuralProfile.SP_ENTITY_DESCRIPTION, profile.getStructuralProfile());
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
  /*                                  Mock Code System Catalog Read Service Class                                     */
  /*                                                                                                                  */
  /********************************************************************************************************************/
  private class MockService extends MockBaseService implements EntityDescriptionReadService {

    @Override
    public EntityDescription read(EntityDescriptionReadId identifier, ResolvedReadContext resolvedReadContext) {
      ScopedEntityName name = new ScopedEntityName();
      name.setNamespace("test");
      EntityDescriptionBase entityDescription = createEntityDescription();

      if (identifier.getEntityName().getName().equals("test")
          && identifier.getCodeSystemVersion().getName().equals("test.version")
          && resolvedReadContext != null) {
        name.setName("success");
      }
      else {
        name.setName("fail");
      }
      entityDescription.setEntityID(name);
      
      return ModelUtils.toEntityDescription(entityDescription);
    }

    @Override
    public boolean exists(EntityDescriptionReadId identifier, ResolvedReadContext resolvedReadContext) {
    	ScopedEntityName entityId = identifier.getEntityName();
    	NameOrURI codeSystemVersion = identifier.getCodeSystemVersion();
    	
      return ( ( entityId != null && StringUtils.equals(entityId.getName(), "test") ) || StringUtils.equals(identifier.getUri(), "test.uri"))
          && codeSystemVersion.getName().equals("test.version")
          && resolvedReadContext != null;
    }

    @Override
    public DirectoryResult<EntityListEntry> readEntityDescriptions(EntityNameOrURI entityId, SortCriteria sortCriteria, ResolvedReadContext readContext, Page page) {
      throw new UnsupportedOperationException("Unimplemented Method");
    }

    @Override
    public EntityReference availableDescriptions(EntityNameOrURI entityId, ResolvedReadContext readContext) {
      EntityReference ref = new EntityReference();
      ref.setAbout("test.about");
      ScopedEntityName name = new ScopedEntityName();
      name.setNamespace("test");
      if (entityId.getUri().equals("test.uri") && readContext != null) {
        name.setName("success");
      }
      else {
        name.setName("fail");
      }

      ref.setName(name);
      return ref;
    }

   
    @Override
    public EntityList readEntityDescriptions(EntityNameOrURI entityId, ResolvedReadContext readContext) {
      EntityList entities = createEntityList();
      EntityListEntry entry = createEntityListEntry();

      if (entityId.getEntityName().getName().equals("test.name") && readContext != null) {
        entry.setResourceName("success");
      }
      else {
        entry.setResourceName("fail");
      }

      entities.addEntry(entry);
      return entities;
    }

    @Override
    public List<CodeSystemReference> getKnownCodeSystems() {
      CodeSystemReference ref = new CodeSystemReference("success");
      CodeSystemReference refs[] = new CodeSystemReference[1];
      refs[0] = ref;
      return Arrays.asList(refs);
    }

    @Override
    public List<CodeSystemVersionReference> getKnownCodeSystemVersions() {
      CodeSystemVersionReference ref = new CodeSystemVersionReference();
      ref.setCodeSystem(new CodeSystemReference("success"));
      ref.setVersion(new NameAndMeaningReference("test.version"));
      CodeSystemVersionReference refs[] = new CodeSystemVersionReference[1];
      refs[0] = ref;
      return Arrays.asList(refs);
    }

    @Override
    public List<VersionTagReference> getSupportedVersionTags() {
      VersionTagReference ref = new VersionTagReference("success");
      VersionTagReference refs[] = new VersionTagReference[1];
      refs[0] = ref;
      return Arrays.asList(refs);
    }

    private EntityDescriptionBase createEntityDescription() {
      EntityDescriptionBase entityDescription = new NamedEntityDescription();
      entityDescription.setAbout("test.about");
      CodeSystemVersionReference versionRef = new CodeSystemVersionReference();
      versionRef.setCodeSystem(new CodeSystemReference("test.codesystem"));
      NameAndMeaningReference nameRef = new NameAndMeaningReference("test.nameref");
      versionRef.setVersion(nameRef);
      entityDescription.setDescribingCodeSystemVersion(versionRef);
      URIAndEntityName uriAndEntityName = new URIAndEntityName();
      uriAndEntityName.setName("test.urientityname");
      uriAndEntityName.setNamespace("test");
      URIAndEntityName uriAndEntityNames[] = new URIAndEntityName[1];
      uriAndEntityNames[0] = uriAndEntityName;
      entityDescription.setEntityType(uriAndEntityNames);
      return entityDescription;
    }

    private EntityList createEntityList() {
      EntityList list = new EntityList();
      RESTResource rest = new RESTResource();
      rest.setResourceRoot("test.root");
      rest.setResourceURI("test.uri");
      rest.setAccessDate(new Date());
      list.setHeading(rest);
      list.setComplete(CompleteDirectory.fromValue("COMPLETE"));
      list.setSortCriteria(new SortCriteria());
      list.setNumEntries(1L);
      return list;
    }

    private EntityListEntry createEntityListEntry() {
      /* EntityDescription */
      EntityDescription entityDescription = new EntityDescription();
      NamedEntityDescription namedEntityDescription = new NamedEntityDescription();
      ScopedEntityName name = new ScopedEntityName();
      name.setName("test");
      name.setNamespace("test");

      CodeSystemVersionReference ref = new CodeSystemVersionReference();
      ref.setCodeSystem(new CodeSystemReference("test.ref"));
      ref.setVersion(new NameAndMeaningReference("test.ref.version"));
      namedEntityDescription.setEntityID(name);
      namedEntityDescription.setDescribingCodeSystemVersion(ref);
      namedEntityDescription.setAbout("test.about");

      URIAndEntityName uriAndEntityName = new URIAndEntityName();
      uriAndEntityName.setName("test.entityType");
      uriAndEntityName.setNamespace("test.namespace");

      List<URIAndEntityName> uriAndEntityNameList = new ArrayList<URIAndEntityName>(1);
      uriAndEntityNameList.add(uriAndEntityName);

      namedEntityDescription.setEntityType(uriAndEntityNameList);
      entityDescription.setNamedEntity(namedEntityDescription);

      /* EntityListEntry */
      EntityListEntry entry = new EntityListEntry();
      entry.setEntry(entityDescription);
      return entry;
    }

  }

  
}
