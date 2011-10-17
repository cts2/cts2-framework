package edu.mayo.cts2.framework.webapp.rest.controller;


public interface UrlTemplateBinder<R> {

	public String getValueForPathAttribute(String attribute, R resource);
	
}
