package edu.mayo.cts2.framework.webapp.rest.controller;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.service.exception.UnknownChangeSet;
import edu.mayo.cts2.framework.model.util.ModelUtils;
import edu.mayo.cts2.framework.service.profile.BaseMaintenanceService;

@Component
public class DeleteHandler {

	protected <I> void delete(
			I identifier,
			String changeSetUri,
			BaseMaintenanceService<?,?,I> service){
		
		if(StringUtils.isBlank(changeSetUri)){
			UnknownChangeSet ex = new UnknownChangeSet();
			ex.setCts2Message(ModelUtils.createOpaqueData(
					"A 'ChangeSetURI' is required to DELETE a Resource. Please supply one and retry your request."));
		
			throw ex;
		}
		
		service.deleteResource(identifier, changeSetUri);
	}

}