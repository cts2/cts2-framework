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

import java.net.URI;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
import edu.mayo.cts2.framework.model.core.OpaqueData;
import edu.mayo.cts2.framework.model.core.types.FinalizableState;
import edu.mayo.cts2.framework.model.directory.DirectoryResult;
import edu.mayo.cts2.framework.model.exception.Cts2RestException;
import edu.mayo.cts2.framework.model.service.core.NameOrURI;
import edu.mayo.cts2.framework.model.service.core.Query;
import edu.mayo.cts2.framework.model.service.core.UpdateChangeSetMetadataRequest;
import edu.mayo.cts2.framework.model.service.core.UpdatedChangeInstructions;
import edu.mayo.cts2.framework.model.service.core.UpdatedCreator;
import edu.mayo.cts2.framework.model.service.core.UpdatedOfficialEffectiveDate;
import edu.mayo.cts2.framework.model.service.core.UpdatedState;
import edu.mayo.cts2.framework.model.service.exception.ChangeSetIsNotOpen;
import edu.mayo.cts2.framework.model.updates.ChangeSet;
import edu.mayo.cts2.framework.model.updates.ChangeSetDirectory;
import edu.mayo.cts2.framework.model.updates.ChangeSetDirectoryEntry;
import edu.mayo.cts2.framework.service.command.restriction.ChangeSetQueryExtensionRestrictions;
import edu.mayo.cts2.framework.service.profile.update.ChangeSetQuery;
import edu.mayo.cts2.framework.service.profile.update.ChangeSetQueryExtension;
import edu.mayo.cts2.framework.service.profile.update.ChangeSetService;
import edu.mayo.cts2.framework.webapp.rest.command.QueryControl;
import edu.mayo.cts2.framework.webapp.rest.command.RestFilter;

/**
 * The Class CodeSystemController.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Controller
public class ChangeSetController extends AbstractServiceAwareController {
	
	@Cts2Service
	private ChangeSetService changeSetService;
	
	@Cts2Service
	private ChangeSetQueryExtension changeSetQueryExtension;
	
	@Resource
	private UrlTemplateBindingCreator urlTemplateBindingCreator;
	
	@RequestMapping(value="/changesets", method=RequestMethod.GET)
	@ResponseBody
	public ChangeSetDirectory getChangSets(
			HttpServletRequest httpServletRequest,
			QueryControl queryControl,
			ChangeSetQueryExtensionRestrictions restrictions,
			RestFilter restFilter,
			Page page) {
		
		return this.getChangSets(
				httpServletRequest,
				queryControl, null,
				restrictions, 
				restFilter,
				page);
	}
	
	@RequestMapping(value="/changesets", method=RequestMethod.POST)
	@ResponseBody
	public ChangeSetDirectory getChangSets(
			HttpServletRequest httpServletRequest,
			QueryControl queryControl,
			@RequestBody Query query,
			ChangeSetQueryExtensionRestrictions restrictions,
			RestFilter restFilter,
			Page page) {
		
		
		DirectoryResult<ChangeSetDirectoryEntry> directoryResult = 
			this.changeSetQueryExtension.getResourceSummaries(
				this.getChangeSetQuery(
						query, 
						restFilter, 
						restrictions),
				page);

		ChangeSetDirectory directory = this.populateDirectory(
				directoryResult, 
				page, 
				httpServletRequest, 
				ChangeSetDirectory.class);

		return directory;
	}

	@RequestMapping(value="/changeset", method=RequestMethod.POST, params="changeseturi")
	public ResponseEntity<Void> importChangeSet(
			@RequestParam(required=true) URI changeseturi) {
		
		String returnedChangeSetUri = this.changeSetService.importChangeSet(changeseturi);
		
		return this.getResponseEntity(returnedChangeSetUri);
	}
	
	@RequestMapping(value="/changeset", method=RequestMethod.POST, params="!changeseturi")
	public ResponseEntity<Void> createChangeSet() {
		
		ChangeSet changeSet = this.changeSetService.createChangeSet();
		
		return this.getResponseEntity(changeSet.getChangeSetURI());
	}
	
	@RequestMapping(value="/changeset/{changeSetUri}", method=RequestMethod.GET)
	@ResponseBody
	public ChangeSet readChangeSet(@PathVariable String changeSetUri) {
		
		return this.changeSetService.readChangeSet(changeSetUri);
	}
	
	@RequestMapping(value="/changeset/{changeSetUri}", method=RequestMethod.DELETE)
	@ResponseBody
	public void rollbackChangeSet(@PathVariable String changeSetUri) {
		
		this.changeSetService.rollbackChangeSet(changeSetUri);
	}

	
	@RequestMapping(value="/changeset/{changeSetUri}", method=RequestMethod.POST)
	@ResponseBody
	public void updateChangeSet(@PathVariable String changeSetUri, @RequestBody UpdateChangeSetMetadataRequest request) {
		FinalizableState currentState = this.changeSetService.readChangeSet(changeSetUri).getState();
		
		UpdatedChangeInstructions updatedChangeInstructions = request.getUpdatedChangeInstructions();
		UpdatedCreator updatedCreator = request.getUpdatedCreator();
		UpdatedOfficialEffectiveDate updatedEffectiveDate = request.getUpdatedOfficialEffectiveDate();
		
		UpdatedState updatedState = request.getUpdatedState();

		NameOrURI creator = null;
		OpaqueData changeInstructions = null;
		Date officialEffectiveDate = null;
		
		if(updatedChangeInstructions != null){
			changeInstructions = updatedChangeInstructions.getChangeInstructions();
		}
		
		if(updatedCreator != null){
			creator = updatedCreator.getCreator();
		}
		
		if(updatedEffectiveDate != null){
			officialEffectiveDate = updatedEffectiveDate.getOfficialEffectiveDate();
		}
		
		this.changeSetService.updateChangeSetMetadata(
				changeSetUri, 
				//TODO: How do we add a Source to the service?
				null, 
				changeInstructions, 
				officialEffectiveDate);
		
		if(updatedState != null){
			FinalizableState newState = updatedState.getState();
			if(currentState.equals(FinalizableState.FINAL)){
				if(newState.equals(FinalizableState.OPEN)){
					throw new Cts2RestException(new ChangeSetIsNotOpen());
				}
			}
			if(currentState.equals(FinalizableState.OPEN)){
				if(newState.equals(FinalizableState.FINAL)){
					this.changeSetService.commitChangeSet(changeSetUri);
				}
			}
		}
	
	}
	
	private ResponseEntity<Void> getResponseEntity(String changeSetUri){
		String location = this.urlTemplateBindingCreator.bindResourceToUrlTemplate("/changeset/{changeSetUri}", changeSetUri);
		
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("Location", location);
		
		return new ResponseEntity<Void>(responseHeaders, HttpStatus.CREATED);
	}
	
	private ChangeSetQuery getChangeSetQuery(
			final Query query, 
			final RestFilter restFilter,
			final ChangeSetQueryExtensionRestrictions restrictions){
		
		final Set<ResolvedFilter> filters = new HashSet<ResolvedFilter>();
		
		filters.add(
				this.getFilterResolver().resolveRestFilter(
						restFilter,
						this.changeSetQueryExtension));
		
		return new ChangeSetQuery() {
			
			@Override
			public Query getQuery() {
				return query;
			}

			@Override
			public Set<ResolvedFilter> getFilterComponent() {
				return filters;
			}

			@Override
			public ChangeSetQueryExtensionRestrictions getChangeSetQueryExtensionRestrictions() {
				return restrictions;
			}
			
		};
	}
}