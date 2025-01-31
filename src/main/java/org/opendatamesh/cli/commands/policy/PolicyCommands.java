package org.opendatamesh.cli.commands.policy;

import java.util.HashMap;
import java.util.Map;

import org.opendatamesh.cli.commands.OdmCliInit;
import org.opendatamesh.cli.commands.policy.subcommands.PolicyGetCommand;
import org.opendatamesh.cli.commands.policy.subcommands.PolicyListCommand;
import org.opendatamesh.cli.commands.policy.subcommands.PolicyPublishCommand;
import org.opendatamesh.cli.commands.policy.subcommands.PolicyUpdateCommand;
import org.opendatamesh.cli.commands.policy.subcommands.PolicyValidateCommand;
import org.opendatamesh.cli.commands.registry.RegistryCommands;
import org.opendatamesh.cli.config.CliConfig;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParentCommand;

@Command(
        name = "policy",
        description = "Interact with remote policy-service",
        version = "odm-cli policy 1.0.0",
        mixinStandardHelpOptions = true,
        subcommands = {
                PolicyListCommand.class,
                PolicyGetCommand.class,
                PolicyPublishCommand.class,
                PolicyUpdateCommand.class,
                PolicyValidateCommand.class
        }
)
public class PolicyCommands implements Runnable{

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
            config.getServices().put("policy", propsMap);
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
