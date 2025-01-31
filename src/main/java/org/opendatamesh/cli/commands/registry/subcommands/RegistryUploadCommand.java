package org.opendatamesh.cli.commands.registry.subcommands;

import org.opendatamesh.cli.commands.registry.RegistryCommands;
import org.opendatamesh.cli.utils.InputManagerUtils;
import org.opendatamesh.platform.pp.registry.api.resources.DataProductDescriptorLocationResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParentCommand;

@Command(name = "upload", description = "Upload an entity from a git repository to registry-service", mixinStandardHelpOptions = true, version = "odm-cli registry upload 1.0.0")
public class RegistryUploadCommand implements Runnable {

    @Option(names = "--type", description = "Type of the entity to upload (dpv)", defaultValue = "dpv", required = false)
    String entityType;

    @ParentCommand

    protected RegistryCommands registryCommands;

    @Override
    public void run() {
        if (entityType.equalsIgnoreCase("dpv")) {
            uploadDataProductVerion();
        }
    }

    public void uploadDataProductVerion() {
        DataProductDescriptorLocationResource dpLocation = new DataProductDescriptorLocationResource();
        DataProductDescriptorLocationResource.Git git = new DataProductDescriptorLocationResource.Git();
        // Request input from user
        String repositorySshUri = InputManagerUtils.getValueFromUser("Insert repository ssh URI: ");
        git.setRepositorySshUri(repositorySshUri);
        String branch = InputManagerUtils.getValueFromUser(
                "Insert branch (blank for \"main\"): ", "main");
        git.setBranch(branch);
        dpLocation.setGit(git);
        String rootDocumentUri = InputManagerUtils.getValueFromUser(
                "Insert the root document URI (inside the repo you previously specified): ");
        dpLocation.setRootDocumentUri(rootDocumentUri);
        try {
            ResponseEntity<String> dataProductResponseEntity = registryCommands.getConfig()
                    .getRegistryServiceClient().uploadDataProductVersion(dpLocation, String.class);
            if (dataProductResponseEntity.getStatusCode().equals(HttpStatus.CREATED)) {
                String dataProductVersion = dataProductResponseEntity.getBody();
                System.out.println("Data product version CREATED:\n" + dataProductVersion);
            } else
                System.out.println(
                        "Got an unexpected response. Error code: " + dataProductResponseEntity.getStatusCode());
        } catch (ResourceAccessException e) {
            System.out.println("Impossible to connect with Registry server. Verify the URL and retry");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
