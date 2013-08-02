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

import java.io.IOException;
import java.net.URL;
import java.util.Set;

/**
 * A service for managing plugins to the service.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface PluginManager {


	/**
	 * Removes the plugin.
	 *
	 * @param pluginName the plugin name
	 * @param pluginVersion the plugin version
	 */
	public void removePlugin(String pluginName, String pluginVersion);

	/**
	 * Gets the plugin description.
	 *
	 * @param pluginName the plugin name
	 * @param pluginVersion the plugin version
	 * @return the plugin description
	 */
	public PluginDescription getPluginDescription(
			String pluginName,
			String pluginVersion);

	public Set<PluginDescription> getPluginDescriptions();

	/**
	 * Activate plugin.
	 *
	 * @param name the name
	 * @param version the version
	 */
	public void activatePlugin(String name, String version);
	
	/**
	 * Dectivate plugin.
	 *
	 * @param name the name
	 * @param version the version
	 */
	public void dectivatePlugin(String name, String version);
	
	/**
	 * Install plugin.
	 *
	 * @param source the source
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void installPlugin(URL source) throws IOException;

	/**
	 * Checks if is plugin active.
	 *
	 * @param ref the ref
	 * @return true, if is plugin active
	 */
	public boolean isPluginActive(PluginReference ref);

	/**
	 * Register extension point.
	 *
	 * @param extensionPoint the extension point
	 */
	public void registerExtensionPoint(ExtensionPoint extensionPoint);

}