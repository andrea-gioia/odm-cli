package org.opendatamesh.odm.cli.utils;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ServiceLoader;

import org.opendatamesh.odm.cli.commands.local.ImportTool;

public class CliExtensionUtils {

    private static boolean classLoaderIntialized = false;

    public static ImportTool getImportTool(String from, String to) {

        ImportTool tool = null;

        try {
            ClassLoader classLoader = getClassLoader();
            
            ServiceLoader<ImportTool> loader = ServiceLoader.load(ImportTool.class, classLoader);
            for (ImportTool t : loader) {
                System.out.println(t.from() + " -> " + t.to());
                if (from.equalsIgnoreCase(t.from()) && to.equalsIgnoreCase(t.to())) {
                    tool = t;
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return tool;
    }

    private static ClassLoader getClassLoader() throws Exception {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader(); // ImportTool.class.getClassLoader();
        if (classLoaderIntialized == false) {
            addJarInFolderToClassLoader(new File("./extensions"), classLoader);
            classLoaderIntialized = true;
        }

        return classLoader;
    }

    private static void addJarInFolderToClassLoader(File folder, ClassLoader classLoader) throws Exception {
        // Convert the folder path to a URL
        if (!folder.exists() || !folder.isDirectory()) {
            throw new IllegalArgumentException("Invalid folder path: " + folder.getPath());
        }

        // Filter and list all .jar files
        File[] jarFiles = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".jar"));

        // Get the system classloader
        URLClassLoader systemClassLoader = (URLClassLoader) classLoader;

        // Use reflection to access the addURL method
        Method addURLMethod = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
        addURLMethod.setAccessible(true);

        if (jarFiles != null && jarFiles.length > 0) {
            System.out.println("JAR files in folder " + folder.getPath() + ":");
            for (File jarFile : jarFiles) {
                System.out.println(jarFile.getName());
                // Add the folder URL to the classpath
                addURLMethod.invoke(systemClassLoader, jarFile.toURI().toURL());
            }
        }
    }

    // =========
    // Debug
    // =========
    public static void printClassLoader() {
        System.out.println("ImportTool loaded by: " + ImportTool.class.getClassLoader());
        System.out.println("Current thread context class loader: " + Thread.currentThread().getContextClassLoader());
    }

    public static void printJarInClasspath() {
        String classpath = System.getProperty("java.class.path");
        System.out.println("Classpath: " + classpath);

        // Split the classpath using the path separator
        String[] classpathEntries = classpath.split(System.getProperty("path.separator"));

        // Print each entry
        System.out.println("JARs in classpath:");
        for (String entry : classpathEntries) {
            if (entry.endsWith(".jar")) { // Only include JAR files
                System.out.println(entry);
            }
        }
    }

    public static void printJarInClassLoader() throws Exception {
        ClassLoader classLoader = getClassLoader();

        // Check if the class loader is an instance of URLClassLoader
        if (classLoader instanceof URLClassLoader) {
            URLClassLoader urlClassLoader = (URLClassLoader) classLoader;

            // Get URLs and print each one
            URL[] urls = urlClassLoader.getURLs();
            System.out.println("JARs in classloader:");
            for (URL url : urls) {
                System.out.println(url);
            }
        } else {
            System.out.println("ClassLoader is not an instance of URLClassLoader.");
        }

    }
}
