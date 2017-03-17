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
package edu.mayo.cts2.framework.core.util;

import org.springframework.util.StringUtils;

import edu.mayo.cts2.framework.model.core.ScopedEntityName;
import edu.mayo.cts2.framework.model.core.URIAndEntityName;

/**
 * The Class SdkUtils.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class EncodingUtils {

	private static final String URL_ESCAPE_CHAR = "%253A";
	private static final String ESCAPE_CHAR = "%3A";
	private static final String SCOPED_ENTITY_NAME_SEPERATOR = ":";

	/**
	 * Encode scoped entity name.
	 *
	 * @param name the name
	 * @return the string
	 */
	public static String encodeScopedEntityName(ScopedEntityName name){
		return encodeColon(name.getNamespace()) + SCOPED_ENTITY_NAME_SEPERATOR + encodeColon(name.getName());
	}
	
	public static String encodeScopedEntityName(URIAndEntityName name){
		return encodeColon(name.getNamespace()) + SCOPED_ENTITY_NAME_SEPERATOR + encodeColon(name.getName());
	}

	/**
	 * Return an encoding of the Entity name suitable for an HREF.
	 *
	 * NOTE: This is NOT to be used for 'scoped' names, or, names with a namespace.
	 *
	 * Don't pass in 'myNamespace:myName'
	 *
	 * @param name
	 * @return
	 */
	public static String encodeEntityName(String name){
		return encodeColon(name);
	}

	/**
	 * Encode colon.
	 *
	 * @param text the text
	 * @return the string
	 */
	private static String encodeColon(String text){
		return text.replaceAll(SCOPED_ENTITY_NAME_SEPERATOR, URL_ESCAPE_CHAR);
	}
	
	/**
	 * Decode entity name.
	 *
	 * @param text the text
	 * @return the scoped entity name
	 */
	public static ScopedEntityName decodeEntityName(String text){
		return decodeEntityName(text, null);
	}

	public static ScopedEntityName decodeEntityName(String text, String defaultNamespace){
		ScopedEntityName scopedName = new ScopedEntityName();
		String[] name = text.split(":");
		if(name.length == 1){
			name = new String[]{defaultNamespace,text};
		}

		scopedName.setNamespace(StringUtils.replace(name[0], ESCAPE_CHAR, SCOPED_ENTITY_NAME_SEPERATOR));
		scopedName.setName(StringUtils.replace(name[1], ESCAPE_CHAR, SCOPED_ENTITY_NAME_SEPERATOR));

		return scopedName;
	}
}
