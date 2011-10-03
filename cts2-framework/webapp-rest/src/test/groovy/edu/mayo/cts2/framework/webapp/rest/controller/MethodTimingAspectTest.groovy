package edu.mayo.cts2.framework.webapp.rest.controller;

import javax.annotation.Resource

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.stereotype.Controller
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

import edu.mayo.cts2.framework.model.exception.Cts2RestException
import edu.mayo.cts2.framework.service.command.QueryControl

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value="MethodTimingAspectTest.xml")
class MethodTimingAspectTest {
	
	@Resource
	QueryControlTestBean queryControlTestBean;
	
	@Test
	void "Test Under Time Limit"(){
		queryControlTestBean.testWithQueryControlUnderTime(new QueryControl(timelimit:1))
	}
	
	@Test(expected=Cts2RestException.class)
	void "Test Over Time Limit"(){
		queryControlTestBean.testWithQueryControlOverTime(new QueryControl(timelimit:1))
	}
}

@Controller
class QueryControlTestBean {
	
	String testWithQueryControl(QueryControl queryControl){
		return "done";
	}
	
	String testWithQueryControlOverTime(QueryControl queryControl){
		Thread.sleep((long) queryControl.getTimelimit() * 1000 * 2);
		
		return "done";
	}
	
	String testWithQueryControlUnderTime(QueryControl queryControl){
		return "done";
	}
}