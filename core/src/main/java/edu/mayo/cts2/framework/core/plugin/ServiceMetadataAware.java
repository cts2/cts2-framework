package edu.mayo.cts2.framework.core.plugin;

import java.util.Hashtable;

public interface ServiceMetadataAware {

	public Hashtable<String,Object> getMetadata();
	
}
