package edu.mayo.cts2.framework.service.name;

public class ResourceIdentifier<I> {

	private I resourceId;

	public ResourceIdentifier(I resourceId) {
		super();
		this.resourceId = resourceId;
	}

	public I getResourceId() {
		return resourceId;
	}

	public void setResourceId(I resourceId) {
		this.resourceId = resourceId;
	}
}
