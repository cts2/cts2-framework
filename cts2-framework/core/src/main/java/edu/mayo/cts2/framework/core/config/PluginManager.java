package edu.mayo.cts2.framework.core.config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.core.config.option.OptionHolder;
import edu.mayo.cts2.framework.core.plugin.PluginClassLoader;

@Component
public class PluginManager implements InitializingBean {
	
	private Log log = LogFactory.getLog(getClass());
	
	private Properties inUsePluginProperties;
	
	private Map<String,ClassLoader> pluginClassLoaders =
			new HashMap<String,ClassLoader>();
	
	private PluginConfig pluginConfig;

	@Resource
	private ConfigInitializer configInitializer;
	
	@Override
	public void afterPropertiesSet() throws Exception {
			this.inUsePluginProperties =
					this.getPluginProperties(
						this.getInUsePluginDirectory());
			
			String inUsePluginName = this.getInUsePluginName();
			
			this.pluginConfig = 
					new PluginConfig(
							this.getPluginSpecificConfigProperties(inUsePluginName),
							this.getPluginWorkDirectory(inUsePluginName),
							this.configInitializer.getServerContext());
	}
	
	public void setPluginSpecificConfigProperties(
			String pluginName,
			String propertyName, 
			String propertyValue) {
		
		ConfigUtils.setProperty(
				propertyName, 
				propertyValue, 
				this.getPluginSpecificConfigPropertiesFile(pluginName));
	}

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
	
	public void removePlugin(String pluginName, String pluginVersion) {
		File pluginFile = this.findPluginFile(pluginName, pluginVersion);
		
		try {
			FileUtils.forceDelete(pluginFile);
			
			this.cleanUpAfterRemove(pluginName, pluginVersion);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	protected ClassLoader createClassLoaderForPlugin(String pluginName, String pluginVersion){
		return new PluginClassLoader(
				Thread.currentThread().getContextClassLoader(), 
				this.getPluginDirectory(pluginName, pluginVersion));
	}
	
	private void cleanUpAfterRemove(String pluginName, String pluginVersion){
		this.pluginClassLoaders.remove(this.getPluginKey(pluginName, pluginVersion));
	}

	public PluginDescription getPluginDescription(String pluginName, String pluginVersion) {
		File pluginFile = this.getPluginDirectory(pluginName, pluginVersion);
		
		return this.toPluginDescription(pluginFile);
	}

	private File getInUsePluginDirectory() {
		 return this.findPluginFile(this.getInUsePluginName(), this.getInUsePluginVersion());
	}
	
	private File getPluginDirectory(String pluginName, String pluginVersion) {
		 return this.findPluginFile(pluginName, pluginVersion);
	}

	public Set<PluginDescription> getPluginDescriptions() {
		
		return this.doInPluginDirectory(new DoInPluginDirectory<Set<PluginDescription>>(){

			public Set<PluginDescription> processPlugins(List<File> plugins) {
				Set<PluginDescription> returnSet = new HashSet<PluginDescription>();
				
				for(File plugin : plugins){

					PluginDescription pluginDescription = toPluginDescription(plugin);
					
					returnSet.add(pluginDescription);
				}
				
				return returnSet;
			}
		});
	}
	
	private PluginDescription toPluginDescription(File plugin) {
		Properties props = getPluginProperties(plugin);
		
		String name = getPluginName(props);
		String version = getPluginVersion(props);
		String description = getPluginDescription(props);
		boolean isActive = isActivePlugin(name, version);
		
		PluginDescription pluginDescription = new PluginDescription(
				name, 
				version,
				description,
				isActive);
		
		return pluginDescription;
	}

	private boolean isActivePlugin(String name, String version){
		return StringUtils.equals(name, this.getInUsePluginName())
				&&
				StringUtils.equals(version, this.getInUsePluginVersion());
	}
	
	public void activatePlugin(String name, String version) {
		
	}
	
	private File findPluginFile(final String pluginName, final String pluginVersion){
		return this.doInPluginDirectory(new DoInPluginDirectory<File>(){

			public File processPlugins(List<File> plugins) {
				for(File plugin : plugins){
	
					Properties props = getPluginProperties(plugin);
					
					String name = getPluginName(props);
					String version = getPluginVersion(props);
					
					if(StringUtils.equals(pluginName, name) &&
							StringUtils.equals(pluginVersion, version)){
						return plugin;
					}
				}
				
				log.warn("Plugin " + pluginName + " Version " + pluginVersion + " was not found.");
				return null;
			}
		});
	}

	/**
	 * Install plugin.
	 * 
	 * @param source
	 *            the source
	 * @param destination
	 *            the destination
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public void installPlugin(InputStream source) throws IOException {

		String destination = this.configInitializer.getPluginsDirectory()
				.getPath();

		ZipInputStream zis = new ZipInputStream(source);

		byte[] buffer = new byte[1024];
		try {
			for (ZipEntry zip; (zip = zis.getNextEntry()) != null;) {

				File file = new File(destination, zip.getName());
				if (zip.isDirectory()) {
					file.mkdir();
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
	}

	private Properties getPluginProperties(File plugin) {
		Properties props = ConfigUtils.loadProperties(
				new File(plugin.getPath()
					+ File.separator + ConfigConstants.PLUGIN_PROPERTIES_FILE_NAME));
		
		return props;
	}
	
	public String getCurrentPluginServiceProviderClassName(){
		File plugin = this.getInUsePluginDirectory();
		
		return this.getPluginProperties(plugin).getProperty(ConfigConstants.PLUGIN_PROVIDER_CLASS_PROP);
	}
	
	protected <T> T doInPluginDirectory(DoInPluginDirectory<T> pluginClosure){
		File pluginDirectory = this.configInitializer.getPluginsDirectory();
		File[] files = pluginDirectory.listFiles();
		
		List<File> returnList = new ArrayList<File>();
		
		if (! ArrayUtils.isEmpty(files)) {
			for (File plugin : pluginDirectory.listFiles()) {
				if (plugin.isDirectory()) {
					returnList.add(plugin);
				}
			}
		}
		
		return pluginClosure.processPlugins(returnList);
	}
	
	private String getInUsePluginName(){
		return (String) ConfigUtils.loadProperties(
				this.configInitializer.getContextConfigFile()).get(ConfigConstants.IN_USE_SERVICE_PLUGIN_NAME_PROP);
	}
	
	private String getInUsePluginVersion(){
		return (String) ConfigUtils.loadProperties(
				this.configInitializer.getContextConfigFile()).get(ConfigConstants.IN_USE_SERVICE_PLUGIN_VERSION_PROP);
	}
	
	public ClassLoader getPluginClassLoader(String pluginName, String pluginVersion){
		
		synchronized(this.pluginClassLoaders){
			String key = getPluginKey(pluginName, pluginVersion);
			
			if(! this.pluginClassLoaders.containsKey(key)){
				this.pluginClassLoaders.put(key, 
						createClassLoaderForPlugin(pluginName, pluginVersion));
			}
			
			return this.pluginClassLoaders.get(key);
		}
		
	}
	
	private String getPluginKey(String pluginName, String pluginVersion){
		return pluginName + pluginVersion;
	}
	
	public PluginConfig getPluginConfig(){
		return this.pluginConfig;
	}
	
	protected File getPluginWorkDirectory(String pluginName){
		return ConfigUtils.createDirectory(this.configInitializer.getContextConfigDirectory().getPath()
						+ File.separator
						+ ".work"
						+ File.separator
						+ pluginName);
	}
	
	public String getPluginServiceProviderClassName(String pluginName, String pluginVersion) {
		return this.inUsePluginProperties.getProperty(ConfigConstants.PLUGIN_PROVIDER_CLASS_PROP);
	}
	
	public PluginReference getActivePlugin() {
		return new PluginReference(
				this.getInUsePluginName(),
				this.getInUsePluginVersion());
	}
	
	private String getPluginName(Properties props){
		return props.getProperty(ConfigConstants.PLUGIN_NAME_PROP);
	}
	
	private String getPluginDescription(Properties props){
		return props.getProperty(ConfigConstants.PLUGIN_DESCRIPTION_PROP);
	}
	
	private String getPluginVersion(Properties props){
		return props.getProperty(ConfigConstants.PLUGIN_VERSION_PROP);
	}
	private static interface DoInPluginDirectory<T> {
		
		public T processPlugins(List<File> plugins);
	}
	
	protected ConfigInitializer getConfigInitializer() {
		return configInitializer;
	}

	protected void setConfigInitializer(ConfigInitializer configInitializer) {
		this.configInitializer = configInitializer;
	}
}
