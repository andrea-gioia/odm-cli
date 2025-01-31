package org.opendatamesh.cli.config;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import org.opendatamesh.cli.utils.CliFileUtils;
import org.opendatamesh.cli.utils.ObjectMapperUtils;
import org.opendatamesh.platform.pp.blueprint.api.clients.BlueprintClient;
import org.opendatamesh.platform.pp.devops.api.clients.DevOpsClient;
import org.opendatamesh.platform.pp.policy.api.clients.PolicyClient;
import org.opendatamesh.platform.pp.policy.api.clients.PolicyClientImpl;
import org.opendatamesh.platform.pp.registry.api.clients.RegistryClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CliConfig {

    File extensionsFolder;
    File templateFolder;

    Map<String, Map<String, String>> services;
    Map<String, Map<String, String>> systems;
    Map<String, String> env;

    private static CliConfig config = null;

    private CliConfig() {

    }


    public BlueprintClient getBlueprintServiceClient() {
        String serviceUrl = null;
        Map<String, String> propsMap = services.get("blueprint");
        if(propsMap != null) {
            serviceUrl = propsMap.get("url");
        } 

        return new BlueprintClient(serviceUrl);
    }

    public RegistryClient getRegistryServiceClient() {
        String serviceUrl = null;
        Map<String, String> propsMap = services.get("registry");
        if(propsMap != null) {
            serviceUrl = propsMap.get("url");
        } 

        return new RegistryClient(serviceUrl);
    }

    public PolicyClientImpl getPolicyServiceClient() {
        String serviceUrl = null;
        Map<String, String> propsMap = services.get("policy");
        if(propsMap != null) {
            serviceUrl = propsMap.get("url");
        } 

        return new PolicyClientImpl(serviceUrl, ObjectMapperUtils.getObjectMapper());
    }

    public DevOpsClient getDevOpsServiceClient() {
        String serviceUrl = null;
        Map<String, String> propsMap = services.get("devops");
        if(propsMap != null) {
            serviceUrl = propsMap.get("url");
        } 

        return new DevOpsClient(serviceUrl);
    }

    public File getExtensionsFolder() {
        return extensionsFolder;
    }


    public File getTemplateFolder() {
        return templateFolder;
    }
    
    public Map<String, Map<String, String>> getServices() {
        return services;
    }

    private void setServices(Map<String, Map<String, String>> services) {
        this.services = services;
    }

    public Map<String, Map<String, String>> getSystems() {
        return systems;
    }

    private void setSystems(Map<String, Map<String, String>> systems) {
        this.systems = systems;
    }

    public Map<String, String> getEnv() {
        return env;
    }

    private void setEnv(Map<String, String> env) {
        this.env = env;
    }

   
    public static CliConfig getConfig() throws URISyntaxException, IOException {
        if(config == null) config = load();
        return config;
    }

    public static CliConfig load() throws URISyntaxException, IOException {
        File userHomeFolder = new File(System.getProperty("user.home"));
        File configFolder = new File(userHomeFolder, ".odmcli");
        return load(configFolder.toPath());
    }

    
    
   
    public static CliConfig load(Path configFolderPath) throws URISyntaxException, IOException {
        config = new CliConfig();

        File configFolder = configFolderPath.toFile();
        File configFile = new File(configFolder, "config.json");

        if (!configFile.exists()) {
            initConfigFolder(configFolderPath);
        }

        try {
            // Load the JSON file
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(configFile);

            config.setServices(loadMap(rootNode, "services"));
            config.setSystems(loadMap(rootNode, "systems"));
            config.setEnv(loadProperties(rootNode, "env"));

            config.extensionsFolder = new File(configFolder, rootNode.get("extensions-folder").asText());
            config.templateFolder = new File(configFolder, rootNode.get("templates-folder").asText());

            // Print the maps to verify
            System.out.println("Extensions folder: " + config.extensionsFolder);
            System.out.println("Templates folder: " + config.templateFolder);
            System.out.println("Services Map: " + config.services);
            System.out.println("Systems Map: " + config.systems);
            System.out.println("Env Map: " + config.env);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return config;
    }


    private static Map<String, String> loadProperties(JsonNode rootNode, String propertyName) {
        Map<String, String> map =  new HashMap<>();
        JsonNode envNode = rootNode.path(propertyName);
        envNode.fields().forEachRemaining(entry -> map.put(entry.getKey(), entry.getValue().asText()));
        return map;
    }
    private static Map<String, Map<String, String>> loadMap(JsonNode rootNode, String propertyName) {
        Map<String, Map<String, String>> map = new HashMap<>();
        JsonNode targetNode = rootNode.path(propertyName);
        targetNode.fields().forEachRemaining(entry -> {
            Map<String, String> targetDetails = new HashMap<>();
            entry.getValue().fields().forEachRemaining(e -> targetDetails.put(e.getKey(), e.getValue().asText()));
            map.put(entry.getKey(), targetDetails);
        });
        return map;

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
