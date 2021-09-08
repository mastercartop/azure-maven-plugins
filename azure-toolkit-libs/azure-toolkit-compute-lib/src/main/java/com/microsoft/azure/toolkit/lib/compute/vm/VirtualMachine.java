/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for license information.
 */

package com.microsoft.azure.toolkit.lib.compute.vm;

import com.microsoft.azure.toolkit.lib.common.entity.IAzureBaseResource;
import com.microsoft.azure.toolkit.lib.common.entity.IAzureModule;
import com.microsoft.azure.toolkit.lib.compute.AbstractAzureResource;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class VirtualMachine extends AbstractAzureResource<com.azure.resourcemanager.compute.models.VirtualMachine, IAzureBaseResource> {

    protected AzureVirtualMachine module;

    public VirtualMachine(@Nonnull String id, @Nullable AzureVirtualMachine module) {
        super(id);
        this.module = module;
    }

    public VirtualMachine(@Nonnull com.azure.resourcemanager.compute.models.VirtualMachine resource, @Nonnull AzureVirtualMachine module) {
        super(resource);
        this.module = module;
    }

    @Nonnull
    @Override
    public IAzureModule<? extends AbstractAzureResource<com.azure.resourcemanager.compute.models.VirtualMachine, IAzureBaseResource>,
            ? extends IAzureBaseResource> module() {
        return module;
    }

    @Override
    protected String loadStatus() {
        return remote().provisioningState();
    }

    @Nullable
    @Override
    protected com.azure.resourcemanager.compute.models.VirtualMachine loadRemote() {
        return module.getVirtualMachinesManager(subscriptionId).getByResourceGroup(resourceGroup, name);
    }
}
