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
package edu.mayo.cts2.framework.service.profile.mapentry.name;

import edu.mayo.cts2.framework.model.core.ScopedEntityName;
import edu.mayo.cts2.framework.model.service.core.EntityNameOrURI;
import edu.mayo.cts2.framework.model.service.core.NameOrURI;
import edu.mayo.cts2.framework.model.util.ModelUtils;

/**
 * The Class MapEntryId.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class MapEntryReadId extends EntityNameOrURI {

	private static final long serialVersionUID = 2567172778896717738L;

	private NameOrURI mapVersion;
	
	public MapEntryReadId(String uri, NameOrURI mapVersion) {
		super();
		this.setUri(uri);
		this.mapVersion = mapVersion;
	}
	
	public MapEntryReadId(EntityNameOrURI mapFromName, NameOrURI mapVersion) {
		super();
		this.setEntityName(mapFromName.getEntityName());
		this.setUri(mapFromName.getUri());
		this.mapVersion = mapVersion;
	}
	
	public MapEntryReadId(ScopedEntityName mapFromName, NameOrURI mapVersion) {
		super();
		this.setEntityName(mapFromName);
		this.mapVersion = mapVersion;
	}
	
	public MapEntryReadId(String mapFromName, String mapFromNamespace, NameOrURI mapVersion) {
		this(ModelUtils.createScopedEntityName(mapFromName, mapFromNamespace), mapVersion);
	}

	public NameOrURI getMapVersion() {
		return mapVersion;
	}
}
