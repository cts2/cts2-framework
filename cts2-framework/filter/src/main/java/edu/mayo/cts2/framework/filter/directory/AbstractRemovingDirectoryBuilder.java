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
import edu.mayo.cts2.framework.filter.match.ResolvableModelAttributeReference;
import edu.mayo.cts2.framework.filter.match.ResolvablePredicateReference;
import edu.mayo.cts2.framework.model.directory.DirectoryResult;
import edu.mayo.cts2.framework.model.exception.ExceptionFactory;
import edu.mayo.cts2.framework.model.core.FilterComponent;
import edu.mayo.cts2.framework.model.core.MatchAlgorithmReference;
import edu.mayo.cts2.framework.model.core.URIAndEntityName;
import edu.mayo.cts2.framework.model.core.types.TargetReferenceType;

/**
 * The Class AbstractRemovingDirectoryBuilder.
 *
 * @param <F> the generic type
 * @param <T> the generic type
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractRemovingDirectoryBuilder<F,T> extends AbstractNonLazyDirectoryBuilder<T> {
	
	private static float DEFAULT_SCORE_THRESHOLD = 0.5f;
	
	private List<Restriction<F>> restrictions = new ArrayList<Restriction<F>>();
		
	/** The match algorithm references. */
	private List<ResolvableMatchAlgorithmReference> matchAlgorithmReferences = 
		new ArrayList<ResolvableMatchAlgorithmReference>();
	
	private List<ResolvableModelAttributeReference<F>> resolvableModelAttributeReferences = 
		new ArrayList<ResolvableModelAttributeReference<F>>();
	
	private List<ResolvablePredicateReference<F>> resolvablePredicateReferences = 
		new ArrayList<ResolvablePredicateReference<F>>();
	
	private Set<F> allPossibleResults;
	
	private int allPossibleResultsCount;

	/**
	 * Instantiates a new abstract removing directory builder.
	 *
	 * @param allPossibleResults the all possible results
	 */
	public AbstractRemovingDirectoryBuilder(List<F> allPossibleResults){
		this(allPossibleResults, null, null, null);
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
			List<ResolvableMatchAlgorithmReference> matchAlgorithmReferences,
			List<ResolvableModelAttributeReference<F>> resolvableModelAttributeReferences,
			List<ResolvablePredicateReference<F>> resolvablePredicateReferences
			){
		super();
		this.allPossibleResults = new HashSet<F>(allPossibleResults);
		this.allPossibleResultsCount = allPossibleResults.size();
		
		if(matchAlgorithmReferences != null){
			this.matchAlgorithmReferences = matchAlgorithmReferences;
		}
		
		if(resolvableModelAttributeReferences != null){
			this.resolvableModelAttributeReferences = resolvableModelAttributeReferences;
		}
		
		if(resolvablePredicateReferences != null){
			this.resolvablePredicateReferences = resolvablePredicateReferences;
		}
	}
	
	/**
	 * Adds the supported model attribute reference.
	 *
	 * @param reference the reference
	 * @return the directory builder
	 */
	public DirectoryBuilder<T> addSupportedModelAttributeReference(
			ResolvableModelAttributeReference<F> reference){
		this.resolvableModelAttributeReferences.add(reference);
		return this;
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
	
	/* (non-Javadoc)
	 * @see org.cts2.internal.model.uri.restrict.ListBasedResolvingRestrictionHandler#restrict(java.util.List, org.cts2.core.Filter)
	 */
	public DirectoryResult<T> resolve() {
		
		this.verifyRange();
		
		for(Restriction<F> restriction : this.getRestrictions()){
			
			allPossibleResults.retainAll(
					doProcessRestriction(restriction));
		}
	
		for(final FilterComponent filter : this.getFilterComponents()){
			
			
						allPossibleResults.retainAll(
								doRestrict(filter, DEFAULT_SCORE_THRESHOLD));
				
		}
		
		return this.createDirectoryResult(new ArrayList<F>(allPossibleResults));
	}
	
	/* (non-Javadoc)
	 * @see edu.mayo.cts2.sdk.filter.directory.DirectoryBuilder#count()
	 */
	public int count() {
	
		for(final FilterComponent filter : this.getFilterComponents()){
			
			
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

		boolean isComplete = resultsMatchingFilters <= this.getMaxToReturn();
		
		boolean atEnd = resultsMatchingFilters <= this.getMaxToReturn() + this.getStart();
		
		return new DirectoryResult<T>(prunedResults, 
				isComplete, atEnd);
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
	protected Set<F> doRestrict(FilterComponent filterComponent, float minScore){

		Set<F> returnSet = new HashSet<F>();
		
		Matcher algorithm = this.getMatchAlgorithm(filterComponent.getMatchAlgorithm());
		
		TargetReferenceType referenceType = filterComponent.getReferenceType();
		
		String matchText = filterComponent.getMatchValue();
		
		for(F candidate : this.allPossibleResults){
			Iterable<String> candidates = 
				this.getCandidateText(filterComponent, referenceType, candidate);
			
			for(String candidateText : candidates){
				float score = algorithm.matchScore(matchText, candidateText);
				
				if(score != 0 && score >= minScore){
					returnSet.add(candidate);
					break;
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
			FilterComponent filterComponent,
			TargetReferenceType referenceType,
			F candidate) {
		Iterable<String> candidateText;
		
		switch (referenceType) {
			case PROPERTY : {
				ResolvablePredicateReference<F> modelAttributeReference = 
					this.getResolvablePredicateReferences(filterComponent.getReferenceTarget());
				
				candidateText = modelAttributeReference.getModelAttributeValue(candidate);
				break;
			}
			case ATTRIBUTE : {
				ResolvableModelAttributeReference<F> modelAttributeReference = 
					this.getModelAttributeReference(
							filterComponent.getReferenceTarget(),
							this.resolvableModelAttributeReferences);
				
				candidateText = modelAttributeReference.getModelAttributeValue(candidate);
				break;
			}
			case SPECIAL : {
				throw new UnsupportedOperationException();
			}
			
			default : {
				throw new IllegalStateException();
			}
		}
		
		return candidateText;
	}
	
	/**
	 * Gets the resolvable predicate references.
	 *
	 * @param nameOrUri the name or uri
	 * @return the resolvable predicate references
	 */

	
	private ResolvablePredicateReference<F> getResolvablePredicateReferences(URIAndEntityName nameOrUri){
		for(ResolvablePredicateReference<F> predicate : this.resolvablePredicateReferences){
			if(predicate.getName().equals(nameOrUri.getName())){
				return predicate;
			}
			if(predicate.getUri().equals(nameOrUri.getUri())){
				return predicate;
			}
		}
		
		throw ExceptionFactory.createUnsupportedModelAttribute(
				nameOrUri, 
				this.resolvableModelAttributeReferences);
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
