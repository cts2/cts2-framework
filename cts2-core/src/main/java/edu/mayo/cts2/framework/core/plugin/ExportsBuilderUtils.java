package edu.mayo.cts2.framework.core.plugin;

import com.atlassian.plugin.util.ClassLoaderUtils;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.apache.commons.io.IOUtils;
import org.osgi.framework.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.twdata.pkgscanner.DefaultOsgiVersionConverter;
import org.twdata.pkgscanner.ExportPackage;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

final class ExportBuilderUtils
{
    private static Logger LOG = LoggerFactory.getLogger(ExportBuilderUtils.class);
    private static final DefaultOsgiVersionConverter converter = new DefaultOsgiVersionConverter();
    private static final String EMPTY_OSGI_VERSION = Version.emptyVersion.toString();

    /**
     * Not for instantiation.
     */
    private ExportBuilderUtils()
    {}

    /**
     * Convert version string into OSGi format.
     */
    private static final Function<String, String> CONVERT_VERSION = new Function<String, String>()
    {
        public String apply(String from)
        {
            if (from != null && (from.trim().length() > 0))
            {
                return converter.getVersion(from);
            }
            else
            {
                return EMPTY_OSGI_VERSION;
            }
        }
    };

    /**
     * Reads export file and return a map of package->version.
     * Returned versions are in OSGi format which can be 0.0.0 if not specified in the file.
     *
     * @param exportFilePath the file path, never null.
     *
     * @return map of package->version, never null.
     */
    static Map<String, String> parseExportFile(String exportFilePath)
    {
        Properties props = new Properties();
        InputStream in = null;

        try
        {
            in = ClassLoaderUtils.getResourceAsStream(exportFilePath, ExportBuilderUtils.class);
            // this should automatically get rid of comment lines.
            props.load(in);
        }
        catch (IOException e)
        {
            LOG.warn("Problem occurred while processing package export:" + exportFilePath, e);
            return ImmutableMap.of();
        }
        finally
        {
            IOUtils.closeQuietly(in);
        }

        // convert version strings to osgi format and return the resultant map.
        // this Maps.transformValues returns a view backed by immutable map in this case so itself is already immutable.
        return Maps.transformValues(Maps.fromProperties(props), CONVERT_VERSION);
    }

    /**
     * Copies all the entries from src into dest for the keys that don't already exist in dest.
     */
    static void copyUnlessExist(final Map<String, String> dest, final Map<String, String> src)
    {
        dest.putAll(Maps.filterKeys(src, new Predicate<String>()
        {
            public boolean apply(String key)
            {
                return !dest.containsKey(key);
            }
        }));
    }

    /**
     * Converts collection of ExportPackage into map of packageName->version.
     */
    static Map<String, String> toMap(Iterable<ExportPackage> exportPackages)
    {
        Map<String, String> output = new HashMap<String, String>();
        for (ExportPackage pkg : exportPackages)
        {
            String version = pkg.getVersion() == null ? EMPTY_OSGI_VERSION : pkg.getVersion();
            output.put(pkg.getPackageName(), version);
        }
        return ImmutableMap.copyOf(output);
    }
}
