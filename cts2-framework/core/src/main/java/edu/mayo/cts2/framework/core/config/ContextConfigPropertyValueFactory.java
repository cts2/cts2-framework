package edu.mayo.cts2.framework.core.config;

import javax.annotation.Resource;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.util.Assert;

public class ContextConfigPropertyValueFactory implements FactoryBean<String> {

	@Resource
	private ServiceConfigManager serviceConfigManager;
	
	private String propertyName;
	
	@Override
	public String getObject() throws Exception {
		Assert.hasText(this.propertyName);
		
		String value = this.serviceConfigManager.getContextConfigProperty(this.propertyName);
		
		return value;
	}

	@Override
	public Class<?> getObjectType() {
		return String.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}
}
