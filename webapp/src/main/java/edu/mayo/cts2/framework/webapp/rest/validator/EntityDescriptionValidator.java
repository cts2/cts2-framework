package edu.mayo.cts2.framework.webapp.rest.validator;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.command.ResolvedReadContext;
import edu.mayo.cts2.framework.model.core.ChangeableElementGroup;
import edu.mayo.cts2.framework.model.core.CodeSystemVersionReference;
import edu.mayo.cts2.framework.model.entity.EntityDescription;
import edu.mayo.cts2.framework.model.entity.EntityDescriptionBase;
import edu.mayo.cts2.framework.model.exception.ExceptionFactory;
import edu.mayo.cts2.framework.model.util.ModelUtils;
import edu.mayo.cts2.framework.service.profile.codesystemversion.CodeSystemVersionReadService;
import edu.mayo.cts2.framework.webapp.naming.CodeSystemVersionNameResolver;

@Component
public class EntityDescriptionValidator {
	
	protected Log log = LogFactory.getLog(getClass());

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
		
		ChangeableElementGroup group = entity.getChangeableElementGroup();
		
			if(group != null && 
					group.getChangeDescription() != null &&
					StringUtils.isNotBlank(group.getChangeDescription().getContainingChangeSet())){
			String changeSetUri = group.getChangeDescription().getContainingChangeSet();
			
			ResolvedReadContext readContext = new ResolvedReadContext();
			readContext.setChangeSetContextUri(changeSetUri);
			
			String fetchedCodeSytemVersionName;
			try{
				fetchedCodeSytemVersionName = 
						codeSystemVersionNameResolver.getCodeSystemVersionNameFromVersionId(
								codeSystemVersionReadService,
								codeSystemName, 
								versionId,
								readContext);
			} catch(Exception e){
				this.log.info("Unable to validate the CodeSystemVersionName of Updated Entity.");
				return;
			}
			
			if(StringUtils.equals(foundCodeSytemVersionName, fetchedCodeSytemVersionName)){
				ExceptionFactory.createUnknownException("Provded CodeSystemVersionName and URL do not match.");
			}
		} else {
			this.log.info("Unable to validate the CodeSystemVersionName of Updated Entity.");
		}
	}

}
