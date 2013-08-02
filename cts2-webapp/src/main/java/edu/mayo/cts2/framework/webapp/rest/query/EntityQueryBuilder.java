package edu.mayo.cts2.framework.webapp.rest.query;

import java.util.Set;

import edu.mayo.cts2.framework.model.command.ResolvedFilter;
import edu.mayo.cts2.framework.model.command.ResolvedReadContext;
import edu.mayo.cts2.framework.model.service.core.Query;
import edu.mayo.cts2.framework.service.command.restriction.EntityDescriptionQueryServiceRestrictions;
import edu.mayo.cts2.framework.service.profile.BaseQueryService;
import edu.mayo.cts2.framework.service.profile.entitydescription.EntitiesFromAssociationsQuery;
import edu.mayo.cts2.framework.service.profile.entitydescription.EntityDescriptionQuery;
import edu.mayo.cts2.framework.webapp.rest.resolver.FilterResolver;
import edu.mayo.cts2.framework.webapp.rest.resolver.ReadContextResolver;

public class EntityQueryBuilder 
	extends AbstractResourceQueryBuilder<EntityQueryBuilder, EntityDescriptionQuery> {

	private EntityDescriptionQueryServiceRestrictions restrictions;
	
	private EntitiesFromAssociationsQuery entitiesFromAssociationsQuery;
	
	public EntityQueryBuilder(
			BaseQueryService baseQueryService,
			FilterResolver filterResolver,
			ReadContextResolver readContextResolver) {
		super(baseQueryService, filterResolver, readContextResolver);
	}

	public EntityQueryBuilder addRestrictions(EntityDescriptionQueryServiceRestrictions restrictions){
		this.restrictions = restrictions;
		
		return this.getThis();
	}
	
	public EntityQueryBuilder addAssociationRestrictions(EntitiesFromAssociationsQuery restrictions){
		this.entitiesFromAssociationsQuery = restrictions;
		
		return this.getThis();
	}

	@Override
	protected EntityQueryBuilder getThis() {
		return this;
	}

	@Override
	public EntityDescriptionQuery build() {
		final DefaultResourceQuery query = new DefaultResourceQuery();
		
		return new EntityDescriptionQuery(){

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
			public EntityDescriptionQueryServiceRestrictions getRestrictions() {
				return restrictions;
			}

			@Override
			public EntitiesFromAssociationsQuery getEntitiesFromAssociationsQuery() {
				return entitiesFromAssociationsQuery;
			}
			
		};
	}
}
