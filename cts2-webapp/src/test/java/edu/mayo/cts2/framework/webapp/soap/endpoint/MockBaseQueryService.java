package edu.mayo.cts2.framework.webapp.soap.endpoint;

import edu.mayo.cts2.framework.model.command.Page;
import edu.mayo.cts2.framework.model.core.ComponentReference;
import edu.mayo.cts2.framework.model.core.MatchAlgorithmReference;
import edu.mayo.cts2.framework.model.core.PredicateReference;
import edu.mayo.cts2.framework.model.core.SortCriteria;
import edu.mayo.cts2.framework.model.directory.DirectoryResult;
import edu.mayo.cts2.framework.service.profile.QueryService;
import edu.mayo.cts2.framework.service.profile.ResourceQuery;

import java.util.Set;

/**
 * Mayo Clinic Research and Support Systems
 * <p/>
 * Package: edu.mayo.cts2.framework.webapp
 *
 * @author <a href="mailto:suesse.dale@mayo.edu">Dale Suesse</a>
 *         <p/>
 *         Last Modified:
 *         Date: 2/17/12
 *         Time: 1:57 PM
 *         User: m091355
 */
public class MockBaseQueryService extends MockBaseService implements QueryService {

  public DirectoryResult getResourceSummaries(ResourceQuery query, SortCriteria sortCriteria, Page page) {
    throw new UnsupportedOperationException();
  }

  public DirectoryResult getResourceList(ResourceQuery query, SortCriteria sortCriteria, Page page) {
    throw new UnsupportedOperationException();
  }

  public int count(ResourceQuery query) {
    throw new UnsupportedOperationException();
  }

  public Set<? extends MatchAlgorithmReference> getSupportedMatchAlgorithms() {
    throw new UnsupportedOperationException();
  }

  public Set<? extends ComponentReference> getSupportedSearchReferences() {
    throw new UnsupportedOperationException();
  }

  public Set<? extends ComponentReference> getSupportedSortReferences() {
    throw new UnsupportedOperationException();
  }

  public Set<PredicateReference> getKnownProperties() {
    throw new UnsupportedOperationException();
  }

}
