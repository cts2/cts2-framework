/*
 * Copyright: (c) 2004-2013 Mayo Foundation for Medical Education and 
 * Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
 * triple-shield Mayo logo are trademarks and service marks of MFMER.
 *
 * Except as contained in the copyright notice above, or as used to identify 
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

/**
 * The Class SupplementalPropetiesLoader.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Component
public class SupplementalPropetiesLoader implements InitializingBean {
	
	private static final String CONFIG_DIR = "config";
	
	@Resource
	private ConfigInitializer configInitializer;

	private Map<String,Properties> overrides = new HashMap<String,Properties>();

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
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
