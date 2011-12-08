package edu.mayo.cts2.framework.webapp.rest.controller;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.core.ChangeableElementGroup;
import edu.mayo.cts2.framework.model.core.IsChangeable;
import edu.mayo.cts2.framework.model.core.OpaqueData;
import edu.mayo.cts2.framework.model.core.SourceReference;
import edu.mayo.cts2.framework.model.core.types.ChangeType;
import edu.mayo.cts2.framework.model.exception.UnspecifiedCts2RuntimeException;
import edu.mayo.cts2.framework.model.exception.changeset.UnknownChangeSetException;
import edu.mayo.cts2.framework.service.profile.BaseMaintenanceService;
import edu.mayo.cts2.framework.service.profile.UpdateChangeableMetadataRequest;

@Component
public class UpdateHandler extends AbstractMainenanceHandler {

	protected <T extends IsChangeable,R extends IsChangeable,I> void update(
			T resource, 
			String changeSetUri, 
			I identifier,
			BaseMaintenanceService<T,R,I> service){
		ChangeableElementGroup group = resource.getChangeableElementGroup();

		if(group == null){
	
			group = this.createChangeableElementGroup(changeSetUri, ChangeType.UPDATE);
	
			resource.setChangeableElementGroup(group);
		} else if(group.getChangeDescription() == null){
			throw new UnspecifiedCts2RuntimeException("ChangeDescription must be specified.");
		} else if(group.getChangeDescription().getChangeType() == null){
			throw new UnspecifiedCts2RuntimeException("ChangeType must be specified.");
		} else if(! (group.getChangeDescription().getChangeType() != ChangeType.METADATA ||
				group.getChangeDescription().getChangeType() != ChangeType.UPDATE) ){
			throw new UnspecifiedCts2RuntimeException("Only UPDATE or METADATA changes allowed via this URL.");
		}

		if(StringUtils.isBlank(group.getChangeDescription().getContainingChangeSet())){
			throw new UnknownChangeSetException(null);
		}
		
		ChangeType type = group.getChangeDescription().getChangeType();
		
		switch(type){
			case UPDATE: {
				service.updateResource(resource);
				break;
			}
			case METADATA: {
				UpdateChangeableMetadataRequest request = this.buildUpdateMetadataRequest(group);
				
				service.updateChangeableMetadata(identifier, request);		
				break;
			}
		}
	}
	
	protected UpdateChangeableMetadataRequest buildUpdateMetadataRequest(ChangeableElementGroup group){
		UpdateChangeableMetadataRequest request = new UpdateChangeableMetadataRequest();
		if(group.getStatus() != null){
			request.setStatus(group.getStatus());
		}
		
		OpaqueData changeNotes = group.getChangeDescription().getChangeNotes();
		if(changeNotes != null){
			request.setChangeNotes(changeNotes);
		}
		
		SourceReference changeSource = group.getChangeDescription().getChangeSource();
		if(changeNotes != null){
			request.setChangeSource(changeSource);
		}
		
		return request;
	}

}