package edu.mayo.cts2.framework.webapp.soap.endpoint;

import edu.mayo.cts2.framework.model.codesystem.CodeSystemCatalogEntry;
import edu.mayo.cts2.framework.model.directory.DirectoryResult;
import edu.mayo.cts2.framework.model.service.core.NameOrURI;
import edu.mayo.cts2.framework.service.profile.HistoryService;

import java.util.Date;

public class MockBaseHistoryService extends MockBaseQueryService implements HistoryService {

  /* TODO: Implement Mock Method: getEarliestChange */
  public Date getEarliestChange() {
    throw new UnsupportedOperationException();
  }

  /* TODO: Implement Mock Method: getLatestChange */
  public Date getLatestChange() {
    throw new UnsupportedOperationException();
  }

  /* TODO: Implement Mock Method: getChangeHistory */
  public DirectoryResult getChangeHistory(Object identifier, Date fromDate, Date toDate) {
    throw new UnsupportedOperationException();
  }

  public Object getEarliestChangeFor(Object identifier) {
    NameOrURI nameOrURI = (NameOrURI) identifier;
    CodeSystemCatalogEntry entry = new CodeSystemCatalogEntry();
    entry.setAbout("This is a test entry");
    if (nameOrURI.getName().equals("test")) {
      entry.setCodeSystemName("success");
    }
    else {
      entry.setCodeSystemName("fail");
    }
    return entry;
  }

  public CodeSystemCatalogEntry getLastChangeFor(Object identifier) {
    NameOrURI nameOrURI = (NameOrURI) identifier;
    CodeSystemCatalogEntry entry = new CodeSystemCatalogEntry();
    entry.setAbout("This is a test entry");
    if (nameOrURI.getName().equals("test")) {
      entry.setCodeSystemName("success");
    }
    else {
      entry.setCodeSystemName("fail");
    }
    return entry;
  }

  /* TODO: Implement Mock Method: getChangeHistoryFor */
  public DirectoryResult getChangeHistoryFor(Object identifier) {
    throw new UnsupportedOperationException();
  }

}
