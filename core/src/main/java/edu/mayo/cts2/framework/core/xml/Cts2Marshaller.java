package edu.mayo.cts2.framework.core.xml;

import java.util.Properties;

import org.springframework.oxm.Marshaller;
import org.springframework.oxm.Unmarshaller;

public interface Cts2Marshaller extends Marshaller, Unmarshaller {
	
	public Properties getCastorBuilderProperties();
	
	public Properties getNamespaceLocationProperties();
	
	public Properties getNamespaceMappingProperties();


}
