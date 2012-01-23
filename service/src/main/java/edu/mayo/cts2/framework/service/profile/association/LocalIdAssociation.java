package edu.mayo.cts2.framework.service.profile.association;

import edu.mayo.cts2.framework.model.association.Association;

public class LocalIdAssociation {
	
	private String localId;
	
	private Association association;

	public String getLocalId() {
		return localId;
	}

	public void setLocalId(String localId) {
		this.localId = localId;
	}

	public Association getAssociation() {
		return association;
	}

	public void setAssociation(Association association) {
		this.association = association;
	}
}
