package edu.mayo.cts2.framework.webapp.rest.controller;

import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.common.collect.Iterables;

import edu.mayo.cts2.framework.model.core.FormatReference;
import edu.mayo.cts2.framework.model.core.SourceReference;
import edu.mayo.cts2.framework.model.service.core.BaseService;
import edu.mayo.cts2.framework.model.service.core.DocumentedNamespaceReference;
import edu.mayo.cts2.framework.model.service.core.ProfileElement;
import edu.mayo.cts2.framework.model.service.core.types.ImplementationProfile;
import edu.mayo.cts2.framework.model.util.ModelUtils;
import edu.mayo.cts2.framework.service.profile.BaseServiceService;
import edu.mayo.cts2.framework.webapp.service.ConformanceFactory;

@Controller
public class BaseController extends AbstractMessageWrappingController {
	
	@Cts2Service
	private BaseServiceService baseServiceService;
	
	@Resource
	private ConformanceFactory conformanceFactory;

	@Value("#{buildProperties.buildversion}")
	private String buildVersion;
	
	@Value("#{buildProperties.name}")
	private String buildName;
	
	@Value("#{buildProperties.description}")
	private String buildDescription;

	@RequestMapping(value="/service", method=RequestMethod.GET)
	public Object getServiceInfo(HttpServletRequest request) {
		
		BaseService service = this.getBaseServiceFromService();
		
		if(service == null){
			service = new BaseService();
			try {
				service.setServiceName(this.baseServiceService.getServiceName());
			} catch (UnsupportedOperationException e) {
				service.setServiceName(buildName);
			}
			
			try {
				service.setServiceDescription(this.baseServiceService.getServiceDescription());
			} catch (UnsupportedOperationException e) {
				service.setServiceDescription(ModelUtils.createOpaqueData(buildDescription));
			}
			
			try {
				service.setServiceVersion(this.baseServiceService.getServiceVersion());
			} catch (UnsupportedOperationException e) {
				service.setServiceVersion(buildVersion);
			}
			
			try {
				service.setServiceProvider(this.baseServiceService.getServiceProvider());
			} catch (UnsupportedOperationException e) {
				service.setServiceProvider(new SourceReference("Unspecified"));
			}

			Set<ProfileElement> supportedProfiles = this.conformanceFactory.getProfileElements();
			if(CollectionUtils.isEmpty(supportedProfiles)){
				throw new UnsupportedOperationException("This CTS2 Instance has no Supported Profiles.");
			}
			
			service.setSupportedProfile(Iterables.toArray(
					this.conformanceFactory.getProfileElements(), ProfileElement.class));
			
			service.setImplementationType(ImplementationProfile.IP_REST);
	
			service.setDefaultFormat(new FormatReference(MediaType.TEXT_XML_VALUE));
			service.addSupportedFormat(new FormatReference(MediaType.APPLICATION_JSON_VALUE));
			service.addKnownNamespace(new DocumentedNamespaceReference());
		}
		
		return this.buildResponse(request, service);
	}
	
	private BaseService getBaseServiceFromService(){
		BaseService baseService;
		try {
			baseService = baseServiceService.getBaseService();
		} catch (UnsupportedOperationException e) {
			return null;
		}
		
		return baseService;
	}

}
