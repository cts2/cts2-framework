package edu.mayo.cts2.framework.webapp.naming;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import edu.mayo.cts2.framework.model.codesystemversion.CodeSystemVersionCatalogEntry;
import edu.mayo.cts2.framework.model.command.ResolvedReadContext;
import edu.mayo.cts2.framework.model.util.ModelUtils;
import edu.mayo.cts2.framework.service.profile.codesystemversion.CodeSystemVersionReadService;
import edu.mayo.cts2.framework.service.provider.ServiceProviderChangeObserver;
import org.springframework.stereotype.Component;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Component
public class CodeSystemVersionNameResolver
    extends TagResolver
    implements ServiceProviderChangeObserver {

	private static final int CACHE_SIZE = 200;
    private static final int CACHE_EXPIRE_MINUTES = 10;

    private Cache<Integer, String> nameCache =
        CacheBuilder.newBuilder().
            expireAfterWrite(CACHE_EXPIRE_MINUTES, TimeUnit.MINUTES).maximumSize(CACHE_SIZE).build();

    private Cache<Integer, String> versionIdCache =
        CacheBuilder.newBuilder().
            expireAfterWrite(CACHE_EXPIRE_MINUTES, TimeUnit.MINUTES).maximumSize(CACHE_SIZE).build();

    @Override
    public void onServiceProviderChange() {
        this.clearCaches();
    }

    protected void clearCaches(){
        this.nameCache.invalidateAll();
        this.versionIdCache.invalidateAll();
    }

	public String getVersionIdFromCodeSystemVersionName(
			final CodeSystemVersionReadService codeSystemVersionReadService,
			final String codeSystemVersionName,
            final ResolvedReadContext readContext) {

		int key = this.getCacheKey(codeSystemVersionName);

        try {
            return this.versionIdCache.get(key, new Callable<String>(){

                @Override
                public String call() throws Exception {
                    CodeSystemVersionCatalogEntry csv = null;

                    if (codeSystemVersionReadService != null) {
                        csv = codeSystemVersionReadService.read(
                                ModelUtils.nameOrUriFromName(codeSystemVersionName),
                                null);
                    }

                    String versionId;

                    if (csv != null) {
                        versionId = csv.getOfficialResourceVersionId();
                    } else {
                        versionId = codeSystemVersionName;
                    }

                    return versionId;
                }
            });
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

	public String getCodeSystemVersionNameFromVersionId(
			final CodeSystemVersionReadService codeSystemVersionReadService,
			final String codeSystemName,
            final String versionId,
			final ResolvedReadContext readContext) {

		int key = this.getCacheKey(codeSystemName, versionId);

        try {
            return this.nameCache.get(key, new Callable<String>(){

                @Override
                public String call() throws Exception {
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

                    return name;
                }
            });
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }

    }

	protected int getCacheKey(String... keys) {
		StringBuffer sb = new StringBuffer();
		for (String key : keys) {
			sb.append(key);
		}
		return sb.toString().hashCode();
	}
}
