package edu.mayo.cts2.framework.webapp.rest.query;

import java.util.Set;

import edu.mayo.cts2.framework.model.command.ResolvedFilter;
import edu.mayo.cts2.framework.model.command.ResolvedReadContext;
import edu.mayo.cts2.framework.model.service.core.Query;
import edu.mayo.cts2.framework.service.command.restriction.ValueSetQueryServiceRestrictions;
import edu.mayo.cts2.framework.service.profile.BaseQueryService;
import edu.mayo.cts2.framework.service.profile.valueset.ValueSetQuery;
import edu.mayo.cts2.framework.webapp.rest.resolver.FilterResolver;
import edu.mayo.cts2.framework.webapp.rest.resolver.ReadContextResolver;

public class ValueSetQueryBuilder 
	extends AbstractResourceQueryBuilder<ValueSetQueryBuilder, ValueSetQuery> {

	private ValueSetQueryServiceRestrictions restrictions;
	
	public ValueSetQueryBuilder(
			BaseQueryService baseQueryService,
			FilterResolver filterResolver,
			ReadContextResolver readContextResolver) {
		super(baseQueryService, filterResolver, readContextResolver);
	}

	public ValueSetQueryBuilder addRestrictions(ValueSetQueryServiceRestrictions restrictions){
		this.restrictions = restrictions;
		
		return this.getThis();
	}

	@Override
	protected ValueSetQueryBuilder getThis() {
		return this;
	}

	@Override
	public ValueSetQuery build() {
		final DefaultResourceQuery query = new DefaultResourceQuery();
		
		return new ValueSetQuery(){

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
			public ValueSetQueryServiceRestrictions getRestrictions() {
				return restrictions;
			}
			
		};
	}
}
