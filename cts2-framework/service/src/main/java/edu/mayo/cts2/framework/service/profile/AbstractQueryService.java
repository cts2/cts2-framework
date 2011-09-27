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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;

import com.google.common.collect.Iterables;

import edu.mayo.cts2.framework.model.core.MatchAlgorithmReference;
import edu.mayo.cts2.framework.model.core.ModelAttributeReference;
import edu.mayo.cts2.framework.model.core.NameAndMeaningReference;
import edu.mayo.cts2.framework.model.core.PredicateReference;
import edu.mayo.cts2.framework.model.exception.ExceptionFactory;
import edu.mayo.cts2.framework.model.service.core.BaseQueryService;

/**
 * The Class AbstractQueryService.
 *
 * @param <T> the generic type
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractQueryService <T extends BaseQueryService> extends AbstractService<T> 
	implements InitializingBean {

	private List<? extends MatchAlgorithmReference> matchAlgorithmReferences = new ArrayList<MatchAlgorithmReference>();
	private List<? extends ModelAttributeReference> modelAttributeReferences = new ArrayList<ModelAttributeReference>();
	private List<? extends PredicateReference> predicateReferences = new ArrayList<PredicateReference>();

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		this.matchAlgorithmReferences = this.getAvailableMatchAlgorithmReferences();
		this.modelAttributeReferences = this.getAvailableModelAttributeReferences();
		this.predicateReferences = this.getAvailablePredicateReferences();
	}
	
	protected abstract String getVersion();
	protected abstract String getProvider();
	protected abstract String getDescription();
	
	protected T getService(){
		T service = super.getService();
		
		service.setSupportedMatchAlgorithm(Iterables.toArray(
				this.matchAlgorithmReferences, MatchAlgorithmReference.class));
		
		service.setSupportedModelAttribute(Iterables.toArray(
				this.modelAttributeReferences, ModelAttributeReference.class));
		
		service.setKnownProperty(Iterables.toArray(
				this.predicateReferences, PredicateReference.class));
		
		return service;
	}
	
	/**
	 * Gets the match algorithm reference.
	 *
	 * @param nameOrUri the name or uri
	 * @return the match algorithm reference
	 */
	public MatchAlgorithmReference getMatchAlgorithmReference(String nameOrUri) {
		return this.getReference(nameOrUri, this.matchAlgorithmReferences);
	}
	
	/**
	 * Gets the model attribute reference.
	 *
	 * @param nameOrUri the name or uri
	 * @return the model attribute reference
	 */
	public ModelAttributeReference getModelAttributeReference(String nameOrUri) {
		return this.getReference(nameOrUri, this.modelAttributeReferences);
	}

	/**
	 * Gets the reference.
	 *
	 * @param <R> the generic type
	 * @param nameOrUri the name or uri
	 * @param list the list
	 * @return the reference
	 */
	public <R extends NameAndMeaningReference> R getReference(String nameOrUri, List<R> list) {
		
		for(R ref : list){
			if(StringUtils.equals(ref.getContent(), nameOrUri) ||
				StringUtils.equals(ref.getUri(), nameOrUri)){
				return ref;
			}
		}
		
		throw ExceptionFactory.createUnsupportedMatchAlgorithm(nameOrUri, list);
	}
	
	/**
	 * Register match algorithm references.
	 *
	 * @return the list<? extends match algorithm reference>
	 */
	protected abstract List<? extends MatchAlgorithmReference> getAvailableMatchAlgorithmReferences();
	
	/**
	 * Register model attribute references.
	 *
	 * @return the list<? extends model attribute reference>
	 */
	protected abstract List<? extends ModelAttributeReference> getAvailableModelAttributeReferences();
	
	/**
	 * Register predicate references.
	 *
	 * @return the list<? extends predicate reference>
	 */
	protected abstract List<? extends PredicateReference> getAvailablePredicateReferences();
	
}
