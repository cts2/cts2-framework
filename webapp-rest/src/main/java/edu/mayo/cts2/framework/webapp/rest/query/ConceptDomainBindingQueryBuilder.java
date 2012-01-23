package edu.mayo.cts2.framework.webapp.rest.query;

import java.util.Set;

import edu.mayo.cts2.framework.model.command.ResolvedFilter;
import edu.mayo.cts2.framework.model.command.ResolvedReadContext;
import edu.mayo.cts2.framework.model.service.core.Query;
import edu.mayo.cts2.framework.service.command.restriction.ConceptDomainBindingQueryServiceRestrictions;
import edu.mayo.cts2.framework.service.profile.BaseQueryService;
import edu.mayo.cts2.framework.service.profile.valueset.ConceptDomainBindingQuery;
import edu.mayo.cts2.framework.webapp.rest.resolver.FilterResolver;
import edu.mayo.cts2.framework.webapp.rest.resolver.ReadContextResolver;

public class ConceptDomainBindingQueryBuilder 
	extends AbstractResourceQueryBuilder<ConceptDomainBindingQueryBuilder, ConceptDomainBindingQuery> {

	private ConceptDomainBindingQueryServiceRestrictions restrictions;
	
	public ConceptDomainBindingQueryBuilder(
			BaseQueryService baseQueryService,
			FilterResolver filterResolver,
			ReadContextResolver readContextResolver) {
		super(baseQueryService, filterResolver, readContextResolver);
	}

	public ConceptDomainBindingQueryBuilder addRestrictions(ConceptDomainBindingQueryServiceRestrictions restrictions){
		this.restrictions = restrictions;
		
		return this.getThis();
	}

	@Override
	protected ConceptDomainBindingQueryBuilder getThis() {
		return this;
	}

	@Override
	public ConceptDomainBindingQuery build() {
		final DefaultResourceQuery query = new DefaultResourceQuery();
		
		return new ConceptDomainBindingQuery(){

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
			public ConceptDomainBindingQueryServiceRestrictions getRestrictions() {
				return restrictions;
			}
			
		};
	}
}
