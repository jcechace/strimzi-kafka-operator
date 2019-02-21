/*
 * Copyright 2018, Strimzi authors.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.strimzi.operator.common.operator.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.fabric8.kubernetes.api.model.rbac.DoneableKubernetesClusterRole;
import io.fabric8.kubernetes.api.model.rbac.KubernetesClusterRole;
import io.fabric8.kubernetes.api.model.rbac.KubernetesClusterRoleBuilder;
import io.fabric8.kubernetes.api.model.rbac.KubernetesClusterRoleList;
import io.fabric8.kubernetes.api.model.rbac.KubernetesPolicyRule;
import io.fabric8.kubernetes.api.model.rbac.KubernetesPolicyRuleBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.MixedOperation;
import io.fabric8.kubernetes.client.dsl.Resource;
import io.vertx.core.Future;
import io.vertx.core.Vertx;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ClusterRoleOperator extends AbstractNonNamespacedResourceOperator<KubernetesClient, KubernetesClusterRole, KubernetesClusterRoleList, DoneableKubernetesClusterRole, Resource<KubernetesClusterRole, DoneableKubernetesClusterRole>> {

    /**
     * Constructor
     * @param vertx The Vertx instance
     * @param client The Kubernetes client
     */

    public ClusterRoleOperator(Vertx vertx, KubernetesClient client) {
        super(vertx, client, "ClusterRole");
    }

    @Override
    protected MixedOperation<KubernetesClusterRole, KubernetesClusterRoleList, DoneableKubernetesClusterRole, Resource<KubernetesClusterRole, DoneableKubernetesClusterRole>> operation() {
        return client.rbac().kubernetesClusterRoles();
    }

    @Override
    protected Future<ReconcileResult<KubernetesClusterRole>> internalPatch(String name, KubernetesClusterRole current, KubernetesClusterRole desired) {
        return Future.succeededFuture(ReconcileResult.noop(current));
    }

    public static KubernetesClusterRole convertYamlToClusterRole(String yaml) {
        try {
            ObjectMapper yamlReader = new ObjectMapper(new YAMLFactory());
            Object obj = yamlReader.readValue(yaml, Object.class);
            KubernetesClusterRole cr = null;
            if (obj instanceof LinkedHashMap) {
                List<KubernetesPolicyRule> rules = new ArrayList<>();
                ArrayList mapRules = (ArrayList) ((LinkedHashMap) obj).get("rules");
                mapRules.forEach(a -> rules.add(new KubernetesPolicyRuleBuilder()
                    .withApiGroups(((LinkedHashMap) a).get("apiGroups").toString().replace("[", "").replace("]", ""))
                    .withResources(((LinkedHashMap) a).get("resources").toString().replace("[", "").replace("]", ""))
                    .withVerbs(((LinkedHashMap) a).get("verbs").toString().replace("[", "").replace("]", "").split(","))
                    .build()));

                Map<String, String> labels = new HashMap<>();
                String[] metadataArray = ((LinkedHashMap) ((LinkedHashMap) obj).get("metadata")).get("labels")
                        .toString()
                        .replace("{", "")
                        .replace("}", "")
                        .split(",");

                for (int i = 0; i < metadataArray.length; i++) {
                    labels.put(metadataArray[i].split("=")[0], metadataArray[i].split("=")[1]);
                }

                cr = new KubernetesClusterRoleBuilder()
                        .withApiVersion(((LinkedHashMap) obj).get("apiVersion").toString())
                        .withKind(((LinkedHashMap) obj).get("kind").toString())
                        .withRules(rules)
                        .withNewMetadata()
                        .withName(((LinkedHashMap) ((LinkedHashMap) obj).get("metadata")).get("name").toString())
                        .withLabels(labels)
                        .endMetadata()
                        .build();
            }
            return cr;
        } catch (IOException e)   {
            throw new RuntimeException(e);
        }
    }
}
