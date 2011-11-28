package edu.mayo.cts2.framework.webapp.rest.validator;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.command.ResolvedReadContext;
import edu.mayo.cts2.framework.model.core.ChangeableElementGroup;
import edu.mayo.cts2.framework.model.core.CodeSystemVersionReference;
import edu.mayo.cts2.framework.model.entity.EntityDescription;
import edu.mayo.cts2.framework.model.entity.EntityDescriptionBase;
import edu.mayo.cts2.framework.model.exception.ExceptionFactory;
import edu.mayo.cts2.framework.model.util.ModelUtils;
import edu.mayo.cts2.framework.service.profile.codesystemversion.CodeSystemVersionReadService;
import edu.mayo.cts2.framework.webapp.rest.naming.CodeSystemVersionNameResolver;

@Component
public class EntityDescriptionValidator {
	
	@Resource
	private CodeSystemVersionNameResolver codeSystemVersionNameResolver;
	
	public void validateUpdateEntityDescription(
			CodeSystemVersionReadService codeSystemVersionReadService,
			String codeSystemName, 
			String versionId, 
			EntityDescription entity){
		EntityDescriptionBase base = ModelUtils.getEntity(entity);
		
		CodeSystemVersionReference csvRef = base.getDescribingCodeSystemVersion();
		
		String foundCodeSytemVersionName = csvRef.getVersion().getContent();
		
		ChangeableElementGroup group = ModelUtils.getChangeableElementGroup(entity);
		String changeSetUri = group.getChangeDescription().getContainingChangeSet();
		
		ResolvedReadContext readContext = new ResolvedReadContext();
		readContext.setChangeSetContextUri(changeSetUri);
		
		String fetchedCodeSytemVersionName = 
				codeSystemVersionNameResolver.getCodeSystemVersionNameFromVersionId(
						codeSystemVersionReadService,
						codeSystemName, 
						versionId,
						readContext);
		
		if(StringUtils.equals(foundCodeSytemVersionName, fetchedCodeSytemVersionName)){
			ExceptionFactory.createUnknownException("Provded CodeSystemVersionName and URL do not match.");
		}
	}

}
