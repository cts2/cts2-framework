package edu.mayo.cts2.framework.service.profile.entitydescription;

import java.util.Set;

import edu.mayo.cts2.framework.model.core.EntityExpression;
import edu.mayo.cts2.framework.model.core.OntologyLanguageAndSyntax;
import edu.mayo.cts2.framework.model.entity.EntityDescription;
import edu.mayo.cts2.framework.model.service.core.NameOrURI;
import edu.mayo.cts2.framework.model.service.core.types.FunctionalProfile;
import edu.mayo.cts2.framework.model.service.core.types.StructuralProfile;
import edu.mayo.cts2.framework.service.profile.BaseService;
import edu.mayo.cts2.framework.service.profile.Cts2Profile;
import edu.mayo.cts2.framework.service.profile.FunctionalConformance;
import edu.mayo.cts2.framework.service.profile.StructuralConformance;

@StructuralConformance(StructuralProfile.SP_ENTITY_DESCRIPTION)
@FunctionalConformance(FunctionalProfile.FP_READ)
public interface EntityDescriptionTransformService extends BaseService, Cts2Profile {

	public EntityExpression fromEntityDirectory(
			OntologyLanguageAndSyntax ontologyLanguageAndSyntax,
			EntityDescriptionQuery restrictions);
	
	public Set<EntityDescription> toEntityDescriptions(
			EntityExpression entityExpression,
			NameOrURI codeSystemVersion);
	
	public EntityExpression fromEntityDescriptions(
			OntologyLanguageAndSyntax ontologyLanguageAndSyntax,
			Set<EntityDescription> entities);
	
	public Set<? extends OntologyLanguageAndSyntax> getSupportedOntologyLanguageAndSyntax();
}
