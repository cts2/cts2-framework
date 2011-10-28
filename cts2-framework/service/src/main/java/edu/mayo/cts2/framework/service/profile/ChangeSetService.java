package edu.mayo.cts2.framework.service.profile;

import edu.mayo.cts2.framework.model.updates.ChangeSet;

public interface ChangeSetService extends Cts2Profile {
	
	public ChangeSet readChangeSet(String changeSetUri);
	
	public ChangeSet createChangeSet();
	
	public void rollbackChangeSet(String changeSetUri);
	
	public void commitChangeSet(String changeSetUri);


}
