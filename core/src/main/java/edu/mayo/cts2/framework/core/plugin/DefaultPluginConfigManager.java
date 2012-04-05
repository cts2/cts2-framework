package edu.mayo.cts2.framework.core.plugin;

import java.io.File;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.core.config.ConfigInitializer;
import edu.mayo.cts2.framework.core.config.ConfigUtils;
import edu.mayo.cts2.framework.core.config.ServerContext;

@Component
@ExportedService( { PluginConfigManager.class })
public class DefaultPluginConfigManager implements PluginConfigManager {
	
	@Resource
	private ServerContext serverContext;

	@Resource
	private ConfigInitializer configInitializer;

	public File getPluginWorkDirectory(String namespace) {
		File file = this.configInitializer.getContextConfigDirectory();

		return ConfigUtils.createDirectory(file.getPath() + File.separator
				+ ".work" + File.separator + namespace);
	}

	protected ConfigInitializer getConfigInitializer() {
		return configInitializer;
	}

	protected void setConfigInitializer(ConfigInitializer configInitializer) {
		this.configInitializer = configInitializer;
	}

	@Override
	public ServerContext getServerContext() {
		return this.serverContext;
	}
}
