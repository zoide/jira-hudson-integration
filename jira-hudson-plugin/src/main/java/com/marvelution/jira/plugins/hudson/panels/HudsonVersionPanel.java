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

import com.atlassian.jira.issue.search.SearchProvider;
import com.atlassian.jira.plugin.versionpanel.BrowseVersionContext;
import com.atlassian.jira.plugin.versionpanel.impl.GenericTabPanel;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.marvelution.jira.plugins.hudson.panels.utils.HudsonPanelHelper;

/**
 * {@link GenericTabPanel} implementation for Hudson Builds
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class HudsonVersionPanel extends GenericTabPanel {

	private final HudsonPanelHelper panelHelper;

	/**
	 * Constructor
	 * 
	 * @param authenticationContext the {@link JiraAuthenticationContext} implementation
	 * @param searchProvider the {@link SearchProvider} implementation
	 * @param panelHelper the {@link HudsonPanelHelper} helper class
	 */
	public HudsonVersionPanel(JiraAuthenticationContext authenticationContext, SearchProvider searchProvider,
			HudsonPanelHelper panelHelper) {
		super(authenticationContext, searchProvider);
		this.panelHelper = panelHelper;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Map<String, Object> createVelocityParams(BrowseVersionContext context) {
		return panelHelper.createVelocityParams(context, super.createVelocityParams(context));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean showPanel(BrowseVersionContext context) {
		return panelHelper.showPanel(context.getProject(), context.getUser());
	}

}
