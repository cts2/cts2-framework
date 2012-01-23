package edu.mayo.cts2.framework.model.extension;

import edu.mayo.cts2.framework.model.conceptdomainbinding.ConceptDomainBinding;

public class LocalIdConceptDomainBinding extends ChangeableLocalIdResource<ConceptDomainBinding> {

	public LocalIdConceptDomainBinding(String localID, ConceptDomainBinding conceptDomainBinding) {
		super(localID,conceptDomainBinding);
		
	}
	
	public LocalIdConceptDomainBinding(ConceptDomainBinding conceptDomainBinding) {
		super(conceptDomainBinding);
		
	}
	
}
