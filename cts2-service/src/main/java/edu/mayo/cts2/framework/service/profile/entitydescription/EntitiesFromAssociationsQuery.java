package edu.mayo.cts2.framework.service.profile.entitydescription;

import edu.mayo.cts2.framework.service.profile.association.AssociationQuery;

public interface EntitiesFromAssociationsQuery {
	
	enum EntitiesFromAssociations {SOURCES, TARGETS, PREDICATES, SOURCES_AND_TARGETS }

	public EntitiesFromAssociations getEntitiesFromAssociationsType();
	
	public AssociationQuery getAssociationQuery();
	
}
