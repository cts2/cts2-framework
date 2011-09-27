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

import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class MappingGsonHttpMessageConverter extends AbstractHttpMessageConverter<Object>{

	public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
	
	private Gson gson;
	
	public MappingGsonHttpMessageConverter() {
		super(new MediaType("application", "json", DEFAULT_CHARSET));
		
		this.gson = this.buildGson();
	}
	
	@Override
	protected boolean supports(Class<?> clazz) {
		return true;
	}

	@Override
	protected Object readInternal(
			Class<? extends Object> clazz,
			HttpInputMessage inputMessage) throws IOException,
			HttpMessageNotReadableException {
		return gson.fromJson(new InputStreamReader(inputMessage.getBody()), clazz);
	}

	@Override
	protected void writeInternal(
			Object t, 
			HttpOutputMessage outputMessage)
			throws IOException, HttpMessageNotWritableException {
		String json = gson.toJson(t);
		
		OutputStreamWriter writer = new OutputStreamWriter(outputMessage.getBody());
		
		writer.write(json);
		
		writer.flush();
	}
	
	protected Gson buildGson(){
		GsonBuilder gson = new GsonBuilder();
		
		gson.setFieldNamingStrategy(new FieldNamingStrategy(){

			@Override
			public String translateName(Field field) {
				char[] array = field.getName().toCharArray();

				array = ArrayUtils.remove(array, 0);
				
				return new String(array);
			}
			
		});
		
		return gson.create();
	}

}
