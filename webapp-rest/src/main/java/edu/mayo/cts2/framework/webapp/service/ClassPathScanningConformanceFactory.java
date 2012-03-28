package edu.mayo.cts2.framework.webapp.service;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;

import org.reflections.Reflections;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.service.core.ProfileElement;
import edu.mayo.cts2.framework.model.service.core.types.FunctionalProfile;
import edu.mayo.cts2.framework.model.service.core.types.StructuralProfile;
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
				FunctionalConformance functionalConformance = 
						this.findAnnotation(profile, FunctionalConformance.class);
				
				StructuralConformance structuralConformance = 
						this.findAnnotation(profile, StructuralConformance.class);
			
				StructuralProfile structuralProfile = null;
				if(structuralConformance != null){
					structuralProfile = structuralConformance.value();
				}
				
				FunctionalProfile functionalProfile = functionalConformance.value();
				
				ProfileElement element = new ProfileElement();
				element.setFunctionalProfile(functionalProfile);
				element.setStructuralProfile(structuralProfile);
				
				returnSet.add(element);
			}
		}
		
		return returnSet;
	}
	
	protected Set<Class<? extends Cts2Profile>> doScan(){
		Reflections reflections = new Reflections("edu.mayo.cts2.framework.service.profile");
		
		Set<Class<? extends Cts2Profile>> profiles = reflections.getSubTypesOf(Cts2Profile.class);

		return profiles;
	}
	
	protected <T extends Annotation> T findAnnotation(Class<?> clazz, Class<T> annotation){
		return AnnotationUtils.findAnnotation(clazz, annotation);
	}

}
