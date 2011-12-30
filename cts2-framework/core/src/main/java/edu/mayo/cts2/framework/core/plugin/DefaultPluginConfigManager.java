package edu.mayo.cts2.framework.core.plugin;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import com.atlassian.plugin.spring.AvailableToPlugins;

import edu.mayo.cts2.framework.core.config.ConfigConstants;
import edu.mayo.cts2.framework.core.config.ConfigInitializer;
import edu.mayo.cts2.framework.core.config.ConfigUtils;
import edu.mayo.cts2.framework.core.config.ServiceConfigManager;
import edu.mayo.cts2.framework.core.config.option.OptionHolder;

@Component
@AvailableToPlugins
public class DefaultPluginConfigManager implements PluginConfigManager, InitializingBean {

	private Log log = LogFactory.getLog(getClass());

	private PluginConfig pluginConfig;

	@Resource
	private ConfigInitializer configInitializer;

	@Resource
	private ServiceConfigManager serviceConfigManager;

	@Override
	public void afterPropertiesSet() throws Exception {
		this.initialize();
	}

	protected void initialize() {
		String inUsePluginName = this.getInUsePluginName();

		this.pluginConfig = new PluginConfig(
				this.getPluginSpecificConfigProperties(inUsePluginName),
				this.getPluginWorkDirectory(inUsePluginName),
				this.serviceConfigManager.getServerContext());
	}

	public void updatePluginSpecificConfigProperties(String namespace,
			Map<String, String> properties) {

		File propertiesFile = this
				.getPluginSpecificConfigPropertiesFile(namespace);

		for (Entry<String, String> entry : properties.entrySet()) {

			ConfigUtils.updateProperty(entry.getKey(), entry.getValue(),
					propertiesFile);
		}
/*
		this.firePluginSpecificConfigPropertiesChangeEvent(ConfigUtils
				.propertiesToOptionHolder(ConfigUtils.loadProperties(this
						.getPluginSpecificConfigPropertiesFile(pluginName))));
						*/
	}

	public void updatePluginSpecificConfigProperty(String namespace,
			String propertyName, String propertyValue) {

		File propertiesFile = this
				.getPluginSpecificConfigPropertiesFile(namespace);

		ConfigUtils.updateProperty(propertyName, propertyValue, propertiesFile);

		this.serviceConfigManager
				.fireContextConfigPropertiesChangeEvent(ConfigUtils.propertiesToOptionHolder(ConfigUtils.loadProperties(this
						.getPluginSpecificConfigPropertiesFile(namespace))));
	}

	public OptionHolder getPluginSpecificConfigProperties(String namespace) {
		File file = this.getPluginSpecificConfigPropertiesFile(namespace);

		return ConfigUtils.propertiesToOptionHolder(ConfigUtils
				.loadProperties(file));
	}

	private File getPluginSpecificConfigPropertiesFile(String namespace) {
		return ConfigUtils.createFile(this
				.getPluginSpecificConfigPropertiesFilePath(namespace));
	}

	private String getPluginSpecificConfigPropertiesFilePath(String namespace) {
		return this.configInitializer.getContextConfigDirectory().getPath()
				+ File.separator + namespace + ".properties";
	}

	public Set<PluginDescription> getPluginDescriptions() {

		return this
				.doInPluginDirectory(new DoInPluginDirectory<Set<PluginDescription>>() {

					public Set<PluginDescription> processPlugins(
							List<File> plugins) {
						Set<PluginDescription> returnSet = new HashSet<PluginDescription>();

						for (File plugin : plugins) {

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

		PluginDescription pluginDescription = new PluginDescription(name,
				version, description, isActive);

		return pluginDescription;
	}

	private boolean isActivePlugin(String name, String version) {
		return StringUtils.equals(name, this.getInUsePluginName())
				&& StringUtils.equals(version, this.getInUsePluginVersion());
	}


	private Properties getPluginProperties(File plugin) {
		Properties props = ConfigUtils
				.loadProperties(new File(plugin.getPath() + File.separator
						+ ConfigConstants.PLUGIN_PROPERTIES_FILE_NAME));

		return props;
	}


	protected <T> T doInPluginDirectory(DoInPluginDirectory<T> pluginClosure) {
		File pluginDirectory = this.configInitializer.getPluginsDirectory();
		File[] files = pluginDirectory.listFiles();

		List<File> returnList = new ArrayList<File>();

		if (!ArrayUtils.isEmpty(files)) {
			for (File plugin : pluginDirectory.listFiles()) {
				if (plugin.isDirectory()) {
					returnList.add(plugin);
				}
			}
		}

		return pluginClosure.processPlugins(returnList);
	}

	private String getInUsePluginName() {
		return (String) ConfigUtils.loadProperties(
				this.configInitializer.getContextConfigFile()).get(
				ConfigConstants.IN_USE_SERVICE_PLUGIN_NAME_PROP);
	}

	private String getInUsePluginVersion() {
		return (String) ConfigUtils.loadProperties(
				this.configInitializer.getContextConfigFile()).get(
				ConfigConstants.IN_USE_SERVICE_PLUGIN_VERSION_PROP);
	}

	@Override
	public PluginConfig getPluginConfig(String namespace) {
		return this.pluginConfig;
	}

	protected File getPluginWorkDirectory(String pluginName) {
		File file = this.configInitializer.getContextConfigDirectory();

		return ConfigUtils.createDirectory(file.getPath() + File.separator
				+ ".work" + File.separator + pluginName);
	}

	private String getPluginName(Properties props) {
		return props.getProperty(ConfigConstants.PLUGIN_NAME_PROP);
	}

	private String getPluginDescription(Properties props) {
		return props.getProperty(ConfigConstants.PLUGIN_DESCRIPTION_PROP);
	}

	private String getPluginVersion(Properties props) {
		return props.getProperty(ConfigConstants.PLUGIN_VERSION_PROP);
	}

	private static interface DoInPluginDirectory<T> {

		public T processPlugins(List<File> plugins);
	}

	protected ServiceConfigManager getServiceConfigManager() {
		return serviceConfigManager;
	}

	protected void setServiceConfigManager(
			ServiceConfigManager serviceConfigManager) {
		this.serviceConfigManager = serviceConfigManager;
	}

	protected ConfigInitializer getConfigInitializer() {
		return configInitializer;
	}

	protected void setConfigInitializer(ConfigInitializer configInitializer) {
		this.configInitializer = configInitializer;
	}
}
