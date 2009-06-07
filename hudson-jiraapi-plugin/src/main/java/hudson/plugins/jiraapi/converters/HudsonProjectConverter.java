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

package hudson.plugins.jiraapi.converters;

import java.util.ArrayList;
import java.util.List;

import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.ItemGroup;
import hudson.plugins.jiraapi.JiraProjectKeyJobProperty;

import com.marvelution.jira.plugins.hudson.model.Build;
import com.marvelution.jira.plugins.hudson.model.BuildsList;
import com.marvelution.jira.plugins.hudson.model.HealthReport;
import com.marvelution.jira.plugins.hudson.model.Job;
import com.marvelution.jira.plugins.hudson.model.JobsList;
import com.marvelution.jira.plugins.hudson.model.Result;

/**
 * Converter class to convert a Hudson Job into a Jira Integration Model Job
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class HudsonProjectConverter {

	/**
	 * Convert a Hudson Job into a Jira Integration Job
	 * 
	 * @param <PROJECT> the type of the project to be converted
	 * @param project the Hudson {@link AbstractProject} to convert
	 * @return the converted {@link Job}
	 */
	@SuppressWarnings("unchecked")
	public static <PROJECT extends AbstractProject<?, ?>> Job convertHudsonProject(PROJECT project) {
		final Job job = new Job(project.getName(), project.getDescription());
		job.setUrl(project.getUrl());
		if (project.getProperty(JiraProjectKeyJobProperty.class) != null) {
			job.setJiraKey(project.getProperty(JiraProjectKeyJobProperty.class).getKey());
		}
		job.setBuildable(project.isBuildable());
		final BuildsList builds = new BuildsList();
		for (AbstractBuild<?, ?> hudsonBuild : project.getBuilds()) {
			final Build build = HudsonBuildConverter.convertHudsonBuild(hudsonBuild);
			builds.getBuilds().add(build);
			if (project.getFirstBuild() != null && build.getNumber() == project.getFirstBuild().getNumber()) {
				job.setFirstBuild(build);
			}
			if (project.getLastBuild() != null && build.getNumber() == project.getLastBuild().getNumber()) {
				job.setLastBuild(build);
			}
			if (project.getLastCompletedBuild() != null
				&& build.getNumber() == project.getLastCompletedBuild().getNumber()) {
				job.setLastCompletedBuild(build);
			}
			if (project.getLastSuccessfulBuild() != null
				&& build.getNumber() == project.getLastSuccessfulBuild().getNumber()) {
				job.setLastSuccessfulBuild(build);
			}
			if (project.getLastStableBuild() != null
				&& build.getNumber() == project.getLastStableBuild().getNumber()) {
				job.setLastStableBuild(build);
			}
			if (project.getLastFailedBuild() != null
				&& build.getNumber() == project.getLastFailedBuild().getNumber()) {
				job.setLastFailedBuild(build);
			}
			if (build.getResult() == Result.UNSTABLE) {
				if (job.getLastUnstableBuild() == null) {
					job.setLastUnstableBuild(build);
				} else if (job.getLastUnstableBuild().getNumber() < build.getNumber()) {
					job.setLastUnstableBuild(build);
				}
			}
		}
		job.setBuildsList(builds);
		job.setNextBuildNumber(project.getNextBuildNumber());
		if (job.getLastBuild() != null) {
			job.setResult(job.getLastBuild().getResult());
		} else {
			job.setResult(Result.NOT_BUILT);
		}
		final List<HealthReport> healthReports = new ArrayList<HealthReport>();
		if (project.getBuildHealthReports() != null && project.getBuildHealthReports().size() > 0) {
			for (hudson.model.HealthReport hudsonHealthReport : project.getBuildHealthReports()) {
				healthReports.add(HudsonHealthReportConverter.convertHudsonHealthReport(hudsonHealthReport));
			}
		} else {
			healthReports.add(HealthReport.NO_RECENT_BUILDS);
		}
		job.setHealthReports(healthReports);
		if (project instanceof ItemGroup) {
			final JobsList modules = new JobsList();
			final ItemGroup<PROJECT> itemGroup = (ItemGroup<PROJECT>) project;
			for (final PROJECT module : itemGroup.getItems()) {
				modules.getJobs().add(convertHudsonProject(module));
			}
			job.setModulesList(modules);
		}
		return job;
	}
}
