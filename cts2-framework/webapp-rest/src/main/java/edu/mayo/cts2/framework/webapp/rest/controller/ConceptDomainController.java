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
import org.springframework.web.servlet.ModelAndView;

import edu.mayo.cts2.framework.model.conceptdomain.ConceptDomainCatalogEntry;
import edu.mayo.cts2.framework.model.conceptdomain.ConceptDomainCatalogEntryDirectory;
import edu.mayo.cts2.framework.model.conceptdomain.ConceptDomainCatalogEntryMsg;
import edu.mayo.cts2.framework.model.conceptdomain.ConceptDomainCatalogEntrySummary;
import edu.mayo.cts2.framework.model.core.FilterComponent;
import edu.mayo.cts2.framework.model.core.Message;
import edu.mayo.cts2.framework.model.directory.DirectoryResult;
import edu.mayo.cts2.framework.model.service.core.Query;
import edu.mayo.cts2.framework.service.command.Filter;
import edu.mayo.cts2.framework.service.command.Page;
import edu.mayo.cts2.framework.service.command.QueryControl;
import edu.mayo.cts2.framework.service.name.Name;
import edu.mayo.cts2.framework.service.profile.conceptdomain.ConceptDomainMaintenanceService;
import edu.mayo.cts2.framework.service.profile.conceptdomain.ConceptDomainQueryService;
import edu.mayo.cts2.framework.service.profile.conceptdomain.ConceptDomainReadService;

/**
 * The Class ConceptDomainController.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Controller
public class ConceptDomainController extends AbstractServiceAwareController {
	
	@Cts2Service
	private ConceptDomainReadService conceptDomainReadService;

	@Cts2Service
	private ConceptDomainQueryService conceptDomainQueryService;
	
	@Cts2Service
	private ConceptDomainMaintenanceService conceptDomainMaintenanceService;

	private final static UrlTemplateBinder<ConceptDomainCatalogEntry> URL_BINDER =
			new UrlTemplateBinder<ConceptDomainCatalogEntry>(){

		@Override
		public String getValueForPathAttribute(String attribute, ConceptDomainCatalogEntry resource) {
			if(attribute.equals(VAR_CONCEPTDOMAINID)){
				return resource.getConceptDomainName();
			}
			return null;
		}

	};
	
	private final static MessageFactory<ConceptDomainCatalogEntry> MESSAGE_FACTORY = 
			new MessageFactory<ConceptDomainCatalogEntry>() {

		@Override
		public Message createMessage(ConceptDomainCatalogEntry resource) {
			ConceptDomainCatalogEntryMsg msg = new ConceptDomainCatalogEntryMsg();
			msg.setConceptDomainCatalogEntry(resource);

			return msg;
		}
	};
	
	/**
	 * Gets the concept domains.
	 *
	 * @param httpServletRequest the http servlet request
	 * @param filter the filter
	 * @param page the page
	 * @return 
	 * @return the concept domains
	 */
	@RequestMapping(value=PATH_CONCEPTDOMAINS, method=RequestMethod.GET)
	@ResponseBody
	public ConceptDomainCatalogEntryDirectory getConceptDomains(
			HttpServletRequest httpServletRequest,
			Filter filter,
			Page page) {
		
		return this.getConceptDomains(
				httpServletRequest,
				null, 
				filter, 
				page);
	}

	/**
	 * Gets the concept domains.
	 *
	 * @param httpServletRequest the http servlet request
	 * @param query the query
	 * @param filter the filter
	 * @param page the page
	 * @return 
	 * @return the concept domains
	 */
	@RequestMapping(value=PATH_CONCEPTDOMAINS, method=RequestMethod.PUT)
	@ResponseBody
	public ConceptDomainCatalogEntryDirectory getConceptDomains(
			HttpServletRequest httpServletRequest,
			@RequestBody Query query,
			Filter filter,
			Page page) {
		
		FilterComponent filterComponent = this.processFilter(filter, this.conceptDomainQueryService);

		DirectoryResult<ConceptDomainCatalogEntrySummary> directoryResult = this.conceptDomainQueryService.getResourceSummaries(
				query,
				filterComponent, 
				null, 
				page);

		ConceptDomainCatalogEntryDirectory directory = this.populateDirectory(
				directoryResult, 
				page, 
				httpServletRequest, 
				ConceptDomainCatalogEntryDirectory.class);

		return directory;
	}
	
	/**
	 * Does concept domain exist.
	 *
	 * @param httpServletResponse the http servlet response
	 * @param conceptDomainName the concept domain name
	 */
	@RequestMapping(value=PATH_CONCEPTDOMAIN_BYID, method=RequestMethod.HEAD)
	@ResponseBody
	public void doesConceptDomainExist(
			HttpServletResponse httpServletResponse,
			@PathVariable(VAR_CONCEPTDOMAINID) String conceptDomainName) {
	
		this.doExists(httpServletResponse, this.conceptDomainReadService, new Name(conceptDomainName));
	}
	
	/**
	 * Gets the concept domains count.
	 *
	 * @param httpServletResponse the http servlet response
	 * @param query the query
	 * @param filter the filter
	 * @return the concept domains count
	 */
	@RequestMapping(value=PATH_CONCEPTDOMAINS, method=RequestMethod.HEAD)
	@ResponseBody
	public void getConceptDomainsCount(
			HttpServletResponse httpServletResponse,
			@RequestBody Query query,
			Filter filter) {
		FilterComponent filterComponent = this.processFilter(filter, this.conceptDomainQueryService);
		
		int count = this.conceptDomainQueryService.count(
				query,
				filterComponent, 
				null);
		
		this.setCount(count, httpServletResponse);
	}

	/**
	 * Gets the concept domain by name.
	 *
	 * @param httpServletRequest the http servlet request
	 * @param conceptDomainName the concept domain name
	 * @return 
	 * @return the concept domain by name
	 */
	@RequestMapping(value=PATH_CONCEPTDOMAIN_BYID, method=RequestMethod.GET)
	@ResponseBody
	public Message getConceptDomainByName(
			HttpServletRequest httpServletRequest,
			QueryControl queryControl,
			@PathVariable(VAR_CONCEPTDOMAINID) String conceptDomainName) {
			
		return this.doRead(
				httpServletRequest, 
				MESSAGE_FACTORY, 
				this.conceptDomainReadService,
				new Name(conceptDomainName));
	}
	
	/**
	 * Creates the concept domain.
	 *
	 * @param httpServletRequest the http servlet request
	 * @param conceptDomain the concept domain
	 * @param changeseturi the changeseturi
	 * @param conceptDomainName the concept domain name
	 */
	@RequestMapping(value=PATH_CONCEPTDOMAIN_BYID, method=RequestMethod.PUT)
	@ResponseBody
	public void createConceptDomain(
			HttpServletRequest httpServletRequest,
			@RequestBody ConceptDomainCatalogEntry conceptDomain,
			@RequestParam(required=false) String changeseturi,
			@PathVariable(VAR_CONCEPTDOMAINID) String conceptDomainName) {
			
		this.conceptDomainMaintenanceService.createResource(
				changeseturi, 
				conceptDomain);
	}
	
	/**
	 * Gets the concept domain by uri.
	 *
	 * @param httpServletRequest the http servlet request
	 * @param uri the uri
	 * @return the concept domain by uri
	 */
	@RequestMapping(value=PATH_CONCEPTDOMAIN_BYURI, method=RequestMethod.GET)
	@ResponseBody
	public ModelAndView getConceptDomainByUri(
			HttpServletRequest httpServletRequest,
			QueryControl queryControl,
			@PathVariable(VAR_URI) String uri,
			@RequestParam(value="redirect", defaultValue="false") boolean redirect) {
		
		return this.doReadByUri(
				httpServletRequest, 
				MESSAGE_FACTORY, 
				PATH_CONCEPTDOMAIN_BYID, 
				PATH_CONCEPTDOMAIN_BYURI, 
				URL_BINDER, 
				this.conceptDomainReadService, 
				uri, 
				redirect);
	}

}