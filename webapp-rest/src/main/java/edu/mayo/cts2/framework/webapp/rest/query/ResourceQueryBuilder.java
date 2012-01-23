package edu.mayo.cts2.framework.webapp.rest.query;

import edu.mayo.cts2.framework.service.profile.BaseQueryService;
import edu.mayo.cts2.framework.service.profile.ResourceQuery;
import edu.mayo.cts2.framework.webapp.rest.resolver.FilterResolver;
import edu.mayo.cts2.framework.webapp.rest.resolver.ReadContextResolver;

public class ResourceQueryBuilder 
	extends AbstractResourceQueryBuilder<ResourceQueryBuilder, ResourceQuery> {

	public ResourceQueryBuilder(BaseQueryService baseQueryService,
			FilterResolver filterResolver,
			ReadContextResolver readContextResolver) {
		super(baseQueryService, filterResolver, readContextResolver);
	}

	@Override
	public ResourceQuery build() {
		return new DefaultResourceQuery();
	}

	@Override
	protected ResourceQueryBuilder getThis() {
		return this;
	}
}
