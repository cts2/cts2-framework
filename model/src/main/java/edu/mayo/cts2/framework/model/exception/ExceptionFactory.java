/*
 * Copyright: (c) 2004-2011 Mayo Foundation for Medical Education and 
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
package edu.mayo.cts2.framework.model.exception;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.exception.ExceptionUtils;

import edu.mayo.cts2.framework.model.core.ModelAttributeReference;
import edu.mayo.cts2.framework.model.core.NameAndMeaningReference;
import edu.mayo.cts2.framework.model.core.OpaqueData;
import edu.mayo.cts2.framework.model.core.PropertyReference;
import edu.mayo.cts2.framework.model.core.URIAndEntityName;
import edu.mayo.cts2.framework.model.service.core.NameOrURI;
import edu.mayo.cts2.framework.model.service.core.types.LoggingLevel;
import edu.mayo.cts2.framework.model.service.exception.QueryTimeout;
import edu.mayo.cts2.framework.model.service.exception.UnknownResourceReference;
import edu.mayo.cts2.framework.model.service.exception.UnsupportedMatchAlgorithm;
import edu.mayo.cts2.framework.model.service.exception.UnsupportedModelAttribute;
import edu.mayo.cts2.framework.model.service.exception.UnsupportedNameOrURI;
import edu.mayo.cts2.framework.model.service.exception.UnsupportedPredicate;
import edu.mayo.cts2.framework.model.service.exception.types.ExceptionType;
import edu.mayo.cts2.framework.model.util.ModelUtils;

/**
 * A factory for creating Exception objects.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class ExceptionFactory {

	/**
	 * Creates a new Exception object.
	 *
	 * @param requestedAlgorithm the requested algorithm
	 * @param possibleValues the possible values
	 * @return the cts2 rest exception
	 */
	public static UnsupportedMatchAlgorithm createUnsupportedMatchAlgorithm(
			String requestedAlgorithm, 
			Iterable<? extends NameAndMeaningReference> possibleValues) {
		UnsupportedMatchAlgorithm ex = new UnsupportedMatchAlgorithm();
		ex.setSeverity(LoggingLevel.ERROR);
		ex.setExceptionType(ExceptionType.INVALID_QUERY_CONTROL);
		
		ex.setCts2Message(getPossibleValuesMessageFromNameAndMeaning("MatchAlgorithm", requestedAlgorithm, possibleValues));
		
		return ex;
	}
	
	public static <T extends NameAndMeaningReference> UnsupportedNameOrURI createUnsupportedNameOrUriException(
			NameOrURI requestedNameOrUri, 
			Iterable<T> possibleValues) {
		String nameOrUri = requestedNameOrUri.getName() != null ? requestedNameOrUri.getName() : requestedNameOrUri.getUri();
		
		return createUnsupportedNameOrUriException(nameOrUri, possibleValues);
	}
	
	public static <T extends NameAndMeaningReference> UnsupportedNameOrURI createUnsupportedNameOrUriException(
			String requestedNameOrUri, 
			Iterable<T> possibleValues) {
		UnsupportedNameOrURI ex = new UnsupportedNameOrURI();
		ex.setSeverity(LoggingLevel.ERROR);
		ex.setExceptionType(ExceptionType.INVALID_READ_CONTEXT);
		
		ex.setCts2Message(getPossibleValuesMessageFromNameAndMeaning("MatchAlgorithm", requestedNameOrUri, possibleValues));
		
		return ex;
	}

	/**
	 * Creates a new Exception object.
	 *
	 * @param name the name
	 * @param matchAlgorithmReferences the match algorithm references
	 * @return the cts2 rest exception
	 */
	public static UnsupportedModelAttribute createUnsupportedModelAttribute(
			URIAndEntityName name,
			Iterable<? extends ModelAttributeReference> matchAlgorithmReferences) {
		UnsupportedModelAttribute ex = new UnsupportedModelAttribute();
		
		ex.setSeverity(LoggingLevel.ERROR);
		ex.setExceptionType(ExceptionType.INVALID_QUERY_CONTROL);
		
		ex.setCts2Message(getPossibleValuesMessageFromNameAndMeaning("ModelAttribute", name.getUri(), matchAlgorithmReferences));
		
		return ex;
	}
	
	public static UnknownResourceReference createUnsupportedPropertyReference(
			PropertyReference propertyReference,
			Iterable<? extends PropertyReference> propertyReferences) {
		UnknownResourceReference ex = new UnknownResourceReference();
		
		ex.setSeverity(LoggingLevel.ERROR);
		ex.setExceptionType(ExceptionType.INVALID_QUERY_CONTROL);
		
		ex.setCts2Message(getPossibleValuesMessageFromPropertyReference("PropertyReference", propertyReference.getReferenceTarget().getUri(), propertyReferences));
		
		return ex;
	}
	
	/**
	 * Creates a new Exception object.
	 *
	 * @param name the name
	 * @param matchAlgorithmReferences the match algorithm references
	 * @return the cts2 rest exception
	 */
	public static UnsupportedModelAttribute createUnsupportedModelAttribute(
			String name,
			Iterable<? extends ModelAttributeReference> matchAlgorithmReferences) {
		UnsupportedModelAttribute ex = new UnsupportedModelAttribute();
		
		ex.setSeverity(LoggingLevel.ERROR);
		ex.setExceptionType(ExceptionType.INVALID_QUERY_CONTROL);
		
		ex.setCts2Message(getPossibleValuesMessageFromNameAndMeaning("ModelAttribute", name, matchAlgorithmReferences));
		
		return ex;
	}
	
	/**
	 * Creates a new Exception object.
	 *
	 * @param name the name
	 * @param exceptionClazz the exception clazz
	 * @return the cts2 rest exception
	 */
	
	public static UnknownResourceReference createUnknownResourceException(
			String name,
			Class<? extends UnknownResourceReference> exceptionClazz) {
		UnknownResourceReference ex;
		try {
			ex = exceptionClazz.newInstance();
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
		
		ex.setSeverity(LoggingLevel.ERROR);
		ex.setExceptionType(ExceptionType.INVALID_SERVICE_INPUT);
		ex.setCts2Message(ModelUtils.createOpaqueData("Resource with Identifier: " + name + " not found."));
		
		return ex;
	}
	
	/**
	 * Creates a new Exception object.
	 *
	 * @param exception the exception
	 * @param url the url
	 * @param paramString the param string
	 * @return the unspecified cts2 exception
	 */
	public static UnspecifiedCts2Exception createUnknownException(
			Exception exception, String url, String paramString) {
		String message = ExceptionUtils.getFullStackTrace(exception);
		
		return createUnknownException(message, url, paramString);
	}
	
	public static UnspecifiedCts2Exception createUnknownException(
			String message) {
		
		UnspecifiedCts2Exception ex = new UnspecifiedCts2Exception();
		
		ex.setCts2Message(ModelUtils.createOpaqueData(message));
		
		ex.setSeverity(LoggingLevel.ERROR);
		ex.setExceptionType(ExceptionType.INVALID_SERVICE_INPUT);
		
		return ex;
	}
	
	public static QueryTimeout createTimeoutException() {
		QueryTimeout timeout = new QueryTimeout();
		timeout.setExceptionType(ExceptionType.INVALID_QUERY_CONTROL);
		timeout.setSeverity(LoggingLevel.ERROR);
		
		return timeout;
	}
	
	/**
	 * Creates a new Exception object.
	 *
	 * @param message the message
	 * @param url the url
	 * @param paramString the param string
	 * @return the unspecified cts2 exception
	 */
	public static UnspecifiedCts2Exception createUnknownException(
			String message, String url, String paramString) {
		UnspecifiedCts2Exception ex = new UnspecifiedCts2Exception();
		
		ex.setSeverity(LoggingLevel.ERROR);
		ex.setExceptionType(ExceptionType.INVALID_SERVICE_INPUT);
		
		message = message + 
		" Request was: " + url + 
			", Parameters were: " + paramString;
		
		ex.setCts2Message(ModelUtils.createOpaqueData(message));
		
		return ex;
	}
	
	
	/**
	 * Creates a new Exception object.
	 *
	 * @return the unspecified cts2 rest exception
	 */
	public static UnspecifiedCts2Exception createPageOutOfBoundsException() {
		return new UnspecifiedCts2Exception("Page Out of Bounds.");
	}
	
	/**
	 * Gets the possible values message from name and meaning.
	 *
	 * @param type the type
	 * @param requestedValue the requested value
	 * @param possibleValues the possible values
	 * @return the possible values message from name and meaning
	 */
	private static OpaqueData getPossibleValuesMessageFromNameAndMeaning(
			String type, 
			String requestedValue, 
			Iterable<? extends NameAndMeaningReference> possibleValues){
		List<String> returnList = new ArrayList<String>();
		
		for(NameAndMeaningReference ref : possibleValues) {
			returnList.add(nameAndMeaningRefToString(ref));
		}
	
		return getPossibleValuesMessage(type, requestedValue, returnList);
	}
	
	private static OpaqueData getPossibleValuesMessageFromPropertyReference(
			String type, 
			String requestedValue, 
			Iterable<? extends PropertyReference> possibleValues){
		List<String> returnList = new ArrayList<String>();
		
		for(PropertyReference ref : possibleValues) {
			returnList.add(propertyRefToString(ref));
		}
	
		return getPossibleValuesMessage(type, requestedValue, returnList);
	}
	
	/**
	 * Name and meaning ref to string.
	 *
	 * @param ref the ref
	 * @return the string
	 */
	private static String nameAndMeaningRefToString(NameAndMeaningReference ref){
		if(ref == null){
			return null;
		}
		
		StringBuffer sb = new StringBuffer();
		sb.append("Name: " + ref.getContent());
		
		if(ref.getUri() != null && !ref.getUri().isEmpty()){
			sb.append(", ");
			sb.append("URI: " + ref.getUri());
		}
		
		if(ref.getHref() != null && !ref.getHref().isEmpty()){
			sb.append(", ");
			sb.append("Href: " + ref.getHref());
		}
		
		return sb.toString();
	}
	
	private static String propertyRefToString(PropertyReference ref){
		if(ref == null){
			return null;
		}
		
		StringBuffer sb = new StringBuffer();
		sb.append("Name: " + ref.getReferenceTarget().getName());
		
		if(ref.getReferenceTarget().getUri() != null && !ref.getReferenceTarget().getUri().isEmpty()){
			sb.append(", ");
			sb.append("URI: " + ref.getReferenceTarget().getUri());
		}
		
		if(ref.getReferenceTarget().getHref() != null && !ref.getReferenceTarget().getHref().isEmpty()){
			sb.append(", ");
			sb.append("Href: " + ref.getReferenceTarget().getHref());
		}
		
		return sb.toString();
	}
	
	/**
	 * Gets the possible values message.
	 *
	 * @param type the type
	 * @param requestedValue the requested value
	 * @param possibleValues the possible values
	 * @return the possible values message
	 */
	private static OpaqueData getPossibleValuesMessage(
			String type, 
			String requestedValue, 
			Collection<String> possibleValues){
		StringBuffer sb = new StringBuffer();
		
		sb.append(type + ": " + requestedValue + " not found. ");
		
		if(possibleValues != null && possibleValues.size() != 0){
				
			sb.append("Available " + type + "s for this request: ");
			for(String value : possibleValues){
				sb.append("[" + value + "] ");
			}
		}
		
		return ModelUtils.createOpaqueData(sb.toString());
	}

	/**
	 * Creates a new Exception object.
	 *
	 * @param name the name
	 * @return the cts2 rest exception
	 */
	public static UnsupportedPredicate createUnsupportedPredicateReference(
			String name) {	
		UnsupportedPredicate ex = new UnsupportedPredicate();
		ex.setExceptionType(ExceptionType.INVALID_SERVICE_INPUT);
		ex.setSeverity(LoggingLevel.ERROR);

		return ex;
	}
}
