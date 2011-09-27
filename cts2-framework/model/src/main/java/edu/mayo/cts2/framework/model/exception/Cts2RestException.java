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

import edu.mayo.cts2.framework.model.service.exception.CTS2Exception;

/**
 * The Class Cts2RestException.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class Cts2RestException extends RuntimeException {

	private static final long serialVersionUID = -8637830378600131685L;

	private CTS2Exception cts2Exception;

	private int statusCode;

	/**
	 * Instantiates a new cts2 rest exception.
	 *
	 * @param cts2Exception the cts2 exception
	 */
	public Cts2RestException(CTS2Exception cts2Exception) {
		super();
		this.cts2Exception = cts2Exception;
	}

	/**
	 * Instantiates a new cts2 rest exception.
	 *
	 * @param cts2Exception the cts2 exception
	 * @param cause the cause
	 */
	public Cts2RestException(CTS2Exception cts2Exception, Throwable cause) {
		super(cause);
		this.cts2Exception = cts2Exception;
	}

	/**
	 * Gets the cts2 exception.
	 * 
	 * @return the cts2 exception
	 */
	public CTS2Exception getCts2Exception() {
		return cts2Exception;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public int getStatusCode() {
		return statusCode;
	}
}
