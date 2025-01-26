package org.opendatamesh.odm.cli.commands.local;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.io.BufferedReader;
import java.io.File;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.opendatamesh.dpds.utils.ObjectMapperFactory;

import org.opendatamesh.dpds.location.DescriptorLocation;
import org.opendatamesh.dpds.location.UriLocation;
import org.opendatamesh.dpds.model.DataProductVersionDPDS;
import org.opendatamesh.dpds.model.core.StandardDefinitionDPDS;
import org.opendatamesh.dpds.model.definitions.DefinitionReferenceDPDS;
import org.opendatamesh.dpds.model.interfaces.PortDPDS;
import org.opendatamesh.dpds.model.interfaces.PromisesDPDS;
import org.opendatamesh.dpds.parser.DPDSParser;
import org.opendatamesh.dpds.parser.DPDSSerializer;
import org.opendatamesh.dpds.parser.ParseOptions;
import org.opendatamesh.dpds.parser.ParseResult;
import org.opendatamesh.odm.cli.utils.CliFileUtils;
import org.opendatamesh.odm.cli.utils.CliOptionUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.shaded.json.JSONObject;

import net.minidev.json.JSONArray;

// bash> ./odm-cli local import -f dpd-test/data-product-descriptor.json --from ddl --to port --in-param file=test.sql --out-param name=pluto --out-param database=odmdb

@Command(name = "import", description = "Initialize a descriptor file", version = "odm-cli local init 1.0.0", mixinStandardHelpOptions = true)
public class ImportCommand implements Runnable {

    @Option(
        names = { "-f", "--file" }, 
        description = "Name of the descriptor file (default: PATH/data-product-descriptor.json)", 
        required = false, defaultValue = "./data-product-descriptor.json")
    String descriptorFilePath;

    @Option(
        names = {"--from" }, 
        description = "Import source type (ex. ddl, jdbc, unity, etc...)", 
        required = true)
    String from;

    @Option(
        names = {"--to" }, 
        description = "Import target type (ex. output-port, input-port, api, schema, etc...)", 
        required = true)
    String to;

    @Option(names = "--in-param", description = "Parameter related to source (es. source file)", arity = "0..*", paramLabel = "<KEY=VALUE>")
    private List<String> inParams;

    @Option(names = "--out-param", description = "Parameter related to target (es. target's name)", arity = "0..*", paramLabel = "<KEY=VALUE>")
    private List<String> outParams;

   
    @Override
    public void run() {
        if(!to.equalsIgnoreCase("output-port")) {
            System.err.println("Unsopported source [" + to + "]");
        }

        try {

            File descriptorFile = new File(descriptorFilePath);

            Map<String, String> inParamMap = CliOptionUtils.parseParams(inParams);
            System.out.println("In parameters:");
            inParamMap.forEach((key, value) -> System.out.println("  - " + key + "=" + value));

            Map<String, String> outParamMap = CliOptionUtils.parseParams(outParams);
            System.out.println("In parameters:");
            outParamMap.forEach((key, value) -> System.out.println("  - " + key + "=" + value));
            setDefaultParmsForOutputPortTarget(outParamMap);

            DescriptorLocation descriptorLocation = new UriLocation(Files.readString(descriptorFile.toPath()));
            DPDSParser descriptorParser = new DPDSParser(
                    "https://raw.githubusercontent.com/opendatamesh-initiative/odm-specification-dpdescriptor/main/schemas/",
                    "1.0.0",
                    null);

            ParseOptions options = new ParseOptions();
            options.setValidate(false);
            ParseResult results = descriptorParser.parse(descriptorLocation, options);
            DataProductVersionDPDS descriptor = results.getDescriptorDocument();

            PortDPDS port = new PortDPDS();
            String portName = outParamMap.get("name");
            if (portName == null) {
                portName = UUID.randomUUID().toString();
                outParamMap.put("name", portName);
            }
            

            String portRef = "ports/output/" + portName + "/port.json";
            port.setRef(portRef);
            port.setRawContent(" {\"$ref\": \"" + portRef + "\"}");
            descriptor.getInterfaceComponents().getOutputPorts().add(port);

            File portFile = new File(descriptorFile.getParentFile(), portRef);
            File portFoder = portFile.getParentFile();

            ObjectMapper mapper = ObjectMapperFactory.JSON_MAPPER;

            //CliFileUtils.createFileFromTemplate(portFoder.getPath(), portFile.getName(), PORT_TEMPLATE_FILEPATH, outParamMap);
            PortDPDS portDef = importOutputPort(outParamMap);
            String portDefContent = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(portDef);
            CliFileUtils.writeFile(portFoder.getPath(), portFile.getName(), portDefContent);

           JSONObject api = new JSONObject();
           api.appendField("datastoreapi", "1.0.0");
           JSONObject schema = new JSONObject();
           if(outParamMap.containsKey("database")) schema.appendField("databaseName", outParamMap.get("database"));
           if(outParamMap.containsKey("schema")) schema.appendField("schemaName", outParamMap.get("schema"));
           schema.appendField("tables", new JSONArray());
           api.appendField("schema", schema);

           CliFileUtils.writeFile(portFoder.getPath(), "api.json", mapper.writerWithDefaultPrettyPrinter().writeValueAsString(api));

            String serializedContent = DPDSSerializer.DEFAULT_JSON_SERIALIZER.serialize(descriptor, "canonical");
            try (FileWriter writer = new FileWriter(descriptorFile)) {
                writer.write(serializedContent);
                System.out.println("Port sucesfully added");
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    PortDPDS importOutputPort(Map<String, String> params) {
        PortDPDS port = new PortDPDS();

        port.setName(params.get("name"));
        port.setVersion("1.0.0");
        
        PromisesDPDS promises = new PromisesDPDS();
        StandardDefinitionDPDS apiStdDef = new StandardDefinitionDPDS();
        apiStdDef.setSpecification("datastoreapi");
        apiStdDef.setSpecification("1.0.0");
        DefinitionReferenceDPDS deinition = new DefinitionReferenceDPDS();
        deinition.setMediaType("text/json");
        deinition.setRef("api.json");
        apiStdDef.setDefinition(deinition);   
        promises.setApi(apiStdDef);
        port.setPromises(promises);

        return port;
    }

    public void setDefaultParmsForOutputPortTarget(Map<String, String> parameters) {
        if (parameters.get("displayName") == null)
            parameters.put("displayName", parameters.get("name"));
        if (parameters.get("description") == null)
            parameters.put("description", parameters.get("name"));
        if (parameters.get("version") == null)
            parameters.put("version", "1.0.0");
    }

    
}
