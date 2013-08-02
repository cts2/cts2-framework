package edu.mayo.cts2.framework.core.url;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import edu.mayo.cts2.framework.model.codesystem.CodeSystemCatalogEntry;
import edu.mayo.cts2.framework.model.codesystemversion.CodeSystemVersionCatalogEntry;
import edu.mayo.cts2.framework.model.core.CodeSystemReference;
import edu.mayo.cts2.framework.model.core.CodeSystemVersionReference;
import edu.mayo.cts2.framework.model.core.NameAndMeaningReference;

public class HrefProcessor {
	
	@Autowired(required=false)
	private UrlConstructor urlConstructor;
	
	public HrefProcessor(UrlConstructor urlConstructor){
		this.urlConstructor = urlConstructor;
	}

	public void processHrefs(String entry, SetUrl setUrl){

		if(entry == null){
			setUrl.setUrl();
		}
	
	}
	
	public void processHrefs(final CodeSystemCatalogEntry entry){

		CodeSystemVersionReference currentVersion = entry.getCurrentVersion();

		this.processHrefs(currentVersion);
		
		
		this.processHrefs(entry.getVersions(), new SetUrl(){

			@Override
			public void setUrl() {
				entry.setVersions(
					urlConstructor.
						createVersionsOfCodeSystemUrl(entry.getCodeSystemName()));
			}
			
		});

	
	}
	
	public void processHrefs(final CodeSystemVersionCatalogEntry entry){

		final CodeSystemReference versionOfReference = entry.getVersionOf();

		this.processHrefs(versionOfReference, new ToUrl(){

			@Override
			public String toUrl() {
				return urlConstructor.createCodeSystemUrl(versionOfReference.getContent());
			}
			
		});
		
		this.processHrefs(entry.getEntityDescriptions(), new SetUrl(){

			@Override
			public void setUrl() {
				entry.setEntityDescriptions(
					urlConstructor.
						createEntitiesOfCodeSystemVersionUrl(
								entry.getVersionOf().getContent(), 
								entry.getCodeSystemVersionName()));
			}
			
		});
		
		this.processHrefs(entry.getEntityDescriptions(), new SetUrl(){

			@Override
			public void setUrl() {
				entry.setAssociations(
					urlConstructor.
						createAssociationsOfCodeSystemVersionUrl(
								entry.getVersionOf().getContent(), 
								entry.getCodeSystemVersionName()));
			}
			
		});
		
		
	}
	
	public void processHrefs(CodeSystemVersionReference ref){
		if(ref == null){
			return;
		}
		
		final CodeSystemReference codeSystemReference = ref.getCodeSystem();
		
		this.processHrefs(
				codeSystemReference, new ToUrl(){

					@Override
					public String toUrl() {
						return urlConstructor.createCodeSystemUrl(
								codeSystemReference.getContent());
					}
					
				});
		
		final NameAndMeaningReference versionReference = ref.getVersion();
		
		this.processHrefs(
				versionReference, new ToUrl(){

					@Override
					public String toUrl() {
						return urlConstructor.createCodeSystemVersionUrl(
								codeSystemReference.getContent(),
								versionReference.getContent());
					}
					
				});
	}
	
	public void processHrefs(NameAndMeaningReference ref, ToUrl toUrl){
		if(ref == null){
			return;
		}
		
		String name = ref.getContent();
		
		if(StringUtils.isNotBlank(name)){
			if(ref.getHref() == null){
				ref.setHref(toUrl.toUrl());
			}
		}
	}
	
	private static interface SetUrl {
		
		public void setUrl();
	}
	
	private static interface ToUrl {
		
		public String toUrl();
	}
}
