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
import edu.mayo.cts2.framework.model.core.URIAndEntityName;
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
	public static Cts2RestException createUnsupportedMatchAlgorithm(
			String requestedAlgorithm, 
			Iterable<? extends NameAndMeaningReference> possibleValues) {
		UnsupportedMatchAlgorithm ex = new UnsupportedMatchAlgorithm();
		ex.setSeverity(LoggingLevel.ERROR);
		
		ex.setMessage(getPossibleValuesMessageFromNameAndMeaning("MatchAlgorithm", requestedAlgorithm, possibleValues));
		
		return new Cts2RestException(ex);
	}
	
	public static <T extends NameAndMeaningReference> Cts2RestException createUnsupportedNameOrUriException(
			String requestedNameOrUri, 
			Iterable<T> possibleValues) {
		UnsupportedNameOrURI ex = new UnsupportedNameOrURI();
		ex.setSeverity(LoggingLevel.ERROR);
		
		ex.setMessage(getPossibleValuesMessageFromNameAndMeaning("MatchAlgorithm", requestedNameOrUri, possibleValues));
		
		return new Cts2RestException(ex);
	}

	/**
	 * Creates a new Exception object.
	 *
	 * @param name the name
	 * @param matchAlgorithmReferences the match algorithm references
	 * @return the cts2 rest exception
	 */
	public static Cts2RestException createUnsupportedModelAttribute(
			URIAndEntityName name,
			Iterable<? extends ModelAttributeReference> matchAlgorithmReferences) {
		UnsupportedModelAttribute ex = new UnsupportedModelAttribute();
		
		ex.setSeverity(LoggingLevel.ERROR);
		
		ex.setMessage(getPossibleValuesMessageFromNameAndMeaning("ModelAttribute", name.getUri(), matchAlgorithmReferences));
		
		return new Cts2RestException(ex);
	}
	
	/**
	 * Creates a new Exception object.
	 *
	 * @param name the name
	 * @param matchAlgorithmReferences the match algorithm references
	 * @return the cts2 rest exception
	 */
	public static Cts2RestException createUnsupportedModelAttribute(
			String name,
			Iterable<? extends ModelAttributeReference> matchAlgorithmReferences) {
		UnsupportedModelAttribute ex = new UnsupportedModelAttribute();
		
		ex.setSeverity(LoggingLevel.ERROR);
		
		ex.setMessage(getPossibleValuesMessageFromNameAndMeaning("ModelAttribute", name, matchAlgorithmReferences));
		
		return new Cts2RestException(ex);
	}
	
	/**
	 * Creates a new Exception object.
	 *
	 * @param name the name
	 * @param exceptionClazz the exception clazz
	 * @return the cts2 rest exception
	 */
	public static Cts2RestException createUnknownResourceException(
			String name,
			Class<? extends UnknownResourceReference> exceptionClazz) {
		UnknownResourceReference ex;
		try {
			ex = exceptionClazz.newInstance();
		} catch (Exception e) {
			throw new UnspecifiedCts2RuntimeException(e);
		}
		
		ex.setSeverity(LoggingLevel.ERROR);
		ex.setExceptionType(ExceptionType.INVALID_SERVICE_INPUT);
		ex.setMessage(ModelUtils.createOpaqueData("Resource with Name: " + name + " not found."));
		
		return new Cts2RestException(ex);
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
	
	public static Cts2RestException createUnknownException(
			String message) {
		
		UnspecifiedCts2Exception ex = new UnspecifiedCts2Exception();
		
		ex.setMessage(ModelUtils.createOpaqueData(message));
		
		ex.setSeverity(LoggingLevel.ERROR);
		
		return new Cts2RestException(ex);
	}
	
	public static Cts2RestException createTimeoutException() {
		QueryTimeout timeout = new QueryTimeout();
		timeout.setExceptionType(ExceptionType.INVALID_QUERY_CONTROL);
		timeout.setSeverity(LoggingLevel.ERROR);
		
		Cts2RestException ex = new Cts2RestException(timeout);
	
		return ex;
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
		
		message = message + 
		" Request was: " + url + 
			", Parameters were: " + paramString;
		
		ex.setMessage(ModelUtils.createOpaqueData(message));
		
		return ex;
	}
	
	/**
	 * Creates a new Exception object.
	 *
	 * @return the unspecified cts2 rest exception
	 */
	public static UnspecifiedCts2RuntimeException createPageOutOfBoundsException() {
		return new UnspecifiedCts2RuntimeException("Page Out of Bounds.", 416);
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
	public static Cts2RestException createUnsupportedPredicateReference(
			String name) {	
		UnsupportedPredicate ex = new UnsupportedPredicate();
		
		ex.setSeverity(LoggingLevel.ERROR);

		return new Cts2RestException(ex);
	}
}
