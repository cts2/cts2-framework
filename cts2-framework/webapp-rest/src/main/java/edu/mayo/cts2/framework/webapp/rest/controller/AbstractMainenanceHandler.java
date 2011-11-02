package edu.mayo.cts2.framework.webapp.rest.controller;

import java.util.Date;

import edu.mayo.cts2.framework.model.core.ChangeDescription;
import edu.mayo.cts2.framework.model.core.ChangeableElementGroup;
import edu.mayo.cts2.framework.model.core.types.ChangeType;

public abstract class AbstractMainenanceHandler {

	protected ChangeableElementGroup createChangeableElementGroup(String changeSetUri, ChangeType changeType){
		ChangeableElementGroup group = new ChangeableElementGroup();
		group.setChangeDescription(new ChangeDescription());
		group.getChangeDescription().setChangeDate(new Date());
		group.getChangeDescription().setChangeType(changeType);
		group.getChangeDescription().setContainingChangeSet(changeSetUri);
		
		return group;
	}
	
}
