/*
 * Copyright: (c) 2004-2012 Mayo Foundation for Medical Education and 
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
package edu.mayo.cts2.framework.core.json;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import edu.mayo.cts2.framework.model.Cts2ModelObject;
import edu.mayo.cts2.framework.model.core.TsAnyType;
import edu.mayo.cts2.framework.model.entity.EntityDescription;

/**
 * Responsible for converting CTS2 Model output into JSON.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Component
public class JsonConverter {

	private static String MODEL_PACKAGE = "edu.mayo.cts2.framework.model";
	private static String WSDL_PACKAGE = "edu.mayo.cts2.framework.model.wsdl.*";

	private Gson gson;

	private JsonParser jsonParser = new JsonParser();

	private Map<String, Class<? extends Cts2ModelObject>> classNameCache;

	/**
	 * Instantiates a new json converter.
	 */
	public JsonConverter() {
		super();
		this.gson = this.buildGson();

		this.classNameCache = this.cacheClasses();
	}

	/**
	 * Cache classes.
	 *
	 * @return the map< string, class<? extends cts2 model object>>
	 */
	protected Map<String, Class<? extends Cts2ModelObject>> cacheClasses() {
		Map<String, Class<? extends Cts2ModelObject>> cache = new HashMap<String, Class<? extends Cts2ModelObject>>();

		Reflections reflections = new Reflections(new ConfigurationBuilder()
				.filterInputsBy(
						new FilterBuilder().include(
								"edu.mayo.cts2.framework.model.*").exclude(
								WSDL_PACKAGE)).setUrls(
						ClasspathHelper.forPackage(MODEL_PACKAGE)));

		Set<Class<? extends Cts2ModelObject>> types = reflections
				.getSubTypesOf(Cts2ModelObject.class);

		for (Class<? extends Cts2ModelObject> type : types) {
			String name = StringUtils.uncapitalize(type.getSimpleName());
			cache.put(name, type);
		}

		return cache;
	}

	/**
	 * Convert a CTS2 Model Object to JSON.
	 *
	 * @param cts2Object the cts2 object
	 * @return the string
	 */
	public String toJson(Object cts2Object) {
		JsonElement element = this.gson.toJsonTree(cts2Object);
		JsonObject object = new JsonObject();
		object.add(
			StringUtils.uncapitalize(
					cts2Object.getClass().getSimpleName()), element);

		return object.toString();
	}

	/**
	 * Convert JSON to a CTS2 Model Object.
	 *
	 * @param <T> the generic type
	 * @param json the json
	 * @param clazz the clazz
	 * @return the t
	 */
	public <T> T fromJson(String json, Class<T> clazz) {
		JsonElement element = this.jsonParser.parse(json);

		Set<Entry<String, JsonElement>> entrySet = element.getAsJsonObject()
				.entrySet();

		Assert.isTrue(entrySet.size() == 1);

		T obj = gson.fromJson(entrySet.iterator().next().getValue(), clazz);

		if (obj instanceof EntityDescription) {
			this.setChoiceValue(obj);
		}

		return obj;
	}

	/**
	 * Convert JSON to a CTS2 Model Object.
	 *
	 * @param json the json
	 * @return the object
	 */
	public Object fromJson(String json) {
		Class<?> clazz = this.getJsonClass(json);

		return this.fromJson(json, clazz);
	}

	/**
	 * Gets the json class.
	 *
	 * @param json the json
	 * @return the json class
	 */
	protected Class<?> getJsonClass(String json) {
		JsonElement element = this.jsonParser.parse(json);

		Set<Entry<String, JsonElement>> entrySet = element.getAsJsonObject()
				.entrySet();

		if(entrySet.size() != 1){
            throw new JsonUnmarshallingException("Could not determine the type of the JSON String: " + json);
        }

		return this.classNameCache.get(entrySet.iterator().next().getKey());
	}

	/**
	 * Sets the choice value.
	 *
	 * @param obj the new choice value
	 */
	protected void setChoiceValue(Object obj) {
		try {
			for (Field f : obj.getClass().getDeclaredFields()) {
				f.setAccessible(true);
				Object fieldValue = f.get(obj);
				if (fieldValue != null) {
					Field choiceValue = obj.getClass().getDeclaredField(
							"_choiceValue");
					choiceValue.setAccessible(true);
					choiceValue.set(obj, fieldValue);
					break;
				}
			}
		} catch (SecurityException e) {
			throw new IllegalStateException(e);
		} catch (IllegalArgumentException e) {
			throw new IllegalStateException(e);
		} catch (IllegalAccessException e) {
			throw new IllegalStateException(e);
		} catch (NoSuchFieldException e) {
			throw new IllegalStateException(e);
		}
	}

	/**
	 * Builds the gson.
	 *
	 * @return the gson
	 */
	protected Gson buildGson(){
		GsonBuilder gson = new GsonBuilder();
		
		gson.setExclusionStrategies(new ExclusionStrategy(){

			@Override
			public boolean shouldSkipField(FieldAttributes f) {
				return f.getName().equals("_choiceValue");
			}

			@Override
			public boolean shouldSkipClass(Class<?> clazz) {
				return false;
			}
			
		});
		
		gson.registerTypeAdapter(List.class, new EmptyCollectionSerializer());
		gson.registerTypeAdapter(TsAnyType.class, new TsAnyTypeSerializer());
		
		gson.setFieldNamingStrategy(new FieldNamingStrategy(){

			@Override
			public String translateName(Field field) {
				String fieldName = field.getName();
				
				char[] array = fieldName.toCharArray();

				if(array[0] == '_'){
					array = ArrayUtils.remove(array, 0);
				}
				
				return new String(array);
			}
			
		});
	
		return gson.create();
	}

	/**
	 * The Class EmptyCollectionSerializer.
	 *
	 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
	 */
	public static class EmptyCollectionSerializer implements JsonSerializer<List<?>> {

		/* (non-Javadoc)
		 * @see com.google.gson.JsonSerializer#serialize(java.lang.Object, java.lang.reflect.Type, com.google.gson.JsonSerializationContext)
		 */
		@Override
		public JsonElement serialize(List<?> collection, Type typeOfSrc,
				JsonSerializationContext context) {
			if(CollectionUtils.isNotEmpty(collection)){
				return context.serialize(collection);
			} else {
				return null;
			}
		}

	}
	
	/**
	 * The Class TsAnyTypeSerializer.
	 *
	 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
	 */
	public static class TsAnyTypeSerializer 
		implements JsonSerializer<TsAnyType>, JsonDeserializer<TsAnyType> {

		/* (non-Javadoc)
		 * @see com.google.gson.JsonSerializer#serialize(java.lang.Object, java.lang.reflect.Type, com.google.gson.JsonSerializationContext)
		 */
		@Override
		public JsonElement serialize(TsAnyType tsAnyType, Type typeOfSrc,
				JsonSerializationContext context) {
			if(tsAnyType == null || tsAnyType.getContent() == null){
				return null;
			}
			
			return new JsonPrimitive(tsAnyType.getContent());
		}

		/* (non-Javadoc)
		 * @see com.google.gson.JsonDeserializer#deserialize(com.google.gson.JsonElement, java.lang.reflect.Type, com.google.gson.JsonDeserializationContext)
		 */
		@Override
		public TsAnyType deserialize(JsonElement json, Type typeOfT,
				JsonDeserializationContext context) throws JsonParseException {
			if(json == null){
				return null;
			}
			
			if(! json.isJsonPrimitive()){
				throw new IllegalStateException("TsAnytype is not a JSON Primitive.");
			}
			
			if(json.getAsJsonPrimitive().getAsString() == null){
				return null;
			} else {
				TsAnyType tsAnyType = new TsAnyType();
				tsAnyType.setContent(json.getAsJsonPrimitive().getAsString());
				
				return tsAnyType;
			}
		}

	}

}
