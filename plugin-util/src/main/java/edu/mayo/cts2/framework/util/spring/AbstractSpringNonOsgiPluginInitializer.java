package edu.mayo.cts2.framework.util.spring;

import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.osgi.context.support.OsgiBundleXmlApplicationContext;

import edu.mayo.cts2.framework.core.plugin.NonOsgiPluginInitializer;
import edu.mayo.cts2.framework.core.plugin.OsgiPluginManager;

public abstract class AbstractSpringNonOsgiPluginInitializer implements
		NonOsgiPluginInitializer {

	@Override
	public void initialize(OsgiPluginManager osgiPluginManager) {
		final ClassLoader cl = this.getClass().getClassLoader();
		OsgiBundleXmlApplicationContext ctx = new OsgiBundleXmlApplicationContext(
				this.getContextConfigLocations()) {

			@Override
			public ClassLoader getClassLoader() {
				return cl;
			}

			@Override
			protected ResourcePatternResolver createResourcePatternResolver() {
				return new PathMatchingResourcePatternResolver();
			}

		};

		ctx.setBundleContext(osgiPluginManager.getBundleContext());

		ctx.refresh();
	}

	protected abstract String[] getContextConfigLocations();

}
