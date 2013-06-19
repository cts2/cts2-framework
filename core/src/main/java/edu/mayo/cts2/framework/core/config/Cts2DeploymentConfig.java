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
package edu.mayo.cts2.framework.core.config;

import java.io.File;
import java.util.Properties;

import javax.annotation.Resource;

import org.apache.commons.lang.BooleanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * A general configuration point for deployment related aspects. Specific properties are not
 * specified, as upstream modules may have different deployment configuration needs. 
 * 
 * Some examples of how this can be used:
 * 
 * - To fully or partially start OSGi.
 * - To expose (or not) various Web Resources (HTML pages, SOAP Services, etc).
 * 
 * This will be configured by a 'cts2-deployment.properties' file in the CTS2 config directory.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Component
public class Cts2DeploymentConfig implements InitializingBean {
	
	@Resource
	private ConfigInitializer configInitializer;
	
	private Properties cts2ConfigProperties;

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		File cts2ConfigFile = 
			new File(
				this.configInitializer.getContextConfigDirectory().getPath() + 
				File.separator + 
				ConfigConstants.CONFIG_DIRECTORY +
				File.separator + ConfigConstants.CTS2_DEPLOYMENT_CONFIG_FILE_NAME);
		
		if(! cts2ConfigFile.exists()){
			cts2ConfigFile.createNewFile();
		}
		
		this.cts2ConfigProperties = ConfigUtils.loadProperties(cts2ConfigFile);
	}
	
	/**
	 * Gets the boolean property.
	 *
	 * @param propertyName the property name
	 * @return the boolean property
	 */
	public boolean getBooleanProperty(String propertyName){
		return BooleanUtils.toBoolean(this.cts2ConfigProperties.getProperty(propertyName));
	}
	
	/**
	 * Gets the string property.
	 *
	 * @param propertyName the property name
	 * @return the string property
	 */
	public String getStringProperty(String propertyName){
		return this.cts2ConfigProperties.getProperty(propertyName);
	}

}
