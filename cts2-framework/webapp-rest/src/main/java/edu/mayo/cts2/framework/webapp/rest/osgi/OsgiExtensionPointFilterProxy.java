package edu.mayo.cts2.framework.webapp.rest.osgi;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

import edu.mayo.cts2.framework.core.plugin.ExtensionPoint;
import edu.mayo.cts2.framework.core.plugin.PluginManager;

public class OsgiExtensionPointFilterProxy implements Filter, ExtensionPoint {

	private ServiceTracker osgiFilterTracker;

	private FilterConfig filterConfig;

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
		Object[] services = this.osgiFilterTracker.getServices();

		FilterChain chainToUse;
		
		if (services != null) {
			Filter[] filters = new Filter[services.length];
			for(int i=0;i<services.length;i++) {
				filters[i] = (Filter) services[i];
			}
			chainToUse = new DelegatingFilterChain(chain, filters);
		} else {
			chainToUse = chain;
		}
		
		chainToUse.doFilter(request, response);
	}

	@Override
	public void destroy() {
		Filter[] filters = (Filter[]) this.osgiFilterTracker.getServices();

		for (Filter filter : filters) {
			filter.destroy();
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
