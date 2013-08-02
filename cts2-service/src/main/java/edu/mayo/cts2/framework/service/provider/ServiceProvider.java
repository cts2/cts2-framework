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
package edu.mayo.cts2.framework.service.provider;

import edu.mayo.cts2.framework.service.profile.Cts2Profile;

/**
 * The ServiceProvider is the coupling point between the CTS2
 * Development Framework and the individual Service Plugins. This interface
 * allows the framework to request an implementation of a specific CTS2 Service
 * from the plugin. 
 * 
 * The service will request plugins by passing in a CTS2 Service interface class.
 * It is the responsibility of the plugin to either:
 * 1) Return an implementation of that interface.
 * or
 * 2) Return a NULL, indicating no such service is available.
 * 
 * Each CTS2 Service Plugin is required to implement this interface once and only once.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface ServiceProvider {
	
	/**
	 * Gets the requested CTS2 Service. If this service has not been implemented,
	 * or is otherwise unavailable, return a 'NULL' value.
	 *
	 * @param <T> the generic type
	 * @param serviceClass the service class
	 *
	 * @return the service, or a NULL if not implemented or unavailable
	 */
	public <T extends Cts2Profile> T getService(Class<T> serviceClass);
	
}
