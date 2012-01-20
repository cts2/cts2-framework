package edu.mayo.cts2.framework.webapp.rest.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import edu.mayo.cts2.framework.model.exception.UnspecifiedCts2Exception;

public class AcceptHeaderAdjustingFilter implements Filter {

	@Override
	public void destroy() {
		//
	}

	@Override
	public void doFilter(
			ServletRequest request, 
			ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		if(! (request instanceof HttpServletRequest)){
			throw new UnspecifiedCts2Exception("ServletRequest expected to be of type HttpServletRequest");
		}

		HttpServletRequest httpRequest = (HttpServletRequest) request;

		@SuppressWarnings("unchecked")
		Map<String, String[]> params = httpRequest.getParameterMap();
		
		if(params.containsKey("format")){
			String[] formats = params.get("format");
			if(formats.length != 1){
				throw new IllegalStateException("Only one 'format' parameter allowed.");
			}
			
			String format = formats[0];
			
			String type;
			
			if(format.equals("json")){
				type = "application/json";
			} else if (format.equals("xml")){
				type = "application/xml";
			} else {
				throw new IllegalStateException("Format: " + format + " not recognized.");
			}
			
			chain.doFilter(new AcceptTypeChangingRequest(httpRequest, type), response);
		} else {
			chain.doFilter(request, response);
		}
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		//
	}
	
	public class AcceptTypeChangingRequest extends HttpServletRequestWrapper {

		private String acceptHeader;
		
		public AcceptTypeChangingRequest(HttpServletRequest request, String acceptHeader) {
			super(request);
			this.acceptHeader = acceptHeader;
		}
		
		@SuppressWarnings("rawtypes")
		public Enumeration getHeaders(String name){
			if(name.equalsIgnoreCase("accept")){
				return Collections.enumeration(Arrays.asList(acceptHeader));
			}
			
			return super.getHeaders(name);
		}

		public String getHeader(String name) {
			if(name.equalsIgnoreCase("accept")){
				return acceptHeader;
			}
			
			return super.getHeader(name);
		}
		
	}

}
