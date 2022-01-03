/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for license information.
 */

package com.microsoft.azure.toolkit.lib;

import com.azure.resourcemanager.resources.fluentcore.arm.ResourceId;
import com.microsoft.azure.toolkit.lib.common.model.AbstractAzResource;
import com.microsoft.azure.toolkit.lib.common.model.AbstractAzResourceModule;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import java.util.LinkedList;
import java.util.Objects;

public interface AzService {

    String getName();

    void refresh();

    default <R> R getFromId(@Nonnull String id) { // move to upper class
        ResourceId resourceId = ResourceId.fromString(id);
        final String resourceGroup = resourceId.resourceGroupName();
        AbstractAzResource<?, ?, ?> resource = Objects.requireNonNull(this.getResourceManager(resourceId.subscriptionId(), resourceGroup));
        final LinkedList<Pair<String, String>> resourceTypeNames = new LinkedList<>();
        while (resourceId != null) {
            resourceTypeNames.push(Pair.of(resourceId.resourceType(), resourceId.name()));
            resourceId = resourceId.parent();
        }
        for (Pair<String, String> resourceTypeName : resourceTypeNames) {
            if (Objects.isNull(resource)) {
                return null;
            }
            resource = ((AbstractAzResourceModule<?, ?, ?>) resource.getSubModule(resourceTypeName.getLeft())).get(resourceTypeName.getRight(), resourceGroup);
        }
        return (R) resource;
    }

    default <R> R getOrDraftFromId(@Nonnull String id) { // move to upper class
        ResourceId resourceId = ResourceId.fromString(id);
        final String resourceGroup = resourceId.resourceGroupName();
        AbstractAzResource<?, ?, ?> resource = Objects.requireNonNull(this.getResourceManager(resourceId.subscriptionId(), resourceGroup));
        final LinkedList<Pair<String, String>> resourceTypeNames = new LinkedList<>();
        while (resourceId != null) {
            resourceTypeNames.push(Pair.of(resourceId.resourceType(), resourceId.name()));
            resourceId = resourceId.parent();
        }
        for (Pair<String, String> resourceTypeName : resourceTypeNames) {
            resource = ((AbstractAzResourceModule<?, ?, ?>) resource.getSubModule(resourceTypeName.getLeft())).getOrDraft(resourceTypeName.getRight(), resourceGroup);
        }
        return (R) resource;
    }

    default AbstractAzResource<?, ?, ?> getResourceManager(String subscriptionId, String resourceGroup) {
        return null;
    }
}
