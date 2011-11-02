package edu.mayo.cts2.framework.webapp.rest.controller;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.core.ChangeableElementGroup;
import edu.mayo.cts2.framework.model.core.OpaqueData;
import edu.mayo.cts2.framework.model.core.SourceReference;
import edu.mayo.cts2.framework.model.core.types.ChangeType;
import edu.mayo.cts2.framework.model.exception.Cts2RestException;
import edu.mayo.cts2.framework.model.exception.UnspecifiedCts2RuntimeException;
import edu.mayo.cts2.framework.model.service.exception.UnknownChangeSet;
import edu.mayo.cts2.framework.model.updates.ChangeableResourceChoice;
import edu.mayo.cts2.framework.model.util.ModelUtils;
import edu.mayo.cts2.framework.service.profile.BaseMaintenanceService;
import edu.mayo.cts2.framework.service.profile.UpdateChangeableMetadataRequest;

@Component
public class UpdateHandler extends AbstractMainenanceHandler {
	
	@SuppressWarnings("unchecked")
	protected <R,I> void update(
			ChangeableResourceChoice resource, 
			String changeSetUri, 
			I identifier,
			BaseMaintenanceService<R,I> service){
		ChangeableElementGroup group = ModelUtils.getChangeableElementGroup(resource);

		if(group == null){
	
			group = this.createChangeableElementGroup(changeSetUri, ChangeType.UPDATE);
	
			ModelUtils.setChangeableElementGroup(resource, group);
		} else if(group.getChangeDescription() == null){
			throw new UnspecifiedCts2RuntimeException("ChangeDescription must be specified.");
		} else if(group.getChangeDescription().getChangeType() == null){
			throw new UnspecifiedCts2RuntimeException("ChangeType must be specified.");
		} else if(group.getChangeDescription().getChangeType() != ChangeType.METADATA ||
				group.getChangeDescription().getChangeType() != ChangeType.UPDATE){
			throw new UnspecifiedCts2RuntimeException("Only UPDATE or METADATA changes allowed via this URL.");
		}
		
		R cts2Resource = (R) resource.getChoiceValue();
		
		if(StringUtils.isBlank(group.getChangeDescription().getContainingChangeSet())){
			throw new Cts2RestException(new UnknownChangeSet());
		}
		
		ChangeType type = group.getChangeDescription().getChangeType();
		
		switch(type){
			case UPDATE: {
				service.updateResource(cts2Resource);
				break;
			}
			case METADATA: {
				UpdateChangeableMetadataRequest request = this.buildUpdateMetadataRequest(group);
				
				service.updateChangeableMetadata(identifier, request);		
				break;
			}
		}

		service.createResource(cts2Resource);
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