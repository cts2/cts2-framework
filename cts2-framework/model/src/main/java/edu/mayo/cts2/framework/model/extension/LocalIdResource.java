package edu.mayo.cts2.framework.model.extension;

import edu.mayo.cts2.framework.model.core.Changeable;

public abstract class LocalIdResource<T> {
	
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
