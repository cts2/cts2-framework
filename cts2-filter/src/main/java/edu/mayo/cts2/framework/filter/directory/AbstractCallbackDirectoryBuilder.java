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
package edu.mayo.cts2.framework.filter.directory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import edu.mayo.cts2.framework.filter.match.ResolvableMatchAlgorithmReference;
import edu.mayo.cts2.framework.model.command.ResolvedFilter;
import edu.mayo.cts2.framework.model.core.MatchAlgorithmReference;
import edu.mayo.cts2.framework.model.directory.DirectoryResult;
import edu.mayo.cts2.framework.model.exception.ExceptionFactory;

/**
 * The Class AbstractCallbackDirectoryBuilder.
 *
 * @param <T> the generic type
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractCallbackDirectoryBuilder<T> extends AbstractNonLazyDirectoryBuilder<T> {
		
	/** The match algorithm references. */
	private Set<MatchAlgorithmReference> matchAlgorithmReferences = 
		new HashSet<MatchAlgorithmReference>();
		
	private Callback<T> callback;
	
	private List<T> resultBuffer;

	/**
	 * Instantiates a new abstract callback directory builder.
	 *
	 * @param callback the callback
	 */
	public AbstractCallbackDirectoryBuilder(Callback<T> callback){
		this(callback, null);
	}
	
	/**
	 * Instantiates a new abstract callback directory builder.
	 *
	 * @param callback the callback
	 * @param matchAlgorithmReferences the match algorithm references
	 */
	public AbstractCallbackDirectoryBuilder(
			Callback<T> callback,
			Set<MatchAlgorithmReference> matchAlgorithmReferences
			){
		super();
		this.callback = callback;
		this.matchAlgorithmReferences = matchAlgorithmReferences;
	}
	
	/**
	 * Adds the supported model attribute reference.
	 *
	 * @param reference the reference
	 * @return the directory builder
	 */
	public DirectoryBuilder<T> addSupportedModelAttributeReference(
			ResolvableMatchAlgorithmReference reference){
		this.matchAlgorithmReferences.add(reference);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.cts2.internal.model.uri.restrict.ListBasedResolvingRestrictionHandler
	 * #restrict(java.util.List, org.cts2.core.Filter)
	 */
	public DirectoryResult<T> resolve() {

		boolean atEnd = true;
		
		if(CollectionUtils.isEmpty(this.getFilterComponents())){
			return this.callback.execute(this.getStart(), this.getMaxToReturn());
		}
		
		if(this.getFilterComponents().size() == 1){
			ResolvedFilter filterComponent = this.getFilterComponents().iterator().next();
			
			return this.callback.execute(
					filterComponent, 
					DEFAULT_SCORE_THRESHOLD,
					this.getStart(),
					this.getMaxToReturn());
		}

		for (final ResolvedFilter filter : this.getFilterComponents()) {

			if (this.resultBuffer == null) {
				this.resultBuffer = new ArrayList<T>();

				DirectoryResult<T> result = doRestrict(
						filter,
						DEFAULT_SCORE_THRESHOLD);
				
				atEnd &= result.isAtEnd();
				
				this.resultBuffer.addAll(result.getEntries());

			} else {
				DirectoryResult<T> result = doRestrict(
						filter,
						DEFAULT_SCORE_THRESHOLD);
				
				atEnd &= result.isAtEnd();
				
				this.resultBuffer.addAll(result.getEntries());
			}

		}

		return new DirectoryResult<T>(this.resultBuffer, atEnd);
	}
	
	/* (non-Javadoc)
	 * @see edu.mayo.cts2.framework.filter.directory.DirectoryBuilder#count()
	 */
	public int count() {
		
		if(CollectionUtils.isEmpty(this.getFilterComponents())){
			return this.callback.executeCount();
		}
		
		if(this.getFilterComponents().size() == 1){
			ResolvedFilter filterComponent = this.getFilterComponents().iterator().next();
			
			return this.callback.executeCount(filterComponent, DEFAULT_SCORE_THRESHOLD);
		}
		
		for (final ResolvedFilter filter : this.getFilterComponents()) {

			if (this.resultBuffer == null) {
				this.resultBuffer = new ArrayList<T>();

				DirectoryResult<T> result = doRestrict(
						filter,
						DEFAULT_SCORE_THRESHOLD);

				this.resultBuffer.addAll(result.getEntries());

			} else {
				DirectoryResult<T> result = doRestrict(
						filter,
						DEFAULT_SCORE_THRESHOLD);
		
				this.resultBuffer.addAll(result.getEntries());
			}

		}

		return resultBuffer.size();
	}

	/**
	 * Do restrict.
	 *
	 * @param filterComponent the filter component
	 * @param minScore the min score
	 * @return the directory result
	 */
	protected DirectoryResult<T> doRestrict(ResolvedFilter filterComponent, float minScore){

		return this.callback.execute(
				filterComponent, 
				minScore,
				this.getStart(),
				this.getMaxToReturn());
	}

	/**
	 * Gets the match algorithm.
	 *
	 * @param reference the reference
	 * @return the match algorithm
	 */
	protected MatchAlgorithmReference getMatchAlgorithm(MatchAlgorithmReference reference){
		for(MatchAlgorithmReference matchAlgorithm : this.matchAlgorithmReferences){
			if(StringUtils.equals(matchAlgorithm.getContent(), reference.getContent())){
				return matchAlgorithm;
			}
			if(StringUtils.equals(matchAlgorithm.getUri(),reference.getUri())){
				return matchAlgorithm;
			}
		}
		
		throw ExceptionFactory.createUnsupportedMatchAlgorithm(
				reference.getContent(), 
				this.matchAlgorithmReferences);
	}
	
	/**
	 * The Interface Callback.
	 *
	 * @param <T> the generic type
	 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
	 */
	public static interface Callback<T> {
		 
 		/**
		  * Execute.
		  *
		  * @param filterComponent the filter component
		  * @param matchAlgorithm the match algorithm
		  * @param minScore the min score
		  * @param start the start
		  * @param maxResults the max results
		  * @return the directory result
		  */
 		public DirectoryResult<T> execute(
				 ResolvedFilter filterComponent, 
				 float minScore,
				 int start,
				 int maxResults);
 		
 		/**
		  * Execute count.
		  *
		  * @param filterComponent the filter component
		  * @param matchAlgorithm the match algorithm
		  * @param minScore the min score
		  * @return the int
		  */
		 public int executeCount(
				 ResolvedFilter filterComponent, 
				 float minScore);
 		
 		/**
		  * Execute.
		  *
		  * @param start the start
		  * @param maxResults the max results
		  * @return the directory result
		  */
		 public DirectoryResult<T> execute(
				 int start,
				 int maxResults);
 		
 		/**
		  * Execute count.
		  *
		  * @return the int
		  */
		 public int executeCount();
	}
	
}
