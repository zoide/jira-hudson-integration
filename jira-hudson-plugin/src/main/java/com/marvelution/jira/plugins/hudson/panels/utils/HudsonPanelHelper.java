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

package com.marvelution.jira.plugins.hudson.panels.utils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.atlassian.crowd.embedded.api.CrowdService;
import com.atlassian.crowd.embedded.api.User;
import com.atlassian.jira.ComponentManager;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.issuetype.IssueType;
import com.atlassian.jira.plugin.componentpanel.BrowseComponentContext;
import com.atlassian.jira.plugin.versionpanel.BrowseVersionContext;
import com.atlassian.jira.project.Project;
import com.atlassian.jira.project.browse.BrowseContext;
import com.atlassian.jira.security.PermissionManager;
import com.atlassian.jira.security.Permissions;
import com.atlassian.jira.security.roles.ProjectRoleManager;
import com.atlassian.plugin.webresource.WebResourceManager;
import com.marvelution.jira.plugins.hudson.panels.context.BrowseIssueContext;
import com.marvelution.jira.plugins.hudson.services.associations.HudsonAssociationManager;
import com.marvelution.jira.plugins.hudson.services.configuration.HudsonConfigurationManager;
import com.marvelution.jira.plugins.hudson.services.servers.HudsonServerManager;
import com.marvelution.jira.plugins.hudson.utils.JiraPluginUtils;
import com.marvelution.jira.plugins.hudson.utils.RequestAndSessionUtils;
import com.marvelution.jira.plugins.hudson.utils.UserUtils;

/**
 * Utility class for Hudson Panels
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class HudsonPanelHelper {

	private static final List<?> VIEWS = Arrays.asList(PanelView.values());

	public static final String SELECTED_ASSOCIATION = "associationId";
	public static final String SELECTED_VIEW = "selectedView";

	private final PermissionManager permissionManager;
	private final ProjectRoleManager roleManager;
	private final WebResourceManager webResourceManager;
	private final HudsonConfigurationManager configurationManager;
	private final HudsonAssociationManager associationManager;
	private final HudsonServerManager serverManager;

	/**
	 * Constructor
	 * 
	 * @param permissionManager the {@link PermissionManager} implementation
	 * @param roleManager the {@link ProjectRoleManager} implementation
	 * @param webResourceManager the {@link WebResourceManager} implementation
	 * @param configurationManager the {@link HudsonConfigurationManager} implementation
	 * @param associationManager the {@link HudsonAssociationManager} implementation
	 * @param serverManager the {@link HudsonServerManager} implementation
	 */
	public HudsonPanelHelper(PermissionManager permissionManager, ProjectRoleManager roleManager,
			WebResourceManager webResourceManager, HudsonConfigurationManager configurationManager,
			HudsonAssociationManager associationManager, HudsonServerManager serverManager) {
		this.permissionManager = permissionManager;
		this.roleManager = roleManager;
		this.webResourceManager = webResourceManager;
		this.configurationManager = configurationManager;
		this.associationManager = associationManager;
		this.serverManager = serverManager;
	}

	/**
	 * Method to populate the Velocity parameters for the Project Panel
	 * 
	 * @param context the Project Panel {@link BrowseContext}
	 * @param velocityParams the base parameters {@link Map}
	 * @return the Velocity parameters {@link Map}
	 */
	public Map<String, Object> createVelocityParams(BrowseContext context, Map<String, Object> velocityParams) {
		populateCommonVelocityParams(context, velocityParams, PanelModule.PROJECT);
		velocityParams.put("objectId", context.getProject().getId());
		return velocityParams;
	}

	/**
	 * Method to populate the Velocity parameters for the Project Version Panel
	 * 
	 * @param context the Project Version Panel {@link BrowseVersionContext}
	 * @param velocityParams the base parameters {@link Map}
	 * @return the Velocity parameters {@link Map}
	 */
	public Map<String, Object> createVelocityParams(BrowseVersionContext context, Map<String, Object> velocityParams) {
		populateCommonVelocityParams(context, velocityParams, PanelModule.VERSION);
		velocityParams.put("objectId", context.getVersion().getId());
		if (context.getVersion().isReleased()) {
			velocityParams.put("extraDescriptionKey", "released.");
		}
		return velocityParams;
	}

	/**
	 * Method to populate the Velocity parameters for the Project Component Panel
	 * 
	 * @param context the Project Component Panel {@link BrowseComponentContext}
	 * @param velocityParams the base parameters {@link Map}
	 * @return the Velocity parameters {@link Map}
	 */
	public Map<String, Object> createVelocityParams(BrowseComponentContext context,
			Map<String, Object> velocityParams) {
		populateCommonVelocityParams(context, velocityParams, PanelModule.COMPONENT);
		velocityParams.put("objectId", context.getComponent().getId());
		return velocityParams;
	}

	/**
	 * Method to populate the Velocity parameters for the Project Issue Panel
	 * 
	 * @param context the Project Issue Panel {@link BrowseIssueContext}
	 * @param velocityParams the base Velocity parameters
	 */
	public Map<String, Object> createVelocityParams(BrowseIssueContext context, Map<String, Object> velocityParams) {
		populateCommonVelocityParams(context, velocityParams, PanelModule.ISSUE);
		velocityParams.put("objectId", context.getIssue().getId());
		return velocityParams;
	}

	/**
	 * Internal method to populate all the common Velocity Parameters
	 * 
	 * @param <CONTEXT> the context type
	 * @param context the Common context
	 * @param velocityParams the base Velocity Parameters {@link Map}
	 * @param module the {@link PanelModule} to populate the parameters for
	 */
	private <CONTEXT extends BrowseContext> void populateCommonVelocityParams(CONTEXT context,
			Map<String, Object> velocityParams, PanelModule module) {
		webResourceManager.requireResource(JiraPluginUtils.getPluginKey() + ":hudson-panel");
		velocityParams.put("serverManager", serverManager);
		velocityParams.put("associationManager", associationManager);
		if (associationManager.hasAssociations(context.getProject())) {
			velocityParams.put("associations", associationManager.getAssociations(context.getProject()));
		}
		velocityParams.put(SELECTED_ASSOCIATION, RequestAndSessionUtils.retrieveFromRequestOrSession(
			SELECTED_ASSOCIATION, "0"));
		velocityParams.put("project", context.getProject());
		velocityParams.put("pluginKey", JiraPluginUtils.getPluginKey());
		velocityParams.put("module", module);
		velocityParams.put("resourceBaseUrl", "/download/resources/" + module.getFullKey());
		velocityParams.put("user", context.getUser());
		velocityParams.put("isProjectAdmin", permissionManager.hasPermission(Permissions.PROJECT_ADMIN,
				context.getProject(), getUserFromContext(context)));
		velocityParams.put("isSystemAdmin", permissionManager.hasPermission(Permissions.SYSTEM_ADMIN,
			getUserFromContext(context)));
		if (module.supportsTabs()) {
			velocityParams.put("availableViews", VIEWS);
		}
		velocityParams.put(SELECTED_VIEW, RequestAndSessionUtils.retrieveFromRequestOrSession(SELECTED_VIEW,
			PanelView.BUILDS_BY_JOB.getViewName()));
	}

	/**
	 * Helper method to get the {@link User} from the given Context
	 * 
	 * @param <CONTEXT> the Context type
	 * @param context the Context
	 * @return the {@link User}
	 */
	private <CONTEXT extends BrowseContext> User getUserFromContext(CONTEXT context) {
		if (context.getUser() instanceof User) {
			return (User) context.getUser();
		} else if (context.getUser() != null) {
			// TODO This should be removed when no longer needed
			CrowdService crowdService = ComponentManager.getInstance().getCrowdService();
			return crowdService.getUser(context.getUser().getName());
		} else {
			return null;
		}
	}

	/**
	 * Method to check if the panel should be shown in the panel list or not
	 * 
	 * @param project the {@link Project} of the current browse context
	 * @param user the current logged in {@link User}
	 * @return <code>true</code> if the panel link should be shown, <code>false</code> otherwise
	 */
	public boolean showPanel(Project project, User user) {
		if (configurationManager.isHideUnassociatedHudsonTabs()) {
			if (!associationManager.hasAssociations(project)) {
				return false;
			}
		}
		if (!configurationManager.getShowIfUserMemberOfUsergroup().isEmpty() && !UserUtils
				.isUserMemberOfAtleastOneGroup(user, configurationManager.getShowIfUserMemberOfUsergroup())) {
			if (!configurationManager.getShowIfUserMemberOfProjectRole().isEmpty() && !UserUtils
						.isUserMemberOfAtleastOneProjectRole(user, project, configurationManager
								.getShowIfUserMemberOfProjectRole(), roleManager)) {
				return false;
			}
		}
		return permissionManager.hasPermission(Permissions.VIEW_VERSION_CONTROL, project, user);
	}

	/**
	 * Method to check if the panel should be shown in the panel list or not
	 * 
	 * @param issue the {@link Issue} of the current browse context
	 * @param user the current logged in {@link User}
	 * @return <code>true</code> if the panel link should be shown, <code>false</code> otherwise
	 */
	public boolean showPanel(Issue issue, User user) {
		if (showPanel(issue.getProjectObject(), user)) {
			IssueType issueType = issue.getIssueTypeObject();
			if (!configurationManager.getShowIfIssueOfIssueType().isEmpty()
					&& !configurationManager.getShowIfIssueOfIssueType().contains(issueType.getId())) {
				return false;
			}
			return permissionManager.hasPermission(Permissions.VIEW_VERSION_CONTROL, issue, user);
		} else {
			return false;
		}
	}

}
