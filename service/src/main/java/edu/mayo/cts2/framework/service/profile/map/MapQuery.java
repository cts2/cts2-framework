package edu.mayo.cts2.framework.service.profile.map;

import edu.mayo.cts2.framework.service.command.restriction.MapQueryServiceRestrictions;
import edu.mayo.cts2.framework.service.profile.ResourceQuery;

public interface MapQuery extends ResourceQuery {
	
	public MapQueryServiceRestrictions getRestrictions();

}
