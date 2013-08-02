package edu.mayo.cts2.framework.webapp.rest.query;

import java.util.Set;

import edu.mayo.cts2.framework.model.command.ResolvedFilter;
import edu.mayo.cts2.framework.model.command.ResolvedReadContext;
import edu.mayo.cts2.framework.model.service.core.Query;
import edu.mayo.cts2.framework.service.command.restriction.AssociationQueryServiceRestrictions;
import edu.mayo.cts2.framework.service.profile.BaseQueryService;
import edu.mayo.cts2.framework.service.profile.association.AssociationQuery;
import edu.mayo.cts2.framework.webapp.rest.resolver.FilterResolver;
import edu.mayo.cts2.framework.webapp.rest.resolver.ReadContextResolver;

public class AssociationQueryBuilder 
	extends AbstractResourceQueryBuilder<AssociationQueryBuilder, AssociationQuery> {

	private AssociationQueryServiceRestrictions restrictions;
	
	public AssociationQueryBuilder(
			BaseQueryService baseQueryService,
			FilterResolver filterResolver,
			ReadContextResolver readContextResolver) {
		super(baseQueryService, filterResolver, readContextResolver);
	}

	public AssociationQueryBuilder addRestrictions(AssociationQueryServiceRestrictions restrictions){
		this.restrictions = restrictions;
		
		return this.getThis();
	}

	@Override
	protected AssociationQueryBuilder getThis() {
		return this;
	}

	@Override
	public AssociationQuery build() {
		final DefaultResourceQuery query = new DefaultResourceQuery();
		
		return new AssociationQuery(){

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
			public AssociationQueryServiceRestrictions getRestrictions() {
				return restrictions;
			}
			
		};
	}
}
