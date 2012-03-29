package edu.mayo.cts2.framework.webapp.service;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;

import org.reflections.Reflections;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import edu.mayo.cts2.framework.model.service.core.ProfileElement;
import edu.mayo.cts2.framework.service.profile.Cts2Profile;
import edu.mayo.cts2.framework.service.profile.FunctionalConformance;
import edu.mayo.cts2.framework.service.profile.StructuralConformance;
import edu.mayo.cts2.framework.service.provider.ServiceProvider;
import edu.mayo.cts2.framework.service.provider.ServiceProviderFactory;

@Component
public class ClassPathScanningConformanceFactory implements ConformanceFactory {
	
	@Resource
	private ServiceProviderFactory serviceProviderFactory;
	
	@Override
	public Iterable<ProfileElement> getProfileElements(){
		Set<ProfileElement> returnSet = new HashSet<ProfileElement>();
		
		ServiceProvider provider = 
			this.serviceProviderFactory.getServiceProvider();
	
		Set<Class<? extends Cts2Profile>> profiles = this.doScan();
		
		for(Class<? extends Cts2Profile> profile : profiles){
			Cts2Profile service = provider.getService(profile);
			
			if(service != null){
				Collection<FunctionalConformance> functionalConformances =
						this.findAnnotations(profile, FunctionalConformance.class);
				
				Collection<StructuralConformance> structuralConformances = 
						this.findAnnotations(profile, StructuralConformance.class);
			
				for(FunctionalConformance functional : functionalConformances){
					for(StructuralConformance structural : structuralConformances){
						ProfileElement element = new ProfileElement();
						element.setFunctionalProfile(functional.value());
						element.setStructuralProfile(structural.value());
						
						returnSet.add(element);
					}
				}
			}
		}
		
		return returnSet;
	}
	
	protected Set<Class<? extends Cts2Profile>> doScan(){
		Reflections reflections = new Reflections("edu.mayo.cts2.framework.service.profile");
		
		Set<Class<? extends Cts2Profile>> profiles = reflections.getSubTypesOf(Cts2Profile.class);

		return profiles;
	}

	protected <A extends Annotation> Collection<A> findAnnotations(Class<?> clazz, Class<A> annotationType) {
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

}
