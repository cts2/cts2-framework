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

import edu.mayo.cts2.framework.core.xml.Cts2Marshaller;
import edu.mayo.cts2.framework.core.xml.Cts2v10MarshallerDecorator;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class Cts2v10XmlHttpMessageConverter extends AbstractHttpMessageConverter<Object> {

	public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    public static final String VERSION_PARAMETER = "version";

    public static final String CTS2_10 = "1.0";

    public static final Map<String,String> CTS2_10_PARAMETER = new HashMap<String,String>();
    static {
        CTS2_10_PARAMETER.put(VERSION_PARAMETER, CTS2_10);
    }

    private Cts2Marshaller v10Cts2Marshaller;

	/**
	 * Instantiates a new mapping gson http message converter.
	 */
	public Cts2v10XmlHttpMessageConverter() {
		super(new MediaType(new MediaType("application", "xml", DEFAULT_CHARSET), CTS2_10_PARAMETER));
	}

    public void setCts2Marshaller(Cts2Marshaller cts2Marshaller) {
        this.v10Cts2Marshaller = new Cts2v10MarshallerDecorator(cts2Marshaller);
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
        throw new UnsupportedOperationException("Cannot accept CTS2 1.0 XML.");
	}

	/* (non-Javadoc)
	 * @see org.springframework.http.converter.AbstractHttpMessageConverter#writeInternal(java.lang.Object, org.springframework.http.HttpOutputMessage)
	 */
	@Override
	protected void writeInternal(
			Object t, 
			HttpOutputMessage outputMessage)
			throws IOException, HttpMessageNotWritableException {

		this.v10Cts2Marshaller.marshal(t, new StreamResult(outputMessage.getBody()));
	}

}
