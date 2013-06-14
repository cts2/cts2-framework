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
import java.util.Map;

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

import edu.mayo.cts2.framework.model.command.Page;
import edu.mayo.cts2.framework.model.core.CodeSystemVersionReference;
import edu.mayo.cts2.framework.model.core.Message;
import edu.mayo.cts2.framework.model.extension.LocalIdStatement;
import edu.mayo.cts2.framework.model.service.core.Query;
import edu.mayo.cts2.framework.model.service.exception.UnknownStatement;
import edu.mayo.cts2.framework.model.statement.Statement;
import edu.mayo.cts2.framework.model.statement.StatementDirectory;
import edu.mayo.cts2.framework.model.statement.StatementList;
import edu.mayo.cts2.framework.model.statement.StatementMsg;
import edu.mayo.cts2.framework.model.util.ModelUtils;
import edu.mayo.cts2.framework.service.profile.ResourceQuery;
import edu.mayo.cts2.framework.service.profile.statement.StatementMaintenanceService;
import edu.mayo.cts2.framework.service.profile.statement.StatementQueryService;
import edu.mayo.cts2.framework.service.profile.statement.StatementReadService;
import edu.mayo.cts2.framework.service.profile.statement.name.StatementReadId;
import edu.mayo.cts2.framework.webapp.rest.command.QueryControl;
import edu.mayo.cts2.framework.webapp.rest.command.RestFilter;
import edu.mayo.cts2.framework.webapp.rest.command.RestReadContext;
import edu.mayo.cts2.framework.webapp.rest.query.ResourceQueryBuilder;

/**
 * The Class StatementController.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Controller
public class StatementController extends AbstractMessageWrappingController {
	
	@Cts2Service
	private StatementReadService statementReadService;
	
	@Cts2Service
	private StatementQueryService statementQueryService;
	
	@Cts2Service
	private StatementMaintenanceService statementMaintenanceService;
	
	private final static UrlTemplateBinder<LocalIdStatement> URL_BINDER =
			new UrlTemplateBinder<LocalIdStatement>(){

		@Override
		public Map<String,String> getPathValues(LocalIdStatement resource) {
			Map<String,String> returnMap = new HashMap<String,String>();
			
			CodeSystemVersionReference assertedIn = getAssertedIn(resource.getResource());
			
			returnMap.put(VAR_CODESYSTEMID, assertedIn.getCodeSystem().getContent());
			returnMap.put(VAR_CODESYSTEMVERSIONID, assertedIn.getVersion().getContent());
			returnMap.put(VAR_STATEMENTID, resource.getLocalID());
			
			return returnMap;
		}

	};
	
	private static CodeSystemVersionReference getAssertedIn(Statement resource){
		if(resource.getAssertedIn() != null){
			return resource.getAssertedIn();
		} else {
			return resource.getAssertedBy();
		}
	}
	
	private final static MessageFactory<LocalIdStatement> MESSAGE_FACTORY = 
			new MessageFactory<LocalIdStatement>() {

		@Override
		public Message createMessage(LocalIdStatement resource) {
			StatementMsg msg = new StatementMsg();
			msg.setStatement(resource.getResource());

			return msg;
		}
	};
	
	/**
	 * Gets the statements.
	 *
	 * @param httpServletRequest the http servlet request
	 * @param resolvedFilter the filter
	 * @param page the page
	 * @return the statements
	 */
	@RequestMapping(value=PATH_STATEMENTS, method=RequestMethod.GET)
	public Object getStatements(
			HttpServletRequest httpServletRequest,
			RestReadContext restReadContext,
			QueryControl queryControl,
			RestFilter restFilter,
			Page page,
			boolean list) {
		
		return this.getStatements(
				httpServletRequest, 
				restReadContext, 
				queryControl,
				null, 
				restFilter, 
				page,
				list);
	}
	
	/**
	 * Gets the statements.
	 *
	 * @param httpServletRequest the http servlet request
	 * @param query the query
	 * @param resolvedFilter the filter
	 * @param page the page
	 * @return the statements
	 */
	@RequestMapping(value=PATH_STATEMENTS, method=RequestMethod.POST)
	public Object getStatements(
			HttpServletRequest httpServletRequest,
			RestReadContext restReadContext,
			QueryControl queryControl,
			@RequestBody Query query,
			RestFilter restFilter,
			Page page,
			boolean list) {
		
		ResourceQueryBuilder builder = this.getNewResourceQueryBuilder();
		
		ResourceQuery resourceQuery = builder.
				addQuery(query).
				addRestFilter(restFilter).
				addRestReadContext(restReadContext).
				build();
		
		return this.doQuery(
				httpServletRequest,
				list, 
				this.statementQueryService,
				resourceQuery,
				page, 
				queryControl,
				StatementDirectory.class, 
				StatementList.class);
	}
	
	/**
	 * Gets the statements count.
	 *
	 * @param httpServletResponse the http servlet response
	 * @param query the query
	 * @param resolvedFilter the filter
	 * @return the statements count
	 */
	@RequestMapping(value=PATH_STATEMENTS, method=RequestMethod.HEAD)
	@ResponseBody
	public void getStatementsCount(
			HttpServletResponse httpServletResponse,
			RestReadContext restReadContext,
			RestFilter restFilter) {
		
		ResourceQueryBuilder builder = this.getNewResourceQueryBuilder();
		
		ResourceQuery resourceQuery = builder.
				addRestFilter(restFilter).
				addRestReadContext(restReadContext).
				build();
		
		int count = this.statementQueryService.count(resourceQuery);
		
		this.setCount(count, httpServletResponse);
	}
	
	/**
	 * Creates the statement.
	 *
	 * @param httpServletRequest the http servlet request
	 * @param statement the statement
	 * @param changeseturi the changeseturi
	 * @param statementName the statement name
	 * @return 
	 */
	@RequestMapping(value=PATH_STATEMENT, method=RequestMethod.POST)
	public Object createStatement(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse,
			@RequestBody Statement statement,
			@RequestParam(value=PARAM_CHANGESETCONTEXT, required=false) String changeseturi) {
	
		return this.doCreate(
				httpServletResponse,
				statement,
				changeseturi, 
				PATH_STATEMENT_OF_CODESYSTEMVERSION_BYID, 
				URL_BINDER, 
				this.statementMaintenanceService);
	}
	
	@RequestMapping(value=PATH_STATEMENT_OF_CODESYSTEMVERSION_BYID, method=RequestMethod.PUT)
	public Object updateStatement(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse,
			@RequestBody Statement statement,
			@PathVariable(VAR_CODESYSTEMID) String codeSystemName,
			@PathVariable(VAR_CODESYSTEMVERSIONID) String codeSystemVersionName,
			@PathVariable(VAR_STATEMENTID) String statementLocalName,
			@RequestParam(value=PARAM_CHANGESETCONTEXT, required=false) String changeseturi) {
				
		return this.doUpdate(
				httpServletResponse,
				new LocalIdStatement(statementLocalName, statement),
				changeseturi, 
				new StatementReadId(
						statementLocalName, 
						ModelUtils.nameOrUriFromName(codeSystemVersionName)),
				this.statementMaintenanceService);
	}
	
	/**
	 * Gets the statement by uri.
	 *
	 * @param httpServletRequest the http servlet request
	 * @param uri the uri
	 * @return the statement by uri
	 */
	@RequestMapping(value=PATH_STATEMENTBYURI, method=RequestMethod.GET)
	public ModelAndView getStatementByUri(
			HttpServletRequest httpServletRequest,
			RestReadContext restReadContext,
			@RequestParam(PARAM_URI) String uri,
			@RequestParam(value="redirect", defaultValue=DEFAULT_REDIRECT) boolean redirect) {
		
		StatementReadId id = new StatementReadId(uri);
		
		return this.doReadByUri(
				httpServletRequest, 
				MESSAGE_FACTORY, 
				PATH_STATEMENTBYURI, 
				PATH_STATEMENT_OF_CODESYSTEMVERSION_BYID, 
				URL_BINDER, 
				this.statementReadService, 
				restReadContext,
				UnknownStatement.class, 
				id,
				redirect);
	}
	
	@RequestMapping(value={	
			PATH_STATEMENT_OF_CODESYSTEMVERSION_BYID
			},
		method=RequestMethod.GET)
	public Object getStatementOfCodeSystemVersionByLocalId(
			HttpServletRequest httpServletRequest,
			RestReadContext restReadContext,
			@PathVariable(VAR_CODESYSTEMID) String codeSystemName,
			@PathVariable(VAR_CODESYSTEMVERSIONID) String codeSystemVersionName,
			@PathVariable(VAR_STATEMENTID) String statementLocalName) {
		
		StatementReadId id = 
				new StatementReadId(
						statementLocalName,
						ModelUtils.nameOrUriFromName(codeSystemVersionName));
		
		return this.doRead(
				httpServletRequest, 
				MESSAGE_FACTORY, 
				this.statementReadService, 
				restReadContext, 
				UnknownStatement.class, 
				id);
	}
	
	@RequestMapping(value={	
			PATH_STATEMENT_OF_CODESYSTEMVERSION_BYID
			},
		method=RequestMethod.DELETE)
	public Object deleteStatement(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse,
			RestReadContext restReadContext,
			@PathVariable(VAR_CODESYSTEMID) String codeSystemName,
			@PathVariable(VAR_CODESYSTEMVERSIONID) String codeSystemVersionName,
			@PathVariable(VAR_STATEMENTID) String statementLocalName,
			@RequestParam(PARAM_CHANGESETCONTEXT) String changeseturi) {
		
		StatementReadId id = 
				new StatementReadId(
						statementLocalName,
						ModelUtils.nameOrUriFromName(codeSystemVersionName));
		
		return this.doDelete(
				httpServletResponse, 
				id, 
				changeseturi,
				this.statementMaintenanceService);
	}
	
	private ResourceQueryBuilder getNewResourceQueryBuilder(){
		return new ResourceQueryBuilder(
			this.statementQueryService, 
			this.getFilterResolver(),
			this.getReadContextResolver());
	}
}