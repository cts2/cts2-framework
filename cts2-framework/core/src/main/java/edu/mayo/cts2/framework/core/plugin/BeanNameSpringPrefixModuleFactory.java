package edu.mayo.cts2.framework.core.plugin;

import com.atlassian.plugin.PluginParseException;
import com.atlassian.plugin.hostcontainer.HostContainer;
import com.atlassian.plugin.osgi.spring.SpringContainerAccessor;

public class BeanNameSpringPrefixModuleFactory extends AbstractSpringPrefixModuleFactory{

	public BeanNameSpringPrefixModuleFactory(HostContainer hostContainer) {
		super(hostContainer);
	}

	@Override
	public String getPrefix() {
		return "bean";
	}

	@SuppressWarnings("unchecked")
	@Override
	protected <T> T doGetBean(SpringContainerAccessor containerAccessor, String name,
			Class<T> clazz) throws PluginParseException {
		return (T) containerAccessor.getBean(name);
	}

}
