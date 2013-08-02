package edu.mayo.cts2.framework.model.extension;

import edu.mayo.cts2.framework.model.association.Association;

public class LocalIdAssociation extends ChangeableLocalIdResource<Association> {

	public LocalIdAssociation(Association resource) {
		super(resource);
	}

	public LocalIdAssociation(String localID, Association resource) {
		super(localID, resource);
	}
	
}
