package org.opendatamesh.odm.cli.commands.local.subcommands;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.opendatamesh.odm.cli.config.CliConfig;
import org.opendatamesh.odm.cli.utils.CliFileUtils;
import org.opendatamesh.odm.cli.utils.CliOptionUtils;

@Command(name = "init", description = "Initialize a descriptor file", version = "odm-cli local init 1.0.0", mixinStandardHelpOptions = true)
public class InitCommand implements Runnable {

    @Option(names = { "-f",
            "--file" }, description = "Name of the descriptor file (default: PATH/data-product-descriptor.json)", required = false, defaultValue = "./data-product-descriptor.json")
    String descriptorFilePath;
    @Option(names = "--init-param", description = "Init parameter", arity = "0..*", paramLabel = "<KEY=VALUE>")
    private List<String> initParams;

    private static final String DESCRIPTOR_TEMPLATE_FILEPATH = "local/descriptor-template.json";

    @Override
    public void run() {

        try {

            CliConfig.load();
            
            File descriptorFile = new File(descriptorFilePath);

            Map<String, String> paramMap = CliOptionUtils.parseParams(initParams);
            System.out.println("Initialization parameters:");
            paramMap.forEach((key, value) -> System.out.println("  - " + key + "=" + value));

            setDefaultParameters(paramMap);

            File descriptorFolder = descriptorFile.getParentFile();
            if (!descriptorFolder.exists()) {
                CliFileUtils.createFolder(descriptorFolder);
                CliFileUtils.createFolder(descriptorFolder, "ports");
                CliFileUtils.createFolder(descriptorFolder, "apps");
                CliFileUtils.createFolder(descriptorFolder, "infra");
                CliFileUtils.createFileFromTemplate(descriptorFolder, descriptorFile.getName(),
                        DESCRIPTOR_TEMPLATE_FILEPATH, paramMap);
            } else {
                System.err.println("Directory [" + descriptorFolder + "] already exists.");
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }

    public void validateParameters(Map<String, String> parameters) throws IOException {
        if (parameters.get("info.owner.id") == null) {
            throw new IOException(
                    "info.owner.id parameter must be specified using the --init-param PARAM=VALUE option");
        }

    }

    public void setDefaultParameters(Map<String, String> parameters) {
        if (parameters.get("product-name") == null)
            parameters.put("product-name", UUID.randomUUID().toString());
        if (parameters.get("namespace") == null)
            parameters.put("namespace", "org.opendatamesh");
        if (parameters.get("info.displayName") == null)
            parameters.put("info.displayName", parameters.get("product-name"));
        if (parameters.get("info.description") == null)
            parameters.put("info.description", parameters.get("product-name"));
        if (parameters.get("info.domain") == null)
            parameters.put("info.domain", parameters.get("namespace"));
        if (parameters.get("info.owner.name") == null)
            parameters.put("info.owner.name", parameters.get("info.owner.id"));
    }
}
