package edu.mayo.cts2.framework.core.config

import java.io.File;

import org.springframework.beans.factory.FactoryBean

import edu.mayo.cts2.framework.core.config.option.OptionHolder;

class TestPluginConfigSpringFactory implements FactoryBean {
	
	private OptionHolder options;
	
	private File workDirectory;
	
	private ServerContext serverContext = new TestServerContext();

	@Override
	public Object getObject() throws Exception {
		new PluginConfig(options, workDirectory, serverContext)
	}

	@Override
	public Class getObjectType() {
		PluginConfig.class
	}

	@Override
	public boolean isSingleton() {
		true
	}		
}

class TestServerContext implements ServerContext {
	
	def serverRoot = "http://test.org"
	def appName = "testApp"

	@Override
	public String getServerRoot() {
		serverRoot
	}

	@Override
	public String getServerRootWithAppName() {
		getServerRoot() + "/" + getAppName()
	}

	@Override
	public String getAppName() {
		appName
	}
	
	
}