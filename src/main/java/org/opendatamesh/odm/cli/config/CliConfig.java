package org.opendatamesh.odm.cli.config;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.opendatamesh.odm.cli.utils.CliFileUtils;
import org.opendatamesh.odm.cli.utils.CliStringUtils;

public class CliConfig {

    private CliConfig() {

    }

    public static CliConfig load() throws URISyntaxException, IOException {
        File userHomeFolder = new File(System.getProperty("user.home"));
        File configFolder = new File(userHomeFolder, ".odmcli");
        return load(configFolder.toPath());
    }

    public static CliConfig load(Path configFolderPath) throws URISyntaxException, IOException {
        CliConfig config = new CliConfig();

        File configFolder = configFolderPath.toFile();
        File configFile = new File(configFolder, "config.json");
        
        if (!configFile.exists()) {
            initConfigFolder(configFolderPath);
        }

        

        return config;
    }

    private static void initConfigFolder(Path configFolderPath) throws URISyntaxException, IOException {
        File configFolder = configFolderPath.toFile();
        if (!configFolder.exists()) {
            configFolder.mkdirs();
            System.out.println("Created config folder [" + configFolder.getPath() + "]");
        }

        File configFile = new File(configFolder, "config.json");
        if (!configFile.exists()) {
             String configFileContent = CliFileUtils.readFileFromClasspath("config.json");
             CliFileUtils.writeFile(configFolder, "config.json", configFileContent);

            File extensionsFolder = new File(configFolder, "extensions");
            extensionsFolder.mkdirs();
            System.out.println("Created extensions folder");
            File templatesFolder = new File(configFolder, "templates");
            templatesFolder.mkdirs();
            System.out.println("Created templates folder");

            String fileContent = CliFileUtils.readFileFromClasspath("local/descriptor-template.json");
            CliFileUtils.writeFile(templatesFolder, "descriptor-template.json", fileContent);
        }
    }

}
