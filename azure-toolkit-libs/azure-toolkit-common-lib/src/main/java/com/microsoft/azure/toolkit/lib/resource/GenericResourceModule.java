/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for license information.
 */

package com.microsoft.azure.toolkit.lib.resource;

import com.azure.resourcemanager.resources.ResourceManager;
import com.azure.resourcemanager.resources.fluentcore.arm.ResourceId;
import com.azure.resourcemanager.resources.models.GenericResources;
import com.microsoft.azure.toolkit.lib.Azure;
import com.microsoft.azure.toolkit.lib.common.model.AbstractAzResource;
import com.microsoft.azure.toolkit.lib.common.model.AbstractAzResourceModule;
import com.microsoft.azure.toolkit.lib.common.operation.AzureOperation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public class GenericResourceModule extends AbstractAzResourceModule {

    public static final String NAME = "genericResources";

    public GenericResourceModule(@Nonnull ResourceGroup parent) {
        super(NAME, parent);
    }

    @Nullable
    @Override
    public GenericResources getClient() {
        final ResourcesServiceSubscription parent = (ResourcesServiceSubscription) this.parent.getParent();
        return Optional.ofNullable(parent.getRemote()).map(ResourceManager::genericResources).orElse(null);
    }

    @Nonnull
    @Override
    @AzureOperation(name = "resource.list_resources.type", params = {"this.getResourceTypeName()"}, type = AzureOperation.Type.SERVICE)
    protected Stream<com.azure.resourcemanager.resources.models.GenericResource> loadResourcesFromAzure() {
        final GenericResources resources = Objects.requireNonNull(this.getClient());
        return resources.listByResourceGroup(this.parent.getName()).stream()
            .filter(r -> Objects.isNull(ResourceId.fromString(r.id()).parent())); // only keep top resources.
    }

    @Nonnull
    @Override
    public String toResourceId(@Nonnull String resourceId, @Nullable String resourceGroup) {
        return resourceId;
    }

    @Nonnull
    protected AbstractAzResource<?, ?, ?> newResource(@Nonnull Object r) {
        if (r instanceof com.azure.resourcemanager.resources.models.GenericResource) {
            final com.azure.resourcemanager.resources.models.GenericResource gr = (com.azure.resourcemanager.resources.models.GenericResource) r;
            final AbstractAzResource<?, ?, ?> concrete = Azure.az().getOrInitById(gr.id());
            if (Objects.isNull(concrete)) {
                return new GenericResource(gr, this);
            } else {
                return concrete;
            }
        } else if (r instanceof AbstractAzResource) {
            return (AbstractAzResource<?, ?, ?>) r;
        }
        throw new UnsupportedOperationException("not supported");
    }

    @Nonnull
    protected AbstractAzResource<?, ?, ?> newResource(@Nonnull String resourceId, @Nullable String resourceGroupName) {
        throw new UnsupportedOperationException("not supported");
    }

    @Nonnull
    @Override
    public String getResourceTypeName() {
        return "Generic resource";
    }
}
