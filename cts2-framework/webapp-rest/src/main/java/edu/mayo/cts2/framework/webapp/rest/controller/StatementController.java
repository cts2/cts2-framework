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

import edu.mayo.cts2.framework.model.core.FilterComponent;
import edu.mayo.cts2.framework.model.core.Message;
import edu.mayo.cts2.framework.model.directory.DirectoryResult;
import edu.mayo.cts2.framework.model.service.core.Query;
import edu.mayo.cts2.framework.model.statement.Statement;
import edu.mayo.cts2.framework.model.statement.StatementDirectory;
import edu.mayo.cts2.framework.model.statement.StatementDirectoryEntry;
import edu.mayo.cts2.framework.model.statement.StatementMsg;
import edu.mayo.cts2.framework.service.command.Filter;
import edu.mayo.cts2.framework.service.command.Page;
import edu.mayo.cts2.framework.service.command.QueryControl;
import edu.mayo.cts2.framework.service.profile.statement.StatementMaintenanceService;
import edu.mayo.cts2.framework.service.profile.statement.StatementQueryService;
import edu.mayo.cts2.framework.service.profile.statement.StatementReadService;

/**
 * The Class StatementController.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Controller
public class StatementController extends AbstractServiceAwareController {
	
	@Cts2Service
	private StatementReadService statementReadService;
	
	@Cts2Service
	private StatementQueryService statementQueryService;
	
	@Cts2Service
	private StatementMaintenanceService statementMaintenanceService;
	
	private final static UrlTemplateBinder<Statement> URL_BINDER =
			new UrlTemplateBinder<Statement>(){

		@Override
		public String getValueForPathAttribute(String attribute, Statement resource) {
			//TODO:
			return null;
		}

	};
	
	private final static MessageFactory<Statement> MESSAGE_FACTORY = 
			new MessageFactory<Statement>() {

		@Override
		public Message createMessage(Statement resource) {
			StatementMsg msg = new StatementMsg();
			msg.setStatement(resource);

			return msg;
		}
	};
	
	/**
	 * Gets the statements.
	 *
	 * @param httpServletRequest the http servlet request
	 * @param filter the filter
	 * @param page the page
	 * @return the statements
	 */
	@RequestMapping(value=PATH_STATEMENTS, method=RequestMethod.GET)
	public StatementDirectory getStatements(
			HttpServletRequest httpServletRequest,
			Filter filter,
			Page page) {
		
		return this.getStatements(httpServletRequest, null, filter, page);
	}
	
	/**
	 * Gets the statements.
	 *
	 * @param httpServletRequest the http servlet request
	 * @param query the query
	 * @param filter the filter
	 * @param page the page
	 * @return the statements
	 */
	@RequestMapping(value=PATH_STATEMENTS, method=RequestMethod.POST)
	@ResponseBody
	public StatementDirectory getStatements(
			HttpServletRequest httpServletRequest,
			@RequestBody Query query,
			Filter filter,
			Page page) {
		
		FilterComponent filterComponent = this.processFilter(filter, this.statementQueryService);

		DirectoryResult<StatementDirectoryEntry> directoryResult = this.statementQueryService.getResourceSummaries(
				query,
				filterComponent, 
				null,
				page);

		StatementDirectory directory = this.populateDirectory(
				directoryResult, 
				page, 
				httpServletRequest, 
				StatementDirectory.class);

		return directory;
	}
	
	/**
	 * Does statement exist.
	 *
	 * @param httpServletResponse the http servlet response
	 * @param statementName the statement name
	 */
	@RequestMapping(value=PATH_STATEMENTBYID, method=RequestMethod.HEAD)
	@ResponseBody
	public void doesStatementExist(
			HttpServletResponse httpServletResponse,
			@PathVariable(VAR_STATEMENTID) String statementId) {
		
		//
	}
	
	/**
	 * Gets the statements count.
	 *
	 * @param httpServletResponse the http servlet response
	 * @param query the query
	 * @param filter the filter
	 * @return the statements count
	 */
	@RequestMapping(value=PATH_STATEMENTS, method=RequestMethod.HEAD)
	@ResponseBody
	public void getStatementsCount(
			HttpServletResponse httpServletResponse,
			@RequestBody Query query,
			Filter filter) {
		FilterComponent filterComponent = this.processFilter(filter, this.statementQueryService);
		
		int count = this.statementQueryService.count(
				query,
				filterComponent,
				null);
		
		this.setCount(count, httpServletResponse);
	}

	/**
	 * Gets the statement by name.
	 *
	 * @param httpServletRequest the http servlet request
	 * @param statementName the statement name
	 * @return the statement by name
	 */
	@RequestMapping(value=PATH_STATEMENTBYID, method=RequestMethod.GET)
	@ResponseBody
	public Message getStatementById(
			HttpServletRequest httpServletRequest,
			QueryControl queryControl,
			@PathVariable(VAR_STATEMENTID) String statementId) {
			
		return this.doRead(
				httpServletRequest, 
				MESSAGE_FACTORY, 
				this.statementReadService, 
				statementId);
	}
	
	/**
	 * Creates the statement.
	 *
	 * @param httpServletRequest the http servlet request
	 * @param statement the statement
	 * @param changeseturi the changeseturi
	 * @param statementName the statement name
	 */
	@RequestMapping(value=PATH_STATEMENTBYID, method=RequestMethod.PUT)
	@ResponseBody
	public void createStatement(
			HttpServletRequest httpServletRequest,
			@RequestBody Statement statement,
			@RequestParam(required=false) String changeseturi,
			@PathVariable(VAR_STATEMENTID) String statementName) {
			
		this.statementMaintenanceService.createResource(changeseturi, statement);
	}
	
	/**
	 * Gets the statement by uri.
	 *
	 * @param httpServletRequest the http servlet request
	 * @param uri the uri
	 * @return the statement by uri
	 */
	@RequestMapping(value=PATH_STATEMENTBYURI, method=RequestMethod.GET)
	@ResponseBody
	public ModelAndView getStatementByUri(
			HttpServletRequest httpServletRequest,
			@PathVariable(VAR_URI) String uri,
			@RequestParam(value="redirect", defaultValue="false") boolean redirect) {
		
		return this.doReadByUri(
				httpServletRequest, 
				MESSAGE_FACTORY, 
				PATH_STATEMENTBYID, 
				PATH_STATEMENTBYURI, 
				URL_BINDER, 
				this.statementReadService, 
				uri,
				redirect);
	}
}