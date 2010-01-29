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

import com.marvelution.jira.plugins.hudson.api.model.BuildsList;
import com.marvelution.jira.plugins.hudson.api.model.HudsonView;
import com.marvelution.jira.plugins.hudson.api.model.HudsonViewsList;
import com.marvelution.jira.plugins.hudson.api.model.Job;
import com.marvelution.jira.plugins.hudson.api.model.JobsList;

import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Hudson;
import hudson.model.ItemGroup;
import hudson.model.TopLevelItem;
import hudson.model.View;
import hudson.plugins.jiraapi.JiraProjectKeyJobProperty;
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
public class ApiImpl {

	/**
	 * Gets the list of all known Hudson projects
	 * 
	 * @return {@link JobsList}
	 */
	@SuppressWarnings("unchecked")
	public JobsList listAllProjects() {
		final JobsList jobs = new JobsList();
		for (AbstractProject<?, ?> project : ProjectUtils.getAllProjects()) {
			final Job job = new Job(project.getName(), project.getDescription());
			job.setUrl(project.getUrl());
			job.setBuildable(project.isBuildable());
			job.setNextBuildNumber(project.getNextBuildNumber());
			if (project.getProperty(JiraProjectKeyJobProperty.class) != null) {
				job.setJiraKey(project.getProperty(JiraProjectKeyJobProperty.class).getKey());
			}
			if (project instanceof ItemGroup) {
				final JobsList modules = new JobsList();
				final ItemGroup<AbstractProject<?, ?>> itemGroup = (ItemGroup<AbstractProject<?, ?>>) project;
				for (final AbstractProject<?, ?> item : itemGroup.getItems()) {
					final Job module = new Job(item.getName(), item.getDescription());
					module.setUrl(item.getUrl());
					module.setBuildable(project.isBuildable());
					module.setNextBuildNumber(project.getNextBuildNumber());
					if (item.getProperty(JiraProjectKeyJobProperty.class) != null) {
						module.setJiraKey(item.getProperty(JiraProjectKeyJobProperty.class).getKey());
					}
					modules.getJobs().add(module);
				}
				job.setModulesList(modules);
			}
			jobs.getJobs().add(job);
		}
		return jobs;
	}

	/**
	 * Gets all Projects from Hudson
	 * 
	 * @return {@link JobsList}
	 */
	public JobsList getAllProjects() {
		final JobsList jobs = new JobsList();
		for (AbstractProject<?, ?> project : ProjectUtils.getAllProjects()) {
			jobs.getJobs().add(HudsonProjectConverter.convertHudsonProject(project));
		}
		return jobs;
	}

	/**
	 * Gets the Hudson project by Jira key
	 * 
	 * @param projectKey the Jira key of the project to get
	 * @return the {@link Job}, may be <code>null</code> if no Hudson project is found with the Jira Key configured
	 */
	public Job getProjectByJiraKey(String projectKey) {
		final AbstractProject<?, ?> project = ProjectUtils.getProjectByJiraProjectKey(projectKey);
		if (project != null) {
			return HudsonProjectConverter.convertHudsonProject(project);
		} else {
			return null;
		}
	}

	/**
	 * Gets all Builds of a Jira Project by Jira project key
	 * 
	 * @param projectKey the Jira project key
	 * @return {@link BuildsList}
	 */
	public BuildsList getBuildsByJiraProject(final String projectKey) {
		final BuildsList builds = new BuildsList();
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
	 * @return {@link BuildsList}
	 */
	public BuildsList getBuildsByJiraVersion(final String projectKey, final long startDate, final long releaseDate) {
		final BuildsList builds = new BuildsList();
		final AbstractProject<?, ?> project = ProjectUtils.getProjectByJiraProjectKey(projectKey);
		if (project != null) {
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
		}
		return builds;
	}

	/**
	 * Gets all the Builds related to the given Jira Issue Keys
	 * 
	 * @param issueKeys {@link String} array of Jira issue keys
	 * @return {@link BuildsList}
	 */
	@SuppressWarnings("unchecked")
	public BuildsList getBuildsByJiraIssueKeys(final String[] issueKeys) {
		final BuildsList builds = new BuildsList();
		for (String issueKey : issueKeys) {
			final Issue indexedIssue = IssueIndexer.getInstance().getIssueIndex(issueKey);
			if (indexedIssue != null && !indexedIssue.getProjects().isEmpty()) {
				for (Project indexedJob : indexedIssue.getProjects()) {
					final AbstractProject<?, ?> item =
						ProjectUtils.getProjectByName(indexedJob.getName(), indexedJob.getParentName());
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

	/**
	 * Get all Views from the Hudson instance
	 * 
	 * @return {@link HudsonViewsList} containing all available views
	 */
	public HudsonViewsList getAllViews() {
		final HudsonViewsList views = new HudsonViewsList();
		for (View view : Hudson.getInstance().getViews()) {
			views.getViews().add(new HudsonView(view.getViewName(), view.getDescription()));
		}
		return views;
	}

	/**
	 * Get the View by the name provided and all the related projects
	 * 
	 * @param viewName the name of the View to get
	 * @return the {@link HudsonView}
	 */
	@SuppressWarnings("unchecked")
	public HudsonView getView(String viewName) {
		final View view = Hudson.getInstance().getView(viewName);
		if (view != null) {
			final HudsonView hudsonView = new HudsonView(view.getDisplayName(), view.getDescription());
			for (TopLevelItem topItem : view.getItems()) {
				if (topItem instanceof AbstractProject) {
					hudsonView.getJobs().add(
						HudsonProjectConverter.convertHudsonProject((AbstractProject<?, ?>) topItem));
				}
			}
			return hudsonView;
		}
		return null;
	}

}
