package edu.mayo.cts2.framework.webapp.soap.directoryuri;

import java.util.HashSet;
import java.util.Set;

import edu.mayo.cts2.framework.model.service.core.FilterComponent;

public class DefaultSoapDirectoryUri<T> implements SoapDirectoryUri<T>{

	private static final long serialVersionUID = -7494327362373420959L;
	
	private Set<FilterComponent> filterComponents = new HashSet<FilterComponent>();
	private T restrictions;
	private SetOperation<T> setOperation;
	
	protected DefaultSoapDirectoryUri(){
		super();
	}

	public Set<FilterComponent> getFilterComponents() {
		return filterComponents;
	}

	public void setFilterComponents(Set<FilterComponent> filterComponents) {
		this.filterComponents = filterComponents;
	}

	public T getRestrictions() {
		return restrictions;
	}

	public void setRestrictions(T restrictions) {
		this.restrictions = restrictions;
	}

	public SetOperation<T> getSetOperation() {
		return setOperation;
	}

	public void setSetOperation(SetOperation<T> setOperation) {
		this.setOperation = setOperation;
	}

}
