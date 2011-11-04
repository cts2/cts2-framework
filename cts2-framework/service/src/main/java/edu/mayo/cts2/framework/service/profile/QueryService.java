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
package edu.mayo.cts2.framework.service.profile;

import java.util.Set;

import edu.mayo.cts2.framework.model.command.Page;
import edu.mayo.cts2.framework.model.command.ResolvedFilter;
import edu.mayo.cts2.framework.model.command.ResolvedReadContext;
import edu.mayo.cts2.framework.model.core.FilterComponent;
import edu.mayo.cts2.framework.model.core.MatchAlgorithmReference;
import edu.mayo.cts2.framework.model.core.ModelAttributeReference;
import edu.mayo.cts2.framework.model.core.PredicateReference;
import edu.mayo.cts2.framework.model.directory.DirectoryResult;
import edu.mayo.cts2.framework.model.service.core.Query;

/**
 * The Interface QueryService.
 *
 * @param <Resource> the generic type
 * @param <Summary> the generic type
 * @param <Restrictions> the generic type
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface QueryService<Resource,Summary,Restrictions> extends Cts2Profile {

	/**
	 * Gets the resource summaries.
	 *
	 * @param query the query
	 * @param filterComponent the filter component
	 * @param restrictions the restrictions
	 * @param readContext TODO
	 * @param page the page
	 * @return the resource summaries
	 */
	public DirectoryResult<Summary> getResourceSummaries(
			Query query,
			Set<ResolvedFilter> filterComponent, 
			Restrictions restrictions,
			ResolvedReadContext readContext, 
			Page page);
	
	/**
	 * Gets the resource list.
	 *
	 * @param query the query
	 * @param filterComponent the filter component
	 * @param restrictions the restrictions
	 * @param page the page
	 * @return the resource list
	 */
	public DirectoryResult<Resource> getResourceList(
			Query query,
			Set<ResolvedFilter> filterComponent, 
			Restrictions restrictions,
			Page page);
	
	/**
	 * Count.
	 *
	 * @param query the query
	 * @param filterComponent the filter component
	 * @param restrictions the restrictions
	 * @return the int
	 */
	public int count(
			Query query,
			Set<ResolvedFilter> filterComponent,
			Restrictions restrictions);
	
	/**
	 * Gets the match algorithm reference.
	 *
	 * @param nameOrUri the name or uri
	 * @return the match algorithm reference
	 */
	public MatchAlgorithmReference getMatchAlgorithmReference(String nameOrUri);
	
	/**
	 * Gets the model attribute reference.
	 *
	 * @param nameOrUri the name or uri
	 * @return the model attribute reference
	 */
	public ModelAttributeReference getModelAttributeReference(String nameOrUri);
	
	/**
	 * Gets the property reference.
	 *
	 * @param nameOrUri the name or uri
	 * @return the property reference
	 */
	public PredicateReference getPropertyReference(String nameOrUri);
}
