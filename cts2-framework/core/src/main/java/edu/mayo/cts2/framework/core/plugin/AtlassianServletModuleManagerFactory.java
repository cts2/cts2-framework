package edu.mayo.cts2.framework.core.plugin;

import org.springframework.stereotype.Component;

import com.atlassian.plugin.event.impl.DefaultPluginEventManager;
import com.atlassian.plugin.servlet.DefaultServletModuleManager;
import com.atlassian.plugin.spring.AvailableToPlugins;

@Component
@AvailableToPlugins
public class AtlassianServletModuleManagerFactory extends DefaultServletModuleManager {

	public AtlassianServletModuleManagerFactory() {
		super(new DefaultPluginEventManager());
	}

}