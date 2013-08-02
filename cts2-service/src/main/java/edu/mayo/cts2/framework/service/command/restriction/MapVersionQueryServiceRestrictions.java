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
package edu.mayo.cts2.framework.service.command.restriction;

import java.util.HashSet;
import java.util.Set;

import edu.mayo.cts2.framework.model.service.core.EntityNameOrURI;
import edu.mayo.cts2.framework.model.service.core.NameOrURI;
import edu.mayo.cts2.framework.model.service.core.types.RestrictionType;
import edu.mayo.cts2.framework.model.service.mapversion.types.MapRole;
import edu.mayo.cts2.framework.model.service.mapversion.types.MapStatus;


/**
 * The Class MapVersionQueryServiceRestrictions.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class MapVersionQueryServiceRestrictions extends MapQueryServiceRestrictions {

	private NameOrURI map;
	
	private EntitiesRestriction entitiesRestriction;

	public EntitiesRestriction getEntitiesRestriction() {
		return entitiesRestriction;
	}

	public void setEntitiesRestriction(EntitiesRestriction entitiesRestriction) {
		this.entitiesRestriction = entitiesRestriction;
	}
	
	public NameOrURI getMap() {
		return map;
	}

	public void setMap(NameOrURI map) {
		this.map = map;
	}

	public static class EntitiesRestriction {
		
		private MapStatus mapStatus;
		
		private MapRole mapRole;
		
		private RestrictionType allOrSome;
		
		public MapStatus getMapStatus() {
			return mapStatus;
		}

		public void setMapStatus(MapStatus mapStatus) {
			this.mapStatus = mapStatus;
		}

		public MapRole getMapRole() {
			return mapRole;
		}

		public void setMapRole(MapRole mapRole) {
			this.mapRole = mapRole;
		}

		private Set<EntityNameOrURI> entities = new HashSet<EntityNameOrURI>();

		public RestrictionType getAllOrSome() {
			return allOrSome;
		}

		public void setAllOrSome(RestrictionType allOrSome) {
			this.allOrSome = allOrSome;
		}

		public Set<EntityNameOrURI> getEntities() {
			return entities;
		}

		public void setEntities(Set<EntityNameOrURI> entities) {
			this.entities = entities;
		}
	}
}
