package org.opendatamesh.cli;

import java.io.File;
import java.nio.file.Files;

import org.opendatamesh.dpds.location.DescriptorLocation;
import org.opendatamesh.dpds.location.UriLocation;
import org.opendatamesh.dpds.model.DataProductVersionDPDS;
import org.opendatamesh.dpds.model.interfaces.PortDPDS;
import org.opendatamesh.dpds.parser.DPDSParser;
import org.opendatamesh.dpds.parser.DPDSSerializer;
import org.opendatamesh.dpds.parser.ParseContext;
import org.opendatamesh.dpds.parser.ParseOptions;
import org.opendatamesh.dpds.parser.ParseResult;
import org.opendatamesh.dpds.utils.ObjectMapperFactory;

public class TestMain {

    private static final String DESCRIPTOR_PATH = "/home/angioia/code/odm-cli/dpd-test/data-product-descriptor.json"; 
    public static void main(String[] args) {
        ParseResult results = null;
        try {
            File descriptorFile = new File(DESCRIPTOR_PATH);
            DescriptorLocation descriptorLocation = new UriLocation(Files.readString(descriptorFile.toPath()));
            DPDSParser descriptorParser = new DPDSParser(
                    "https://raw.githubusercontent.com/opendatamesh-initiative/odm-specification-dpdescriptor/main/schemas/",
                    "1.0.0",
                    null);
    
            ParseOptions options = new ParseOptions();
            options.setValidate(false);
            results = descriptorParser.parse(descriptorLocation, options);

            DataProductVersionDPDS descriptor = results.getDescriptorDocument();

            PortDPDS port = new PortDPDS();
            String portName = "trip-tms";
            String portRef = "ports/output/" + portName + "/port.json";
            port.setRef(portRef);
            port.setRawContent(" {\"$ref\": \"" + portRef + "\"}");
            descriptor.getInterfaceComponents().getOutputPorts().add(port);

            String serializedContent = null;

            serializedContent = DPDSSerializer.DEFAULT_JSON_SERIALIZER.serialize(descriptor, "canonical");

            System.out.println(serializedContent);
        } catch (Throwable e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
