package org.opendatamesh.cli.commands.blueprint.subcommands;

import org.opendatamesh.cli.commands.blueprint.BlueprintCommands;
import org.opendatamesh.cli.utils.CliFileUtils;
import org.opendatamesh.cli.utils.ObjectMapperUtils;
import org.opendatamesh.platform.core.commons.clients.resources.ErrorRes;
import org.opendatamesh.platform.pp.blueprint.api.resources.BlueprintResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParentCommand;

import java.io.IOException;

@Command(
        name = "publish",
        description = "Publish a blueprint to blueprint-service",
        version = "odm-cli blueprint create 1.0.0",
        mixinStandardHelpOptions = true
)
public class PublishBlueprintCommand implements Runnable {


    @Option(
            names = "--entity-file",
            required = true,
            description = "File that contains the blueprint description"
    )
    private String entityFilePath;

    @Option(
            names = "--check",
            description = "Whether to check or not the content of the repository"
    )
    private Boolean checkContent;

    @ParentCommand
    BlueprintCommands blueprintCommands;

    @Override
    public void run() {
        BlueprintResource blueprintResource;
        try {
            blueprintResource = ObjectMapperUtils.stringToResource(
                    CliFileUtils.readFileFromPath(entityFilePath),
                    BlueprintResource.class
            );
        } catch (IOException e) {
            System.out.println(
                    "Impossible to parse file [" + entityFilePath + "] as a BlueprintResource."
                            + "Check that the file exists and it's well-formatted."
            );
            return;
        }
        try {
            ResponseEntity<Object> blueprintResponseEntity =
                    blueprintCommands.getConfig().getBlueprintServiceClient().createBlueprint(blueprintResource, checkContent);
            if (blueprintResponseEntity.getStatusCode().equals(HttpStatus.CREATED)) {
                blueprintResource = ObjectMapperUtils.convertObject(blueprintResponseEntity.getBody(), BlueprintResource.class);
                System.out.println("Blueprint correctly created: \n" + ObjectMapperUtils.formatAsString(blueprintResource));
            }
            else {
                ErrorRes error = ObjectMapperUtils.convertObject(blueprintResponseEntity.getBody(), ErrorRes.class);
                System.out.println(
                        "Got an unexpected response. Error code  [" + error.getCode() + "]. "
                                + "Error message: " + error.getMessage()
                );
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ResourceAccessException e){
            System.out.println("Impossible to connect with blueprint server. Verify the URL and retry");
        }
    }

}
