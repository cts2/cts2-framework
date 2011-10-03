package edu.mayo.cts2.framework.webapp.rest.controller;

import org.springframework.stereotype.Controller;

import edu.mayo.cts2.framework.service.command.QueryControl;

@Controller
public class QueryControlTestBean {

	public String testWithQueryControl(QueryControl queryControl){
		return "done";
	}
	
	public String testWithQueryControlOverTime(QueryControl queryControl) throws Exception {
		Thread.sleep((long) queryControl.getTimelimit() * 1000 * 2);
		
		return "done";
	}
	
	public String testWithQueryControlUnderTime(QueryControl queryControl){
		return "done";
	}
}
