/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for license information.
 */

package com.microsoft.azure.toolkit.lib.compute.vm;

import com.azure.resourcemanager.compute.models.VirtualMachineSize;
import lombok.Getter;

@Getter
public class AzureVirtualMachineSize {
    private final String name;
    private final int numberOfCores;
    private final int osDiskSizeInMB;
    private final int resourceDiskSizeInMB;
    private final int memoryInMB;
    private final int maxDataDiskCount;

    AzureVirtualMachineSize(final VirtualMachineSize size) {
        this.name = size.name();
        this.numberOfCores = size.numberOfCores();
        this.osDiskSizeInMB = size.osDiskSizeInMB();
        this.resourceDiskSizeInMB = size.resourceDiskSizeInMB();
        this.memoryInMB = size.memoryInMB();
        this.maxDataDiskCount = size.maxDataDiskCount();
    }
}
