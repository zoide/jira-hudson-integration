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
import java.util.HashMap;

import org.ofbiz.core.entity.GenericValue;

import com.atlassian.jira.plugin.projectpanel.impl.AbstractProjectTabPanel;
import com.atlassian.jira.project.Project;
import com.atlassian.jira.security.PermissionManager;
import com.atlassian.jira.security.Permissions;
import com.atlassian.jira.web.action.ProjectActionSupport;
import com.atlassian.jira.web.action.browser.Browser;
import com.marvelution.jira.plugins.hudson.service.HudsonServerManager;

/**
 * {@link ProjectTabPanel} to show Hudson Builds for a Project
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class HudsonBuildsForProjectTabPanel extends AbstractProjectTabPanel {

	private final PermissionManager permissionManager;

	private final HudsonServerManager serverManager;

	private final HudsonBuildsTabPanelHelper tabPanelHelper;

	/**
	 * Constructor
	 * 
	 * @param permissionManager the {@link PermissionManager} implementation
	 * @param serverManager the {@link HudsonServerManager} implementation
	 * @param tabPanelHelper the {@link HudsonBuildsTabPanelHelper} class
	 */
	public HudsonBuildsForProjectTabPanel(PermissionManager permissionManager, HudsonServerManager serverManager,
											HudsonBuildsTabPanelHelper tabPanelHelper) {
		this.permissionManager = permissionManager;
		this.serverManager = serverManager;
		this.tabPanelHelper = tabPanelHelper;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getHtml(Browser browser) {
		final Project project = browser.getProjectObject();
		final Map<String, Object> velocityParams = new HashMap<String, Object>();
		velocityParams.put("showRss", Boolean.TRUE);
		tabPanelHelper.prepareVelocityParameters(velocityParams, project, "/browse/" + project.getKey() + "?report=",
			HudsonBuildsTabPanelHelper.SUB_TABS);
		return descriptor.getHtml("view", velocityParams);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("deprecation")
	@Override
	public boolean showPanel(ProjectActionSupport action, GenericValue project) {
		return (serverManager.isHudsonConfigured() && permissionManager.hasPermission(
			Permissions.VIEW_VERSION_CONTROL, project, action.getRemoteUser()));
	}

}
