package edu.mayo.cts2.framework.core.config;

public interface ServerContext {
	/**
	 * Gets the server root.
	 * 
	 * @return the server root
	 */
	public String getServerRoot();

	/**
	 * Gets the server root with app name.
	 * 
	 * @return the server root with app name
	 */
	public String getServerRootWithAppName();

	/**
	 * Gets the app name.
	 * 
	 * @return the app name
	 */
	public String getAppName();
}
