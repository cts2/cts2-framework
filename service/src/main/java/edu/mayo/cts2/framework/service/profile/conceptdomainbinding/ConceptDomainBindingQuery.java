package edu.mayo.cts2.framework.service.profile.conceptdomainbinding;

import edu.mayo.cts2.framework.service.command.restriction.ConceptDomainBindingQueryServiceRestrictions;
import edu.mayo.cts2.framework.service.profile.ResourceQuery;

public interface ConceptDomainBindingQuery extends ResourceQuery {
	
	public ConceptDomainBindingQueryServiceRestrictions getRestrictions();

}
