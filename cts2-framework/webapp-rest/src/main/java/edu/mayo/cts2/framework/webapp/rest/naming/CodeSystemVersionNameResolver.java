package edu.mayo.cts2.framework.webapp.rest.naming;

import org.apache.commons.collections.map.LRUMap;
import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.codesystemversion.CodeSystemVersionCatalogEntry;
import edu.mayo.cts2.framework.model.util.ModelUtils;
import edu.mayo.cts2.framework.service.profile.codesystemversion.CodeSystemVersionReadService;

@Component
public class CodeSystemVersionNameResolver {

	private LRUMap nameCache = new LRUMap(200);
	private LRUMap versionIdCache = new LRUMap(200);
	
	public String getVersionIdFromCodeSystemVersionName(
			CodeSystemVersionReadService codeSystemVersionReadService,
			String codeSystemVersionName){
		
		int key = this.getCacheKey(codeSystemVersionName);
		
		if(!this.versionIdCache.containsKey(key)){
			
			CodeSystemVersionCatalogEntry csv = null;
			
			if(codeSystemVersionReadService != null){
				csv = 
					codeSystemVersionReadService.read(
							ModelUtils.nameOrUriFromName(codeSystemVersionName), null);
			}
			
			String versoinId;
			
			if(csv != null){
				versoinId = csv.getOfficialResourceVersionId();
			} else {
				versoinId = codeSystemVersionName;
			}
			
			this.nameCache.put(key, versoinId);
		}
		
		return (String) this.nameCache.get(key);
	}
	
	public String getCodeSystemVersionNameFromVersionId(
			CodeSystemVersionReadService codeSystemVersionReadService,
			String codeSystemName, 
			String versionId){
		
		int key = this.getCacheKey(codeSystemName, versionId);
		
		if(!this.nameCache.containsKey(key)){
			
			CodeSystemVersionCatalogEntry csv = null;
			
			if(codeSystemVersionReadService != null){
				csv = 
					codeSystemVersionReadService.getCodeSystemByVersionId(
							ModelUtils.nameOrUriFromName(codeSystemName), versionId, null);
			}
			
			String name;
			if(csv != null){
				name = csv.getCodeSystemVersionName();
			} else {
				name = versionId;
			}
			
			this.nameCache.put(key, name);
		}
		
		return (String) this.nameCache.get(key);
	}
	
	/*
	protected String getVersioIdFromDefaultNameFormat(String codeSystemVersionName){
		return StringUtils.split(codeSystemVersionName)[0];
	}
	
	protected String createCodeSystemVersionName(String codeSystemName, 
			String versionId){
		return codeSystemName + "_" + versionId;
	}
	*/
	
	protected int getCacheKey(String... keys){
		StringBuffer sb = new StringBuffer();
		for(String key : keys){
			sb.append(key);
		}
		return sb.toString().hashCode();
	}
}
