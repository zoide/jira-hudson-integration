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

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import webwork.action.ActionContext;

import com.atlassian.core.util.collection.EasyList;
import com.atlassian.jira.bc.project.component.ProjectComponent;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.project.Project;
import com.atlassian.jira.project.ProjectManager;
import com.atlassian.jira.project.version.Version;
import com.atlassian.jira.util.ParameterUtils;
import com.atlassian.plugin.webresource.WebResourceManager;
import com.marvelution.jira.plugins.hudson.service.HudsonServerManager;

/**
 * Helper class for Hudson Builds TabPanels
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class HudsonBuildsTabPanelHelper {

	public static final String HUDSON_BUILD_PLUGIN = "com.marvelution.jira.plugins.hudson";

	public static final String SELECTED_SUB_TAB_KEY = "selectedSubTab";

	public static final String SUB_TAB_BUILD_BY_PLAN = "buildByPlan";

	public static final String SUB_TAB_BUILD_BY_ISSUE = "buildByIssue";

	public static final List<?> SUB_TABS = EasyList.build(SUB_TAB_BUILD_BY_PLAN, SUB_TAB_BUILD_BY_ISSUE);

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
	 * @param baseLinkUrl the base url for links to other tab panels
	 * @param subTabs available sub tabs
	 */
	public void prepareVelocityParameters(Map<String, Object> velocityParams, Project project, String baseLinkUrl,
					List<?> subTabs) {
		velocityParams.put("projectKey", project.getKey());
		velocityParams.put("hudsonServer", serverManager.getServerByJiraProject(project));
		prepareVelocityParameters(velocityParams, "projectKey=" + project.getKey(), "project", baseLinkUrl, subTabs);
	}

	/**
	 * Prepare the velocity parameters for the Tab Panel
	 * 
	 * @param velocityParams {@link Map} of existing velocity parameters to extend
	 * @param version the {@link Version} to prepare the velocity parameters for
	 * @param baseLinkUrl the base url for links to other tab panels
	 * @param subTabs available sub tabs
	 */
	public void prepareVelocityParameters(Map<String, Object> velocityParams, Version version, String baseLinkUrl,
					List<?> subTabs) {
		velocityParams.put("versionId", version.getId());
		velocityParams.put("hudsonServer", serverManager.getServerByJiraProject(version.getProjectObject()));
		prepareVelocityParameters(velocityParams, "versionId=" + version.getId(), "version", baseLinkUrl, subTabs);
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
		prepareVelocityParameters(velocityParams, "issueKey=" + issue.getKey(), "issue", null, null);
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
		prepareVelocityParameters(velocityParams, "componentId=" + component.getId(), "component", null, null);
	}

	/**
	 * Prepare velocity parameters for Hudson Builds Tab panels
	 * 
	 * @param velocityParams the existing {@link Map} of velocity parameters to extend
	 * @param querySection the query section for invoking the ViewHudsonServerPanelContent page
	 * @param moduleKey the module key of the panel
	 * @param baseLinkUrl the base url for links to other tab panels, may be <code>null</code> to not add it to the
	 *            velocity parameters
	 * @param subTabs available sub tabs
	 */
	private void prepareVelocityParameters(Map<String, Object> velocityParams, String querySection, String moduleKey,
					String baseLinkUrl, List<?> subTabs) {
		webResourceManager.requireResource(HUDSON_BUILD_PLUGIN + ":panel-css");
		velocityParams.put("querySection", querySection);
		velocityParams.put("moduleKey", moduleKey);
		velocityParams.put("baseResourceUrl", "/download/resources/" + HUDSON_BUILD_PLUGIN + ":hudson-" + moduleKey
			+ "-tabpanel");
		if (baseLinkUrl != null) {
			velocityParams.put("baseLinkUrl", baseLinkUrl + HUDSON_BUILD_PLUGIN + ":hudson-" + moduleKey
				+ "-tabpanel");
		}
		velocityParams.put("selectedSubTab", retrieveFromRequestOrSession("selectedSubTab", SUB_TAB_BUILD_BY_PLAN));
		velocityParams.put("availableTabs", subTabs);
	}

	/**
	 * Get a parameter from the current Request or current Session. If it is not set the default value will be
	 * returned.
	 * 
	 * @param requestKey the parameter key to get
	 * @param defaultValue the default value if the parameter is not set
	 * @return the value of the parameter, or the given default value
	 */
	public static String retrieveFromRequestOrSession(String requestKey, String defaultValue) {
		final String value = retrieveFromRequestOrSession(requestKey);
		return ((value != null) ? value : defaultValue);
	}

	/**
	 * Get a parameter value from the current Request or Session.
	 * 
	 * @param requestKey the parameter key to get
	 * @return the value of the parameter
	 */
	@SuppressWarnings("unchecked")
	public static String retrieveFromRequestOrSession(String requestKey) {
		final String sessionKey = HudsonBuildsTabPanelHelper.HUDSON_BUILD_PLUGIN + "." + requestKey;
		final String paramFromRequest = ParameterUtils.getStringParam(ActionContext.getParameters(), requestKey);
		final Map<String, String> session = ActionContext.getSession();
		if (StringUtils.isNotBlank(paramFromRequest)) {
			session.put(sessionKey, paramFromRequest);
			return paramFromRequest;
		}
		return ((String) session.get(sessionKey));
	}

}
