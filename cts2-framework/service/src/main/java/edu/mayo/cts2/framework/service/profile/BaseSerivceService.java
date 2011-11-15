package edu.mayo.cts2.framework.service.profile;

import java.util.Set;

import edu.mayo.cts2.framework.model.core.NamespaceReference;
import edu.mayo.cts2.framework.model.service.core.BaseService;

public interface BaseSerivceService extends Cts2Profile {
	
	public Set<NamespaceReference> getKnownNamespace();
	
	public BaseService getBaseService();

}
