package edu.mayo.cts2.framework.core.plugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;

import org.osgi.framework.Constants;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.core.config.ConfigInitializer;
import edu.mayo.cts2.framework.core.config.ConfigUtils;

@Component
public class SupplementalPropetiesLoader implements InitializingBean {
	
	private static final String CONFIG_DIR = "config";
	
	@Resource
	private ConfigInitializer configInitializer;

	private Map<String,Properties> overrides = new HashMap<String,Properties>();

	@Override
	public void afterPropertiesSet() throws Exception {
		File overridesDir = 
			new File(this.configInitializer.getContextConfigDirectory().getPath() + File.separator + CONFIG_DIR);
		
		if(overridesDir.exists()){
			for(File file : overridesDir.listFiles()){
				Properties props = ConfigUtils.loadProperties(file);
				String pid = (String) props.get(Constants.SERVICE_PID);
				
				if(pid == null){
					throw new IllegalStateException("Overriding Properties File must include a " + Constants.SERVICE_PID + " propery.");
				}
				
				this.overrides.put(pid, props);
			}
		}
	}
	
	
	protected Map<String,Properties> getOverriddenProperties(){
		return this.overrides;
	}
}
