package edu.mayo.cts2.framework.model.service.core;

import edu.mayo.cts2.framework.model.core.ScopedEntityName;

public abstract class AbstractEntityNameToString {

	public String toString(){
		String returnString;
		if(this.getEntityName() != null){
			returnString = 
					"Namespace: " + this.getEntityName().getNamespace() +
					"Name: " + this.getEntityName().getName();
		} else {
			returnString = "URI: " + this.getUri();
		}
		
		return returnString;
	}
	
	public abstract ScopedEntityName getEntityName();
	
	public abstract String getUri();
	
}
