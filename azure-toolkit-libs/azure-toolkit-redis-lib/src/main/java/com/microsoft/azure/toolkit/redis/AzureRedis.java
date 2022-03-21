/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for license information.
 */

package com.microsoft.azure.toolkit.redis;

import com.azure.core.http.policy.HttpLogDetailLevel;
import com.azure.core.management.profile.AzureProfile;
import com.azure.resourcemanager.redis.RedisManager;
import com.microsoft.azure.toolkit.lib.Azure;
import com.microsoft.azure.toolkit.lib.AzureConfiguration;
import com.microsoft.azure.toolkit.lib.auth.Account;
import com.microsoft.azure.toolkit.lib.auth.AzureAccount;
import com.microsoft.azure.toolkit.lib.common.model.AbstractAzResourceManager;
import com.microsoft.azure.toolkit.lib.common.model.AbstractAzService;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nonnull;
import java.util.Optional;

@Slf4j
public class AzureRedis extends AbstractAzService<RedisResourceManager, RedisManager> {
    public AzureRedis() {
        super("Microsoft.Cache");
    }

    @Nonnull
    public RedisCacheModule caches(@Nonnull String subscriptionId) {
        final RedisResourceManager rm = get(subscriptionId, null);
        assert rm != null;
        return rm.getCacheModule();
    }

    @Nonnull
    @Override
    protected RedisManager loadResourceFromAzure(@Nonnull String subscriptionId, String resourceGroup) {
        final Account account = Azure.az(AzureAccount.class).account();
        final AzureConfiguration config = Azure.az().config();
        final String userAgent = config.getUserAgent();
        final HttpLogDetailLevel logLevel = Optional.ofNullable(config.getLogLevel()).map(HttpLogDetailLevel::valueOf).orElse(HttpLogDetailLevel.NONE);
        final AzureProfile azureProfile = new AzureProfile(null, subscriptionId, account.getEnvironment());
        return RedisManager.configure()
            .withHttpClient(AbstractAzResourceManager.getDefaultHttpClient())
            .withLogLevel(logLevel)
            .withPolicy(AbstractAzResourceManager.getUserAgentPolicy(userAgent)) // set user agent with policy
            .authenticate(account.getTokenCredential(subscriptionId), azureProfile);
    }

    @Nonnull
    @Override
    protected RedisResourceManager newResource(@Nonnull RedisManager remote) {
        return new RedisResourceManager(remote, this);
    }

    @Nonnull
    @Override
    public String getResourceTypeName() {
        return "Azure Cache for Redis";
    }
}
