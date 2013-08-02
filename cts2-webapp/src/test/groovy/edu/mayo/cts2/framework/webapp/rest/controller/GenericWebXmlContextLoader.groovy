package edu.mayo.cts2.framework.webapp.rest.controller;

import javax.servlet.RequestDispatcher;

import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mock.web.MockRequestDispatcher;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.MergedContextConfiguration;
import org.springframework.test.context.support.AbstractContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.GenericWebApplicationContext;

public class GenericWebXmlContextLoader extends AbstractContextLoader {

	private final MockServletContext servletContext;

	public GenericWebXmlContextLoader(String warRootDir, boolean isClasspathRelative) {
		ResourceLoader resourceLoader = isClasspathRelative ? new DefaultResourceLoader() : new FileSystemResourceLoader();
		this.servletContext = initServletContext(warRootDir, resourceLoader);
	}

	private MockServletContext initServletContext(String warRootDir, ResourceLoader resourceLoader) {
		return new MockServletContext(warRootDir, resourceLoader) {
			// Required for DefaultServletHttpRequestHandler...
			public RequestDispatcher getNamedDispatcher(String path) {
				return (path.equals("default")) ? new MockRequestDispatcher(path) : super.getNamedDispatcher(path);
			}
		};
	}

	public ApplicationContext loadContext(MergedContextConfiguration mergedConfig) throws Exception {
		GenericWebApplicationContext context = new GenericWebApplicationContext();
		context.getEnvironment().setActiveProfiles(mergedConfig.getActiveProfiles());
		prepareContext(context);
		new XmlBeanDefinitionReader(context).loadBeanDefinitions(mergedConfig.getLocations());
		AnnotationConfigUtils.registerAnnotationConfigProcessors(context);
		context.refresh();
		context.registerShutdownHook();
		return context;
	}

	public ApplicationContext loadContext(String... locations) throws Exception {
		GenericWebApplicationContext context = new GenericWebApplicationContext();
		prepareContext(context);
		new XmlBeanDefinitionReader(context).loadBeanDefinitions(locations);
		AnnotationConfigUtils.registerAnnotationConfigProcessors(context);
		context.refresh();
		context.registerShutdownHook();
		return context;
	}

	protected void prepareContext(GenericWebApplicationContext context) {
		this.servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, context);
		context.setServletContext(this.servletContext);
	}

	@Override
	protected String getResourceSuffix() {
		return "-context.xml";
	}

}