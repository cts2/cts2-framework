package edu.mayo.cts2.framework.service.profile.entitydescription;

import edu.mayo.cts2.framework.service.command.restriction.EntityDescriptionQueryServiceRestrictions;
import edu.mayo.cts2.framework.service.profile.ResourceQuery;

public interface EntityDescriptionQuery extends ResourceQuery {
	
	public EntityDescriptionQueryServiceRestrictions getRestrictions();

}
