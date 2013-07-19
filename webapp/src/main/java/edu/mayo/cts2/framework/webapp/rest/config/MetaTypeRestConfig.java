/*
 * Copyright: (c) 2004-2012 Mayo Foundation for Medical Education and 
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
package edu.mayo.cts2.framework.webapp.rest.config;

import edu.mayo.cts2.framework.core.config.AbstractConfigurableExportedService;
import edu.mayo.cts2.framework.core.plugin.ExportedService;
import org.apache.commons.lang.BooleanUtils;
import org.osgi.framework.Constants;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.metatype.MetaTypeProvider;
import org.springframework.stereotype.Component;

import java.util.Dictionary;
import java.util.Hashtable;

/**
 * The Class MetaTypeRestConfig.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@ExportedService( { MetaTypeProvider.class, ManagedService.class  })
@Component
public class MetaTypeRestConfig extends AbstractConfigurableExportedService implements RestConfig {
	
	public static final String ALLOW_HTML_RENDERING = "allowHtmlRendering";
	public static final String ALLOW_SOAP = "allowSoap";
	public static final String SHOW_STACK_TRACE = "showStackTrace";
	public static final String SHOW_HOME_PAGE = "showHomePage";
    public static final String SUPPORT_EMAIL = "supportEmail";

    private static final String SERVICE_PID = "edu.mayo.cts2.framework.webapp.rest.config";
	
	private static final boolean ALLOW_HTML_RENDERING_DEFAULT = false;
	private boolean allowHtmlRendering = ALLOW_HTML_RENDERING_DEFAULT;
	
	private static final boolean ALLOW_SOAP_DEFAULT = false;
	private boolean allowSoap = ALLOW_SOAP_DEFAULT;
	
	private static final boolean SHOW_STACK_TRACE_DEFAULT = false;
	private boolean showStackTrace = SHOW_STACK_TRACE_DEFAULT;
	
	private static final boolean SHOW_HOME_PAGE_DEFAULT = true;
	private boolean showHomePage = SHOW_HOME_PAGE_DEFAULT;

	/* (non-Javadoc)
	 * @see org.osgi.service.metatype.MetaTypeProvider#getLocales()
	 */
	@Override
	public String[] getLocales() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.osgi.service.cm.ManagedService#updated(java.util.Dictionary)
	 */
	@Override
	public void updated(@SuppressWarnings("rawtypes") Dictionary properties) throws ConfigurationException {
		if(properties != null){
			boolean allow = BooleanUtils.toBooleanDefaultIfNull( (Boolean) properties.get(ALLOW_HTML_RENDERING), ALLOW_HTML_RENDERING_DEFAULT);
			this.allowHtmlRendering = allow;
			
			boolean show = BooleanUtils.toBooleanDefaultIfNull( (Boolean) properties.get(SHOW_STACK_TRACE), SHOW_STACK_TRACE_DEFAULT);
			this.showStackTrace = show;
			
			boolean home = BooleanUtils.toBooleanDefaultIfNull( (Boolean) properties.get(SHOW_HOME_PAGE), SHOW_HOME_PAGE_DEFAULT);
			this.showHomePage = home;
			
			boolean soap = BooleanUtils.toBooleanDefaultIfNull( (Boolean) properties.get(ALLOW_SOAP), ALLOW_SOAP_DEFAULT);
			this.allowSoap = soap;
		}		
	}

    protected String checkEnvironmentVariableOverride(String property, String value){
        String enValue = System.getProperty(property);
        if(enValue != null){
            return enValue;
        } else {
            return value;
        }
    }
	
	protected boolean checkEnvironmentVariableOverride(String property, boolean value){
		String enValue = System.getProperty(property);
		if(enValue != null){
			return BooleanUtils.toBoolean(enValue);
		} else {
			return value;
		}
	}

	/* (non-Javadoc)
	 * @see edu.mayo.cts2.framework.core.plugin.ServiceMetadataAware#getMetadata()
	 */
	@Override
	public Hashtable<String, Object> getMetadata() {
		Hashtable<String, Object> table = new Hashtable<String, Object>();
		table.put(Constants.SERVICE_PID, SERVICE_PID);
		
		return table;
	}

	@Override
	public boolean getAllowHtmlRendering() {
		return this.checkEnvironmentVariableOverride(ALLOW_HTML_RENDERING, this.allowHtmlRendering);
	}

	@Override
	public boolean getShowStackTraceOnError() {
		return this.checkEnvironmentVariableOverride(SHOW_STACK_TRACE, this.showStackTrace);
	}

	@Override
	public boolean getShowHomePage() {
		return this.checkEnvironmentVariableOverride(SHOW_HOME_PAGE, this.showHomePage);
	}
	
	@Override
	public boolean getAllowSoap() {
		return this.checkEnvironmentVariableOverride(ALLOW_SOAP, this.allowSoap);
	}

    @Override
    public String getSupportEmail() {
        return this.checkEnvironmentVariableOverride(SUPPORT_EMAIL, null);
    }

    /* (non-Javadoc)
         * @see edu.mayo.cts2.framework.core.config.AbstractConfigurableExportedService#getMetatypeXmlPath()
         */
	@Override
	protected String getMetatypeXmlPath() {
		return "/rest/webapp-rest-metatype.xml";
	}

}
