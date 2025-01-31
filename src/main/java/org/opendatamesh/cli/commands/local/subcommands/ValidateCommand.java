package org.opendatamesh.cli.commands.local.subcommands;

import org.opendatamesh.cli.utils.CliFileUtils;
import org.opendatamesh.cli.utils.PrintUtils;
import org.opendatamesh.dpds.exceptions.ParseException;
import org.opendatamesh.dpds.location.DescriptorLocation;
import org.opendatamesh.dpds.location.UriLocation;
import org.opendatamesh.dpds.parser.DPDSParser;
import org.opendatamesh.dpds.parser.ParseOptions;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Set;

@Command(
        name = "validate",
        description = "Validate the syntax of a Data Product Version JSON descriptor",
        version = "odm-cli local validate dpv 1.0.0",
        mixinStandardHelpOptions = true
)
public class ValidateCommand implements Runnable {

    @Option(
            names = {"-f", "--file"},
            description = "Path of the JSON descriptor of the Data Product Version object",
            required = true
    )
    String dataProductVersionDescriptorPath;

    @Override
    public void run() {
        try {
            String descriptorContent = CliFileUtils.readFileFromPath(dataProductVersionDescriptorPath);
            validateDPV(descriptorContent);
        } catch (IOException e) {
            System.out.println("\nInvalid Data Product Version");
            RuntimeException exception = new RuntimeException(
                    "Error parsing Data Product Version file: " + e.getMessage()
            );
            exception.setStackTrace(new StackTraceElement[0]);
            throw exception;
        } catch (ParseException e) {
            System.out.println("\nInvalid Data Product Version");
            RuntimeException exception = new RuntimeException("Data Product Version not valid: " + e.getMessage());
            exception.setStackTrace(new StackTraceElement[0]);
            throw exception;
        } catch (Exception e) {
            System.out.println("\nInvalid Data Product Version");
            RuntimeException exception = new RuntimeException(
                    "Generic error validationg Data Product Version: " + e.getMessage()
                            + ". Check for missing parts of the descriptor."
            );
            exception.setStackTrace(new StackTraceElement[0]);
            throw exception;
        }
    }

    private void validateDPV(String descriptorContent) throws Exception {

        System.out.println("Validating file ...");

        /*
        DescriptorLocation descriptorLocation = new UriLocation(descriptorContent); 
        DPDSParser descriptorParser = new DPDSParser(
                "https://raw.githubusercontent.com/opendatamesh-initiative/odm-specification-dpdescriptor/main/schemas/",
                "1.0.0",
                null
        );
        
        
        ParseOptions options = new ParseOptions();
        PrintUtils.silentExecution(() -> descriptorParser.parse(descriptorLocation, options));
        */
        
        try {
            // Create ObjectMapper for parsing JSON
            ObjectMapper objectMapper = new ObjectMapper();

            /// Fetch the schema from the URI
            JsonNode schemaNode = fetchSchemaFromUri("https://dpds.opendatamesh.org/specifications/dpds/1.0.0/schema.json");

            // Parse the JSON data
            JsonNode dataNode = objectMapper.readTree(descriptorContent);

            // Load JSON schema
            JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V202012);
            JsonSchema schema = factory.getSchema(schemaNode);

            // Validate JSON data against the schema
            //Set<ValidationMessage> validationMessages = schema.validate(dataNode);
            Set<ValidationMessage> validationMessages = PrintUtils.silentExecution(() -> schema.validate(dataNode));

            // Check validation results
            if (validationMessages.isEmpty()) {
                System.out.println("JSON is valid.");
            } else {
                System.out.println("JSON validation failed:");
                validationMessages.forEach(message -> System.out.println("  " + message.getMessage()));
            }
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }

    }

    // Method to fetch the JSON schema from a URI
    private static JsonNode fetchSchemaFromUri(String schemaUri) throws Exception {
        // Open a connection to the URI
        URL url = new URL(schemaUri);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);

        // Check the response code
        if (connection.getResponseCode() != 200) {
            throw new Exception("Failed to fetch schema: HTTP " + connection.getResponseCode());
        }

        // Read the response into a JsonNode
        try (InputStream inputStream = connection.getInputStream()) {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readTree(inputStream);
        }
    }

}
