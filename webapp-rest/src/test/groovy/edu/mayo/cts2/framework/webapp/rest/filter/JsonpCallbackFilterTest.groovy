package edu.mayo.cts2.framework.webapp.rest.filter;

import static org.junit.Assert.*

import org.apache.commons.io.IOUtils
import org.junit.Test
import org.springframework.mock.web.MockFilterChain
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse

import edu.mayo.cts2.framework.webapp.rest.filter.jsonp.JsonpCallbackFilter

class JsonpCallbackFilterTest {
	
	def jsonpCallbackFilter = new JsonpCallbackFilter();
	
	@Test
	void "Test doFilter with JSON Callback"(){
		def request = new MockHttpServletRequest("GET", "test/cs/name");
		request.setParameter("callback", "somecallback")
	
		def response = new MockHttpServletResponse()
	
		jsonpCallbackFilter.doFilter(
			request,
			response,
			new MockFilterChain() )

		
		assertEquals "somecallback();", response.getContentAsString()
	}

}
