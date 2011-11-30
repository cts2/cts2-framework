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
package edu.mayo.cts2.framework.model.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import edu.mayo.cts2.framework.model.castor.MarshallSuperClass;
import edu.mayo.cts2.framework.model.codesystemversion.CodeSystemVersionCatalogEntry;
import edu.mayo.cts2.framework.model.core.AbstractResourceDescription;
import edu.mayo.cts2.framework.model.core.Changeable;
import edu.mayo.cts2.framework.model.core.ChangeableElementGroup;
import edu.mayo.cts2.framework.model.core.EntryDescription;
import edu.mayo.cts2.framework.model.core.OpaqueData;
import edu.mayo.cts2.framework.model.core.ResourceDescriptionDirectoryEntry;
import edu.mayo.cts2.framework.model.core.ScopedEntityName;
import edu.mayo.cts2.framework.model.core.TsAnyType;
import edu.mayo.cts2.framework.model.entity.Designation;
import edu.mayo.cts2.framework.model.entity.EntityDescription;
import edu.mayo.cts2.framework.model.entity.EntityDescriptionBase;
import edu.mayo.cts2.framework.model.entity.NamedEntityDescription;
import edu.mayo.cts2.framework.model.entity.types.DesignationRole;
import edu.mayo.cts2.framework.model.service.core.EntityNameOrURI;
import edu.mayo.cts2.framework.model.service.core.NameOrURI;
import edu.mayo.cts2.framework.model.updates.ChangeableResourceChoice;

/**
 * The Class RestModelUtils.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class ModelUtils {
	
	//TODO: This is probably an overly simplistic check
	private static Pattern URI_PATTERN = Pattern.compile(
			"(urn:[a-zA-Z]+:.*)|([a-zA-Z]+://.*)");
 
	/**
	 * Instantiates a new rest model utils.
	 */
	private ModelUtils(){
		super();
	}
	
	/**
	 * To ts any type.
	 *
	 * @param string the string
	 * @return the ts any type
	 */
	public static TsAnyType toTsAnyType(String string){
		TsAnyType ts = new TsAnyType();
		ts.setContent(string);
		
		return ts;
	}
	
	/**
	 * Gets the resource synopsis value.
	 *
	 * @param entry the entry
	 * @return the resource synopsis value
	 */
	public static String getResourceSynopsisValue(AbstractResourceDescription entry){
		EntryDescription synopsis = entry.getResourceSynopsis();
		if(synopsis != null){
			TsAnyType value = synopsis.getValue();
			
			if(value != null){
				return value.getContent();
			}
		}
		
		return null;
	}
	
	/**
	 * Gets the resource synopsis value.
	 *
	 * @param entry the entry
	 * @return the resource synopsis value
	 */
	public static String getResourceSynopsisValue(ResourceDescriptionDirectoryEntry entry){
		EntryDescription synopsis = entry.getResourceSynopsis();
		if(synopsis != null){
			TsAnyType value = synopsis.getValue();
			
			if(value != null){
				return value.getContent();
			}
		}
		
		return null;
	}
	
	/**
	 * Creates the scoped entity name.
	 *
	 * @param name the name
	 * @param namespace the namespace
	 * @return the scoped entity name
	 */
	public static ScopedEntityName createScopedEntityName(String name, String namespace){
		ScopedEntityName scopedName = new ScopedEntityName();
		scopedName.setName(name);
		scopedName.setNamespace(namespace);
		
		return scopedName;
	}
	
	/**
	 * Creates the opaque data.
	 *
	 * @param text the text
	 * @return the opaque data
	 */
	public static OpaqueData createOpaqueData(String text){
		OpaqueData data = new OpaqueData();
		data.setValue(toTsAnyType(text));
		
		return data;
	}
	
	/**
	 * Gets the entity.
	 *
	 * @param entityDescription the entity description
	 * @return the entity
	 */
	public static EntityDescriptionBase getEntity(EntityDescription entityDescription){
		return (EntityDescriptionBase) entityDescription.getChoiceValue();
	}
	
	public static ChangeableElementGroup getChangeableElementGroup(ChangeableResourceChoice changeableResource){
		return doWithChangeableResourceChoice(changeableResource, 
				new ChoiceCallback<ChangeableElementGroup>(){

					@Override
					public ChangeableElementGroup doInCallback(
							Changeable changeable) {
						return changeable.getChangeableElementGroup();
					}

					@Override
					public ChangeableElementGroup doInCallback(
							NamedEntityDescription changeable) {
						return changeable.getChangeableElementGroup();
					}
			
		});
		
	}
	
	public static ChangeableElementGroup getChangeableElementGroup(Object changeableResource){
		if(changeableResource instanceof Changeable){
			return ((Changeable)changeableResource).getChangeableElementGroup();
		}
		
		if(changeableResource instanceof EntityDescription){
			EntityDescription entity = (EntityDescription)changeableResource;
			
			EntityDescriptionBase base = getEntity(entity);
			
			if(base instanceof NamedEntityDescription){
				return ((NamedEntityDescription)base).getChangeableElementGroup();
			}
			
		}
		
		throw new IllegalStateException("Cannot find ChangeableElementGroup.");
	}
	
	private static interface ChoiceCallback<T> {
		
		public T doInCallback(Changeable changeable);
		
		public T doInCallback(NamedEntityDescription changeable);
	}
	
	private static <T> T  
		doWithChangeableResourceChoice(
				ChangeableResourceChoice changeableResource, ChoiceCallback<T> callback){

		Object obj = changeableResource.getChoiceValue();
			
		if(obj instanceof Changeable){
			return callback.doInCallback((Changeable)obj);
		}
		
		if(obj instanceof EntityDescription){
			EntityDescription entity = (EntityDescription)obj;
			
			EntityDescriptionBase base = getEntity(entity);
			
			if(base instanceof NamedEntityDescription){
				return callback.doInCallback((NamedEntityDescription)base);
			}
			
		}
			
		throw new IllegalStateException("Cannot find ChangeableElementGroup.");
	}

	/**
	 * Sets the entity.
	 *
	 * @param wrapper the wrapper
	 * @param entityDescription the entity description
	 */
	public static void setEntity(EntityDescription wrapper,
			EntityDescriptionBase entityDescription) {
		try {
			for(Method method : EntityDescription.class.getDeclaredMethods()){
				if(method.getName().startsWith("set") && 
						method.getParameterTypes().length == 1 &&
						method.getParameterTypes()[0].equals(entityDescription.getClass())){
					method.invoke(wrapper, entityDescription);
				}
			}
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}
	
	/**
	 * To entity description.
	 *
	 * @param entityDescriptionBase the entity description base
	 * @return the entity description
	 */
	public static EntityDescription toEntityDescription(
			EntityDescriptionBase entityDescriptionBase) {
		EntityDescription wrapper = new EntityDescription();
		
		setEntity(wrapper, entityDescriptionBase);
		
		return wrapper;
	}
	
	/**
	 * Gets the preferred designation.
	 *
	 * @param entity the entity
	 * @return the preferred designation
	 */
	public static Designation getPreferredDesignation(EntityDescriptionBase entity){
		if(entity.getDesignationCount() == 1){
			return entity.getDesignation(0);
		}
		
		for(Designation designation : entity.getDesignation()){
			DesignationRole role = designation.getDesignationRole();
			if(role != null && role.equals(DesignationRole.PREFERRED)){
				return designation;
			}
		}
		
		return null;
	}

	/**
	 * Gets the code system name of code system version.
	 *
	 * @param resource the resource
	 * @return the code system name of code system version
	 */
	public static String getCodeSystemNameOfCodeSystemVersion(
			CodeSystemVersionCatalogEntry resource) {
		return resource.getVersionOf().getContent();
	}
	
	public static boolean isValidUri(String uriCandidate){
		return URI_PATTERN.matcher(uriCandidate).matches();	
	}
	
	public static NameOrURI nameOrUriFromEither(String nameOrUri) {
		NameOrURI n;
		if(isValidUri(nameOrUri)){
			n = nameOrUriFromUri(nameOrUri);
		} else {
			n = nameOrUriFromName(nameOrUri);
		}
		return n;
	}

	public static NameOrURI nameOrUriFromName(String name) {
		NameOrURI nameOrUri = new ToStringNameOrURI();
		nameOrUri.setName(name);
		
		return nameOrUri;
	}
	
	public static EntityNameOrURI entityNameOrUriFromName(ScopedEntityName name) {
		EntityNameOrURI nameOrUri = new ToStringEntityNameOrURI();
		nameOrUri.setEntityName(name);
		
		return nameOrUri;
	}
	
	public static EntityNameOrURI entityNameOrUriFromUri(String uri) {
		EntityNameOrURI nameOrUri = new ToStringEntityNameOrURI();
		nameOrUri.setUri(uri);
		
		return nameOrUri;
	}
	
	public static ChangeableResourceChoice createChangeableResourceChoice(Object changeable) {
		ChangeableResourceChoice choice = new ChangeableResourceChoice();
		
		for(Field field : choice.getClass().getDeclaredFields()){
			if(field.getType().equals(changeable.getClass())
					||
					field.getName().equals("_choiceValue")){
				field.setAccessible(true);
				try {
					field.set(choice, changeable);
				} catch (IllegalArgumentException e) {
					throw new IllegalStateException(e);
				} catch (IllegalAccessException e) {
					throw new IllegalStateException(e);
				}
			}
		}
		
		return choice;
	}


	public static NameOrURI nameOrUriFromUri(String uri) {
		NameOrURI nameOrUri = new ToStringNameOrURI();
		nameOrUri.setUri(uri);
		
		return nameOrUri;
	}
	
	public static void setChangeableElementGroup(Object changeableResource, final ChangeableElementGroup group){
		if(changeableResource instanceof Changeable){
			((Changeable)changeableResource).setChangeableElementGroup(group);
			
			return;
		}
		
		if(changeableResource instanceof EntityDescription){
			EntityDescription entity = (EntityDescription)changeableResource;
			
			EntityDescriptionBase base = getEntity(entity);
			
			if(base instanceof NamedEntityDescription){
				((NamedEntityDescription)base).setChangeableElementGroup(group);
				
				return;
			}
			
		}
		
		throw new IllegalStateException("Cannot find ChangeableElementGroup.");
	}

	public static void setChangeableElementGroup(
			ChangeableResourceChoice changeableResource, final ChangeableElementGroup group) {
		
		doWithChangeableResourceChoice(changeableResource, 
				new ChoiceCallback<Void>(){

					@Override
					public Void doInCallback(
							Changeable changeable) {
						changeable.setChangeableElementGroup(group);
						
						return null;
					}

					@Override
					public Void doInCallback(
							NamedEntityDescription changeable) {
						changeable.setChangeableElementGroup(group);
						
						return null;
					}
			
		});
	}
	
	private static class ToStringNameOrURI extends NameOrURI implements MarshallSuperClass {

		private static final long serialVersionUID = 1917212561253120049L;

		public String toString(){
			String returnString;
			if(StringUtils.isNotBlank(this.getName())){
				returnString = "Name: " + this.getName();
			} else {
				returnString = "URI: " + this.getUri();
			}
			
			return returnString;
		}
	}
	
	private static class ToStringEntityNameOrURI extends EntityNameOrURI implements MarshallSuperClass {

		private static final long serialVersionUID = 1917212561253120049L;

		public String toString(){
			String returnString;
			if(this.getEntityName() != null){
				returnString = 
						"Namespace: " + this.getEntityName().getNamespace() +
						"Name: " + this.getEntityName().getName();
			} else {
				returnString = "URI: " + this.getUri();
			}
			
			return returnString;
		}
	}
}
