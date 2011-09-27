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
package edu.mayo.cts2.framework.core;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import com.google.common.util.concurrent.SimpleTimeLimiter;

/**
 * The Class ExecutionUtils.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class ExecutionUtils {

	private static final SimpleTimeLimiter TIME_LIMITER = new SimpleTimeLimiter();

	/**
	 * Instantiates a new execution utils.
	 */
	private ExecutionUtils() {
		super();
	}

	/**
	 * Call with timeout.
	 *
	 * @param <T> the generic type
	 * @param callable the callable
	 * @param timeoutDuration the timeout duration
	 * @param timeoutUnit the timeout unit
	 * @return the t
	 */
	public static <T> T callWithTimeout(Callable<T> callable, long timeoutDuration, TimeUnit timeoutUnit){
		
		try {
			return TIME_LIMITER.callWithTimeout(callable, timeoutDuration, timeoutUnit, false);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
}
