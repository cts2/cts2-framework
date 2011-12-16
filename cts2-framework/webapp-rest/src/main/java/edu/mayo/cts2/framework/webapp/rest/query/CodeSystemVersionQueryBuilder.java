package edu.mayo.cts2.framework.webapp.rest.query;

import java.util.Set;

import edu.mayo.cts2.framework.model.command.ResolvedFilter;
import edu.mayo.cts2.framework.model.command.ResolvedReadContext;
import edu.mayo.cts2.framework.model.service.core.Query;
import edu.mayo.cts2.framework.service.command.restriction.CodeSystemVersionQueryServiceRestrictions;
import edu.mayo.cts2.framework.service.profile.BaseQueryService;
import edu.mayo.cts2.framework.service.profile.codesystemversion.CodeSystemVersionQuery;
import edu.mayo.cts2.framework.webapp.rest.resolver.FilterResolver;
import edu.mayo.cts2.framework.webapp.rest.resolver.ReadContextResolver;

public class CodeSystemVersionQueryBuilder 
	extends AbstractResourceQueryBuilder<CodeSystemVersionQueryBuilder, CodeSystemVersionQuery> {

	private CodeSystemVersionQueryServiceRestrictions restrictions;
	
	public CodeSystemVersionQueryBuilder(
			BaseQueryService baseQueryService,
			FilterResolver filterResolver,
			ReadContextResolver readContextResolver) {
		super(baseQueryService, filterResolver, readContextResolver);
	}

	public CodeSystemVersionQueryBuilder addRestrictions(CodeSystemVersionQueryServiceRestrictions restrictions){
		this.restrictions = restrictions;
		
		return this.getThis();
	}

	@Override
	protected CodeSystemVersionQueryBuilder getThis() {
		return this;
	}

	@Override
	public CodeSystemVersionQuery build() {
		final DefaultResourceQuery query = new DefaultResourceQuery();
		
		return new CodeSystemVersionQuery(){

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
			public CodeSystemVersionQueryServiceRestrictions getRestrictions() {
				return restrictions;
			}
			
		};
	}
}
