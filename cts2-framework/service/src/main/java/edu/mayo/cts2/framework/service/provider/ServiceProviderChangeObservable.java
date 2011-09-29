package edu.mayo.cts2.framework.service.provider;


public interface ServiceProviderChangeObservable {


	/**
	 * Register listener.
	 *
	 * @param observer the observer
	 */
	public void registerListener(ServiceProviderChangeObserver observer);
	
	/**
	 * Unregister listener.
	 *
	 * @param observer the observer
	 */
	public void unregisterListener(ServiceProviderChangeObserver observer);
}
