package edu.mayo.cts2.framework.model.core;

import edu.mayo.cts2.framework.model.Cts2ModelObject;
import edu.mayo.cts2.framework.model.core.types.EntryState;

public interface IsChangeable extends Cts2ModelObject {

	public ChangeableElementGroup getChangeableElementGroup();
	
	public void setChangeableElementGroup(ChangeableElementGroup group);
	
	public EntryState getEntryState();
	
	public void setEntryState(EntryState entryState);
}
