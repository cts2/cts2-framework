/*
 * Copyright: (c) 2004-2011 Mayo Foundation for Medical Education and 
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

import java.util.Dictionary;
import java.util.Hashtable;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.osgi.framework.Constants;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.metatype.MetaTypeProvider;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.core.plugin.ExportedService;


/**
 * The Class ServerContext.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@ExportedService( { ServerContext.class, MetaTypeProvider.class, ManagedService.class  })
@Component
public class RefreshableServerContext extends AbstractConfigurableExportedService 
	implements InitializingBean, ServerContext {
	
	private static final String DEFAULT_APPNAME = "webapp";
	private static final String DEFAULT_SERVERROOT = "http://localhost:8080";

	private String serverRoot = DEFAULT_SERVERROOT;

	private String appName = DEFAULT_APPNAME;

	@Resource
	private ConfigInitializer configInitializer;
	
	private boolean appNameUpdated = false;
	
	private Object mutex = new Object();
	
	@Override
	public void afterPropertiesSet() throws Exception {
		//set this to default to the context
		String context = this.configInitializer.getContext();

		synchronized(this.mutex){
			
			if(! this.appNameUpdated){
			
				if(StringUtils.equals(ConfigInitializer.DEFAULT_CONTEXT, context)){
					context = "";
				}
				
				this.appName = context;
			}
		}
	}
	
	@Override
	@SuppressWarnings("rawtypes") 
	public void updated(Dictionary properties) throws ConfigurationException {
		
		if(properties != null){
			String newServerRoot = (String) properties.get(ConfigConstants.SERVER_ROOT_PROPERTY);
			if(StringUtils.isNotBlank(newServerRoot)) {
				this.serverRoot = newServerRoot;
			}
			String newAppName = (String) properties.get(ConfigConstants.APP_NAME_PROPERTY);
			
			synchronized(this.mutex){
				if(newAppName != null) {
					this.appName = newAppName;
					appNameUpdated = true;
				}
			}
		}
	}

	@Override
	public String[] getLocales() {
		return null;
	}

	@Override
	protected String getMetatypeXmlPath() {
		return "/server-context-metatype.xml";
	}

	/**
	 * Gets the server root.
	 * 
	 * @return the server root
	 */
	public String getServerRoot() {
		return serverRoot;
	}

	/**
	 * Gets the server root with app name.
	 * 
	 * @return the server root with app name
	 */
	public String getServerRootWithAppName() {
		String appName = this.getAppName();
		
		if(StringUtils.isNotBlank(appName)){
			return this.getServerRoot() + "/" + this.getAppName();
		} else {
			return this.getServerRoot();
		}
	}

	/**
	 * Gets the app name.
	 * 
	 * @return the app name
	 */
	public String getAppName() {
		return appName;
	}

	@Override
	public Hashtable<String, Object> getMetadata() {
		Hashtable<String, Object> table = new Hashtable<String, Object>();
		table.put(Constants.SERVICE_PID, ServerContext.class.getSimpleName());
		
		return table;
	}

}
