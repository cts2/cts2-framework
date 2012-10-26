package edu.mayo.cts2.framework.webapp.rest.osgi;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.commons.lang.StringUtils;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

import edu.mayo.cts2.framework.core.plugin.ExtensionPoint;
import edu.mayo.cts2.framework.core.plugin.PluginManager;

public class OsgiExtensionPointFilterProxy implements Filter, ExtensionPoint {

	private ServiceTracker osgiFilterTracker;

	private FilterConfig filterConfig;
	
	public final static String ORDER_PROPERTY = "ORDER";
	public final static String ORDER_FIRST = "FIRST";
	public final static String ORDER_LAST = "LAST";

	
	private static Comparator<ServiceReference>
		SERVICE_REF_COMPARATOR = new Comparator<ServiceReference>(){

			@Override
			public int compare(ServiceReference o1, ServiceReference o2) {
				String prop1 = (String)o1.getProperty(ORDER_PROPERTY);
				String prop2 = (String)o2.getProperty(ORDER_PROPERTY);

				if(prop1 == null && prop2 == null){
					return 0;
				} else if(StringUtils.equalsIgnoreCase(prop1, prop2)){
					throw new IllegalStateException("More than one Filter registered with ORDER: " + prop1);
				} else if(StringUtils.equals(prop1, ORDER_FIRST) ||
						StringUtils.equals(prop2, ORDER_LAST)
						){
					return -1;
				} else if(StringUtils.equals(prop2, ORDER_FIRST) ||
						StringUtils.equals(prop1, ORDER_LAST)
						){
					return 1;
				}
				
				throw new IllegalStateException("Illegal Filter ORDER combination.");
			}
		
	};

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;

		PluginManager pluginManager = (PluginManager) filterConfig
				.getServletContext()
				.getAttribute(PluginManager.class.getName());

		pluginManager.registerExtensionPoint(this);
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		ServiceReference[] refs = this.osgiFilterTracker.getServiceReferences();
		
		FilterChain chainToUse;
		
		if (refs != null && refs.length > 0) {

			Arrays.sort(refs, SERVICE_REF_COMPARATOR);
			
			Filter[] filters = new Filter[refs.length];
			for(int i=0;i<refs.length;i++) {
				filters[i] = (Filter) this.osgiFilterTracker.getService(refs[i]);
			}
			
			chainToUse = 
				new DelegatingFilterChain(
						chain, filters);
		} else {
			chainToUse = chain;
		}
		
		chainToUse.doFilter(request, response);
	}

	@Override
	public void destroy() {
		Object[] filters = (Object[]) this.osgiFilterTracker.getServices();

		if(filters != null){
			for (Object filter : filters) {
				( (Filter)filter ).destroy();
			}
		}

		this.osgiFilterTracker.close();
	}

	protected Filter[] getTrackedFilters() {
		return (Filter[]) this.osgiFilterTracker.getServices();
	}

	@Override
	public Class<?> getServiceClass() {
		return Filter.class;
	}

	@Override
	public void setServiceTracker(ServiceTracker serviceTracker) {
		this.osgiFilterTracker = serviceTracker;
	}

	@Override
	public ServiceTrackerCustomizer addServiceTrackerCustomizer() {
		return new ServiceTrackerCustomizer() {

			@Override
			public Object addingService(ServiceReference reference) {
				Filter service = (Filter) reference.getBundle()
						.getBundleContext().getService(reference);
				try {
					service.init(filterConfig);
				} catch (ServletException e) {
					throw new IllegalStateException(e);
				}

				return service;
			}

			@Override
			public void modifiedService(ServiceReference reference,
					Object service) {
				//
			}

			@Override
			public void removedService(ServiceReference reference,
					Object service) {
				((Filter) service).destroy();
			}

		};
	}

}
