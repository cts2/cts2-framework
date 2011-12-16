package edu.mayo.cts2.framework.service.profile.association;

import edu.mayo.cts2.framework.service.command.restriction.AssociationQueryServiceRestrictions;
import edu.mayo.cts2.framework.service.profile.ResourceQuery;

public interface AssociationQuery extends ResourceQuery {
	
	public AssociationQueryServiceRestrictions getRestrictions();

}
