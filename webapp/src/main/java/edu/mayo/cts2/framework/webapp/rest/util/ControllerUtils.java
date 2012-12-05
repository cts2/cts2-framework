/*
 * Copyright: (c) 2004-2012 Mayo Foundation for Medical Education and 
 * Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
 * triple-shield Mayo logo are trademarks and service marks of MFMER.
 *
 * Except as contained in the copyright notice above, or as used to identify 
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.mayo.cts2.framework.webapp.rest.util;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import edu.mayo.cts2.framework.core.util.EncodingUtils;
import edu.mayo.cts2.framework.model.core.NameAndMeaningReference;
import edu.mayo.cts2.framework.model.core.PredicateReference;
import edu.mayo.cts2.framework.model.core.PropertyReference;
import edu.mayo.cts2.framework.model.exception.ExceptionFactory;
import edu.mayo.cts2.framework.model.service.core.EntityNameOrURI;
import edu.mayo.cts2.framework.model.service.core.NameOrURI;
import edu.mayo.cts2.framework.model.util.ModelUtils;

/**
 * The Class ControllerUtils.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class ControllerUtils {

	/**
	 * Instantiates a new controller utils.
	 */
	private ControllerUtils(){
		super();
	}
	
	/**
	 * Ids to name or uri set.
	 *
	 * @param ids the ids
	 * @return the sets the
	 */
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
	
	/**
	 * Ids to entity name or uri set.
	 *
	 * @param ids the ids
	 * @return the sets the
	 */
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
	
	/**
	 * Gets the reference.
	 *
	 * @param <R> the generic type
	 * @param nameOrUri the name or uri
	 * @param list the list
	 * @return the reference
	 */
	public static <R extends NameAndMeaningReference> R getReference(String nameOrUri, Iterable<R> list) {
		
		for(R ref : list){
			if(StringUtils.equals(ref.getContent(), nameOrUri) ||
				StringUtils.equals(ref.getUri(), nameOrUri)){
				return ref;
			}
		}
		
		throw ExceptionFactory.createUnsupportedNameOrUriException(nameOrUri, list);
	}
	
	/**
	 * Gets the property reference.
	 *
	 * @param nameOrUri the name or uri
	 * @param list the list
	 * @return the property reference
	 */
	public static PropertyReference getPropertyReference(String nameOrUri, Iterable<? extends PropertyReference> list) {
	
		for(PropertyReference ref : list){
			if(StringUtils.equals(ref.getReferenceTarget().getName(), nameOrUri) ||
					StringUtils.equals(ref.getReferenceTarget().getUri(), nameOrUri)){
					return ref;
				}
		}
		
		throw ExceptionFactory.createUnsupportedPredicateReference(nameOrUri);
	}
	
	/**
	 * Gets the reference.
	 *
	 * @param <R> the generic type
	 * @param nameOrUri the name or uri
	 * @param list the list
	 * @return the reference
	 */
	public static <R extends NameAndMeaningReference> R getReference(NameOrURI nameOrUri, Iterable<R> list) {
		
		for(R ref : list){
			if(StringUtils.equals(ref.getContent(), nameOrUri.getName()) ||
				StringUtils.equals(ref.getUri(), nameOrUri.getUri())){
				return ref;
			}
		}
		
		throw ExceptionFactory.createUnsupportedNameOrUriException(nameOrUri, list);
	}
	
	/**
	 * Gets the predicate reference.
	 *
	 * @param nameOrUri the name or uri
	 * @param list the list
	 * @return the predicate reference
	 */
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
