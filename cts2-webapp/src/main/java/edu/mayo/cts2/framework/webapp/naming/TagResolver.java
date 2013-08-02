package edu.mayo.cts2.framework.webapp.naming;

import org.apache.commons.collections.map.LRUMap;
import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.codesystemversion.CodeSystemVersionCatalogEntry;
import edu.mayo.cts2.framework.model.command.ResolvedReadContext;
import edu.mayo.cts2.framework.model.core.VersionTagReference;
import edu.mayo.cts2.framework.model.exception.ExceptionFactory;
import edu.mayo.cts2.framework.model.extension.LocalIdValueSetDefinition;
import edu.mayo.cts2.framework.model.service.core.NameOrURI;
import edu.mayo.cts2.framework.model.util.ModelUtils;
import edu.mayo.cts2.framework.service.profile.TagAwareReadService;
import edu.mayo.cts2.framework.service.profile.codesystemversion.CodeSystemVersionReadService;
import edu.mayo.cts2.framework.service.profile.valuesetdefinition.ValueSetDefinitionReadService;

@Component
public class TagResolver {

	private static final int CACHE_SIZE = 200;

	private LRUMap tagCache = new LRUMap(CACHE_SIZE);
	
	private interface VersionNameResolver<T> {
		public String getVersionName(T version);
	}
	
	private static VersionNameResolver<CodeSystemVersionCatalogEntry> CODESYSTEM_VERSION_RESOLVER =
		new VersionNameResolver<CodeSystemVersionCatalogEntry>() {
			
			public String getVersionName(CodeSystemVersionCatalogEntry version){
				return version.getCodeSystemVersionName();
			}
	};
	
	private static VersionNameResolver<LocalIdValueSetDefinition> VALUE_SET_DEF_RESOLVER = 
		new VersionNameResolver<LocalIdValueSetDefinition>() {
			public String getVersionName(LocalIdValueSetDefinition def){
				return def.getLocalID();
			}
	};
	
	public String getVersionNameFromTag(
			CodeSystemVersionReadService readService,
			NameOrURI nameOrUri, VersionTagReference tag,
			ResolvedReadContext readContext) {
		
		return this.getVersionNameFromTag(
				readService, 
				CODESYSTEM_VERSION_RESOLVER, 
				nameOrUri, 
				tag, 
				readContext);
	}
	
	public String getVersionNameFromTag(
			ValueSetDefinitionReadService readService,
			NameOrURI nameOrUri, VersionTagReference tag,
			ResolvedReadContext readContext) {
		
		return this.getVersionNameFromTag(
				readService, 
				VALUE_SET_DEF_RESOLVER, 
				nameOrUri, 
				tag, 
				readContext);
	}

	private <T> String getVersionNameFromTag(
			TagAwareReadService<T,?> readService,
			VersionNameResolver<T> nameResolver,
			NameOrURI nameOrUri, VersionTagReference tag,
			ResolvedReadContext readContext) {

		int key = this.getCacheKey(
				nameResolver.getClass().getCanonicalName(), 
				nameOrUri.getName(), 
				tag.getContent());

		if (!this.tagCache.containsKey(key)) {

			T obj = readService
					.readByTag(nameOrUri, tag, readContext);

			if (obj != null) {
				String name = nameResolver.getVersionName(obj);

				this.tagCache.put(key, name);
			} else {
				ExceptionFactory.createUnsupportedVersionTag(
						ModelUtils.nameOrUriFromName(tag.getContent()),
						readService.getSupportedTags());
			}
		} 
		
		return (String) this.tagCache.get(key);
	}
	
	protected int getCacheKey(String... keys) {
		StringBuffer sb = new StringBuffer();
		for (String key : keys) {
			sb.append(key);
		}
		return sb.toString().hashCode();
	}
}
