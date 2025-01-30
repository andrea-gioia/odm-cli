package org.opendatamesh.odm.cli.commands.policy.subcommands;

import java.io.IOException;

import org.opendatamesh.odm.cli.commands.policy.PolicyCommands;
import org.opendatamesh.odm.cli.utils.CliFileUtils;
import org.opendatamesh.odm.cli.utils.ObjectMapperUtils;
import org.opendatamesh.platform.pp.policy.api.resources.PolicyEngineResource;
import org.opendatamesh.platform.pp.policy.api.resources.PolicyEvaluationResultResource;
import org.opendatamesh.platform.pp.policy.api.resources.PolicyResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;

import com.fasterxml.jackson.databind.node.ObjectNode;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParentCommand;

@Command(
        name = "publish",
        description = "Publish an entity to policy-service",
        version = "odm-cli policy publish 1.0.0",
        mixinStandardHelpOptions = true
)
public class PolicyPublishCommand implements Runnable {

    @Option(names = "--type", description = "Type of the entity to start (policy, engine, eval)", defaultValue = "policy", required = false)
    String type;

    @Option(names = "--entity-file", description = "File that contains the entity description", required = true)
    String entityFilePath;


    @ParentCommand
    protected PolicyCommands policyCommands;

    @Override
    public void run() {
        if (type.equalsIgnoreCase("policy")) {
            publishPolicy();
        } else if (type.equalsIgnoreCase("engine")) {
            publishEngine();
        } else if (type.equalsIgnoreCase("eval")) {
            publishEvaluation();
        } else {
            System.err.println("Unknown entity type [" + type + "]");
        }
     }

      public void publishPolicy() {
        PolicyResource policy;
        try {
            policy = ObjectMapperUtils.stringToResource(
                    CliFileUtils.readFileFromPath(entityFilePath),
                    PolicyResource.class
            );
        } catch (IOException e) {
            System.out.println(
                    "Impossible to read file [" + entityFilePath + "]. Check if the file exists and retry"
            );
            return;
        }
        try {
            ResponseEntity<ObjectNode> policyResponseEntity =
            policyCommands.getConfig().getPolicyServiceClient().createPolicyResponseEntity(policy);
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

    public void publishEngine() {
        PolicyEngineResource policyEngine;
        try {
            policyEngine = ObjectMapperUtils.stringToResource(
                    CliFileUtils.readFileFromPath(entityFilePath),
                    PolicyEngineResource.class
            );
        } catch (IOException e) {
            System.out.println(
                    "Impossible to read file [" + entityFilePath + "]. Check if the file exists and retry"
            );
            return;
        }
        try {
            ResponseEntity<ObjectNode> engineResponseEntity =
            policyCommands.getConfig().getPolicyServiceClient().createPolicyEngineResponseEntity(policyEngine);
            if(engineResponseEntity.getStatusCode().equals(HttpStatus.CREATED)) {
                policyEngine = ObjectMapperUtils.convertObjectNode(engineResponseEntity.getBody(), PolicyEngineResource.class);
                System.out.println("Policy Engine CREATED:\n" + ObjectMapperUtils.formatAsString(policyEngine));
            }
            else
                System.out.println("Got an unexpected response. Error code: " + engineResponseEntity.getStatusCode());
        } catch (ResourceAccessException e) {
            System.out.println("Impossible to connect with Policy server. Verify the URL and retry");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public void publishEvaluation() {
        PolicyEvaluationResultResource policyEvaluationResult;
        try {
            policyEvaluationResult = ObjectMapperUtils.stringToResource(
                    CliFileUtils.readFileFromPath(entityFilePath),
                    PolicyEvaluationResultResource.class
            );
        } catch (IOException e) {
            System.out.println(
                    "Impossible to read file [" + entityFilePath + "]. Check if the file exists and retry"
            );
            return;
        }
        try {
            ResponseEntity<ObjectNode> resultResponseEntity =
            policyCommands.getConfig().getPolicyServiceClient().createPolicyEvaluationResultResponseEntity(policyEvaluationResult);
            if(resultResponseEntity.getStatusCode().equals(HttpStatus.CREATED)) {
                policyEvaluationResult = ObjectMapperUtils.convertObjectNode(
                        resultResponseEntity.getBody(), PolicyEvaluationResultResource.class
                );
                System.out.println("Policy Evaluation Result CREATED:\n" + ObjectMapperUtils.formatAsString(policyEvaluationResult));
            }
            else
                System.out.println("Got an unexpected response. Error code: " + resultResponseEntity.getStatusCode());
        } catch (ResourceAccessException e) {
            System.out.println("Impossible to connect with Policy server. Verify the URL and retry");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}
