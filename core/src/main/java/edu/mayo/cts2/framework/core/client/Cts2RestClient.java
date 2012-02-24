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
package edu.mayo.cts2.framework.core.client;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.CommonsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.xml.MarshallingHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import edu.mayo.cts2.framework.core.xml.Cts2Marshaller;
import edu.mayo.cts2.framework.core.xml.DelegatingMarshaller;

/**
 * The Class Cts2RestClient.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@SuppressWarnings("deprecation")
public class Cts2RestClient {
	
	private static Cts2RestClient instance;
	
	private RestTemplate template;
	
	private Cts2Marshaller marshaller;
	private HttpMessageConverter<Object> converter;
	
	/**
	 * Instance.
	 *
	 * @return the cts2 rest client
	 */
	@Deprecated
	public static synchronized Cts2RestClient instance(){
		if(instance == null){
			try {
				instance = new Cts2RestClient();
			} catch (Exception e) {
				throw new IllegalStateException(e);
			}
		}
		return instance;
	}

	/**
	 * Instantiates a new cts2 rest client.
	 *
	 * @throws Exception the exception
	 */
	private Cts2RestClient() throws Exception {
		this(new DelegatingMarshaller());
	}
	
	public Cts2RestClient(Cts2Marshaller marshaller) throws Exception {
		this.marshaller = marshaller;
		this.converter = new MarshallingHttpMessageConverter(marshaller);
		this.template = createRestTemplate(null);
	}

	/**
	 * Creates the rest template.
	 *
	 * @param requestFactory the request factory
	 * @return the rest template
	 */
	protected RestTemplate createRestTemplate(ClientHttpRequestFactory requestFactory) {
		RestTemplate restTemplate;
		if(requestFactory != null){
			restTemplate = new RestTemplate(requestFactory);
		} else {
			restTemplate = new RestTemplate();
		}
		
		List<HttpMessageConverter<?>> list = new ArrayList<HttpMessageConverter<?>>();
		
		list.add(converter);
		
		restTemplate.setMessageConverters(list);
		
		return restTemplate;
	}
	
	/**
	 * Gets the cts2 resource.
	 *
	 * @param <T> the generic type
	 * @param url the url
	 * @param clazz the clazz
	 * @return the cts2 resource
	 */
	public <T> T getCts2Resource(String url, Class<T> clazz){
		return this.template.getForObject(url, clazz);
	}
	
	/**
	 * Put cts2 resource.
	 *
	 * @param url the url
	 * @param cts2Resource the cts2 resource
	 */
	public void putCts2Resource(String url, Object cts2Resource){
		this.template.put(url, cts2Resource);
	}
	
	public URI postCts2Resource(String url, Object cts2Resource){
		return this.template.postForLocation(url, cts2Resource);
	}
	
	public void deleteCts2Resource(String url){
		this.template.delete(url);
	}
	
	/**
	 * Put cts2 resource.
	 *
	 * @param url the url
	 * @param cts2Resource the cts2 resource
	 * @param username the username
	 * @param password the password
	 */
	public void putCts2Resource(String url, Object cts2Resource, String username, String password){
		HttpClient client = new HttpClient();
		UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(username,password);
		client.getState().setCredentials(AuthScope.ANY, credentials);
		CommonsClientHttpRequestFactory commons = new CommonsClientHttpRequestFactory(client);

		
		this.createRestTemplate(commons).put(url, cts2Resource);
	}

	public Cts2Marshaller getMarshaller() {
		return marshaller;
	}

	public void setMarshaller(Cts2Marshaller marshaller) {
		this.marshaller = marshaller;
	}

}
