package edu.mayo.cts2.framework.model.command;

import edu.mayo.cts2.framework.model.core.LanguageReference;
import edu.mayo.cts2.framework.model.service.core.types.ActiveOrAll;

public class ResolvedReadContext {

	/**
	 * Determines whether the query only applies to only active or all entries.
	 */
	private ActiveOrAll active = ActiveOrAll.ACTIVE_ONLY;

	/**
	 * The spoken or written language that should be used for the results of the
	 * inquiry, where appropriate.
	 */
	private LanguageReference languageReference;
	
	/**
	 * An option to limit the content of the returned result to a specific set
	 * of fields. May be null, meaning that the entire resource is to be returned.
	 * 
	 * NOTE: This field will only apply in contexts where the entire resource is 
	 * being requested, such as 'read' and 'resolveToList' operations. 
	 */
	private ReturnContentFilter returnContentFilter;

	/**
	 * The URI of an open change set whose contents should be included in the
	 * results of the access request. changeSetContext is only applicable in
	 * services that support the Authoring profile
	 */
	private String changeSetContextUri;

	/**
	 * The contextual date and time of the query. referenceTime is may only be
	 * present in services that support the Temporal profile.
	 */
	private java.util.Date referenceTime;

	public ActiveOrAll getActive() {
		return active;
	}

	public void setActive(ActiveOrAll active) {
		this.active = active;
	}

	public LanguageReference getLanguageReference() {
		return languageReference;
	}

	public void setLanguageReference(LanguageReference languageReference) {
		this.languageReference = languageReference;
	}

	public java.lang.String getChangeSetContextUri() {
		return changeSetContextUri;
	}

	public void setChangeSetContextUri(java.lang.String changeSetContextUri) {
		this.changeSetContextUri = changeSetContextUri;
	}

	public java.util.Date getReferenceTime() {
		return referenceTime;
	}

	public void setReferenceTime(java.util.Date referenceTime) {
		this.referenceTime = referenceTime;
	}

	public ReturnContentFilter getReturnContentFilter() {
		return returnContentFilter;
	}

	public void setReturnContentFilter(ReturnContentFilter returnContentFilter) {
		this.returnContentFilter = returnContentFilter;
	}

	@Override
	public int hashCode()
	{
		final int prime = 37;
		int result = 1;
		result = prime * result + ((active == null) ? 0 : active.hashCode());
		result = prime * result + ((changeSetContextUri == null) ? 0 : changeSetContextUri.hashCode());
		result = prime * result + ((languageReference == null) ? 0 : languageReference.hashCode());
		result = prime * result + ((referenceTime == null) ? 0 : referenceTime.hashCode());
		result = prime * result + ((returnContentFilter == null) ? 0 : returnContentFilter.hashCode());
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
		ResolvedReadContext other = (ResolvedReadContext) obj;
		if (active != other.active)
		{
			return false;
		}
		if (changeSetContextUri == null)
		{
			if (other.changeSetContextUri != null)
			{
				return false;
			}
		}
		else if (!changeSetContextUri.equals(other.changeSetContextUri))
		{
			return false;
		}
		if (languageReference == null)
		{
			if (other.languageReference != null)
			{
				return false;
			}
		}
		else if (!languageReference.equals(other.languageReference))
		{
			return false;
		}
		if (referenceTime == null)
		{
			if (other.referenceTime != null)
			{
				return false;
			}
		}
		else if (!referenceTime.equals(other.referenceTime))
		{
			return false;
		}
		if (returnContentFilter == null)
		{
			if (other.returnContentFilter != null)
			{
				return false;
			}
		}
		else if (!returnContentFilter.equals(other.returnContentFilter))
		{
			return false;
		}
		return true;
	}
}
