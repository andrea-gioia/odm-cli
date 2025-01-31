package org.opendatamesh.cli.commands;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.opendatamesh.cli.commands.blueprint.BlueprintCommands;
import org.opendatamesh.cli.commands.devops.DevOpsCommands;
import org.opendatamesh.cli.commands.local.LocalCommands;
import org.opendatamesh.cli.commands.policy.PolicyCommands;
import org.opendatamesh.cli.commands.registry.RegistryCommands;
import org.opendatamesh.cli.config.CliConfig;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "odm-cli", description = "ODM CLI init method", version = "odm-cli 1.0.0", mixinStandardHelpOptions = true, subcommands = {
        LocalCommands.class,
        RegistryCommands.class,
        BlueprintCommands.class,
        PolicyCommands.class,
        DevOpsCommands.class
})
public class OdmCliInit implements Runnable {

    @Option(names = { "--config" }, description = "Location of odmcli config file", required = false)
    String cofigFilePath;

    CliConfig config;

    public CliConfig getConfig() {
        if(config == null) loadConfig();
        return config;
    }
    public CliConfig loadConfig() {
       
        if(config != null) return config;
        
        // try to read config from command line or local env
        cofigFilePath = (cofigFilePath != null)? cofigFilePath: System.getenv("ODMCLI_CONFIG");
        if (cofigFilePath != null) {
            File configFile = new File(cofigFilePath);
            try {
                config = CliConfig.load(configFile.toPath());
            } catch (Exception e) {
                System.err.println("Impossible to read config file [" + cofigFilePath
                        + "]. Default config file will be used");
                config = null;
            }
        } 

        // if it is impossible to read conf from command line and env try to read config from user home folder
        if (config == null) {
            try {
                config = CliConfig.load();
            } catch (Exception e) {
                System.err.println("Impossible to read default config file");
            }
        }
        
        return config;
    }
    public static void main(String[] args) {
        CommandLine.run(new OdmCliInit(), args);
    }

    @Override
    public void run() {
        loadConfig();      
    }

}
