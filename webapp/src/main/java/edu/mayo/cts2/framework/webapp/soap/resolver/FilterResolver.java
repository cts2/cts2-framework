package edu.mayo.cts2.framework.webapp.soap.resolver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import edu.mayo.cts2.framework.model.command.ResolvedFilter;
import edu.mayo.cts2.framework.model.core.MatchAlgorithmReference;
import edu.mayo.cts2.framework.model.core.PropertyReference;
import edu.mayo.cts2.framework.model.service.core.FilterComponent;
import edu.mayo.cts2.framework.model.service.core.NameOrURI;
import edu.mayo.cts2.framework.service.profile.BaseQueryService;
import edu.mayo.cts2.framework.webapp.rest.util.ControllerUtils;

public class FilterResolver {

	public Collection<ResolvedFilter> resolveFilter(FilterComponent filter, BaseQueryService service){
		if(filter == null ||
				StringUtils.isBlank(filter.getMatchValue())){
			return null;
		}
		
		NameOrURI matchAlgorithmReference = filter.getMatchAlgorithm();
		
		MatchAlgorithmReference matchRef = 
			ControllerUtils.getReference(matchAlgorithmReference, service.getSupportedMatchAlgorithms());
			
		List<ResolvedFilter> returnList = new ArrayList<ResolvedFilter>();
		
		if(filter.getFilterComponents() != null){
			for(NameOrURI nameOrURI : filter.getFilterComponents().getEntry()){
				ResolvedFilter resolvedFilter = new ResolvedFilter();
				resolvedFilter.setMatchAlgorithmReference(matchRef);

				//TODO: Check for a URI here
				String name = nameOrURI.getName();

				PropertyReference propertyReference = 
						ControllerUtils.getPropertyReference(name, service.getSupportedSearchReferences());
				
				resolvedFilter.setPropertyReference(propertyReference);

				resolvedFilter.setMatchValue(filter.getMatchValue());
				
				returnList.add(resolvedFilter);
			}
		}
		
		return returnList;
	}
}
