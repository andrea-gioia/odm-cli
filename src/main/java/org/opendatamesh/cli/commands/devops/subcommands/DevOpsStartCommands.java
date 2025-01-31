package org.opendatamesh.cli.commands.devops.subcommands;

import org.opendatamesh.cli.commands.devops.DevOpsCommands;
import org.opendatamesh.cli.utils.ObjectMapperUtils;
import org.opendatamesh.platform.pp.devops.api.resources.ActivityStatusResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParentCommand;

@Command(name = "start", description = "Start an runnable entity managed by devops-service", version = "odm-cli devops start 1.0.0", mixinStandardHelpOptions = true)
public class DevOpsStartCommands implements Runnable {

    @Option(names = "--type", description = "Type of the entity to start (activity)", defaultValue = "activity", required = false)
    String type;

    @Option(names = "--id", description = "Id of the entity to start", required = true)
    Long entityId;

    @ParentCommand
    protected DevOpsCommands devOpsCommands;

    @Override
    public void run() {
        if (type.equalsIgnoreCase("task")) {
            System.err.println("Tasks cannot be started from cli");
        } else if (type.equalsIgnoreCase("activity")) {
            stratActivity();
        } else {
            System.err.println("Unknown entity type [" + type + "]");
        }
    }

    public void stratActivity() {
        try {
            ResponseEntity<ActivityStatusResource> activityStatusResourceResponseEntity =
                    devOpsCommands.getConfig().getDevOpsServiceClient().patchActivityStart(entityId);
            if (activityStatusResourceResponseEntity.getStatusCode().equals(HttpStatus.OK))
                System.out.println("Activity STARTED: \n" + ObjectMapperUtils.formatAsString(activityStatusResourceResponseEntity.getBody()));
            else if (activityStatusResourceResponseEntity.getStatusCode().equals(HttpStatus.NOT_FOUND))
                System.out.println("Activity with ID [" + entityId + "] not found");
            else
                System.out.println(
                        "Got an unexpected response. Error code: " + activityStatusResourceResponseEntity.getStatusCode()
                );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
