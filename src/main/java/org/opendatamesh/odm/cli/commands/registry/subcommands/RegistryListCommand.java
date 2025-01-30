package org.opendatamesh.odm.cli.commands.registry.subcommands;

import org.opendatamesh.odm.cli.commands.registry.RegistryCommands;
import org.opendatamesh.odm.cli.utils.ObjectMapperUtils;
import org.opendatamesh.platform.pp.registry.api.resources.DataProductResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParentCommand;

@Command(name = "list", description = "Commands to list data products and versions", mixinStandardHelpOptions = true, version = "odm-cli registry list 1.0.0")
public class RegistryListCommand implements Runnable {

    @Option(names = "--type", description = "Type of the entity to list (dp|dpv)", defaultValue = "dp", required = false)
    String type;

    @Option(names = "--id", description = "Id of data product. Applicable only if type is equal to dpv", required = false)
    String dataProductId;

    @ParentCommand
    protected RegistryCommands registryCommands;

    @Override
    public void run() {
        if (type.equalsIgnoreCase("dp")) {
            listDataProducts();
        } else if (type.equalsIgnoreCase("dpv")) {
            listDataProductVerions();
        }
    }

    public void listDataProducts() {
        try {
            ResponseEntity<DataProductResource[]> dataProductResourceResponseEntity = registryCommands.getConfig()
                    .getRegistryServiceClient().getDataProducts();
            if (dataProductResourceResponseEntity.getStatusCode().equals(HttpStatus.OK)) {
                DataProductResource[] dataProducts = dataProductResourceResponseEntity.getBody();
                if (dataProducts.length == 0)
                    System.out.println("[]");
                for (DataProductResource dataProduct : dataProducts)
                    System.out.println(ObjectMapperUtils.formatAsString(dataProduct));
            } else
                System.out.println("Error in response from Registry Server");
        } catch (ResourceAccessException e) {
            System.out.println("Impossible to connect with Registry server. Verify the URL and retry");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void listDataProductVerions() {
        try {

            if(dataProductId == null) {
                System.err.println("Use option --dp-id to specify the id of the data produc whose versions you want to list");
                System.exit(-1);
            }

            ResponseEntity<DataProductResource> dataProductResourceResponseEntity = registryCommands.getConfig()
                    .getRegistryServiceClient().getDataProduct(dataProductId);
            if (!dataProductResourceResponseEntity.getStatusCode().is2xxSuccessful()) {
                System.out.println("Data Product [" + dataProductId + "] not found.");
                return;
            }
            ResponseEntity<String[]> dataProductVersionsResponseEntity = registryCommands.getConfig()
                    .getRegistryServiceClient().getDataProductVersions(dataProductId);
            if (dataProductVersionsResponseEntity.getStatusCode().equals(HttpStatus.OK)) {
                String[] dataProductVersions = dataProductVersionsResponseEntity.getBody();
                if (dataProductVersions.length == 0)
                    System.out.println("[]");
                for (String dataProductVersion : dataProductVersions)
                    System.out.println(dataProductVersion);
            } else
                System.out.println("Error in response from Registry Server");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
