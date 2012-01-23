package edu.mayo.cts2.framework.webapp.rest.resolver;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.command.ResolvedFilter;
import edu.mayo.cts2.framework.model.core.MatchAlgorithmReference;
import edu.mayo.cts2.framework.model.core.ModelAttributeReference;
import edu.mayo.cts2.framework.model.core.PredicateReference;
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

		switch(restFilter.getReferencetype()){
			case ATTRIBUTE: {
				ModelAttributeReference modelAttributeRef = 
						ControllerUtils.getReference(nameOrUri, service.getSupportedModelAttributes());
				
				resolvedFilter.setModelAttributeReference(modelAttributeRef);
				break;
			}
			case PROPERTY: {
				PredicateReference propertyRef = 
						ControllerUtils.getPredicateReference(nameOrUri, service.getSupportedProperties());
				
				resolvedFilter.setPropertyReference(propertyRef);
				break;
			}
			case SPECIAL: {
				throw new UnsupportedOperationException("SPECIAL not supported yet.");
			}
			default : {
				throw new IllegalStateException();
			}
		}

		resolvedFilter.setMatchValue(restFilter.getMatchvalue());
		
		return resolvedFilter;
	}
}
