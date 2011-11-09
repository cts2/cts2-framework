package edu.mayo.cts2.framework.webapp.rest.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.mayo.cts2.framework.model.core.FormatReference;
import edu.mayo.cts2.framework.model.core.NamespaceReference;
import edu.mayo.cts2.framework.model.core.SourceReference;
import edu.mayo.cts2.framework.model.service.core.BaseService;
import edu.mayo.cts2.framework.model.service.core.types.ImplementationProfile;
import edu.mayo.cts2.framework.model.service.core.types.StructuralProfile;
import edu.mayo.cts2.framework.model.util.ModelUtils;

@Controller
public class BaseController {

	@Value("#{buildProperties.buildversion}")
	private String buildVersion;
	
	@Value("#{buildProperties.name}")
	private String buildName;
	
	@Value("#{buildProperties.description}")
	private String buildDescription;

	@RequestMapping(value="/service", produces="asdf", method=RequestMethod.GET)
	@ResponseBody
	public BaseService getServiceInfo(
			HttpServletRequest httpServletRequest) {
		BaseService service = new BaseService();
		service.setServiceName(buildName);
		service.setServiceDescription(ModelUtils.createOpaqueData(buildDescription));
		service.setServiceVersion(buildVersion);
		service.setServiceProvider(new SourceReference());
		service.addSupportedFormat(new FormatReference());
		service.addSupportedProfile(StructuralProfile.SP_ASSOCIATION);
		service.setImplementationType(ImplementationProfile.IP_REST);
		service.addKnownNamespace(new NamespaceReference());
		service.setDefaultFormat(new FormatReference());
	
		return service;
	}
}
