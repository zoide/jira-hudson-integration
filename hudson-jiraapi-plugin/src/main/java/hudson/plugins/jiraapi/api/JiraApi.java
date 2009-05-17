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

package hudson.plugins.jiraapi.api;

import java.util.List;

import com.marvelution.jira.plugins.hudson.model.Builds;
import com.marvelution.jira.plugins.hudson.model.Jobs;

import hudson.model.Hudson;
import hudson.plugins.jiraapi.converters.HudsonBuildConverter;
import hudson.plugins.jiraapi.converters.HudsonJobConverter;
import hudson.plugins.jiraapi.utils.JobUtils;

/**
 * API to get Jira related Builds and Projects
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class JiraApi {

	/**
	 * Gets all Jira related projects from Hudson
	 * 
	 * @return {@link Jobs}
	 */
	@SuppressWarnings("unchecked")
	public Jobs getAllJiraProjects() {
		final Jobs jobs = new Jobs();
		final List<hudson.model.Job> hudsonJobs = Hudson.getInstance().getAllItems(hudson.model.Job.class);
		for (hudson.model.Job<?, ?> hudsonJob : hudsonJobs) {
			jobs.getJobs().add(HudsonJobConverter.convertHudsonJob(hudsonJob));
		}
		return jobs;
	}

	/**
	 * Gets all Builds of a Jira Project by Jira project key
	 * 
	 * @param projectKey the Jira project key
	 * @return {@link Builds}
	 */
	public Builds getBuildsByJiraProject(final String projectKey) {
		final Builds builds = new Builds();
		final hudson.model.Job<?, ?> hudsonJob = JobUtils.getJobByJiraProjectKey(projectKey);
		if (hudsonJob != null) {
			for (hudson.model.Run<?, ?> hudsonRun : hudsonJob.getBuilds()) {
				final hudson.model.AbstractBuild<?, ?> hudsonBuild = (hudson.model.AbstractBuild<?, ?>) hudsonRun;
				builds.getBuilds().add(HudsonBuildConverter.convertHudsonBuild(hudsonBuild));
			}
		}
		return builds;
	}

	/**
	 * Gets all Builds related to a Jira Version of a Project
	 * 
	 * @param projectKey the Jira project key
	 * @param startDate the start date in milliseconds of the version, <code>null</code> to start from the first Build
	 * @param releaseDate the release date in milliseconds of the version, <code>null</code> to end at the last Build
	 * @return {@link Builds}
	 */
	public Builds getBuildsByJiraVersion(final String projectKey, final long startDate, final long releaseDate) {
		final Builds builds = new Builds();
		final hudson.model.Job<?, ?> hudsonJob = JobUtils.getJobByJiraProjectKey(projectKey);
		for (hudson.model.Run<?, ?> hudsonRun : hudsonJob.getBuilds()) {
			final hudson.model.AbstractBuild<?, ?> hudsonBuild = (hudson.model.AbstractBuild<?, ?>) hudsonRun;
			if (hudsonBuild.getTimestamp().getTimeInMillis() >= startDate
				&& hudsonBuild.getTimestamp().getTimeInMillis() <= releaseDate) {
				builds.getBuilds().add(HudsonBuildConverter.convertHudsonBuild(hudsonBuild));
			}
		}
		return builds;
	}

	/**
	 * Gets all the Builds related to the given Jira Issue Keys
	 * 
	 * @param issueKeys {@link String} array of Jira issue keys
	 * @return {@link Builds}
	 */
	public Builds getBuildsByJiraIssueKeys(final String[] issueKeys) {
		return new Builds();
	}

}
