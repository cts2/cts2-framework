package edu.mayo.cts2.framework.core.plugin;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.atlassian.plugin.ModuleDescriptor;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExtensionPointDescriptor {
	
	String xmlPrefix();
	
	Class<? extends ModuleDescriptor<?>> descriptor() default DefaultModuleDescriptor.class;

}
