package edu.mayo.cts2.framework.webapp.soap.endpoint.codesystem;

import java.util.HashSet;
import java.util.Set;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import edu.mayo.cts2.framework.model.codesystem.CodeSystemCatalogEntryDirectory;
import edu.mayo.cts2.framework.model.codesystem.CodeSystemCatalogEntrySummary;
import edu.mayo.cts2.framework.model.command.Page;
import edu.mayo.cts2.framework.model.command.ResolvedFilter;
import edu.mayo.cts2.framework.model.command.ResolvedReadContext;
import edu.mayo.cts2.framework.model.directory.DirectoryResult;
import edu.mayo.cts2.framework.model.service.core.FilterComponent;
import edu.mayo.cts2.framework.model.service.core.Query;
import edu.mayo.cts2.framework.model.service.core.QueryControl;
import edu.mayo.cts2.framework.model.wsdl.codesystemquery.Resolve;
import edu.mayo.cts2.framework.model.wsdl.codesystemquery.ResolveResponse;
import edu.mayo.cts2.framework.service.profile.ResourceQuery;
import edu.mayo.cts2.framework.service.profile.codesystem.CodeSystemQueryService;
import edu.mayo.cts2.framework.webapp.soap.directoryuri.DirectoryUriUtils;
import edu.mayo.cts2.framework.webapp.soap.directoryuri.SoapDirectoryUri;
import edu.mayo.cts2.framework.webapp.soap.directoryuri.SoapDirectoryUriRequest;
import edu.mayo.cts2.framework.webapp.soap.endpoint.AbstractQueryServiceEndpoint;
import edu.mayo.cts2.framework.webapp.soap.resolver.FilterResolver;

@Endpoint("CodeSystemCatalogQueryServicesEndpoint")
public class CodeSystemCatalogQueryServicesEndpoint extends AbstractQueryServiceEndpoint {
	
	@Cts2Service 
	private CodeSystemQueryService codeSystemQueryService;
	
	private FilterResolver filterResolver = new FilterResolver();

	@PayloadRoot(localPart = "resolve", namespace = "http://schema.omg.org/spec/CTS2/1.0/wsdl/CodeSystemCatalogQueryServices")
	@ResponsePayload
	public ResolveResponse resolve(@RequestPayload final Resolve request) {

		@SuppressWarnings("unchecked")
		final SoapDirectoryUriRequest<Void> directoryUriRequest = 
				(SoapDirectoryUriRequest<Void>) 
					DirectoryUriUtils.deserialize(request.getDirectory());
		
		SoapDirectoryUri<Void> directoryUri = directoryUriRequest.getSoapDirectoryUri();
		
		if(directoryUri.getSetOperation() != null){
			throw new UnsupportedOperationException("Set Operations not implemented.");
		}
		
		Set<FilterComponent> filters = directoryUri.getFilterComponents();
		
		final Set<ResolvedFilter> resolvedFilters = new HashSet<ResolvedFilter>();
		
		for(FilterComponent filter : filters){
			ResolvedFilter resolvedFilter = 
					filterResolver.resolveFilter(filter, this.codeSystemQueryService);
			
			resolvedFilters.add(resolvedFilter);
		}

		//TODO: Need to abstract this for other Query Services, 
		//and ad Sort, etc.
		DirectoryResult<CodeSystemCatalogEntrySummary> result = this.codeSystemQueryService.getResourceSummaries(
				new ResourceQuery(){

					@Override
					public Query getQuery() {
						return null;
					}

					@Override
					public Set<ResolvedFilter> getFilterComponent() {
						return resolvedFilters;
					}

					@Override
					public ResolvedReadContext getReadContext() {
						return resolveReadContext(request.getContext());
					}
					
				}, 
				null,
				this.getPage(
						directoryUriRequest.getPage(),
						request.getQueryControl()));
		
		CodeSystemCatalogEntryDirectory directory = 
				this.populateDirectory(
						result, 
						directoryUriRequest, 
						CodeSystemCatalogEntryDirectory.class);
		
		ResolveResponse response = new ResolveResponse();
		response.setCodeSystemCatalogEntryDirectory(directory);
		
		return response;
	}
	
	
	
	protected Page getPage(int pageNumber, QueryControl queryControl){
		Page page = new Page();
		if(queryControl != null){
			page.setMaxToReturn(queryControl.getMaxToReturn().intValue());
		}
		
		page.setPage(pageNumber);
		
		return page;
	}

}
