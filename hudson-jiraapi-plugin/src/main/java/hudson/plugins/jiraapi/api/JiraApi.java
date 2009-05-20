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

import java.util.SortedMap;

import com.marvelution.jira.plugins.hudson.model.Builds;
import com.marvelution.jira.plugins.hudson.model.Jobs;

import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Hudson;
import hudson.plugins.jiraapi.converters.HudsonBuildConverter;
import hudson.plugins.jiraapi.converters.HudsonProjectConverter;
import hudson.plugins.jiraapi.index.IssueIndexer;
import hudson.plugins.jiraapi.index.model.Issue;
import hudson.plugins.jiraapi.index.model.Project;
import hudson.plugins.jiraapi.utils.ProjectUtils;

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
	public Jobs getAllJiraProjects() {
		final Jobs jobs = new Jobs();
		for (AbstractProject<?, ?> project : Hudson.getInstance().getAllItems(AbstractProject.class)) {
			jobs.getJobs().add(HudsonProjectConverter.convertHudsonProject(project));
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
		final AbstractProject<?, ?> project = ProjectUtils.getProjectByJiraProjectKey(projectKey);
		if (project != null) {
			for (AbstractBuild<?, ?> build : project.getBuilds()) {
				builds.getBuilds().add(HudsonBuildConverter.convertHudsonBuild(build));
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
		final AbstractProject<?, ?> project = ProjectUtils.getProjectByJiraProjectKey(projectKey);
		for (AbstractBuild<?, ?> build : project.getBuilds()) {
			if (releaseDate > 0L) {
				if (build.getTimestamp().getTimeInMillis() >= startDate
					&& build.getTimestamp().getTimeInMillis() <= releaseDate) {
					builds.getBuilds().add(HudsonBuildConverter.convertHudsonBuild(build));
				}
			} else {
				if (build.getTimestamp().getTimeInMillis() >= startDate) {
					builds.getBuilds().add(HudsonBuildConverter.convertHudsonBuild(build));
				}
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
		final Builds builds = new Builds();
		for (String issueKey : issueKeys) {
			final Issue indexedIssue = IssueIndexer.getInstance().getIssueIndex(issueKey);
			if (indexedIssue != null && !indexedIssue.getProjects().isEmpty()) {
				for (Project indexedJob : indexedIssue.getProjects()) {
					final hudson.model.TopLevelItem item = Hudson.getInstance().getItem(indexedJob.getName());
					if (item != null && item instanceof AbstractProject) {
						final SortedMap<Integer, ?> jobBuilds = ((AbstractProject<?, ?>) item).getBuildsAsMap();
						for (Integer buildNumber : indexedJob.getBuildNumbers()) {
							final AbstractBuild<?, ?> build = (AbstractBuild<?, ?>) jobBuilds.get(buildNumber);
							builds.getBuilds().add(HudsonBuildConverter.convertHudsonBuild(build));
						}
					}
				}
			}
		}
		return builds;
	}

}
