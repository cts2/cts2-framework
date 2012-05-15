package edu.mayo.cts2.framework.service.command.restriction;

import java.util.HashSet;
import java.util.Set;

import edu.mayo.cts2.framework.model.service.core.EntityNameOrURI;
import edu.mayo.cts2.framework.model.service.core.NameOrURI;

public class ResolvedValueSetResolutionEntityRestrictions {

	private Set<EntityNameOrURI> entities = new HashSet<EntityNameOrURI>();
	
	private NameOrURI codeSystemVersion;
	
	private TaggedCodeSystemRestriction taggedCodeSystem;

	public Set<EntityNameOrURI> getEntities() {
		return entities;
	}

	public void setEntities(Set<EntityNameOrURI> entities) {
		this.entities = entities;
	}

	public NameOrURI getCodeSystemVersion() {
		return codeSystemVersion;
	}

	public void setCodeSystemVersion(NameOrURI codeSystemVersion) {
		this.codeSystemVersion = codeSystemVersion;
	}

	public TaggedCodeSystemRestriction getTaggedCodeSystem() {
		return taggedCodeSystem;
	}

	public void setTaggedCodeSystem(TaggedCodeSystemRestriction taggedCodeSystem) {
		this.taggedCodeSystem = taggedCodeSystem;
	}

}
