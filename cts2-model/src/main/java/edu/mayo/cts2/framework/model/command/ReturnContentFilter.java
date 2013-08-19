/*
 * Copyright: (c) 2004-2013 Mayo Foundation for Medical Education and 
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

import java.util.HashSet;
import java.util.Set;

/**
 * A filter to restrict returned Resource content to only requested
 * field types.
 */
public class ReturnContentFilter {

	public enum PropertyType {PROPERTY, PRESENTATION, DEFINTION, COMMENT}

	private Set<PropertyType> propertyTypes = new HashSet<PropertyType>();

	public Set<PropertyType> getPropertyTypes() {
		return propertyTypes;
	}

	public void setPropertyTypes(Set<PropertyType> propertyTypes) {
		this.propertyTypes = propertyTypes;
	}

	@Override
	public int hashCode()
	{
		final int prime = 37;
		int result = 1;
		result = prime * result + ((propertyTypes == null) ? 0 : propertyTypes.hashCode());
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
		ReturnContentFilter other = (ReturnContentFilter) obj;
		if (propertyTypes == null)
		{
			if (other.propertyTypes != null)
			{
				return false;
			}
		}
		return propertyTypes.equals(other.propertyTypes);
	}
}
