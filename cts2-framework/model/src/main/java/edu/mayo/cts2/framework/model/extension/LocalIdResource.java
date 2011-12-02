package edu.mayo.cts2.framework.model.extension;

import edu.mayo.cts2.framework.model.core.ChangeableElementGroup;
import edu.mayo.cts2.framework.model.core.IsChangeable;
import edu.mayo.cts2.framework.model.core.types.EntryState;

public abstract class LocalIdResource<T extends IsChangeable> implements IsChangeable{
	
	public String localID;
	
	public T Resource;
	
	public LocalIdResource(T resource) {
		this(null,resource);
	}

	public LocalIdResource(String localID, T resource) {
		super();
		this.localID = localID;
		Resource = resource;
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

	public String getLocalID() {
		return localID;
	}

	public void setLocalID(String localID) {
		this.localID = localID;
	}

	public T getResource() {
		return Resource;
	}

	public void setResource(T resource) {
		Resource = resource;
	}
	
}
