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
package edu.mayo.cts2.framework.service.provider;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.core.config.option.OptionHolder;
import edu.mayo.cts2.framework.core.plugin.AbstractExtensionPoint;
import edu.mayo.cts2.framework.core.plugin.ExtensionPoint;
import edu.mayo.cts2.framework.core.plugin.ExtensionPointDescriptor;
import edu.mayo.cts2.framework.core.plugin.PluginConfigChangeObserver;
import edu.mayo.cts2.framework.core.plugin.PluginManager;
import edu.mayo.cts2.framework.core.plugin.PluginReference;

/**
 * A factory for creating ServiceProvider objects.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Component
@ExtensionPointDescriptor(xmlPrefix="service-provider")
public class ServiceProviderFactory extends AbstractExtensionPoint<ServiceProvider> implements InitializingBean,
		PluginConfigChangeObserver, ServiceProviderChangeObservable {

	private final Log log = LogFactory.getLog(getClass().getName());

	@Resource
	private PluginManager pluginManager;

	private ServiceProvider serviceProvider;

	private Set<ServiceProviderChangeObserver> observers = new HashSet<ServiceProviderChangeObserver>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		this.pluginManager.registerExtensionPoint(this);
	}

	/**
	 * Instantiates a new service provider factory.
	 */
	public ServiceProviderFactory() {
		super();
	}

	public ServiceProvider getServiceProvider() {
		return this.getService();
	}



	private void fireServiceProviderChangeEvent() {
		for (ServiceProviderChangeObserver observer : this.observers) {
			observer.onServiceProviderChange();
		}
	}

	public void registerListener(ServiceProviderChangeObserver observer) {
		this.observers.add(observer);
	}

	public void unregisterListener(ServiceProviderChangeObserver observer) {
		this.observers.remove(observer);
	}
	
	@Override
	public void onPluginActivated(PluginReference ref) {
	
		this.fireServiceProviderChangeEvent();
	}

	@Override
	public void onPluginRemoved(PluginReference ref) {
		//
	}

	@Override
	public void onPluginAdded(PluginReference ref) {
		//no-op
	}

	@Override
	public void onPluginSpecificConfigPropertiesChange(OptionHolder newOptions) {

		this.fireServiceProviderChangeEvent();
	}

}
