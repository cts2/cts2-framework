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
package edu.mayo.cts2.framework.webapp.service;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;

import org.reflections.Reflections;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import edu.mayo.cts2.framework.core.config.ServerContext;
import edu.mayo.cts2.framework.core.url.UrlConstructor;
import edu.mayo.cts2.framework.model.service.core.ProfileElement;
import edu.mayo.cts2.framework.service.profile.Cts2Profile;
import edu.mayo.cts2.framework.service.provider.ServiceProvider;
import edu.mayo.cts2.framework.service.provider.ServiceProviderFactory;

/**
 * A factory for creating ClassPathScanningConformance objects.
 */
@Component
public class ClassPathScanningConformanceFactory implements ConformanceFactory, InitializingBean {
	
	@Resource
	private ServiceProviderFactory serviceProviderFactory;
	
	@Resource
	private ServerContext serverContext;
	
	private UrlConstructor urlConstructor;
	
	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(this.serverContext);
		
		this.urlConstructor = new UrlConstructor(this.serverContext);
	}
	
	/* (non-Javadoc)
	 * @see edu.mayo.cts2.framework.webapp.service.ConformanceFactory#getProfileElements()
	 */
	@Override
	public Set<ProfileElement> getProfileElements(){
		Set<ProfileElement> returnSet = new HashSet<ProfileElement>();
		
		ServiceProvider provider = 
			this.serviceProviderFactory.getServiceProvider();
		
		if(provider == null){
			return returnSet;
		}
	
		Set<Class<? extends Cts2Profile>> profiles = this.doScan();
		
		for(Class<? extends Cts2Profile> profile : profiles){
			if(provider.getService(profile) != null){
				returnSet.addAll(ServiceUtils.buildProfileElements(profile, urlConstructor));
			}
		}
		
		return returnSet;
	}
	
	/**
	 * Do scan.
	 *
	 * @return the set< class<? extends cts2 profile>>
	 */
	protected Set<Class<? extends Cts2Profile>> doScan(){
		Reflections reflections = new Reflections("edu.mayo.cts2.framework.service.profile");
		
		Set<Class<? extends Cts2Profile>> profiles = reflections.getSubTypesOf(Cts2Profile.class);

		return profiles;
	}

}
