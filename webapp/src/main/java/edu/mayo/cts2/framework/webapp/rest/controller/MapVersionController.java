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
import edu.mayo.cts2.framework.model.core.Message;
import edu.mayo.cts2.framework.model.directory.DirectoryResult;
import edu.mayo.cts2.framework.model.mapversion.MapVersion;
import edu.mayo.cts2.framework.model.mapversion.MapVersionDirectory;
import edu.mayo.cts2.framework.model.mapversion.MapVersionList;
import edu.mayo.cts2.framework.model.mapversion.MapVersionMsg;
import edu.mayo.cts2.framework.model.service.core.Query;
import edu.mayo.cts2.framework.model.service.core.types.RestrictionType;
import edu.mayo.cts2.framework.model.service.exception.UnknownMapVersion;
import edu.mayo.cts2.framework.model.service.mapversion.types.MapRole;
import edu.mayo.cts2.framework.model.service.mapversion.types.MapStatus;
import edu.mayo.cts2.framework.model.util.ModelUtils;
import edu.mayo.cts2.framework.service.command.restriction.MapQueryServiceRestrictions.CodeSystemRestriction;
import edu.mayo.cts2.framework.service.command.restriction.MapQueryServiceRestrictions.ValueSetRestriction;
import edu.mayo.cts2.framework.service.command.restriction.MapVersionQueryServiceRestrictions;
import edu.mayo.cts2.framework.service.command.restriction.MapVersionQueryServiceRestrictions.EntitiesRestriction;
import edu.mayo.cts2.framework.service.profile.mapversion.MapVersionHistoryService;
import edu.mayo.cts2.framework.service.profile.mapversion.MapVersionMaintenanceService;
import edu.mayo.cts2.framework.service.profile.mapversion.MapVersionQuery;
import edu.mayo.cts2.framework.service.profile.mapversion.MapVersionQueryService;
import edu.mayo.cts2.framework.service.profile.mapversion.MapVersionReadService;
import edu.mayo.cts2.framework.webapp.rest.command.QueryControl;
import edu.mayo.cts2.framework.webapp.rest.command.RestFilter;
import edu.mayo.cts2.framework.webapp.rest.command.RestReadContext;
import edu.mayo.cts2.framework.webapp.rest.query.MapVersionQueryBuilder;
import edu.mayo.cts2.framework.webapp.rest.util.ControllerUtils;

/**
 * The Class MapVersionController.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Controller
public class MapVersionController extends AbstractMessageWrappingController {

	@Cts2Service
	private MapVersionReadService mapVersionReadService;
	
	@Cts2Service
	private MapVersionQueryService mapVersionQueryService;
	
	@Cts2Service
	private MapVersionMaintenanceService mapVersionMaintenanceService;
	
	@Cts2Service
	private MapVersionHistoryService mapVersionHistoryService;
	
	private static UrlTemplateBinder<MapVersion> URL_BINDER = new 
			UrlTemplateBinder<MapVersion>(){

		@Override
		public Map<String,String> getPathValues(MapVersion resource) {
			Map<String,String> returnMap = new HashMap<String,String>();
			returnMap.put(VAR_MAPVERSIONID,resource.getMapVersionName());
			returnMap.put(VAR_MAPID,resource.getVersionOf().getContent());
			
			return returnMap;
		}

	};
	
	private final static MessageFactory<MapVersion> MESSAGE_FACTORY = 
			new MessageFactory<MapVersion>() {

		@Override
		public Message createMessage(MapVersion resource) {
			MapVersionMsg msg = new MapVersionMsg();
			msg.setMapVersion(resource);

			return msg;
		}
	};
	
	@RequestMapping(value=PATH_MAPVERSION_CHANGEHISTORY, method=RequestMethod.GET)
	@ResponseBody
	public MapVersionList getChangeHistory(
			HttpServletRequest httpServletRequest,
			@PathVariable(VAR_MAPID) String mapName,
			@PathVariable(VAR_MAPVERSIONID) String mapVersionName,
			@RequestParam(required=false) Date PARAM_FROMDATE,
			@RequestParam(required=false) Date PARAM_TODATE,
			Page page) {
	
		DirectoryResult<MapVersion> result = 
				this.mapVersionHistoryService.getChangeHistory(
						ModelUtils.nameOrUriFromName(mapVersionName),
						PARAM_FROMDATE,
						PARAM_TODATE);
		
		return this.populateDirectory(
				result, 
				page, 
				httpServletRequest, 
				MapVersionList.class);	
	}
	
	@RequestMapping(value=PATH_MAPVERSION_EARLIESTCHANGE, method=RequestMethod.GET)
	@ResponseBody
	public MapVersionMsg getEarliesChange(
			HttpServletRequest httpServletRequest,
			@PathVariable(VAR_MAPID) String mapName,
			@PathVariable(VAR_MAPVERSIONID) String mapVersionName) {
	
		MapVersion result = 
				this.mapVersionHistoryService.getEarliestChangeFor(
						ModelUtils.nameOrUriFromName(mapVersionName));
		
		MapVersionMsg msg = new MapVersionMsg();
		msg.setMapVersion(result);
		
		return this.wrapMessage(msg, httpServletRequest);
	}
	
	@RequestMapping(value=PATH_MAPVERSION_LATESTCHANGE, method=RequestMethod.GET)
	@ResponseBody
	public MapVersionMsg getLastChange(
			HttpServletRequest httpServletRequest,
			@PathVariable(VAR_MAPID) String mapName,
			@PathVariable(VAR_MAPVERSIONID) String mapVersionName) {
	
		MapVersion result = 
				this.mapVersionHistoryService.getLastChangeFor(
						ModelUtils.nameOrUriFromName(mapVersionName));
	
		MapVersionMsg msg = new MapVersionMsg();
		msg.setMapVersion(result);
		
		return this.wrapMessage(msg, httpServletRequest);
	}
	
	/**
	 * Creates the map version.
	 *
	 * @param httpServletRequest the http servlet request
	 * @param changeseturi the changeseturi
	 * @param mapVersion the map version
	 * @param mapName the map name
	 * @param mapVersionName the map version name
	 */
	@RequestMapping(value=PATH_MAPVERSION_OF_MAP_BYID, method=RequestMethod.PUT)
	public Object updateMapVersion(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse,
			@RequestParam(value=PARAM_CHANGESETCONTEXT, required=false) String changeseturi,
			@RequestBody MapVersion mapVersion,
			@PathVariable(VAR_MAPID) String mapName,
			@PathVariable(VAR_MAPVERSIONID) String mapVersionName) {
	
		return this.doUpdate(
				httpServletResponse,
				mapVersion, 
				changeseturi,
				ModelUtils.nameOrUriFromName(mapVersionName), 
				this.mapVersionMaintenanceService);
	}
	
	@RequestMapping(value=PATH_MAPVERSION, method=RequestMethod.POST)
	public Object createMapVersion(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse,
			@RequestParam(value=PARAM_CHANGESETCONTEXT, required=false) String changeseturi,
			@RequestBody MapVersion mapVersion) {

		return this.doCreate(
				httpServletResponse,
				mapVersion,
				changeseturi,
				PATH_MAPVERSION_OF_MAP_BYID,
				URL_BINDER, 
				this.mapVersionMaintenanceService);
	}

	@RequestMapping(value=PATH_MAPVERSION_OF_MAP_BYID, method=RequestMethod.DELETE)
	public Object deleteMapVersion(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse,
			@PathVariable(VAR_MAPID) String mapName,
			@PathVariable(VAR_MAPVERSIONID) String mapVersionName,	
			@RequestParam(PARAM_CHANGESETCONTEXT) String changeseturi) {

			return this.doDelete(httpServletResponse,
				ModelUtils.nameOrUriFromName(
						mapVersionName), 
						changeseturi, 
						this.mapVersionMaintenanceService);
	}
	/**
	 * Gets the map versions of map.
	 *
	 * @param httpServletRequest the http servlet request
	 * @param restrictions the restrictions
	 * @param resolvedFilter the filter
	 * @param page the page
	 * @param mapName the map name
	 * @return the map versions of map
	 */
	@RequestMapping(value={
			PATH_MAPVERSIONS_OF_MAP}, method=RequestMethod.GET)
	public Object getMapVersionsOfMap(
			HttpServletRequest httpServletRequest,
			RestReadContext restReadContext,
			QueryControl queryControl,
			MapVersionQueryServiceRestrictions restrictions,
			RestFilter restFilter,
			Page page,
			boolean list,
			@PathVariable(VAR_MAPID) String mapName) {
		
		return this.getMapVersionsOfMap(
				httpServletRequest, 
				restReadContext,
				queryControl,
				null, 
				restrictions,
				restFilter,
				page,
				list,
				mapName);
	}
	
	/**
	 * Gets the map versions of map.
	 *
	 * @param httpServletRequest the http servlet request
	 * @param query the query
	 * @param restrictions the restrictions
	 * @param resolvedFilter the filter
	 * @param page the page
	 * @param mapName the map name
	 * @return the map versions of map
	 */
	@RequestMapping(value={
			PATH_MAPVERSIONS_OF_MAP}, method=RequestMethod.POST)
	public Object getMapVersionsOfMap(
			HttpServletRequest httpServletRequest,
			RestReadContext restReadContext,
			QueryControl queryControl,
			@RequestBody Query query,
			MapVersionQueryServiceRestrictions restrictions,
			RestFilter restFilter,
			Page page,
			boolean list,
			@PathVariable(VAR_MAPID) String mapName) {
		
		restrictions.setMap(ModelUtils.nameOrUriFromEither(mapName));
		
		return this.getMapVersions(
				httpServletRequest,
				restReadContext, 
				queryControl, 
				query,
				restrictions, 
				restFilter,
				page,
				list);
	}
	
	/**
	 * Gets the map versions.
	 *
	 * @param httpServletRequest the http servlet request
	 * @param restrictions the restrictions
	 * @param resolvedFilter the filter
	 * @param page the page
	 * @return the map versions
	 */
	@RequestMapping(value={
			PATH_MAPVERSIONS}, method=RequestMethod.GET)
	public Object getMapVersions(
			HttpServletRequest httpServletRequest,
			RestReadContext restReadContext,
			QueryControl queryControl,
			MapVersionQueryServiceRestrictions restrictions,
			RestFilter restFilter,
			Page page,
			boolean list) {
		
		return this.getMapVersions(
				httpServletRequest, 
				restReadContext, 
				queryControl,
				null,
				restrictions, 
				restFilter,
				page,
				list);
	}
	
	/**
	 * Gets the map versions.
	 *
	 * @param httpServletRequest the http servlet request
	 * @param query the query
	 * @param restrictions the restrictions
	 * @param resolvedFilter the filter
	 * @param page the page
	 * @return the map versions
	 */
	@RequestMapping(value={
			PATH_MAPVERSIONS}, method=RequestMethod.POST)
	public Object getMapVersions(
			HttpServletRequest httpServletRequest,
			RestReadContext restReadContext,
			QueryControl queryControl,
			@RequestBody Query query,
			MapVersionQueryServiceRestrictions restrictions,
			RestFilter restFilter,
			Page page,
			boolean list) {
		
		MapVersionQueryBuilder builder = this.getNewResourceQueryBuilder();
		
		MapVersionQuery resourceQuery = builder.
				addQuery(query).
				addRestFilter(restFilter).
				addRestrictions(restrictions).
				addRestReadContext(restReadContext).
				build();
		
		return this.doQuery(
				httpServletRequest,
				list, 
				this.mapVersionQueryService,
				resourceQuery,
				page, 
				queryControl,
				MapVersionDirectory.class, 
				MapVersionList.class);
	}
	
	/**
	 * Does map version exist.
	 *
	 * @param httpServletResponse the http servlet response
	 * @param mapName the map name
	 * @param mapVersionName the map version name
	 */
	@RequestMapping(value=PATH_MAPVERSION_OF_MAP_BYID, method=RequestMethod.HEAD)
	@ResponseBody
	public void doesMapVersionExist(
			HttpServletResponse httpServletResponse,
			@PathVariable(VAR_MAPID) String mapName,
			@PathVariable(VAR_MAPVERSIONID) String mapVersionName) {
		
		this.doExists(
				httpServletResponse, 
				this.mapVersionReadService, 
				UnknownMapVersion.class, 
				ModelUtils.nameOrUriFromName(mapVersionName));
	}
	
	/**
	 * Gets the map versions of map count.
	 *
	 * @param httpServletResponse the http servlet response
	 * @param query the query
	 * @param restrictions the restrictions
	 * @param resolvedFilter the filter
	 * @param mapId the map id
	 * @return the map versions of map count
	 */
	@RequestMapping(value={
			PATH_MAPVERSIONS_OF_MAP}, method=RequestMethod.HEAD)
	@ResponseBody
	public void getMapVersionsOfMapCount(
			HttpServletResponse httpServletResponse,
			RestReadContext restReadContext,
			MapVersionQueryServiceRestrictions restrictions,
			RestFilter restFilter,
			@PathVariable(VAR_MAPID) String mapId) {
		
		restrictions.setMap(ModelUtils.nameOrUriFromEither(mapId));
		
		this.getMapVersionsCount(
				httpServletResponse, 
				restReadContext, 
				restrictions, 
				restFilter);
	}
	
	
	/**
	 * Gets the map versions count.
	 *
	 * @param httpServletResponse the http servlet response
	 * @param query the query
	 * @param restrictions the restrictions
	 * @param resolvedFilter the filter
	 * @param page the page
	 * @return the map versions count
	 */
	@RequestMapping(value={
			PATH_MAPVERSIONS}, method=RequestMethod.HEAD)
	@ResponseBody
	public void getMapVersionsCount(
			HttpServletResponse httpServletResponse,
			RestReadContext restReadContext,
			MapVersionQueryServiceRestrictions restrictions,
			RestFilter restFilter) {
		
		MapVersionQueryBuilder builder = this.getNewResourceQueryBuilder();
		
		MapVersionQuery resourceQuery = builder.
				addRestFilter(restFilter).
				addRestrictions(restrictions).
				addRestReadContext(restReadContext).
				build();
		
		int count =
			this.mapVersionQueryService.count(resourceQuery);
		
		this.setCount(count, httpServletResponse);
	}
	
	/**
	 * Gets the map version by name.
	 *
	 * @param httpServletRequest the http servlet request
	 * @param mapName the map name
	 * @param mapVersionName the map version name
	 * @return the map version by name
	 */
	@RequestMapping(value={	
			PATH_MAPVERSION_OF_MAP_BYID
			},
		method=RequestMethod.GET)
	@ResponseBody
	public Object getMapVersionByName(
			HttpServletRequest httpServletRequest,
			RestReadContext restReadContext,
			@PathVariable(VAR_MAPID) String mapName,
			@PathVariable(VAR_MAPVERSIONID) String mapVersionName) {
		
		return this.doRead(
				httpServletRequest, 
				MESSAGE_FACTORY, 
				this.mapVersionReadService, 
				restReadContext,
				UnknownMapVersion.class,
				ModelUtils.nameOrUriFromName(mapVersionName));
	}
	
	@RequestMapping(value=PATH_MAPVERSION_BYURI, method=RequestMethod.GET)
	public ModelAndView getMapVersionByUri(
			HttpServletRequest httpServletRequest,
			RestReadContext restReadContext,
			QueryControl queryControl,
			@RequestParam(PARAM_URI) String uri,
			@RequestParam(value="redirect", defaultValue="false") boolean redirect) {
		
		return this.doReadByUri(
				httpServletRequest, 
				MESSAGE_FACTORY, 
				PATH_MAPVERSION_BYURI, 
				PATH_MAPVERSION_OF_MAP_BYID, 
				URL_BINDER, 
				this.mapVersionReadService,
				restReadContext,
				UnknownMapVersion.class,
				ModelUtils.nameOrUriFromUri(uri),
				redirect);
	}
	
	@InitBinder
	public void initMapVersionRestrictionBinder(
			 WebDataBinder binder,
			 @RequestParam(value=PARAM_ENTITY, required=false) List<String> entity,
			 @RequestParam(value=PARAM_VALUESET, required=false) List<String> valueset,
			 @RequestParam(value=PARAM_VALUESET, required=false) List<String> codesystem,
			 @RequestParam(value=PARAM_ENTITIES_MAPROLE, required=false) MapRole entitiesmaprole,
			 @RequestParam(value=PARAM_ENTITIES_MAPSTATUS, required=false) MapStatus mapstatus,
			 @RequestParam(value=PARAM_ALL_OR_SOME, required=false) RestrictionType allorsome,
			 @RequestParam(value=PARAM_CODESYSTEMS_MAPROLE, required=false) MapRole codesystemsmaprole,
			 @RequestParam(value=PARAM_VALUESETS_MAPROLE, required=false) MapRole valuesetsmaprole) {
		
		if(binder.getTarget() instanceof MapVersionQueryServiceRestrictions){
			MapVersionQueryServiceRestrictions restrictions = 
					(MapVersionQueryServiceRestrictions) binder.getTarget();

			if(CollectionUtils.isNotEmpty(entity)){
				restrictions.setEntitiesRestriction(new EntitiesRestriction());
				restrictions.getEntitiesRestriction().setEntities(
						ControllerUtils.idsToEntityNameOrUriSet(entity));
				
				restrictions.getEntitiesRestriction().setAllOrSome(allorsome);
				restrictions.getEntitiesRestriction().setMapRole(entitiesmaprole);
				restrictions.getEntitiesRestriction().setMapStatus(mapstatus);
			}		
			
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
	
	private MapVersionQueryBuilder getNewResourceQueryBuilder(){
		return new MapVersionQueryBuilder(
			this.mapVersionQueryService, 
			this.getFilterResolver(),
			this.getReadContextResolver());
	}

	public MapVersionReadService getMapVersionReadService() {
		return mapVersionReadService;
	}

	public void setMapVersionReadService(MapVersionReadService mapVersionReadService) {
		this.mapVersionReadService = mapVersionReadService;
	}

	public MapVersionQueryService getMapVersionQueryService() {
		return mapVersionQueryService;
	}

	public void setMapVersionQueryService(
			MapVersionQueryService mapVersionQueryService) {
		this.mapVersionQueryService = mapVersionQueryService;
	}

	public MapVersionMaintenanceService getMapVersionMaintenanceService() {
		return mapVersionMaintenanceService;
	}

	public void setMapVersionMaintenanceService(
			MapVersionMaintenanceService mapVersionMaintenanceService) {
		this.mapVersionMaintenanceService = mapVersionMaintenanceService;
	}

	public MapVersionHistoryService getMapVersionHistoryService() {
		return mapVersionHistoryService;
	}

	public void setMapVersionHistoryService(
			MapVersionHistoryService mapVersionHistoryService) {
		this.mapVersionHistoryService = mapVersionHistoryService;
	}
	
}