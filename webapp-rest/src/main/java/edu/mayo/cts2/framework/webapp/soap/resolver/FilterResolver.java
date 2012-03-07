package edu.mayo.cts2.framework.webapp.soap.resolver;

import org.apache.commons.lang.StringUtils;

import edu.mayo.cts2.framework.model.command.ResolvedFilter;
import edu.mayo.cts2.framework.model.core.MatchAlgorithmReference;
import edu.mayo.cts2.framework.model.core.PropertyReference;
import edu.mayo.cts2.framework.model.core.types.TargetReferenceType;
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
		
		//TODO: Check for a URI here
		String name = restFilter.getEntityName().getName();

		TargetReferenceType type = restFilter.getReferenceType();

		PropertyReference propertyReference = 
				ControllerUtils.getPropertyReference(type, name, service.getSupportedSearchReferences());
		
		resolvedFilter.setPropertyReference(propertyReference);

		resolvedFilter.setMatchValue(restFilter.getMatchValue());
		
		return resolvedFilter;
	}
}
