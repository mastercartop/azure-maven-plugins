/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for license information.
 */

package com.microsoft.azure.maven.springcloud.pom;

import com.microsoft.azure.maven.springcloud.TestHelper;
import com.microsoft.azure.maven.springcloud.config.AppDeploymentRawConfig;
import com.microsoft.azure.maven.springcloud.config.AppRawConfig;
import com.microsoft.azure.maven.springcloud.config.ConfigurationUpdater;
import org.apache.commons.io.FileUtils;
import org.apache.maven.model.Model;
import org.apache.maven.model.Plugin;
import org.apache.maven.plugin.descriptor.PluginDescriptor;
import org.apache.maven.project.MavenProject;
import org.junit.Test;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PomUtilsTest {
    @Test
    public void testSaveXml() throws Exception {
        final File pomFile = new File(this.getClass().getClassLoader().getResource("pom-4.xml").getFile());
        final File tempFile = Files.createTempFile("azure-spring-apps-plugin-test", ".xml").toFile();
        FileUtils.copyFile(pomFile, tempFile);
        final Model model = TestHelper.readMavenModel(tempFile);
        final MavenProject project = mock(MavenProject.class);
        when(project.getModel()).thenReturn(model);
        when(project.getFile()).thenReturn(tempFile);
        final AppRawConfig app = new AppRawConfig();
        app.setSubscriptionId("subscriptionId1");
        app.setClusterName("clusterName1");
        app.setAppName("appName1");
        app.setIsPublic("true");

        final AppDeploymentRawConfig deploy = new AppDeploymentRawConfig();
        deploy.setCpu("1");
        deploy.setMemoryInGB("2");
        deploy.setInstanceCount("3");
        deploy.setRuntimeVersion("8");
        deploy.setJvmOptions("jvmOptions1");
        final Plugin plugin = model.getBuild().getPlugins().get(0);

        final PluginDescriptor pd = mock(PluginDescriptor.class);
        when(pd.getGroupId()).thenReturn(plugin.getGroupId());
        when(pd.getArtifactId()).thenReturn(plugin.getArtifactId());
        when(pd.getVersion()).thenReturn(plugin.getVersion());
        app.setDeployment(deploy);
        ConfigurationUpdater.updateAppConfigToPom(app, project, pd);
        final String updatedXml = String.join("\n", Files.readAllLines(tempFile.toPath(), Charset.defaultCharset()));
        assertTrue(updatedXml.contains("<maven.compiler.target>1.8\n" +
            "        </maven.compiler.target>"));
        assertTrue(updatedXml.contains("<configuration>\n" +
            "                    <subscriptionId>subscriptionId1</subscriptionId>\n" +
            "                    <clusterName>clusterName1</clusterName>\n" +
            "                    <appName>appName1</appName>\n" +
            "                    <isPublic>true</isPublic>\n" +
            "                    <deployment>\n" +
            "                        <cpu>1</cpu>\n" +
            "                        <memoryInGB>2</memoryInGB>\n" +
            "                        <instanceCount>3</instanceCount>\n" +
            "                        <jvmOptions>jvmOptions1</jvmOptions>\n" +
            "                        <runtimeVersion>8</runtimeVersion>\n" +
            "                        <resources>\n" +
            "                            <resource>\n" +
            "                                <filtering/>\n" +
            "                                <mergeId/>\n" +
            "                                <targetPath/>\n" +
            "                                <directory>${project.basedir}/target</directory>\n" +
            "                                <includes>\n" +
            "                                    <include>*.jar</include>\n" +
            "                                </includes>\n" +
            "                            </resource>\n" +
            "                        </resources>\n" +
            "                    </deployment>\n" +
            "                </configuration>"));
        tempFile.delete();
    }

    @Test
    public void testSaveXmlNoBuild() throws Exception {
        final File pomFile = new File(this.getClass().getClassLoader().getResource("pom-5.xml").getFile());
        final File tempFile = Files.createTempFile("azure-spring-apps-plugin-test", ".xml").toFile();
        FileUtils.copyFile(pomFile, tempFile);
        final Model model = TestHelper.readMavenModel(tempFile);
        final MavenProject project = mock(MavenProject.class);
        when(project.getModel()).thenReturn(model);
        when(project.getFile()).thenReturn(tempFile);
        final AppRawConfig app = new AppRawConfig();
        app.setSubscriptionId("subscriptionId1");
        app.setClusterName("clusterName1");
        app.setAppName("appName1");
        app.setIsPublic("true");

        final AppDeploymentRawConfig deploy = new AppDeploymentRawConfig();
        deploy.setCpu("1");
        deploy.setMemoryInGB("2");
        deploy.setInstanceCount("3");
        deploy.setRuntimeVersion("8");
        deploy.setJvmOptions("jvmOptions1");

        final PluginDescriptor pd = mock(PluginDescriptor.class);
        when(pd.getGroupId()).thenReturn("com.microsoft.azure");
        when(pd.getArtifactId()).thenReturn("azure-spring-apps-maven-plugin");
        when(pd.getVersion()).thenReturn("0.1.0.SNAPSHOT");
        app.setDeployment(deploy);
        ConfigurationUpdater.updateAppConfigToPom(app, project, pd);
        final String updatedXml = String.join("\n", Files.readAllLines(tempFile.toPath(), Charset.defaultCharset()));
        assertTrue(updatedXml.contains("<maven.compiler.target>1.8\n" +
            "        </maven.compiler.target>"));
        assertTrue(updatedXml.contains("<configuration>\n" +
            "                    <subscriptionId>subscriptionId1</subscriptionId>\n" +
            "                    <clusterName>clusterName1</clusterName>\n" +
            "                    <appName>appName1</appName>\n" +
            "                    <isPublic>true</isPublic>\n" +
            "                    <deployment>\n" +
            "                        <cpu>1</cpu>\n" +
            "                        <memoryInGB>2</memoryInGB>\n" +
            "                        <instanceCount>3</instanceCount>\n" +
            "                        <jvmOptions>jvmOptions1</jvmOptions>\n" +
            "                        <runtimeVersion>8</runtimeVersion>\n" +
            "                        <resources>\n" +
            "                            <resource>\n" +
            "                                <filtering/>\n" +
            "                                <mergeId/>\n" +
            "                                <targetPath/>\n" +
            "                                <directory>${project.basedir}/target</directory>\n" +
            "                                <includes>\n" +
            "                                    <include>*.jar</include>\n" +
            "                                </includes>\n" +
            "                            </resource>\n" +
            "                        </resources>\n" +
            "                    </deployment>\n" +
            "                </configuration>"));
        tempFile.delete();
    }
}
