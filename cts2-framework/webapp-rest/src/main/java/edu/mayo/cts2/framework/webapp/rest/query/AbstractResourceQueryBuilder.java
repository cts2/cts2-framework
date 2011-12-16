package edu.mayo.cts2.framework.webapp.rest.query;

import java.util.HashSet;
import java.util.Set;

import edu.mayo.cts2.framework.model.command.ResolvedFilter;
import edu.mayo.cts2.framework.model.command.ResolvedReadContext;
import edu.mayo.cts2.framework.model.service.core.Query;
import edu.mayo.cts2.framework.service.profile.BaseQueryService;
import edu.mayo.cts2.framework.service.profile.ResourceQuery;
import edu.mayo.cts2.framework.webapp.rest.command.RestFilter;
import edu.mayo.cts2.framework.webapp.rest.command.RestReadContext;
import edu.mayo.cts2.framework.webapp.rest.resolver.FilterResolver;
import edu.mayo.cts2.framework.webapp.rest.resolver.ReadContextResolver;

public abstract class AbstractResourceQueryBuilder<T,Q extends ResourceQuery> {
	
	private FilterResolver filterResolver;
	private ReadContextResolver readContextResolver;
	private BaseQueryService baseQueryService;
	
	private Query query;
	private RestReadContext restReadContext;
	private Set<RestFilter> restFilter = new HashSet<RestFilter>();
	
	public AbstractResourceQueryBuilder(
			BaseQueryService baseQueryService,
			FilterResolver filterResolver, 
			ReadContextResolver readContextResolver){
		super();
		this.filterResolver = filterResolver;
		this.readContextResolver = readContextResolver;
	}
	
	public T addQuery(Query query){
		this.query = query;
		
		return this.getThis();
	}
	
	public T addRestReadContext(RestReadContext restReadContext){
		this.restReadContext = restReadContext;
		
		return this.getThis();
	}
	
	public T addRestFilter(RestFilter restFilter){
		this.restFilter.add(restFilter);
		
		return this.getThis();
	}
	
	public abstract Q build();
	
	protected abstract T getThis();
	
	protected class DefaultResourceQuery implements ResourceQuery {
		
		private ResolvedReadContext resolvedReadContext;
		private Set<ResolvedFilter> resolvedFilters = new HashSet<ResolvedFilter>();
		
		public DefaultResourceQuery() {	
			super();
			this.resolvedReadContext = readContextResolver.resolveRestReadContext(restReadContext);
			for(RestFilter filter : restFilter){
				resolvedFilters.add(
						filterResolver.resolveRestFilter(
								filter, 
								baseQueryService));
			}
		}

		@Override
		public Query getQuery() {
			return query;
		}

		@Override
		public Set<ResolvedFilter> getFilterComponent() {
			return resolvedFilters;
		}

		@Override
		public ResolvedReadContext getReadContext() {
			return resolvedReadContext;
		}
		
	}

}