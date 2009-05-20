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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import hudson.model.Cause;
import hudson.model.CauseAction;
import hudson.model.Run.Artifact;
import hudson.scm.ChangeLogSet.Entry;
import hudson.tasks.test.AbstractTestResultAction;

import com.marvelution.jira.plugins.hudson.model.Build;
import com.marvelution.jira.plugins.hudson.model.State;
import com.marvelution.jira.plugins.hudson.model.TestResult;
import com.marvelution.jira.plugins.hudson.utils.JiraKeyUtils;

/**
 * Converter class to convert a Hudson Build into a Jira Integration Model Build
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class HudsonBuildConverter {

	/**
	 * Convert a Hudson Build into a Jira Integration Model Build
	 * 
	 * @param hudsonBuild the Hudson Build to convert
	 * @return the Jira Integration Model Build
	 */
	@SuppressWarnings("unchecked")
	public static Build convertHudsonBuild(final hudson.model.AbstractBuild<?, ?> hudsonBuild) {
		final Build build = new Build(hudsonBuild.getNumber(), hudsonBuild.getProject().getName());
		build.setUrl(hudsonBuild.getUrl());
		build.setDuration(hudsonBuild.getDuration());
		build.setTimestamp(hudsonBuild.getTimestamp().getTimeInMillis());
		build.setResult(HudsonResultConverter.convertHudsonResult(hudsonBuild.getResult()));
		if (hudsonBuild.hasntStartedYet()) {
			build.setState(State.NOT_STARTED);
		} else if (hudsonBuild.isBuilding()) {
			build.setState(State.BUILDING);
		} else if (hudsonBuild.isLogUpdated()) {
			build.setState(State.COMPLETED);
		}
		final AbstractTestResultAction<?> testAction = hudsonBuild.getTestResultAction();
		if (testAction != null) {
			final TestResult testResult = new TestResult();
			testResult.setFailed(testAction.getFailCount());
			testResult.setSkipped(testAction.getSkipCount());
			testResult.setTotal(testAction.getTotalCount());
			build.setTestResult(testResult);
			build.setHealthReport(HudsonHealthReportConverter.convertHudsonHealthReport(testAction.getBuildHealth()));
		}
		final List<String> artifacts = new ArrayList<String>();
		for (Artifact artifact : hudsonBuild.getArtifacts()) {
			artifacts.add(artifact.getFileName());
		}
		build.setArtifacts(artifacts);
		final List<String> triggers = new ArrayList<String>();
		for (CauseAction causeAction : hudsonBuild.getActions(CauseAction.class)) {
			for (Cause cause : causeAction.getCauses()) {
				triggers.add(cause.getShortDescription());
			}
		}
		build.setTriggers(triggers);
		final Set<String> relatedIssueKeys = new HashSet<String>();
		for (Entry entry : hudsonBuild.getChangeSet()) {
			relatedIssueKeys.addAll(JiraKeyUtils.getJiraIssueKeysFromText(entry.getMsg()));
		}
		build.setRelatedIssueKeys(relatedIssueKeys);
		return build;
	}

}
