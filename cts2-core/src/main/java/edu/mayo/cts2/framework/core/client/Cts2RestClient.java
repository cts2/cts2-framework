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

import edu.mayo.cts2.framework.core.xml.Cts2Marshaller;
import edu.mayo.cts2.framework.core.xml.DelegatingMarshaller;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.CommonsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.xml.MarshallingHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.net.URI;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

/**
 * A client for interacting with a CTS2 REST service.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@SuppressWarnings("deprecation")
public class Cts2RestClient {
	
	public static int connectTimeoutMS = 5000;
	public static int readTimeoutMS = 30000;
	
	private static Cts2RestClient instance;
	
	private RestTemplate nonSecureTemplate;
	
	private Cts2Marshaller marshaller;
	
	private HttpMessageConverter<Object> converter;
	
	/**
	 * Instance.
	 *
	 * @return the cts2 rest client
	 */
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
	 */
	private Cts2RestClient() {
		this(new DelegatingMarshaller(), true);
	}
	
	/**
	 * Instantiates a new cts2 rest client.
	 *
	 * @param trustSelfSignedSsl the trust self signed ssl
	 */
	public Cts2RestClient(boolean trustSelfSignedSsl) {
		this(new DelegatingMarshaller(), trustSelfSignedSsl);
	}
	
	/**
	 * Instantiates a new cts2 rest client.
	 *
	 * @param marshaller the marshaller
	 * @param trustSelfSignedSsl the trust self signed ssl
	 */
	public Cts2RestClient(Cts2Marshaller marshaller, boolean trustSelfSignedSsl) {
		this.marshaller = marshaller;
		this.converter = new MarshallingHttpMessageConverter(marshaller);
		
		if(trustSelfSignedSsl){
			trustSelfSignedSSL();
		}
		this.nonSecureTemplate = this.createRestTemplate(null,null);
	}

	
	/**
	 * Gets the rest template.
	 *
	 * @param username the username
	 * @param password the password
	 * @return the rest template
	 */
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
	 * @param username the username
	 * @param password the password
	 * @return the rest template
	 */
	protected RestTemplate createRestTemplate(String username, String password) {
		RestTemplate restTemplate;
		if(username != null && password != null){
			restTemplate = new FixedRestTemplate(this.createSecureTransport(username, password));
		} else {
			restTemplate = new FixedRestTemplate();
		}
		
		SimpleClientHttpRequestFactory rf = (SimpleClientHttpRequestFactory)restTemplate.getRequestFactory();
		rf.setConnectTimeout(connectTimeoutMS);
		rf.setReadTimeout(readTimeoutMS);
		
		List<HttpMessageConverter<?>> list = new ArrayList<HttpMessageConverter<?>>();
		
		list.add(converter);
		
		restTemplate.setMessageConverters(list);
		
		return restTemplate;
	}

	/**
	 * Perform an HTTP 'GET' of the CTS2 resource.
	 *
	 * @param url the url
	 * @param clazz the clazz
	 * @return the cts2 resource
	 */
	public <T> T getCts2Resource(String url, Class<T> clazz){
		return this.getCts2Resource(url, null, null, clazz);
	}
	
	/**
	 * Perform an HTTP 'GET' of the CTS2 resource.
	 *
	 * @param url the url
	 * @param username the username
	 * @param password the password
	 * @param clazz the clazz
	 * @return the cts2 resource
	 */
	public <T> T getCts2Resource(String url, String username, String password, Class<T> clazz){
		return this.getCts2Resource(url, username, password, clazz, new String[0]);
	}

    /**
     * Perform an HTTP 'HEAD' of the CTS2 resource.
     *
     * @param url the url
     * @param username the username
     * @param password the password
     * @return the cts2 resource
     */
    public HttpHeaders headCts2Resource(String url, String username, String password, String... queryParameters){
        return this.doGetRestTemplate(username, password).headForHeaders(url, queryParameters);
    }
	
	/**
	 * Perform an HTTP 'GET' of the CTS2 resource.
	 *
	 * @param url the url
	 * @param username the username
	 * @param password the password
	 * @param clazz the clazz
	 * @param queryParameters the query parameters
	 * @return the cts2 resource
	 */
	public <T> T getCts2Resource(String url, String username, String password, Class<T> clazz, String... queryParameters){
		return this.doGetRestTemplate(username, password).getForObject(url, clazz, (Object[])queryParameters);
	}

	/**
	 * Perform an HTTP 'POST' of the CTS2 resource.
	 *
	 * @param url the url
	 * @param cts2Resource the cts2 resource
	 * @return the uri
	 */
	public URI postCts2Resource(String url, Object cts2Resource){
		return this.postCts2Resource(url, null, null, cts2Resource);
	}
	
	/**
	 * Perform an HTTP 'POST' of the CTS2 resource.
	 *
	 * @param url the url
	 * @param username the username
	 * @param password the password
	 * @param cts2Resource the cts2 resource
	 * @return the uri
	 */
	public URI postCts2Resource(String url, String username, String password, Object cts2Resource){
		return this.doGetRestTemplate(username, password).postForLocation(url, cts2Resource);
	}
	
	/**
	 * Perform an HTTP 'DELETE' of the CTS2 resource.
	 *
	 * @param url the url
	 */
	public void deleteCts2Resource(String url){
		this.deleteCts2Resource(null, null, url);
	}
	
	/**
	 * Perform an HTTP 'DELETE' of the CTS2 resource.
	 *
	 * @param username the username
	 * @param password the password
	 * @param url the url
	 */
	public void deleteCts2Resource(String username, String password, String url){
		this.doGetRestTemplate(username, password).delete(url);
	}

	/**
	 * Perform an HTTP 'PUT' of the CTS2 resource.
	 *
	 * @param url the url
	 * @param cts2Resource the cts2 resource
	 */
	public void putCts2Resource(String url, Object cts2Resource){
		this.putCts2Resource(url, null, null, cts2Resource);
	}
	
	/**
	 * Perform an HTTP 'PUT' of the CTS2 resource.
	 *
	 * @param url the url
	 * @param username the username
	 * @param password the password
	 * @param cts2Resource the cts2 resource
	 */
	public void putCts2Resource(String url, String username, String password, Object cts2Resource){
		this.doGetRestTemplate(username,password).put(url, cts2Resource);
	}
	
	/**
	 * Creates the secure transport.
	 *
	 * @param username the username
	 * @param password the password
	 * @return the client http request factory
	 */
	protected ClientHttpRequestFactory createSecureTransport(String username, String password){
		HttpClient client = new HttpClient();
		UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(username,password);
		client.getState().setCredentials(AuthScope.ANY, credentials);
		CommonsClientHttpRequestFactory commons = new CommonsClientHttpRequestFactory(client);

		return commons;
	}
	
	/**
	 * Enable trust for a self signed ssl.
	 */
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
