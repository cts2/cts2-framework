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
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import javax.naming.NoInitialContextException;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The Class Cts2Config.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class Cts2Config implements ConfigChangeObservable {

	protected static Log log = LogFactory.getLog(Cts2Config.class);

	private static Cts2Config instance;

	private ConfigChangeListener configChangeListener;

	private String context;

	private ServerContext serverContext;

	private static final String DEFAULT_CTS2_CONFIG_DIRECTORY = System
			.getProperty("user.home") + File.separator + ".cts2";

	private String configDirectory = DEFAULT_CTS2_CONFIG_DIRECTORY;

	private String pluginsDirectory = configDirectory + File.separator
			+ ConfigConstants.PLUGINS_DIRECTORY;

	private File globalPropsFile;

	/**
	 * Instantiates a new cts2 config.
	 */
	private Cts2Config() {
		super();
		this.configDirectory = this.getVariable(ConfigConstants.JNDI_CONFIG_DIRECTORY,
				ConfigConstants.CTS2_CONFIG_DIRECTORY_ENV_VARIABLE, DEFAULT_CTS2_CONFIG_DIRECTORY);
		this.pluginsDirectory = this.getVariable(ConfigConstants.JNDI_PLUGINS_DIRECTORY,
				ConfigConstants.CTS2_PLUGINS_DIRECTORY_ENV_VARIABLE, pluginsDirectory);
		
		log.info("Cts2Sdk using Configuration Directory: " + this.configDirectory);
		log.info("Cts2Sdk using Configuration Directory: " + this.pluginsDirectory);
	}

	/**
	 * Gets the variable.
	 *
	 * @param jndiName the jndi name
	 * @param systemVariableName the system variable name
	 * @param defaultValue the default value
	 * @return the variable
	 */
	public String getVariable(String jndiName, String systemVariableName,
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
	public static synchronized Cts2Config instance() {
		if (instance == null) {
			throw new IllegalStateException(
					"Cts2Config has not been initialized by the web container.");
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

			instance = new Cts2Config();

			File file = new File(instance.configDirectory);
			if (!file.exists()) {
				file.mkdir();
			}

			instance.globalPropsFile = new File(instance.configDirectory
					+ "/global.properties");

			String jndiContext = instance.getVariable(ConfigConstants.JNDI_CONTEXT_ID,
					ConfigConstants.CTS2_CONTEXT_ENV_VARIABLE, context);
			instance.context = jndiContext;
			log.info("Webapp registered with service identifier: "
					+ instance.context);
			log.info("Webapp may be configured at: "
					+ instance.getContextPropertiesFilePath(instance.context));

			if (!instance.globalPropsFile.exists()) {
				try {
					instance.globalPropsFile.createNewFile();
				} catch (IOException e) {
					throw new IllegalStateException(e);
				}
			}

			File contextConfigDir = new File(
					instance.getContextDirectoryPath(instance.context));
			if (!contextConfigDir.exists()) {
				contextConfigDir.mkdir();
			}

			File contextConfigPropsFile = new File(
					instance.getContextPropertiesFilePath(instance.context));
			if (!contextConfigPropsFile.exists()) {
				try {
					contextConfigPropsFile.createNewFile();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}

			ConfigChangeListener configChangeListener = new ConfigChangeListener(
					new File(instance.getPluginsDirectory()),
					new File(instance
							.getContextPropertiesFilePath(instance.context)));

			configChangeListener.start();

			instance.configChangeListener = configChangeListener;

			instance.serverContext = new ServerContext(instance);

		} else {
			throw new IllegalStateException(
					"Cannot be initialized more than once.");
		}
	}
	
	public void setProperty(String propertyName, String propertyValue) {
		
		PropertiesConfiguration config;
		try {
			config = new PropertiesConfiguration(this.globalPropsFile);
			if(! config.containsKey(propertyName)){
				config = new PropertiesConfiguration(this.getContextConfigFilePath());
				if(!config.containsKey(propertyName)){
					throw new RuntimeException("Property: " + propertyName + " not found.");
				}
			}
		} catch (ConfigurationException e) {
			throw new IllegalStateException(e);
		}
		
		config.setProperty(propertyName, propertyValue);
		try {
			config.save();
		} catch (ConfigurationException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Gets the property.
	 *
	 * @param propertyName the property name
	 * @return the property
	 */
	public String getProperty(String propertyName) {
		return this.getProperty(propertyName, null);
	}

	/**
	 * Gets the property.
	 *
	 * @param propertyName the property name
	 * @param defaultValue the default value
	 * @return the property
	 */
	public String getProperty(String propertyName, String defaultValue) {

		String value = this.getContextProperties().getProperty(propertyName);
		if (value == null) {
			value = this.getGlobalProperties().getProperty(propertyName);
		}

		if (value == null) {
			return defaultValue;
		} else {
			return value;
		}
	}

	private Properties getGlobalProperties() {
		return this.doLoadProperties(this.globalPropsFile);
	}

	/**
	 * Gets the context directory path.
	 *
	 * @param context the context
	 * @return the context directory path
	 */
	private String getContextDirectoryPath(String context) {
		return configDirectory + File.separator + context;
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

	private Properties getContextProperties() {
		return this.doLoadProperties(new File(
				getContextPropertiesFilePath(context)));
	}

	/**
	 * Do load properties.
	 *
	 * @param file the file
	 * @return the properties
	 */
	private Properties doLoadProperties(File file) {
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				throw new IllegalStateException(e);
			}
		}

		Properties props = new Properties();
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			props.load(fis);
		} catch (Exception e) {
			throw new IllegalStateException(e);
		} finally {
			try {
				if (fis != null) {
					fis.close();
				}
			} catch (IOException e) {
				throw new IllegalStateException(e);
			}
		}

		return props;
	}

	public String getPluginsDirectory() {
		return this.pluginsDirectory;
	}

	public void setConfigDirectory(String configDirectory) {
		this.configDirectory = configDirectory;
	}

	public String getConfigDirectory() {
		return configDirectory;
	}

	protected String getContextConfigFilePath() {
		return getContextPropertiesFilePath(context);
	}

	/* (non-Javadoc)
	 * @see edu.mayo.cts2.framework.core.config.ConfigChangeObservable#registerListener(edu.mayo.cts2.framework.core.config.ConfigChangeObserver)
	 */
	public void registerListener(ConfigChangeObserver observer) {
		this.configChangeListener.registerListener(observer);
	}

	/* (non-Javadoc)
	 * @see edu.mayo.cts2.framework.core.config.ConfigChangeObservable#unregisterListener(edu.mayo.cts2.framework.core.config.ConfigChangeObserver)
	 */
	public void unregisterListener(ConfigChangeObserver observer) {
		this.configChangeListener.unregisterListener(observer);
	}

	public void setServerContext(ServerContext serverContext) {
		this.serverContext = serverContext;
	}

	public ServerContext getServerContext() {
		return serverContext;
	}
}
