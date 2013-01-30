package edu.mayo.cts2.framework.core.xml;

import java.util.Properties;

import org.springframework.oxm.Marshaller;
import org.springframework.oxm.Unmarshaller;

/**
 * Used for CTS2 Java Bean <-> XML conversions.
 *
 * This Marshaller/UnMarshaller will automatically register all CTS2
 * XSD schemas and other options. This will produce CTS2 compliant XML.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface Cts2Marshaller extends Marshaller, Unmarshaller {
	
	public Properties getCastorBuilderProperties();
	
	public Properties getNamespaceLocationProperties();
	
	public Properties getNamespaceMappingProperties();

}
