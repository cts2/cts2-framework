package edu.mayo.cts2.framework.service.profile;

import java.util.Set;

import edu.mayo.cts2.framework.model.command.ResolvedFilter;
import edu.mayo.cts2.framework.model.command.ResolvedReadContext;
import edu.mayo.cts2.framework.model.service.core.Query;

public interface ResourceQuery {
	
	public Query getQuery();
	
	public Set<ResolvedFilter> getFilterComponent();
	
	public ResolvedReadContext getReadContext();
	
}
