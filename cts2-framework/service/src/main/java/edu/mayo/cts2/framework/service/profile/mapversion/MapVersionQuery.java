package edu.mayo.cts2.framework.service.profile.mapversion;

import edu.mayo.cts2.framework.service.command.restriction.MapVersionQueryServiceRestrictions;
import edu.mayo.cts2.framework.service.profile.ResourceQuery;

public interface MapVersionQuery extends ResourceQuery {
	
	public MapVersionQueryServiceRestrictions getRestrictions();

}
