package edu.mayo.cts2.framework.core.config

import org.springframework.beans.factory.FactoryBean;

import edu.mayo.cts2.framework.core.config.Cts2Config;
import edu.mayo.cts2.framework.core.config.Cts2ConfigAlreadyInitializedException;

class Cts2TestConfigFactory implements FactoryBean {
	
	String context;

	@Override
	public Object getObject() throws Exception {
		try {
			Cts2Config.initialize(context)
		} catch (Cts2ConfigAlreadyInitializedException e){
			println "Context Alread Initialized!!"
		}
		Cts2Config.instance()
	}

	@Override
	public Class getObjectType() {
		Cts2Config.class
	}

	@Override
	public boolean isSingleton() {
		true
	}		
}
