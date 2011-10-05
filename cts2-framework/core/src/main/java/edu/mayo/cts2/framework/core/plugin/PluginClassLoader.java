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
package edu.mayo.cts2.framework.core.plugin;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.tools.ant.AntClassLoader;

/**
 * The Class PluginClassLoader.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class PluginClassLoader extends AntClassLoader {
	
	private final static String LIB_DIR = File.separator + "lib";
	
	private URL pluginJarUrl;
	
	public PluginClassLoader(ClassLoader parent, File pluginDir) {
		this(parent, pluginDir.getPath());
	}

	/**
	 * Instantiates a new plugin class loader.
	 *
	 * @param parent the parent
	 * @param pluginDir the plugin dir
	 */
	public PluginClassLoader(ClassLoader parent, String pluginDir) {
		super(parent, false);
		
		for(URL url : this.getAllJarUrls(pluginDir)){
			try {
				this.addPathFile(new File(url.getFile()));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	/**
	 * Gets the all jar urls.
	 *
	 * @param dir the dir
	 * @return the all jar urls
	 */
	protected URL[] getAllJarUrls(String dir){
		List<String> returnList = new ArrayList<String>();
		String pluginUrl = getJars(dir)[0];
		try {
			this.pluginJarUrl = new File(pluginUrl).toURI().toURL();
		} catch (MalformedURLException e) {
			throw new IllegalStateException(e);
		}
		
		returnList.add(pluginUrl);
		returnList.addAll(Arrays.asList(getJars(dir + LIB_DIR)));
		
		URL[] urls = new URL[returnList.size()];
		for(int i=0;i<returnList.size();i++){
			try {
				File file = new File(returnList.get(i));
				urls[i] = file.toURI().toURL();
				
			} catch (MalformedURLException e) {
				throw new IllegalStateException(e);
			}
		}
		
		return urls;
	}
	
	/**
	 * Gets the jars.
	 *
	 * @param dir the dir
	 * @return the jars
	 */
	protected String[] getJars(String dir){
		File plugin = new File(dir);
		String[] files = plugin.list(new FilenameFilter(){

			public boolean accept(File dir, String name) {
				return name.endsWith(".jar");
			}
			
		});

		for(int i=0;i<files.length;i++){
			files[i] = dir + File.separator + files[i];
		}
		
		return files;
	}

	public URL getPluginJarUrl() {
		return pluginJarUrl;
	}

}
