package edu.mayo.cts2.framework.service.profile.update;

import edu.mayo.cts2.framework.model.updates.ChangeSet;
import edu.mayo.cts2.framework.model.updates.ChangeSetDirectoryEntry;
import edu.mayo.cts2.framework.service.profile.QueryService;

public interface ChangeSetQueryExtension extends QueryService<
	ChangeSet, 
	ChangeSetDirectoryEntry, 
	Void> {

}
