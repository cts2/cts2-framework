package edu.mayo.cts2.framework.service.profile.valueset;

import edu.mayo.cts2.framework.service.command.restriction.ValueSetQueryServiceRestrictions;
import edu.mayo.cts2.framework.service.profile.ResourceQuery;

public interface ValueSetQuery extends ResourceQuery {
	
	public ValueSetQueryServiceRestrictions getRestrictions();

}
