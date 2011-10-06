package edu.mayo.cts2.framework.core.config;

import java.io.File;
import java.util.Properties;

import javax.annotation.Resource;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.core.config.ConfigChangeListener.ConfigChangeCallback;
import edu.mayo.cts2.framework.core.config.option.OptionHolder;

@Component
public class ServiceConfigManager extends BaseReloadObservable implements InitializingBean {
	
	@Resource
	private ConfigInitializer configInitializer;

	private Properties globalProperties;
	private Properties contextProperties;

	@Override
	public void afterPropertiesSet() throws Exception {
		this.globalProperties = ConfigUtils.loadProperties(this.configInitializer.getGlobalConfigFile());
		this.contextProperties = ConfigUtils.loadProperties(this.configInitializer.getContextConfigFile());	
	
		ConfigChangeListener configChangeListener = 
				new ConfigChangeListener(
				this.configInitializer.getPluginsDirectory(),
				this.configInitializer.getContextConfigFile(),
				
				new ConfigChangeCallback(){

					@Override
					public void onConfigChange() {
						reload();
					}
					
				});
		
		configChangeListener.start();
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
		
		this.doSetProperty(
				propertyName, 
				propertyValue, 
				this.configInitializer.getGlobalConfigFile());
	}
	
	public void setContextConfigProperty(
			String propertyName,
			String propertyValue) {
		
		this.doSetProperty(
				propertyName, 
				propertyValue, 
				this.configInitializer.getContextConfigFile());
	}
	
	private void doSetProperty(
			String propertyName, 
			String propertyValue, 
			File propertiesFile){
		ConfigUtils.setProperty(
				propertyName, 
				propertyValue, 
				propertiesFile);
		
		this.reload();
	}

	public ServerContext getServerContext() {
		return this.configInitializer.getServerContext();
	}
	
	protected void reload() {
		this.fireReloadEvent();
	}
}
