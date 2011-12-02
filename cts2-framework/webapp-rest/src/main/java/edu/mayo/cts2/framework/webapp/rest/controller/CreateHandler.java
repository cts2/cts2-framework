package edu.mayo.cts2.framework.webapp.rest.controller;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.core.constants.URIHelperInterface;
import edu.mayo.cts2.framework.model.core.ChangeableElementGroup;
import edu.mayo.cts2.framework.model.core.IsChangeable;
import edu.mayo.cts2.framework.model.core.types.ChangeType;
import edu.mayo.cts2.framework.model.exception.ExceptionFactory;
import edu.mayo.cts2.framework.service.profile.BaseMaintenanceService;

@Component
public class CreateHandler extends AbstractMainenanceHandler {
	
	@Resource
	private UrlTemplateBindingCreator urlTemplateBindingCreator;
	
	protected <R extends IsChangeable> ResponseEntity<Void> create(
			R resource, 
			String changeSetUri, 
			String urlTemplate,
			UrlTemplateBinder<R> template,
			BaseMaintenanceService<R,?> service){
		
		ChangeableElementGroup group = resource.getChangeableElementGroup();

		if(group == null){
	
			group = this.createChangeableElementGroup(changeSetUri, ChangeType.CREATE);
	
			resource.setChangeableElementGroup(group);
		} else if(group.getChangeDescription() == null){
			group.setChangeDescription(this.createChangeDescription(changeSetUri, ChangeType.CREATE));
		}

		if(StringUtils.isBlank(group.getChangeDescription().getContainingChangeSet())){
			throw ExceptionFactory.createUnknownChangeSetException(null);
		}
		
		R returnedResource = service.createResource(resource);
	
		return this.createResponseEntity(
				returnedResource, 
				changeSetUri, 
				urlTemplate, 
				template);
	}
	
	protected <R> ResponseEntity<Void> createResponseEntity(
			R returnedResource,
			String changeSetUri, 
			String urlTemplate,
			UrlTemplateBinder<R> template){
		
		String location = this.urlTemplateBindingCreator.bindResourceToUrlTemplate(
				template,
				returnedResource, 
				urlTemplate);
		
		if(StringUtils.isNotBlank(changeSetUri)){
			location = location + ("?" + URIHelperInterface.PARAM_CHANGESETCONTEXT + "=" +  changeSetUri);
		}
		
		return this.createResponseEntity(location);
	}
	
	protected <R> ResponseEntity<Void> createResponseEntity(
			String location){
		HttpHeaders responseHeaders = new HttpHeaders();
		
		responseHeaders.set("Location", location);
		
		return new ResponseEntity<Void>(responseHeaders, HttpStatus.CREATED);
	}

}