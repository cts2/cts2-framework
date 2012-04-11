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

import edu.mayo.cts2.framework.model.core.IsChangeable;


/**
 * The BaseMaintenanceService exposes basic Create, Read, Update and Delete (CRUD)
 * functionality.
 * 
 * Note that is is acceptable for the service to return a Resource of a different type
 * during the Create request. This is to facilitate cases where the service may add extra
 * metadata to the Resource upon create (such as an auto-generated id, etc).
 *
 * @param <T> The Resouce Type to be input to the Service in an Update request, and 
 * output in a Create request
 * @param <R> The Resource Type supplied by the client to the Create request.
 * @param <I> The Identifier that will uniquely define the CTS2 REsource
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface BaseMaintenanceService<
	T extends IsChangeable, 
	R extends IsChangeable,
	I> extends BaseService {

	/**
	 * Updates only the Resource's ChangeableMetadata
	 *
	 * @param identifier the identifier
	 * @param request the request
	 */
	public void updateChangeableMetadata(I identifier, UpdateChangeableMetadataRequest request);
	
	/**
	 * Updates the supplied Resource. It is assumed that the supplied Resource will carry
	 * enough identity to uniquely identify it within the service.
	 *
	 * @param resource the resource
	 */
	public void updateResource(T resource);
	
	/**
	 * Creates and persists the resource into the service.
	 * Note that is is acceptable for the service to return a Resource of a different type
	 * during the Create request. This is to facilitate cases where the service may add extra
	 * metadata to the Resource upon create (such as an auto-generated id, etc).
	 *
	 * @param resource the resource
	 * @return The actual resource created and persisted by the service.
	 */
	public T createResource(R resource);
	
	/**
	 * Deletes the Resource identified by the identifier in the ChangeSet identified by the
	 * provided changeSetUri.
	 *
	 * @param identifier the identifier
	 * @param changeSetUri the change set uri
	 */
	public void deleteResource(I identifier, String changeSetUri);
	
}
