package edu.mayo.cts2.framework.webapp.soap.resolver;

import org.apache.commons.lang.StringUtils;

import edu.mayo.cts2.framework.model.command.ResolvedFilter;
import edu.mayo.cts2.framework.model.core.MatchAlgorithmReference;
import edu.mayo.cts2.framework.model.core.ModelAttributeReference;
import edu.mayo.cts2.framework.model.core.PredicateReference;
import edu.mayo.cts2.framework.model.service.core.FilterComponent;
import edu.mayo.cts2.framework.model.service.core.NameOrURI;
import edu.mayo.cts2.framework.service.profile.BaseQueryService;
import edu.mayo.cts2.framework.webapp.rest.util.ControllerUtils;

public class FilterResolver {

	public ResolvedFilter resolveFilter(FilterComponent restFilter, BaseQueryService service){
		if(restFilter == null ||
				StringUtils.isBlank(restFilter.getMatchValue())){
			return null;
		}
		
		NameOrURI matchAlgorithmReference = restFilter.getMatchAlgorithm();
		
		MatchAlgorithmReference matchRef = 
			ControllerUtils.getReference(matchAlgorithmReference, service.getSupportedMatchAlgorithms());
			
		ResolvedFilter resolvedFilter = new ResolvedFilter();
		resolvedFilter.setMatchAlgorithmReference(matchRef);
		
		String name = restFilter.getEntityName().getName();

		switch(restFilter.getReferenceType()){
			case ATTRIBUTE: {
				ModelAttributeReference modelAttributeRef = 
						ControllerUtils.getReference(name, service.getSupportedModelAttributes());
				
				resolvedFilter.setModelAttributeReference(modelAttributeRef);
				break;
			}
			case PROPERTY: {
				PredicateReference propertyRef = 
						ControllerUtils.getPredicateReference(name, service.getSupportedProperties());
				
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

		resolvedFilter.setMatchValue(restFilter.getMatchValue());
		
		return resolvedFilter;
	}
}
