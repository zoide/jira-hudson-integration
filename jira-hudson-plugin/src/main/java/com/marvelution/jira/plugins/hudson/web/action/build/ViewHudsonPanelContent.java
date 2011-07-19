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

package com.marvelution.jira.plugins.hudson.web.action.build;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import webwork.action.ActionContext;

import com.atlassian.jira.bc.EntityNotFoundException;
import com.atlassian.jira.bc.project.component.ProjectComponent;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.search.SearchException;
import com.atlassian.jira.issue.search.SearchResults;
import com.atlassian.jira.jql.builder.JqlQueryBuilder;
import com.atlassian.jira.project.Project;
import com.atlassian.jira.project.version.Version;
import com.atlassian.jira.security.Permissions;
import com.atlassian.jira.web.action.JiraWebActionSupport;
import com.atlassian.jira.web.bean.PagerFilter;
import com.marvelution.hudson.plugins.apiv2.client.ClientException;
import com.marvelution.hudson.plugins.apiv2.client.HudsonClient;
import com.marvelution.hudson.plugins.apiv2.client.services.BuildQuery;
import com.marvelution.hudson.plugins.apiv2.client.services.JobQuery;
import com.marvelution.hudson.plugins.apiv2.client.services.SearchQuery;
import com.marvelution.hudson.plugins.apiv2.resources.model.build.Builds;
import com.marvelution.jira.plugins.hudson.panels.resultset.BuildsResultSet;
import com.marvelution.jira.plugins.hudson.panels.resultset.JobStatusResultSet;
import com.marvelution.jira.plugins.hudson.panels.resultset.ResultSet;
import com.marvelution.jira.plugins.hudson.panels.utils.HudsonPanelHelper;
import com.marvelution.jira.plugins.hudson.panels.utils.PanelModule;
import com.marvelution.jira.plugins.hudson.panels.utils.PanelView;
import com.marvelution.jira.plugins.hudson.services.HudsonClientFactory;
import com.marvelution.jira.plugins.hudson.services.HudsonConfigurationManager;
import com.marvelution.jira.plugins.hudson.services.associations.HudsonAssociation;
import com.marvelution.jira.plugins.hudson.services.associations.HudsonAssociationManager;
import com.marvelution.jira.plugins.hudson.services.servers.HudsonServer;
import com.marvelution.jira.plugins.hudson.services.servers.HudsonServerManager;
import com.marvelution.jira.plugins.hudson.utils.BuildUtils;
import com.marvelution.jira.plugins.hudson.utils.DateFormatUtils;
import com.marvelution.jira.plugins.hudson.utils.JobUtils;
import com.marvelution.jira.plugins.hudson.utils.RequestAndSessionUtils;
import com.marvelution.jira.plugins.hudson.utils.TriggerFormatUtils;
import com.marvelution.jira.plugins.hudson.web.action.HudsonWebActionSupport;

/**
 * {@link JiraWebActionSupport} implementation to create the panel build list content
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class ViewHudsonPanelContent extends HudsonWebActionSupport {

	private static final long serialVersionUID = 1L;

	private final HudsonServerManager serverManager;
	private final HudsonAssociationManager associationManager;
	private final HudsonClientFactory clientFactory;
	private final HudsonConfigurationManager configurationManager;

	private ResultSet<?> resultSet;
	private PanelView view;
	private int associationId;
	private PanelModule module;
	private long objectId;
	private HudsonAssociation association;
	private HudsonServer server;

	/**
	 * Constructor
	 * 
	 * @param serverManager the {@link HudsonServerManager} implementation
	 * @param associationManager the {@link HudsonAssociationManager} implementation
	 * @param clientFactory the {@link HudsonClientFactory} implementation
	 * @param configurationManager the {@link HudsonConfigurationManager} implementation
	 */
	public ViewHudsonPanelContent(HudsonServerManager serverManager, HudsonAssociationManager associationManager,
			HudsonClientFactory clientFactory, HudsonConfigurationManager configurationManager) {
		this.serverManager = serverManager;
		this.associationManager = associationManager;
		this.clientFactory = clientFactory;
		this.configurationManager = configurationManager;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String doExecute() throws Exception {
		ActionContext.getRequest().setAttribute("__sitemesh__decorator", "none");
		if (!associationManager.hasAssociation(associationId)) {
			addErrorMessage(getText("hudson.panel.error.invalid.association"));
			return ERROR;
		}
		association = associationManager.getAssociation(associationId);
		if (!serverManager.hasServer(association.getServerId())) {
			addErrorMessage(getText("hudson.panel.error.invalid.association.server"));
			return ERROR;
		}
		final Project project = getProjectManager().getProjectObj(association.getProjectId());
		if (!getPermissionManager().hasPermission(Permissions.VIEW_VERSION_CONTROL, project, getRemoteUser())) {
			addErrorMessage(getText("hudson.panel.error.permissions.violation"));
			return ERROR;
		}
		RequestAndSessionUtils.storeInSession(HudsonPanelHelper.SELECTED_VIEW, view.getViewName());
		RequestAndSessionUtils.storeInSession(HudsonPanelHelper.SELECTED_ASSOCIATION, associationId);
		server = serverManager.getServer(association.getServerId());
		final HudsonClient client = clientFactory.create(server);
		switch (module) {
		case ISSUE:
			getPanelContentForIssueView(client);
			break;
		case COMPONENT:
			try {
				getPanelContentForComponentView(client);
			} catch (EntityNotFoundException e) {
				addErrorMessage(getText("hudson.panel.error.invalid.project.component"));
				return ERROR;
			}
			break;
		case VERSION:
			getPanelContentForVersionView(client);
			break;
		case PROJECT:
		default:
			getPanelContentForProjectView(client);
			break;
		}
		if (resultSet != null && resultSet.hasResults()) {
			return resultSet.getView().getView();
		} else {
			addErrorMessage(getText("hudson.panel.error.no.results"));
			return ERROR;
		}
	}

	/**
	 * Internal method to get the content for the project panel view
	 * 
	 * @param client the {@link HudsonClient}
	 * @throws ClientException in case of {@link HudsonClient} errors
	 */
	private void getPanelContentForProjectView(HudsonClient client) throws ClientException {
		final Project project = getProjectManager().getProjectObj(objectId);
		switch (view) {
		case JOB_STATUS:
			resultSet = new JobStatusResultSet(server, client.find(JobQuery.createForJobByName(
					association.getJobName())), getI18nHelper());
			break;
		case BUILDS_BY_ISSUE:
			// See MARVJIRAHUDSON-130, simply use the project key followed by a '-' as the search term
			resultSet = new BuildsResultSet(server, getBuildsRelatedToIssueKeys(client,
					Collections.singleton(project.getKey() + "-")));
			break;
		case BUILDS_BY_JOB:
		default:
			resultSet = new BuildsResultSet(server, client.findAll(BuildQuery.createForAllBuilds(
					association.getJobName())));
			break;
		}
	}

	/**
	 * Internal method to get the content for the project version panel view
	 * 
	 * @param client the {@link HudsonClient}
	 * @throws ClientException in case of {@link HudsonClient} errors
	 */
	private void getPanelContentForVersionView(HudsonClient client) throws ClientException {
		final Version version = getVersionManager().getVersion(objectId);
		switch (view) {
		case JOB_STATUS:
			resultSet = new JobStatusResultSet(server, client.find(JobQuery.createForJobByName(
					association.getJobName())), getI18nHelper());
			break;
		case BUILDS_BY_ISSUE:
			Collection<String> issueKeys = getIssueKeys(version);
			resultSet = new BuildsResultSet(server, getBuildsRelatedToIssueKeys(client, issueKeys));
			break;
		case BUILDS_BY_JOB:
		default:
			Long startDate = 0L, releaseDate = 0L;
			if (version.isReleased()) {
				releaseDate = version.getReleaseDate().getTime();
			}
			List<Version> versions = (List<Version>) version.getProjectObject().getVersions();
			if (versions.size() > 1) {
				for (int i = (int) (version.getSequence().longValue() - 1L); i >= 0; i--) {
					Version prevVersion = versions.get(i);
					if ((prevVersion == null) || (prevVersion.getSequence().longValue() >= version.getSequence()
							.longValue()) || (!prevVersion.isReleased())) {
						continue;
					}
					startDate = prevVersion.getReleaseDate().getTime();
				}
			}
			BuildQuery query;
			if (startDate > 0L && releaseDate > 0L) {
				query = BuildQuery.createForBetweenTimes(association.getJobName(), startDate, releaseDate);
			} else if (startDate > 0L) {
				query = BuildQuery.createForAfterFrom(association.getJobName(), startDate);
			} else {
				query = BuildQuery.createForAllBuilds(association.getJobName());
			}
			resultSet = new BuildsResultSet(server, client.findAll(query));
			break;
		}
	}

	/**
	 * Internal method to get the content for the project component panel view
	 * 
	 * @param client the {@link HudsonClient}
	 * @throws EntityNotFoundException in case the {@link ProjectComponent} cannot be found
	 * @throws ClientException in case of {@link HudsonClient} errors
	 */
	private void getPanelContentForComponentView(HudsonClient client) throws EntityNotFoundException, ClientException {
		final ProjectComponent component = getProjectComponentManager().find(objectId);
		Collection<String> issueKeys = getIssueKeys(component);
		resultSet = new BuildsResultSet(server, getBuildsRelatedToIssueKeys(client, issueKeys));
	}

	/**
	 * Internal method to get the content for the issue panel view
	 * 
	 * @param client the {@link HudsonClient}
	 * @throws ClientException in case of {@link HudsonClient} errors
	 */
	private void getPanelContentForIssueView(HudsonClient client) throws ClientException {
		final Issue issue = getIssueManager().getIssueObject(objectId);
		resultSet = new BuildsResultSet(server, getBuildsRelatedToIssueKeys(client,
				Collections.singletonList(issue.getKey())));
	}

	/**
	 * Internal method to get all the builds related to the {@link Collection} of issue keys given
	 * 
	 * @param client the {@link HudsonClient}
	 * @param issueKeys the {@link Collection} of issue keys
	 * @return the {@link Builds} result
	 * @throws ClientException in case of {@link HudsonClient} errors
	 */
	private Builds getBuildsRelatedToIssueKeys(HudsonClient client, Collection<String> issueKeys) throws ClientException {
		Builds builds;
		if (configurationManager.isFilterHudsonBuilds()) {
			builds = client.findAll(SearchQuery.createForBuildSearch(issueKeys, association.getJobName()));
		} else {
			builds = client.findAll(SearchQuery.createForBuildSearch(issueKeys));
		}
		return builds;
	}

	/**
	 * Get all the Issue Keys related to the given {@link Version}
	 * 
	 * @param version the {@link Version} to get all the issue keys for
	 * @return the {@link Collection} of issue keys
	 */
	private Collection<String> getIssueKeys(Version version) {
		try {
			JqlQueryBuilder queryBuilder = JqlQueryBuilder.newBuilder().where().project(
					new Long[] { version.getProjectObject().getId() }).and().fixVersion(version.getId()).endWhere();
			return getIssueKeys(queryBuilder);
		} catch (SearchException e) {
			this.log.warn("Unable to get all issues from version " + version.getName() + ". Reason: " + e.getMessage(),
					e);
		}
		return null;
	}

	/**
	 * Get all the issue keys related to the given {@link ProjectComponent}
	 * 
	 * @param component the {@link ProjectComponent} to get all the issue keys for
	 * @return the {@link Collection} of issue keys
	 */
	private Collection<String> getIssueKeys(ProjectComponent component) {
		try {
			JqlQueryBuilder queryBuilder = JqlQueryBuilder.newBuilder().where().component(
					new Long[] { component.getId() }).endWhere();
			return getIssueKeys(queryBuilder);
		} catch (SearchException e) {
			this.log.warn("Unable to get all issues from component " + component.getName() + ". Reason: "
					+ e.getMessage(), e);
		}
		return null;
	}

	/**
	 * Get all the issue keys that match the given {@link JqlQueryBuilder} 
	 * 
	 * @param queryBuilder the {@link JqlQueryBuilder}
	 * @return the {@link Collection} of issue keys
	 * @throws SearchException in case of errors during the execution of the {@link JqlQueryBuilder}
	 */
	private Collection<String> getIssueKeys(JqlQueryBuilder queryBuilder) throws SearchException {
		SearchResults searchResults = getSearchProvider().search(queryBuilder.buildQuery(), getAuthenticationContext()
				.getUser(), PagerFilter.getUnlimitedFilter());
		Collection<?> issues = searchResults.getIssues();
		Collection<String> issueKeys = new ArrayList<String>();
		for (Object issue : issues) {
			if (issue instanceof Issue) {
				issueKeys.add(((Issue) issue).getKey());
			}
		}
		return (Collection<String>) issueKeys;
	}

	/**
	 * Getter for the {@link ResultSet}
	 * 
	 * @return the {@link ResultSet}
	 */
	public ResultSet<?> getResultSet() {
		return resultSet;
	}

	/**
	 * Getter for view
	 * 
	 * @return the view
	 */
	public PanelView getView() {
		return view;
	}

	/**
	 * Setter for view
	 * 
	 * @param view the view to set
	 */
	public void setView(PanelView view) {
		this.view = view;
	}

	/**
	 * Getter for view name
	 * 
	 * @return the view name
	 */
	public String getViewName() {
		return view.getViewName();
	}

	/**
	 * Setter for view name
	 * 
	 * @param viewName the view name to set
	 */
	public void setViewName(String viewName) {
		this.view = PanelView.getPanelView(viewName);
	}

	/**
	 * Getter for associationId
	 * 
	 * @return the associationId
	 */
	public int getAssociationId() {
		return associationId;
	}

	/**
	 * Setter for associationId
	 * 
	 * @param associationId the associationId to set
	 */
	public void setAssociationId(int associationId) {
		this.associationId = associationId;
	}

	/**
	 * Getter for module
	 * 
	 * @return the module
	 */
	public PanelModule getModule() {
		return module;
	}

	/**
	 * Setter for module
	 * 
	 * @param module the module to set
	 */
	public void setModule(PanelModule module) {
		this.module = module;
	}

	/**
	 * Getter for module
	 * 
	 * @return the module
	 */
	public String getModuleName() {
		return module.getName();
	}

	/**
	 * Setter for module name
	 * 
	 * @param moduleName the module name to set
	 */
	public void setModuleName(String moduleName) {
		this.module = PanelModule.valueByName(moduleName);
	}

	/**
	 * Getter for objectId
	 * 
	 * @return the objectId
	 */
	public long getObjectId() {
		return objectId;
	}

	/**
	 * Setter for objectId
	 * 
	 * @param objectId the objectId to set
	 */
	public void setObjectId(long objectId) {
		this.objectId = objectId;
	}

	/**
	 * Getter for the {@link BuildUtils} helper class
	 * 
	 * @return the {@link BuildUtils} helper class
	 */
	public BuildUtils getBuildUtils() {
		return new BuildUtils();
	}

	/**
	 * Getter for the {@link JobUtils} helper class
	 * 
	 * @return the {@link JobUtils} helper class
	 */
	public JobUtils getJobUtils() {
		return new JobUtils();
	}

	/**
	 * Getter for the {@link DateFormatUtils} helper class
	 * 
	 * @return the {@link DateFormatUtils} helper class
	 */
	public DateFormatUtils getDateFormatUtils() {
		return new DateFormatUtils(getAuthenticationContext(), configurationManager.isTimePastDateStrings());
	}

	/**
	 * Getter for the {@link TriggerFormatUtils} helper class
	 * 
	 * @return the {@link TriggerFormatUtils} helper class
	 */
	public TriggerFormatUtils getTriggerFormatUtils() {
		return new TriggerFormatUtils(getAuthenticationContext(), request.getContextPath(), server);
	}

}
