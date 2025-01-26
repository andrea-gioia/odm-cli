package org.opendatamesh.odm.cli.utils;

import java.util.Map;

public class CliStringUtils {
    public static String substituteParameters(String template, Map<String, String> parameters) {
        String result = template;

        // Iterate over the map and replace each placeholder with the corresponding
        // value
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            String placeholder = "{" + entry.getKey() + "}";
            result = result.replace(placeholder, entry.getValue());
        }

        return result;
    }
}
