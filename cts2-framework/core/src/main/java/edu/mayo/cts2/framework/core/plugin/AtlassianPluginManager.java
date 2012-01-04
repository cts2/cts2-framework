package edu.mayo.cts2.framework.core.plugin;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.reflections.Reflections;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.atlassian.plugin.DefaultModuleDescriptorFactory;
import com.atlassian.plugin.PluginState;
import com.atlassian.plugin.hostcontainer.DefaultHostContainer;
import com.atlassian.plugin.main.AtlassianPlugins;
import com.atlassian.plugin.main.PluginsConfiguration;
import com.atlassian.plugin.main.PluginsConfigurationBuilder;
import com.atlassian.plugin.osgi.container.PackageScannerConfiguration;
import com.atlassian.plugin.osgi.container.impl.DefaultPackageScannerConfiguration;
import com.atlassian.plugin.osgi.hostcomponents.HostComponentProvider;
import com.atlassian.plugin.spring.SpringHostComponentProviderFactoryBean;

import edu.mayo.cts2.framework.core.config.ConfigInitializer;

@Component
public class AtlassianPluginManager implements PluginManager, InitializingBean {

	private AtlassianPlugins plugins;

	@Resource
	private ConfigInitializer configInitializer;

	@Resource 
	private HostComponentProvider hostComponentProvider;
	
	@Component
	public static class Cts2SpringHostComponentProviderFactoryBean extends SpringHostComponentProviderFactoryBean{};
	
	public void afterPropertiesSet() throws Exception {

		PackageScannerConfiguration scannerConfig = new DefaultPackageScannerConfiguration();
		scannerConfig.getPackageIncludes().add("edu.mayo.cts2.*");
		scannerConfig.getPackageIncludes().add("org.jaxen*");
		scannerConfig.getPackageIncludes().add("javax.servlet*");
	
		scannerConfig.getPackageExcludes().remove("org.apache.commons.logging*");
		
		// Determine which module descriptors, or extension points, to expose.
		// This 'on-start' module is used throughout this guide as an example only
		DefaultModuleDescriptorFactory modules = new DefaultModuleDescriptorFactory(new DefaultHostContainer());
	
		Reflections reflections = new Reflections("edu.mayo.cts2");

		Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(ExtensionPointDescriptor.class);

		for(Class<?> clazz : annotated){
			ExtensionPointDescriptor descriptor = clazz.getAnnotation(ExtensionPointDescriptor.class);

			Assert.notNull(descriptor, "Every Extension Point must have an ExtensionPointXmlDescriptor annotation.");
			
			modules.addModuleDescriptor(descriptor.xmlPrefix(), descriptor.descriptor());
		}
		
		PluginsConfiguration config = new PluginsConfigurationBuilder()
	        .pluginDirectory(this.configInitializer.getPluginsDirectory())
	        .hostComponentProvider(this.hostComponentProvider)
	        .packageScannerConfiguration(scannerConfig)
	        .hotDeployPollingFrequency(2, TimeUnit.SECONDS)
	        .moduleDescriptorFactory(modules)
	        .build();
		
		// Start the plugin framework
		plugins = new AtlassianPlugins(config);
	
		plugins.start();
	}
	
	@Override
	public void registerExtensionPoint(ExtensionPoint<?> extensionPoint) {
		extensionPoint.init(this.plugins);
	}

	@Override
	public void removePlugin(String pluginName, String pluginVersion) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public PluginDescription getPluginDescription(String pluginName,
			String pluginVersion) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<PluginDescription> getPluginDescriptions() {
		Set<PluginDescription> returnSet = new HashSet<PluginDescription>();
		
		for(com.atlassian.plugin.Plugin plugin : this.plugins.getPluginAccessor().getPlugins()){
			
			returnSet.add(new PluginDescription(
					plugin.getName(), 
					plugin.getPluginInformation().getVersion(),
					plugin.getPluginInformation().getDescription(), plugin.getPluginState().equals(PluginState.ENABLED)));
		}
		
		return returnSet;
	}

	@Override
	public void activatePlugin(String name, String version) {
		this.plugins.getPluginController().disablePlugin(name);
		
	}

	@Override
	public void dectivatePlugin(String name, String version) {
		this.plugins.getPluginController().disablePlugin(name);
	}

	@Override
	public void installPlugin(InputStream source) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isPluginActive(PluginReference ref) {
		// TODO Auto-generated method stub
		return false;
	}

	
}
