package org.opendatamesh.cli.commands.devops.subcommands;

import java.io.IOException;

import org.opendatamesh.cli.commands.devops.DevOpsCommands;
import org.opendatamesh.cli.utils.CliFileUtils;
import org.opendatamesh.cli.utils.ObjectMapperUtils;
import org.opendatamesh.platform.core.commons.clients.resources.ErrorRes;
import org.opendatamesh.platform.pp.devops.api.resources.ActivityResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParentCommand;

@Command(name = "publish", description = "Commands to publish objects related to DevOps microservice", version = "odm-cli devops publish 1.0.0", mixinStandardHelpOptions = true)
public class DevOpsPublishCommands implements Runnable {

    @Option(names = "--activity-file", required = true, description = "Path of the JSON descriptor of the activity file")
    private String activityPath;

    @Option(names = "--start", description = "Whether to start or not the activity after the creation")
    private Boolean startActivity;

    @ParentCommand
    protected DevOpsCommands devOpsCommands;

    @Override
    public void run() {
        ActivityResource activityResource;
        try {
            activityResource = ObjectMapperUtils.stringToResource(
                    CliFileUtils.readFileFromPath(activityPath),
                    ActivityResource.class);
        } catch (IOException e) {
            System.out.println(
                    "Impossible to parse file [" + activityPath + "] as a ActivityResource."
                            + "Check that the file exists and it's well-formatted.");
            return;
        }
        try {
            ResponseEntity<ActivityResource> activityResponseEntity = devOpsCommands.getConfig()
                    .getDevOpsServiceClient().postActivity(activityResource, startActivity);
            if (activityResponseEntity.getStatusCode().equals(HttpStatus.CREATED)) {
                activityResource = ObjectMapperUtils.convertObject(activityResponseEntity.getBody(),
                        ActivityResource.class);
                System.out.println("Activity CREATED: \n" + ObjectMapperUtils.formatAsString(activityResource));
            } else {
                ErrorRes error = ObjectMapperUtils.convertObject(activityResponseEntity.getBody(), ErrorRes.class);
                System.out.println(
                        "Got an unexpected response. Error code  [" + error.getCode() + "]. "
                                + "Error message: " + error.getMessage());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ResourceAccessException e) {
            System.out.println("Impossible to connect with devops server. Verify the URL and retry");
        }
    }
}
