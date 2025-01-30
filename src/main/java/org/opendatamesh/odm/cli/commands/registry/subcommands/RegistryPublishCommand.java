package org.opendatamesh.odm.cli.commands.registry.subcommands;

import java.io.IOException;

import org.opendatamesh.dpds.model.DataProductVersionDPDS;
import org.opendatamesh.odm.cli.commands.registry.RegistryCommands;
import org.opendatamesh.odm.cli.utils.CliFileUtils;
import org.opendatamesh.odm.cli.utils.ObjectMapperUtils;
import org.opendatamesh.platform.pp.registry.api.resources.DataProductResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParentCommand;

@Command(name = "publish", description = "Commands to publish data products and versions", version = "odm-cli registry publish 1.0.0", mixinStandardHelpOptions = true)
public class RegistryPublishCommand implements Runnable {

    @ParentCommand
    protected RegistryCommands registryCommands;

    @Option(names = { "-f",
            "--file" }, description = "File that contains the definition of the entity to publish", required = true)
    String entityFilePath;

    @Option(names = "--entity-file", description = "File that contains entity description", required = true)
    String entityType;

    @Option(names = "--id", description = "Id of data product. Applicable only if type is equal to dpv", required = false)
    String entityId;

    @Override
    public void run() {
        if (entityType.equalsIgnoreCase("dp")) {
            publishDataProduct();
        } else if (entityType.equalsIgnoreCase("dpv")) {
            publishDataProductVerion();
        }
    }

    public void publishDataProduct() {
        String dp;
        try {
            dp = CliFileUtils.readFileFromPath(entityFilePath);
        } catch (IOException e) {
            System.out.println(
                    "Impossible to read file [" + entityFilePath
                            + "]. Check if the file exists and retry");
            return;
        }
        try {
            ResponseEntity<DataProductResource> dataProductResponseEntity = registryCommands.getConfig()
                    .getRegistryServiceClient().postDataProduct(dp);
            if (dataProductResponseEntity.getStatusCode().equals(HttpStatus.CREATED)) {
                DataProductResource dataProduct = dataProductResponseEntity.getBody();
                System.out.println("Data product CREATED:\n"
                        + ObjectMapperUtils.formatAsString(dataProduct));
            } else
                System.out.println(
                        "Got an unexpected response. Error code: "
                                + dataProductResponseEntity.getStatusCode());
        } catch (ResourceAccessException e) {
            System.out.println("Impossible to connect with Registry server. Verify the URL and retry");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void publishDataProductVerion() {
        String dpv;
        try {
            dpv = CliFileUtils.readFileFromPath(entityFilePath);
        } catch (IOException e) {
            System.out.println(
                    "Impossible to read file [" + entityFilePath +
                            "]. Check if the file exists and retry");
            return;
        }

        System.out.println("Publishing a new version of product [" + entityId + "]: \n" + dpv);

        try {
            ResponseEntity<DataProductVersionDPDS> dataProductResponseEntity = registryCommands.getConfig()
                    .getRegistryServiceClient().postDataProductVersion(entityId, dpv);
            if (dataProductResponseEntity.getStatusCode().equals(HttpStatus.CREATED)) {
                DataProductVersionDPDS dataProductVersion = dataProductResponseEntity.getBody();
                System.out.println("Data Product Version CREATED:\n"
                        + ObjectMapperUtils.formatAsString(dataProductVersion));
            } else
                System.out.println(
                        "Got an unexpected response. Error code: "
                                + dataProductResponseEntity.getStatusCode());
        } catch (ResourceAccessException e) {
            System.out.println("Impossible to connect with Registry server. Verify the URL and retry");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
