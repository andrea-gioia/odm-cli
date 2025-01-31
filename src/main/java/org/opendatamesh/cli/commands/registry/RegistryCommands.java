package org.opendatamesh.cli.commands.registry;

import java.util.HashMap;
import java.util.Map;

import org.opendatamesh.cli.commands.OdmCliInit;
import org.opendatamesh.cli.commands.registry.subcommands.RegistryGetCommand;
import org.opendatamesh.cli.commands.registry.subcommands.RegistryListCommand;
import org.opendatamesh.cli.commands.registry.subcommands.RegistryPublishCommand;
import org.opendatamesh.cli.commands.registry.subcommands.RegistryUploadCommand;
import org.opendatamesh.cli.config.CliConfig;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParentCommand;

@Command(name = "registry", description = "Interact with remote registry-service", version = "odm-cli registry 1.0.0", mixinStandardHelpOptions = true, subcommands = {
        RegistryListCommand.class,
        RegistryGetCommand.class,
        RegistryPublishCommand.class,
        RegistryUploadCommand.class
})
public class RegistryCommands implements Runnable {

    @Option(names = { "-s",
            "--service-url" }, description = "URL of the registry-service. It must include the port. It overrides the value inside the properties file, if it is present")
    String serviceUrl;

    @ParentCommand
    protected OdmCliInit odmCliInit;

    public CliConfig getConfig() {
       
        CliConfig config = odmCliInit.getConfig();

        if(serviceUrl != null) {
            Map<String, String> propsMap = new HashMap();
            propsMap.put("url", serviceUrl);
            config.getServices().put("registry", propsMap);
        }
       
        
        return config;
    }

    public static void main(String[] args) {
        CommandLine.run(new RegistryCommands(), args);
    }

    @Override
    public void run() {
    }

}