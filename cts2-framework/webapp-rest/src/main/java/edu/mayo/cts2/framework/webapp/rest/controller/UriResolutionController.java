package edu.mayo.cts2.framework.webapp.rest.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UriResolutionController {
	
	protected final static String ATTRIBUTE_NAME = "forwardedObject";
	protected final static String FORWARDING_URL = "forwardeduriresolution";

	@RequestMapping(value=FORWARDING_URL, method=RequestMethod.GET)
	@ResponseBody
	public Object outputFowardedResponse(HttpServletRequest request) {
		return request.getAttribute(ATTRIBUTE_NAME);
	}
}
