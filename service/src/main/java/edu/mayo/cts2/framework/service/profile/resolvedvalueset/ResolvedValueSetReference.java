package edu.mayo.cts2.framework.service.profile.resolvedvalueset;

import edu.mayo.cts2.framework.model.core.ValueSetDefinitionReference;

public class ResolvedValueSetReference {
	
	private String localID;
	
	private ValueSetDefinitionReference valueSetDefinitionReference;

	public String getLocalID() {
		return localID;
	}

	public void setLocalID(String localID) {
		this.localID = localID;
	}

	public ValueSetDefinitionReference getValueSetDefinitionReference() {
		return valueSetDefinitionReference;
	}

	public void setValueSetDefinitionReference(
			ValueSetDefinitionReference valueSetDefinitionReference) {
		this.valueSetDefinitionReference = valueSetDefinitionReference;
	}
}
