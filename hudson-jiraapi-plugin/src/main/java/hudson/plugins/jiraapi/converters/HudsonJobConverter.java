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

import hudson.plugins.jiraapi.JiraProjectKeyJobProperty;

import com.marvelution.jira.plugins.hudson.model.Build;
import com.marvelution.jira.plugins.hudson.model.Builds;
import com.marvelution.jira.plugins.hudson.model.HealthReport;
import com.marvelution.jira.plugins.hudson.model.Job;
import com.marvelution.jira.plugins.hudson.model.Result;

/**
 * Converter class to convert a Hudson Job into a Jira Integration Model Job
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class HudsonJobConverter {

	/**
	 * Convert a Hudson Job into a Jira Integration Job
	 * 
	 * @param hudsonJob the Hudson Job to convert
	 * @return the converted {@link Job}
	 */
	public static Job convertHudsonJob(hudson.model.Job<?, ?> hudsonJob) {
		final Job job = new Job(hudsonJob.getName(), hudsonJob.getDescription(), hudsonJob.getUrl());
		if (hudsonJob.getProperty(JiraProjectKeyJobProperty.class) != null) {
			job.setJiraKey(hudsonJob.getProperty(JiraProjectKeyJobProperty.class).getKey());
		}
		job.setBuildable(hudsonJob.isBuildable());
		final Builds builds = new Builds();
		for (hudson.model.Run<?, ?> hudsonRun : hudsonJob.getBuilds()) {
			final hudson.model.AbstractBuild<?, ?> hudsonBuild = (hudson.model.AbstractBuild<?, ?>) hudsonRun;
			final Build build = HudsonBuildConverter.convertHudsonBuild(hudsonBuild);
			builds.getBuilds().add(build);
			if (hudsonJob.getFirstBuild() != null && build.getNumber() == hudsonJob.getFirstBuild().getNumber()) {
				job.setFirstBuild(build);
			}
			if (hudsonJob.getLastBuild() != null && build.getNumber() == hudsonJob.getLastBuild().getNumber()) {
				job.setLastBuild(build);
			}
			if (hudsonJob.getLastCompletedBuild() != null
				&& build.getNumber() == hudsonJob.getLastCompletedBuild().getNumber()) {
				job.setLastCompletedBuild(build);
			}
			if (hudsonJob.getLastSuccessfulBuild() != null
				&& build.getNumber() == hudsonJob.getLastSuccessfulBuild().getNumber()) {
				job.setLastSuccessfulBuild(build);
			}
			if (hudsonJob.getLastStableBuild() != null
				&& build.getNumber() == hudsonJob.getLastStableBuild().getNumber()) {
				job.setLastStableBuild(build);
			}
			if (hudsonJob.getLastFailedBuild() != null
				&& build.getNumber() == hudsonJob.getLastFailedBuild().getNumber()) {
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
		job.setBuilds(builds);
		job.setNextBuildNumber(hudsonJob.getNextBuildNumber());
		if (job.getLastBuild() != null) {
			job.setResult(job.getLastBuild().getResult());
		} else {
			job.setResult(Result.NOT_BUILT);
		}
		final List<HealthReport> healthReports = new ArrayList<HealthReport>();
		if (hudsonJob.getBuildHealthReports() != null && hudsonJob.getBuildHealthReports().size() > 0) {
			for (hudson.model.HealthReport hudsonHealthReport : hudsonJob.getBuildHealthReports()) {
				healthReports.add(HudsonHealthReportConverter.convertHudsonHealthReport(hudsonHealthReport));
			}
		} else {
			healthReports.add(HealthReport.EMPTY);
		}
		job.setHealthReports(healthReports);
		return job;
	}
}
