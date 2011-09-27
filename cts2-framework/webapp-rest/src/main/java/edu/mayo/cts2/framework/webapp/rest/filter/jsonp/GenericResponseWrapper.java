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
package edu.mayo.cts2.framework.webapp.rest.filter.jsonp;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * The Class GenericResponseWrapper.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class GenericResponseWrapper extends HttpServletResponseWrapper {

	private ByteArrayOutputStream output;
	private int contentLength;
	private String contentType;

	/**
	 * Instantiates a new generic response wrapper.
	 *
	 * @param response the response
	 */
	public GenericResponseWrapper(HttpServletResponse response) {
		super(response);

		output = new ByteArrayOutputStream();
	}

	/**
	 * Gets the data.
	 *
	 * @return the data
	 */
	public byte[] getData() {
		return output.toByteArray();
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletResponseWrapper#getOutputStream()
	 */
	public ServletOutputStream getOutputStream() {
		return new FilterServletOutputStream(output);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletResponseWrapper#getWriter()
	 */
	public PrintWriter getWriter() {
		return new PrintWriter(getOutputStream(), true);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletResponseWrapper#setContentLength(int)
	 */
	public void setContentLength(int length) {
		this.contentLength = length;
		super.setContentLength(length);
	}

	/**
	 * Gets the content length.
	 *
	 * @return the content length
	 */
	public int getContentLength() {
		return contentLength;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletResponseWrapper#setContentType(java.lang.String)
	 */
	public void setContentType(String type) {
		this.contentType = type;
		super.setContentType(type);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletResponseWrapper#getContentType()
	 */
	public String getContentType() {
		return contentType;
	}
}