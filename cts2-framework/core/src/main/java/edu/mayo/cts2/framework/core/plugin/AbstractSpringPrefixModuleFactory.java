package edu.mayo.cts2.framework.core.plugin;

import com.atlassian.plugin.ModuleDescriptor;
import com.atlassian.plugin.PluginParseException;
import com.atlassian.plugin.hostcontainer.HostContainer;
import com.atlassian.plugin.module.ContainerManagedPlugin;
import com.atlassian.plugin.module.ModuleClassNotFoundException;
import com.atlassian.plugin.module.PrefixModuleFactory;
import com.atlassian.plugin.osgi.spring.SpringContainerAccessor;

@SuppressWarnings("unchecked")
public abstract class AbstractSpringPrefixModuleFactory implements PrefixModuleFactory
{
    protected final HostContainer hostContainer;

    public AbstractSpringPrefixModuleFactory(final HostContainer hostContainer)
    {
        this.hostContainer = hostContainer;
    }

    public <T> T createModule(String name, ModuleDescriptor<T> moduleDescriptor) throws PluginParseException
    {

		Class<T> cls = getModuleClass(name, moduleDescriptor);

        if (moduleDescriptor.getPlugin() instanceof ContainerManagedPlugin)
        {
            ContainerManagedPlugin cmPlugin = (ContainerManagedPlugin) moduleDescriptor.getPlugin();
            return this.doGetBean((SpringContainerAccessor)cmPlugin.getContainerAccessor(), name, cls);
        }
        else if (cls != null)
        {
            return hostContainer.create(cls);
        }
        return null;
    }
    
    protected abstract <T> T doGetBean(SpringContainerAccessor containerAccessor, String name, Class<T> clazz) throws PluginParseException;

    @SuppressWarnings("rawtypes")
	private Class getModuleClass(final String name, final ModuleDescriptor moduleDescriptor) throws ModuleClassNotFoundException
    {
        try
        {
            return moduleDescriptor.getPlugin().loadClass(name, null);
        }
        catch (ClassNotFoundException e)
        {
            throw new ModuleClassNotFoundException(name, moduleDescriptor.getPluginKey(), moduleDescriptor.getKey(), e, createErrorMsg(name));
        }
    }

    private String createErrorMsg(String className)
    {
        StringBuilder builder = new StringBuilder();
        builder.append("Couldn't load the class '").append(className).append("'. ");
        builder.append("This could mean that you misspelled the name of the class (double check) or that ");
        builder.append("you're using a class in your plugin that you haven't provided bundle instructions for. ");
        builder.append("See http://confluence.atlassian.com/x/QRS-Cg for more details on how to fix this.");
        return builder.toString();
    }
}