package org.opendatamesh.odm.cli.commands.devops.subcommands;

import org.opendatamesh.odm.cli.commands.devops.DevOpsCommands;
import org.opendatamesh.odm.cli.utils.ObjectMapperUtils;
import org.opendatamesh.platform.pp.devops.api.resources.TaskStatusResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParentCommand;

@Command(
        name = "stop",
        description = "Stops an runnable entity managed by devops-service",
        version = "odm-cli devops stop 1.0.0",
        mixinStandardHelpOptions = true
)
public class DevOpsStopCommands implements Runnable {


    @Option(names = "--type", description = "Type of the entity to stop (task)", defaultValue = "task", required = false)
    String type;

    @Option(
            names = "--id",
            description = "Id of the entity to stop",
            required = true
    )
    Long taskId;

    @ParentCommand
    protected DevOpsCommands devOpsCommands;

    @Override
    public void run() {
        if (type.equalsIgnoreCase("task")) {
            stopTask();
        } else if (type.equalsIgnoreCase("activity")) {
            System.err.println("Activities cannot be stopped from cli");
        } else {
            System.err.println("Unknown entity type [" + type + "]");
        }
    }

    public void stopTask() {
        try {
            final ResponseEntity<TaskStatusResource> taskStatusResourceResponseEntity =
                    devOpsCommands.getConfig().getDevOpsServiceClient().patchTaskStop(taskId);
            if (taskStatusResourceResponseEntity.getStatusCode().equals(HttpStatus.OK))
                System.out.println(ObjectMapperUtils.formatAsString(taskStatusResourceResponseEntity.getBody()));
            else if (taskStatusResourceResponseEntity.getStatusCode().equals(HttpStatus.NOT_FOUND))
                System.out.println("Task number: [" + taskId + "] not found");
            else
                System.out.println(
                        "Got an unexpected response. Error code: " + taskStatusResourceResponseEntity.getStatusCode()
                );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

