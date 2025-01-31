package org.opendatamesh.cli.commands.local;

import org.opendatamesh.cli.commands.local.subcommands.AddCommand;
import org.opendatamesh.cli.commands.local.subcommands.ImportCommand;
import org.opendatamesh.cli.commands.local.subcommands.InitCommand;
import org.opendatamesh.cli.commands.local.subcommands.ValidateCommand;

import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(
        name = "local",
        description = "Manage local env",
        version = "odm-cli local 1.0.0",
        mixinStandardHelpOptions = true,
        subcommands = {
            ValidateCommand.class,
            InitCommand.class,
            AddCommand.class,
            ImportCommand.class
        }
)
public class LocalCommands implements Runnable {

    public static void main(String[] args) {
        CommandLine.run(new LocalCommands(), args);
    }

    @Override
    public void run() { }

}

