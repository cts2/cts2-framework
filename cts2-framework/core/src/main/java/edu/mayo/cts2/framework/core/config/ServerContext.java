/*
 * Copyright: (c) 2004-2011 Mayo Foundation for Medical Education and 
 * Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
 * triple-shield Mayo logo are trademarks and service marks of MFMER.
 *
 * Except as contained in the copyright notice above, or as used to identify 
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.mayo.cts2.framework.core.config;

import org.apache.commons.lang.StringUtils;

/**
 * The Class ServerContext.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class ServerContext implements ConfigChangeObserver {

	private static String SERVER_ROOT_PROPERTY = "server.root";
	private static String APP_NAME_PROPERTY = "app.name";

	private String serverRoot = "http://informatics.mayo.edu/exist/cts2";

	private String appName = "rest";
	
	private Cts2Config cts2Config;
	
	/**
	 * Instantiates a new server context.
	 */
	private ServerContext(){
		super();
	}

	/**
	 * Instantiates a new server context.
	 *
	 * @param cts2Config the cts2 config
	 */
	protected ServerContext(Cts2Config cts2Config) {
		this();
		this.cts2Config = cts2Config;
		this.cts2Config.registerListener(this);
		this.loadProperties();
	}

	/**
	 * Load properties.
	 */
	private void loadProperties() {
		String serverRootFromProps = cts2Config.getProperty(
				SERVER_ROOT_PROPERTY);
		if (StringUtils.isNotBlank(serverRootFromProps)) {
			this.serverRoot = serverRootFromProps;
		}

		String appNameFromProps = Cts2Config.instance().getProperty(
				APP_NAME_PROPERTY);
		if (StringUtils.isNotBlank(appNameFromProps)) {
			this.appName = appNameFromProps;
		}
	}

	/**
	 * Gets the server root.
	 * 
	 * @return the server root
	 */
	public String getServerRoot() {
		return serverRoot;
	}

	/**
	 * Gets the server root with app name.
	 * 
	 * @return the server root with app name
	 */
	public String getServerRootWithAppName() {
		return this.getServerRoot() + "/" + this.getAppName();
	}

	/**
	 * Sets the server root.
	 * 
	 * @param serverRoot
	 *            the new server root
	 */
	public void setServerRoot(String serverRoot) {
		this.serverRoot = serverRoot;
	}

	/**
	 * Gets the app name.
	 * 
	 * @return the app name
	 */
	public String getAppName() {
		return appName;
	}

	/**
	 * Sets the app name.
	 * 
	 * @param appName
	 *            the new app name
	 */
	public void setAppName(String appName) {
		this.appName = appName;
	}

	/* (non-Javadoc)
	 * @see edu.mayo.cts2.framework.core.config.ConfigChangeObserver#onContextPropertiesFileChange()
	 */
	public void onContextPropertiesFileChange() {
		this.loadProperties();
	}

	/* (non-Javadoc)
	 * @see edu.mayo.cts2.framework.core.config.ConfigChangeObserver#onPluginsDirectoryChange()
	 */
	public void onPluginsDirectoryChange() {
		this.loadProperties();
	}
}
