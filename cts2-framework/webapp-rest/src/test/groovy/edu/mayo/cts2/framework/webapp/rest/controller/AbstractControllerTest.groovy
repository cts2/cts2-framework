package edu.mayo.cts2.framework.webapp.rest.controller;

import static org.junit.Assert.*

import org.junit.Before
import org.junit.Test

import edu.mayo.cts2.framework.service.command.Page

 class AbstractControllerTest {
	
	private AbstractController abstractController;
	
	@Before
	void setup(){
		this.abstractController = new AbstractController(){}
	}
	
	@Test
	void testIsEnd(){
		Page page = new Page(maxtoreturn:100, page:2)
		
		assertEquals 300, this.abstractController.getEnd(page)
	}
	
	@Test
	void testGetStart(){
		Page page = new Page(maxtoreturn:100, page:2)
		
		assertEquals 200, this.abstractController.getStart(page)
	}
}
