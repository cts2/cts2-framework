package edu.mayo.cts2.framework.service.profile.valuesetdefinition;

import edu.mayo.cts2.framework.service.command.restriction.ValueSetDefinitionQueryServiceRestrictions;
import edu.mayo.cts2.framework.service.profile.ResourceQuery;

public interface ValueSetDefinitionQuery extends ResourceQuery {
	
	public ValueSetDefinitionQueryServiceRestrictions getRestrictions();

}
