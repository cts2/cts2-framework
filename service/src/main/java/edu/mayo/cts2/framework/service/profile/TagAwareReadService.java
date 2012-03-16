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
package edu.mayo.cts2.framework.service.profile;

import java.util.List;

import edu.mayo.cts2.framework.model.command.ResolvedReadContext;
import edu.mayo.cts2.framework.model.core.VersionTagReference;
import edu.mayo.cts2.framework.model.service.core.NameOrURI;

/**
 * The Interface TagAwareReadService.
 *
 * @param <R> the generic type
 * @param <I> the generic type
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface TagAwareReadService<R,I> extends ReadService<R,I> {

	/**
	 * Read by tag.
	 *
	 * @param parentIdentifier the parent identifier
	 * @param tagNameOrUri the tag name or uri
	 * @param readContext the read context
	 * @return the r
	 */
	public R readByTag(NameOrURI parentIdentifier, VersionTagReference tag, ResolvedReadContext readContext);

	/**
	 * Exists by tag.
	 *
	 * @param parentIdentifier the parent identifier
	 * @param tagNameOrUri the tag name or uri
	 * @param readContext the read context
	 * @return the r
	 */
	public boolean existsByTag(NameOrURI parentIdentifier, VersionTagReference tag, ResolvedReadContext readContext);
	
    /**
     * Gets the supported tags.
     *
     * @return the supported tags
     */
    public List<VersionTagReference> getSupportedTags();
}
