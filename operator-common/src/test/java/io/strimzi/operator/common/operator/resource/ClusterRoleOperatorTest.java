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
            "  - rbac.authorization.k8s.io\n" +
            "  resources:\n" +
            "  - rolebindings\n" +
            "  verbs:\n" +
            "  - get\n" +
            "  - create\n" +
            "  - delete\n" +
            "  - patch\n" +
            "  - update\n" +
            "- apiGroups:\n" +
            "  - \"\"\n" +
            "  resources:\n" +
            "  - configmaps\n" +
            "  verbs:\n" +
            "  - get\n" +
            "  - list\n" +
            "  - watch\n" +
            "  - create\n" +
            "  - delete\n" +
            "  - patch\n" +
            "  - update\n" +
            "- apiGroups:\n" +
            "  - kafka.strimzi.io\n" +
            "  resources:\n" +
            "  - kafkas\n" +
            "  - kafkaconnects\n" +
            "  - kafkaconnects2is\n" +
            "  - kafkamirrormakers\n" +
            "  verbs:\n" +
            "  - get\n" +
            "  - list\n" +
            "  - watch\n" +
            "  - create\n" +
            "  - delete\n" +
            "  - patch\n" +
            "  - update\n" +
            "- apiGroups:\n" +
            "  - \"\"\n" +
            "  resources:\n" +
            "  - pods\n" +
            "  verbs:\n" +
            "  - get\n" +
            "  - list\n" +
            "  - watch\n" +
            "  - delete\n" +
            "- apiGroups:\n" +
            "  - \"\"\n" +
            "  resources:\n" +
            "  - services\n" +
            "  verbs:\n" +
            "  - get\n" +
            "  - list\n" +
            "  - watch\n" +
            "  - create\n" +
            "  - delete\n" +
            "  - patch\n" +
            "  - update\n" +
            "- apiGroups:\n" +
            "  - \"\"\n" +
            "  resources:\n" +
            "  - endpoints\n" +
            "  verbs:\n" +
            "  - get\n" +
            "  - list\n" +
            "  - watch\n" +
            "- apiGroups:\n" +
            "  - extensions\n" +
            "  resources:\n" +
            "  - deployments\n" +
            "  - deployments/scale\n" +
            "  - replicasets\n" +
            "  verbs:\n" +
            "  - get\n" +
            "  - list\n" +
            "  - watch\n" +
            "  - create\n" +
            "  - delete\n" +
            "  - patch\n" +
            "  - update\n" +
            "- apiGroups:\n" +
            "  - apps\n" +
            "  resources:\n" +
            "  - deployments\n" +
            "  - deployments/scale\n" +
            "  - deployments/status\n" +
            "  - statefulsets\n" +
            "  - replicasets\n" +
            "  verbs:\n" +
            "  - get\n" +
            "  - list\n" +
            "  - watch\n" +
            "  - create\n" +
            "  - delete\n" +
            "  - patch\n" +
            "  - update\n" +
            "- apiGroups:\n" +
            "  - \"\"\n" +
            "  resources:\n" +
            "  - events\n" +
            "  verbs:\n" +
            "  - create\n" +
            "- apiGroups:\n" +
            "  - extensions\n" +
            "  resources:\n" +
            "  - replicationcontrollers\n" +
            "  verbs:\n" +
            "  - get\n" +
            "  - list\n" +
            "  - watch\n" +
            "  - create\n" +
            "  - delete\n" +
            "  - patch\n" +
            "  - update\n" +
            "- apiGroups:\n" +
            "  - apps.openshift.io\n" +
            "  resources:\n" +
            "  - deploymentconfigs\n" +
            "  - deploymentconfigs/scale\n" +
            "  - deploymentconfigs/status\n" +
            "  - deploymentconfigs/finalizers\n" +
            "  verbs:\n" +
            "  - get\n" +
            "  - list\n" +
            "  - watch\n" +
            "  - create\n" +
            "  - delete\n" +
            "  - patch\n" +
            "  - update\n" +
            "- apiGroups:\n" +
            "  - build.openshift.io\n" +
            "  resources:\n" +
            "  - buildconfigs\n" +
            "  - builds\n" +
            "  verbs:\n" +
            "  - create\n" +
            "  - delete\n" +
            "  - get\n" +
            "  - list\n" +
            "  - patch\n" +
            "  - watch\n" +
            "  - update\n" +
            "- apiGroups:\n" +
            "  - image.openshift.io\n" +
            "  resources:\n" +
            "  - imagestreams\n" +
            "  - imagestreams/status\n" +
            "  verbs:\n" +
            "  - create\n" +
            "  - delete\n" +
            "  - get\n" +
            "  - list\n" +
            "  - watch\n" +
            "  - patch\n" +
            "  - update\n" +
            "- apiGroups:\n" +
            "  - \"\"\n" +
            "  resources:\n" +
            "  - replicationcontrollers\n" +
            "  verbs:\n" +
            "  - get\n" +
            "  - list\n" +
            "  - watch\n" +
            "  - create\n" +
            "  - delete\n" +
            "  - patch\n" +
            "  - update\n" +
            "- apiGroups:\n" +
            "  - \"\"\n" +
            "  resources:\n" +
            "  - secrets\n" +
            "  verbs:\n" +
            "  - get\n" +
            "  - list\n" +
            "  - create\n" +
            "  - delete\n" +
            "  - patch\n" +
            "  - update\n" +
            "- apiGroups:\n" +
            "  - extensions\n" +
            "  resources:\n" +
            "  - networkpolicies\n" +
            "  verbs:\n" +
            "  - get\n" +
            "  - list\n" +
            "  - watch\n" +
            "  - create\n" +
            "  - delete\n" +
            "  - patch\n" +
            "  - update\n" +
            "- apiGroups:\n" +
            "  - networking.k8s.io\n" +
            "  resources:\n" +
            "  - networkpolicies\n" +
            "  verbs:\n" +
            "  - get\n" +
            "  - list\n" +
            "  - watch\n" +
            "  - create\n" +
            "  - delete\n" +
            "  - patch\n" +
            "  - update\n" +
            "- apiGroups:\n" +
            "  - route.openshift.io\n" +
            "  resources:\n" +
            "  - routes\n" +
            "  verbs:\n" +
            "  - get\n" +
            "  - list\n" +
            "  - create\n" +
            "  - delete\n" +
            "  - patch\n" +
            "  - update\n" +
            "- apiGroups:\n" +
            "  - \"\"\n" +
            "  resources:\n" +
            "  - persistentvolumeclaims\n" +
            "  verbs:\n" +
            "  - get\n" +
            "  - list\n" +
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
        KubernetesClusterRole cr = cro.convertYamlToClusterRole("test", yaml);
        assertTrue(cr.getRules().get(0).getApiGroups().get(0).equals(""));
        assertTrue(cr.getRules().get(0).getResources().get(0).equals("serviceaccounts"));
        assertTrue(cr.getRules().get(0).getVerbs().size() == 5);

        assertTrue(cr.getRules().get(20).getApiGroups().get(0).equals("policy"));
        assertTrue(cr.getRules().get(20).getResources().get(0).equals("poddisruptionbudgets"));
        assertTrue(cr.getRules().get(20).getVerbs().size() == 7);
    }
}
