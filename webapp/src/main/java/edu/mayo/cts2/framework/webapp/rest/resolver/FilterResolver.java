package edu.mayo.cts2.framework.webapp.rest.resolver;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.command.ResolvedFilter;
import edu.mayo.cts2.framework.model.core.MatchAlgorithmReference;
import edu.mayo.cts2.framework.model.core.PropertyReference;
import edu.mayo.cts2.framework.service.profile.BaseQueryService;
import edu.mayo.cts2.framework.webapp.rest.command.RestFilter;
import edu.mayo.cts2.framework.webapp.rest.util.ControllerUtils;

@Component
public class FilterResolver {

	public ResolvedFilter resolveRestFilter(RestFilter restFilter, BaseQueryService service){
		if(restFilter == null ||
				StringUtils.isBlank(restFilter.getMatchvalue())){
			return null;
		}
		
		String matchAlgorithmReference = restFilter.getMatchalgorithm();
		
		MatchAlgorithmReference matchRef = 
			ControllerUtils.getReference(matchAlgorithmReference, service.getSupportedMatchAlgorithms());
			
		ResolvedFilter resolvedFilter = new ResolvedFilter();
		resolvedFilter.setMatchAlgorithmReference(matchRef);
		
		String nameOrUri = restFilter.getFiltercomponent();

		PropertyReference propertyReference = 
				ControllerUtils.getComponentReference(nameOrUri, service.getSupportedSearchReferences());
		
		resolvedFilter.setPropertyReference(propertyReference);

		resolvedFilter.setMatchValue(restFilter.getMatchvalue());
		
		return resolvedFilter;
	}
}
