package edu.mayo.cts2.framework.service.profile;

import java.net.URI;
import java.util.Date;

import edu.mayo.cts2.framework.model.core.OpaqueData;
import edu.mayo.cts2.framework.model.service.core.NameOrURI;
import edu.mayo.cts2.framework.model.updates.ChangeSet;

public interface ChangeSetService extends Cts2Profile {
	
	public ChangeSet readChangeSet(String changeSetUri);
	
	public ChangeSet createChangeSet();
	
	public void updateChangeSetMetadata(
			String changeSetUri, 
			NameOrURI creator, 
			OpaqueData changeInstructions,
			Date officialEffectiveDate);
	
	public void rollbackChangeSet(String changeSetUri);
	
	public void commitChangeSet(String changeSetUri);

	public String importChangeSet(URI changeSetUri);

}
