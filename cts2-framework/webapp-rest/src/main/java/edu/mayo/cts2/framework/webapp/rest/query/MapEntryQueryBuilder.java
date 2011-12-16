package edu.mayo.cts2.framework.webapp.rest.query;

import java.util.Set;

import edu.mayo.cts2.framework.model.command.ResolvedFilter;
import edu.mayo.cts2.framework.model.command.ResolvedReadContext;
import edu.mayo.cts2.framework.model.service.core.Query;
import edu.mayo.cts2.framework.service.command.restriction.MapEntryQueryServiceRestrictions;
import edu.mayo.cts2.framework.service.profile.BaseQueryService;
import edu.mayo.cts2.framework.service.profile.mapentry.MapEntryQuery;
import edu.mayo.cts2.framework.webapp.rest.resolver.FilterResolver;
import edu.mayo.cts2.framework.webapp.rest.resolver.ReadContextResolver;

public class MapEntryQueryBuilder 
	extends AbstractResourceQueryBuilder<MapEntryQueryBuilder, MapEntryQuery> {

	private MapEntryQueryServiceRestrictions restrictions;
	
	public MapEntryQueryBuilder(
			BaseQueryService baseQueryService,
			FilterResolver filterResolver,
			ReadContextResolver readContextResolver) {
		super(baseQueryService, filterResolver, readContextResolver);
	}

	public MapEntryQueryBuilder addRestrictions(MapEntryQueryServiceRestrictions restrictions){
		this.restrictions = restrictions;
		
		return this.getThis();
	}

	@Override
	protected MapEntryQueryBuilder getThis() {
		return this;
	}

	@Override
	public MapEntryQuery build() {
		final DefaultResourceQuery query = new DefaultResourceQuery();
		
		return new MapEntryQuery(){

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
			public MapEntryQueryServiceRestrictions getRestrictions() {
				return restrictions;
			}
			
		};
	}
}
