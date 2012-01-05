package edu.mayo.cts2.framework.core.plugin;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.servlet.ServletContext;

import org.osgi.framework.BundleContext;
import org.reflections.Reflections;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.context.ServletContextAware;

import com.atlassian.plugin.DefaultModuleDescriptorFactory;
import com.atlassian.plugin.ModuleDescriptor;
import com.atlassian.plugin.Plugin;
import com.atlassian.plugin.PluginState;
import com.atlassian.plugin.event.impl.DefaultPluginEventManager;
import com.atlassian.plugin.hostcontainer.SimpleConstructorHostContainer;
import com.atlassian.plugin.main.AtlassianPlugins;
import com.atlassian.plugin.main.PluginsConfiguration;
import com.atlassian.plugin.main.PluginsConfigurationBuilder;
import com.atlassian.plugin.metadata.DefaultPluginMetadataManager;
import com.atlassian.plugin.metadata.PluginMetadataManager;
import com.atlassian.plugin.module.ModuleFactory;
import com.atlassian.plugin.osgi.container.PackageScannerConfiguration;
import com.atlassian.plugin.osgi.container.impl.DefaultPackageScannerConfiguration;
import com.atlassian.plugin.osgi.hostcomponents.HostComponentProvider;
import com.atlassian.plugin.servlet.DefaultServletModuleManager;
import com.atlassian.plugin.servlet.ServletModuleManager;
import com.atlassian.plugin.servlet.descriptors.ServletContextListenerModuleDescriptor;
import com.atlassian.plugin.servlet.descriptors.ServletFilterModuleDescriptor;
import com.atlassian.plugin.servlet.descriptors.ServletModuleDescriptor;
import com.atlassian.plugin.spring.AvailableToPlugins;
import com.atlassian.plugin.spring.SpringHostComponentProviderFactoryBean;

import edu.mayo.cts2.framework.core.config.ConfigInitializer;

@Component
public class AtlassianPluginManager implements PluginManager, InitializingBean, ServletContextAware {

	AtlassianPlugins plugins;

	@Resource
	private ConfigInitializer configInitializer;

	@Resource 
	private HostComponentProvider hostComponentProvider;
	
	ServletContext servletContext;
	
	@Component
	public static class Cts2SpringHostComponentProviderFactoryBean extends SpringHostComponentProviderFactoryBean{};
	
	@Component
	@AvailableToPlugins
	public static class Cts2SpringPluginMetadataManager implements PluginMetadataManager {
		private PluginMetadataManager delegate = new DefaultPluginMetadataManager();

		@Override
		public boolean isUserInstalled(Plugin plugin) {
			return this.delegate.isUserInstalled(plugin);
		}

		@Override
		public boolean isSystemProvided(Plugin plugin) {
			return this.delegate.isSystemProvided(plugin);
		}

		@Override
		public boolean isOptional(Plugin plugin) {
			return this.delegate.isOptional(plugin);
		}

		@Override
		public boolean isOptional(ModuleDescriptor<?> moduleDescriptor) {
			return this.delegate.isOptional(moduleDescriptor);
		}
	}
	
	public void afterPropertiesSet() throws Exception {

		PackageScannerConfiguration scannerConfig = new DefaultPackageScannerConfiguration();
		scannerConfig.getPackageIncludes().add("edu.mayo.cts2.*");
		scannerConfig.getPackageIncludes().add("org.jaxen*");
		scannerConfig.getPackageIncludes().add("javax.servlet*");
		scannerConfig.getPackageIncludes().add("com.sun*");
		scannerConfig.getPackageIncludes().add("org.json*");
		scannerConfig.getPackageExcludes().add("com.atlassian.plugins*");
	
		scannerConfig.getPackageExcludes().remove("org.apache.commons.logging*");
		scannerConfig.getPackageVersions().put("javax.servlet", "2.4");
		scannerConfig.getPackageVersions().put("javax.servlet.http", "2.4");
		
		// Determine which module descriptors, or extension points, to expose.
		// This 'on-start' module is used throughout this guide as an example only
		
		Map<Class<?>, Object> context = new HashMap<Class<?>, Object>();
		context.put(ModuleFactory.class, ModuleFactory.LEGACY_MODULE_FACTORY);
		context.put(ServletModuleManager.class, new DefaultServletModuleManager(
				this.servletContext, 
				new DefaultPluginEventManager()));
		
		SimpleConstructorHostContainer hostContainer = new SimpleConstructorHostContainer(context);
		
		DefaultModuleDescriptorFactory modules = new DefaultModuleDescriptorFactory(hostContainer);
		modules.addModuleDescriptor("servlet-filter", ServletFilterModuleDescriptor.class);
		modules.addModuleDescriptor("servlet", ServletModuleDescriptor.class);
		modules.addModuleDescriptor("servlet-context-listener", ServletContextListenerModuleDescriptor.class);
	
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
		
		servletContext.setAttribute(
				BundleContext.class.getName(),
				plugins.getOsgiContainerManager().getBundles()[0].getBundleContext());

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

	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	
}
