/*
 * Copyright 2017-2018, Strimzi authors.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.strimzi.operator.common.operator.resource;

import io.fabric8.kubernetes.api.model.rbac.KubernetesClusterRole;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.vertx.core.Vertx;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.mockito.Mockito.mock;

public class ClusterRoleOperatorTest {

    private String yaml = "apiVersion: rbac.authorization.k8s.io/v1beta1\n" +
            "kind: ClusterRole\n" +
            "metadata:\n" +
            "  name: strimzi-cluster-operator-namespaced\n" +
            "  labels:\n" +
            "    app: strimzi\n" +
            "rules:\n" +
            "- apiGroups:\n" +
            "  - \"\"\n" +
            "  resources:\n" +
            "  - serviceaccounts\n" +
            "  verbs:\n" +
            "  - get\n" +
            "  - create\n" +
            "  - delete\n" +
            "  - patch\n" +
            "  - update\n" +
            "- apiGroups:\n" +
            "  - policy\n" +
            "  resources:\n" +
            "  - poddisruptionbudgets\n" +
            "  verbs:\n" +
            "  - get\n" +
            "  - list\n" +
            "  - watch\n" +
            "  - create\n" +
            "  - delete\n" +
            "  - patch\n" +
            "  - update\n";

    @Test
    public void parseYAMLtoClusterRoleTest() {
        ClusterRoleOperator cro = new ClusterRoleOperator(Vertx.vertx(), mock(KubernetesClient.class));
        KubernetesClusterRole cr = cro.convertYamlToClusterRole(yaml);
        assertTrue(cr.getRules().get(0).getApiGroups().get(0).equals(""));
        assertTrue(cr.getRules().get(0).getResources().get(0).equals("serviceaccounts"));
        assertTrue(cr.getRules().get(0).getVerbs().size() == 5);

        assertTrue(cr.getRules().get(1).getApiGroups().get(0).equals("policy"));
        assertTrue(cr.getRules().get(1).getResources().get(0).equals("poddisruptionbudgets"));
        assertTrue(cr.getRules().get(1).getVerbs().size() == 7);
    }
}
