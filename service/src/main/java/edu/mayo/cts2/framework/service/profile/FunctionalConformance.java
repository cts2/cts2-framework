package edu.mayo.cts2.framework.service.profile;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import edu.mayo.cts2.framework.model.service.core.types.FunctionalProfile;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface FunctionalConformance {
	
	FunctionalProfile value();
	
}
