package org.opendatamesh.odm.cli.commands.local.subcommands;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.io.File;

import java.io.FileWriter;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.opendatamesh.dpds.location.DescriptorLocation;
import org.opendatamesh.dpds.location.UriLocation;
import org.opendatamesh.dpds.model.DataProductVersionDPDS;
import org.opendatamesh.dpds.model.interfaces.PortDPDS;
import org.opendatamesh.dpds.parser.DPDSParser;
import org.opendatamesh.dpds.parser.DPDSSerializer;
import org.opendatamesh.dpds.parser.ParseOptions;
import org.opendatamesh.dpds.parser.ParseResult;
import org.opendatamesh.odm.cli.utils.CliFileUtils;
import org.opendatamesh.odm.cli.utils.CliOptionUtils;

// bash> /odm-cli local add -f dpd-test/data-product-descriptor.json --name paperino

@Command(name = "add", description = "Add a component to the descriptor", version = "odm-cli local add 1.0.0", mixinStandardHelpOptions = true)
public class AddCommand implements Runnable {

    @Option(names = { "-f",
            "--file" }, description = "Name of the descriptor file (default: PATH/data-product-descriptor.json)", required = false, defaultValue = "./data-product-descriptor.json")
    String descriptorFilePath;

    @Option(names = { "-n", "--name" }, description = "Port name", required = false)
    String portName;

    @Option(names = "--init-param", description = "Init parameter", arity = "0..*", paramLabel = "<KEY=VALUE>")
    private List<String> initParams;

    private static final String PORT_TEMPLATE_FILEPATH = "local/port-template.json";

    @Override
    public void run() {

        try {

            File descriptorFile = new File(descriptorFilePath);

            Map<String, String> paramMap = CliOptionUtils.parseParams(initParams);
            System.out.println("Initialization parameters:");
            paramMap.forEach((key, value) -> System.out.println("  - " + key + "=" + value));
            if (portName == null) {
                portName = UUID.randomUUID().toString();
            }
            paramMap.put("name", portName);
            setDefaultParameters(paramMap);

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
            String portRef = "ports/output/" + portName + "/port.json";
            port.setRef(portRef);
            port.setRawContent(" {\"$ref\": \"" + portRef + "\"}");
            descriptor.getInterfaceComponents().getOutputPorts().add(port);

            File portFile = new File(descriptorFile.getParentFile(), portRef);
            File portFoder = portFile.getParentFile();

            CliFileUtils.createFileFromTemplate(portFoder.getPath(), portFile.getName(), PORT_TEMPLATE_FILEPATH,
                    paramMap);

            String serializedContent = DPDSSerializer.DEFAULT_JSON_SERIALIZER.serialize(descriptor, "canonical");
            try (FileWriter writer = new FileWriter(descriptorFile)) {
                writer.write(serializedContent);
                System.out.println("Port sucesfully added");
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }

    public void setDefaultParameters(Map<String, String> parameters) {
        if (parameters.get("displayName") == null)
            parameters.put("displayName", parameters.get("name"));
        if (parameters.get("description") == null)
            parameters.put("description", parameters.get("name"));
        if (parameters.get("version") == null)
            parameters.put("version", "1.0.0");
    }
}
