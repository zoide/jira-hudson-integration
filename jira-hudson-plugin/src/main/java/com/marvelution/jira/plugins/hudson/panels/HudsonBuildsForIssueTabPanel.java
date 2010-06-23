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

import com.atlassian.core.util.collection.EasyList;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.plugin.issuetabpanel.AbstractIssueTabPanel;
import com.atlassian.jira.security.PermissionManager;
import com.atlassian.jira.security.Permissions;
import com.marvelution.jira.plugins.hudson.service.HudsonConfigurationManager;
import com.marvelution.jira.plugins.hudson.service.HudsonServerManager;
import com.opensymphony.user.User;

/**
 * {@link IssueTabPanel} to show Hudson Builds for Project Issues
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class HudsonBuildsForIssueTabPanel extends AbstractIssueTabPanel {

	private final PermissionManager permissionManager;

	private final HudsonServerManager serverManager;

	private final HudsonBuildsTabPanelHelper tabPanelHelper;

	private final HudsonConfigurationManager configurationManager;

	/**
	 * Constructor
	 * 
	 * @param permissionManager the {@link PermissionManager} implementation
	 * @param serverManager the {@link HudsonServerManager} implementation
	 * @param tabPanelHelper the {@link HudsonBuildsTabPanelHelper} implementation
	 * @param configurationManager the {@link HudsonConfigurationManager} implementation
	 */
	public HudsonBuildsForIssueTabPanel(PermissionManager permissionManager, HudsonServerManager serverManager,
										HudsonBuildsTabPanelHelper tabPanelHelper,
										HudsonConfigurationManager configurationManager) {
		this.permissionManager = permissionManager;
		this.serverManager = serverManager;
		this.tabPanelHelper = tabPanelHelper;
		this.configurationManager = configurationManager;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<?> getActions(Issue issue, User user) {
		return EasyList.build(new HudsonBuildsForIssueTabPanelAction(issue, descriptor, tabPanelHelper));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean showPanel(Issue issue, User user) {
		if (configurationManager.getBooleanProperty(HudsonConfigurationManager.HIDE_UNASSOCIATED_HUDSON_TAB)) {
			return (serverManager.isHudsonConfigured()
				&& serverManager.hasServerAssociation(issue.getProjectObject()) && permissionManager.hasPermission(
				Permissions.VIEW_VERSION_CONTROL, issue, user));
		} else {
			return (serverManager.isHudsonConfigured() && permissionManager.hasPermission(
				Permissions.VIEW_VERSION_CONTROL, issue, user));
		}
	}

}
