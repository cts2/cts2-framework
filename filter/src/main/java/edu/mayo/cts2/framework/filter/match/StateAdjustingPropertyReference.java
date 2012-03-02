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
package edu.mayo.cts2.framework.filter.match;

import edu.mayo.cts2.framework.model.castor.MarshallSuperClass;
import edu.mayo.cts2.framework.model.core.MatchAlgorithmReference;
import edu.mayo.cts2.framework.model.core.PropertyReference;

/**
 * The Class StateAdjustingModelAttributeReference.
 *
 * @param <S> the generic type
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class StateAdjustingPropertyReference<S> extends PropertyReference implements MarshallSuperClass {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 5500382462242484409L;
	
	private StateUpdater<S> stateUpdater;
		
	/**
	 * Instantiates a new state adjusting model attribute reference.
	 *
	 * @param stateUpdater the state updater
	 */
	public StateAdjustingPropertyReference(StateUpdater<S> stateUpdater) {
		this.stateUpdater = stateUpdater;
	}

	/**
	 * Update state.
	 *
	 * @param currentState the current state
	 * @param matchAlgorithm TODO
	 * @param queryString the query string
	 * @return the s
	 */
	public S updateState(S currentState, MatchAlgorithmReference matchAlgorithm, String queryString){
		return this.stateUpdater.updateState(currentState, matchAlgorithm, queryString);
	}

	public static <S> StateAdjustingPropertyReference<S> toPropertyReference(
			PropertyReference ref, StateUpdater<S> stateUpdater){
		StateAdjustingPropertyReference<S> returnRef = 
			new StateAdjustingPropertyReference<S>(stateUpdater);
		
		returnRef.setReferenceTarget(ref.getReferenceTarget());
		returnRef.setReferenceTarget(ref.getReferenceTarget());
	
		return returnRef;
	}

	/**
	 * The Interface StateUpdater.
	 *
	 * @param <S> the generic type
	 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
	 */
	public static interface StateUpdater<S> {
		
		/**
		 * Update state.
		 *
		 * @param currentState the current state
		 * @param matchAlgorithm TODO
		 * @param queryString the query string
		 * @return the s
		 */
		public S updateState(S currentState, MatchAlgorithmReference matchAlgorithm, String queryString);
	}
}
