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
import com.atlassian.jira.plugin.componentpanel.ComponentContext;
import com.atlassian.jira.plugin.componentpanel.impl.GenericTabPanel;
import com.atlassian.jira.project.ProjectManager;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.security.PermissionManager;
import com.atlassian.jira.security.Permissions;
import com.marvelution.jira.plugins.hudson.service.HudsonServerManager;

/**
 * {@link GenericTabPanel} to show Hudson Builds for a Component
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class HudsonBuildsForComponentTabPanel extends GenericTabPanel {

	private final PermissionManager permissionManager;

	private final HudsonServerManager serverManager;

	private final HudsonBuildsTabPanelHelper tabPanelHelper;

	/**
	 * Constructor
	 * 
	 * @param projectManager the {@link ProjectManager} implementation
	 * @param authenticationContext the {@link JiraAuthenticationContext}
	 * @param permissionManager the {@link PermissionManager} implementation
	 * @param serverManager the {@link HudsonServerManager} implementation
	 * @param tabPanelHelper the {@link HudsonBuildsTabPanelHelper} class
	 */
	public HudsonBuildsForComponentTabPanel(ProjectManager projectManager,
											JiraAuthenticationContext authenticationContext,
											PermissionManager permissionManager, HudsonServerManager serverManager,
											HudsonBuildsTabPanelHelper tabPanelHelper) {
		super(projectManager, authenticationContext);
		this.permissionManager = permissionManager;
		this.serverManager = serverManager;
		this.tabPanelHelper = tabPanelHelper;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected Map createVelocityParams(ComponentContext context) {
		final ProjectComponent component = context.getComponent();
		final Map<String, Object> velocityParams = super.createVelocityParams(context);
		tabPanelHelper.prepareVelocityParameters(velocityParams, component);
		return velocityParams;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean showPanel(ComponentContext context) {
		return (serverManager.isHudsonConfigured() && permissionManager.hasPermission(
			Permissions.VIEW_VERSION_CONTROL, projectManager.getProjectObj(context.getComponent().getProjectId()),
			authenticationContext.getUser()));
	}

}
