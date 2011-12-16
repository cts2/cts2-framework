package edu.mayo.cts2.framework.service.profile.update;

import java.util.Set;

import edu.mayo.cts2.framework.model.command.ResolvedFilter;
import edu.mayo.cts2.framework.model.service.core.Query;
import edu.mayo.cts2.framework.service.command.restriction.ChangeSetQueryExtensionRestrictions;

public interface ChangeSetQuery {
	
	public Query getQuery();
	
	public Set<ResolvedFilter> getFilterComponent();
	
	public ChangeSetQueryExtensionRestrictions getChangeSetQueryExtensionRestrictions();
	
}
