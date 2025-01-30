package org.opendatamesh.odm.cli.commands.registry.subcommands;

import org.opendatamesh.dpds.model.DataProductVersionDPDS;
import org.opendatamesh.odm.cli.commands.registry.RegistryCommands;
import org.opendatamesh.odm.cli.utils.ObjectMapperUtils;
import org.opendatamesh.platform.pp.registry.api.resources.DataProductResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParentCommand;

@Command(name = "get", description = "Commands to get data products and versions", mixinStandardHelpOptions = true, version = "odm-cli registry get 1.0.0")
public class RegistryGetCommand implements Runnable {

    @Option(names = "--type", description = "Type of the entity to list (dp|dpv)", defaultValue = "dp", required = false)
    String type;

    @Option(names = "--id", description = "Version of the data product", required = false)
    String dataProductId;

    @Option(names = "--dp-version", description = "Version of the data product", required = false)
    String dataProductVersion;

    @ParentCommand
    protected RegistryCommands registryCommands;

    @Override
    public void run() {
        if (type.equalsIgnoreCase("dp")) {
            getDataProduct();
        } else if (type.equalsIgnoreCase("dpv")) {
            getDataProductVerion();
        }
    }

    public void getDataProduct() {
        try {
            ResponseEntity<DataProductResource> dataProductResponseEntity = registryCommands.getConfig()
                .getRegistryServiceClient().getDataProduct(dataProductId);
            if (dataProductResponseEntity.getStatusCode().equals(HttpStatus.OK)) {
                DataProductResource dataProduct = dataProductResponseEntity.getBody();
                System.out.println(ObjectMapperUtils.formatAsString(dataProduct));
            } else if (dataProductResponseEntity.getStatusCode().equals(HttpStatus.NOT_FOUND))
                System.out.println("Data product with ID [" + dataProductId + "] not found");
            else
                System.out.println(
                        "Got an unexpected response. Error code: " + dataProductResponseEntity.getStatusCode());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void getDataProductVerion() {
        try {
            ResponseEntity<DataProductVersionDPDS> dataProductVersionsResponseEntity = registryCommands.getConfig()
                    .getRegistryServiceClient().getDataProductVersion(
                            dataProductId, dataProductVersion);
            if (dataProductVersionsResponseEntity.getStatusCode().equals(HttpStatus.OK)) {
                DataProductVersionDPDS dataProductVersion = dataProductVersionsResponseEntity.getBody();
                System.out.println(ObjectMapperUtils.formatAsString(dataProductVersion));
            } else if (dataProductVersionsResponseEntity.getStatusCode().equals(HttpStatus.NOT_FOUND))
                System.out.println(
                        "Version [" + dataProductVersion + "] of Data product with ID [" + dataProductId
                                + "] not found");
            else
                System.out.println(
                        "Got an unexpected response. Error code: " + dataProductVersionsResponseEntity.getStatusCode());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
