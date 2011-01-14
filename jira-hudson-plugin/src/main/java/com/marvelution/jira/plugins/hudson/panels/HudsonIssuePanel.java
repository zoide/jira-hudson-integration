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
import com.marvelution.jira.plugins.hudson.panels.utils.HudsonPanelHelper;
import com.opensymphony.user.User;

/**
 * {@link AbstractIssueTabPanel} implementation for Hudson Builds
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class HudsonIssuePanel extends AbstractIssueTabPanel {

	private final HudsonPanelHelper panelHelper;

	/**
	 * Constructor
	 * 
	 * @param panelHelper the {@link HudsonPanelHelper} helper class
	 */
	public HudsonIssuePanel(HudsonPanelHelper panelHelper) {
		this.panelHelper = panelHelper;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public List getActions(Issue issue, User user) {
		return EasyList.build(new HudsonIssuePanelAction(descriptor, panelHelper, issue, user));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean showPanel(Issue issue, User user) {
		return panelHelper.showPanel(issue, user);
	}

}
