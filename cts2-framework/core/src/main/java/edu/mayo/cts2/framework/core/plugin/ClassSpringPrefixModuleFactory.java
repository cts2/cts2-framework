package edu.mayo.cts2.framework.core.plugin;

import java.util.Collection;

import org.springframework.util.CollectionUtils;

import com.atlassian.plugin.PluginParseException;
import com.atlassian.plugin.hostcontainer.HostContainer;
import com.atlassian.plugin.osgi.spring.SpringContainerAccessor;

public class ClassSpringPrefixModuleFactory extends AbstractSpringPrefixModuleFactory{

	public ClassSpringPrefixModuleFactory(HostContainer hostContainer) {
		super(hostContainer);
	}

	@Override
	public String getPrefix() {
		return "class";
	}

	@Override
	protected <T> T doGetBean(SpringContainerAccessor containerAccessor, String name,
			Class<T> clazz) throws PluginParseException {
		Collection<T> collection = containerAccessor.getBeansOfType(clazz);
		
		if(CollectionUtils.isEmpty(collection)){
			throw new PluginParseException("Bean name: " + name + " ,class: " + clazz + " not found.");
		}
		
		if(collection.size() > 1){
			throw new PluginParseException("Bean name: " + name + " ,class: " + clazz + " occurs more than once in the bean definitions.");
		}
		
		return collection.iterator().next();
	}

}
