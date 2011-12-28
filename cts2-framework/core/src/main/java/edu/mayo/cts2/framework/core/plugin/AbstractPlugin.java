package edu.mayo.cts2.framework.core.plugin;

import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.cm.ManagedService;
import org.osgi.util.tracker.ServiceTracker;

import edu.mayo.cts2.framework.core.config.option.Option;

public abstract class AbstractPlugin implements Plugin {
	
	protected Log log = LogFactory.getLog(getClass());

	private BundleContext bundleContext;

	private ServiceTracker<ConfigurationAdmin,ConfigurationAdmin> configurationAdmin;
	
	private ServiceRegistration<?> registration;

	@Override
	public void start(BundleContext context) throws Exception {
		this.bundleContext = context;
		/*
		ServiceReference<PluginConfig> reference = 
				bundleContext.getServiceReference(PluginConfig.class);
	
		this.pluginConfig = 
				new ServiceTracker<PluginConfig,PluginConfig>(
						bundleContext, 
						reference,
						null);
		this.pluginConfig.open(true);
		
		this.bundleContext.addServiceListener(new ServiceListener(){

			@Override
			public void serviceChanged(ServiceEvent event) {
				System.out.println(event);
			}
			
		});
		/*
		this.configurationAdmin = 
				new ServiceTracker(bundleContext, ConfigurationAdmin.class.getName(), null);
		this.configurationAdmin.open(true);
*/
		this.registerService();

	}

	private void registerService() {
		String bundleName = this.getBundleName();
		
		this.log.info("Registering Plugin: " + bundleName);
		
		Hashtable<String,String> props = new Hashtable<String,String>();
		props.put(
				Constants.SERVICE_PID,
				bundleName);

		this.getBundleContext().registerService(
						new String[]{
							ManagedService.class.getName(),
						},
						this, 
						props);
	
	}
	
	protected String getBundleName(){
		return this.getBundleContext().getBundle().getSymbolicName();
	}
	
	protected abstract Class<?> getPluginServiceInterface();

	@SuppressWarnings("rawtypes")
	@Override
	public void updated(Dictionary properties) {
		log.info("Properties updating on Plugin: " + this.getBundleName());
		
		ServiceReference<PluginConfig> reference = 
				bundleContext.getServiceReference(PluginConfig.class);
	
		ServiceTracker<PluginConfig,PluginConfig> pluginConfig = 
				new ServiceTracker<PluginConfig,PluginConfig>(
						bundleContext, 
						reference,
						null);
		pluginConfig.open(true);
		
		this.initialize(pluginConfig.getService());

		if(this.registration != null){
			registration.unregister();
		}
		
		this.registration = this.getBundleContext().registerService(
				new String[]{
					this.getPluginServiceInterface().getName()
				},
				this, 
				properties);
	}

	@Override
	public void updatePluginOptions(Set<Option> newOptions) {
		Configuration config;
		try {
			config = this.configurationAdmin.getService().getConfiguration(
					this.getBundleContext().getBundle().getSymbolicName());
			
			this.updateDictionary(config.getProperties(), newOptions);
			config.update(config.getProperties());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	private Dictionary<String,String> optionsToDictionary(Set<Option> options){
		Dictionary<String,String> dictionary = new Hashtable<String,String>();
		for(Option option : options){
			dictionary.put(option.getOptionName(), option.getOptionValueAsString());
		}
		return dictionary;
	}

	private void updateDictionary(Dictionary dictionary, Set<Option> options){
		if(options != null){
			for(Option option : options){
				dictionary.put(option.getOptionName(), option.getOptionValueAsString());
			}
		}
	}
	
	protected BundleContext getBundleContext(){
		return this.bundleContext;
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		this.destroy();
	}

}
