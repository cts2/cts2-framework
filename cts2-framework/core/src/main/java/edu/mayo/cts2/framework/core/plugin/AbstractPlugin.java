package edu.mayo.cts2.framework.core.plugin;

import java.util.Collection;
import java.util.Dictionary;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.util.tracker.ServiceTracker;
import org.springframework.util.CollectionUtils;

public abstract class AbstractPlugin implements Plugin {

	private BundleContext bundleContext;
	
	private Set<PluginService<?>> services = 
			new HashSet<PluginService<?>>();

	@Override
	public void start(BundleContext context) throws Exception {
		this.bundleContext = context;
		
		ServiceReference<PluginConfig> reference = 
				bundleContext.getServiceReference(PluginConfig.class);
	
		ServiceTracker<PluginConfig,PluginConfig> tracker = 
				new ServiceTracker<PluginConfig,PluginConfig>(
						bundleContext, 
						reference,
						null);
		
		PluginConfig config = tracker.getService();
		
		this.initialize(config);
		
		this.registerServices(this.getServicesToRegister());
	}

	@Override
	public Iterable<PluginService<?>> getPluginServices() {
		return this.services;
	}


	protected abstract Collection<ServiceToRegister> getServicesToRegister();
	
	private void registerServices(Collection<ServiceToRegister> services){
		if(CollectionUtils.isEmpty(services)){
			return;
		}
		
		for(ServiceToRegister service : services){
			String clazz = service.getInterfaceClass();
			Object serviceObj = service.getServiceObject();
			Dictionary<String,?> dictionary = service.getDictionary();
			
			@SuppressWarnings("unchecked")
			ServiceRegistration<Object> registeredService = 
					(ServiceRegistration<Object>) 
					this.getBundleContext().registerService(clazz, serviceObj, dictionary);
			
			this.services.add(
					new DefaultPluginService<Object>(
							this.getBundleContext(), 
							registeredService));
		}
	}
	
	protected BundleContext getBundleContext(){
		return this.bundleContext;
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		this.destroy();
	}
	
	protected static class ServiceToRegister {
		
		private String interfaceClass;
		private Object serviceObject;
		private Dictionary<String,?> dictionary;
		
		public ServiceToRegister(String interfaceClass, Object serviceObject, Dictionary<String,?> dictionary) {
			super();
			this.interfaceClass = interfaceClass;
			this.serviceObject = serviceObject;
			this.dictionary = dictionary;
		}

		private String getInterfaceClass() {
			return interfaceClass;
		}
	
		private Object getServiceObject() {
			return serviceObject;
		}

		private Dictionary<String,?> getDictionary() {
			return dictionary;
		}

	}

}
