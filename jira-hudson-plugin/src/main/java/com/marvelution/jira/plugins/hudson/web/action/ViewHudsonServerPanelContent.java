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
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang.StringUtils;

import webwork.action.ActionContext;

import com.atlassian.jira.bc.project.component.ProjectComponent;
import com.atlassian.jira.bc.project.component.ProjectComponentManager;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.issue.search.SearchException;
import com.atlassian.jira.issue.search.SearchProvider;
import com.atlassian.jira.issue.search.SearchResults;
import com.atlassian.jira.jql.builder.JqlQueryBuilder;
import com.atlassian.jira.project.Project;
import com.atlassian.jira.project.ProjectManager;
import com.atlassian.jira.project.version.Version;
import com.atlassian.jira.project.version.VersionManager;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.security.PermissionManager;
import com.atlassian.jira.security.Permissions;
import com.atlassian.jira.user.util.UserUtil;
import com.atlassian.jira.util.I18nHelper;
import com.atlassian.jira.web.action.JiraWebActionSupport;
import com.atlassian.jira.web.bean.PagerFilter;
import com.marvelution.jira.plugins.hudson.api.model.Build;
import com.marvelution.jira.plugins.hudson.api.model.Job;
import com.marvelution.jira.plugins.hudson.model.HudsonBuildTabPanelResult;
import com.marvelution.jira.plugins.hudson.panels.HudsonBuildsTabPanelHelper;
import com.marvelution.jira.plugins.hudson.service.HudsonConfigurationManager;
import com.marvelution.jira.plugins.hudson.service.HudsonServer;
import com.marvelution.jira.plugins.hudson.service.HudsonServerAccessDeniedException;
import com.marvelution.jira.plugins.hudson.service.HudsonServerAccessor;
import com.marvelution.jira.plugins.hudson.service.HudsonServerAccessorException;
import com.marvelution.jira.plugins.hudson.service.HudsonServerManager;
import com.marvelution.jira.plugins.hudson.utils.BuildUtils;
import com.marvelution.jira.plugins.hudson.utils.DateTimeUtils;
import com.marvelution.jira.plugins.hudson.utils.HudsonBuildTriggerParser;

/**
 * {@link JiraWebActionSupport} implementation for {@link HudsonServer} TabPanel actions
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class ViewHudsonServerPanelContent extends JiraWebActionSupport {

	private static final long serialVersionUID = 1L;

	private final JiraAuthenticationContext authenticationContext;

	private final PermissionManager permissionManager;

	private final ProjectManager projectManager;

	private final ProjectComponentManager componentManager;

	private final IssueManager issueManager;

	private final VersionManager versionManager;

	private final HudsonServerAccessor serverAccessor;

	private final HudsonServerManager serverManager;

	private final HudsonConfigurationManager configurationManager;

	private final SearchProvider searchProvider;

	private final I18nHelper i18n;

	private final UserUtil userUtil;

	private Project project;

	private String projectKey;

	private Long componentId;

	private Long versionId;

	private String issueKey;

	private HudsonBuildTabPanelResult results;

	/**
	 * Constructor
	 * 
	 * @param authenticationContext the {@link JiraAuthenticationContext} implementation
	 * @param permissionManager the {@link PermissionManager} implementation
	 * @param projectManager the {@link ProjectManager} implementation
	 * @param componentManager the {@link ProjectComponentManager} implementation
	 * @param issueManager the {@link IssueManager} implementation
	 * @param versionManager the {@link VersionManager} implementation
	 * @param userUtil the {@link UserUtil} implementation
	 * @param serverAccessor the {@link HudsonServerAccessor} implementation
	 * @param configurationManager the {@link HudsonConfigurationManager} implementation
	 * @param serverManager the {@link HudsonServerManager} implementation
	 * @param searchProvider the {@link SearchProvider} implementation
	 */
	public ViewHudsonServerPanelContent(JiraAuthenticationContext authenticationContext,
										PermissionManager permissionManager, ProjectManager projectManager,
										ProjectComponentManager componentManager, IssueManager issueManager,
										VersionManager versionManager, UserUtil userUtil,
										HudsonServerAccessor serverAccessor,
										HudsonConfigurationManager configurationManager,
										HudsonServerManager serverManager, SearchProvider searchProvider) {
		this.authenticationContext = authenticationContext;
		this.permissionManager = permissionManager;
		this.projectManager = projectManager;
		this.componentManager = componentManager;
		this.issueManager = issueManager;
		this.versionManager = versionManager;
		this.userUtil = userUtil;
		this.serverAccessor = serverAccessor;
		this.serverManager = serverManager;
		this.searchProvider = searchProvider;
		this.configurationManager = configurationManager;
		i18n = authenticationContext.getI18nHelper();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String doExecute() throws Exception {
		ActionContext.getRequest().setAttribute("__sitemesh__decorator", "none");
		if (!serverManager.hasServers()) {
			addErrorMessage(getText("hudson.panel.error.not.configured"));
			return ERROR;
		}
		HudsonServer server = serverManager.getDefaultServer();
		results = new HudsonBuildTabPanelResult(server);
		final String selectedSubTab =
			HudsonBuildsTabPanelHelper.retrieveFromRequestOrSession(HudsonBuildsTabPanelHelper.SELECTED_SUB_TAB_KEY,
				HudsonBuildsTabPanelHelper.SUB_TAB_BUILD_BY_PLAN);
		try {
			List<Build> builds = new ArrayList<Build>();
			if (!StringUtils.isEmpty(getProjectKey())) {
				project = projectManager.getProjectObjByKey(getProjectKey());
				if (project != null
					&& permissionManager.hasPermission(Permissions.VIEW_VERSION_CONTROL, project, authenticationContext
						.getUser())) {
					server = serverManager.getServerByJiraProject(project);
					if (HudsonBuildsTabPanelHelper.SUB_TAB_BUILD_BY_ISSUE.equals(selectedSubTab)) {
						builds = serverAccessor.getBuilds(server, getIssueKeys(project));
					} else {
						builds = serverAccessor.getBuilds(server, project);
					}
				} else {
					addErrorMessage(getText("hudson.panel.error.no.permission"));
					return ERROR;
				}
			} else if (getVersionId() != null && getVersionId() > 0L) {
				final Version version = versionManager.getVersion(getVersionId());
				if (version != null
					&& permissionManager.hasPermission(Permissions.VIEW_VERSION_CONTROL, version.getProjectObject(),
						authenticationContext.getUser())) {
					project = version.getProjectObject();
					server = serverManager.getServerByJiraProject(version.getProjectObject());
					if (HudsonBuildsTabPanelHelper.SUB_TAB_BUILD_BY_ISSUE.equals(selectedSubTab)) {
						builds = serverAccessor.getBuilds(server, getIssueKeys(version));
					} else {
						builds = serverAccessor.getBuilds(server, version);
					}
				} else {
					addErrorMessage(getText("hudson.panel.error.no.permission"));
					return ERROR;
				}
			} else if (getComponentId() != null && getComponentId() > 0L) {
				final ProjectComponent component = componentManager.find(getComponentId());
				project = projectManager.getProjectObj(component.getProjectId());
				if (component != null && project != null
					&& permissionManager.hasPermission(Permissions.VIEW_VERSION_CONTROL, project, authenticationContext
						.getUser())) {
					server = serverManager.getServerByJiraProject(project);
					builds = serverAccessor.getBuilds(server, getIssueKeys(component));
				} else {
					addErrorMessage(getText("hudson.panel.error.no.permission"));
					return ERROR;
				}
			} else if (!StringUtils.isEmpty(getIssueKey())) {
				final MutableIssue issue = issueManager.getIssueObject(getIssueKey());
				if (issue != null
					&& permissionManager.hasPermission(Permissions.VIEW_VERSION_CONTROL, issue, authenticationContext
						.getUser())) {
					project = issue.getProjectObject();
					server = serverManager.getServerByJiraProject(issue.getProjectObject());
					builds = serverAccessor.getBuilds(server, Collections.singletonList(issue.getKey()));
				} else {
					addErrorMessage(getText("hudson.panel.error.no.permission"));
					return ERROR;
				}
			} else {
				addErrorMessage(getText("hudson.panel.error.invalid.request"));
				return ERROR;
			}
			results.setServer(server);
			processBuilds(builds);
			results.setBuilds(builds);
		} catch (HudsonServerAccessorException e) {
			log.warn("Failed to connect to Hudson Server. Reason: " + e.getMessage(), e);
			addErrorMessage(getText("hudson.panel.error.cannot.connect", server.getName()));
			return ERROR;
		} catch (HudsonServerAccessDeniedException e) {
			log.warn("Failed to connect to Hudson Server. Reason: " + e.getMessage(), e);
			addErrorMessage(getText("hudson.panel.error.access.denied", server.getName()));
			return ERROR;
		}
		return super.doExecute();
	}

	/**
	 * Get the redirect to the RSS feed of the project
	 * 
	 * @return the RSS feed link
	 * @throws Exception in case the request is unsupported
	 */
	public String doShowRss() throws Exception {
		project = null;
		if (StringUtils.isNotEmpty(getProjectKey())) {
			project = projectManager.getProjectObjByKey(getProjectKey());
		} else if (getVersionId() != null && getVersionId() > 0L) {
			project = versionManager.getVersion(getVersionId()).getProjectObject();
		} else if (getComponentId() != null && getComponentId() > 0L) {
			final ProjectComponent component = componentManager.find(getComponentId());
			project = projectManager.getProjectObj(component.getProjectId());
		}
		if (project != null) {
			final HudsonServer server = serverManager.getServerByJiraProject(project);
			final Job job = serverAccessor.getProject(server, project);
			return getRedirect(server.getHost() + "/" + job.getUrl() + "rssAll");
		} else {
			throw new UnsupportedOperationException(getText("hudson.panel.error.unsupport.rss.action"));
		}
	}

	/**
	 * Get the issue keys that relate to the given {@link Project}
	 * 
	 * @param projectObj the {@link Project} to get the related issues for
	 * @return {@link Collection} of issue keys
	 */
	private Collection<String> getIssueKeys(final Project projectObj) {
		try {
			final JqlQueryBuilder queryBuilder =
				JqlQueryBuilder.newBuilder().where().project(new Long[] {projectObj.getId()}).endWhere();
			return getIssueKeys(queryBuilder, projectObj.getId());
		} catch (SearchException e) {
			log.warn(
				"Unable to get all issues from project " + projectObj.getName() + ". Reason: " + e.getMessage(), e);
		}
		return null;
	}

	/**
	 * Get the issue keys that relate to the given {@link Version}
	 * 
	 * @param version the {@link Version} to get the related issues for
	 * @return {@link Collection} of issue keys
	 */
	private Collection<String> getIssueKeys(final Version version) {
		try {
			final JqlQueryBuilder queryBuilder =
				JqlQueryBuilder.newBuilder().where().project(new Long[] {version.getProjectObject().getId()}).and()
					.fixVersion(version.getId()).endWhere();
			return getIssueKeys(queryBuilder, version.getProjectObject().getId());
		} catch (SearchException e) {
			log.warn(
				"Unable to get all issues from version " + version.getName() + ". Reason: " + e.getMessage(), e);
		}
		return null;
	}

	/**
	 * Get the issue keys that relate to the given component
	 * 
	 * @param component the {@link ProjectComponent} to get the related issues for
	 * @return {@link Collection} of issue keys
	 */
	private Collection<String> getIssueKeys(final ProjectComponent component) {
		try {
			final JqlQueryBuilder queryBuilder =
				JqlQueryBuilder.newBuilder().where().component(new Long[] {component.getId()}).endWhere();
			return getIssueKeys(queryBuilder, component.getProjectId());
		} catch (SearchException e) {
			log.warn(
				"Unable to get all issues from component " + component.getName() + ". Reason: " + e.getMessage(), e);
		}
		return null;
	}

	/**
	 * Get the issue keys that relate to the given component
	 * 
	 * @param queryBuilder the {@link JqlQueryBuilder}
	 * @param projectId the project id to search within
	 * @return {@link Collection} of issue keys
	 * @throws SearchException in case of search exceptions
	 */
	@SuppressWarnings("unchecked")
	private Collection<String> getIssueKeys(final JqlQueryBuilder queryBuilder, final long projectId)
					throws SearchException {
		final SearchResults searchResults =
			this.searchProvider.search(queryBuilder.buildQuery(), this.authenticationContext.getUser(), PagerFilter
				.getUnlimitedFilter());
		final Collection issues = searchResults.getIssues();
		final Collection issueKeys = CollectionUtils.collect(issues, new Transformer() {

			public Object transform(Object object) {
				final Issue issue = (Issue) object;
				return issue.getKey();
			}

		});
		return issueKeys;
	}

	/**
	 * Process all the builds to validate them
	 * 
	 * @param builds the {@link List} of {@link Build} objects to process
	 */
	private void processBuilds(List<Build> builds) {
		Collections.sort(builds);
		Collections.reverse(builds);
		for (Build build : builds) {
			// Process all the related issue keys to make sure they exist
			for (final Iterator<String> iter = build.getRelatedIssueKeys().iterator(); iter.hasNext();) {
				final String key = iter.next();
				final MutableIssue issue = issueManager.getIssueObject(key);
				if (issue == null) {
					iter.remove();
				}
			}
		}
		if (configurationManager.getBooleanProperty(HudsonConfigurationManager.FILTER_HUDSON_BUILDS)) {
			for (final Iterator<Build> iter = builds.iterator(); iter.hasNext();) {
				final Build build = iter.next();
				if (!project.getKey().equalsIgnoreCase(build.getJobKey())) {
					iter.remove();
				}
			}
		}
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
	 * Gets the component id
	 * 
	 * @return the component id
	 */
	public Long getComponentId() {
		return componentId;
	}

	/**
	 * Sets the component id
	 * 
	 * @param componentId the component id
	 */
	public void setComponentId(Long componentId) {
		this.componentId = componentId;
	}

	/**
	 * Gets the results
	 * 
	 * @return the {@link HudsonBuildTabPanelResult} results
	 */
	public HudsonBuildTabPanelResult getResults() {
		return results;
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
	 * Gets the {@link BuildUtils} helper class
	 * 
	 * @return the {@link BuildUtils} helper object
	 */
	public BuildUtils getBuildUtils() {
		return new BuildUtils();
	}

	/**
	 * Gets the internationalisation bean
	 * 
	 * @return the {@link I18nBean}
	 */
	public I18nHelper getI18n() {
		return i18n;
	}

	/**
	 * Gets the {@link HudsonBuildTriggerParser} helper class
	 * 
	 * @return the {@link HudsonBuildTriggerParser} instance
	 */
	public HudsonBuildTriggerParser getBuildTriggerParser() {
		return new HudsonBuildTriggerParser(authenticationContext, userUtil, results.getServer());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getText(String key) {
		return getI18n().getText(key);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getText(String key, String value1) {
		return getI18n().getText(key, value1);
	}

}
