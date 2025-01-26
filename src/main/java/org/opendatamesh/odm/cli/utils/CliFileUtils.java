package org.opendatamesh.odm.cli.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Properties;

public final class CliFileUtils {

    public static String readFileFromPath(String filePath) throws IOException {

        Path resolvedPath = Paths.get(filePath).toAbsolutePath();

        if (!Files.exists(resolvedPath) || !Files.isRegularFile(resolvedPath)) {
            throw new IOException("File [" + resolvedPath + "] not found");
        }

        try {
            String fileContent = Files.readString(resolvedPath, StandardCharsets.UTF_8);
            System.out.println("Reading file [" + resolvedPath + "]");
            return fileContent;
        } catch (IOException e) {
            throw new IOException("Error reading the file [" + resolvedPath + "]: " + e.getMessage());
        }
    }

    public static String readFileFromClasspath(String filePath) throws IOException {

        InputStream fileInputStream = CliFileUtils.class.getClassLoader().getResourceAsStream(filePath);

        if (fileInputStream == null) {
            throw new IOException("File [" + filePath + "] not found");
        }

        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append(System.lineSeparator());
            }
        }
        return content.toString();
    }

    public static Properties getPropertiesFromFilePath(String localPath) throws IOException {
        String properties = readFileFromPath(localPath);
        final Properties p = new Properties();
        p.load(new StringReader(properties));
        return p;
    }

    public static void writeFile(String rootFoolder, String fileName, String fileContent) throws IOException {
        writeFile(new File(rootFoolder), fileName, fileContent);
    }
    public static void writeFile(File folder, String fileName, String fileContent) throws IOException {
        File file = new File(folder, fileName);

        createFolder(file.getParentFile());
        
        if (!file.exists()) {
            file.createNewFile(); // Create the file
        }

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(fileContent);
        }
    }

    
    public static void createFileFromTemplate(String folderPath, String fileName, String templatePath, Map<String, String> initParamsMap) {
        createFileFromTemplate(new File(folderPath), fileName, templatePath, initParamsMap);
    }

    public static void createFileFromTemplate(File folder, String fileName, String templatePath, Map<String, String> initParamsMap) {
        try {
            // Read the content from the template file
            String descriptorTemplate = CliFileUtils.readFileFromClasspath(templatePath);
            if (initParamsMap != null) {
                descriptorTemplate = CliStringUtils.substituteParameters(descriptorTemplate, initParamsMap);
            }

            CliFileUtils.writeFile(folder, fileName, descriptorTemplate);
        } catch (IOException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }


    public static boolean createFolder(File parentFolder, String folderName) throws IOException {
        return createFolder(new File(parentFolder, folderName));
    }

    public static boolean createFolder(String parentFolderPath, String folderName) throws IOException {
        return createFolder(new File(parentFolderPath, folderName));
    }

    public static boolean createFolder(String rootFoolder) throws IOException {
        return createFolder(new File(rootFoolder));
    }

    public static boolean createFolder(File folder) throws IOException {
        if (!folder.exists()) {
            boolean created = folder.mkdirs();
            if (!created) {
                throw new IOException("Failed to create folder [" + folder.toPath() + "].");
            }
            return true;
        } else {
            return false;
        }
    }

}
