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


/**
 * The Class ServerContext.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class ServerContext {

	private String serverRoot = "http://informatics.mayo.edu/exist/cts2";

	private String appName = "rest";

	/**
	 * Instantiates a new server context.
	 */
	protected ServerContext(){
		super();
	}

	/**
	 * Instantiates a new server context.
	 *
	 * @param cts2Config the cts2 config
	 */
	protected ServerContext(String serverRoot, String appName) {
		super();
		this.serverRoot = serverRoot;
		this.appName = appName;
	}
	
	protected void refresh(String serverRoot, String appName){
		this.serverRoot = serverRoot;
		this.appName = appName;
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
	 * Gets the app name.
	 * 
	 * @return the app name
	 */
	public String getAppName() {
		return appName;
	}
}
