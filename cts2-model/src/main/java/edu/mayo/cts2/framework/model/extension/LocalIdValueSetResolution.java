package edu.mayo.cts2.framework.model.extension;

import edu.mayo.cts2.framework.model.valuesetdefinition.ResolvedValueSet;

public class LocalIdValueSetResolution extends LocalIdResource<ResolvedValueSet> {

	public LocalIdValueSetResolution(ResolvedValueSet resource) {
		super(resource);
	}

	public LocalIdValueSetResolution(String localID,
			ResolvedValueSet resource) {
		super(localID, resource);
	}
	
}
