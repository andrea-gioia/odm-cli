package org.opendatamesh.cli.commands.policy.subcommands;

import java.util.List;

import org.opendatamesh.cli.commands.policy.PolicyCommands;
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

@Command(name = "list", description = "List policy-service entities", mixinStandardHelpOptions = true, version = "odm-cli policy list 1.0.0")
public class PolicyListCommand implements Runnable {

    @Option(names = "--type", description = "Type of the entity to start (policy, engine, eval)", defaultValue = "policy", required = false)
    String type;

    @ParentCommand
    protected PolicyCommands policyCommands;

    @Override
    public void run() {
        if (type.equalsIgnoreCase("policy")) {
            listPolicies();
        } else if (type.equalsIgnoreCase("engine")) {
            listEngines();
        } else if (type.equalsIgnoreCase("eval")) {
            listEvaluations();
        } else {
            System.err.println("Unknown entity type [" + type + "]");
        }
    }

    public void listPolicies() {
        try {
            ResponseEntity<ObjectNode> policyResourceResponseEntity = policyCommands.getConfig()
                    .getPolicyServiceClient().readAllPoliciesResponseEntity();
            if (policyResourceResponseEntity.getStatusCode().equals(HttpStatus.OK)) {
                List<PolicyResource> policies = ObjectMapperUtils.extractListFromPageFromObjectNode(
                        policyResourceResponseEntity.getBody(), PolicyResource.class);
                if (policies.size() == 0)
                    System.out.println("[]");
                for (PolicyResource policy : policies)
                    System.out.println(ObjectMapperUtils.formatAsString(policy));
            } else
                System.out.println("Error in response from Policy Server");
        } catch (ResourceAccessException e) {
            System.out.println("Impossible to connect with Policy server. Verify the URL and retry");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void listEngines() {
        try {
            ResponseEntity<ObjectNode> engineResourceResponseEntity = policyCommands.getConfig()
                    .getPolicyServiceClient().readAllPolicyEnginesResponseEntity();
            if (engineResourceResponseEntity.getStatusCode().equals(HttpStatus.OK)) {
                List<PolicyEngineResource> engines = ObjectMapperUtils.extractListFromPageFromObjectNode(
                        engineResourceResponseEntity.getBody(), PolicyEngineResource.class);
                if (engines.size() == 0)
                    System.out.println("[]");
                for (PolicyEngineResource engine : engines)
                    System.out.println(ObjectMapperUtils.formatAsString(engine));
            } else
                System.out.println("Error in response from Policy Server");
        } catch (ResourceAccessException e) {
            System.out.println("Impossible to connect with Policy server. Verify the URL and retry");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void listEvaluations() {
        try {
            ResponseEntity<ObjectNode> resultResourceResponseEntity = policyCommands.getConfig()
                    .getPolicyServiceClient().readAllPolicyEvaluationResultsResponseEntity();
            if (resultResourceResponseEntity.getStatusCode().equals(HttpStatus.OK)) {
                List<PolicyEvaluationResultResource> results = ObjectMapperUtils.extractListFromPageFromObjectNode(
                        resultResourceResponseEntity.getBody(), PolicyEvaluationResultResource.class);
                if (results.size() == 0)
                    System.out.println("[]");
                for (PolicyEvaluationResultResource result : results)
                    System.out.println(ObjectMapperUtils.formatAsString(result));
            } else
                System.out.println("Error in response from Policy Server");
        } catch (ResourceAccessException e) {
            System.out.println("Impossible to connect with Policy server. Verify the URL and retry");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
