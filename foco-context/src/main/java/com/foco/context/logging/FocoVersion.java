package com.foco.context.logging;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.CodeSource;
import java.util.jar.Attributes;
import java.util.jar.JarFile;

/**
 * @author lucoo
 * @version 1.0.0
 * @description TODO
 * @date 2021/12/15 18:01
 * @since foco2.1.0
 */
public class FocoVersion {
    private FocoVersion() {
    }
    public static String getVersion(Class mainClass) {
        return determineSpringBootVersion(mainClass);
    }

    private static String determineSpringBootVersion(Class mainClass) {
        String implementationVersion = mainClass.getPackage().getImplementationVersion();
        if (implementationVersion != null) {
            return implementationVersion;
        }
        CodeSource codeSource = FocoVersion.class.getProtectionDomain().getCodeSource();
        if (codeSource == null) {
            return null;
        }
        URL codeSourceLocation = codeSource.getLocation();
        try {
            URLConnection connection = codeSourceLocation.openConnection();
            if (connection instanceof JarURLConnection) {
                return getImplementationVersion(((JarURLConnection) connection).getJarFile());
            }
            try (JarFile jarFile = new JarFile(new File(codeSourceLocation.toURI()))) {
                return getImplementationVersion(jarFile);
            }
        }
        catch (Exception ex) {
            return null;
        }
    }
    private static String getImplementationVersion(JarFile jarFile) throws IOException {
        return jarFile.getManifest().getMainAttributes().getValue(Attributes.Name.IMPLEMENTATION_VERSION);
    }
}
