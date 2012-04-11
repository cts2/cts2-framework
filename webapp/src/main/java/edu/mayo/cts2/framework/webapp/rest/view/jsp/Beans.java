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
package edu.mayo.cts2.framework.webapp.rest.view.jsp;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.springframework.beans.BeanUtils;

import edu.mayo.cts2.framework.model.core.DirectoryEntry;
import edu.mayo.cts2.framework.model.core.ResourceDescriptionDirectoryEntry;
import edu.mayo.cts2.framework.model.core.ResourceVersionDescriptionDirectoryEntry;
import edu.mayo.cts2.framework.model.entity.EntityDirectoryEntry;

/**
 * JSP tag lib for various bean introspection utilities.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@SuppressWarnings( {"rawtypes", "unchecked"} )
public class Beans {
	
	private static final String HEADING_FIELD = "_heading";
	
	private static final String ENTRY_LIST_FIELD = "_entryList";
	
	private static final Comparator<Map.Entry<String, Object>> BEAN_COMPARATOR = new Comparator<Map.Entry<String, Object>>(){

		@Override
		public int compare(Map.Entry<String, Object> val1, Map.Entry<String, Object> val2) {
			if(val1.getKey().equals(HEADING_FIELD) || !val1.getKey().equals(ENTRY_LIST_FIELD)){
				return -1;
			}
			if(val2.getKey().equals(HEADING_FIELD) || !val2.getKey().equals(ENTRY_LIST_FIELD)){
				return 1;
			}
			
			return 0;
		}
		
	};

	/**
	 * Should recurse.
	 *
	 * @param bean the bean
	 * @return true, if successful
	 */
	public static boolean shouldRecurse(Object bean) {
		return !BeanUtils.isSimpleProperty(bean.getClass()) && !bean.getClass().isEnum();
	}
	
	public static String capitalize(String string) {
		return WordUtils.capitalize(string);
	}

	public static Map summary(Object bean) {
		Map returnMap = new LinkedHashMap();
		
		if(bean instanceof ResourceVersionDescriptionDirectoryEntry){
			return 
					returnSummary(
							buildSummary( ( ResourceVersionDescriptionDirectoryEntry ) bean, returnMap), bean);
		}
		
		if(bean instanceof ResourceDescriptionDirectoryEntry){
			return 
					returnSummary(
							buildSummary( ( ResourceDescriptionDirectoryEntry ) bean, returnMap), bean);
		}
		if(bean instanceof DirectoryEntry){
			return 
					returnSummary(
							buildSummary( ( DirectoryEntry ) bean, returnMap), bean);
		}
		if(bean instanceof EntityDirectoryEntry){
			return 
					returnSummary(
							buildSummary( (EntityDirectoryEntry) bean, returnMap), bean);
		}
		
		throw new IllegalStateException("Unknown type of Directory Entry: " + bean.getClass().getName());
	}
	
	private static Map returnSummary(Map summary, Object bean){
		if(summary.isEmpty()){
			summary.put("Entry", bean.getClass().getSimpleName());
		}
		
		return summary;
	}
	
	private static Map buildSummary(EntityDirectoryEntry bean, Map returnMap) {
		returnMap.put("Namespace", bean.getName().getNamespace());
		returnMap.put("Name", bean.getName().getName());
		returnMap.put("About", bean.getAbout());
	
		return returnMap;
	}
	
	private static Map buildSummary(ResourceVersionDescriptionDirectoryEntry bean, Map returnMap) {
		buildSummary( (ResourceDescriptionDirectoryEntry) bean, returnMap).put("Version", bean.getOfficialResourceVersionId());
		
		return returnMap;
	}
	
	private static Map buildSummary(ResourceDescriptionDirectoryEntry bean, Map returnMap) {
		buildSummary( (DirectoryEntry) bean, returnMap).put("About", bean.getAbout());
		
		return returnMap;
	}
	
	private static Map buildSummary(DirectoryEntry bean, Map returnMap) {
		String resourceName = bean.getResourceName();
		if(StringUtils.isNotBlank(resourceName)){
			returnMap.put("Name", bean.getResourceName());
		}
		
		return returnMap;
	}

	/**
	 * Inspect.
	 *
	 * @param bean the bean
	 * @return the list
	 */
	public static List<Map.Entry<String, Object>> inspect(Object bean) {
		Map<String, Object> props = new LinkedHashMap<String, Object>();

		Class<?> clazz = bean.getClass();
		
		while(clazz != null){
			for(Field field : clazz.getDeclaredFields()){
				field.setAccessible(true);
				String name = field.getName();
				
				Object value;
				try {
					value = field.get(bean);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
	
				if(value != null){
					props.put(name, value);
				}
			}
			clazz = clazz.getSuperclass();
		}
		
		List<Map.Entry<String, Object>> list = new ArrayList<Map.Entry<String, Object>>(props.entrySet());
		
		Collections.sort(list, BEAN_COMPARATOR);

		return list;
	}
}
