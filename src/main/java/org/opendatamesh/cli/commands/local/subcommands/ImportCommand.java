package org.opendatamesh.cli.commands.local.subcommands;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

import org.opendatamesh.cli.extensions.ImportTool;
import org.opendatamesh.cli.utils.CliExtensionUtils;
import org.opendatamesh.cli.utils.CliOptionUtils;
import org.opendatamesh.dpds.location.DescriptorLocation;
import org.opendatamesh.dpds.location.UriLocation;
import org.opendatamesh.dpds.model.DataProductVersionDPDS;
import org.opendatamesh.dpds.model.interfaces.PortDPDS;
import org.opendatamesh.dpds.parser.DPDSParser;
import org.opendatamesh.dpds.parser.DPDSSerializer;
import org.opendatamesh.dpds.parser.ParseOptions;
import org.opendatamesh.dpds.parser.ParseResult;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

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
        if(!to.equalsIgnoreCase("port")) {
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


            ImportTool<PortDPDS> importTool = CliExtensionUtils.getImportTool(from, to);
            if(importTool == null) {
                System.out.println("Impossible to load import tool extension form calsspath for pair from: [" + from + "] - to: [" + to + "]");
                importTool = new TestImportTool();
            }
            
            PortDPDS port = importTool.importElement(descriptorFile, new PortDPDS(), outParamMap);
            descriptor.getInterfaceComponents().getOutputPorts().add(port);
            System.out.println(port);

           
            String serializedContent = DPDSSerializer.DEFAULT_JSON_SERIALIZER.serialize(descriptor, "canonical");
            try (FileWriter writer = new FileWriter(descriptorFile)) {
                writer.write(serializedContent);
                System.out.println("Port sucesfully added");
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
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
