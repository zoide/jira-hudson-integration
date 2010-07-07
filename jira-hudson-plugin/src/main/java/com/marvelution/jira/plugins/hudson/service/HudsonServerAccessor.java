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

package com.marvelution.jira.plugins.hudson.service;

import java.util.Collection;
import java.util.List;

import com.atlassian.jira.project.Project;
import com.atlassian.jira.project.version.Version;
import com.marvelution.jira.plugins.hudson.api.model.ApiImplementation;
import com.marvelution.jira.plugins.hudson.api.model.Build;
import com.marvelution.jira.plugins.hudson.api.model.HudsonView;
import com.marvelution.jira.plugins.hudson.api.model.Job;

/**
 * Interface for Hudson Server Accessor
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public interface HudsonServerAccessor {

	/**
	 * The Hudson plugin base url
	 */
	String BASE_ACTION_URL = "/plugin/hudson-jiraapi-plugin";

	/**
	 * The getCrumb API endpoint url
	 */
	String GET_CRUMB_ACTION = BASE_ACTION_URL + "/getCrumb";

	/**
	 * The getApiVersion endpoint url
	 */
	String GET_API_VERSION_ACTION = BASE_ACTION_URL + "/getApiVersion";

	/**
	 * The listAllProjects endpoint url
	 */
	String LIST_ALL_PROJECTS_ACTION = BASE_ACTION_URL + "/listAllProjects";

	/**
	 * The getAllProjects endpoint url
	 */
	String GET_ALL_PROJECTS_ACTION = BASE_ACTION_URL + "/getAllProjects";

	/**
	 * The getProject endpoint url
	 */
	String GET_PROJECT_ACTION = BASE_ACTION_URL + "/getProject";

	/**
	 * The getProjectBuilds endpoint url
	 */
	String GET_PROJECT_BUILDS_ACTION = BASE_ACTION_URL + "/getProjectBuilds";

	/**
	 * The getVersionBuilds endpoint url
	 */
	String GET_VERSION_BUILDS_ACTION = BASE_ACTION_URL + "/getVersionBuilds";

	/**
	 * The getIssueBuilds endpoint url
	 */
	String GET_ISSUE_BUILDS_ACTION = BASE_ACTION_URL + "/getIssueBuilds";

	/**
	 * The listAllViews endpoint url
	 */
	String LIST_ALL_VIEWS_ACTION = BASE_ACTION_URL + "/listAllViews";

	/**
	 * The getView endpoint url
	 */
	String GET_VIEW_ACTION = BASE_ACTION_URL + "/getView";

	/**
	 * Get the crumb data for a {@link HudsonServer}
	 * 
	 * @param hudsonServer the {@link HudsonServer} to get the Crumb from
	 * @throws HudsonServerAccessorException in case of communication exceptions with the Hudson server
	 * @throws HudsonServerAccessDeniedException in case Hudson denies access to the Remote API
	 */
	void getCrumb(HudsonServer hudsonServer) throws HudsonServerAccessorException, HudsonServerAccessDeniedException;

	/**
	 * Gets the API Implementation information of the {@link HudsonServer} given
	 * 
	 * @param hudsonServer the {@link HudsonServer} to get the API version from
	 * @return the {@link ApiImplementation}
	 * @throws HudsonServerAccessorException in case of communication exceptions with the Hudson server
	 * @throws HudsonServerAccessDeniedException in case Hudson denies access to the Remote API
	 */
	ApiImplementation getApiImplementation(HudsonServer hudsonServer) throws HudsonServerAccessorException,
					HudsonServerAccessDeniedException;

	/**
	 * Get all Projects that are configured in a specific Hudson Server
	 * 
	 * @param hudsonServer the {@link HudsonServer} to get the projects from
	 * @return {@link List} of {@link Job}s
	 * @throws HudsonServerAccessorException in case of communication exceptions with the Hudson server
	 * @throws HudsonServerAccessDeniedException in case Hudson denies access to the Remote API
	 */
	List<Job> getProjectsList(HudsonServer hudsonServer) throws HudsonServerAccessorException,
					HudsonServerAccessDeniedException;

	/**
	 * Get all Projects that are configured in a specific Hudson Server
	 * 
	 * @param hudsonServer the {@link HudsonServer} to get the projects from
	 * @return {@link List} of {@link Job}s
	 * @throws HudsonServerAccessorException in case of communication exceptions with the Hudson server
	 * @throws HudsonServerAccessDeniedException in case Hudson denies access to the Remote API
	 */
	List<Job> getProjects(HudsonServer hudsonServer) throws HudsonServerAccessorException,
					HudsonServerAccessDeniedException;

	/**
	 * Get a Project that are configured in a specific Hudson Server
	 * 
	 * @param project the Jira {@link Project} to get from the {@link HudsonServer}
	 * @return {@link List} of {@link Job} objects
	 * @throws HudsonServerAccessorException in case of communication exceptions with the Hudson server
	 * @throws HudsonServerAccessDeniedException in case Hudson denies access to the Remote API
	 */
	List<Job> getProject(Project project) throws HudsonServerAccessorException, HudsonServerAccessDeniedException;

	/**
	 * Get a Project that are configured in a specific Hudson Server
	 * 
	 * @param hudsonServer the {@link HudsonServer} to get the projects from
	 * @param project the Jira {@link Project} to get from the {@link HudsonServer}
	 * @return {@link List} of {@link Job} objects
	 * @throws HudsonServerAccessorException in case of communication exceptions with the Hudson server
	 * @throws HudsonServerAccessDeniedException in case Hudson denies access to the Remote API
	 */
	List<Job> getProject(HudsonServer hudsonServer, Project project) throws HudsonServerAccessorException,
					HudsonServerAccessDeniedException;

	/**
	 * Gets all Builds of a Jira {@link Project}. If the project is associated with a specific {@link HudsonServer}
	 * only that server will be checked for builds, otherwise the default Hudson server will be called.
	 * 
	 * @param project the Jira {@link Project}
	 * @return {@link List} of {@link Build}s of the project
	 * @throws HudsonServerAccessorException in case of communication exceptions with the Hudson server
	 * @throws HudsonServerAccessDeniedException in case Hudson denies access to the Remote API
	 */
	List<Build> getBuilds(Project project) throws HudsonServerAccessorException, HudsonServerAccessDeniedException;

	/**
	 * Gets all Builds of a Jira {@link Project} that are configured in a specific Hudson Server
	 * 
	 * @param hudsonServer the {@link HudsonServer} to get the builds from
	 * @param project the Jira {@link Project}
	 * @return {@link List} of {@link Build}s of the project
	 * @throws HudsonServerAccessorException in case of communication exceptions with the Hudson server
	 * @throws HudsonServerAccessDeniedException in case Hudson denies access to the Remote API
	 */
	List<Build> getBuilds(HudsonServer hudsonServer, Project project) throws HudsonServerAccessorException,
					HudsonServerAccessDeniedException;

	/**
	 * Gets all Builds of a Jira {@link Version}. If the project version is associated with a specific
	 * {@link HudsonServer} only that server will be checked for builds, otherwise the default Hudson server will be
	 * called.
	 * 
	 * @param version the Jira {@link Version}
	 * @return {@link List} of {@link Build}s of the project
	 * @throws HudsonServerAccessorException in case of communication exceptions with the Hudson server
	 * @throws HudsonServerAccessDeniedException in case Hudson denies access to the Remote API
	 */
	List<Build> getBuilds(Version version) throws HudsonServerAccessorException, HudsonServerAccessDeniedException;

	/**
	 * Gets all Builds of a Jira {@link Version} that are configured in a specific Hudson Server
	 * 
	 * @param hudsonServer the {@link HudsonServer} to get the builds from
	 * @param version the Jira {@link Version}
	 * @return {@link List} of {@link Build}s of the project
	 * @throws HudsonServerAccessorException in case of communication exceptions with the Hudson server
	 * @throws HudsonServerAccessDeniedException in case Hudson denies access to the Remote API
	 */
	List<Build> getBuilds(HudsonServer hudsonServer, Version version) throws HudsonServerAccessorException,
					HudsonServerAccessDeniedException;

	/**
	 * Gets all the Builds related to a Set of Jira issue keys that are configured in all known Hudson Servers
	 * 
	 * @param issueKeys the {@link Collection} of issue keys
	 * @return {@link List} issue related {@link Build} objects
	 * @throws HudsonServerAccessorException in case of communication exceptions with the Hudson server
	 * @throws HudsonServerAccessDeniedException in case Hudson denies access to the Remote API
	 */
	List<Build> getBuilds(Collection<String> issueKeys) throws HudsonServerAccessorException,
					HudsonServerAccessDeniedException;

	/**
	 * Gets all the Builds related to a Set of Jira issue keys that are configured in all known Hudson Servers
	 * 
	 * @param hudsonServer the {@link HudsonServer} to get the builds from
	 * @param issueKeys the {@link Collection} of issue keys
	 * @return {@link List} issue related {@link Build} objects
	 * @throws HudsonServerAccessorException in case of communication exceptions with the Hudson server
	 * @throws HudsonServerAccessDeniedException in case Hudson denies access to the Remote API
	 */
	List<Build> getBuilds(HudsonServer hudsonServer, Collection<String> issueKeys) throws HudsonServerAccessorException,
					HudsonServerAccessDeniedException;

	/**
	 * Gets all the available Views on the Hudson Server
	 * 
	 * @param hudsonServer the {@link HudsonServer} to get the Views from
	 * @return {@link List} of {@link HudsonView}
	 * @throws HudsonServerAccessorException in case of communication exceptions with the Hudson server
	 * @throws HudsonServerAccessDeniedException in case Hudson denies access to the Remote API
	 */
	List<HudsonView> getViewsList(HudsonServer hudsonServer) throws HudsonServerAccessorException,
					HudsonServerAccessDeniedException;

	/**
	 * Gets the View by name from the {@link HudsonServer}
	 * 
	 * @param hudsonServer the {@link HudsonServer} to get the View from
	 * @param name the name of the View to get
	 * @return the {@link HudsonView}
	 * @throws HudsonServerAccessorException in case of communication exceptions with the Hudson server
	 * @throws HudsonServerAccessDeniedException in case Hudson denies access to the Remote API
	 */
	HudsonView getView(HudsonServer hudsonServer, String name) throws HudsonServerAccessorException,
					HudsonServerAccessDeniedException;

}
