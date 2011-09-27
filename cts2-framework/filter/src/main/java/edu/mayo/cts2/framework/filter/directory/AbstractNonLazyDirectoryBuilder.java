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

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.Iterables;

import edu.mayo.cts2.framework.model.directory.DirectoryResult;
import edu.mayo.cts2.framework.model.core.types.SetOperator;

/**
 * The Class AbstractNonLazyDirectoryBuilder.
 *
 * @param <T> the generic type
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractNonLazyDirectoryBuilder<T> extends AbstractDirectoryBuilder<T> {

	/**
	 * Process set operation.
	 *
	 * @param i1 the i1
	 * @param i2 the i2
	 * @param setOperator the set operator
	 * @return the collection
	 */
	protected Collection<T> processSetOperation(final DirectoryBuilder<T> i1,
			final DirectoryBuilder<T> i2, final SetOperator setOperator) {

		Set<T> set1 = new HashSet<T>();
		Set<T> set2 = new HashSet<T>();

		DirectoryResult<T> result1 = i1.resolve();
		DirectoryResult<T> result2 = i2.resolve();

		Iterables.addAll(set1, result1.getEntries());

		Iterables.addAll(set2, result2.getEntries());

		switch (setOperator) {
			case UNION: {
				set1.addAll(set2);
				return set1;
			}
			case INTERSECT: {
				set1.retainAll(set2);
				return set1;
			}
			case SUBTRACT: {
				set1.removeAll(set2);
				return set1;
			}
		}
		
		return set1;
	}
}
