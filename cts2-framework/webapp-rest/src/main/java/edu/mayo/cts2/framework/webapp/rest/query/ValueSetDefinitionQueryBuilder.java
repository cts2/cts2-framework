package edu.mayo.cts2.framework.webapp.rest.query;

import java.util.Set;

import edu.mayo.cts2.framework.model.command.ResolvedFilter;
import edu.mayo.cts2.framework.model.command.ResolvedReadContext;
import edu.mayo.cts2.framework.model.service.core.Query;
import edu.mayo.cts2.framework.service.command.restriction.ValueSetDefinitionQueryServiceRestrictions;
import edu.mayo.cts2.framework.service.profile.BaseQueryService;
import edu.mayo.cts2.framework.service.profile.valuesetdefinition.ValueSetDefinitionQuery;
import edu.mayo.cts2.framework.webapp.rest.resolver.FilterResolver;
import edu.mayo.cts2.framework.webapp.rest.resolver.ReadContextResolver;

public class ValueSetDefinitionQueryBuilder 
	extends AbstractResourceQueryBuilder<ValueSetDefinitionQueryBuilder, ValueSetDefinitionQuery> {

	private ValueSetDefinitionQueryServiceRestrictions restrictions;
	
	public ValueSetDefinitionQueryBuilder(
			BaseQueryService baseQueryService,
			FilterResolver filterResolver,
			ReadContextResolver readContextResolver) {
		super(baseQueryService, filterResolver, readContextResolver);
	}

	public ValueSetDefinitionQueryBuilder addRestrictions(ValueSetDefinitionQueryServiceRestrictions restrictions){
		this.restrictions = restrictions;
		
		return this.getThis();
	}

	@Override
	protected ValueSetDefinitionQueryBuilder getThis() {
		return this;
	}

	@Override
	public ValueSetDefinitionQuery build() {
		final DefaultResourceQuery query = new DefaultResourceQuery();
		
		return new ValueSetDefinitionQuery(){

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
			public ValueSetDefinitionQueryServiceRestrictions getRestrictions() {
				return restrictions;
			}
			
		};
	}
}
