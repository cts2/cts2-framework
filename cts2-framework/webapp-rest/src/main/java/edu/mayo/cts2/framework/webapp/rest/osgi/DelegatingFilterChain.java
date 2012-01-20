package edu.mayo.cts2.framework.webapp.rest.osgi;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class DelegatingFilterChain implements FilterChain {
	
	private FilterChain delegate;
	private Filter[] additionalFilters;
	
	int pos = 0;
	
	public DelegatingFilterChain(FilterChain delegate, Filter... additionalFilters){
		super();
		this.delegate = delegate;
		this.additionalFilters = additionalFilters;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response)
			throws IOException, ServletException {
		if(pos < this.additionalFilters.length){
			Filter filter = this.additionalFilters[pos];
			pos++;
			filter.doFilter(request, response, this);
		} else {
			delegate.doFilter(request, response);
		}
	}
	
	

}
