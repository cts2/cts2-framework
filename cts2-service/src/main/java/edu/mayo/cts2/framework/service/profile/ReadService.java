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

import edu.mayo.cts2.framework.model.command.ResolvedReadContext;
import edu.mayo.cts2.framework.model.service.core.types.FunctionalProfile;

/**
 * The Interface ReadService allows for the retrieval or existence test
 * of a single CTS2 Resource that can be uniquely, unambiguously identified 
 * by a single 'identifier.'
 * 
 * Sub-interfaces will define both the CTS2 Resource type and the Identifier
 * type to be used, as well as any other retrieval/existence methods as appropriate.
 *
 * @param <R> the CTS2 Resource Type returned by this Service
 * @param <I> the Identifier used to uniquely identify the CTS2 Resource
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@FunctionalConformance(FunctionalProfile.FP_READ)
public interface ReadService<R,I> extends BaseService {

	/**
	 * Reads the specified CTS2 Resource.
	 *
	 * @param identifier the identifier
	 * @param readContext the change set context
	 * @return the CTS2 Resource
	 */
	public R read(I identifier, ResolvedReadContext readContext);
	
	/**
	 * Check if the specified CTS2 Resource exists.
	 *
	 * @param identifier the identifier
	 * @param readContext the change set context
	 * @return true, if successful
	 */
	public boolean exists(I identifier, ResolvedReadContext readContext);
	
}
