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
package edu.mayo.cts2.framework.service.admin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.core.config.ConfigConstants;
import edu.mayo.cts2.framework.core.config.Cts2Config;
import edu.mayo.cts2.framework.core.config.Cts2Config.PropertyNamespace;

/**
 * The Class AdminService.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Component
public class AdminService {

	private Log log = LogFactory.getLog(getClass());

	@Resource
	private Cts2Config cts2Config;

	public void removePlugin(String pluginName, String pluginVersion) {
		File pluginFile = this.findPluginFile(pluginName, pluginVersion);
		
		try {
			FileUtils.forceDelete(pluginFile);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public PluginDescription getPluginDescription(String pluginName, String pluginVersion) {
		File pluginFile = this.getPluginDirectory(pluginName, pluginVersion);
		
		return this.toPluginDescription(pluginFile);
	}
	
	public File getInUsePluginDirectory() {
		 return this.findPluginFile(this.getInUsePluginName(), this.getInUsePluginVersion());
	}
	
	public File getPluginDirectory(String pluginName, String pluginVersion) {
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
	
	public Properties getContextConfigProperties(){
		return this.cts2Config.getContextConfigProperties();
	}
	
	public Properties getCurrentPluginConfigProperties(){
		String name = this.getInUsePluginName();
		
		return this.cts2Config.getPluginConfigProperties(name);
	}
	
	private boolean isActivePlugin(String name, String version){
		return StringUtils.equals(name, this.getInUsePluginName())
				&&
				StringUtils.equals(version, this.getInUsePluginVersion());
	}
	
	public void activatePlugin(PluginReference plugin) {
		this.cts2Config.setProperty(
				ConfigConstants.IN_USE_SERVICE_PLUGIN_NAME_PROP, plugin.getPluginName(), PropertyNamespace.CONTEXT);	
		this.cts2Config.setProperty(
				ConfigConstants.IN_USE_SERVICE_PLUGIN_VERSION_PROP, plugin.getPluginVersion(), PropertyNamespace.CONTEXT);	
	
		this.cts2Config.refresh();
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
	public void installPlugin(InputStream source, File destination)
			throws IOException {
		ZipInputStream zis = new ZipInputStream(source);
		byte[] buffer = new byte[1024];
		for (ZipEntry zip; (zip = zis.getNextEntry()) != null;) {
			File file = new File(destination, zip.getName());
			if (zip.isDirectory()) {
				file.mkdir();
			} else {
				FileOutputStream fos = new FileOutputStream(file);
				for (int length; (length = zis.read(buffer)) > 0;) {
					fos.write(buffer, 0, length);
				}
				fos.close();
			}
			zis.closeEntry();
		}
		zis.close();
	}

	private Properties getPluginProperties(File plugin) {
		Properties props = new Properties();
		try {
			props.load(new FileInputStream(new File(plugin.getPath()
					+ File.separator + ConfigConstants.PLUGIN_PROPERTIES_FILE_NAME)));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return props;
	}
	
	public String getCurrentPluginServiceProviderClassName(){
		File plugin = this.getInUsePluginDirectory();
		
		return this.getPluginProperties(plugin).getProperty(ConfigConstants.PLUGIN_PROVIDER_CLASS_PROP);
	}
	
	protected <T> T doInPluginDirectory(DoInPluginDirectory<T> pluginClosure){
		File pluginDirectory = new File(this.cts2Config.getPluginsDirectory());
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
		return this.cts2Config.getProperty(ConfigConstants.IN_USE_SERVICE_PLUGIN_NAME_PROP, PropertyNamespace.CONTEXT);	
	}
	
	private String getInUsePluginVersion(){
		return this.cts2Config.getProperty(ConfigConstants.IN_USE_SERVICE_PLUGIN_VERSION_PROP, PropertyNamespace.CONTEXT);	
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
}
