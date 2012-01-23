package edu.mayo.cts2.framework.service.profile.codesystemversion;

import edu.mayo.cts2.framework.service.command.restriction.CodeSystemVersionQueryServiceRestrictions;
import edu.mayo.cts2.framework.service.profile.ResourceQuery;

public interface CodeSystemVersionQuery extends ResourceQuery {
	
	public CodeSystemVersionQueryServiceRestrictions getRestrictions();

}
