package edu.mayo.cts2.framework.core.config;

import java.io.IOException;

import javax.annotation.Resource;

import org.apache.felix.metatype.DefaultMetaTypeProvider;
import org.apache.felix.metatype.MetaData;
import org.apache.felix.metatype.MetaDataReader;
import org.osgi.framework.Constants;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.metatype.MetaTypeProvider;
import org.osgi.service.metatype.ObjectClassDefinition;
import org.springframework.core.io.ClassPathResource;
import org.xmlpull.v1.XmlPullParserException;

import edu.mayo.cts2.framework.core.plugin.OsgiPluginManager;
import edu.mayo.cts2.framework.core.plugin.ServiceMetadataAware;

public abstract class AbstractConfigurableExportedService 
	implements MetaTypeProvider, ManagedService, ServiceMetadataAware {
	
	@Resource
	private OsgiPluginManager osgiPluginManager;
	
	protected abstract String getMetatypeXmlPath();

	@Override
	public ObjectClassDefinition getObjectClassDefinition(String id,
			String locale) {
		MetaDataReader reader = new MetaDataReader();
		
		ClassPathResource resource = new ClassPathResource(this.getMetatypeXmlPath());
		
		MetaData result;
		try {
			result = reader.parse(resource.getInputStream());
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (XmlPullParserException e) {
			throw new RuntimeException(e);
		}

		DefaultMetaTypeProvider dmtp = new DefaultMetaTypeProvider(
			this.osgiPluginManager.getBundleContext().getBundle(), result);
		
		String pid = (String) this.getMetadata().get(Constants.SERVICE_PID);
		
		return dmtp.getObjectClassDefinition(pid, null);
	}
}
