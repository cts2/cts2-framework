package edu.mayo.cts2.framework.webapp.rest.controller;

import edu.mayo.cts2.framework.model.core.ChangeableElementGroup;

public interface ChangeableElementGroupHandler<R>{
	
	public void setChangeableElementGroup(R resource, ChangeableElementGroup group);
	
	public ChangeableElementGroup getChangeableElementGroup(R resource);

}
