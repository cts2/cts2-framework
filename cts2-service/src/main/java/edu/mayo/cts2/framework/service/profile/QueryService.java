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

import edu.mayo.cts2.framework.model.command.Page;
import edu.mayo.cts2.framework.model.core.SortCriteria;
import edu.mayo.cts2.framework.model.directory.DirectoryResult;
import edu.mayo.cts2.framework.service.profile.resolvedvalueset.name.ResolvedValueSetReadId;

/**
 * The Interface QueryService allows for querying and counting CTS2 Resources.
 * CTS2 Resources may be optionally constrained by a "query" object, and represented
 * as either a "summary" (getResourceSummaries) or a "full" resource (getResourceList).
 * 
 * Sub-interfaces will define the CTS2 Resource types returned (both full and summary), as
 * well as the specific "query" object used.
 * 
 * @see edu.mayo.cts2.framework.service.profile.ResourceQuery
 *
 * @param <ListEntry> the (full) CTS2 Resource type to return
 * @param <Summary> the (summary) CTS2 Resource type to return
 * @param <Q> the query object used to specify query parameters
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface QueryService<ListEntry,Summary,Q extends ResourceQuery> extends BaseQueryService {
	/**
	 * Gets the resource summaries.
	 *
	 * @param query the query
	 * @param sortCriteria the sort criteria
	 * @param page the page
	 * @param String uri that represents the resource
	 * @return the resource summaries
	 */
	public DirectoryResult<Summary> getResourceSummaries(
			Q query,
			SortCriteria sortCriteria,
			Page page,
			String uri);
	/**
	 * Gets the resource summaries.
	 *
	 * @param query the query
	 * @param sortCriteria the sort criteria
	 * @param page the page
	 * @return the resource summaries
	 */
	public DirectoryResult<Summary> getResourceSummaries(
			Q query,
			SortCriteria sortCriteria,
			Page page);
	
	/**
	 * Gets the resource list.
	 *
	 * @param query the query
	 * @param sortCriteria the sort criteria
	 * @param page the page
	 * @return the resource list
	 */
	public DirectoryResult<ListEntry> getResourceList(
			Q query, 
			SortCriteria sortCriteria,
			Page page);
	
	/**
	 * Count.
	 *
	 * @param query the query
	 * @return the int
	 */
	public int count(
			Q query);
	
}
