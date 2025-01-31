package org.opendatamesh.cli.commands.devops.subcommands;

import org.opendatamesh.cli.commands.devops.DevOpsCommands;
import org.opendatamesh.cli.utils.ObjectMapperUtils;
import org.opendatamesh.platform.pp.devops.api.resources.ActivityResource;
import org.opendatamesh.platform.pp.devops.api.resources.ActivityStatusResource;
import org.opendatamesh.platform.pp.devops.api.resources.ActivityTaskResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonProcessingException;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParentCommand;

@Command(name = "get", description = "Get an entity from devops-service", mixinStandardHelpOptions = true, version = "odm-cli devops get 1.0.0")
public class DevOpsGetCommands implements Runnable {

    @Option(names = "--type", description = "Type of the entity to start (activity, task)", defaultValue = "activity", required = false)
    String type;

    @Option(names = "--id", description = "The id of the entity to get", required = true)
    Long entityId;

    @Option(names = "--status", description = "Whether to retrieve only the status of the entity (true) or the full entity (false)", required = true, defaultValue = "false")
    Boolean status;

    @ParentCommand
    protected DevOpsCommands devOpsCommands;

    @Override
    public void run() {
        if (type.equalsIgnoreCase("task")) {
            getTask();
        } else if (type.equalsIgnoreCase("activity")) {
            getActivity();
        } else {
            System.err.println("Unknown entity type [" + type + "]");
        }
    }

    public void getTask() {
        try {
            ResponseEntity<ActivityTaskResource> taskResponseEntity = devOpsCommands.getConfig()
                    .getDevOpsServiceClient().getTask(entityId);
            if (taskResponseEntity.getStatusCode().equals(HttpStatus.OK))
                System.out.println(ObjectMapperUtils.formatAsString(taskResponseEntity.getBody()));
            else if (taskResponseEntity.getStatusCode().equals(HttpStatus.NOT_FOUND))
                System.out.println("Task number: [" + entityId + "] not found");
            else
                System.out.println(
                        "Got an unexpected response. Error code: " + taskResponseEntity.getStatusCode());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void getActivity() {
        try {
            if (status != null && status) {
                // Retrieve only status
                getActivityStatus(entityId);
            } else {
                // Retrieve full activity
                getActivityResource(entityId);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void getActivityResource(Long activityId) throws JsonProcessingException {
        ResponseEntity<ActivityResource> activityResponseEntity = devOpsCommands.getConfig().getDevOpsServiceClient()
                .getActivity(activityId);
        if (activityResponseEntity.getStatusCode().equals(HttpStatus.OK))
            System.out.println(ObjectMapperUtils.formatAsString(activityResponseEntity.getBody()));
        else if (activityResponseEntity.getStatusCode().equals(HttpStatus.NOT_FOUND))
            System.out.println("Activity with ID [" + activityId + "] not found");
        else
            logError(activityResponseEntity.getStatusCode());
    }

    private void getActivityStatus(Long activityId) throws JsonProcessingException {
        ResponseEntity<ActivityStatusResource> activityStatusResponseEntity = devOpsCommands.getConfig()
                .getDevOpsServiceClient().getActivityStatus(activityId);
        if (activityStatusResponseEntity.getStatusCode().equals(HttpStatus.OK))
            System.out.println(ObjectMapperUtils.formatAsString(activityStatusResponseEntity.getBody()));
        else if (activityStatusResponseEntity.getStatusCode().equals(HttpStatus.NOT_FOUND))
            System.out.println("Activity status of activity number: [" + activityId + "] not found");
        else
            logError(activityStatusResponseEntity.getStatusCode());
    }

    private void logError(HttpStatus statusCode) {
        System.out.println("Got an unexpected response. Error code: " + statusCode);
    }

}
