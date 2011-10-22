package edu.mayo.cts2.framework.webapp.rest.naming;

import org.apache.commons.collections.map.LRUMap;
import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.codesystemversion.CodeSystemVersionCatalogEntry;
import edu.mayo.cts2.framework.model.util.ModelUtils;
import edu.mayo.cts2.framework.service.profile.codesystemversion.CodeSystemVersionReadService;

@Component
public class CodeSystemVersionNameResolver {

	private LRUMap nameCache = new LRUMap(200);
	
	public String getCodeSystemVersionNameFromVersionId(
			CodeSystemVersionReadService codeSystemVersionReadService,
			String codeSystemName, 
			String versionId){
		
		String key = this.getCacheKey(codeSystemName, versionId);
		
		if(!this.nameCache.containsKey(key)){
			
			CodeSystemVersionCatalogEntry csv = 
					codeSystemVersionReadService.getCodeSystemByVersionId(
							ModelUtils.nameOrUriFromName(codeSystemName), versionId, null);
			
			String name;
			if(csv != null){
				name = csv.getCodeSystemVersionName();
			} else {
				name = this.createCodeSystemVersionName(codeSystemName, versionId);
			}
			
			this.nameCache.put(key, name);
		}
		
		return (String) this.nameCache.get(key);
	}
	
	public String createCodeSystemVersionName(String codeSystemName, 
			String versionId){
		return codeSystemName + "_" + versionId;
	}
	
	
	protected String getCacheKey(String codeSystemName, 
			String versionId){
		return codeSystemName + versionId;
	}
}
