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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import javax.annotation.Resource;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import edu.mayo.cts2.framework.core.config.ServerContext;
import edu.mayo.cts2.framework.core.url.UrlConstructor;
import edu.mayo.cts2.framework.core.util.EncodingUtils;
import edu.mayo.cts2.framework.model.core.ComponentReference;
import edu.mayo.cts2.framework.model.core.FormatReference;
import edu.mayo.cts2.framework.model.core.MatchAlgorithmReference;
import edu.mayo.cts2.framework.model.core.ModelAttributeReference;
import edu.mayo.cts2.framework.model.core.OpaqueData;
import edu.mayo.cts2.framework.model.core.PredicateReference;
import edu.mayo.cts2.framework.model.core.SourceReference;
import edu.mayo.cts2.framework.model.core.VersionTagReference;
import edu.mayo.cts2.framework.model.service.core.DocumentedNamespaceReference;
import edu.mayo.cts2.framework.model.service.core.ProfileElement;
import edu.mayo.cts2.framework.model.service.core.types.ImplementationProfile;
import edu.mayo.cts2.framework.model.service.entitydescription.EntityDescriptionQueryService;
import edu.mayo.cts2.framework.model.service.entitydescription.EntityDescriptionReadService;
import edu.mayo.cts2.framework.model.service.valuesetdefinition.ValueSetDefinitionResolution;
import edu.mayo.cts2.framework.model.util.ModelUtils;
import edu.mayo.cts2.framework.service.profile.Cts2Profile;

/**
 * The Class ServiceBuilder.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Component
public class ServiceBuilder implements InitializingBean {

	@Resource
	private ServerContext serverContext;
	
	private UrlConstructor urlConstructor;
	
	private static String UNSPECIFIED = "unspecified";
	
	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(this.serverContext);
		
		this.urlConstructor = new UrlConstructor(this.serverContext);
	}
	
	@SuppressWarnings("unchecked")
	protected <T extends edu.mayo.cts2.framework.model.service.core.BaseService> T buildBaseServiceMetadata(
			edu.mayo.cts2.framework.service.profile.BaseService cts2Service,
			Class<T> serviceMetadataBeanClass){
		
		T service = this.newInstance(serviceMetadataBeanClass);
		
		service.setImplementationType(ImplementationProfile.IP_REST);
		
		service.setDefaultFormat(new FormatReference(MediaType.TEXT_XML_VALUE));
		service.addSupportedFormat(new FormatReference(MediaType.TEXT_XML_VALUE));
		service.addSupportedFormat(new FormatReference(MediaType.APPLICATION_JSON_VALUE));
		
		String serviceName = cts2Service.getServiceName();
		if(serviceName == null){
			serviceName = cts2Service.getClass().getSimpleName();
		}
		service.setServiceName(serviceName);
		
		SourceReference provider = cts2Service.getServiceProvider();
		if(provider == null){
			provider = new SourceReference(UNSPECIFIED);
		}
		service.setServiceProvider(provider);

		String version = cts2Service.getServiceVersion();
		if(version == null){
			version = UNSPECIFIED;
		}
		service.setServiceVersion(version);
		
		OpaqueData description = cts2Service.getServiceDescription();
		if(description == null){
			description = ModelUtils.createOpaqueData(
				//TODO: Maybe add a little more pizazz to the default description...
				"A CTS2 Development Framework Service Implementation."
			);
		}
		service.setServiceDescription(description);
		
		List<DocumentedNamespaceReference> namespaces = cts2Service.getKnownNamespaceList();
		if(CollectionUtils.isEmpty(namespaces)){
			namespaces = Arrays.asList(new DocumentedNamespaceReference());
		}
		service.setKnownNamespace(namespaces);
	
		for(ProfileElement profile : ServiceUtils.buildProfileElements(
				(Class<? extends Cts2Profile>)cts2Service.getClass(), 
				this.urlConstructor)){
			service.addSupportedProfile(profile);
		}
		
		return service;
	}
	
	/**
	 * Builds the service metadata.
	 *
	 * @param <T> the generic type
	 * @param cts2Service the cts2 service
	 * @param serviceMetadataBean the service metadata bean
	 * @return the t
	 */
	public <T extends edu.mayo.cts2.framework.model.service.core.BaseService> T buildServiceMetadata(
			edu.mayo.cts2.framework.service.profile.BaseService cts2Service,
			Class<T> serviceMetadataBeanClass){
	
		return this.buildBaseServiceMetadata(cts2Service, serviceMetadataBeanClass);
	}
	
	/**
	 * Builds the service metadata.
	 *
	 * @param <T> the generic type
	 * @param cts2Service the cts2 service
	 * @param serviceMetadataBean the service metadata bean
	 * @return the t
	 */
	public <T extends edu.mayo.cts2.framework.model.service.core.BaseReadService> T buildServiceMetadata(
			edu.mayo.cts2.framework.service.profile.ReadService<?,?> cts2Service,
			Class<T> serviceMetadataBeanClass){
		
		T service = this.buildBaseServiceMetadata(cts2Service, serviceMetadataBeanClass);
		
		//TODO I think the other advanced services need some sort of mapping like this too... both here and in the QueryService 
		//Otherwise, all of the service metadata calls fail.
		if (service instanceof EntityDescriptionReadService)
		{
			((EntityDescriptionReadService)service).setKnownCodeSystem(
					((edu.mayo.cts2.framework.service.profile.entitydescription.EntityDescriptionReadService)cts2Service).getKnownCodeSystems());
			((EntityDescriptionReadService)service).setKnownCodeSystemVersion(
					((edu.mayo.cts2.framework.service.profile.entitydescription.EntityDescriptionReadService)cts2Service).getKnownCodeSystemVersions());
			((EntityDescriptionReadService)service).setSupportedVersionTag(
					((edu.mayo.cts2.framework.service.profile.entitydescription.EntityDescriptionReadService)cts2Service).getSupportedVersionTags());
		}

		return service;
	}
	
	/**
	 * Builds the service metadata.
	 *
	 * @param <T> the generic type
	 * @param cts2Service the cts2 service
	 * @param serviceMetadataBean the service metadata bean
	 * @return the t
	 */
	public <T extends edu.mayo.cts2.framework.model.service.core.BaseQueryService> T buildServiceMetadata(
			edu.mayo.cts2.framework.service.profile.BaseQueryService cts2Service,
			Class<T> serviceMetadataBeanClass){
		
		T service = this.buildBaseServiceMetadata(cts2Service, serviceMetadataBeanClass);
		
		service.setSupportedMatchAlgorithm(
			new ArrayList<MatchAlgorithmReference>(cts2Service.getSupportedMatchAlgorithms()));
		
		service.setSupportedModelAttribute(
			this.toModelAttributeReferences(cts2Service.getSupportedSearchReferences()));	
		
		Set<PredicateReference> pr = cts2Service.getKnownProperties();
		if (pr != null)
		{
			service.setKnownProperty(new ArrayList<PredicateReference>(pr));
		}
		
		if (service instanceof EntityDescriptionQueryService)
		{
			//TODO this is an odd mis-match (set to list)
			((EntityDescriptionQueryService)service).setSupportedVersionTag(
					(new ArrayList<VersionTagReference>(
							((edu.mayo.cts2.framework.service.profile.entitydescription.EntityDescriptionQueryService)cts2Service).getSupportedTags())));
			//TODO I have no idea what this is supposed to represent.. but the schema requires it.
			((EntityDescriptionQueryService)service).setEntities("entities");
		}
		
		return service;
	}
	
	private List<ModelAttributeReference> toModelAttributeReferences(Set<? extends ComponentReference> props){
		if(CollectionUtils.isEmpty(props)){
			return null;
		}
		
		List<ModelAttributeReference> returnList = new ArrayList<ModelAttributeReference>();
		
		for(ComponentReference component : props){
			if(component.getPropertyReference() != null){
				ModelAttributeReference ref = new ModelAttributeReference();
				ref.setContent(EncodingUtils.encodeScopedEntityName(component.getPropertyReference()));
				ref.setUri(component.getPropertyReference().getUri());
				ref.setHref(component.getPropertyReference().getHref());
				returnList.add(ref);
			}
			else if (component.getAttributeReference() != null)
			{
				ModelAttributeReference ref = new ModelAttributeReference();
				ref.setContent(component.getAttributeReference());
				returnList.add(ref);
			}
			else if (component.getSpecialReference() != null)
			{
				ModelAttributeReference ref = new ModelAttributeReference();
				ref.setContent(component.getAttributeReference());
				returnList.add(ref);
			}
		}
		
		return returnList;
	}
	
	protected <T> T newInstance(Class<T> clazz){
		try {
			return clazz.newInstance();
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}

}
