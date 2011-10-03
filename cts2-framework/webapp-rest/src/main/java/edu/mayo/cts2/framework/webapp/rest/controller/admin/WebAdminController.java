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
package edu.mayo.cts2.framework.webapp.rest.controller.admin;

import java.io.File;
import java.io.IOException;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import edu.mayo.cts2.framework.core.config.Cts2Config;
import edu.mayo.cts2.framework.core.config.Cts2Config.PropertyNamespace;
import edu.mayo.cts2.framework.service.admin.AdminService;
import edu.mayo.cts2.framework.service.admin.PluginDescription;
import edu.mayo.cts2.framework.service.admin.PluginReference;

/**
 * The Class WebAdminController.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Controller
public class WebAdminController {

	@Resource
	private Cts2Config cts2Config;
	
	@Resource
	private AdminService adminService;

	@RequestMapping
	public ModelAndView getValueSetDefinitionsOfValueSet() {

		ModelAndView mav = new ModelAndView("admin");

		return mav;
	}

	@RequestMapping(value = "/admin/")
	public ModelAndView getAdminView(@RequestBody PluginReference plugin) {
		return new ModelAndView("admin");
	}
	
	@RequestMapping(value = { "/admin/config/currentplugin" }, method = RequestMethod.GET)
	@ResponseBody
	public Properties getCurrentPluginConfigProperties() {
		return this.adminService.getCurrentPluginConfigProperties();
	}
	
	@RequestMapping(value = { "/admin/config/currentplugin" }, method = RequestMethod.PUT)
	@ResponseBody
	public void updatePluginConfigProperties(@RequestBody Properties properties) {
		for(Entry<Object, Object> entry : properties.entrySet()){
			this.cts2Config.
				setProperty(
						(String)entry.getKey(), 
						(String)entry.getValue(), PropertyNamespace.PLUGIN);
		}
	}
	
	@RequestMapping(value = { "/admin/plugins/active" }, method = RequestMethod.PUT)
	@ResponseBody
	public void activatePlugin(@RequestBody PluginReference plugin) {
		this.adminService.activatePlugin(plugin);
	}

	@RequestMapping(value = { "/admin/plugins" }, method = RequestMethod.GET)
	@ResponseBody
	public Set<PluginDescription> getPlugins() {
		return this.adminService.getPluginDescriptions();	
	}
	
	@RequestMapping(value = { "/admin/plugins" }, method = RequestMethod.POST)
	@ResponseBody
	public void addPlugin(@RequestParam MultipartFile file) throws IOException {

		this.adminService.installPlugin(
				file.getInputStream(),
				new File(this.cts2Config.getPluginsDirectory()));
	}

	@RequestMapping(value = { "/admin/plugin/{pluginName}/version/{pluginVersion}" }, method = RequestMethod.DELETE)
	@ResponseBody
	public void removePlugin(
			@PathVariable("pluginName") String pluginName,
			@PathVariable("pluginVersion") String pluginVersion) {
		
		this.adminService.removePlugin(pluginName, pluginVersion);
	}
	
	@RequestMapping(value = { "/admin/plugin/{pluginName}/version/{pluginVersion}" }, method = RequestMethod.GET)
	@ResponseBody
	public PluginDescription getPlugin(
			@PathVariable("pluginName") String pluginName,
			@PathVariable("pluginVersion") String pluginVersion){
		return this.adminService.getPluginDescription(pluginName, pluginVersion);
	}

	public static class UploadItem {
		private CommonsMultipartFile fileData;

		public CommonsMultipartFile getFileData() {
			return fileData;
		}

		public void setFileData(CommonsMultipartFile fileData) {
			this.fileData = fileData;
		}
	}
}