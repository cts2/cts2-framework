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

import edu.mayo.cts2.framework.model.core.*;
import edu.mayo.cts2.framework.model.service.core.NameOrURI;
import edu.mayo.cts2.framework.model.service.core.types.LoggingLevel;
import edu.mayo.cts2.framework.model.service.exception.*;
import edu.mayo.cts2.framework.model.util.ModelUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
		
		ex.setCts2Message(getPossibleValuesMessageFromNameAndMeaning("Name or URI", requestedNameOrUri, possibleValues));
		
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

		ex.setCts2Message(getPossibleValuesMessageFromNameAndMeaning("ModelAttribute", name.getUri(), matchAlgorithmReferences));

		return ex;
	}

	public static UnknownResourceReference createUnsupportedComponentReference(
			ComponentReference componentReference,
			Iterable<? extends ComponentReference> componentReferences) {
        UnknownResourceReference ex = new UnknownResourceReference();

        ex.setSeverity(LoggingLevel.ERROR);

        ex.setCts2Message(getPossibleValuesMessageFromComponentReference("ComponentReference", componentReference, componentReferences));

        return ex;
    }
	
	public static UnknownResourceReference createUnsupportedPropertyReference(
			String requestedNameOrUri,
			Iterable<? extends ComponentReference> propertyReferences) {
		UnknownResourceReference ex = new UnknownResourceReference();
		
		ex.setSeverity(LoggingLevel.ERROR);
		
		ex.setCts2Message(getPossibleValuesMessageFromComponentReference("PropertyReference", requestedNameOrUri, propertyReferences));
		
		return ex;
	}

	public static UnsupportedVersionTag createUnsupportedVersionTag(
			NameOrURI versionTagReference,
			Iterable<? extends VersionTagReference> tagReferences) {
		UnsupportedVersionTag ex = new UnsupportedVersionTag();
		
		ex.setSeverity(LoggingLevel.ERROR);
		
		ex.setCts2Message(
				getPossibleValuesMessageFromNameAndMeaning("VersionTagReference", 
						versionTagReference.getUri(), tagReferences));
		
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
			Exception exception, 
			String url, 
			String paramString,
			boolean showStackTrace) {
		String message;
		if(showStackTrace){
			message = ExceptionUtils.getFullStackTrace(exception);
		} else {
			message = exception.getMessage();
		}
		
		return createUnknownException(message, url, paramString);
	}
	
	public static UnspecifiedCts2Exception createUnknownException(
			String message) {
		
		UnspecifiedCts2Exception ex = new UnspecifiedCts2Exception();
		
		ex.setCts2Message(ModelUtils.createOpaqueData(message));
		
		ex.setSeverity(LoggingLevel.ERROR);
		
		return ex;
	}

	public static QueryTimeout createTimeoutException() {
		return createTimeoutException("");
	}

	public static QueryTimeout createTimeoutException(String message) {
		QueryTimeout timeout = new QueryTimeout();
		timeout.setSeverity(LoggingLevel.ERROR);
		timeout.setCts2Message(ModelUtils.createOpaqueData(message));
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
		
		StringBuilder sb = new StringBuilder(message);
		sb.append(" Request was: " + url);
		
		if(StringUtils.isNotBlank(paramString)){
			sb.append(", Parameters were: " + paramString);
		}
		
		ex.setCts2Message(ModelUtils.createOpaqueData(sb.toString()));
		
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

    private static OpaqueData getPossibleValuesMessageFromComponentReference(
            String type,
            ComponentReference requestedValue,
            Iterable<? extends ComponentReference> possibleValues){
        String nameOrUri = "Unspecified";
        if(requestedValue.getAttributeReference() != null){
            nameOrUri = requestedValue.getAttributeReference();
        }
        if(requestedValue.getPropertyReference() != null){
            nameOrUri = requestedValue.getPropertyReference().getName();
        }
        if(requestedValue.getSpecialReference() != null){
            nameOrUri = requestedValue.getSpecialReference();
        }

        return getPossibleValuesMessageFromComponentReference(type, nameOrUri, possibleValues);
    }
	
	private static OpaqueData getPossibleValuesMessageFromComponentReference(
			String type, 
			String requestedValue, 
			Iterable<? extends ComponentReference> possibleValues){
		List<String> returnList = new ArrayList<String>();
		
		for(ComponentReference ref : possibleValues) {
			returnList.add(componentRefToString(ref));
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
	
	private static String componentRefToString(ComponentReference ref){
		if(ref == null){
			return null;
		}
		
		StringBuffer sb = new StringBuffer();
		
		if(ref.getAttributeReference() != null){
			sb.append("Attribute Name: " + ref.getAttributeReference());
			
		} else if (ref.getPropertyReference() != null){
			URIAndEntityName propertyReference = ref.getPropertyReference();
			
			List<String> segments = new ArrayList<String>();

			if(propertyReference.getName() != null){
				segments.add("Property Name: " + propertyReference.getName());
			}
			
			if(propertyReference.getNamespace() != null){
				segments.add("Property Namespace: " + propertyReference.getNamespace());
			}

			if(propertyReference.getUri() != null){
				sb.append("Property URI: " + propertyReference.getUri());
			}
			
			if(propertyReference.getDesignation() != null){
				sb.append("Property Designation: " + propertyReference.getDesignation());
			}
			
			if(propertyReference.getHref() != null){
				sb.append("Property Href: " + propertyReference.getHref());
			}
			
			sb.append(StringUtils.join(segments, ", "));			
		} else if (ref.getSpecialReference() != null){
			sb.append("Special Reference Name: " + ref.getSpecialReference());
		} else {
			throw new IllegalArgumentException("ComponentReference must not be empty.");
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

}
