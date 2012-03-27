/*
 * Copyright: (c) 2004-2012 Mayo Foundation for Medical Education and 
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
package edu.mayo.cts2.framework.service.profile.valuesetdefinition;

import java.util.List;

import edu.mayo.cts2.framework.model.core.EntitySynopsis;
import edu.mayo.cts2.framework.model.directory.DirectoryResult;
import edu.mayo.cts2.framework.model.valuesetdefinition.ResolvedValueSetHeader;

/**
 * The Class ResolvedValueSetResult.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class ResolvedValueSetResult extends DirectoryResult<EntitySynopsis>{
	
	private ResolvedValueSetHeader resolvedValueSetHeader;

	/**
	 * Instantiates a new resolved value set result.
	 *
	 * @param resolvedValueSetHeader the resolved value set header
	 * @param entries the entries
	 * @param atEnd the at end
	 */
	public ResolvedValueSetResult(
			ResolvedValueSetHeader resolvedValueSetHeader,
			List<EntitySynopsis> entries, 
			boolean atEnd) {
		super(entries, atEnd);
		this.setResolvedValueSetHeader(resolvedValueSetHeader);
	}

	/**
	 * Gets the resolved value set header.
	 *
	 * @return the resolved value set header
	 */
	public ResolvedValueSetHeader getResolvedValueSetHeader() {
		return resolvedValueSetHeader;
	}

	/**
	 * Sets the resolved value set header.
	 *
	 * @param resolvedValueSetHeader the new resolved value set header
	 */
	public void setResolvedValueSetHeader(ResolvedValueSetHeader resolvedValueSetHeader) {
		this.resolvedValueSetHeader = resolvedValueSetHeader;
	}
}
