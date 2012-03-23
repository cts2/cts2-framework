
package edu.mayo.cts2.framework.webapp.rest.osgi;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.osgi.util.tracker.ServiceTracker;

public class OsgiDispatcherServlet extends
		org.springframework.web.servlet.DispatcherServlet {

	private static final long serialVersionUID = -1902220453736915877L;

	private ServiceTracker osgiServletTracker;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);

		this.osgiServletTracker = (ServiceTracker) config.getServletContext().getAttribute("osgi-servlet-tracker");
	}

	protected void noHandlerFound(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
	
		 HttpServlet dispatcher = (HttpServlet) this.osgiServletTracker.getService();
		 if(dispatcher == null){
			 super.noHandlerFound(request, response);
		 } else {
			 //dispatcher.service(request, response);
			 dispatcher.service(new PathInfoChangingRequest(request), response);
		 }	 
	}
	
	public class PathInfoChangingRequest extends HttpServletRequestWrapper {

		public PathInfoChangingRequest(HttpServletRequest request) {
			super(request);
		}

		@Override
		public String getPathInfo() {
			String pathInfo = super.getPathInfo();
			
			if (StringUtils.isBlank(pathInfo)) {
				String servletPath = super.getServletPath();
				
				return servletPath;
			} else {
				return pathInfo;
			}
		}
	}

}
