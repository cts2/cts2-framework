package edu.mayo.cts2.framework.service.profile.mapentry;

import edu.mayo.cts2.framework.service.command.restriction.MapEntryQueryServiceRestrictions;
import edu.mayo.cts2.framework.service.profile.ResourceQuery;

public interface MapEntryQuery extends ResourceQuery {
	
	public MapEntryQueryServiceRestrictions getRestrictions();

}
