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

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codeSystemVersion == null) ? 0 : codeSystemVersion.hashCode());
		result = prime * result + ((entities == null) ? 0 : entities.hashCode());
		result = prime * result + ((taggedCodeSystem == null) ? 0 : taggedCodeSystem.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}
		ResolvedValueSetResolutionEntityRestrictions other = (ResolvedValueSetResolutionEntityRestrictions) obj;
		if (codeSystemVersion == null)
		{
			if (other.codeSystemVersion != null)
			{
				return false;
			}
		}
		else if (!codeSystemVersion.equals(other.codeSystemVersion))
		{
			return false;
		}
		if (entities == null)
		{
			if (other.entities != null)
			{
				return false;
			}
		}
		else if (!entities.equals(other.entities))
		{
			return false;
		}
		if (taggedCodeSystem == null)
		{
			if (other.taggedCodeSystem != null)
			{
				return false;
			}
		}
		else if (!taggedCodeSystem.equals(other.taggedCodeSystem))
		{
			return false;
		}
		return true;
	}
}
