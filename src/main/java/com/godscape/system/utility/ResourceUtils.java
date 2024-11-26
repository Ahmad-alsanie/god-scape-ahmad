package com.godscape.system.utility;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Utility class for resource-related operations.
 */
public class ResourceUtils {

    /**
     * Lists all resource files within a given resource directory inside the JAR.
     *
     * @param resourceDirPath The resource directory path (e.g., "osrs/premade/json").
     * @return A list of resource file paths.
     * @throws IOException If an I/O error occurs.
     */
    public static List<String> listResourceFiles(String resourceDirPath) throws IOException {
        List<String> filenames = new ArrayList<>();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL dirURL = classLoader.getResource(resourceDirPath);

        if (dirURL != null && dirURL.getProtocol().equals("jar")) {
            String jarPath = dirURL.getPath().substring(5, dirURL.getPath().indexOf("!"));
            try (JarFile jar = new JarFile(jarPath)) {
                Enumeration<JarEntry> entries = jar.entries(); // gives ALL entries in jar
                while (entries.hasMoreElements()) {
                    String name = entries.nextElement().getName();
                    if (name.startsWith(resourceDirPath + "/") && !name.endsWith("/")) { // filter according to the path
                        filenames.add(name);
                    }
                }
            }
        } else if (dirURL != null && dirURL.getProtocol().equals("file")) {
            try {
                Path dirPath = Paths.get(dirURL.toURI());
                try (DirectoryStream<Path> stream = Files.newDirectoryStream(dirPath)) {
                    for (Path path : stream) {
                        if (!Files.isDirectory(path)) {
                            filenames.add(resourceDirPath + "/" + path.getFileName().toString());
                        }
                    }
                }
            } catch (URISyntaxException e) {
                throw new IOException("Invalid URI syntax: " + e.getMessage(), e);
            }
        } else {
            throw new IOException("Cannot list files for URL " + dirURL);
        }
        return filenames;
    }
}
