package edu.mayo.cts2.framework.webapp.rest.util;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import edu.mayo.cts2.framework.core.util.EncodingUtils;
import edu.mayo.cts2.framework.model.core.NameAndMeaningReference;
import edu.mayo.cts2.framework.model.core.PredicateReference;
import edu.mayo.cts2.framework.model.exception.ExceptionFactory;
import edu.mayo.cts2.framework.model.service.core.EntityNameOrURI;
import edu.mayo.cts2.framework.model.service.core.NameOrURI;
import edu.mayo.cts2.framework.model.util.ModelUtils;

public class ControllerUtils {

	private ControllerUtils(){
		super();
	}
	
	public static Set<NameOrURI> idsToNameOrUriSet(Iterable<String> ids){
		Set<NameOrURI> returnSet = new HashSet<NameOrURI>();
		
		if(ids == null){
			return returnSet;
		}
		
		for(String id : ids){
			NameOrURI nameOrUri = ModelUtils.nameOrUriFromEither(id);
			returnSet.add(nameOrUri);
		}
		return returnSet;
	}
	
	public static Set<EntityNameOrURI> idsToEntityNameOrUriSet(Iterable<String> ids){
		Set<EntityNameOrURI> returnSet = new HashSet<EntityNameOrURI>();
		
		if(ids == null){
			return returnSet;
		}
		
		for(String id : ids){
			EntityNameOrURI nameOrUri;
			if(ModelUtils.isValidUri(id)){
				nameOrUri = ModelUtils.entityNameOrUriFromUri(id);
			} else {
				nameOrUri = ModelUtils.entityNameOrUriFromName(
						EncodingUtils.decodeEntityName(id));
			}
			returnSet.add(nameOrUri);
		}
		return returnSet;
	}
	
	public static <R extends NameAndMeaningReference> R getReference(String nameOrUri, Iterable<R> list) {
		
		for(R ref : list){
			if(StringUtils.equals(ref.getContent(), nameOrUri) ||
				StringUtils.equals(ref.getUri(), nameOrUri)){
				return ref;
			}
		}
		
		throw ExceptionFactory.createUnsupportedNameOrUriException(nameOrUri, list);
	}
	
	public static <R extends NameAndMeaningReference> R getReference(NameOrURI nameOrUri, Iterable<R> list) {
		
		for(R ref : list){
			if(StringUtils.equals(ref.getContent(), nameOrUri.getName()) ||
				StringUtils.equals(ref.getUri(), nameOrUri.getUri())){
				return ref;
			}
		}
		
		throw ExceptionFactory.createUnsupportedNameOrUriException(nameOrUri, list);
	}
	
	public static PredicateReference getPredicateReference(String nameOrUri, Iterable<? extends PredicateReference> list) {
		
		//TODO: does not take into account namespaces
		for(PredicateReference ref : list){
			if(StringUtils.equals(ref.getName(), nameOrUri) ||
					StringUtils.equals(ref.getUri(), nameOrUri)){
				return ref;
			}
		}
		
		throw ExceptionFactory.createUnsupportedPredicateReference(nameOrUri);
	}
}
