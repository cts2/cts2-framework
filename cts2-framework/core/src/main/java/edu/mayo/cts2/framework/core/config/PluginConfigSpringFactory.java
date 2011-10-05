package edu.mayo.cts2.framework.core.config;

import org.springframework.beans.factory.FactoryBean;

public class PluginConfigSpringFactory implements FactoryBean<PluginConfig>{

	@Override
	public PluginConfig getObject() throws Exception {
		return PluginConfigFactory.instance().getPluginConfig();
	}

	@Override
	public Class<?> getObjectType() {
		return PluginConfig.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}
}
