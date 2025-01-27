package org.opendatamesh.odm.cli.utils;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ServiceLoader;

import org.opendatamesh.odm.cli.commands.local.ImportTool;

public class CliExtensionUtils {
    public static ImportTool getImportTool(String from, String to) {

        String classpath = System.getProperty("java.class.path");
        // Split the classpath using the path separator
        String[] classpathEntries = classpath.split(System.getProperty("path.separator"));

        // Print each entry
        System.out.println("JAR files in the classpath:");
        for (String entry : classpathEntries) {
            if (entry.endsWith(".jar")) { // Only include JAR files
                System.out.println(entry);
            }
        }
        System.out.println("Classpath: " + classpath);
        System.out.println("ImportTool loaded by: " + ImportTool.class.getClassLoader());
        System.out.println("Current thread context class loader: " + Thread.currentThread().getContextClassLoader());

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try {
            addFolderToClasspath("./extensions/odm-platform-cli-extensions-1.0.12.jar", classLoader);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Check if the class loader is an instance of URLClassLoader
        if (classLoader instanceof URLClassLoader) {
            URLClassLoader urlClassLoader = (URLClassLoader) classLoader;

            // Get URLs and print each one
            URL[] urls = urlClassLoader.getURLs();
            System.out.println("JARs in classpath:");
            for (URL url : urls) {
                System.out.println(url);
            }
        } else {
            System.out.println("ClassLoader is not an instance of URLClassLoader.");
        }

        ServiceLoader<ImportTool> loader = ServiceLoader.load(ImportTool.class, classLoader);

        System.out.println("Loader: " + loader);
        for (ImportTool tool : loader) {
            System.out.println(tool.from() + " -> " + tool.to());
            if (from.equalsIgnoreCase(tool.from()) && to.equalsIgnoreCase(tool.to())) {
                return tool;
            }
        }

        return null;
    }

    public static void addFolderToClasspath(String folderPath, ClassLoader classLoader) throws Exception {
        // Convert the folder path to a URL
        File folder = new File(folderPath);
        if (!folder.exists()) {
            throw new IllegalArgumentException("Invalid file path: " + folderPath);
        }
        URL folderUrl = folder.toURI().toURL();

        // Get the system classloader
        URLClassLoader systemClassLoader = (URLClassLoader) classLoader;

        // Use reflection to access the addURL method
        Method addURLMethod = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
        addURLMethod.setAccessible(true);

        // Add the folder URL to the classpath
        addURLMethod.invoke(systemClassLoader, folderUrl);
    }
}
