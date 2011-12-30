package edu.mayo.cts2.framework.core.plugin;

import java.util.HashSet;
import java.util.Set;

import com.atlassian.plugin.descriptors.AbstractModuleDescriptor;
import com.atlassian.plugin.hostcontainer.DefaultHostContainer;
import com.atlassian.plugin.module.PrefixDelegatingModuleFactory;
import com.atlassian.plugin.module.PrefixModuleFactory;

public class DefaultModuleDescriptor extends AbstractModuleDescriptor<Object> {
	
	private static Set<PrefixModuleFactory> getModuleFactories(){
		Set<PrefixModuleFactory> prefixes = new HashSet<PrefixModuleFactory>();
		prefixes.add(new ClassSpringPrefixModuleFactory(new DefaultHostContainer()));
		prefixes.add(new BeanNameSpringPrefixModuleFactory(new DefaultHostContainer()));
		
		return prefixes;
	}
	
	public DefaultModuleDescriptor(){
		super(new PrefixDelegatingModuleFactory(getModuleFactories()));
		
	}
	
    public Object getModule()
    {
        return this.moduleFactory.createModule(moduleClassName, this);
    }
}