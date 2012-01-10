package edu.mayo.cts2.framework.core.plugin;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.ServletContext;

import org.apache.commons.io.FileUtils;
import org.apache.felix.framework.Felix;
import org.apache.felix.framework.Logger;
import org.apache.felix.framework.util.FelixConstants;
import org.apache.felix.framework.util.StringMap;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.util.tracker.ServiceTracker;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.web.context.ServletContextAware;

import com.atlassian.plugin.osgi.container.OsgiContainerException;
import com.atlassian.plugin.osgi.container.PackageScannerConfiguration;
import com.atlassian.plugin.osgi.container.impl.DefaultPackageScannerConfiguration;

import edu.mayo.cts2.framework.core.config.ConfigInitializer;
import edu.mayo.cts2.framework.core.config.ConfigUtils;

/**
 * Felix implementation of the OSGi container manager
 */
@Component
public class FelixPluginManager implements 
	InitializingBean, PluginManager, ServletContextAware, ApplicationContextAware {
    public static final String OSGI_FRAMEWORK_BUNDLES_ZIP = "osgi-framework-bundles.zip";
    public static final int REFRESH_TIMEOUT = 10;
    
    private ServletContext servletContext;
    
    private ApplicationContext applicationContext;
    
	@Resource
	private ConfigInitializer configInitializer;

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(FelixPluginManager.class);
    private static final String OSGI_BOOTDELEGATION = "org.osgi.framework.bootdelegation";
    private static final String ATLASSIAN_PREFIX = "atlassian.";

    private Collection<ServiceTracker> trackers = new ArrayList<ServiceTracker>();
    private ExportsBuilder exportsBuilder = new ExportsBuilder();

    private Felix felix = null;
    private boolean felixRunning = false;
    private Logger felixLogger = new Logger(){
    	
    };

	@Override
	public void afterPropertiesSet() throws Exception {
		this.start();
	}
	
	protected void autodeployBundles(File pluginDirectory) throws IOException{
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		
		for(org.springframework.core.io.Resource resource : 
			resolver.getResources("classpath:/autodeployBundles/*.jar")){
			
			File bundleFile = new File(
					pluginDirectory.getPath() + File.separator + resource.getFilename());
			
			if(! bundleFile.exists()){
				FileUtils.copyInputStreamToFile(resource.getInputStream(), bundleFile);
			}
		}
		
	}

    public void start() throws OsgiContainerException
    {
        if (isRunning())
        {
            return;
        }
        
        try {
			this.autodeployBundles(this.configInitializer.getPluginsDirectory());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
        
		PackageScannerConfiguration scannerConfig = new DefaultPackageScannerConfiguration();
		scannerConfig.getPackageIncludes().add("edu.mayo.cts2.*");
		scannerConfig.getPackageIncludes().add("org.jaxen*");
		scannerConfig.getPackageIncludes().add("javax.servlet*");
		scannerConfig.getPackageIncludes().add("com.sun*");
		scannerConfig.getPackageIncludes().add("org.json*");
		scannerConfig.getPackageExcludes().add("com.atlassian.plugins*");
	
		scannerConfig.getPackageExcludes().remove("org.apache.commons.logging*");
		scannerConfig.getPackageVersions().put("javax.servlet*", "2.4");
		scannerConfig.getPackageVersions().put("org.apache.commons.collections*", "3.2.1");
		

        // Create a case-insensitive configuration property map.
        final StringMap configMap = new StringMap(false);

        // Add the bundle provided service interface package and the core OSGi
        // packages to be exported from the class path via the system bundle.
        configMap.put(Constants.FRAMEWORK_SYSTEMPACKAGES_EXTRA, exportsBuilder.getExports(scannerConfig));

        // Explicitly specify the directory to use for caching bundles.
        File felixCache = ConfigUtils.createSubDirectory(
        		this.configInitializer.getContextConfigDirectory(),
        		".osgi-felix-cache");
        
        configMap.put(FelixConstants.FRAMEWORK_STORAGE, felixCache.getPath());

        configMap.put(FelixConstants.LOG_LEVEL_PROP, String.valueOf(felixLogger.getLogLevel()));
        configMap.put(FelixConstants.LOG_LOGGER_PROP, felixLogger);
        configMap.put(FelixConstants.FRAGMENT_ATTACHMENT_RESOLVETIME, felixLogger);

        String bootDelegation = getAtlassianSpecificOsgiSystemProperty(OSGI_BOOTDELEGATION);
        if ((bootDelegation == null) || (bootDelegation.trim().length() == 0))
        {
            // These exist to work around JAXP problems.  Specifically, bundles that use static factories to create JAXP
            // instances will execute FactoryFinder with the CCL set to the bundle.  These delegations ensure the appropriate
            // implementation is found and loaded.
            bootDelegation = "weblogic,weblogic.*," +
                             "META-INF.services," +
                             "com.yourkit,com.yourkit.*," +
                             "com.chronon,com.chronon.*," +
                             "com.jprofiler,com.jprofiler.*," +
                             "org.apache.xerces,org.apache.xerces.*," +
                             "org.apache.xalan,org.apache.xalan.*," +
                             "org.apache.xml.serializer," +
                             "sun.*," +
                             "com.sun.xml.bind.v2," +
                             "com.icl.saxon";
        }

        configMap.put(FelixConstants.FRAMEWORK_BOOTDELEGATION, bootDelegation);
        configMap.put(FelixConstants.IMPLICIT_BOOT_DELEGATION_PROP, "false");

        configMap.put(FelixConstants.FRAMEWORK_BUNDLE_PARENT, FelixConstants.FRAMEWORK_BUNDLE_PARENT_FRAMEWORK);
        if (log.isDebugEnabled())
        {
            log.debug("Felix configuration: " + configMap);
        }

        validateConfiguration(configMap);

        try
        {
      
        	final List<BundleActivator> hostServices = new ArrayList<BundleActivator>();
            
            for(Entry<String, Object> bean : 
            	this.applicationContext.getBeansWithAnnotation(ExportedService.class).entrySet()){
            	Object service = bean.getValue();
            	
            	ExportedService annotation = service.getClass().getAnnotation(ExportedService.class);
            	Class<?>[] interfaces = annotation.value();
            	if(interfaces.length == 1 && interfaces[0] == Void.class){
            		interfaces = ClassUtils.getAllInterfaces(service);
            	}
            	
            	hostServices.add(new HostActivator(service, interfaces));	
            }
            
            configMap.put(FelixConstants.SYSTEMBUNDLE_ACTIVATORS_PROP, hostServices);

            // Now create an instance of the framework with
            // our configuration properties and activator.
            felix = new Felix(configMap);
            felixRunning = true;
            
            felix.init();

            for(File bundle : this.configInitializer.getPluginsDirectory().listFiles()){
            	Bundle installedBundle = felix.getBundleContext().installBundle(bundle.toURI().toString());
            	try {
					installedBundle.start();
					log.info("Auto-starting system bundle: " + installedBundle.getSymbolicName());
				} catch (BundleException e) {
					log.warn("Bundle: " + installedBundle.getSymbolicName() + " failed to start.", e);
				}
            }
            
            felix.start();
            
            for(String bean : 
            	this.applicationContext.getBeanNamesForType(ExtensionPoint.class)){
            	
            	ExtensionPoint extensionPoint = this.applicationContext.getBean(bean, ExtensionPoint.class);
            	
            	this.registerExtensionPoint(extensionPoint);
            }
            
            BundleContext context = felix.getBundleContext();
            
            servletContext.setAttribute(
    				BundleContext.class.getName(),
    				context);
        }
        catch (final Exception ex)
        {
            throw new OsgiContainerException("Unable to start OSGi container", ex);
        }
    }
    
    public static class HostActivator implements BundleActivator {
        private BundleContext m_context = null;
        private ServiceRegistration m_registration = null;

        private Object service;
        private Class<?>[] interfaces;
        
        public HostActivator(Object service, Class<?>[] interfaces){
     
        	this.service = service;
        	this.interfaces = interfaces;
        }

        public void start(BundleContext context)
        {
            // Save a reference to the bundle context.
            m_context = context;
           
            // Register the property lookup service and save
            // the service registration.
            m_registration = m_context.registerService(
                this.classesToStrings(
                		this.interfaces),
                this.service, 
                null);
        }

        public void stop(BundleContext context)
        {
            // Unregister the property lookup service.
            m_registration.unregister();
            m_context = null;
        }
        

        private String[] classesToStrings(Class<?>[] classes){
        	String[] strings = new String[classes.length];
        	for(int i=0;i<strings.length;i++){
        		strings[i] = classes[i].getName();
        	}
        	return strings;
        }
    }

    /**
     *
     * @param configMap The Felix configuration
     * @throws OsgiContainerException If any validation fails
     */
    private void validateConfiguration(StringMap configMap) throws OsgiContainerException
    {
        String systemExports = (String) configMap.get(Constants.FRAMEWORK_SYSTEMPACKAGES_EXTRA);
        detectIncorrectOsgiVersion();
        detectXercesOverride(systemExports);
    }

    /**
     * Detect when xerces has no version, most likely due to an installation of Tomcat where an old version of xerces
     * is installed into common/lib/endorsed in order to support Java 1.4.
     *
     * @param systemExports The system exports
     * @throws OsgiContainerException If xerces has no version
     */
    private void detectXercesOverride(String systemExports) throws OsgiContainerException
    {
        int pos = systemExports.indexOf("org.apache.xerces.util");
        if (pos > -1)
        {
            if (pos == 0 || (pos > 0 && systemExports.charAt(pos - 1) == ','))
            {
                pos += "org.apache.xerces.util".length();

                // only fail if no xerces found and xerces has no version
                if (pos >= systemExports.length() || ';' != systemExports.charAt(pos))
                {
                    throw new OsgiContainerException(
                            "Detected an incompatible version of Apache Xerces on the classpath.  If using Tomcat, you may have " +
                                    "an old version of Xerces in $TOMCAT_HOME/common/lib/endorsed that will need to be removed.");
                }
            }
        }
    }

    /**
     * Detects incorrect configuration of WebSphere 6.1 that leaks OSGi 4.0 jars into the application
     */
    private void detectIncorrectOsgiVersion()
    {
        try
        {
            Bundle.class.getMethod("getBundleContext");
        }
        catch (final NoSuchMethodException e)
        {
            throw new OsgiContainerException(
                "Detected older version (4.0 or earlier) of OSGi.  If using WebSphere " + "6.1, please enable application-first (parent-last) classloading and the 'Single classloader for " + "application' WAR classloader policy.");
        }
    }

    public void stop() throws OsgiContainerException
    {
        if (felixRunning)
        {
            for (final ServiceTracker tracker : this.trackers)
            {
                tracker.close();
            }
            try
            {
                felix.stop();
                felix.waitForStop(5000);
            }
            catch (InterruptedException e)
            {
                log.warn("Interrupting Felix shutdown", e);
            }
            catch (BundleException ex)
            {
                log.error("An error occurred while stopping the Felix OSGi Container. ", ex);
            }
        }

        felixRunning = false;
        felix = null;
    }


    public ServiceReference[] getRegisteredServices()
    {
        return felix.getRegisteredServices();
    }

	public ServiceTracker getServiceTracker(final String clazz) {
		if (!isRunning()) {
			throw new IllegalStateException(
					"Unable to create a tracker when osgi is not running");
		}

		final ServiceTracker tracker = new ServiceTracker(
				this.felix.getBundleContext(), clazz, null) {
			@Override
			public void close() {
				super.close();
				trackers.remove(this);
			}
		};

		tracker.open();
		trackers.add(tracker);

		return tracker;
	}
  
    public boolean isRunning()
    {
        return felixRunning;
    }

    private String getAtlassianSpecificOsgiSystemProperty(final String originalSystemProperty)
    {
        return System.getProperty(ATLASSIAN_PREFIX + originalSystemProperty);
    }

	private interface DoWithBundle<T>{
		T doWithBundle(Bundle bundle) throws BundleException;
	}

	@Override
	public void removePlugin(String pluginName, String pluginVersion) {
		this.doWithBundle(pluginName, pluginVersion, new DoWithBundle<Void>(){

			@Override
			public Void doWithBundle(Bundle bundle) throws BundleException {
				bundle.uninstall();
				return null;
			}
			
		});
	}
	
	protected Bundle findBundle(String name, String version){
		for(Bundle bundle : this.felix.getBundleContext().getBundles()){
			if(bundle.getSymbolicName().equals(name) &&
					bundle.getVersion().toString().equals(version)){
				return bundle;
			}
		}
		
		return null;
	}
	
	private <T> T doWithBundle(String name, String version, DoWithBundle<T> closure){
		Bundle bundle = this.findBundle(name, version);
		if(bundle == null){
			log.warn("Plugin: " + name + "version: " + version + " was not found.");
		}
		try {
			return closure.doWithBundle(bundle);
		} catch (BundleException e) {
			throw new RuntimeException(e);
		}
	}


	@Override
	public PluginDescription getPluginDescription(
			String pluginName,
			String pluginVersion) {
		return this.doWithBundle(pluginName, pluginVersion, new DoWithBundle<PluginDescription>(){

			@Override
			public PluginDescription doWithBundle(Bundle bundle)
					throws BundleException {
				return buildPluginDescription(bundle);
			}
			
		});
		
	}
	
	private PluginDescription buildPluginDescription(Bundle bundle){
		return new PluginDescription(
				bundle.getSymbolicName(),
				bundle.getVersion().toString(),
				(String)bundle.getHeaders().get(Constants.BUNDLE_DESCRIPTION),
				bundle.getState() == Bundle.ACTIVE);
	}


	@Override
	public Set<PluginDescription> getPluginDescriptions() {
		Set<PluginDescription> returnSet = new HashSet<PluginDescription>();
		
		for(Bundle bundle : this.felix.getBundleContext().getBundles()){
			returnSet.add(this.buildPluginDescription(bundle));
		}
		
		return returnSet;
	}


	@Override
	public void activatePlugin(String name, String version) {
		this.doWithBundle(name, version, new DoWithBundle<Void>(){

			@Override
			public Void doWithBundle(Bundle bundle) throws BundleException {
				bundle.start();
				return null;
			}
			
		});
	}


	@Override
	public void dectivatePlugin(String name, String version) {
		this.doWithBundle(name, version, new DoWithBundle<Void>(){

			@Override
			public Void doWithBundle(Bundle bundle) throws BundleException {
				bundle.stop();
				return null;
			}
			
		});
	}

	@Override
	public void installPlugin(URL source) throws IOException {
		try {
			this.felix.getBundleContext().installBundle(source.toString());
		} catch (BundleException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean isPluginActive(PluginReference ref) {
		return this.doWithBundle(
				ref.getPluginName(), 
				ref.getPluginVersion(), new DoWithBundle<Boolean>(){

			@Override
			public Boolean doWithBundle(Bundle bundle) throws BundleException {
				return (bundle.getState() == Bundle.ACTIVE);
			}
			
		});
	}


	@Override
	public void registerExtensionPoint(ExtensionPoint extensionPoint) {
		extensionPoint.setServiceTracker(
				this.getServiceTracker(
						extensionPoint.getServiceClass().getName()));
	}


	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;	
	}
}
