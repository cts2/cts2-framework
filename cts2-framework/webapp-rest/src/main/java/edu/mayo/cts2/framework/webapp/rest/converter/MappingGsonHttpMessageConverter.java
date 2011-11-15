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
package edu.mayo.cts2.framework.webapp.rest.converter;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.nio.charset.Charset;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import edu.mayo.cts2.framework.model.entity.EntityDescription;

/**
 * The Class MappingGsonHttpMessageConverter.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class MappingGsonHttpMessageConverter extends AbstractHttpMessageConverter<Object>{

	public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
	
	private Gson gson;
	
	/**
	 * Instantiates a new mapping gson http message converter.
	 */
	public MappingGsonHttpMessageConverter() {
		super(new MediaType("application", "json", DEFAULT_CHARSET));
		
		this.gson = this.buildGson();
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.http.converter.AbstractHttpMessageConverter#supports(java.lang.Class)
	 */
	@Override
	protected boolean supports(Class<?> clazz) {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.springframework.http.converter.AbstractHttpMessageConverter#readInternal(java.lang.Class, org.springframework.http.HttpInputMessage)
	 */
	@Override
	protected Object readInternal(
			Class<? extends Object> clazz,
			HttpInputMessage inputMessage) throws IOException,
			HttpMessageNotReadableException {
		
		Object obj = 
				gson.fromJson(new InputStreamReader(inputMessage.getBody()), clazz);
		
		if(obj instanceof EntityDescription){
			this.setChoiceValue(obj);
		}
		
		return obj;
	}
	
	protected void setChoiceValue(Object obj){
		try {
			for(Field f : obj.getClass().getDeclaredFields()){
				f.setAccessible(true);
				Object fieldValue = f.get(obj);
				if(fieldValue!= null){
					Field choiceValue = obj.getClass().getDeclaredField("_choiceValue");
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

	/* (non-Javadoc)
	 * @see org.springframework.http.converter.AbstractHttpMessageConverter#writeInternal(java.lang.Object, org.springframework.http.HttpOutputMessage)
	 */
	@Override
	protected void writeInternal(
			Object t, 
			HttpOutputMessage outputMessage)
			throws IOException, HttpMessageNotWritableException {
		String json = gson.toJson(t);
		
		OutputStreamWriter writer = null;
		try {
			writer = new OutputStreamWriter(outputMessage.getBody());
		
			writer.write(json);
		
			writer.flush();
		} finally {
			if(writer != null){
				writer.close();
			}
		}
	}
	
	/**
	 * Builds the gson.
	 *
	 * @return the gson
	 */
	protected Gson buildGson(){
		GsonBuilder gson = new GsonBuilder();
		
		gson = gson.disableHtmlEscaping();
		
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
		
		gson.setFieldNamingStrategy(new FieldNamingStrategy(){

			@Override
			public String translateName(Field field) {
				char[] array = field.getName().toCharArray();

				if(array[0] == '_'){
					array = ArrayUtils.remove(array, 0);
				}
				
				return new String(array);
			}
			
		});
		
		return gson.create();
	}

}
