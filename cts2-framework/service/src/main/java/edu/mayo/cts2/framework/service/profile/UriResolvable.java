package edu.mayo.cts2.framework.service.profile;

public interface UriResolvable<R> {

	public R readByUri(String uri);
}
