package edu.mayo.cts2.framework.webapp.soap.endpoint;

import edu.mayo.cts2.framework.model.command.Page;
import edu.mayo.cts2.framework.model.command.ResolvedFilter;
import edu.mayo.cts2.framework.model.command.ResolvedReadContext;
import edu.mayo.cts2.framework.model.core.Directory;
import edu.mayo.cts2.framework.model.core.RESTResource;
import edu.mayo.cts2.framework.model.core.types.CompleteDirectory;
import edu.mayo.cts2.framework.model.directory.DirectoryResult;
import edu.mayo.cts2.framework.model.service.core.FilterComponent;
import edu.mayo.cts2.framework.model.service.core.Query;
import edu.mayo.cts2.framework.model.service.core.QueryControl;
import edu.mayo.cts2.framework.model.service.core.ReadContext;
import edu.mayo.cts2.framework.model.wsdl.SoapResolveRequest;
import edu.mayo.cts2.framework.service.profile.QueryService;
import edu.mayo.cts2.framework.service.profile.ResourceQuery;
import edu.mayo.cts2.framework.webapp.soap.directoryuri.DirectoryUriUtils;
import edu.mayo.cts2.framework.webapp.soap.directoryuri.SoapDirectoryUri;
import edu.mayo.cts2.framework.webapp.soap.directoryuri.SoapDirectoryUriRequest;
import edu.mayo.cts2.framework.webapp.soap.resolver.FilterResolver;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


public abstract class AbstractQueryServiceEndpoint extends AbstractEndpoint {

    private FilterResolver filterResolver = new FilterResolver();

    protected <T, I> T doCount(
            final QueryService queryService,
            final I identifier,
            final QueryControl queryControl,
            ReadContext readContext) {
        return null;
    }

    protected <T, I> T doUnion(
            final QueryService queryService,
            final I identifier,
            final QueryControl queryControl,
            ReadContext readContext) {
        return null;
    }

    protected <T, I> T doIntersect(
            final QueryService queryService,
            final I identifier,
            final QueryControl queryControl,
            ReadContext readContext) {
        return null;
    }

    protected <T, I> T doDifference(
            final QueryService queryService,
            final I identifier,
            final QueryControl queryControl,
            ReadContext readContext) {
        return null;
    }

    protected <T, I> T doRestrict(
            final QueryService queryService,
            final I identifier,
            final QueryControl queryControl,
            ReadContext readContext) {
        return null;
    }

    protected <T extends Directory> T populateDirectory(
            DirectoryResult<?> result,
            SoapDirectoryUriRequest<?> request,
            Class<T> directoryClazz) {

        boolean atEnd = result.isAtEnd();

        boolean isComplete = atEnd && (request.getPage() == 0);

        T directory;
        try {
            directory = directoryClazz.newInstance();

            final Field field = ReflectionUtils.findField(directoryClazz,
                    "_entryList");

            AccessController.doPrivileged(new PrivilegedAction<Void>() {
                public Void run() {
                    field.setAccessible(true);

                    return null;
                }
            });

            ReflectionUtils.setField(field, directory, result.getEntries());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (isComplete) {
            directory.setComplete(CompleteDirectory.COMPLETE);
        } else {
            directory.setComplete(CompleteDirectory.PARTIAL);

            //TODO:
        }

        directory.setNumEntries((long) result.getEntries().size());

        directory.setHeading(new RESTResource());
        directory.getHeading().setAccessDate(new Date());
        directory.getHeading().setResourceRoot("soap");
        directory.getHeading().setResourceURI("soap");

        return directory;
    }

    protected interface ResponseBuilder<R,D extends Directory> {

        public R buildResponse(D directory);

    }

    protected <D extends Directory, R> R doResolve(
            final QueryService queryService,
            final SoapResolveRequest request,
            final Class<D> directoryClass,
            final ResponseBuilder<R,D> responseBuilder) {

        @SuppressWarnings("unchecked")
        final SoapDirectoryUriRequest<Void> directoryUriRequest =
                (SoapDirectoryUriRequest<Void>)
                        DirectoryUriUtils.deserialize(request.getDirectory());

        SoapDirectoryUri<Void> directoryUri = directoryUriRequest.getSoapDirectoryUri();

        if (directoryUri.getSetOperation() != null) {
            throw new UnsupportedOperationException("Set Operations not implemented.");
        }

        Set<FilterComponent> filters = directoryUri.getFilterComponents();

        final Set<ResolvedFilter> resolvedFilters = new HashSet<ResolvedFilter>();

        if (CollectionUtils.isNotEmpty(filters)) {
            for (FilterComponent filter : filters) {
                Collection<ResolvedFilter> resolvedFilterCollection =
                        this.filterResolver.resolveFilter(filter, queryService);

                resolvedFilters.addAll(resolvedFilterCollection);
            }
        }

        //TODO: Need to abstract this for other Query Services,
        //and ad Sort, etc.
        DirectoryResult<?> result = queryService.getResourceSummaries(
                new ResourceQuery() {

                    @Override
                    public Query getQuery() {
                        return null;
                    }

                    @Override
                    public Set<ResolvedFilter> getFilterComponent() {
                        return resolvedFilters;
                    }

                    @Override
                    public ResolvedReadContext getReadContext() {
                        return resolveReadContext(request.getContext());
                    }

                },
                null,
                this.getPage(
                        directoryUriRequest.getPage(),
                        request.getQueryControl()));

        D directory =
                this.populateDirectory(
                        result,
                        directoryUriRequest,
                        directoryClass);

        return responseBuilder.buildResponse(directory);
    }

    protected Page getPage(int pageNumber, QueryControl queryControl) {
        throw new UnsupportedOperationException();
    }
}
