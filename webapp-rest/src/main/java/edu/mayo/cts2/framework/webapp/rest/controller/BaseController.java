package edu.mayo.cts2.framework.webapp.rest.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.collect.Iterables;

import edu.mayo.cts2.framework.model.core.FormatReference;
import edu.mayo.cts2.framework.model.core.SourceReference;
import edu.mayo.cts2.framework.model.service.core.BaseService;
import edu.mayo.cts2.framework.model.service.core.ProfileElement;
import edu.mayo.cts2.framework.model.service.core.types.ImplementationProfile;
import edu.mayo.cts2.framework.model.util.ModelUtils;
import edu.mayo.cts2.framework.service.profile.BaseServiceService;
import edu.mayo.cts2.framework.webapp.service.ConformanceFactory;

@Controller
public class BaseController extends AbstractMessageWrappingController{
	
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
		
		if(this.baseServiceService != null){
			try {
				BaseService baseService = baseServiceService.getBaseService();
				
				if(baseService != null){
					//return baseService;
				}
			} catch (Exception e){
				e.printStackTrace();
			}
		}
		
		BaseService service = new BaseService();
		service.setServiceName(buildName);
		service.setServiceDescription(ModelUtils.createOpaqueData(buildDescription));
		service.setServiceVersion(buildVersion);
		service.setServiceProvider(new SourceReference());
		service.addSupportedFormat(new FormatReference());

		service.setSupportedProfile(Iterables.toArray(
				this.conformanceFactory.getProfileElements(), ProfileElement.class));
		
		service.setImplementationType(ImplementationProfile.IP_REST);

		service.setDefaultFormat(new FormatReference());
		
		String acceptHeader = request.getHeader("Accept");
	
		List<MediaType> types = MediaType.parseMediaTypes(acceptHeader);
		MediaType.sortByQualityValue(types);
		
		MediaType type = types.get(0);
				
		if(type.isCompatibleWith(MediaType.TEXT_HTML)){
			ModelAndView mav = new ModelAndView("test");
			mav.addObject("testBean", service);
			return mav;
		} else {
			return new ResponseEntity<BaseService>(service, HttpStatus.OK);
		}
		
	}
	
}
