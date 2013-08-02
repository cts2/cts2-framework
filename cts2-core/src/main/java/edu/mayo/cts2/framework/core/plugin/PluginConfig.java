/*
 * Copyright: (c) 2004-2013 Mayo Foundation for Medical Education and 
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
package edu.mayo.cts2.framework.core.plugin;

import java.io.File;

import edu.mayo.cts2.framework.core.config.ServerContext;
import edu.mayo.cts2.framework.core.config.option.OptionHolder;
import edu.mayo.cts2.framework.core.config.option.StringOption;

/**
 * Metadata for Plugin Configuration options.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class PluginConfig {

	private OptionHolder options;
	
	private File workDirectory;
	
	private ServerContext serverContext;

	/**
	 * Instantiates a new plugin config.
	 *
	 * @param options the options
	 * @param workDirectory the work directory
	 * @param serverContext the server context
	 */
	public PluginConfig(
			OptionHolder options, 
			File workDirectory,
			ServerContext serverContext){
		super();
		this.options = options;
		this.workDirectory = workDirectory;
		this.serverContext = serverContext;
	}

	/**
	 * Gets the string option.
	 *
	 * @param optionName the option name
	 * @return the string option
	 */
	public StringOption getStringOption(String optionName) {
		return options.getStringOption(optionName);
	}

	public File getWorkDirectory() {
		return workDirectory;
	}

	public ServerContext getServerContext() {
		return serverContext;
	}
}
