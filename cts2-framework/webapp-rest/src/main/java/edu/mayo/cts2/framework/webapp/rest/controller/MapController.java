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
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.mayo.cts2.framework.model.core.FilterComponent;
import edu.mayo.cts2.framework.model.directory.DirectoryResult;
import edu.mayo.cts2.framework.model.map.MapCatalogEntry;
import edu.mayo.cts2.framework.model.map.MapCatalogEntryDirectory;
import edu.mayo.cts2.framework.model.map.MapCatalogEntryMsg;
import edu.mayo.cts2.framework.model.map.MapCatalogEntrySummary;
import edu.mayo.cts2.framework.model.service.core.Query;
import edu.mayo.cts2.framework.model.service.exception.UnknownMap;
import edu.mayo.cts2.framework.service.command.Filter;
import edu.mayo.cts2.framework.service.command.Page;
import edu.mayo.cts2.framework.service.command.restriction.MapQueryServiceRestrictions;
import edu.mayo.cts2.framework.service.profile.map.MapMaintenanceService;
import edu.mayo.cts2.framework.service.profile.map.MapQueryService;
import edu.mayo.cts2.framework.service.profile.map.MapReadService;

/**
 * The Class MapController.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Controller
public class MapController extends AbstractServiceAwareController {
	
	@Cts2Service
	private MapReadService mapReadService;
	
	@Cts2Service
	private MapQueryService mapQueryService;
	
	@Cts2Service
	private MapMaintenanceService mapMaintenanceService;
	
	/**
	 * Gets the maps.
	 *
	 * @param httpServletRequest the http servlet request
	 * @param restrictions the restrictions
	 * @param filter the filter
	 * @param page the page
	 * @return the maps
	 */
	@RequestMapping(value=PATH_MAPS, method=RequestMethod.GET)
	@ResponseBody
	public MapCatalogEntryDirectory getMaps(
			HttpServletRequest httpServletRequest,
			MapQueryServiceRestrictions restrictions,
			Filter filter,
			Page page) {
		
		return this.getMaps(httpServletRequest, null, restrictions, filter, page);
	}
	
	/**
	 * Gets the maps.
	 *
	 * @param httpServletRequest the http servlet request
	 * @param query the query
	 * @param restrictions the restrictions
	 * @param filter the filter
	 * @param page the page
	 * @return the maps
	 */
	@RequestMapping(value=PATH_MAPS, method=RequestMethod.POST)
	@ResponseBody
	public MapCatalogEntryDirectory getMaps(
			HttpServletRequest httpServletRequest,	
			@RequestBody Query query,
			MapQueryServiceRestrictions restrictions,
			Filter filter,
			Page page) {
		
		FilterComponent filterComponent = this.processFilter(filter, mapQueryService);

		DirectoryResult<MapCatalogEntrySummary> directoryResult = this.mapQueryService.getResourceSummaries(
				null,
				filterComponent,
				restrictions, 
				page);

		MapCatalogEntryDirectory directory = this.populateDirectory(
				directoryResult, 
				page, 
				httpServletRequest, 
				MapCatalogEntryDirectory.class);

		return directory;
	}

	/**
	 * Does map exist.
	 *
	 * @param httpServletResponse the http servlet response
	 * @param mapName the map name
	 */
	@RequestMapping(value=PATH_MAP_BYID, method=RequestMethod.HEAD)
	@ResponseBody
	public void doesMapExist(
			HttpServletResponse httpServletResponse,
			@PathVariable(VAR_MAPID) String mapName) {
		
		boolean exists = this.mapReadService.exists(mapName);
		
		this.handleExists(mapName, UnknownMap.class, httpServletResponse, exists);
	}
	
	/**
	 * Gets the maps count.
	 *
	 * @param httpServletResponse the http servlet response
	 * @param query the query
	 * @param restrictions the restrictions
	 * @param filter the filter
	 * @return the maps count
	 */
	@RequestMapping(value=PATH_MAPS, method=RequestMethod.HEAD)
	@ResponseBody
	public void getMapsCount(
			HttpServletResponse httpServletResponse,
			@RequestBody Query query,
			MapQueryServiceRestrictions restrictions,
			Filter filter) {
		FilterComponent filterComponent = this.processFilter(filter, mapQueryService);
		
		int count = this.mapQueryService.count(
				null,
				filterComponent, 
				restrictions);
		
		this.setCount(count, httpServletResponse);
	}

	/**
	 * Gets the map by name.
	 *
	 * @param httpServletRequest the http servlet request
	 * @param mapName the map name
	 * @return the map by name
	 */
	@RequestMapping(value=PATH_MAP_BYID, method=RequestMethod.GET)
	@ResponseBody
	public MapCatalogEntryMsg getMapByName(
			HttpServletRequest httpServletRequest,
			@PathVariable(VAR_MAPID) String mapName) {
			
		MapCatalogEntry map = this.mapReadService.read(mapName);
		
		MapCatalogEntryMsg msg = new MapCatalogEntryMsg();
		msg.setMap(map);
		
		msg = this.wrapMessage(msg, httpServletRequest);
		
		return msg;
	}
	
	/**
	 * Creates the map.
	 *
	 * @param httpServletRequest the http servlet request
	 * @param map the map
	 * @param changeseturi the changeseturi
	 * @param MapName the map name
	 */
	@RequestMapping(value=PATH_MAP_BYID, method=RequestMethod.PUT)
	@ResponseBody
	public void createMap(
			HttpServletRequest httpServletRequest,
			@RequestBody MapCatalogEntry map,
			@RequestParam(required=false) String changeseturi,
			@PathVariable(VAR_MAPID) String MapName) {
			
		this.mapMaintenanceService.createResource(changeseturi, map);
	}
	
	/**
	 * Gets the map by uri.
	 *
	 * @param httpServletRequest the http servlet request
	 * @param uri the uri
	 * @return the map by uri
	 */
	@RequestMapping(value=PATH_MAP_BYURI, method=RequestMethod.GET)
	@ResponseBody
	public MapCatalogEntryMsg getMapByUri(
			HttpServletRequest httpServletRequest,
			@PathVariable(VAR_URI) String uri) {
		/*
		uri = this.decodeUri(uri);
		
		String name = this.mapService.getMapNameFromUri(uri);
			
		String path = "../../" + MAP + "/" + name;

		return this.redirect(path, httpServletRequest, VAR_URI);
		*/ 
		return null;
	}
}