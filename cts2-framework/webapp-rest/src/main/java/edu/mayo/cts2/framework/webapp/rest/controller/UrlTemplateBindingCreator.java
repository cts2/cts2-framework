package edu.mayo.cts2.framework.webapp.rest.controller;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class UrlTemplateBindingCreator {
	
	protected <R> String bindResourceToUrlTemplate(
			String urlTemplate,
			final String... params){
		
		return this.dobindResourceToUrlTemplate(
				new Binder(){
					
					private int i=0;

					@Override
					public String doBind(String attribute) {
						return params[i++];
					}
			
		}, urlTemplate);
	}

	protected <R> String bindResourceToUrlTemplate(
			final UrlTemplateBinder<R> binder, 
			final R resource,  
			String urlTemplate){
		
		return this.dobindResourceToUrlTemplate(
				new Binder(){

					@Override
					public String doBind(String attribute) {
						return binder.getValueForPathAttribute(attribute, resource);
					}
			
		}, urlTemplate);
	}
	
	private interface Binder {
		
		public String doBind(String attribute);
		
	}
	
	protected Set<String> getUrlTemplateVariables(String urlTemplate){
		Set<String> pathParamNames = new HashSet<String>();

		char[] chars = urlTemplate.toCharArray();
		
		for(int i=0;i<chars.length;i++){
			char c = chars[i];
			
			if(c == '{'){
				 StringBuilder sb = new StringBuilder();
				 
				 while(chars[++i] != '}'){
					 sb.append(chars[i]);
				 }
				 
				 pathParamNames.add(sb.toString());	 
			}
		}
		
		return pathParamNames;
	}
	
	private String dobindResourceToUrlTemplate(
			Binder binder,
			String urlTemplate){
		Set<String> variables = this.getUrlTemplateVariables(urlTemplate);
		
		String[] matchArray = new String[variables.size()];
		String[] valuesArray = new String[variables.size()];
		
		{//scope limit
			int i=0;
			for(Iterator<String> itr = variables.iterator(); itr.hasNext(); i++){
				String variable = itr.next();
				
				String value = binder.doBind(variable);
				
				valuesArray[i] = value;			
				matchArray[i] = '{' + variable + '}';
			}
		}//end scope limit
		
		return StringUtils.replaceEach(urlTemplate, matchArray, valuesArray);
	}
}
