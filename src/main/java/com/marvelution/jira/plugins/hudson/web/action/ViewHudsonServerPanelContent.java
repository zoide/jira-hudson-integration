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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import webwork.action.ActionContext;

import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.project.Project;
import com.atlassian.jira.project.ProjectManager;
import com.atlassian.jira.project.version.Version;
import com.atlassian.jira.project.version.VersionManager;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.security.PermissionManager;
import com.atlassian.jira.security.Permissions;
import com.atlassian.jira.web.action.JiraWebActionSupport;
import com.atlassian.jira.web.bean.I18nBean;
import com.marvelution.jira.plugins.hudson.model.Build;
import com.marvelution.jira.plugins.hudson.service.HudsonServer;
import com.marvelution.jira.plugins.hudson.service.HudsonServerAccessor;
import com.marvelution.jira.plugins.hudson.service.HudsonServerAccessorException;
import com.marvelution.jira.plugins.hudson.service.HudsonServerManager;
import com.marvelution.jira.plugins.hudson.utils.DateTimeUtils;

import edu.emory.mathcs.backport.java.util.Collections;

/**
 * {@link JiraWebActionSupport} implementation for {@link HudsonServer} TabPanel actions
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class ViewHudsonServerPanelContent extends JiraWebActionSupport {

	private static final long serialVersionUID = 1L;

	private JiraAuthenticationContext authenticationContext;

	private PermissionManager permissionManager;

	private ProjectManager projectManager;

	private IssueManager issueManager;

	private VersionManager versionManager;

	private HudsonServerAccessor serverAccessor;

	private HudsonServerManager serverManager;

	private I18nBean i18n;

	private HudsonServer server;

	private String projectKey;

	private String issueKey;

	private Long versionId;

	private List<Build> builds;

	/**
	 * Constructor
	 * 
	 * @param authenticationContext the {@link JiraAuthenticationContext} implementation
	 * @param permissionManager the {@link PermissionManager} implementation
	 * @param projectManager the {@link ProjectManager} implementation
	 * @param issueManager the {@link IssueManager} implementation
	 * @param versionManager the {@link VersionManager} implementation
	 * @param serverAccessor the {@link HudsonServerAccessor} implementation
	 * @param serverManager the {@link HudsonServerManager} implementation
	 */
	public ViewHudsonServerPanelContent(JiraAuthenticationContext authenticationContext,
										PermissionManager permissionManager, ProjectManager projectManager,
										IssueManager issueManager, VersionManager versionManager,
										HudsonServerAccessor serverAccessor, HudsonServerManager serverManager) {
		this.authenticationContext = authenticationContext;
		this.permissionManager = permissionManager;
		this.projectManager = projectManager;
		this.issueManager = issueManager;
		this.versionManager = versionManager;
		this.serverAccessor = serverAccessor;
		this.serverManager = serverManager;
		i18n =
			authenticationContext
				.getI18nBean("com.marvelution.jira.plugins.hudson.web.action.ViewHudsonServerPanelContent");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String doExecute() throws Exception {
		ActionContext.getRequest().setAttribute("__sitemesh__decorator", "none");
		if (!serverManager.hasServers()) {
			addErrorMessage(getText("hudson.panel.error.not.configured"));
			return "error";
		}
		try {
			if (!StringUtils.isEmpty(issueKey)) {
				final MutableIssue issue = issueManager.getIssueObject(issueKey);
				if (issue != null
					&& permissionManager.hasPermission(Permissions.VIEW_VERSION_CONTROL, issue, authenticationContext
						.getUser())) {
					server = serverManager.getServerByJiraProject(issue.getProjectObject());
					log.debug("Request for project " + projectKey + " builds from server " + server.getName());
					if (server != null) {
						final List<String> issueKeys = new ArrayList<String>();
						issueKeys.add(issue.getKey());
						builds = serverAccessor.getBuilds(server, issueKeys);
						Collections.sort(builds);
						Collections.reverse(builds);
					}
				}
			} else if (versionId != null && versionId > 0L) {
				final Version version = versionManager.getVersion(versionId);
				if (version != null
					&& permissionManager.hasPermission(Permissions.VIEW_VERSION_CONTROL, version.getProjectObject(),
						authenticationContext.getUser())) {
					server = serverManager.getServerByJiraProject(version.getProjectObject());
					log.debug("Request for project version " + versionId + " builds from server " + server.getName());
					if (server != null) {
						builds = serverAccessor.getBuilds(server, version);
						Collections.sort(builds);
						Collections.reverse(builds);
					}
				}
			} else if (!StringUtils.isEmpty(projectKey)) {
				final Project project = projectManager.getProjectObjByKey(projectKey);
				if (project != null
					&& permissionManager.hasPermission(Permissions.VIEW_VERSION_CONTROL, project, authenticationContext
						.getUser())) {
					server = serverManager.getServerByJiraProject(project);
					log.debug("Request for project " + projectKey + " builds from server " + server.getName());
					if (server != null) {
						builds = serverAccessor.getBuilds(server, project);
						Collections.sort(builds);
						Collections.reverse(builds);
					}
				}
			}
		} catch (HudsonServerAccessorException e) {
			log.warn("Failed to connect to Hudson Server. Reason: " + e.getMessage(), e);
			addErrorMessage(getText("hudson.panel.error.cannot.connect"));
			return "error";
		}
		return super.doExecute();
	}

	/**
	 * Gets the project key
	 * 
	 * @return the project key
	 */
	public String getProjectKey() {
		return projectKey;
	}

	/**
	 * Sets the project key
	 * 
	 * @param projectKey the project key
	 */
	public void setProjectKey(String projectKey) {
		this.projectKey = projectKey;
	}

	/**
	 * Gets the issue key
	 * 
	 * @return the issue key
	 */
	public String getIssueKey() {
		return issueKey;
	}

	/**
	 * Sets the issue key
	 * 
	 * @param issueKey the issue key
	 */
	public void setIssueKey(String issueKey) {
		this.issueKey = issueKey;
	}

	/**
	 * Gets the version id
	 * 
	 * @return the version id
	 */
	public Long getVersionId() {
		return versionId;
	}

	/**
	 * Sets the version id
	 * 
	 * @param versionId the version id
	 */
	public void setVersionId(Long versionId) {
		this.versionId = versionId;
	}

	/**
	 * Gets the {@link HudsonServer}
	 * 
	 * @return the {@link HudsonServer}
	 */
	public HudsonServer getServer() {
		return server;
	}

	/**
	 * Sets the {@link HudsonServer}
	 * 
	 * @param server the {@link HudsonServer}
	 */
	public void setServer(HudsonServer server) {
		this.server = server;
	}

	/**
	 * Gets the builds
	 * 
	 * @return the {@link List} of {@link Build} objects
	 */
	public List<Build> getBuilds() {
		return builds;
	}

	/**
	 * Gets the builds
	 * 
	 * @param builds the {@link List} of {@link Build} objects
	 */
	public void setBuilds(List<Build> builds) {
		this.builds = builds;
	}

	/**
	 * Get if any builds are found
	 * 
	 * @return <code>true</code> if any builds are found, <code>false</code> otherwise
	 */
	public boolean getFoundBuilds() {
		if (builds != null && !builds.isEmpty()) {
			return true;
		}
		return false;
	}

	/**
	 * Gets the {@link DateTimeUtils} helper class
	 * 
	 * @return the {@link DateTimeUtils} helper object
	 */
	public DateTimeUtils getDateTimeUtils() {
		return new DateTimeUtils(authenticationContext);
	}

	/**
	 * Gets the internationalisation bean
	 * 
	 * @return the {@link I18nBean}
	 */
	public I18nBean getI18n() {
		return i18n;
	}

	/**
	 * Gets Internationalisation text for a key
	 * 
	 * @param key the key to get the text for
	 * @return the text
	 */
	public String getText(String key) {
		return i18n.getText(key);
	}

}
