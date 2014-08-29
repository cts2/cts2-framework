package edu.mayo.cts2.framework.webapp.rest.controller;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import edu.mayo.cts2.framework.webapp.rest.config.RestConfig;

@Controller
public class HomeController {
	
	@Resource
	private RestConfig restConfig;
	
	public class HomePageDisabledException extends RuntimeException {
		private static final long serialVersionUID = 5068045753067839000L;
	}
	
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(HomePageDisabledException.class)
    public void handle() {
        // ...
    }
	
	@RequestMapping(value="/", method=RequestMethod.GET)
	public ModelAndView renderHomePage(){
		if(this.restConfig.getShowHomePage()){
			String alternateHomePage = this.restConfig.getAlternateHomePage();
			if (StringUtils.isNotBlank(alternateHomePage)) {
				return new ModelAndView("redirect:" + alternateHomePage);
			} else {
				return new ModelAndView("home");
			}
		} else {
			throw new HomePageDisabledException();
		}
	}

}
