package org.opendatamesh.odm.cli.commands.devops;


import java.util.HashMap;
import java.util.Map;

import org.opendatamesh.odm.cli.commands.OdmCliInit;
import org.opendatamesh.odm.cli.commands.devops.subcommands.DevOpsGetCommands;
import org.opendatamesh.odm.cli.commands.devops.subcommands.DevOpsListCommands;
import org.opendatamesh.odm.cli.commands.devops.subcommands.DevOpsPublishCommands;
import org.opendatamesh.odm.cli.commands.registry.RegistryCommands;
import org.opendatamesh.odm.cli.config.CliConfig;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParentCommand;

@Command(
        name = "devops",
        description = "Interact with remote devops-service",
        version = "odm-cli devops 1.0.0",
        mixinStandardHelpOptions = true,
        subcommands = {
                DevOpsPublishCommands.class,
                DevOpsGetCommands.class,
                DevOpsListCommands.class
        }
)
public class DevOpsCommands implements Runnable {

    @Option(names = { "-s",
            "--service-url" }, description = "URL of the devops-service. It must include the port. It overrides the value inside the properties file, if it is present")
    String serviceUrl;

    @ParentCommand
    protected OdmCliInit odmCliInit;

    public CliConfig getConfig() {

        CliConfig config = odmCliInit.getConfig();

        if (serviceUrl != null) {
            Map<String, String> propsMap = new HashMap();
            propsMap.put("url", serviceUrl);
            config.getServices().put("devops", propsMap);
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
