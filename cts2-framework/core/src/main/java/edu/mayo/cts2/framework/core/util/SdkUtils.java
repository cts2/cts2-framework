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

import edu.mayo.cts2.framework.model.core.ScopedEntityName;

/**
 * The Class SdkUtils.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class SdkUtils {
	
	private static final String ESCAPE_CHAR = "%3A";
	private static final String SCOPED_ENTITY_NAME_SEPERATOR = ":";

	/**
	 * Encode scoped entity name.
	 *
	 * @param name the name
	 * @return the string
	 */
	public static String encodeScopedEntityName(ScopedEntityName name){
		return encodeColon(name.getName()) + SCOPED_ENTITY_NAME_SEPERATOR + encodeColon(name.getNamespace());
	}
	
	/**
	 * Encode colon.
	 *
	 * @param text the text
	 * @return the string
	 */
	private static String encodeColon(String text){
		return text.replaceAll(SCOPED_ENTITY_NAME_SEPERATOR, ESCAPE_CHAR);		
	}
	
	/**
	 * Decode entity name.
	 *
	 * @param text the text
	 * @return the scoped entity name
	 */
	public static ScopedEntityName decodeEntityName(String text){
		ScopedEntityName scopedName = new ScopedEntityName();
		String[] name = text.split(":");
		
		scopedName.setNamespace(name[0].replaceAll(ESCAPE_CHAR, SCOPED_ENTITY_NAME_SEPERATOR));
		scopedName.setName(name[1].replaceAll(ESCAPE_CHAR, SCOPED_ENTITY_NAME_SEPERATOR));
		
		return scopedName;
	}
}
