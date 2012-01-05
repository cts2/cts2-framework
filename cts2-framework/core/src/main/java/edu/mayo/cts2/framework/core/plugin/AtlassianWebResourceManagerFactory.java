package edu.mayo.cts2.framework.core.plugin;

import org.springframework.stereotype.Component;

import com.atlassian.plugin.spring.AvailableToPlugins;
import com.atlassian.plugin.webresource.WebResourceManagerImpl;

@Component
@AvailableToPlugins
public class AtlassianWebResourceManagerFactory extends WebResourceManagerImpl {

	public AtlassianWebResourceManagerFactory() {
		super(null, null, null);
	}

	
}