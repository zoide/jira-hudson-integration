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

package com.marvelution.jira.plugins.hudson.web.action;

import com.atlassian.jira.ComponentManager;
import com.atlassian.jira.ManagerFactory;
import com.atlassian.jira.bc.project.component.ProjectComponentManager;
import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.issue.search.SearchProvider;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.security.roles.ProjectRoleManager;
import com.atlassian.jira.util.I18nHelper;
import com.atlassian.jira.web.action.JiraWebActionSupport;

/**
 * Base {@link JiraWebActionSupport} implementation for the Hudson plugin
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class HudsonWebActionSupport extends JiraWebActionSupport {

	private static final long serialVersionUID = 1L;

	private ProjectComponentManager projectComponentManager;
	private IssueManager issueManager;
	private ProjectRoleManager projectRoleManager;
	private SearchProvider searchProvider;

	/**
	 * Getter for the {@link JiraAuthenticationContext}
	 * 
	 * @return the {@link JiraAuthenticationContext}
	 */
	protected JiraAuthenticationContext getAuthenticationContext() {
		return ComponentManager.getInstance().getJiraAuthenticationContext();
	}

	/**
	 * Getter for the {@link IssueManager}
	 * 
	 * @return the {@link IssueManager}
	 */
	protected IssueManager getIssueManager() {
		if (issueManager == null) {
			issueManager = ManagerFactory.getIssueManager();
		}
		return issueManager;
	}

	/**
	 * Getter for the {@link ProjectComponentManager}
	 * 
	 * @return the {@link ProjectComponentManager}
	 */
	protected ProjectComponentManager getProjectComponentManager() {
		if (projectComponentManager == null) {
			projectComponentManager = ComponentManager.getInstance().getProjectComponentManager();
		}
		return projectComponentManager;
	}

	/**
	 * Getter for the {@link ProjectRoleManager}
	 * 
	 * @return the {@link ProjectRoleManager}
	 */
	protected ProjectRoleManager getProjectRoleManager() {
		if (projectRoleManager == null) {
			projectRoleManager = ComponentManager.getComponent(ProjectRoleManager.class);
		}
		return projectRoleManager;
	}

	/**
	 * Getter for the {@link SearchProvider}
	 * 
	 * @return the {@link SearchProvider}
	 */
	protected SearchProvider getSearchProvider() {
		if (searchProvider == null) {
			searchProvider = ComponentManager.getInstance().getSearchProvider();
		}
		return searchProvider;
	}

	/**
	 * Getter for the {@link I18nHelper}
	 * 
	 * @return the {@link I18nHelper}
	 */
	protected I18nHelper getI18nHelper() {
		return ComponentManager.getInstance().getJiraAuthenticationContext().getI18nHelper();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getText0(String i18nKey) {
		return getI18nHelper().getText(i18nKey);
	}

}
