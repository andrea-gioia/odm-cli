package org.opendatamesh.odm.cli.commands.devops.subcommands;


import org.opendatamesh.odm.cli.commands.devops.DevOpsCommands;
import org.opendatamesh.odm.cli.utils.ObjectMapperUtils;
import org.opendatamesh.platform.pp.devops.api.resources.ActivityResource;
import org.opendatamesh.platform.pp.devops.api.resources.ActivityTaskResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;

import com.fasterxml.jackson.core.JsonProcessingException;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParentCommand;

@Command(
        name = "list",
        description = "List devops-service entities",
        mixinStandardHelpOptions = true,
        version = "odm-cli devops list 1.0.0"
)
public class DevOpsListCommands implements Runnable {

    @Option(names = "--type", description = "Type of the entity to list (activity|task)", defaultValue = "activity", required = false)
    String type;

 
    @ParentCommand
    protected DevOpsCommands devOpsCommands;

    @Override
    public void run() {
        if (type.equalsIgnoreCase("activity")) {
            listActivities();
        } else if (type.equalsIgnoreCase("task")) {
            listTasks();
        }
    }

       
    public void listActivities() {
        try {
            ResponseEntity<ActivityResource[]> activityResponseEntitity =
                    devOpsCommands.getConfig().getDevOpsServiceClient().getActivities();
            if (activityResponseEntitity.getStatusCode().equals(HttpStatus.OK)) {
                ActivityResource[] activities = activityResponseEntitity.getBody();
                if (activities.length == 0)
                    System.out.println("[]");
                for (ActivityResource activityResource : activities)
                    System.out.println(ObjectMapperUtils.formatAsString(activityResource));
            } else
                System.out.println("Error in response from DevOps Server");
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (ResourceAccessException e) {
            System.out.println("Impossible to connect with devops server. Verify the URL and retry");
        }
    }

     public void listTasks() {
        try {
            ResponseEntity<ActivityTaskResource[]> tasksResponse =
                    devOpsCommands.getConfig().getDevOpsServiceClient().getTasks();
            if (tasksResponse.getStatusCode().equals(HttpStatus.OK)) {
                ActivityTaskResource[] taskResources = tasksResponse.getBody();
                if (taskResources.length == 0)
                    System.out.println("[]");
                for (ActivityTaskResource activityTaskResource : taskResources)
                    System.out.println(ObjectMapperUtils.formatAsString(activityTaskResource));
            } else
                System.out.println("Error in response from DevOps Server");
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (ResourceAccessException e) {
            System.out.println("Impossible to connect with devops server. Verify the URL and retry");
        }
    }

}

