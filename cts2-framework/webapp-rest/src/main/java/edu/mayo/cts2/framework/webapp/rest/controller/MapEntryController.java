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
package edu.mayo.cts2.framework.webapp.rest.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.mayo.cts2.framework.model.core.FilterComponent;
import edu.mayo.cts2.framework.model.core.Message;
import edu.mayo.cts2.framework.model.directory.DirectoryResult;
import edu.mayo.cts2.framework.model.mapversion.MapEntry;
import edu.mayo.cts2.framework.model.mapversion.MapEntryDirectory;
import edu.mayo.cts2.framework.model.mapversion.MapEntryDirectoryEntry;
import edu.mayo.cts2.framework.model.mapversion.MapEntryMsg;
import edu.mayo.cts2.framework.model.service.core.Query;
import edu.mayo.cts2.framework.model.service.exception.UnknownResourceReference;
import edu.mayo.cts2.framework.service.command.Filter;
import edu.mayo.cts2.framework.service.command.Page;
import edu.mayo.cts2.framework.service.command.restriction.MapEntryQueryServiceRestrictions;
import edu.mayo.cts2.framework.service.profile.mapentry.MapEntryMaintenanceService;
import edu.mayo.cts2.framework.service.profile.mapentry.MapEntryQueryService;
import edu.mayo.cts2.framework.service.profile.mapentry.MapEntryReadService;
import edu.mayo.cts2.framework.service.profile.mapentry.name.MapEntryReadId;

/**
 * The Class MapEntryController.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Controller
public class MapEntryController extends AbstractServiceAwareController {

	@Cts2Service
	private MapEntryReadService mapEntryReadService;
	
	@Cts2Service
	private MapEntryQueryService mapEntryQueryService;
	
	@Cts2Service
	private MapEntryMaintenanceService mapEntryMaintenanceService;
	
	private static UrlTemplateBinder<MapEntry> URL_BINDER = new 
			UrlTemplateBinder<MapEntry>(){

		@Override
		public String getValueForPathAttribute(String attribute, MapEntry resource) {
			//TODO
			return null;
		}

	};
	
	private final static MessageFactory<MapEntry> MESSAGE_FACTORY = 
			new MessageFactory<MapEntry>() {

		@Override
		public Message createMessage(MapEntry resource) {
			MapEntryMsg msg = new MapEntryMsg();
			msg.setEntry(resource);

			return msg;
		}
	};

	/**
	 * Creates the map entry.
	 *
	 * @param httpServletRequest the http servlet request
	 * @param mapEntry the map entry
	 * @param changeseturi the changeseturi
	 * @param mapName the map name
	 * @param mapVersionName the map version name
	 * @param mapsFromName the maps from name
	 */
	@RequestMapping(value=PATH_MAPENTRY_OF_MAPVERSION_BYID, method=RequestMethod.PUT)
	@ResponseBody
	public void createMapEntry(
			HttpServletRequest httpServletRequest,
			@RequestBody MapEntry mapEntry,
			@RequestParam(required=false) String changeseturi,
			@PathVariable(VAR_MAPID) String mapName,
			@PathVariable(VAR_MAPVERSIONID) String mapVersionName,
			@PathVariable(VAR_MAPENTRYID) String mapsFromName) {
			
		this.mapEntryMaintenanceService.createResource(mapEntry);
	}
	
	@RequestMapping(value=PATH_MAPENTRY_OF_MAPVERSION_BYID, method=RequestMethod.GET)
	@ResponseBody
	public Message getMapEntryByMapsFromName(
			HttpServletRequest httpServletRequest,
			@PathVariable(VAR_MAPID) String mapName,
			@PathVariable(VAR_MAPVERSIONID) String mapVersionName,
			@PathVariable(VAR_MAPENTRYID) String mapsFromName) {
		
		return this.getMapEntryByMapsFromName(
				httpServletRequest, 
				null,
				mapName, 
				mapVersionName,
				mapsFromName);
	}
	/**
	 * Gets the map entry by maps from name.
	 *
	 * @param httpServletRequest the http servlet request
	 * @param mapName the map name
	 * @param mapVersionName the map version name
	 * @param mapsFromName the maps from name
	 * @return the map entry by maps from name
	 */
	@RequestMapping(value=PATH_MAPENTRY_OF_MAPVERSION_BYID, method=RequestMethod.POST)
	@ResponseBody
	public Message getMapEntryByMapsFromName(
			HttpServletRequest httpServletRequest,
			@RequestBody Query query,
			@PathVariable(VAR_MAPID) String mapName,
			@PathVariable(VAR_MAPVERSIONID) String mapVersionName,
			@PathVariable(VAR_MAPENTRYID) String mapsFromName) {
			
		
		MapEntryReadId id = new MapEntryReadId(
				this.getScopedEntityName(mapsFromName), 
				mapVersionName);
		
		return this.doRead(
				httpServletRequest, 
				MESSAGE_FACTORY, 
				this.mapEntryReadService, 
				//TODO: This needs to be fixed
				UnknownResourceReference.class,id);
	}
	
	/**
	 * Gets the map entries.
	 *
	 * @param httpServletRequest the http servlet request
	 * @param restrictions the restrictions
	 * @param filter the filter
	 * @param page the page
	 * @param mapName the map name
	 * @param mapVersionName the map version name
	 * @return the map entries
	 */
	@RequestMapping(value={
			PATH_MAPENTRIES}, method=RequestMethod.GET)
	@ResponseBody
	public MapEntryDirectory getMapEntries(
			HttpServletRequest httpServletRequest,
			MapEntryQueryServiceRestrictions restrictions,
			Filter filter,
			Page page,
			@PathVariable(VAR_MAPID) String mapName,
			@PathVariable(VAR_MAPVERSIONID) String mapVersionName) {
		
		return this.getMapEntries(httpServletRequest, null, restrictions, filter, page, mapName, mapVersionName);
	}
	
	/**
	 * Gets the map entries.
	 *
	 * @param httpServletRequest the http servlet request
	 * @param query the query
	 * @param restrictions the restrictions
	 * @param filter the filter
	 * @param page the page
	 * @param mapName the map name
	 * @param mapVersionName the map version name
	 * @return the map entries
	 */
	@RequestMapping(value={
			PATH_MAPENTRIES}, method=RequestMethod.POST)
	@ResponseBody
	public MapEntryDirectory getMapEntries(
			HttpServletRequest httpServletRequest,
			@RequestBody Query query,
			MapEntryQueryServiceRestrictions restrictions,
			Filter filter,
			Page page,
			@PathVariable(VAR_MAPID) String mapName,
			@PathVariable(VAR_MAPVERSIONID) String mapVersionName) {

		FilterComponent filterComponent = this.processFilter(filter, this.mapEntryQueryService);

		DirectoryResult<MapEntryDirectoryEntry> directoryResult = 
			this.mapEntryQueryService.getResourceSummaries(query, filterComponent, restrictions, page);

		MapEntryDirectory directory = this.populateDirectory(
				directoryResult, 
				page, 
				httpServletRequest, 
				MapEntryDirectory.class);
		
		return directory;
	}
}