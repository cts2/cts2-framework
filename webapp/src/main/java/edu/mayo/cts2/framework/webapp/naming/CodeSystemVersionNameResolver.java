package edu.mayo.cts2.framework.webapp.naming;

import org.apache.commons.collections.map.LRUMap;
import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.codesystemversion.CodeSystemVersionCatalogEntry;
import edu.mayo.cts2.framework.model.command.ResolvedReadContext;
import edu.mayo.cts2.framework.model.core.VersionTagReference;
import edu.mayo.cts2.framework.model.exception.ExceptionFactory;
import edu.mayo.cts2.framework.model.service.core.NameOrURI;
import edu.mayo.cts2.framework.model.util.ModelUtils;
import edu.mayo.cts2.framework.service.profile.codesystemversion.CodeSystemVersionReadService;

@Component
public class CodeSystemVersionNameResolver {

	private static final int CACHE_SIZE = 200;
	
	private LRUMap nameCache = new LRUMap(CACHE_SIZE);
	private LRUMap versionIdCache = new LRUMap(CACHE_SIZE);
	private LRUMap tagCache = new LRUMap(CACHE_SIZE);

	public String getCodeSystemVersionNameFromTag(
			CodeSystemVersionReadService codeSystemVersionReadService,
			NameOrURI codeSystem, VersionTagReference tag,
			ResolvedReadContext readContext) {

		int key = this.getCacheKey(codeSystem.getName(), tag.getContent());

		if (!this.tagCache.containsKey(key)) {

			CodeSystemVersionCatalogEntry csv = codeSystemVersionReadService
					.readByTag(codeSystem, tag, readContext);

			if (csv != null) {
				String name = csv.getCodeSystemVersionName();

				this.tagCache.put(key, name);
			} else {
				ExceptionFactory.createUnsupportedVersionTag(
						ModelUtils.nameOrUriFromName(tag.getContent()),
						codeSystemVersionReadService.getSupportedTags());
			}
		} 
		
		return (String) this.tagCache.get(key);
	}

	public String getVersionIdFromCodeSystemVersionName(
			CodeSystemVersionReadService codeSystemVersionReadService,
			String codeSystemVersionName, ResolvedReadContext readContext) {

		int key = this.getCacheKey(codeSystemVersionName);

		if (!this.versionIdCache.containsKey(key)) {

			CodeSystemVersionCatalogEntry csv = null;

			if (codeSystemVersionReadService != null) {
				csv = codeSystemVersionReadService.read(
						ModelUtils.nameOrUriFromName(codeSystemVersionName),
						null);
			}

			String versoinId;

			if (csv != null) {
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
			String codeSystemName, String versionId,
			ResolvedReadContext readContext) {

		int key = this.getCacheKey(codeSystemName, versionId);

		if (!this.nameCache.containsKey(key)) {

			CodeSystemVersionCatalogEntry csv = null;

			if (codeSystemVersionReadService != null) {
				try {
					csv = codeSystemVersionReadService
							.getCodeSystemByVersionId(ModelUtils
									.nameOrUriFromName(codeSystemName),
									versionId, readContext);

					// try without the ReadContext
					if (csv == null) {
						csv = codeSystemVersionReadService
								.getCodeSystemByVersionId(ModelUtils
										.nameOrUriFromName(codeSystemName),
										versionId, null);
					}
				} catch (UnsupportedOperationException e) {
					// if this isn't available, we can't resolve the name from
					// the version id.
				}
			}

			String name;
			if (csv != null) {
				name = csv.getCodeSystemVersionName();
			} else {
				name = versionId;
			}

			this.nameCache.put(key, name);
		}

		return (String) this.nameCache.get(key);
	}

	protected int getCacheKey(String... keys) {
		StringBuffer sb = new StringBuffer();
		for (String key : keys) {
			sb.append(key);
		}
		return sb.toString().hashCode();
	}
}
