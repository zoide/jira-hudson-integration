/*
 * Licensed to Marvelution under one or more contributor license 
 * agreements.  See the NOTICE file distributed with this work 
 * for additional information regarding copyright ownership.
 * Marvelution licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.marvelution.jira.plugins.hudson.panels;

import java.util.Map;

import com.atlassian.jira.bc.project.component.ProjectComponent;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.project.Project;
import com.atlassian.jira.project.ProjectManager;
import com.atlassian.jira.project.version.Version;
import com.atlassian.plugin.webresource.WebResourceManager;
import com.marvelution.jira.plugins.hudson.service.HudsonServerManager;

/**
 * Helper class for Hudson Builds TabPanels
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class HudsonBuildsTabPanelHelper {

	private static final String HUDSON_BUILD_PLUGIN = "com.marvelution.jira.plugins.hudson";

	private final HudsonServerManager serverManager;

	private final ProjectManager projectManager;

	private final WebResourceManager webResourceManager;

	/**
	 * Constructor
	 * 
	 * @param projectManager the {@link ProjectManager} implementation
	 * @param serverManager the {@link HudsonServerManager} implementation
	 * @param webResourceManager the {@link WebResourceManager} implementation
	 */
	public HudsonBuildsTabPanelHelper(ProjectManager projectManager, HudsonServerManager serverManager,
										WebResourceManager webResourceManager) {
		this.projectManager = projectManager;
		this.serverManager = serverManager;
		this.webResourceManager = webResourceManager;
	}

	/**
	 * Prepare the velocity parameters for the Tab Panel
	 * 
	 * @param velocityParams {@link Map} of existing velocity parameters to extend
	 * @param project the {@link Project} to prepare the velocity parameters for
	 */
	public void prepareVelocityParameters(Map<String, Object> velocityParams, Project project) {
		velocityParams.put("projectKey", project.getKey());
		velocityParams.put("hudsonServer", serverManager.getServerByJiraProject(project));
		prepareVelocityParameters(velocityParams, "projectKey=" + project.getKey(), "project");
	}

	/**
	 * Prepare the velocity parameters for the Tab Panel
	 * 
	 * @param velocityParams {@link Map} of existing velocity parameters to extend
	 * @param version the {@link Version} to prepare the velocity parameters for
	 */
	public void prepareVelocityParameters(Map<String, Object> velocityParams, Version version) {
		velocityParams.put("versionId", version.getId());
		velocityParams.put("hudsonServer", serverManager.getServerByJiraProject(version.getProjectObject()));
		prepareVelocityParameters(velocityParams, "versionId=" + version.getId(), "version");
	}

	/**
	 * Prepare the velocity parameters for the Tab Panel
	 * 
	 * @param velocityParams {@link Map} of existing velocity parameters to extend
	 * @param issue the {@link Issue} to prepare the velocity parameters for
	 */
	public void prepareVelocityParameters(Map<String, Object> velocityParams, Issue issue) {
		velocityParams.put("issueKey", issue.getKey());
		velocityParams.put("hudsonServer", serverManager.getServerByJiraProject(issue.getProjectObject()));
		prepareVelocityParameters(velocityParams, "issueKey=" + issue.getKey(), "issue");
	}

	/**
	 * Prepare the velocity parameters for the Tab Panel
	 * 
	 * @param velocityParams {@link Map} of existing velocity parameters to extend
	 * @param component the {@link ProjectComponent} to prepare the velocity parameters for
	 */
	public void prepareVelocityParameters(Map<String, Object> velocityParams, ProjectComponent component) {
		velocityParams.put("componentId", component.getId());
		final Project project = projectManager.getProjectObj(component.getProjectId());
		velocityParams.put("hudsonServer", serverManager.getServerByJiraProject(project));
		prepareVelocityParameters(velocityParams, "componentId=" + component.getId(), "component");
	}

	/**
	 * Prepare velocity paremters for Hudson Builds Tab panels
	 * 
	 * @param velocityParams the existing {@link Map} of velocity parameters to extend
	 * @param querySection the query section for invoking the ViewHudsonServerPanelContent page
	 * @param moduleKey the module key of the panel
	 */
	private void prepareVelocityParameters(Map<String, Object> velocityParams, String querySection, String moduleKey) {
		webResourceManager.requireResource(HUDSON_BUILD_PLUGIN + ":css");
		webResourceManager.requireResource("jira.webresources:prototype");
		velocityParams.put("querySection", querySection);
		velocityParams.put("moduleKey", moduleKey);
		velocityParams.put("baseResourceUrl", "/download/resources/" + HUDSON_BUILD_PLUGIN + ":hudson-" + moduleKey
			+ "-tabpanel");
	}

}
