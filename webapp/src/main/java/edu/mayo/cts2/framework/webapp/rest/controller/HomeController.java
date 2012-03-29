package edu.mayo.cts2.framework.webapp.rest.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {
	
	@RequestMapping(value="/", method=RequestMethod.GET)
	public ModelAndView renderAvailableRestUrls(){
		ModelAndView mav = new ModelAndView("home");
		
		return mav;
	}

}
