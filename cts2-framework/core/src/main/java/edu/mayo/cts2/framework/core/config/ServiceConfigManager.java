package edu.mayo.cts2.framework.core.config;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.annotation.Resource;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.core.config.option.OptionHolder;

@Component
public class ServiceConfigManager extends BaseConfigChangeObservable 
	implements InitializingBean {
	
	@Resource
	private ConfigInitializer configInitializer;

	@Override
	public void afterPropertiesSet() throws Exception {
	    /*
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
		*/
		
		Map<String,String> contextConfigDefaults = new HashMap<String,String>();
		contextConfigDefaults.put(ConfigConstants.ADMIN_USERNAME_PROPERTY, ConfigConstants.DEFAULT_ADMIN_USERNAME_VALUE);
		contextConfigDefaults.put(ConfigConstants.ADMIN_PASSWORD_PROPERTY, ConfigConstants.DEFAULT_ADMIN_PASSWORD_VALUE);
	
		this.initializePropertiesFile(
				this.configInitializer.getContextConfigFile(), 
				contextConfigDefaults);
	}
	
	
	protected void initializePropertiesFile(File propertiesFile, Map<String,String> defaults){
		
		Properties props = ConfigUtils.loadProperties(propertiesFile);
		
		if(defaults != null){
			for(Entry<String, String> entry : defaults.entrySet()){
				if(! props.contains(entry.getKey())){
					ConfigUtils.addPropertyIfNotFound(
							entry.getKey(),
							entry.getValue(), 
							propertiesFile);
				}
			}
		}
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
		return this.doGetPropertyValue(this.configInitializer.getGlobalConfigFile(), propertyName);
	}
	
	public OptionHolder getGlobalConfigProperties(
			String propertyName) {
		return this.doGetProperties(this.configInitializer.getGlobalConfigFile());
	}
	
	public OptionHolder getContextConfigProperties(
			String propertyName) {
		return this.doGetProperties(this.configInitializer.getContextConfigFile());
	}
	
	public String getContextConfigProperty(
			String propertyName) {
		return this.doGetPropertyValue(this.configInitializer.getContextConfigFile(), propertyName);
	}
	
	private String doGetPropertyValue(File propertiesFile, String propertyName){
		return ConfigUtils.loadProperties(
				propertiesFile).getProperty(propertyName);
	}
	
	private OptionHolder doGetProperties(File propertiesFile){
		return ConfigUtils.propertiesToOptionHolder(
				ConfigUtils.loadProperties(
				propertiesFile));
	}

	public void updateGlobalConfigProperty(
			String propertyName, 
			String propertyValue) {
		
		OptionHolder newOptions = this.doUpdateProperty(
				propertyName, 
				propertyValue, 
				this.configInitializer.getGlobalConfigFile());
		
		this.fireGlobalConfigPropertiesChangeEvent(newOptions);
	}
	
	public void updateContextConfigProperty(
			String propertyName,
			String propertyValue) {
		
		OptionHolder newOptions = this.doUpdateProperty(
				propertyName, 
				propertyValue, 
				this.configInitializer.getContextConfigFile());
		
		this.fireContextConfigPropertiesChangeEvent(newOptions);
	}
	
	private OptionHolder doUpdateProperty(
			String propertyName, 
			String propertyValue, 
			File propertiesFile){
		
		ConfigUtils.updateProperty(
				propertyName, 
				propertyValue, 
				propertiesFile);
		
		return ConfigUtils.propertiesToOptionHolder(
				ConfigUtils.loadProperties(propertiesFile));
	}

	public ServerContext getServerContext() {
		return this.configInitializer.getServerContext();
	}
}
