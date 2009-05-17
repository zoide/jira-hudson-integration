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

import java.util.List;

import com.atlassian.jira.project.Project;
import com.atlassian.jira.project.version.Version;
import com.marvelution.jira.plugins.hudson.model.Build;
import com.marvelution.jira.plugins.hudson.model.Job;

/**
 * Interface for Hudson Server Accessor
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public interface HudsonServerAccessor {

	String BASE_ACTION_URL = "/plugin/hudson-jiraapi-plugin";

	String GET_JOBS_ACTION = BASE_ACTION_URL + "/getJobs";

	String GET_BUILDS_ACTION = BASE_ACTION_URL + "/getBuilds";

	/**
	 * Get all Jobs that are configured in a specific Hudson Server
	 * 
	 * @param hudsonServer the {@link HudsonServer} to get the jobs from
	 * @return {@link List} of {@link Job}s
	 * @throws HudsonServerAccessorException in case of communication exceptions with the Hudson server
	 */
	List<Job> getJobs(HudsonServer hudsonServer) throws HudsonServerAccessorException;

	/**
	 * Gets all Builds of a Jira {@link Project}. If the project is associated with a specific {@link HudsonServer}
	 * only that server will be checked for builds, otherwise all the configured servers will be called.
	 * 
	 * @param project the Jira {@link Project}
	 * @return {@link List} of {@link Build}s of the project
	 * @throws HudsonServerAccessorException in case of communication exceptions with the Hudson server
	 */
	List<Build> getBuilds(Project project)throws HudsonServerAccessorException;

	/**
	 * Gets all Builds of a Jira {@link Project} that are configured in a specific Hudson Server
	 * 
	 * @param hudsonServer the {@link HudsonServer} to get the builds from
	 * @param project the Jira {@link Project}
	 * @return {@link List} of {@link Build}s of the project
	 * @throws HudsonServerAccessorException in case of communication exceptions with the Hudson server
	 */
	List<Build> getBuilds(HudsonServer hudsonServer, Project project)throws HudsonServerAccessorException;

	/**
	 * Gets all Builds of a Jira {@link Version}. If the project version is associated with a specific
	 * {@link HudsonServer} only that server will be checked for builds, otherwise all the configured servers will be
	 * called.
	 * 
	 * @param version the Jira {@link Version}
	 * @return {@link List} of {@link Build}s of the project
	 * @throws HudsonServerAccessorException in case of communication exceptions with the Hudson server
	 */
	List<Build> getBuilds(Version version)throws HudsonServerAccessorException;

	/**
	 * Gets all Builds of a Jira {@link Version} that are configured in a specific Hudson Server
	 * 
	 * @param hudsonServer the {@link HudsonServer} to get the builds from
	 * @param version the Jira {@link Version}
	 * @return {@link List} of {@link Build}s of the project
	 * @throws HudsonServerAccessorException in case of communication exceptions with the Hudson server
	 */
	List<Build> getBuilds(HudsonServer hudsonServer, Version version)throws HudsonServerAccessorException;

	/**
	 * Gets all the Builds related to a Set of Jira issue keys that are configured in all known Hudson Servers
	 * 
	 * @param issueKeys the {@link List} of issue keys
	 * @return {@link List} issue related {@link Build} objects
	 * @throws HudsonServerAccessorException in case of communication exceptions with the Hudson server
	 */
	List<Build> getBuilds(List<String> issueKeys) throws HudsonServerAccessorException;

	/**
	 * Gets all the Builds related to a Set of Jira issue keys that are configured in all known Hudson Servers
	 * 
	 * @param hudsonServer the {@link HudsonServer} to get the builds from
	 * @param issueKeys the {@link List} of issue keys
	 * @return {@link List} issue related {@link Build} objects
	 * @throws HudsonServerAccessorException in case of communication exceptions with the Hudson server
	 */
	List<Build> getBuilds(HudsonServer hudsonServer, List<String> issueKeys)
					throws HudsonServerAccessorException;

}
