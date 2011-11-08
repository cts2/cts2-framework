package edu.mayo.cts2.framework.webapp.rest.controller;

import java.util.HashSet;
import java.util.Set;

import edu.mayo.cts2.framework.core.util.EncodingUtils;
import edu.mayo.cts2.framework.model.service.core.EntityNameOrURI;
import edu.mayo.cts2.framework.model.service.core.NameOrURI;
import edu.mayo.cts2.framework.model.util.ModelUtils;

public class ControllerUtils {

	private ControllerUtils(){
		super();
	}
	
	public static Set<NameOrURI> idsToNameOrUriSet(Iterable<String> ids){
		Set<NameOrURI> returnSet = new HashSet<NameOrURI>();
		
		for(String id : ids){
			NameOrURI nameOrUri = ModelUtils.nameOrUriFromEither(id);
			returnSet.add(nameOrUri);
		}
		return returnSet;
	}
	
	public static Set<EntityNameOrURI> idsToEntityNameOrUriSet(Iterable<String> ids){
		Set<EntityNameOrURI> returnSet = new HashSet<EntityNameOrURI>();
		
		for(String id : ids){
			EntityNameOrURI nameOrUri;
			if(ModelUtils.isValidUri(id)){
				nameOrUri = ModelUtils.entityNameOrUriFromUri(id);
			} else {
				nameOrUri = ModelUtils.entityNameOrUriFromName(EncodingUtils.decodeEntityName(id));
			}
			returnSet.add(nameOrUri);
		}
		return returnSet;
	}
}
