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

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import edu.mayo.cts2.framework.model.command.Page;
import edu.mayo.cts2.framework.model.core.Directory;
import edu.mayo.cts2.framework.model.core.Message;
import edu.mayo.cts2.framework.model.directory.DirectoryResult;
import edu.mayo.cts2.framework.model.map.MapCatalogEntry;
import edu.mayo.cts2.framework.model.map.MapCatalogEntryDirectory;
import edu.mayo.cts2.framework.model.map.MapCatalogEntryList;
import edu.mayo.cts2.framework.model.map.MapCatalogEntryMsg;
import edu.mayo.cts2.framework.model.service.core.Query;
import edu.mayo.cts2.framework.model.service.core.types.RestrictionType;
import edu.mayo.cts2.framework.model.service.exception.UnknownMap;
import edu.mayo.cts2.framework.model.service.mapversion.types.MapRole;
import edu.mayo.cts2.framework.model.util.ModelUtils;
import edu.mayo.cts2.framework.service.command.restriction.MapQueryServiceRestrictions;
import edu.mayo.cts2.framework.service.command.restriction.MapQueryServiceRestrictions.CodeSystemRestriction;
import edu.mayo.cts2.framework.service.command.restriction.MapQueryServiceRestrictions.ValueSetRestriction;
import edu.mayo.cts2.framework.service.profile.map.MapHistoryService;
import edu.mayo.cts2.framework.service.profile.map.MapMaintenanceService;
import edu.mayo.cts2.framework.service.profile.map.MapQueryService;
import edu.mayo.cts2.framework.service.profile.map.MapReadService;
import edu.mayo.cts2.framework.service.profile.valueset.MapQuery;
import edu.mayo.cts2.framework.webapp.rest.command.QueryControl;
import edu.mayo.cts2.framework.webapp.rest.command.RestFilter;
import edu.mayo.cts2.framework.webapp.rest.command.RestReadContext;
import edu.mayo.cts2.framework.webapp.rest.query.MapQueryBuilder;

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
	
	@Cts2Service
	private MapHistoryService mapHistoryService;
	
	private static UrlTemplateBinder<MapCatalogEntry> URL_BINDER = new 
			UrlTemplateBinder<MapCatalogEntry>(){

		@Override
		public Map<String,String> getPathValues(MapCatalogEntry resource) {
			Map<String,String> returnMap = new HashMap<String,String>();
			returnMap.put(VAR_MAPID ,resource.getMapName());
			
			return returnMap;
		}

	};
	
	private final static MessageFactory<MapCatalogEntry> MESSAGE_FACTORY = 
			new MessageFactory<MapCatalogEntry>() {

		@Override
		public Message createMessage(MapCatalogEntry resource) {
			MapCatalogEntryMsg msg = new MapCatalogEntryMsg();
			msg.setMap(resource);

			return msg;
		}
	};
	
	@RequestMapping(value=PATH_MAP_CHANGEHISTORY, method=RequestMethod.GET)
	@ResponseBody
	public MapCatalogEntryList getChangeHistory(
			HttpServletRequest httpServletRequest,
			@PathVariable(VAR_MAPID) String mapName,
			@RequestParam(required=false) Date PARAM_FROMDATE,
			@RequestParam(required=false) Date PARAM_TODATE,
			Page page) {
	
		DirectoryResult<MapCatalogEntry> result = 
				this.mapHistoryService.getChangeHistory(
						ModelUtils.nameOrUriFromName(mapName),
						PARAM_FROMDATE,
						PARAM_TODATE);
		
		return this.populateDirectory(
				result, 
				page, 
				httpServletRequest, 
				MapCatalogEntryList.class);	
	}
	
	@RequestMapping(value=PATH_MAP_EARLIESTCHANGE, method=RequestMethod.GET)
	@ResponseBody
	public MapCatalogEntryMsg getEarliesChange(
			HttpServletRequest httpServletRequest,
			@PathVariable(VAR_MAPID) String mapName) {
	
		MapCatalogEntry result = 
				this.mapHistoryService.getEarliestChangeFor(ModelUtils.nameOrUriFromName(mapName));
		
		MapCatalogEntryMsg msg = new MapCatalogEntryMsg();
		msg.setMap(result);
		
		return this.wrapMessage(msg, httpServletRequest);
	}
	
	@RequestMapping(value=PATH_MAP_LATESTCHANGE, method=RequestMethod.GET)
	@ResponseBody
	public MapCatalogEntryMsg getLastChange(
			HttpServletRequest httpServletRequest,
			@PathVariable(VAR_MAPID) String mapName) {
	
		MapCatalogEntry result = 
				this.mapHistoryService.getLastChangeFor(ModelUtils.nameOrUriFromName(mapName));
	
		MapCatalogEntryMsg msg = new MapCatalogEntryMsg();
		msg.setMap(result);
		
		return this.wrapMessage(msg, httpServletRequest);
	}
	
	/**
	 * Gets the maps.
	 *
	 * @param httpServletRequest the http servlet request
	 * @param restrictions the restrictions
	 * @param resolvedFilter the filter
	 * @param page the page
	 * @return the maps
	 */
	@RequestMapping(value=PATH_MAPS, method=RequestMethod.GET)
	@ResponseBody
	public Directory getMaps(
			HttpServletRequest httpServletRequest,
			RestReadContext restReadContext,
			MapQueryServiceRestrictions restrictions,
			RestFilter restFilter,
			Page page,
			boolean list) {
		
		return this.getMaps(
				httpServletRequest, 
				restReadContext, 
				null,
				restrictions, 
				restFilter, 
				page,
				list);
	}
	
	/**
	 * Gets the maps.
	 *
	 * @param httpServletRequest the http servlet request
	 * @param query the query
	 * @param restrictions the restrictions
	 * @param resolvedFilter the filter
	 * @param page the page
	 * @return the maps
	 */
	@RequestMapping(value=PATH_MAPS, method=RequestMethod.POST)
	@ResponseBody
	public Directory getMaps(
			HttpServletRequest httpServletRequest,
			RestReadContext restReadContext,
			@RequestBody Query query,
			MapQueryServiceRestrictions restrictions,
			RestFilter restFilter,
			Page page,
			boolean list) {
		
		MapQueryBuilder builder = this.getNewResourceQueryBuilder();
		
		MapQuery resourceQuery = builder.
				addQuery(query).
				addRestFilter(restFilter).
				addRestrictions(restrictions).
				addRestReadContext(restReadContext).
				build();
		
		return this.doQuery(
				httpServletRequest,
				list, 
				this.mapQueryService,
				resourceQuery,
				page, 
				null,//TODO: Sort not yet supported 
				MapCatalogEntryDirectory.class, 
				MapCatalogEntryList.class);
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
		
		this.doExists(
				httpServletResponse, 
				this.mapReadService, 
				UnknownMap.class,
				ModelUtils.nameOrUriFromName(mapName));
	}
	
	/**
	 * Gets the maps count.
	 *
	 * @param httpServletResponse the http servlet response
	 * @param query the query
	 * @param restrictions the restrictions
	 * @param resolvedFilter the filter
	 * @return the maps count
	 */
	@RequestMapping(value=PATH_MAPS, method=RequestMethod.HEAD)
	@ResponseBody
	public void getMapsCount(
			HttpServletResponse httpServletResponse,
			RestReadContext restReadContext,
			MapQueryServiceRestrictions restrictions,
			RestFilter restFilter) {
		
		MapQueryBuilder builder = this.getNewResourceQueryBuilder();
		
		MapQuery resourceQuery = builder.
				addRestFilter(restFilter).
				addRestrictions(restrictions).
				addRestReadContext(restReadContext).
				build();
		
		int count = this.mapQueryService.count(resourceQuery);
		
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
	public Message getMapByName(
			HttpServletRequest httpServletRequest,
			RestReadContext restReadContext,
			QueryControl queryControl,
			@PathVariable(VAR_MAPID) String mapName) {
			
		return this.doRead(
				httpServletRequest, 
				MESSAGE_FACTORY, 
				this.mapReadService, 
				restReadContext,
				UnknownMap.class,
				ModelUtils.nameOrUriFromName(mapName));
	}
	
	/**
	 * Creates the map.
	 *
	 * @param httpServletRequest the http servlet request
	 * @param map the map
	 * @param changeseturi the changeseturi
	 * @param MapName the map name
	 * @return 
	 */
	@RequestMapping(value=PATH_MAP, method=RequestMethod.POST)
	public ResponseEntity<Void> createMap(
			HttpServletRequest httpServletRequest,
			@RequestBody MapCatalogEntry map,
			@RequestParam(value=PARAM_CHANGESETCONTEXT, required=false) String changeseturi) {

		return this.getCreateHandler().create(
				map,
				changeseturi,
				PATH_MAP_BYID,
				URL_BINDER, 
				this.mapMaintenanceService);
	}
	
	@RequestMapping(value=PATH_MAP_BYID, method=RequestMethod.PUT)
	@ResponseBody
	public void updateMap(
			HttpServletRequest httpServletRequest,
			@RequestBody MapCatalogEntry map,
			@RequestParam(value=PARAM_CHANGESETCONTEXT, required=false) String changeseturi,
			@PathVariable(VAR_MAPID) String mapName) {
		
		this.getUpdateHandler().update(
				map, 
				changeseturi, 
				ModelUtils.nameOrUriFromName(mapName),
				this.mapMaintenanceService);
	}
	
	@RequestMapping(value=PATH_MAP_BYID, method=RequestMethod.DELETE)
	@ResponseBody
	public void deleteMap(
			HttpServletRequest httpServletRequest,
			RestReadContext restReadContext,
			@PathVariable(VAR_MAPID) String mapName,
			@RequestParam(PARAM_CHANGESETCONTEXT) String changeseturi) {
			
		this.mapMaintenanceService.
		deleteResource(
				ModelUtils.nameOrUriFromName(
						mapName), 
						changeseturi);
	}
	
	/**
	 * Gets the map by uri.
	 *
	 * @param httpServletRequest the http servlet request
	 * @param uri the uri
	 * @return the map by uri
	 */
	@RequestMapping(value=PATH_MAP_BYURI, method=RequestMethod.GET)
	public ModelAndView getMapByUri(
			HttpServletRequest httpServletRequest,
			RestReadContext restReadContext,
			QueryControl queryControl,
			@RequestParam(PARAM_URI) String uri,
			@RequestParam(value="redirect", defaultValue="false") boolean redirect) {
		
		return this.doReadByUri(
				httpServletRequest, 
				MESSAGE_FACTORY, 
				PATH_MAP_BYURI, 
				PATH_MAP_BYID, 
				URL_BINDER, 
				this.mapReadService,
				restReadContext,
				UnknownMap.class,
				ModelUtils.nameOrUriFromUri(uri),
				redirect);
	}
	
	@InitBinder
	public void initMapRestrictionBinder(
			 WebDataBinder binder,
			 @RequestParam(value=PARAM_ENTITY, required=false) List<String> entity,
			 @RequestParam(value=PARAM_VALUESET, required=false) List<String> valueset,
			 @RequestParam(value=PARAM_VALUESET, required=false) List<String> codesystem,
			 @RequestParam(value=PARAM_ALL_OR_SOME, required=false) RestrictionType allorsome,
			 @RequestParam(value=PARAM_CODESYSTEMS_MAPROLE, required=false) MapRole codesystemsmaprole,
			 @RequestParam(value=PARAM_VALUESETS_MAPROLE, required=false) MapRole valuesetsmaprole) {
		
		if(binder.getTarget() instanceof MapQueryServiceRestrictions){
			MapQueryServiceRestrictions restrictions = 
					(MapQueryServiceRestrictions) binder.getTarget();

			if(CollectionUtils.isNotEmpty(valueset)){
				restrictions.setValueSetRestriction(new ValueSetRestriction());
				restrictions.getValueSetRestriction().setMapRole(valuesetsmaprole);
				restrictions.getValueSetRestriction().setValueSets(
						ControllerUtils.idsToNameOrUriSet(valueset));
			}
			
			if(CollectionUtils.isNotEmpty(codesystem)){
				restrictions.setCodeSystemRestriction(new CodeSystemRestriction());
				restrictions.getCodeSystemRestriction().setMapRole(codesystemsmaprole);
				restrictions.getCodeSystemRestriction().setCodeSystems(
						ControllerUtils.idsToNameOrUriSet(codesystem));
			}
		}
	}
	
	private MapQueryBuilder getNewResourceQueryBuilder(){
		return new MapQueryBuilder(
			this.mapQueryService, 
			this.getFilterResolver(),
			this.getReadContextResolver());
	}

	public MapReadService getMapReadService() {
		return mapReadService;
	}

	public void setMapReadService(MapReadService mapReadService) {
		this.mapReadService = mapReadService;
	}

	public MapQueryService getMapQueryService() {
		return mapQueryService;
	}

	public void setMapQueryService(MapQueryService mapQueryService) {
		this.mapQueryService = mapQueryService;
	}

	public MapMaintenanceService getMapMaintenanceService() {
		return mapMaintenanceService;
	}

	public void setMapMaintenanceService(MapMaintenanceService mapMaintenanceService) {
		this.mapMaintenanceService = mapMaintenanceService;
	}

	public MapHistoryService getMapHistoryService() {
		return mapHistoryService;
	}

	public void setMapHistoryService(MapHistoryService mapHistoryService) {
		this.mapHistoryService = mapHistoryService;
	}
}