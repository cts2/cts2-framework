package edu.mayo.cts2.framework.core.plugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import com.atlassian.plugin.spring.AvailableToPlugins;

import edu.mayo.cts2.framework.core.config.ConfigConstants;
import edu.mayo.cts2.framework.core.config.ConfigInitializer;
import edu.mayo.cts2.framework.core.config.ConfigUtils;
import edu.mayo.cts2.framework.core.config.ServerContext;
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
				this.getPluginConfigProperties(inUsePluginName),
				this.getPluginWorkDirectory(inUsePluginName),
				this.serviceConfigManager.getServerContext());
	}

	public void updatePluginConfigProperties(String namespace,
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

	public void updatePluginConfigProperty(String namespace,
			String propertyName, String propertyValue) {

		File propertiesFile = this
				.getPluginSpecificConfigPropertiesFile(namespace);

		ConfigUtils.updateProperty(propertyName, propertyValue, propertiesFile);

		this.serviceConfigManager
				.fireContextConfigPropertiesChangeEvent(ConfigUtils.propertiesToOptionHolder(ConfigUtils.loadProperties(this
						.getPluginSpecificConfigPropertiesFile(namespace))));
	}

	public OptionHolder getPluginConfigProperties(String namespace) {
		File file = this.getPluginSpecificConfigPropertiesFile(namespace);

		return ConfigUtils.propertiesToOptionHolder(ConfigUtils
				.loadProperties(file));
	}

	@Override
	public ServerContext getServerContext() {
		return this.serviceConfigManager.getServerContext();
	}
	
	private File getPluginSpecificConfigPropertiesFile(String namespace) {
		return ConfigUtils.createFile(this
				.getPluginSpecificConfigPropertiesFilePath(namespace));
	}

	private String getPluginSpecificConfigPropertiesFilePath(String namespace) {
		return this.configInitializer.getContextConfigDirectory().getPath()
				+ File.separator + namespace + ".properties";
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

	@Override
	public PluginConfig getPluginConfig(String namespace) {
		return this.pluginConfig;
	}

	protected File getPluginWorkDirectory(String pluginName) {
		File file = this.configInitializer.getContextConfigDirectory();

		return ConfigUtils.createDirectory(file.getPath() + File.separator
				+ ".work" + File.separator + pluginName);
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
