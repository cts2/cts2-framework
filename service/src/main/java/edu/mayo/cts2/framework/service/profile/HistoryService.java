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

import java.util.Date;

import edu.mayo.cts2.framework.model.directory.DirectoryResult;

/**
 * The Interface HistoryService.
 *
 * @param <R> the generic type
 * @param <I> the generic type
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface HistoryService<R,I> extends BaseQueryService {

	/**
	 * Gets the earliest change.
	 *
	 * @return the earliest change
	 */
	public Date getEarliestChange();
	
	/**
	 * Gets the latest change.
	 *
	 * @return the latest change
	 */
	public Date getLatestChange();
	
	/**
	 * Gets the change history.
	 *
	 * @param identifier the identifier
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @return the change history
	 */
	public DirectoryResult<R> getChangeHistory(I identifier, Date fromDate, Date toDate);
	
	/**
	 * Gets the earliest change for.
	 *
	 * @param identifier the identifier
	 * @return the earliest change for
	 */
	public R getEarliestChangeFor(I identifier);
	
	/**
	 * Gets the last change for.
	 *
	 * @param identifier the identifier
	 * @return the last change for
	 */
	public R getLastChangeFor(I identifier);
	
	/**
	 * Gets the change history for.
	 *
	 * @param identifier the identifier
	 * @return the change history for
	 */
	public DirectoryResult<R> getChangeHistoryFor(I identifier);
}
