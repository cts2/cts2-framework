package edu.mayo.cts2.framework.core.plugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.annotation.Resource;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceFactory;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.framework.launch.Framework;
import org.osgi.framework.launch.FrameworkFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.core.config.BasePluginConfigChangeObservable;
import edu.mayo.cts2.framework.core.config.ConfigConstants;
import edu.mayo.cts2.framework.core.config.ConfigInitializer;
import edu.mayo.cts2.framework.core.config.ConfigUtils;
import edu.mayo.cts2.framework.core.config.ServiceConfigManager;
import edu.mayo.cts2.framework.core.config.option.OptionHolder;

@Component
public class OsgiPluginManager extends BasePluginConfigChangeObservable 
	implements InitializingBean, DisposableBean, PluginManager {
	
	private Log log = LogFactory.getLog(getClass());
	
	private Framework framework;

	@Resource
	private ConfigInitializer configInitializer;
	
	@Resource
	private ServiceConfigManager serviceConfigManager;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		this.initialize();
	}
	
	protected void initialize() {
		ServiceLoader<FrameworkFactory> ff = ServiceLoader.load(FrameworkFactory.class);
		Map<String, String> config = new HashMap<String,String>();
		//config.put(Constants.FRAMEWORK_SYSTEMPACKAGES_EXTRA,
		//           "edu.mayo.cts2.framework.core.plugin");
		
		//config.put("osgi.compatibility.bootdelegation", "false");
		config.put("org.osgi.framework.bootdelegation", "*");

		//config.put("org.osgi.framework.bootdelegation", "edu.mayo.cts2.framework.*");
		//config.put("osgi.parentClassloader","fwk");
		
		config.put("org.osgi.framework.bundle.parent","framework");
		
		//config.put("felix.service.urlhandlers", "false");

		// add some params to config ...
		framework = ff.iterator().next().newFramework(config);
		
		try {
			framework.init();
		} catch (BundleException e) {
			throw new RuntimeException(e);
		}
		
		final BundleContext bundleContext = framework.getBundleContext();
		
		bundleContext.registerService(PluginConfig.class.getName(), new PluginConfigService(this), null);

	
		this.doInPluginDirectory(new DoInPluginDirectory(){

			@Override
			public void processPlugins(File plugin) {
					try {
						URI pluginPath = plugin.toURI();
						log.info("Installing Plugin: " + pluginPath);
						
						Bundle bundle = bundleContext.installBundle(pluginPath.toString());
						bundle.update();

					} catch (BundleException e) {
						throw new RuntimeException(e);
					}

			}
			
		});
		
		try {
			framework.start();
		} catch (BundleException e) {
			throw new RuntimeException(e);
		}
	}
	
	/* (non-Javadoc)
	 * @see edu.mayo.cts2.framework.core.plugin.IPluginManager#updatePluginSpecificConfigProperties(java.lang.String, java.util.Map)
	 */
	@Override
	public void updatePluginSpecificConfigProperties(
			String pluginName,
			Map<String,String> properties) {
		
		File propertiesFile = 
				this.getPluginSpecificConfigPropertiesFile(pluginName);
		
		for(Entry<String, String> entry : properties.entrySet()){
			
			ConfigUtils.updateProperty(
					entry.getKey(), 
					entry.getValue(), 
					propertiesFile);
		}

		this.firePluginSpecificConfigPropertiesChangeEvent(
				ConfigUtils.propertiesToOptionHolder(
						ConfigUtils.loadProperties(
								this.getPluginSpecificConfigPropertiesFile(pluginName))));
	}
	
	/* (non-Javadoc)
	 * @see edu.mayo.cts2.framework.core.plugin.IPluginManager#updatePluginSpecificConfigProperty(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void updatePluginSpecificConfigProperty(
			String pluginName,
			String propertyName, 
			String propertyValue) {
		
		File propertiesFile = 
				this.getPluginSpecificConfigPropertiesFile(pluginName);
		
		ConfigUtils.updateProperty(
				propertyName, 
				propertyValue, 
				propertiesFile);
		
		this.serviceConfigManager.fireContextConfigPropertiesChangeEvent(
				ConfigUtils.propertiesToOptionHolder(
						ConfigUtils.loadProperties(
								this.getPluginSpecificConfigPropertiesFile(pluginName))));
	}

	/* (non-Javadoc)
	 * @see edu.mayo.cts2.framework.core.plugin.IPluginManager#getPluginSpecificConfigProperties(java.lang.String)
	 */
	@Override
	public OptionHolder getPluginSpecificConfigProperties(String pluginName) {
		File file = this.getPluginSpecificConfigPropertiesFile(pluginName);
		
		return ConfigUtils.propertiesToOptionHolder(
				ConfigUtils.loadProperties(file));
	}
	
	private File getPluginSpecificConfigPropertiesFile(String pluginName) {
		return ConfigUtils.createFile(this.getPluginSpecificConfigPropertiesFilePath(pluginName));
	}
	
	private String getPluginSpecificConfigPropertiesFilePath(String pluginName) {
		return this.configInitializer.getContextConfigDirectory().getPath()
				+ File.separator
				+ pluginName + ".properties";
	}
	
	/* (non-Javadoc)
	 * @see edu.mayo.cts2.framework.core.plugin.IPluginManager#removePlugin(java.lang.String, java.lang.String)
	 */
	@Override
	public void removePlugin(String pluginName, String pluginVersion) {
		/*
		boolean isActive = this.isActivePlugin(pluginName, pluginVersion);
		
		if(isActive){
			this.activatePlugin("", "");
		}
		
		File pluginFile = this.findPluginFile(pluginName, pluginVersion);
		
		try {
			FileUtils.forceDelete(pluginFile);
			
			this.cleanUpAfterRemove(pluginName, pluginVersion);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		this.firePluginRemovedEvent(
				new PluginReference(pluginName, pluginVersion));
		*/
		throw new UnsupportedOperationException();
	}


	@Override
	@SuppressWarnings("unchecked")
	public <T> Iterable<PluginService<T>> getServices(Class<T> clazz) {

		ServiceReference<T>[] references;
		try {
			references = 
					(ServiceReference<T>[]) 
					this.framework.getBundleContext().getAllServiceReferences(clazz.getName(), null);
		} catch (InvalidSyntaxException e) {
			throw new RuntimeException(e);
		}
		
		List<PluginService<T>> services = new ArrayList<PluginService<T>>();
		
		for(ServiceReference<T> ref : references){

			T service = (T) this.framework.getBundleContext().getService(ref);
			
			services.add(new DefaultPluginService<T>(service, this.getServicePropertiesMap(ref)));
			
			this.framework.getRegisteredServices()[0].
		}
		
		return services;
	}
	
	private Map<String,?> getServicePropertiesMap(ServiceReference<?> ref){
		Map<String,Object> map = new HashMap<String,Object>();
		for(String key : ref.getPropertyKeys()){
			map.put(key, ref.getProperty(key));
		}
		
		return map;
	}

	/* (non-Javadoc)
	 * @see edu.mayo.cts2.framework.core.plugin.IPluginManager#getPluginDescription(java.lang.String, java.lang.String)
	 */
	@Override
	public PluginDescription getPluginDescription(String pluginName, String pluginVersion) {
		//File pluginFile = this.getPluginDirectory(pluginName, pluginVersion);
		
		//return this.toPluginDescription(pluginFile);
		
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see edu.mayo.cts2.framework.core.plugin.IPluginManager#getPluginDescriptions()
	 */
	@Override
	public Set<PluginDescription> getPluginDescriptions() {
		Set<PluginDescription> descriptions = new HashSet<PluginDescription>();
		
		for(Bundle bundle : this.framework.getBundleContext().getBundles()){
			descriptions.add(this.toPluginDescription(bundle));
		}
		
		return descriptions;
	}
	
	private PluginDescription toPluginDescription(Bundle bundle){
		String name = bundle.getSymbolicName();
		String version = bundle.getVersion().toString();
		boolean isActive = ( bundle.getState() == Bundle.ACTIVE );
		
		PluginDescription description = new PluginDescription(name, version, null, isActive);
		
		return description;
	}
	
	private boolean isActivePlugin(String name, String version){
		return StringUtils.equals(name, this.getInUsePluginName())
				&&
				StringUtils.equals(version, this.getInUsePluginVersion());
	}
	
	/* (non-Javadoc)
	 * @see edu.mayo.cts2.framework.core.plugin.IPluginManager#activatePlugin(java.lang.String, java.lang.String)
	 */
	@Override
	public void activatePlugin(String name, String version) {
		Map<String,String> propsMap = new HashMap<String,String>();
		
		propsMap.put(ConfigConstants.IN_USE_SERVICE_PLUGIN_NAME_PROP, name);
		propsMap.put(ConfigConstants.IN_USE_SERVICE_PLUGIN_VERSION_PROP, version);
		
		this.serviceConfigManager.updateContextConfigProperties(propsMap);
		
		this.initialize();
		
		this.firePluginActivatedEvent(new PluginReference(name, version));
	}

	/* (non-Javadoc)
	 * @see edu.mayo.cts2.framework.core.plugin.IPluginManager#installPlugin(java.io.InputStream)
	 */
	@Override
	public void installPlugin(InputStream source) throws IOException {

		String destination = this.configInitializer.getPluginsDirectory()
				.getPath();

		ZipInputStream zis = new ZipInputStream(source);
		
		File pluginDestinationFile = null;

		byte[] buffer = new byte[1024];
		try {
			for (ZipEntry zip; (zip = zis.getNextEntry()) != null;) {
				
				File file = new File(destination, zip.getName());
				if (zip.isDirectory()) {
					
					file.mkdir();
					
					//record the new Plugin directory
					if(pluginDestinationFile == null){
						pluginDestinationFile = file;
					}
			
				} else {
					FileOutputStream fos = null;
					try {
						fos = new FileOutputStream(file);
						for (int length; (length = zis.read(buffer)) > 0;) {
							fos.write(buffer, 0, length);
						}
					} finally {
						if (fos != null) {
							fos.close();
						}
					}
				}
				zis.closeEntry();
			}
		} finally {
			if (zis != null) {
				zis.closeEntry();
				zis.close();
			}
		}
		
		Properties pluginProps = this.getPluginProperties(pluginDestinationFile);
		
		String pluginName = this.getPluginName(pluginProps);
		String pluginVersion = this.getPluginVersion(pluginProps);
		
		this.firePluginAddedEvent(
				new PluginReference(pluginName, pluginVersion));
	}

	private Properties getPluginProperties(File plugin) {
		Properties props = ConfigUtils.loadProperties(
				new File(plugin.getPath()
					+ File.separator + ConfigConstants.PLUGIN_PROPERTIES_FILE_NAME));
		
		return props;
	}

	protected void doInPluginDirectory(DoInPluginDirectory pluginClosure){
		File pluginDirectory = this.configInitializer.getPluginsDirectory();
		File[] files = pluginDirectory.listFiles();
		
		if (! ArrayUtils.isEmpty(files)) {
			for (File plugin : pluginDirectory.listFiles()) {
				pluginClosure.processPlugins(plugin);
			}
		}
	}
	
	private String getInUsePluginName(){
		return (String) ConfigUtils.loadProperties(
				this.configInitializer.getContextConfigFile()).get(ConfigConstants.IN_USE_SERVICE_PLUGIN_NAME_PROP);
	}
	
	private String getInUsePluginVersion(){
		return (String) ConfigUtils.loadProperties(
				this.configInitializer.getContextConfigFile()).get(ConfigConstants.IN_USE_SERVICE_PLUGIN_VERSION_PROP);
	}

	/* (non-Javadoc)
	 * @see edu.mayo.cts2.framework.core.plugin.IPluginManager#getPluginConfig(java.lang.String)
	 */
	@Override
	public PluginConfig getPluginConfig(String pluginName){
		return new PluginConfig(
				this.getPluginSpecificConfigProperties(pluginName),
				this.getPluginWorkDirectory(pluginName),
				this.serviceConfigManager.getServerContext());
	}
	
	protected File getPluginWorkDirectory(String pluginName){
		File file = this.configInitializer.getContextConfigDirectory();
		
		return ConfigUtils.createDirectory(file.getPath()
						+ File.separator
						+ ".work"
						+ File.separator
						+ pluginName);
	}

	/* (non-Javadoc)
	 * @see edu.mayo.cts2.framework.core.plugin.IPluginManager#isActivePlugin(edu.mayo.cts2.framework.core.plugin.PluginReference)
	 */
	@Override
	public boolean isPluginActive(PluginReference ref) {
		return this.isActivePlugin(ref.getPluginName(), ref.getPluginVersion());
	}

	@Override
	public void dectivatePlugin(String name, String version) {
		//
	}

	@Override
	public void destroy() throws Exception {
		framework.stop();
	}

	private String getPluginName(Properties props){
		return props.getProperty(ConfigConstants.PLUGIN_NAME_PROP);
	}
	
	private String getPluginVersion(Properties props){
		return props.getProperty(ConfigConstants.PLUGIN_VERSION_PROP);
	}
	
	private static interface DoInPluginDirectory {
		
		public void processPlugins(File plugins);
	}

	protected ServiceConfigManager getServiceConfigManager() {
		return serviceConfigManager;
	}

	protected void setServiceConfigManager(ServiceConfigManager serviceConfigManager) {
		this.serviceConfigManager = serviceConfigManager;
	}

	protected ConfigInitializer getConfigInitializer() {
		return configInitializer;
	}

	protected void setConfigInitializer(ConfigInitializer configInitializer) {
		this.configInitializer = configInitializer;
	}
	
	public static class PluginConfigService implements ServiceFactory<PluginConfig> {
		
		private PluginManager pluginManager;
		
		public PluginConfigService(PluginManager pluginManager){
			this.pluginManager = pluginManager;
		}

		@Override
		public PluginConfig getService(Bundle bundle, ServiceRegistration<PluginConfig> registration) {
			return this.pluginManager.getPluginConfig(bundle.getSymbolicName());
		}

		@Override
		public void ungetService(Bundle bundle, ServiceRegistration<PluginConfig> registration, PluginConfig obj) {
			//
		}
		
	}
}
