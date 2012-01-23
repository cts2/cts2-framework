package edu.mayo.cts2.framework.model.extension;

import edu.mayo.cts2.framework.model.valuesetdefinition.ValueSetDefinition;

public class LocalIdValueSetDefinition extends ChangeableLocalIdResource<ValueSetDefinition> {

	public LocalIdValueSetDefinition(ValueSetDefinition resource) {
		super(resource);
	}

	public LocalIdValueSetDefinition(String localID,
			ValueSetDefinition resource) {
		super(localID, resource);
	}
	
}
