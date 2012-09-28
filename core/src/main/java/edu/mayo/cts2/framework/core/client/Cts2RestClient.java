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
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

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
	
	private RestTemplate nonSecureTemplate;
	
	private Cts2Marshaller marshaller;
	private HttpMessageConverter<Object> converter;
	
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
	private Cts2RestClient() {
		this(new DelegatingMarshaller(), true);
	}
	
	public Cts2RestClient(boolean trustSelfSignedSsl) {
		this(new DelegatingMarshaller(), trustSelfSignedSsl);
	}
	
	public Cts2RestClient(Cts2Marshaller marshaller, boolean trustSelfSignedSsl) {
		this.marshaller = marshaller;
		this.converter = new MarshallingHttpMessageConverter(marshaller);
		
		if(trustSelfSignedSsl){
			trustSelfSignedSSL();
		}
		this.nonSecureTemplate = this.doGetRestTemplate();
	}

	protected RestTemplate doGetRestTemplate() {
		return this.doGetRestTemplate(null, null);
	}
	
	protected final RestTemplate doGetRestTemplate(String username, String password) {
		if(username != null && password != null){
			return this.createRestTemplate(username, password);
		} else {
			return this.nonSecureTemplate;
		}
	}
	
	/**
	 * Creates the rest template.
	 *
	 * @param requestFactory the request factory
	 * @return the rest template
	 */
	protected RestTemplate createRestTemplate(String username, String password) {
		RestTemplate restTemplate;
		if(username != null && password != null){
			restTemplate = new RestTemplate(this.createSecureTransport(username, password));
		} else {
			restTemplate = new RestTemplate();
		}
		
		List<HttpMessageConverter<?>> list = new ArrayList<HttpMessageConverter<?>>();
		
		list.add(converter);
		
		restTemplate.setMessageConverters(list);
		
		return restTemplate;
	}

	public <T> T getCts2Resource(String url, Class<T> clazz){
		return this.getCts2Resource(url, null, null, clazz);
	}
	
	public <T> T getCts2Resource(String url, String username, String password, Class<T> clazz){
		return this.getCts2Resource(url, username, password, clazz, new String[0]);
	}
	
	public <T> T getCts2Resource(String url, String username, String password, Class<T> clazz, String... queryParameters){
		return this.doGetRestTemplate(username, password).getForObject(url, clazz, (Object[])queryParameters);
	}

	public URI postCts2Resource(String url, Object cts2Resource){
		return this.postCts2Resource(url, null, null, cts2Resource);
	}
	
	public URI postCts2Resource(String url, String username, String password, Object cts2Resource){
		return this.doGetRestTemplate().postForLocation(url, cts2Resource);
	}
	
	public void deleteCts2Resource(String url){
		this.deleteCts2Resource(null, null, url);
	}
	
	public void deleteCts2Resource(String username, String password, String url){
		this.doGetRestTemplate(username, password).delete(url);
	}

	public void putCts2Resource(String url, Object cts2Resource){
		this.putCts2Resource(url, null, null, cts2Resource);
	}
	
	public void putCts2Resource(String url, String username, String password, Object cts2Resource){
		this.doGetRestTemplate(username,password).put(url, cts2Resource);
	}
	
	protected ClientHttpRequestFactory createSecureTransport(String username, String password){
		HttpClient client = new HttpClient();
		UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(username,password);
		client.getState().setCredentials(AuthScope.ANY, credentials);
		CommonsClientHttpRequestFactory commons = new CommonsClientHttpRequestFactory(client);

		return commons;
	}
	
	protected void trustSelfSignedSSL() {
		try {
			SSLContext ctx = SSLContext.getInstance("TLS");
			X509TrustManager tm = new X509TrustManager() {

				public void checkClientTrusted(X509Certificate[] xcs,
						String string) throws CertificateException {
				}

				public void checkServerTrusted(X509Certificate[] xcs,
						String string) throws CertificateException {
				}

				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
			};
			ctx.init(null, new TrustManager[] { tm }, null);
			SSLContext.setDefault(ctx);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public Cts2Marshaller getMarshaller() {
		return marshaller;
	}

	public void setMarshaller(Cts2Marshaller marshaller) {
		this.marshaller = marshaller;
	}

}
