package org.opendatamesh.odm.cli.commands.policy.subcommands;

import java.io.IOException;

import org.opendatamesh.odm.cli.commands.policy.PolicyCommands;
import org.opendatamesh.odm.cli.utils.CliFileUtils;
import org.opendatamesh.odm.cli.utils.ObjectMapperUtils;
import org.opendatamesh.platform.pp.policy.api.resources.PolicyEvaluationRequestResource;
import org.opendatamesh.platform.pp.policy.api.resources.PolicyEvaluationResultResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;

import com.fasterxml.jackson.databind.node.ObjectNode;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParentCommand;

@Command(name = "validate", description = "Validate a document", version = "odm-cli policy validate 1.0.0", mixinStandardHelpOptions = true)
public class PolicyValidateCommand implements Runnable {

    @Option(names = "--document-file", description = "File that contains the document to validate", required = true)
    String evaluationRequestPath;

    @ParentCommand
    protected PolicyCommands policyCommands;

    @Override
    public void run() {
        PolicyEvaluationRequestResource evaluationRequest;
        try {
            evaluationRequest = ObjectMapperUtils.stringToResource(
                    CliFileUtils.readFileFromPath(evaluationRequestPath),
                    PolicyEvaluationRequestResource.class);
        } catch (IOException e) {
            System.out.println(
                    "Impossible to read file [" + evaluationRequestPath + "]. Check if the file exists and retry");
            return;
        }
        try {
            ResponseEntity<ObjectNode> validateResponseEntity = policyCommands.getConfig().getPolicyServiceClient()
                    .validateInputObjectResponseEntity(evaluationRequest);
            if (validateResponseEntity.getStatusCode().equals(HttpStatus.OK)) {
                PolicyEvaluationResultResource evaluationResult = ObjectMapperUtils.convertObjectNode(
                        validateResponseEntity.getBody(), PolicyEvaluationResultResource.class);
                System.out.println("Policy Engine CREATED:\n" + ObjectMapperUtils.formatAsString(evaluationResult));
            } else
                System.out.println("Got an unexpected response. Error code: " + validateResponseEntity.getStatusCode());
        } catch (ResourceAccessException e) {
            System.out.println("Impossible to connect with Policy server. Verify the URL and retry");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
