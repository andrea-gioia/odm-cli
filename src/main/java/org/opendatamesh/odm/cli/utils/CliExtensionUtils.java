package org.opendatamesh.odm.cli.utils;

import java.util.ServiceLoader;

import org.opendatamesh.odm.cli.commands.local.ImportTool;

public class CliExtensionUtils {
    public static ImportTool getImportTool(String from, String to) {

        System.out.println("ImportTool loaded by: " + ImportTool.class.getClassLoader());
        System.out.println("Current thread context class loader: " + Thread.currentThread().getContextClassLoader());

        ServiceLoader<ImportTool> loader = ServiceLoader.load(ImportTool.class, Thread.currentThread().getContextClassLoader());

        System.out.println("Loader: " + loader);
        for (ImportTool tool : loader) {
            System.out.println(tool.from() + " -> " + tool.to());
            if (from.equalsIgnoreCase(tool.from()) && to.equalsIgnoreCase(tool.to())) {
                return tool;
            }
        }

        return null;
    }
}
