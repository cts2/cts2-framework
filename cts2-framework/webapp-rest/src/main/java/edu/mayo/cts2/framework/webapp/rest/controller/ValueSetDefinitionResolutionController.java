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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.mayo.cts2.framework.model.command.Page;
import edu.mayo.cts2.framework.model.command.ResolvedFilter;
import edu.mayo.cts2.framework.model.command.ResolvedReadContext;
import edu.mayo.cts2.framework.model.core.Message;
import edu.mayo.cts2.framework.model.directory.DirectoryResult;
import edu.mayo.cts2.framework.model.entity.EntityDirectory;
import edu.mayo.cts2.framework.model.entity.EntityDirectoryEntry;
import edu.mayo.cts2.framework.model.extension.LocalIdValueSetResolution;
import edu.mayo.cts2.framework.model.service.core.NameOrURI;
import edu.mayo.cts2.framework.model.service.core.Query;
import edu.mayo.cts2.framework.model.service.exception.UnknownResourceReference;
import edu.mayo.cts2.framework.model.util.ModelUtils;
import edu.mayo.cts2.framework.model.valuesetdefinition.ResolvedValueSet;
import edu.mayo.cts2.framework.model.valuesetdefinition.ResolvedValueSetDirectory;
import edu.mayo.cts2.framework.model.valuesetdefinition.ResolvedValueSetDirectoryEntry;
import edu.mayo.cts2.framework.model.valuesetdefinition.ResolvedValueSetMsg;
import edu.mayo.cts2.framework.service.command.restriction.EntityDescriptionQueryServiceRestrictions;
import edu.mayo.cts2.framework.service.profile.valuesetdefinition.ValueSetDefinitionResolutionService;
import edu.mayo.cts2.framework.service.profile.valuesetdefinition.name.ValueSetDefinitionReadId;
import edu.mayo.cts2.framework.service.profile.valuesetresolution.ResolvedValueSetReference;
import edu.mayo.cts2.framework.service.profile.valuesetresolution.ValueSetResolutionLoaderService;
import edu.mayo.cts2.framework.service.profile.valuesetresolution.ValueSetResolutionQueryService;
import edu.mayo.cts2.framework.service.profile.valuesetresolution.ValueSetResolutionReadService;
import edu.mayo.cts2.framework.service.profile.valuesetresolution.name.ValueSetResolutionReadId;
import edu.mayo.cts2.framework.webapp.rest.command.RestFilter;
import edu.mayo.cts2.framework.webapp.rest.command.RestReadContext;

/**
 * The Class ValueSetDefinitionController.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Controller
public class ValueSetDefinitionResolutionController extends AbstractServiceAwareController {
	
	@Resource
	private UrlTemplateBindingCreator urlTemplateBindingCreator;

	@Cts2Service
	private ValueSetResolutionLoaderService valueSetResolutionLoaderService;
	
	@Cts2Service
	private ValueSetResolutionQueryService valueSetResolutionQueryService;
	
	@Cts2Service
	private ValueSetResolutionReadService valueSetResolutionReadService;
	
	@Cts2Service
	private ValueSetDefinitionResolutionService valueSetDefinitionResolutionService;
	
	final static MessageFactory<LocalIdValueSetResolution> MESSAGE_FACTORY = 
			new MessageFactory<LocalIdValueSetResolution>() {

		@Override
		public Message createMessage(LocalIdValueSetResolution resource) {
			ResolvedValueSetMsg msg = new ResolvedValueSetMsg();
			msg.setResolvedValueSet(resource.getResource());

			return msg;
		}
	};
	
	private final static UrlTemplateBinder<ResolvedValueSetReference> URL_BINDER =
			new UrlTemplateBinder<ResolvedValueSetReference>(){

		@Override
		public Map<String,String> getPathValues(ResolvedValueSetReference resource) {
			Map<String,String> returnMap = new HashMap<String,String>();
			
			returnMap.put(VAR_VALUESETID, 
					resource.getValueSetDefinitionReference().getValueSet().getContent());
			returnMap.put(VAR_VALUESETDEFINITIONID, 
					resource.getValueSetDefinitionReference().getValueSetDefinition().getContent());
			returnMap.put(VAR_RESOLVEDVALUESETID, 
					resource.getLocalID());

			return returnMap;
		}

	};

	@RequestMapping(value={	
			PATH_RESOLUTION_OF_VALUESETDEFINITION
			},
		method=RequestMethod.GET)
	@ResponseBody
	public Object getValueSetDefinitionResolution(
			HttpServletRequest httpServletRequest,
			RestReadContext restReadContext,
			EntityDescriptionQueryServiceRestrictions restrictions,
			RestFilter restFilter,
			@PathVariable(VAR_VALUESETID) String valueSetName,
			@PathVariable(VAR_VALUESETDEFINITIONID) String definitionLocalId,
			@RequestParam(
					value=RESOLUTION_TYPE, defaultValue=DEFAULT_VALUESETDEFINITION_RESOLUTION) 
			ValueSetDefinitionResolutionTypes resolution,
			@RequestParam(value=PARAM_CODESYSTEMVERSION, required=false) List<String> codeSystemVersionIds,
			@RequestParam(value=PARAM_CODESYSTEMTAG, required=false) String tagName,
			Page page) {
		
		return this.getValueSetDefinitionResolution(
				httpServletRequest,
				restReadContext,
				null,
				restrictions, 
				restFilter,
				valueSetName, 
				definitionLocalId,
				resolution, 
				codeSystemVersionIds, 
				tagName, 
				page);
	}
	
	@RequestMapping(value={	
			PATH_RESOLUTION_OF_VALUESETDEFINITION
			},
		method=RequestMethod.POST)
	@ResponseBody
	public Object getValueSetDefinitionResolution(
			HttpServletRequest httpServletRequest,
			RestReadContext restReadContext,
			@RequestBody Query query,
			EntityDescriptionQueryServiceRestrictions restrictions,
			RestFilter restFilter,
			@PathVariable(VAR_VALUESETID) String valueSetName,
			@PathVariable(VAR_VALUESETDEFINITIONID) String definitionLocalId,
			@RequestParam(
					value=RESOLUTION_TYPE) 
			ValueSetDefinitionResolutionTypes resolution,
			@RequestParam(value=PARAM_CODESYSTEMVERSION, required=false) List<String> codeSystemVersionIds,
			@RequestParam(value=PARAM_CODESYSTEMTAG, required=false) String tagName,
			Page page) {
		
		ValueSetDefinitionReadId definitionId = 
				new ValueSetDefinitionReadId(
						definitionLocalId,
						ModelUtils.nameOrUriFromName(valueSetName));
		
		ResolvedFilter filterComponent = this.processFilter(restFilter, this.valueSetResolutionQueryService);
		ResolvedReadContext readContext = this.resolveRestReadContext(restReadContext);
		
		Set<NameOrURI> codeSystemVersions = 
				ControllerUtils.idsToNameOrUriSet(codeSystemVersionIds);
		
		NameOrURI tag = ModelUtils.nameOrUriFromEither(tagName);
		
		switch (resolution) {
			case directory : {
				DirectoryResult<ResolvedValueSetDirectoryEntry> directory = this.valueSetDefinitionResolutionService.
						resolveDefinitionAsDirectory(
								definitionId, 
								codeSystemVersions, 
								tag, 
								query, 
								createSet(filterComponent), 
								readContext, 
								page);
				
				return this.populateDirectory(
						directory, 
						page, 
						httpServletRequest, 
						ResolvedValueSetDirectory.class);
			}
			case entitydirectory : {
				DirectoryResult<EntityDirectoryEntry> entityDirectory = this.valueSetDefinitionResolutionService.
						resolveDefinitionAsEntityDirectory(
								definitionId, 
								codeSystemVersions, 
								tag,
								query, 
								createSet(filterComponent), 
								restrictions,
								readContext, 
								page);
				
				return this.populateDirectory(
						entityDirectory, 
						page, 
						httpServletRequest, 
						EntityDirectory.class);
			}
			case complete : {
				
				ResolvedValueSet completeSet = this.valueSetDefinitionResolutionService.
					resolveDefinitionAsCompleteSet(
							definitionId, 
							codeSystemVersions, 
							tag, 
							readContext);
				
				ResolvedValueSetMsg msg = new ResolvedValueSetMsg();
				msg.setResolvedValueSet(completeSet);
				
				return this.wrapMessage(msg, httpServletRequest);
			}
			default : {
				throw new IllegalStateException();
			}
		}
		
	}
	
	@RequestMapping(value={	
			PATH_RESOLVED_VALUESET_OF_VALUESETDEFINITION_BYID
			},
		method=RequestMethod.GET)
	@ResponseBody
	public Message getValueSetResolutionByLocalId(
			HttpServletRequest httpServletRequest,
			RestReadContext restReadContext,
			@PathVariable(VAR_VALUESETID) String valueSetName,
			@PathVariable(VAR_VALUESETDEFINITIONID) String definitionLocalId,
			@PathVariable(VAR_RESOLVEDVALUESETID) String resolvedValueSetLocalId) {
		
		ValueSetResolutionReadId id = 
				new ValueSetResolutionReadId(
						definitionLocalId,
						ModelUtils.nameOrUriFromName(valueSetName));
		
		return this.doRead(
				httpServletRequest, 
				MESSAGE_FACTORY, 
				this.valueSetResolutionReadService, 
				restReadContext, 
				UnknownResourceReference.class, 
				id);
	}
	
	@RequestMapping(value={	
			PATH_RESOLVED_VALUESET_OF_VALUESETDEFINITION_BYID
			},
		method=RequestMethod.DELETE)
	@ResponseBody
	public void deleteValueSetResolutionByLocalId(
			HttpServletRequest httpServletRequest,
			RestReadContext restReadContext,
			@PathVariable(VAR_VALUESETID) String valueSetName,
			@PathVariable(VAR_VALUESETDEFINITIONID) String definitionLocalId,
			@PathVariable(VAR_RESOLVEDVALUESETID) String resolvedValueSetLocalId) {
		
		ValueSetResolutionReadId id = 
				new ValueSetResolutionReadId(
						definitionLocalId,
						ModelUtils.nameOrUriFromName(valueSetName));
		
		this.valueSetResolutionLoaderService.delete(id);
	}
	
	@RequestMapping(value=PATH_RESOLVED_VALUESET, method=RequestMethod.POST)
	public ResponseEntity<Void> loadValueSetResolution(
			HttpServletRequest httpServletRequest,
			@RequestBody ResolvedValueSet resolvedValueSet) {
		
		ResolvedValueSetReference id = this.valueSetResolutionLoaderService.load(resolvedValueSet);


		String localtion = this.urlTemplateBindingCreator.bindResourceToUrlTemplate(
				URL_BINDER, 
				id, 
				PATH_RESOLVED_VALUESET_OF_VALUESETDEFINITION_BYID);
		
		return this.getCreateHandler().createResponseEntity(localtion);
	}

	public ValueSetDefinitionResolutionService getValueSetDefinitionResolutionService() {
		return valueSetDefinitionResolutionService;
	}

	public void setValueSetDefinitionResolutionService(
			ValueSetDefinitionResolutionService valueSetDefinitionResolutionService) {
		this.valueSetDefinitionResolutionService = valueSetDefinitionResolutionService;
	}
}