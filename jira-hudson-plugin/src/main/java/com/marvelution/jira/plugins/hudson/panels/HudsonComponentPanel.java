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

import com.atlassian.jira.plugin.componentpanel.BrowseComponentContext;
import com.atlassian.jira.plugin.componentpanel.impl.GenericTabPanel;
import com.atlassian.jira.project.ProjectManager;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.marvelution.jira.plugins.hudson.panels.utils.HudsonPanelHelper;

/**
 * {@link GenericTabPanel} implementation for Hudson Builds
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class HudsonComponentPanel extends GenericTabPanel {

	private final HudsonPanelHelper panelHelper;

	/**
	 * Constructor
	 * 
	 * @param projectManager the {@link ProjectManager} implementation
	 * @param authenticationContext the {@link JiraAuthenticationContext} implementation
	 * @param panelHelper the {@link HudsonPanelHelper} helper class
	 */
	public HudsonComponentPanel(ProjectManager projectManager, JiraAuthenticationContext authenticationContext,
			HudsonPanelHelper panelHelper) {
		super(projectManager, authenticationContext);
		this.panelHelper = panelHelper;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Map<String, Object> createVelocityParams(BrowseComponentContext context) {
		return panelHelper.createVelocityParams(context, super.createVelocityParams(context));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean showPanel(BrowseComponentContext context) {
		return panelHelper.showPanel(context.getProject(), context.getUser());
	}

}
