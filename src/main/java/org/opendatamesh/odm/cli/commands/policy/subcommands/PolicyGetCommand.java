package org.opendatamesh.odm.cli.commands.policy.subcommands;

import org.opendatamesh.odm.cli.commands.policy.PolicyCommands;
import org.opendatamesh.odm.cli.utils.ObjectMapperUtils;
import org.opendatamesh.platform.pp.policy.api.resources.PolicyEngineResource;
import org.opendatamesh.platform.pp.policy.api.resources.PolicyEvaluationResultResource;
import org.opendatamesh.platform.pp.policy.api.resources.PolicyResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.databind.node.ObjectNode;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParentCommand;

@Command(name = "get", description = "Get an entity from policy-service", mixinStandardHelpOptions = true, version = "odm-cli policy get 1.0.0")
public class PolicyGetCommand implements Runnable {

    @Option(names = "--type", description = "Type of the entity to start (policy, engine, eval)", defaultValue = "policy", required = false)
    String type;

    @Option(names = "--id", description = "Id of the entity to get", required = true)
    Long entityId;

    @Option(names = "--root", description = "Whether the ID is the root ID (default) or the version ID. Set it to 'false' to get by version ID", defaultValue = "true")
    Boolean rootIdFlag;

    @ParentCommand
    protected PolicyCommands policyCommands;

    @Override
    public void run() {
        if (type.equalsIgnoreCase("policy")) {
            getPolicy();
        } else if (type.equalsIgnoreCase("engine")) {
            getEngine();
        } else if (type.equalsIgnoreCase("eval")) {
            getEvaluation();
        } else {
            System.err.println("Unknown entity type [" + type + "]");
        }
    }

    public void getPolicy() {

        try {
            // Remove ResponseEntity and change the methods used by the client after
            // refactoring RestUtils in policy service
            ResponseEntity<ObjectNode> policyObjectNodeResponseEntity;
            if (!rootIdFlag)
                policyObjectNodeResponseEntity = policyCommands.getConfig().getPolicyServiceClient()
                        .readOnePolicyVersionResponseEntity(entityId);
            else
                policyObjectNodeResponseEntity = policyCommands.getConfig().getPolicyServiceClient()
                        .readOnePolicyResponseEntity(entityId);
            if (policyObjectNodeResponseEntity.getStatusCode().equals(HttpStatus.OK)) {
                PolicyResource policyResource = ObjectMapperUtils.convertObjectNode(
                        policyObjectNodeResponseEntity.getBody(), PolicyResource.class);
                System.out.println(ObjectMapperUtils.formatAsString(policyResource));
            } else if (policyObjectNodeResponseEntity.getStatusCode().equals(HttpStatus.NOT_FOUND))
                System.out.println("Policy with " + (rootIdFlag ? "root" : "") + "ID [" + entityId + "] not found");
            else
                System.out.println(
                        "Got an unexpected response. Error code: " + policyObjectNodeResponseEntity.getStatusCode());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public void getEngine() {

        try {
            // Remove ResponseEntity and change the methods used by the client after
            // refactoring RestUtils in policy service
            ResponseEntity<ObjectNode> engineResponseEntity = policyCommands.getConfig().getPolicyServiceClient()
                    .readOnePolicyEngineResponseEntity(entityId);
            if (engineResponseEntity.getStatusCode().equals(HttpStatus.OK)) {
                PolicyEngineResource engineResource = ObjectMapperUtils.convertObjectNode(
                        engineResponseEntity.getBody(), PolicyEngineResource.class);
                System.out.println(ObjectMapperUtils.formatAsString(engineResource));
            } else if (engineResponseEntity.getStatusCode().equals(HttpStatus.NOT_FOUND))
                System.out.println("Policy Engine with ID [" + entityId + "] not found");
            else
                System.out.println("Got an unexpected response. Error code: " + engineResponseEntity.getStatusCode());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

     public void getEvaluation() {

        try {
            // Remove ResponseEntity and change the methods used by the client after refactoring RestUtils in policy service
            ResponseEntity<ObjectNode> resultResponseEntity =
            policyCommands.getConfig().getPolicyServiceClient().readOnePolicyEvaluationResultResponseEntity(entityId);

            if(resultResponseEntity.getStatusCode().equals(HttpStatus.OK)) {
                PolicyEvaluationResultResource resultResource = ObjectMapperUtils.convertObjectNode(
                        resultResponseEntity.getBody(), PolicyEvaluationResultResource.class
                );
                System.out.println(ObjectMapperUtils.formatAsString(resultResource));
            }
            else if(resultResponseEntity.getStatusCode().equals(HttpStatus.NOT_FOUND))
                System.out.println("Policy Evaluation Result with ID [" + entityId + "] not found");
            else
                System.out.println("Got an unexpected response. Error code: " + resultResponseEntity.getStatusCode());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}
