package edu.mayo.cts2.framework.core.plugin;

import com.atlassian.plugin.osgi.container.PackageScannerConfiguration;
import com.atlassian.plugin.osgi.hostcomponents.HostComponentRegistration;
import com.atlassian.plugin.osgi.util.OsgiHeaderUtil;
import com.atlassian.plugin.util.PluginFrameworkUtils;
import com.google.common.base.Predicate;
import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.twdata.pkgscanner.DefaultOsgiVersionConverter;
import org.twdata.pkgscanner.ExportPackage;
import org.twdata.pkgscanner.PackageScanner;

import static edu.mayo.cts2.framework.core.plugin.ExportBuilderUtils.parseExportFile;
import static edu.mayo.cts2.framework.core.plugin.ExportBuilderUtils.copyUnlessExist;
import static org.twdata.pkgscanner.PackageScanner.exclude;
import static org.twdata.pkgscanner.PackageScanner.include;
import static org.twdata.pkgscanner.PackageScanner.jars;
import static org.twdata.pkgscanner.PackageScanner.packages;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Builds the OSGi package exports string.  Uses a file to cache the scanned results, keyed by the application version.
 */
class ExportsBuilder
{
    static final String JDK_6 = "1.6";
    static final String JDK_7 = "1.7";

    static final String OSGI_PACKAGES_PATH = "osgi-packages.txt";
    static final String JDK_PACKAGES_PATH = "jdk-packages.txt";

    private static Logger log = LoggerFactory.getLogger(ExportsBuilder.class);
    private static String exportStringCache;

    private static final Predicate<String> UNDER_PLUGIN_FRAMEWORK = new Predicate<String>()
    {
        public boolean apply(String pkg)
        {
            return pkg.startsWith("com.atlassian.plugin.");
        }
    };

    /**
     * Gets the framework exports taking into account host components and package scanner configuration.
     * <p>
     * Often, this information will not change without a system restart, so we determine this once and then cache the value.
     * The cache is only useful if the plugin system is thrown away and re-initialised. This is done thousands of times
     * during JIRA functional testing, and the cache was added to speed this up.
     *
     * If needed, call {@link #clearExportCache()} to clear the cache.
     *
     * @param regs The list of host component registrations
     * @param packageScannerConfig The configuration for the package scanning
     * @return A list of exports, in a format compatible with OSGi headers
     */
    public String getExports(PackageScannerConfiguration packageScannerConfig)
    {
        if (exportStringCache == null)
        {
            exportStringCache = determineExports(packageScannerConfig);
        }
        return exportStringCache;
    }

    /**
     * Clears the export string cache. This results in {@link #getExports(java.util.List, com.atlassian.plugin.osgi.container.PackageScannerConfiguration)}
     * having to recalculate the export string next time which can significantly slow down the start up time of plugin framework.
     * @since 2.9.0
     */
    public void clearExportCache()
    {
        exportStringCache = null;
    }

    /**
     * Determines framework exports taking into account host components and package scanner configuration.
     *
     * @param regs The list of host component registrations
     * @param packageScannerConfig The configuration for the package scanning
     * @param cacheDir No longer used. (method deprecated).
     * @return A list of exports, in a format compatible with OSGi headers
     * @deprecated Please use {@link #getExports}. Deprecated since 2.3.6
     */
    @SuppressWarnings ({ "UnusedDeclaration" })
    public String determineExports(PackageScannerConfiguration packageScannerConfig, File cacheDir)
    {
        return determineExports(packageScannerConfig);
    }

    /**
     * Determines framework exports taking into account host components and package scanner configuration.
     *
     * @param regs The list of host component registrations
     * @param packageScannerConfig The configuration for the package scanning
     * @return A list of exports, in a format compatible with OSGi headers
     */
    String determineExports(PackageScannerConfiguration packageScannerConfig)
    {
        Map<String, String> exportPackages = new HashMap<String, String>();

        // The first part is osgi related packages.
        copyUnlessExist(exportPackages, parseExportFile(OSGI_PACKAGES_PATH));

        // The second part is JDK packages.
        copyUnlessExist(exportPackages, parseExportFile(JDK_PACKAGES_PATH));

        // Third part by scanning packages available via classloader. The versions are determined by jar names.
        Collection<ExportPackage> scannedPackages = generateExports(packageScannerConfig);
        copyUnlessExist(exportPackages, ExportBuilderUtils.toMap(scannedPackages));

        // Fourth part by scanning host components since all the classes referred to by them must be available to consumers.
        /*
        try
        {
            Map<String,String> referredPackages = OsgiHeaderUtil.findReferredPackageVersions(packageScannerConfig.getPackageVersions());
            copyUnlessExist(exportPackages, referredPackages);
        }
        catch (IOException ex)
        {
            log.error("Unable to calculate necessary exports based on host components", ex);
        }
        */

        // All the packages under plugin framework namespace must be exported as the plugin framework's version.
        enforceFrameworkVersion(exportPackages);

        // Generate the actual export string in OSGi spec.
        final String exports = OsgiHeaderUtil.generatePackageVersionString(exportPackages);

        if (log.isDebugEnabled())
        {
            log.debug("Exports:\n"+exports.replaceAll(",", "\r\n"));
        }

        return exports;
    }

    private void enforceFrameworkVersion(Map<String, String> exportPackages)
    {
        final String frameworkVersion = PluginFrameworkUtils.getPluginFrameworkVersion();

        // convert the version to OSGi format.
        DefaultOsgiVersionConverter converter = new DefaultOsgiVersionConverter();
        final String frameworkVersionOsgi = converter.getVersion(frameworkVersion);

        for(String pkg: Sets.filter(exportPackages.keySet(), UNDER_PLUGIN_FRAMEWORK))
        {
            exportPackages.put(pkg, frameworkVersionOsgi);
        }
    }

    Collection<ExportPackage> generateExports(PackageScannerConfiguration packageScannerConfig)
    {
        String[] arrType = new String[0];

        Map<String,String> pkgVersions = new HashMap<String,String>(packageScannerConfig.getPackageVersions());
        if (packageScannerConfig.getServletContext() != null)
        {
            String ver = packageScannerConfig.getServletContext().getMajorVersion() + "." + packageScannerConfig.getServletContext().getMinorVersion();
            pkgVersions.put("javax.servlet*", ver);
        }

        PackageScanner scanner = new PackageScanner()
           .select(
               jars(
                       include(packageScannerConfig.getJarIncludes().toArray(arrType)),
                       exclude(packageScannerConfig.getJarExcludes().toArray(arrType))),
               packages(
                       include(packageScannerConfig.getPackageIncludes().toArray(arrType)),
                       exclude(packageScannerConfig.getPackageExcludes().toArray(arrType)))
           )
           .withMappings(pkgVersions);

        if (log.isDebugEnabled())
        {
            scanner.enableDebug();
        }

        Collection<ExportPackage> exports = scanner.scan();
        log.info("Package scan completed. Found " + exports.size() + " packages to export.");

        if (!isPackageScanSuccessful(exports) && packageScannerConfig.getServletContext() != null)
        {
            log.warn("Unable to find expected packages via classloader scanning.  Trying ServletContext scanning...");
            ServletContext ctx = packageScannerConfig.getServletContext();
            try
            {
                exports = scanner.scan(ctx.getResource("/WEB-INF/lib"), ctx.getResource("/WEB-INF/classes"));
            }
            catch (MalformedURLException e)
            {
                log.warn("Unable to scan webapp for packages", e);
            }
        }

        if (!isPackageScanSuccessful(exports))
        {
            throw new IllegalStateException("Unable to find required packages via classloader or servlet context"
                    + " scanning, most likely due to an application server bug.");
        }
        return exports;
    }

    /**
     * Tests to see if a scan of packages to export was successful, using the presence of slf4j as the criteria.
     *
     * @param exports The exports found so far
     * @return True if slf4j is present, false otherwise
     */
    private static boolean isPackageScanSuccessful(Collection<ExportPackage> exports)
    {
        boolean slf4jFound = false;
        for (ExportPackage export : exports)
        {
            if (export.getPackageName().equals("org.slf4j"))
            {
                slf4jFound = true;
                break;
            }
        }
        return slf4jFound;
    }
}