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

import edu.mayo.cts2.framework.filter.match.Matcher;
import edu.mayo.cts2.framework.filter.match.ResolvableMatchAlgorithmReference;
import edu.mayo.cts2.framework.filter.match.ResolvablePropertyReference;
import edu.mayo.cts2.framework.model.command.ResolvedFilter;
import edu.mayo.cts2.framework.model.core.MatchAlgorithmReference;
import edu.mayo.cts2.framework.model.core.PropertyReference;
import edu.mayo.cts2.framework.model.directory.DirectoryResult;
import edu.mayo.cts2.framework.model.exception.ExceptionFactory;

/**
 * A {@link DirectoryBuilder} implementation based on knowing a priori all potential
 * results, and incrementally removing ones that don't match the filters.
 * 
 * Use this {@link DirectoryBuilder} implementation when the result set is small enough
 * to be held entirely in memory.
 *
 * @param <F> the generic type
 * @param <T> the generic type
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractRemovingDirectoryBuilder<F,T> extends AbstractNonLazyDirectoryBuilder<T> {
	
	private static float DEFAULT_SCORE_THRESHOLD = 0.5f;
	
	private List<Restriction<F>> restrictions = new ArrayList<Restriction<F>>();
		
	/** The match algorithm references. */
	private Set<ResolvableMatchAlgorithmReference> matchAlgorithmReferences = 
		new HashSet<ResolvableMatchAlgorithmReference>();
	
	private Set<ResolvablePropertyReference<F>> resolvablePropertyReferences = 
		new HashSet<ResolvablePropertyReference<F>>();
	
	private Set<F> allPossibleResults;
	
	private int allPossibleResultsCount;

	/**
	 * Instantiates a new abstract removing directory builder.
	 *
	 * @param allPossibleResults the all possible results
	 */
	public AbstractRemovingDirectoryBuilder(List<F> allPossibleResults){
		this(allPossibleResults, null, null);
	}
	
	/**
	 * Instantiates a new abstract removing directory builder.
	 *
	 * @param allPossibleResults the all possible results
	 * @param matchAlgorithmReferences the match algorithm references
	 * @param resolvableModelAttributeReferences the resolvable model attribute references
	 * @param resolvablePredicateReferences the resolvable predicate references
	 */
	public AbstractRemovingDirectoryBuilder(
			List<F> allPossibleResults,
			Set<ResolvableMatchAlgorithmReference> matchAlgorithmReferences,
			Set<ResolvablePropertyReference<F>> resolvablePropertyReferences
			){
		super();
		this.allPossibleResults = new HashSet<F>(allPossibleResults);
		this.allPossibleResultsCount = allPossibleResults.size();
		
		if(matchAlgorithmReferences != null){
			this.matchAlgorithmReferences = matchAlgorithmReferences;
		}
		
		if(resolvablePropertyReferences != null){
			this.resolvablePropertyReferences = resolvablePropertyReferences;
		}
	}
	
	/**
	 * Adds the supported model attribute reference.
	 *
	 * @param reference the reference
	 * @return the directory builder
	 */
	public DirectoryBuilder<T> addSupportedPropertyReference(
			ResolvablePropertyReference<F> reference){
		this.resolvablePropertyReferences.add(reference);
		return this;
	}
	
	/**
	 * Adds the supported model attribute reference.
	 *
	 * @param reference the reference
	 * @return the directory builder
	 */
	public DirectoryBuilder<T> addSupportedMatchAlgorithmReference(
			ResolvableMatchAlgorithmReference reference){
		this.matchAlgorithmReferences.add(reference);
		
		return this;
	}
	
	public DirectoryBuilder<T> addResolvablePropertyReference(
			ResolvablePropertyReference<F> reference){
		this.resolvablePropertyReferences.add(reference);
		return this;
	}
	
	/* (non-Javadoc)
	 * @see org.cts2.internal.model.uri.restrict.ListBasedResolvingRestrictionHandler#restrict(java.util.List, org.cts2.core.Filter)
	 */
	public DirectoryResult<T> resolve() {
		
		this.verifyRange();
		
		for(Restriction<F> restriction : this.getRestrictions()){
			allPossibleResults.retainAll(
					doProcessRestriction(restriction));
		}
	
		for(final ResolvedFilter filter : this.getFilterComponents()){
			allPossibleResults.retainAll(
					doRestrict(filter, DEFAULT_SCORE_THRESHOLD));
		}
		
		return this.createDirectoryResult(new ArrayList<F>(allPossibleResults));
	}
	
	/* (non-Javadoc)
	 * @see edu.mayo.cts2.framework.filter.directory.DirectoryBuilder#count()
	 */
	public int count() {
	
		for(final ResolvedFilter filter : this.getFilterComponents()){
			allPossibleResults.retainAll(
					doRestrict(filter, DEFAULT_SCORE_THRESHOLD));	
		}
		
		return this.allPossibleResults.size();
	}
	
	/**
	 * Verify range.
	 */
	private void verifyRange() {
		if(this.getStart() != 0 &&
				this.getStart() >= this.allPossibleResultsCount){
			throw ExceptionFactory.createPageOutOfBoundsException();
		}	
	}

	/**
	 * Creates the directory result.
	 *
	 * @param results the results
	 * @return the directory result
	 */
	protected DirectoryResult<T> createDirectoryResult(List<F> results){
		
		List<T> transformedResults = this.transformResults(results);
		
		int resultsMatchingFilters = transformedResults.size();
		
		List<T> prunedResults = this.pruneResults(transformedResults);

		boolean atEnd = resultsMatchingFilters <= this.getEnd();

        return new DirectoryResult<T>(prunedResults, atEnd);
	}

	/**
	 * Transform results.
	 *
	 * @param results the results
	 * @return the list
	 */
	protected abstract List<T> transformResults(List<F> results);

	/**
	 * Prune results.
	 *
	 * @param results the results
	 * @return the list
	 */
	protected List<T> pruneResults(List<T> results){
		List<T> prunedList = new ArrayList<T>();
		
		for(int i = this.getStart(); 
			(i < this.getMaxToReturn() + this.getStart()) && i < results.size();
			i++){
			
			prunedList.add(results.get(i));
		}
		
		return prunedList;
	}
	
	/**
	 * Do process restriction.
	 *
	 * @param restriction the restriction
	 * @return the sets the
	 */
	protected Set<F> doProcessRestriction(Restriction<F> restriction){
		Set<F> passingResults = new HashSet<F>();
		
		for(F candidate : this.allPossibleResults){
				if(restriction.passRestriction(candidate)){
					passingResults.add(candidate);
			}
		}
		return passingResults;	
	}

	/**
	 * Do restrict.
	 *
	 * @param filterComponent the filter component
	 * @param minScore the min score
	 * @return the sets the
	 */
	protected Set<F> doRestrict(ResolvedFilter filterComponent, float minScore){

		Set<F> returnSet = new HashSet<F>();
		
		Matcher algorithm = this.getMatchAlgorithm(filterComponent.getMatchAlgorithmReference());
		
		String matchText = filterComponent.getMatchValue();
		
		for(F candidate : this.allPossibleResults){
			Iterable<String> candidates = 
				this.getCandidateText(filterComponent.getPropertyReference(), candidate);
			
			if(candidates != null){
				for(String candidateText : candidates){
					float score = algorithm.matchScore(matchText, candidateText);
					
					if(score != 0 && score >= minScore){
						returnSet.add(candidate);
						break;
					}
				}
			}
		}

		return returnSet;
	}
	
	/**
	 * Gets the candidate text.
	 *
	 * @param filterComponent the filter component
	 * @param referenceType the reference type
	 * @param candidate the candidate
	 * @return the candidate text
	 */
	protected Iterable<String> getCandidateText(
			PropertyReference propertyReference,
			F candidate) {
		ResolvablePropertyReference<F> ref = 
				this.getResolvablePropertyReferences(propertyReference);
		
		return ref.getModelAttributeValue(candidate);
	}
	
	/**
	 * Gets the resolvable predicate references.
	 *
	 * @param nameOrUri the name or uri
	 * @return the resolvable predicate references
	 */

	
	private ResolvablePropertyReference<F> getResolvablePropertyReferences(PropertyReference nameOrUri){
		for(ResolvablePropertyReference<F> predicate : this.resolvablePropertyReferences){
			if(predicate.getReferenceTarget().getName().equals(nameOrUri.getReferenceTarget().getName())){
				return predicate;
			}
			if(predicate.getReferenceTarget().getUri().equals(nameOrUri.getReferenceTarget().getUri())){
				return predicate;
			}
		}
		
		throw ExceptionFactory.createUnsupportedPropertyReference(
				nameOrUri, 
				this.resolvablePropertyReferences);				
	}
	
	/**
	 * Gets the match algorithm.
	 *
	 * @param reference the reference
	 * @return the match algorithm
	 */
	protected ResolvableMatchAlgorithmReference getMatchAlgorithm(MatchAlgorithmReference reference){
		for(ResolvableMatchAlgorithmReference matchAlgorithm : this.matchAlgorithmReferences){
			if(matchAlgorithm.getContent().equals(reference.getContent())){
				return matchAlgorithm;
			}
		}
		
		throw ExceptionFactory.createUnsupportedMatchAlgorithm(
				reference.getContent(), 
				this.matchAlgorithmReferences);
	}
	
	/**
	 * Adds the restriction.
	 *
	 * @param restriction the restriction
	 */
	public void addRestriction(Restriction<F> restriction) {
		this.restrictions.add(restriction);
	}

	protected List<Restriction<F>> getRestrictions() {
		return restrictions;
	}
}
