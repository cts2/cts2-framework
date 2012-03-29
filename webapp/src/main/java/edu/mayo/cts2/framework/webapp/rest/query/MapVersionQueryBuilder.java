package edu.mayo.cts2.framework.webapp.rest.query;

import java.util.Set;

import edu.mayo.cts2.framework.model.command.ResolvedFilter;
import edu.mayo.cts2.framework.model.command.ResolvedReadContext;
import edu.mayo.cts2.framework.model.service.core.Query;
import edu.mayo.cts2.framework.service.command.restriction.MapVersionQueryServiceRestrictions;
import edu.mayo.cts2.framework.service.profile.BaseQueryService;
import edu.mayo.cts2.framework.service.profile.mapversion.MapVersionQuery;
import edu.mayo.cts2.framework.webapp.rest.resolver.FilterResolver;
import edu.mayo.cts2.framework.webapp.rest.resolver.ReadContextResolver;

public class MapVersionQueryBuilder 
	extends AbstractResourceQueryBuilder<MapVersionQueryBuilder, MapVersionQuery> {

	private MapVersionQueryServiceRestrictions restrictions;
	
	public MapVersionQueryBuilder(
			BaseQueryService baseQueryService,
			FilterResolver filterResolver,
			ReadContextResolver readContextResolver) {
		super(baseQueryService, filterResolver, readContextResolver);
	}

	public MapVersionQueryBuilder addRestrictions(MapVersionQueryServiceRestrictions restrictions){
		this.restrictions = restrictions;
		
		return this.getThis();
	}

	@Override
	protected MapVersionQueryBuilder getThis() {
		return this;
	}

	@Override
	public MapVersionQuery build() {
		final DefaultResourceQuery query = new DefaultResourceQuery();
		
		return new MapVersionQuery(){

			@Override
			public Query getQuery() {
				return query.getQuery();
			}

			@Override
			public Set<ResolvedFilter> getFilterComponent() {
				return query.getFilterComponent();
			}

			@Override
			public ResolvedReadContext getReadContext() {
				return query.getReadContext();
			}

			@Override
			public MapVersionQueryServiceRestrictions getRestrictions() {
				return restrictions;
			}
			
		};
	}
}
