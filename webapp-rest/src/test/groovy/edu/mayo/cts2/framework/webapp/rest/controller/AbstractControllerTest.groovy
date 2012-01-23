package edu.mayo.cts2.framework.webapp.rest.controller;

import static org.junit.Assert.*

import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.springframework.mock.web.MockHttpServletRequest

import edu.mayo.cts2.framework.model.command.Page;

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
	
	@Test
	@Ignore
	void "test isPartialRedirect False"(){
		def request = new MockHttpServletRequest("GET", "/codesystembyuri")
		
		assertFalse this.abstractController.isPartialRedirect(request, "/codesystembyuri")
	}
	
	@Test
	@Ignore
	void "test isPartialRedirect True"(){
		def request = new MockHttpServletRequest("GET", "/codesystembyuri/version/TESTVERSION")
		
		assertTrue this.abstractController.isPartialRedirect(request, "/codesystembyuri")
	}
}
