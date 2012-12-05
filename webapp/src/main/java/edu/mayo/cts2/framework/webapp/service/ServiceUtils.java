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
package edu.mayo.cts2.framework.webapp.service;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.util.Assert;

import edu.mayo.cts2.framework.core.url.UrlConstructor;
import edu.mayo.cts2.framework.model.service.core.FunctionalProfileEntry;
import edu.mayo.cts2.framework.model.service.core.ProfileElement;
import edu.mayo.cts2.framework.service.profile.Cts2Profile;
import edu.mayo.cts2.framework.service.profile.FunctionalConformance;
import edu.mayo.cts2.framework.service.profile.StructuralConformance;

/**
 * The Class ServiceUtils.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public final class ServiceUtils {
	
	/**
	 * Instantiates a new service utils.
	 */
	private ServiceUtils(){
		super();
	}
	
	/**
	 * Find annotations.
	 *
	 * @param <A> the generic type
	 * @param clazz the clazz
	 * @param annotationType the annotation type
	 * @return the collection
	 */
	public static <A extends Annotation> Collection<A> findAnnotations(Class<?> clazz, Class<A> annotationType) {
		Collection<A> returnCollection = new HashSet<A>();
		
		Assert.notNull(clazz, "Class must not be null");
		A annotation = clazz.getAnnotation(annotationType);
		if (annotation != null) {
			returnCollection.add(annotation);
		}
		
		for (Class<?> ifc : clazz.getInterfaces()) {
			Collection<A> annotations = findAnnotations(ifc, annotationType);
			if (annotations != null) {
				returnCollection.addAll(annotations);
			}
		}
		if (!Annotation.class.isAssignableFrom(clazz)) {
			for (Annotation ann : clazz.getAnnotations()) {
				Collection<A> annotations = findAnnotations(ann.annotationType(), annotationType);
				if (annotations != null) {
					returnCollection.addAll(annotations);
				}
			}
		}
		Class<?> superClass = clazz.getSuperclass();
		if (superClass == null || superClass == Object.class) {
			return returnCollection;
		}
		
		returnCollection.addAll(findAnnotations(superClass, annotationType));
		
		return returnCollection;
	}
	
	public static Set<ProfileElement> buildProfileElements(
			Class<? extends Cts2Profile> profile,
			UrlConstructor urlConstructor){
		Set<ProfileElement> returnSet = new HashSet<ProfileElement>();
		
		Collection<StructuralConformance> structuralConformances = 
				findAnnotations(profile, StructuralConformance.class);
		
		Collection<FunctionalConformance> functionalConformances =
				findAnnotations(profile, FunctionalConformance.class);

		for(StructuralConformance structural : structuralConformances){
			for(FunctionalConformance functional : functionalConformances){
				ProfileElement element = new ProfileElement();
				
				FunctionalProfileEntry entry = new FunctionalProfileEntry();
				entry.setContent(functional.value().name());
				entry.setHref(
					urlConstructor.
						createServiceUrl(structural.value(), functional.value()));

				element.addFunctionalProfile(entry);
				element.setStructuralProfile(structural.value());
				
				returnSet.add(element);
			}
		}
		
		return returnSet;
	}

}
