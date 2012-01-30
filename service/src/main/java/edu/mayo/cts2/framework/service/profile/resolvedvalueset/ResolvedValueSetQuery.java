package edu.mayo.cts2.framework.service.profile.resolvedvalueset;

import java.util.Set;

import edu.mayo.cts2.framework.model.command.ResolvedFilter;
import edu.mayo.cts2.framework.model.service.core.Query;
import edu.mayo.cts2.framework.service.command.restriction.ResolvedValueSetQueryServiceRestrictions;

public interface ResolvedValueSetQuery {
	
	public Query getQuery();
	
	public Set<ResolvedFilter> getFilterComponent();
	
	public ResolvedValueSetQueryServiceRestrictions getResolvedValueSetQueryServiceRestrictions();

}
