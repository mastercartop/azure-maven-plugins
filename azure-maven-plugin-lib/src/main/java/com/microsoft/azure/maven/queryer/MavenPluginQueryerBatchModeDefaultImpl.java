/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for license information.
 */

package com.microsoft.azure.maven.queryer;

import com.microsoft.azure.toolkit.lib.common.exception.AzureToolkitRuntimeException;
import com.microsoft.azure.toolkit.lib.common.logging.Log;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class MavenPluginQueryerBatchModeDefaultImpl extends MavenPluginQueryer {
    @Override
    public String assureInputFromUser(String attribute, String defaultValue, List<String> options, String prompt) {
        final String initValue = System.getProperty(attribute);
        final String input = StringUtils.isNotEmpty(initValue) ? initValue : defaultValue;
        if (validateInputByOptions(input, options)) {
            Log.info(String.format("Use %s for %s", input, attribute));
            return input;
        }
        throw new AzureToolkitRuntimeException(String.format("Invalid input for %s : %s", attribute, input));
    }

    @Override
    public String assureInputFromUser(String attribute, String defaultValue, String regex, String errorMessage, String prompt) {
        final String initValue = System.getProperty(attribute);
        final String input = StringUtils.isNotEmpty(initValue) ? initValue : defaultValue;
        if (StringUtils.isNotEmpty(input) && validateInputByRegex(input, regex)) {
            Log.info(String.format("Use %s for %s", input, attribute));
            return input;
        } else {
            throw new AzureToolkitRuntimeException(String.format("Invalid input for %s : %s", attribute, input));
        }
    }

    @Override
    public void close() {
    }
}
