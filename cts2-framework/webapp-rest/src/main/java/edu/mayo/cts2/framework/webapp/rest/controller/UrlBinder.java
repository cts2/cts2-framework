package edu.mayo.cts2.framework.webapp.rest.controller;

import edu.mayo.cts2.framework.model.core.AbstractResourceDescription;

public interface UrlBinder<T extends AbstractResourceDescription> {

	public String getValueForPathAttribute(String attribute, T resource) throws UrlVariableNotBoundException;
	
	public class UrlVariableNotBoundException extends Exception {

		private static final long serialVersionUID = -4876125839922624795L;
		
	}
}
