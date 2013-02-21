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
import java.util.*;
import java.util.Map.Entry;

import javax.xml.transform.Result;
import javax.xml.transform.Source;

import edu.mayo.cts2.framework.model.castor.MarshallSuperClass;
import org.apache.commons.lang.StringUtils;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.XmlMappingException;
import org.springframework.oxm.castor.CastorMarshaller;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import com.google.common.collect.Iterables;

import edu.mayo.cts2.framework.core.plugin.ExportedService;

/**
 * The default implementation of the Cts2Marshaller.
 *
 * @see Cts2Marshaller
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Component
@ExportedService( { Cts2Marshaller.class,Marshaller.class,Unmarshaller.class } )
public class DelegatingMarshaller implements Cts2Marshaller {

	public static final String NS_PROP = "org.exolab.castor.builder.nspackages";
    public static final String PROXY_INTERFACES_PROP = "org.exolab.castor.xml.proxyInterfaces";

    private Set<String> marshallSuperClasses = new HashSet<String>();
	
	private Map<String, CastorMarshaller> packageToMarshallerMap = new HashMap<String, CastorMarshaller>();
	
	private Map<String, String> namespaceMap;
	private Map<String, String> namespaceLocationMap;

	private CastorMarshaller defaultMarshaller;
	
	private ModelXmlPropertiesHandler modelXmlPropertiesHandler = new ModelXmlPropertiesHandler();
	
	private Properties castorBuilderProperties;
	private Properties namespaceLocationProperties;
	private Properties namespaceMappingProperties;

	public DelegatingMarshaller(){
		this(true);
	}
	
	/**
	 * Instantiates a new delgating marshaller.
	 *
	 * @throws Exception the exception
	 */
	public DelegatingMarshaller(boolean validate){
		super();
		this.castorBuilderProperties = this.modelXmlPropertiesHandler.getCastorBuilderProperties();
		this.namespaceLocationProperties = this.modelXmlPropertiesHandler.getNamespaceLocationProperties();
		this.namespaceMappingProperties = this.modelXmlPropertiesHandler.getNamespaceMappingProperties();
		
		this.populateNamespaceMaps(
				this.namespaceMappingProperties,
				this.namespaceLocationProperties
		);

        String proxyInterfaces = this.createMapFromProperties(
                this.modelXmlPropertiesHandler.getCastorProperties()).get(PROXY_INTERFACES_PROP);

        this.marshallSuperClasses = new HashSet<String>(Arrays.asList(StringUtils.split(proxyInterfaces, ',')));
		
		String nsMappings = (String) this.castorBuilderProperties.get(NS_PROP);

		String[] nsAndPackage = StringUtils.split(nsMappings, ",");

		List<String> allPackages = new ArrayList<String>();
		
		Map<String,String> namespacePackageMapping = new HashMap<String,String>();
		
		for (String entry : nsAndPackage) {
			String ns = StringUtils.substringBefore(entry, "=");
			String pkg = StringUtils.substringAfter(entry, "=");
	
			packageToMarshallerMap.put(pkg, createNewMarshaller(ns, validate));
			allPackages.add(pkg);
			
			namespacePackageMapping.put(ns, pkg);
		}
		
		this.defaultMarshaller = new PatchedCastorMarshaller();
		this.defaultMarshaller.setNamespaceMappings(this.namespaceMap);
		this.defaultMarshaller.setTargetPackages(Iterables.toArray(allPackages, String.class));
		this.defaultMarshaller.setNamespaceToPackageMapping(namespacePackageMapping);
		this.defaultMarshaller.setValidating(validate);
	
		try {
			this.defaultMarshaller.afterPropertiesSet();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
	}

	/**
	 * Creates the new marshaller.
	 *
	 * @param namespace the namespace
	 * @return the castor marshaller
	 */
	protected CastorMarshaller createNewMarshaller(String namespace, boolean validate){

		CastorMarshaller marshaller =
			new NamespaceAdjustingCastorMarshaller(namespace, this.namespaceMap);
		
		String location = this.namespaceLocationMap.get(namespace);
		
		if(StringUtils.isNotBlank(location)){
			marshaller.setSchemaLocation(namespace + " " + location);
		}

		marshaller.setValidating(validate);
		
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
	private void populateNamespaceMaps(Properties namespaceMappings, Properties namespaceLocationMappings) {
		this.namespaceMap = this.createMapFromProperties(namespaceMappings);
		this.namespaceLocationMap = this.createMapFromProperties(namespaceLocationMappings);
	}
	
	/**
	 * Creates the map from properties.
	 *
	 * @param resource the resource
	 * @return the map
	 */
	private Map<String, String> createMapFromProperties(Properties props){	
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

        final Class[] interfaces = clazz.getInterfaces();

        if(interfaces != null){
            for(Class<?> interfaze : interfaces){
                if(this.marshallSuperClasses.contains(interfaze.getName())){
                    clazz = clazz.getSuperclass();
                    break;
                }
            }
        }

        String packageName = ClassUtils.getPackageName(clazz);

		Marshaller marshaller = this.packageToMarshallerMap.get(packageName);
		
		if(marshaller == null){
			return this.defaultMarshaller;
		} else {
			return marshaller;
		}
	}

	@Override
	public Properties getCastorBuilderProperties() {
		return this.castorBuilderProperties;
	}

	@Override
	public Properties getNamespaceLocationProperties() {
		return this.namespaceLocationProperties;
	}

	@Override
	public Properties getNamespaceMappingProperties() {
		return this.namespaceMappingProperties;
	}
}
