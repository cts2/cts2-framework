package edu.mayo.cts2.framework.service.profile;

import edu.mayo.cts2.framework.service.name.ResourceIdentifier;

public interface UriResolvable<R,I extends ResourceIdentifier<?>> {

	//public I resolveUri(String uri);
	
	public R readByUri(String uri);
}
