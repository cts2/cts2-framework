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
package edu.mayo.cts2.framework.model.exception;

/**
 * The Class UnspecifiedCts2RestException.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class UnspecifiedCts2RestException extends RuntimeException {

	private static final long serialVersionUID = 1252172451874543855L;
	
	private static final int DEFAULT_STATUS_CODE = 500;
	
	private int statusCode = DEFAULT_STATUS_CODE;
	
	/**
	 * Instantiates a new unspecified cts2 rest exception.
	 *
	 * @param message the message
	 */
	public UnspecifiedCts2RestException(String message) {
		super(message);
	}
	
	/**
	 * Instantiates a new unspecified cts2 rest exception.
	 *
	 * @param throwable the throwable
	 */
	public UnspecifiedCts2RestException(Throwable throwable) {
		super(throwable);
	}

	/**
	 * Instantiates a new unspecified cts2 rest exception.
	 *
	 * @param message the message
	 * @param statusCode the status code
	 */
	public UnspecifiedCts2RestException(String message, int statusCode) {
		super(message);
		this.statusCode = statusCode;
	}

	/**
	 * Instantiates a new unspecified cts2 rest exception.
	 *
	 * @param cause the cause
	 * @param statusCode the status code
	 */
	public UnspecifiedCts2RestException(Throwable cause, int statusCode) {
		super(cause);
		this.statusCode = statusCode;
	}
	
	/**
	 * Gets the status code.
	 *
	 * @return the status code
	 */
	public int getStatusCode() {
		return statusCode;
	}
}
