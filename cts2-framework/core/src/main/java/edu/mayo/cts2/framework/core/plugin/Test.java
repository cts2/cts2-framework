package edu.mayo.cts2.framework.core.plugin;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.ServiceLoader;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.launch.Framework;
import org.osgi.framework.launch.FrameworkFactory;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {

		//System.setProperty("osgi.frameworkParentClassloader", "framework");
		//System.setProperty("org.osgi.framework.bootdelegation", "*");
		
		ServiceLoader<FrameworkFactory> ff = ServiceLoader.load(FrameworkFactory.class);
		Map<String, String> config = new HashMap<String,String>();
		//config.put(Constants.FRAMEWORK_SYSTEMPACKAGES_EXTRA,
		//           "edu.mayo.cts2.framework.core.plugin");
		
		config.put("osgi.compatibility.bootdelegation", "false");
		config.put("org.osgi.framework.bootdelegation", "edu.mayo.cts2.framework.*");
		config.put("osgi.parentClassloader","fwk");


		// add some params to config ...
		Framework fwk = ff.iterator().next().newFramework(config);
		fwk.start();
	

		BundleContext bc = fwk.getBundleContext();
		//bc.installBundle("file:/Users/m005256/Downloads/equinox-SDK-3.7.1-1/plugins/org.eclipse.osgi.services_3.3.0.v20110513.jar");
		//bc.installBundle("file:/Users/m005256/Downloads/equinox-SDK-3.7.1-1/plugins/org.eclipse.equinox.ds_1.3.1.R37x_v20110701.jar");
	//	bc.installBundle("file:/Users/m005256/Documents/workspace-sts-2.8.0.RELEASE/testds/plugins/testds_1.0.0.201112211928.jar");
		//bc.installBundle("file:/Users/m005256/Downloads/equinox-SDK-3.7.1-1/plugins/org.eclipse.equinox.util_1.0.300.v20110502.jar");
		//bc.installBundle("file:/Users/m005256/Downloads/equinox-SDK-3.7.1-1/plugins/org.eclipse.equinox.event_1.2.100.v20110502.jar");
		//bc.installBundle("file:/Users/m005256/git/example-service/target/example-service-0.4.2-SNAPSHOT.jar");
		//bc.installBundle("file:/Users/m005256/git/example-service/target/example-service-0.4.2-SNAPSHOT-plugin.jar");
		
		//bc.installBundle("file:/Users/m005256/Downloads/equinox-SDK-3.7.1/plugins/org.eclipse.equinox.cm_1.0.300.v20110502.jar");
		//bc.installBundle("file:/Users/m005256/Downloads/equinox-SDK-3.7.1/plugins/org.eclipse.osgi.services_3.3.0.v20110513.jar");
		bc.installBundle("file:/Users/m005256/git/example-service/target/example-service-0.4.2-SNAPSHOT-plugin.jar");
		
		
		Bundle tb = null;
		for(Bundle bundle : bc.getBundles()){
			System.out.println(bundle.getSymbolicName());
			System.out.println(bundle.getState());
			if(bundle.getSymbolicName().equals("test")){
				bundle.start();
		//bundle.update(new FileInputStream("/Users/m005256/git/example-service/target/example-service-0.4.2-SNAPSHOT-plugin.jar"));
				//bundle.uninstall();
				
				URL resource = 
						bundle.getResource("plugin.properties");
				
				Properties props = new Properties();
				props.load(resource.openStream());
				
				String className = props.getProperty("service.provider.class");
				

				bundle.getBundleContext().registerService("edu.mayo.cts2.framework.core.plugin.Plugin",
						bundle.loadClass(className).newInstance(), null);
				
				
				
				tb = bundle;

				//bundle.start();
				
				Thread.sleep(5000);
			}
		
				try {
				//bundle.uninstall();
				} catch (Exception e) {}
					//				}
				//if(bundle.getSymbolicName().equals("testosgiservice")){
				//	System.out.println("here");
				//	bundle.loadClass("testosgiservice.SimpleLogService");
				//}
		//	}
			//} else {
				bundle.start();
			//}
		} 
		
		bc.registerService(File.class.getName(), new TestServiceFactory(), null);
	

		ServiceReference ref = bc.getServiceReference("edu.mayo.cts2.framework.core.plugin.Plugin");
		//bc.ungetService(ref);
	
		Plugin srv = (Plugin) bc.getService(ref);

		//

		//bc.installBundle("file:/Users/m005256/Documents/workspace-sts-2.8.0.RELEASE/testds/plugins/testds_1.0.0.201112211653.jar");
		System.out.println(fwk.getRegisteredServices().length);
		for(ServiceReference service : fwk.getRegisteredServices()){
			System.out.println(((String[])service.getProperty("objectClass"))[0]);
			System.out.println("Bundle: " + service.getBundle().getSymbolicName());
			//System.out.println("Bundle: " + service.getBundle().getVersion().toString());
		}
		

		fwk.stop();

	}

}
