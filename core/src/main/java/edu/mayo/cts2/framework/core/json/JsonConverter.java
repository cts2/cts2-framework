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
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import edu.mayo.cts2.framework.model.Cts2Model;
import edu.mayo.cts2.framework.model.entity.EntityDescription;

@Component
public class JsonConverter {

	private static String MODEL_PACKAGE = "edu.mayo.cts2.framework.model";
	private static String WSDL_PACKAGE = "edu.mayo.cts2.framework.model.wsdl.*";

	private Gson gson;

	private JsonParser jsonParser = new JsonParser();

	private Map<String, Class<? extends Cts2Model>> classNameCache;

	public JsonConverter() {
		super();
		this.gson = this.buildGson();

		this.classNameCache = this.cacheClasses();
	}

	protected Map<String, Class<? extends Cts2Model>> cacheClasses() {
		Map<String, Class<? extends Cts2Model>> cache = new HashMap<String, Class<? extends Cts2Model>>();

		Reflections reflections = new Reflections(new ConfigurationBuilder()
				.filterInputsBy(
						new FilterBuilder().include(
								"edu.mayo.cts2.framework.model.*").exclude(
								WSDL_PACKAGE)).setUrls(
						ClasspathHelper.forPackage(MODEL_PACKAGE)));

		Set<Class<? extends Cts2Model>> types = reflections
				.getSubTypesOf(Cts2Model.class);

		for (Class<? extends Cts2Model> type : types) {
			String name = type.getSimpleName();
			cache.put(name, type);
		}

		return cache;
	}

	public String toJson(Object cts2Object) {
		JsonElement element = this.gson.toJsonTree(cts2Object);
		JsonObject object = new JsonObject();
		object.add(cts2Object.getClass().getSimpleName(), element);

		return object.toString();
	}

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

	public Object fromJson(String json) {
		Class<?> clazz = this.getJsonClass(json);

		return this.fromJson(json, clazz);
	}

	protected Class<?> getJsonClass(String json) {
		JsonElement element = this.jsonParser.parse(json);

		Set<Entry<String, JsonElement>> entrySet = element.getAsJsonObject()
				.entrySet();

		Assert.isTrue(entrySet.size() == 1);

		return this.classNameCache.get(entrySet.iterator().next().getKey());
	}

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

	public static class EmptyCollectionSerializer implements JsonSerializer<List<?>> {

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

}
