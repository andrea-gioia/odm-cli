package org.opendatamesh.cli.commands.policy.subcommands;

import java.io.IOException;

import org.opendatamesh.cli.commands.policy.PolicyCommands;
import org.opendatamesh.cli.utils.CliFileUtils;
import org.opendatamesh.cli.utils.ObjectMapperUtils;
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

@Command(name = "update", description = "Publish an entity to policy-service", version = "odm-cli policy update 1.0.0", mixinStandardHelpOptions = true)
public class PolicyUpdateCommand implements Runnable {

    @Option(names = "--type", description = "Type of the entity to start (policy, engine, eval)", defaultValue = "policy", required = false)
    String type;

    @Option(names = "--id", description = "Id of the entity to get", required = true)
    Long entityId;

    @Option(names = "--entity-file", description = "File that contains entity description", required = true)
    String entityFilePath;

    @ParentCommand
    protected PolicyCommands policyCommands;

    @Override
    public void run() {
        if (type.equalsIgnoreCase("policy")) {
            updatePolicy();
        } else if (type.equalsIgnoreCase("engine")) {
            updateEngine();
        } else if (type.equalsIgnoreCase("eval")) {
            updateEvaluation();
        } else {
            System.err.println("Unknown entity type [" + type + "]");
        }
    }

    public void updatePolicy() {
        PolicyResource policy;
        try {
            policy = ObjectMapperUtils.stringToResource(
                    CliFileUtils.readFileFromPath(entityFilePath),
                    PolicyResource.class);
        } catch (IOException e) {
            System.out.println(
                    "Impossible to read file [" + entityFilePath + "]. Check if the file exists and retry");
            return;
        }
        try {
            ResponseEntity<ObjectNode> policyResponseEntity = policyCommands.getConfig().getPolicyServiceClient()
                    .updatePolicyResponseEntity(entityId, policy);
            if (policyResponseEntity.getStatusCode().equals(HttpStatus.OK)) {
                policy = ObjectMapperUtils.convertObjectNode(policyResponseEntity.getBody(), PolicyResource.class);
                System.out.println("Policy UPDATED:\n" + ObjectMapperUtils.formatAsString(policy));
            } else
                System.out.println("Got an unexpected response. Error code: " + policyResponseEntity.getStatusCode());
        } catch (ResourceAccessException e) {
            System.out.println("Impossible to connect with Policy server. Verify the URL and retry");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void updateEngine() {
        PolicyEngineResource policyEngine;
        try {
            policyEngine = ObjectMapperUtils.stringToResource(
                    CliFileUtils.readFileFromPath(entityFilePath),
                    PolicyEngineResource.class);
        } catch (IOException e) {
            System.out.println(
                    "Impossible to read file [" + entityFilePath + "]. Check if the file exists and retry");
            return;
        }
        try {
            ResponseEntity<ObjectNode> engineResponseEntity = policyCommands.getConfig().getPolicyServiceClient()
                    .updatePolicyEngineResponseEntity(entityId, policyEngine);
            if (engineResponseEntity.getStatusCode().equals(HttpStatus.OK)) {
                policyEngine = ObjectMapperUtils.convertObjectNode(engineResponseEntity.getBody(),
                        PolicyEngineResource.class);
                System.out.println("Policy Engine UPDATED:\n" + ObjectMapperUtils.formatAsString(policyEngine));
            } else
                System.out.println("Got an unexpected response. Error code: " + engineResponseEntity.getStatusCode());
        } catch (ResourceAccessException e) {
            System.out.println("Impossible to connect with Policy server. Verify the URL and retry");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public void updateEvaluation() {
        PolicyEvaluationResultResource policyEvaluationResult;
        try {
            policyEvaluationResult = ObjectMapperUtils.stringToResource(
                    CliFileUtils.readFileFromPath(entityFilePath),
                    PolicyEvaluationResultResource.class);
        } catch (IOException e) {
            System.out.println(
                    "Impossible to read file [" + entityFilePath + "]. Check if the file exists and retry");
            return;
        }
        try {
            ResponseEntity<ObjectNode> policyEvaluationResultResponseEntity = policyCommands.getConfig()
                    .getPolicyServiceClient().updatePolicyEvaluationResultResponseEntity(
                            entityId, policyEvaluationResult);
            if (policyEvaluationResultResponseEntity.getStatusCode().equals(HttpStatus.OK)) {
                policyEvaluationResult = ObjectMapperUtils.convertObjectNode(
                        policyEvaluationResultResponseEntity.getBody(), PolicyEvaluationResultResource.class);
                System.out.println("Policy Evaluation Result UPDATED:\n"
                        + ObjectMapperUtils.formatAsString(policyEvaluationResult));
            } else
                System.out.println("Got an unexpected response. Error code: "
                        + policyEvaluationResultResponseEntity.getStatusCode());
        } catch (ResourceAccessException e) {
            System.out.println("Impossible to connect with Policy server. Verify the URL and retry");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
