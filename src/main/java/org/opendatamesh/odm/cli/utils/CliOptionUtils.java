package org.opendatamesh.odm.cli.utils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CliOptionUtils {
   
    /*
    public static File getDescriptorFile(String fileOption) throws IOException {
        File descriptorFile = new File(fileOption);

        if (!descriptorFile.exists()) {
            throw new IOException("Descriptor file [" + fileOption + "] does not exist");
        }

        if (descriptorFile.isDirectory()) {
            descriptorFile = new File(descriptorFile, "data-product-descriptor.json");
            if (!descriptorFile.exists()) {
                throw new IOException("Descriptor file [" + fileOption + "] does not exist");
            }
        }

        return descriptorFile;
    }
    */

    public static Map<String, String> parseParams(List<String> params) throws IOException {
        Map<String, String> paramsMap = new HashMap<>();

        if(params == null) {
            return paramsMap;
        }

        for (String p : params) {
            // Split the string into KEY=VALUE format
            String[] parts = p.split("=", 2);
            if (parts.length == 2) {
                paramsMap.put(parts[0], parts[1]);
            } else {
                throw new IOException("Invalid build argument format [" + p + "]");
            }
        }
        return paramsMap;
    }
}
