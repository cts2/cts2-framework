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
package edu.mayo.cts2.framework.core.xml;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.xml.transform.Result;
import javax.xml.transform.Source;

import org.apache.commons.lang.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.XmlMappingException;
import org.springframework.oxm.castor.CastorMarshaller;
import org.springframework.util.ClassUtils;

import com.google.common.collect.Iterables;

/**
 * The Class DelegatingMarshaller.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class DelegatingMarshaller implements Marshaller, Unmarshaller {

	public static final String NS_PROP = "org.exolab.castor.builder.nspackages";
	public static final String CASTORBUILDER_PROPS = "castorbuilder.properties";
	public static final String NAMESPACE_LOCATION_PROPS = "namespaceLocations.properties";
	public static final String NAMESPACE_MAPPINGS_PROPS = "namespaceMappings.properties";
	
	private Resource namespaceMappings = new ClassPathResource(NAMESPACE_MAPPINGS_PROPS);
	private Resource namespaceLocationMappings = new ClassPathResource(NAMESPACE_LOCATION_PROPS);

	private Map<String, CastorMarshaller> packageToMarshallerMap = new HashMap<String, CastorMarshaller>();
	
	private Map<String, String> namespaceMap;
	private Map<String, String> namespaceLocationMap;

	private CastorMarshaller defaultMarshaller;
	
	/**
	 * Instantiates a new delgating marshaller.
	 *
	 * @throws Exception the exception
	 */
	public DelegatingMarshaller() throws Exception {
		this.populateNamespaceMaps(); 
		
		Properties castorProps = new Properties();
		ClassPathResource res = new ClassPathResource(CASTORBUILDER_PROPS);
		castorProps.load(res.getInputStream());

		String nsMappings = (String) castorProps.get(NS_PROP);

		String[] nsAndPackage = StringUtils.split(nsMappings, ",");

		List<String> allPackages = new ArrayList<String>();
		
		Map<String,String> namespacePackageMapping = new HashMap<String,String>();
		
		for (String entry : nsAndPackage) {
			String ns = StringUtils.substringBefore(entry, "=");
			String pkg = StringUtils.substringAfter(entry, "=");
	
			packageToMarshallerMap.put(pkg, createNewMarshaller(ns));
			allPackages.add(pkg);
			
			namespacePackageMapping.put(ns, pkg);
		}
		
		this.defaultMarshaller = new PatchedCastorMarshaller();
		this.defaultMarshaller.setNamespaceMappings(this.namespaceMap);
		this.defaultMarshaller.setTargetPackages(Iterables.toArray(allPackages, String.class));
		this.defaultMarshaller.setNamespaceToPackageMapping(namespacePackageMapping);
		this.defaultMarshaller.setValidating(true);
	
		this.defaultMarshaller.afterPropertiesSet();
	}

	/**
	 * Creates the new marshaller.
	 *
	 * @param namespace the namespace
	 * @return the castor marshaller
	 */
	protected CastorMarshaller createNewMarshaller(String namespace){

		CastorMarshaller marshaller =
			new NamespaceAdjustingCastorMarshaller(namespace, this.namespaceMap);
		
		String location = this.namespaceLocationMap.get(namespace);
		
		marshaller.setSchemaLocation(namespace + " " + location);
		try {
			marshaller.afterPropertiesSet();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 

		try {
			marshaller.afterPropertiesSet();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		return marshaller;
	}
	
	/**
	 * Populate namespace maps.
	 */
	private void populateNamespaceMaps() {
		this.namespaceMap = this.createMapFromProperties(this.namespaceMappings);
		this.namespaceLocationMap = this.createMapFromProperties(this.namespaceLocationMappings);
	}
	
	/**
	 * Creates the map from properties.
	 *
	 * @param resource the resource
	 * @return the map
	 */
	private Map<String, String> createMapFromProperties(Resource resource){
		Properties props = new Properties();
		try {
			props.load(resource.getInputStream());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		Map<String,String> returnMap = new HashMap<String,String>();
		
		for(Entry<Object, Object> entry : props.entrySet()){
			returnMap.put((String)entry.getKey(), (String)entry.getValue());
		}
		
		return returnMap;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.oxm.Unmarshaller#unmarshal(javax.xml.transform.Source
	 * )
	 */
	public Object unmarshal(Source source) throws IOException,
			XmlMappingException {
		return this.defaultMarshaller.unmarshal(source);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.oxm.Marshaller#supports(java.lang.Class)
	 */
	public boolean supports(Class<?> clazz) {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.oxm.Marshaller#marshal(java.lang.Object,
	 * javax.xml.transform.Result)
	 */
	public void marshal(Object graph, Result result) throws IOException,
			XmlMappingException {
		if(graph != null){
			this.getMarshaller(graph.getClass()).marshal(graph, result);
		}
	}

	/**
	 * Gets the marshaller.
	 *
	 * @param clazz the clazz
	 * @return the marshaller
	 */
	private Marshaller getMarshaller(Class<?> clazz) {
		String packageName = ClassUtils.getPackageName(clazz);

		Marshaller marshaller = this.packageToMarshallerMap.get(packageName);
		
		if(marshaller == null){
			return this.defaultMarshaller;
		} else {
			return marshaller;
		}
	}
}
