package edu.mayo.cts2.framework.webapp.rest.controller;

import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.util.UriTemplate;

@Component
public class UrlTemplateBindingCreator {
	
	protected <R> String bindResourceToUrlTemplate(
			String urlTemplate,
			final String... params){
		
		return new UriTemplate(urlTemplate).expand((Object[])params).toString();
	}

	protected <R> String bindResourceToUrlTemplate(
			final UrlTemplateBinder<R> binder, 
			final R resource,  
			String urlTemplate){
		
		Map<String, String> pathValues = binder.getPathValues(resource);
		
		return new UriTemplate(urlTemplate).expand(pathValues).toString();
	}

}
