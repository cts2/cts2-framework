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
import java.util.List;

import edu.mayo.cts2.framework.filter.match.StateAdjustingModelAttributeReference;
import edu.mayo.cts2.framework.model.directory.DirectoryResult;
import edu.mayo.cts2.framework.model.core.FilterComponent;
import edu.mayo.cts2.framework.model.core.MatchAlgorithmReference;
import edu.mayo.cts2.framework.model.core.types.TargetReferenceType;
import edu.mayo.cts2.framework.model.service.core.Query;

/**
 * The Class AbstractStateBuildingDirectoryBuilder.
 *
 * @param <S> the generic type
 * @param <T> the generic type
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractStateBuildingDirectoryBuilder<S,T> extends AbstractDirectoryBuilder<T> {
	
	/** The match algorithm references. */
	private List<MatchAlgorithmReference> matchAlgorithmReferences = 
		new ArrayList<MatchAlgorithmReference>();
	
	private List<StateAdjustingModelAttributeReference<S>> stateAdjustingModelAttributeReference = 
		new ArrayList<StateAdjustingModelAttributeReference<S>>();

	private Callback<S,T> callback;

	private S initialState;
	
	private List<StateBuildingRestriction<S>> restrictions = new ArrayList<StateBuildingRestriction<S>>();
	
	
	/**
	 * Instantiates a new abstract state building directory builder.
	 *
	 * @param initialState the initial state
	 * @param callback the callback
	 */
	public AbstractStateBuildingDirectoryBuilder(S initialState, Callback<S,T> callback){
		this(initialState, callback, null, null);
	}

	/**
	 * Instantiates a new abstract state building directory builder.
	 *
	 * @param initialState the initial state
	 * @param callback the callback
	 * @param matchAlgorithmReferences the match algorithm references
	 * @param modelAttributeReferences the model attribute references
	 */
	public AbstractStateBuildingDirectoryBuilder(
			S initialState,
			Callback<S,T> callback,
			List<MatchAlgorithmReference> matchAlgorithmReferences,
			List<StateAdjustingModelAttributeReference<S>> modelAttributeReferences
			){
		super();
		this.initialState = initialState;
		this.callback = callback;
		this.matchAlgorithmReferences = matchAlgorithmReferences;
		this.stateAdjustingModelAttributeReference = modelAttributeReferences;
	}

	/**
	 * Adds the supported model attribute reference.
	 *
	 * @param reference the reference
	 * @return the directory builder
	 */
	public DirectoryBuilder<T> addSupportedModelAttributeReference(
			StateAdjustingModelAttributeReference<S> reference){
		this.stateAdjustingModelAttributeReference.add(reference);
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

		return this.callback.execute(computeState(), getStart(), getMaxToReturn());
	}

	/* (non-Javadoc)
	 * @see edu.mayo.cts2.framework.filter.directory.DirectoryBuilder#count()
	 */
	public int count() {
		return this.callback.executeCount(computeState());
	}

	/**
	 * Compute state.
	 *
	 * @return the s
	 */
	private S computeState() {
		S state = this.initialState;

		for (final FilterComponent filter : this.getFilterComponents()) {
			if(filter == null){
				continue;
			}
			
			TargetReferenceType referenceType = filter.getReferenceType();
			
			MatchAlgorithmReference matchAlgorithmReference = 
					this.getMatchAlgorithm(filter.getMatchAlgorithm(), matchAlgorithmReferences);
			
			String queryString = filter.getMatchValue();
			
			switch (referenceType) {
				case PROPERTY : {
					throw new UnsupportedOperationException();
				}
				case ATTRIBUTE : {
					StateAdjustingModelAttributeReference<S> modelAttributeReference = 
						this.getModelAttributeReference(
								filter.getReferenceTarget(),
								this.stateAdjustingModelAttributeReference);
					
					state = modelAttributeReference.updateState(
							state, 
							matchAlgorithmReference, 
							queryString);
					break;
				}
				case SPECIAL : {
					throw new UnsupportedOperationException();
				}
				
				default : {
					throw new IllegalStateException();
				}
			}
		}
		
		for(StateBuildingRestriction<S> restriction : this.getRestrictions()){
			state = restriction.restrict(state);
		}
		
		return state;
	}

	/**
	 * Process query.
	 *
	 * @param query the query
	 */
	protected void processQuery(Query query){
		//
	}
	
	/**
	 * The Interface Callback.
	 *
	 * @param <S> the generic type
	 * @param <T> the generic type
	 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
	 */
	public static interface Callback<S,T> {
		 
 		/**
		  * Execute.
		  *
		  * @param state the state
		  * @param start the start
		  * @param maxResults the max results
		  * @return the directory result
		  */
 		public DirectoryResult<T> execute(
				 S state,
				 int start,
				 int maxResults);
 		
 		/**
		  * Execute count.
		  *
		  * @param state the state
		  * @return the int
		  */
		 public int executeCount(
				 S state);
 		
	}

	/**
	 * The Interface StateBuildingRestriction.
	 *
	 * @param <S> the generic type
	 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
	 */
	public static interface StateBuildingRestriction<S> {
		
		/**
		 * Restrict.
		 *
		 * @param state the state
		 * @return the s
		 */
		public S restrict(S state);
		
	}
	
	protected List<StateBuildingRestriction<S>> getRestrictions() {
		return restrictions;
	}

	protected void setRestrictions(List<StateBuildingRestriction<S>> restrictions) {
		this.restrictions = restrictions;
	}
	
	
	
	
	
}
