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

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import edu.mayo.cts2.framework.model.command.ResolvedFilter;
import edu.mayo.cts2.framework.model.core.MatchAlgorithmReference;
import edu.mayo.cts2.framework.model.core.PropertyReference;
import edu.mayo.cts2.framework.model.exception.ExceptionFactory;
import edu.mayo.cts2.framework.model.service.core.Query;

/**
 * The Class AbstractDirectoryBuilder.
 *
 * @param <T> the generic type
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractDirectoryBuilder<T> implements DirectoryBuilder<T> {
	
	protected static final float DEFAULT_SCORE_THRESHOLD = 0.5f;
	
	private int start = 0;

	private int maxToReturn = Integer.MAX_VALUE;

	/**
	 * The Interface Restriction.
	 *
	 * @param <T> the generic type
	 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
	 */
	protected interface Restriction<T> {
		
		/**
		 * Pass restriction.
		 *
		 * @param candidate the candidate
		 * @return true, if successful
		 */
		public boolean passRestriction(T candidate);

	}
	
	private Set<ResolvedFilter> filterComponents = new HashSet<ResolvedFilter>();
	
	private Query query;
	

	/**
	 * Adds the filter component.
	 *
	 * @param filterComponent the filter component
	 */
	protected void addFilterComponent(ResolvedFilter filterComponent) {
		this.filterComponents.add(filterComponent);
	}

	/**
	 * Gets the filter components.
	 *
	 * @return the filter components
	 */
	protected Set<ResolvedFilter> getFilterComponents() {
		return filterComponents;
	}

	/* (non-Javadoc)
	 * @see edu.mayo.cts2.rest.filter.directory.DirectoryBuilder#restrict(org.cts2.core.FilterComponent)
	 */
	@Override
	public DirectoryBuilder<T> restrict(Set<ResolvedFilter> filters) {
		if(CollectionUtils.isNotEmpty(filters)){
			for(ResolvedFilter filter : filters){
				this.restrict(filter);
			}
		}
		
		return this;
	}
	
	
	public DirectoryBuilder<T> restrict(ResolvedFilter filterComponent) {
		if(filterComponent == null ||
				StringUtils.isBlank(filterComponent.getMatchValue())){
			return this;
		} else {
			this.filterComponents.add(filterComponent);
			return this;
		}
	}

	/* (non-Javadoc)
	 * @see edu.mayo.cts2.rest.filter.directory.DirectoryBuilder#restrict(org.cts2.service.core.Query)
	 */
	@Override
	public DirectoryBuilder<T> restrict(Query query) {
		if(this.query != null){
			throw new IllegalStateException("Query alread added.");
		}
		
		this.query = query;
		
		return this;
	}

	/**
	 * Gets the match algorithm.
	 *
	 * @param reference the reference
	 * @param references the references
	 * @return the match algorithm
	 */
	protected MatchAlgorithmReference getMatchAlgorithm(MatchAlgorithmReference reference, Iterable<MatchAlgorithmReference> references){
		for(MatchAlgorithmReference matchAlgorithm : references){
			if(StringUtils.equals(matchAlgorithm.getContent(), reference.getContent())){
				return matchAlgorithm;
			}
			if(StringUtils.isNotBlank(matchAlgorithm.getUri())
					&& StringUtils.isNotBlank(reference.getUri()) &&
					StringUtils.equals(matchAlgorithm.getUri(),reference.getUri())){
				return matchAlgorithm;
			}
		}
		
		throw ExceptionFactory.createUnsupportedMatchAlgorithm(
				reference.getContent(), 
				references);
	}
	
	
	
	/**
	 * Gets the model attribute reference.
	 *
	 * @param <M> the generic type
	 * @param nameOrUri the name or uri
	 * @param references the references
	 * @return the model attribute reference
	 */
	protected <M extends PropertyReference> M getPropertyReference(
			PropertyReference nameOrUri, 
			Iterable<? extends M> references){
		for(M modelAttribute : references){
			if(modelAttribute.getReferenceTarget().getName().equals(nameOrUri.getReferenceTarget().getName())){
				return modelAttribute;
			}
			if(modelAttribute.getReferenceTarget().getUri().equals(nameOrUri.getReferenceTarget().getUri())){
				return modelAttribute;
			}
		}
		
		throw ExceptionFactory.createUnsupportedPropertyReference(
				nameOrUri, 
				references);
	}
	

	/* (non-Javadoc)
	 * @see edu.mayo.cts2.rest.filter.directory.DirectoryBuilder#addMaxToReturn(int)
	 */
	public DirectoryBuilder<T> addMaxToReturn(int maxToReturn){
		this.setMaxToReturn(maxToReturn);
		return this;
	}
	
	/* (non-Javadoc)
	 * @see edu.mayo.cts2.rest.filter.directory.DirectoryBuilder#addStart(int)
	 */
	public DirectoryBuilder<T> addStart(int start){
		this.setStart(start);
		return this;
	}
	
	/**
	 * Sets the max to return.
	 *
	 * @param maxToReturn the new max to return
	 */
	protected void setMaxToReturn(int maxToReturn) {
		this.maxToReturn = maxToReturn;
	}


	/**
	 * Gets the max to return.
	 *
	 * @return the max to return
	 */
	protected int getMaxToReturn() {
		return maxToReturn;
	}


	/**
	 * Sets the start.
	 *
	 * @param start the new start
	 */
	protected void setStart(int start) {
		this.start = start;
	}


	/**
	 * Gets the start.
	 *
	 * @return the start
	 */
	protected int getStart() {
		return start;
	}

    protected int getEnd() {
        return this.getStart() + this.getMaxToReturn();
    }
	
	protected Query getQuery(){
		return this.query;
	}
}
