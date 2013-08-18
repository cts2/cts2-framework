package edu.mayo.cts2.framework.service.command.restriction;

import edu.mayo.cts2.framework.model.service.core.NameOrURI;

public class TaggedCodeSystemRestriction {
	private NameOrURI codeSystem;
	private String tag;
	
	public NameOrURI getCodeSystem() {
		return codeSystem;
	}
	public void setCodeSystem(NameOrURI codeSystem) {
		this.codeSystem = codeSystem;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	@Override
	public int hashCode()
	{
		final int prime = 37;
		int result = 1;
		result = prime * result + ((codeSystem == null) ? 0 : codeSystem.hashCode());
		result = prime * result + ((tag == null) ? 0 : tag.hashCode());
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
		TaggedCodeSystemRestriction other = (TaggedCodeSystemRestriction) obj;
		if (codeSystem == null)
		{
			if (other.codeSystem != null)
			{
				return false;
			}
		}
		else if (!codeSystem.equals(other.codeSystem))
		{
			return false;
		}
		if (tag == null)
		{
			if (other.tag != null)
			{
				return false;
			}
		}
		else if (!tag.equals(other.tag))
		{
			return false;
		}
		return true;
	}	
}