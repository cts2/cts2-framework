package edu.mayo.cts2.framework.core.xml;

import org.springframework.oxm.Marshaller;
import org.springframework.oxm.Unmarshaller;

import com.atlassian.plugin.spring.AvailableToPlugins;

@AvailableToPlugins
public interface Cts2Marshaller extends Marshaller, Unmarshaller{

}
