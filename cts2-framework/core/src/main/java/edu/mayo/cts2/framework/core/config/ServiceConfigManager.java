package edu.mayo.cts2.framework.core.config;

import java.util.Properties;

import javax.annotation.Resource;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.core.config.option.OptionHolder;

@Component
public class ServiceConfigManager implements InitializingBean {
	
	@Resource
	private ConfigInitializer configInitializer;

	private Properties globalProperties;
	private Properties contextProperties;

	@Override
	public void afterPropertiesSet() throws Exception {
		this.globalProperties = ConfigUtils.loadProperties(this.configInitializer.getGlobalConfigFile());
		this.contextProperties = ConfigUtils.loadProperties(this.configInitializer.getContextConfigFile());	
	}
	
	/**
	 * Gets the property.
	 *
	 * @param propertyName the property name
	 * @param defaultValue the default value
	 * @return the property
	 */
	public String getGlobalConfigProperty(
			String propertyName) {
		return this.globalProperties.getProperty(propertyName);
	}
	
	public OptionHolder getGlobalConfigProperties(
			String propertyName) {
		return ConfigUtils.propertiesToOptionHolder(this.globalProperties);
	}
	
	public OptionHolder getContextConfigProperties(
			String propertyName) {
		return ConfigUtils.propertiesToOptionHolder(this.contextProperties);
	}
	
	public String getContextConfigProperty(
			String propertyName) {
		return this.contextProperties.getProperty(propertyName);
	}

	public void setGlobalConfigProperty(
			String propertyName, 
			String propertyValue) {
		ConfigUtils.setProperty(
				propertyName, 
				propertyValue, 
				this.configInitializer.getGlobalConfigFile());
	}
	
	public void setContextConfigProperty(
			String propertyName,
			String propertyValue) {
		ConfigUtils.setProperty(
				propertyName, 
				propertyValue, 
				this.configInitializer.getContextConfigFile());
	}

	public ServerContext getServerContext() {
		return this.configInitializer.getServerContext();
	}
}
