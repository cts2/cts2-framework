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
package edu.mayo.cts2.framework.webapp.rest.exception;

import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import edu.mayo.cts2.framework.model.exception.Cts2RestException;

/**
 * The Class PropertiesFileExceptionCodeMapper.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Component
public class PropertiesFileExceptionCodeMapper implements Cts2RestExceptionCodeMapper, InitializingBean {
	
	private static Log log = LogFactory.getLog(PropertiesFileExceptionCodeMapper.class);
	
	private static final int DEFAULT_ERROR_CODE = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;

	@Resource(name="errorCodes")
	private Properties properties;

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(properties, "Error codes mapping file must be supplied.");
	}
	
	@Deprecated
	public int getErrorCode(Cts2RestException exception) {
		String exceptionName = exception.getCts2Exception().getClass().getSimpleName();
		
		String code = this.properties.getProperty(exceptionName);
		
		if(code == null){
			log.warn("No mapped error code for: " + exceptionName + ". Defaulting to: " + DEFAULT_ERROR_CODE);
			
			return DEFAULT_ERROR_CODE;
		}
		
		return Integer.parseInt(code);
	}
	
	/* (non-Javadoc)
	 * @see edu.mayo.cts2.framework.webapp.rest.exception.Cts2RestExceptionCodeMapper#getErrorCode(edu.mayo.cts2.framework.model.exception.Cts2RestException)
	 */
	public int getErrorCode(Exception exception) {
		String exceptionName = exception.getClass().getSimpleName();
		
		String code = this.properties.getProperty(exceptionName);
		
		if(code == null){
			log.warn("No mapped error code for: " + exceptionName + ". Defaulting to: " + DEFAULT_ERROR_CODE);
			
			return DEFAULT_ERROR_CODE;
		}
		
		return Integer.parseInt(code);
	}
}
