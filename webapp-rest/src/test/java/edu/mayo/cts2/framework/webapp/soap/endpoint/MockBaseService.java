package edu.mayo.cts2.framework.webapp.soap.endpoint;

import edu.mayo.cts2.framework.model.core.FormatReference;
import edu.mayo.cts2.framework.model.core.OpaqueData;
import edu.mayo.cts2.framework.model.core.SourceReference;
import edu.mayo.cts2.framework.model.service.core.DocumentedNamespaceReference;
import edu.mayo.cts2.framework.model.util.ModelUtils;
import edu.mayo.cts2.framework.service.profile.BaseService;

import java.util.ArrayList;
import java.util.List;

public class MockBaseService implements BaseService {

  public String getServiceName() {
    return MockBaseService.class.getName();
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
