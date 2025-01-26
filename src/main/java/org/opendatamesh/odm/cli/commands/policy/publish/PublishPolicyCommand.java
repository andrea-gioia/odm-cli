package org.opendatamesh.odm.cli.commands.policy.publish;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.opendatamesh.odm.cli.utils.CliFileUtils;
import org.opendatamesh.odm.cli.utils.ObjectMapperUtils;
import org.opendatamesh.platform.pp.policy.api.resources.PolicyResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParentCommand;

import java.io.IOException;

@Command(
        name = "policy",
        description = "Publish a Policy",
        version = "odm-cli policy publish policy 1.0.0",
        mixinStandardHelpOptions = true
)
public class PublishPolicyCommand implements Runnable {

    @ParentCommand
    private PolicyPublishCommand policyPublishCommand;

    @Option(
            names = "--policy-file",
            description = "Path of the JSON descriptor of the Policy object",
            required = true
    )
    String policyDescriptorPath;

    @Override
    public void run() {
        PolicyResource policy;
        try {
            policy = ObjectMapperUtils.stringToResource(
                    CliFileUtils.readFileFromPath(policyDescriptorPath),
                    PolicyResource.class
            );
        } catch (IOException e) {
            System.out.println(
                    "Impossible to read file [" + policyDescriptorPath + "]. Check if the file exists and retry"
            );
            return;
        }
        try {
            ResponseEntity<ObjectNode> policyResponseEntity =
                    policyPublishCommand.policyCommands.getPolicyClient().createPolicyResponseEntity(policy);
            if(policyResponseEntity.getStatusCode().equals(HttpStatus.CREATED)) {
                policy = ObjectMapperUtils.convertObjectNode(policyResponseEntity.getBody(), PolicyResource.class);
                System.out.println("Policy CREATED:\n" + ObjectMapperUtils.formatAsString(policy));
            }
            else
                System.out.println("Got an unexpected response. Error code: " + policyResponseEntity.getStatusCode());
        } catch (ResourceAccessException e) {
            System.out.println("Impossible to connect with Policy server. Verify the URL and retry");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}
