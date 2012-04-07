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

import java.io.File;

import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import javax.naming.NoInitialContextException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The Class Cts2Config.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class ConfigInitializer {

	protected static Log log = LogFactory.getLog(ConfigInitializer.class);

	private static ConfigInitializer instance;
	
	protected static final String DEFAULT_CONTEXT = "default";

	private String context;

	private static final String DEFAULT_CTS2_CONFIG_DIRECTORY_PATH = System
			.getProperty("user.home") + File.separator + ".cts2";

	private String configDirectoryPath = DEFAULT_CTS2_CONFIG_DIRECTORY_PATH;

	private String pluginsDirectoryPath = configDirectoryPath + File.separator
			+ ConfigConstants.PLUGINS_DIRECTORY;
	
	private File configDirectory;
	private File contextConfigDirectory;
	private File pluginsDirectory;

	/**
	 * Instantiates a new cts2 config.
	 */
	private ConfigInitializer(String context) {
		super();

		this.configDirectoryPath = this.getVariable(ConfigConstants.JNDI_CONFIG_DIRECTORY,
				ConfigConstants.CTS2_CONFIG_DIRECTORY_ENV_VARIABLE, DEFAULT_CTS2_CONFIG_DIRECTORY_PATH);
		
		this.pluginsDirectoryPath = this.getVariable(ConfigConstants.JNDI_PLUGINS_DIRECTORY,
				ConfigConstants.CTS2_PLUGINS_DIRECTORY_ENV_VARIABLE, pluginsDirectoryPath);
		
		this.context = this.getVariable(ConfigConstants.JNDI_CONTEXT_ID,
				ConfigConstants.CTS2_CONTEXT_ENV_VARIABLE, context);
		
		log.info("CTS2 Development Framework using Configuration Directory: " + this.configDirectoryPath);
		log.info("CTS2 Development Framework using Plugin Directory: " + this.pluginsDirectoryPath);

		//create config directory if its not there
		this.configDirectory = ConfigUtils.createDirectory(this.configDirectoryPath);
		
		//create plugin directory if its not there
		this.pluginsDirectory = ConfigUtils.createDirectory(this.pluginsDirectoryPath);
		
		log.info("Webapp registered with service identifier: "
				+ this.context);
		log.info("Webapp may be configured at: "
				+ this.getContextPropertiesFilePath(this.context));

		this.contextConfigDirectory = ConfigUtils.createDirectory(
				this.getContextDirectoryPath(this.context));
	}
	
	/**
	 * Gets the variable.
	 *
	 * @param jndiName the jndi name
	 * @param systemVariableName the system variable name
	 * @param defaultValue the default value
	 * @return the variable
	 */
	private String getVariable(String jndiName, String systemVariableName,
			String defaultValue) {
		String sysVarValue = System.getProperty(systemVariableName);

		if (StringUtils.isNotBlank(sysVarValue)) {
			log.info("Using value: " + sysVarValue + " for Environment Variable: " + systemVariableName);
			return sysVarValue;
		}

		String jndiValue = null;
		try {
			InitialContext ctx = new InitialContext();
			jndiValue = (String) ctx.lookupLink("java:/comp/env/" + jndiName);
		} catch (NoInitialContextException e) {
			log.warn("No JNDI Context found.");
		} catch (NameNotFoundException e) {
			// this is ok, it means there is no JNDI name registered
		} catch (NamingException e) {
			throw new IllegalStateException(e);
		}

		if (StringUtils.isNotBlank(jndiValue)) {
			log.info("Using value: " + jndiValue + " for JNDI Variable: " + jndiName);
			return jndiValue;
		}

		log.info("Using default value: " + defaultValue);
		return defaultValue;
	}

	/**
	 * Instance.
	 *
	 * @return the cts2 config
	 */
	protected static synchronized ConfigInitializer instance() {
		if (instance == null) {
			throw new IllegalStateException(
					"FrameworkInitializer has not been initialized by the web container.");
		}
		return instance;
	}


	/**
	 * Initialize.
	 *
	 * @param context the context
	 * @throws Cts2ConfigAlreadyInitializedException the cts2 config already initialized exception
	 */
	protected static synchronized void initialize(String context) throws Cts2ConfigAlreadyInitializedException {
		if (instance == null) {
			if(StringUtils.isBlank(context)	|| 
					StringUtils.equals(context, "/")){
				context = DEFAULT_CONTEXT;
			}
			instance = new ConfigInitializer(context);
		} else {
			throw new IllegalStateException(
					"Cannot be initialized more than once.");
		}
	}

	/**
	 * Gets the context directory path.
	 *
	 * @param context the context
	 * @return the context directory path
	 */
	private String getContextDirectoryPath(String context) {
		return configDirectoryPath + File.separator + context;
	}

	/**
	 * Gets the context properties file path.
	 *
	 * @param context the context
	 * @return the context properties file path
	 */
	private String getContextPropertiesFilePath(String context) {
		return getContextDirectoryPath(context) + File.separator
				+ ConfigConstants.CONTEXT_PROPERTIES_FILE;
	}

	public File getContextConfigDirectory() {
		return this.contextConfigDirectory;
	}
	
	public File getPluginsDirectory() {
		return this.pluginsDirectory;
	}

	protected File getConfigDirectory() {
		return this.configDirectory;
	}
	
	protected String getContext(){
		return this.context;
	}
}
