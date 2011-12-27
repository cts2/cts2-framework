package edu.mayo.cts2.framework.core.plugin;

import java.util.Dictionary;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;


public class DefaultPluginService<T> implements PluginService<T> {

	private ServiceRegistration<T> registeredService;
	private BundleContext context;

	public DefaultPluginService(BundleContext context, ServiceRegistration<T> registeredService) {
		super();
		this.registeredService = registeredService;
		this.context = context;
	}

	public T getService() {
		return this.context.getService(this.registeredService.getReference());
	}

	@Override
	public void updateProperties(Dictionary<String, ?> newProperties) {
		this.registeredService.setProperties(newProperties);
	}

	@Override
	public void unregister() {
		this.registeredService.unregister();
	}
}
