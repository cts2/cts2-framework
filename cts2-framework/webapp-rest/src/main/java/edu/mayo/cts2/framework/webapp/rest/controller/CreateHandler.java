package edu.mayo.cts2.framework.webapp.rest.controller;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.core.ChangeableElementGroup;
import edu.mayo.cts2.framework.model.core.types.ChangeType;
import edu.mayo.cts2.framework.model.exception.Cts2RestException;
import edu.mayo.cts2.framework.model.exception.UnspecifiedCts2RuntimeException;
import edu.mayo.cts2.framework.model.service.exception.UnknownChangeSet;
import edu.mayo.cts2.framework.model.updates.ChangeableResourceChoice;
import edu.mayo.cts2.framework.model.util.ModelUtils;
import edu.mayo.cts2.framework.service.profile.BaseMaintenanceService;

@Component
public class CreateHandler extends AbstractMainenanceHandler {
	
	@Resource
	private UrlTemplateBindingCreator urlTemplateBindingCreator;
	
	@SuppressWarnings("unchecked")
	protected <R> ResponseEntity<Void> create(
			ChangeableResourceChoice resource, 
			String changeSetUri, 
			String urlTemplate,
			UrlTemplateBinder<R> template,
			BaseMaintenanceService<R,?> service){
		ChangeableElementGroup group = ModelUtils.getChangeableElementGroup(resource);

		if(group == null){
	
			group = this.createChangeableElementGroup(changeSetUri, ChangeType.CREATE);
	
			ModelUtils.setChangeableElementGroup(resource, group);
		} else if(group.getChangeDescription() == null){
			group.setChangeDescription(this.createChangeDescription(changeSetUri, ChangeType.CREATE));
		}
		
		R cts2Resource = (R) resource.getChoiceValue();
		
		if(StringUtils.isBlank(group.getChangeDescription().getContainingChangeSet())){
			throw new Cts2RestException(new UnknownChangeSet());
		}
		
		service.createResource(cts2Resource);
		
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("Location", this.urlTemplateBindingCreator.bindResourceToUrlTemplate(
				template,
				cts2Resource, 
				urlTemplate) + "?changesetcontext=" +  changeSetUri);
		
		return new ResponseEntity<Void>(responseHeaders, HttpStatus.CREATED);
	}

}