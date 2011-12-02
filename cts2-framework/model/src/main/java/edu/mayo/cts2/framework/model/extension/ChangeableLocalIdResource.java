package edu.mayo.cts2.framework.model.extension;

import edu.mayo.cts2.framework.model.core.ChangeableElementGroup;
import edu.mayo.cts2.framework.model.core.IsChangeable;
import edu.mayo.cts2.framework.model.core.types.EntryState;

public abstract class ChangeableLocalIdResource<T extends IsChangeable> 
	extends LocalIdResource<T>
	implements IsChangeable{

	public ChangeableLocalIdResource(T resource) {
		this(null,resource);
	}

	public ChangeableLocalIdResource(String localID, T resource) {
		super(localID, resource);
	}

	@Override
	public ChangeableElementGroup getChangeableElementGroup() {
		return this.getResource().getChangeableElementGroup();
	}

	@Override
	public EntryState getEntryState() {
		return this.getResource().getEntryState();
	}

	@Override
	public void setChangeableElementGroup(ChangeableElementGroup group) {
		this.getResource().setChangeableElementGroup(group);
	}

	@Override
	public void setEntryState(EntryState entryState) {
		this.getResource().setEntryState(entryState);
	}
	
}
