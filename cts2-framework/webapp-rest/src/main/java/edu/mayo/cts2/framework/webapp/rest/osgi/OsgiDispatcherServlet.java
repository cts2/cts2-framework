package edu.mayo.cts2.framework.webapp.rest.osgi;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.util.tracker.ServiceTracker;
import org.springframework.web.multipart.MultipartResolver;

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
			 /*
			 MultipartResolver resolver = this.getMultipartResolver();
			 
			 HttpServletRequest resolvedRequest;
			 if(resolver.isMultipart(request)){
				 resolvedRequest = resolver.resolveMultipart(request);
			 } else {
				 resolvedRequest = request;
			 }
			 */
			 dispatcher.service(request, response);
		 }	 
	}
}
