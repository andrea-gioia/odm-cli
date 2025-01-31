package org.opendatamesh.cli.commands.blueprint;

import java.util.HashMap;
import java.util.Map;

import org.opendatamesh.cli.commands.OdmCliInit;
import org.opendatamesh.cli.commands.blueprint.subcommands.InitBlueprintCommand;
import org.opendatamesh.cli.commands.blueprint.subcommands.ListBlueprintCommand;
import org.opendatamesh.cli.commands.blueprint.subcommands.PublishBlueprintCommand;
import org.opendatamesh.cli.config.CliConfig;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParentCommand;

@Command(name = "blueprint", description = "Interact with remote blueprint-service", version = "odm-cli blueprint 1.0.0", mixinStandardHelpOptions = true, subcommands = {
        PublishBlueprintCommand.class,
        InitBlueprintCommand.class,
        ListBlueprintCommand.class
})
public class BlueprintCommands implements Runnable {

    @Option(names = { "-s",
            "--service-url" }, description = "URL of the blueprint-service. It must include the port. It overrides the value inside the properties file, if it is present")
    String serviceUrl;

    @ParentCommand
    protected OdmCliInit odmCliInit;

    public CliConfig getConfig() {

        CliConfig config = odmCliInit.getConfig();

        if (serviceUrl != null) {
            Map<String, String> propsMap = new HashMap();
            propsMap.put("url", serviceUrl);
            config.getServices().put("blueprint", propsMap);
        }

        return config;
    }

    public static void main(String[] args) {
        CommandLine.run(new BlueprintCommands(), args);
    }

    @Override
    public void run() {
    }

}
