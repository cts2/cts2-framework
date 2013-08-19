/*
 * Copyright: (c) 2004-2011 Mayo Foundation for Medical Education and 
 * Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
 * triple-shield Mayo logo are trademarks and service marks of MFMER.
 *
 * Except as contained in the copyright notice above, or as used to identify 
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.mayo.cts2.framework.model.command;

import edu.mayo.cts2.framework.model.core.ComponentReference;
import edu.mayo.cts2.framework.model.core.MatchAlgorithmReference;

/**
 * A 'filter' used to request a subset of CTS2 resource based on some criteria.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class ResolvedFilter {
	
	private MatchAlgorithmReference matchAlgorithmReference;
	private ComponentReference componentReference;
	private String matchValue;
	
	public MatchAlgorithmReference getMatchAlgorithmReference() {
		return matchAlgorithmReference;
	}
	
	public void setMatchAlgorithmReference(
			MatchAlgorithmReference matchAlgorithmReference) {
		this.matchAlgorithmReference = matchAlgorithmReference;
	}

	public String getMatchValue() {
		return matchValue;
	}

	public void setMatchValue(String matchValue) {
		this.matchValue = matchValue;
	}

	public ComponentReference getComponentReference() {
		return componentReference;
	}

	public void setComponentReference(ComponentReference componentReference) {
		this.componentReference = componentReference;
	}

	@Override
	public int hashCode()
	{
		final int prime = 37;
		int result = 1;
		result = prime * result + ((matchAlgorithmReference == null) ? 0 : matchAlgorithmReference.hashCode());
		result = prime * result + ((matchValue == null) ? 0 : matchValue.hashCode());
		result = prime * result + ((componentReference == null) ? 0 : componentReference.hashCode());
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
		ResolvedFilter other = (ResolvedFilter) obj;
		if (matchAlgorithmReference == null)
		{
			if (other.matchAlgorithmReference != null)
			{
				return false;
			}
		}
		else if (!matchAlgorithmReference.equals(other.matchAlgorithmReference))
		{
			return false;
		}
		if (matchValue == null)
		{
			if (other.matchValue != null)
			{
				return false;
			}
		}
		else if (!matchValue.equals(other.matchValue))
		{
			return false;
		}
		if (componentReference == null)
		{
			if (other.componentReference != null)
			{
				return false;
			}
		}
		else if (!componentReference.equals(other.componentReference))
		{
			return false;
		}
		return true;
	}
}
