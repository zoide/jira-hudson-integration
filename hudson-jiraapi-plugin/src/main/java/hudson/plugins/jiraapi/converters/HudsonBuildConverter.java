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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Cause;
import hudson.model.CauseAction;
import hudson.model.Item;
import hudson.model.ItemGroup;
import hudson.model.Cause.RemoteCause;
import hudson.model.Cause.UpstreamCause;
import hudson.model.Cause.UserCause;
import hudson.model.Run.Artifact;
import hudson.plugins.jiraapi.JiraProjectKeyJobProperty;
import hudson.plugins.jiraapi.utils.JiraKeyUtils;
import hudson.plugins.jiraapi.utils.ProjectUtils;
import hudson.scm.ChangeLogSet.Entry;
import hudson.tasks.test.AbstractTestResultAction;
import hudson.triggers.SCMTrigger.SCMTriggerCause;
import hudson.triggers.TimerTrigger.TimerTriggerCause;

import com.marvelution.jira.plugins.hudson.api.model.Build;
import com.marvelution.jira.plugins.hudson.api.model.BuildArtifact;
import com.marvelution.jira.plugins.hudson.api.model.State;
import com.marvelution.jira.plugins.hudson.api.model.TestResult;
import com.marvelution.jira.plugins.hudson.api.model.triggers.ProjectTrigger;
import com.marvelution.jira.plugins.hudson.api.model.triggers.RemoteTrigger;
import com.marvelution.jira.plugins.hudson.api.model.triggers.SCMTrigger;
import com.marvelution.jira.plugins.hudson.api.model.triggers.TimeTrigger;
import com.marvelution.jira.plugins.hudson.api.model.triggers.Trigger;
import com.marvelution.jira.plugins.hudson.api.model.triggers.UserTrigger;

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
	public static Build convertHudsonBuild(final AbstractBuild<?, ?> hudsonBuild) {
		final Build build = new Build(hudsonBuild.getNumber(), hudsonBuild.getProject().getName());
		build.setUrl(hudsonBuild.getUrl());
		build.setJobUrl(hudsonBuild.getProject().getUrl());
		if (hudsonBuild.getProject().getProperty(JiraProjectKeyJobProperty.class) != null) {
			build.setJobKey(hudsonBuild.getProject().getProperty(JiraProjectKeyJobProperty.class).getKey());
		}
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
		}
		final List<BuildArtifact> artifacts = new ArrayList<BuildArtifact>();
		for (Artifact artifact : hudsonBuild.getArtifacts()) {
			artifacts.add(new BuildArtifact(artifact.getFileName(), hudsonBuild.getUrl() + "artifact/"
				+ artifact.relativePath));
		}
		artifacts.addAll(getArtifactsFromModuleBuilds(hudsonBuild));
		build.setArtifacts(artifacts);
		final List<Trigger> triggers = new ArrayList<Trigger>();
		for (CauseAction causeAction : hudsonBuild.getActions(CauseAction.class)) {
			for (Cause cause : causeAction.getCauses()) {
				if (cause instanceof UserCause) {
					triggers.add(new UserTrigger(((UserCause) cause).getUserName()));
				} else if (cause instanceof UpstreamCause) {
					final UpstreamCause upCause = (UpstreamCause) cause;
					triggers.add(new ProjectTrigger(upCause.getUpstreamProject(), upCause.getUpstreamUrl(), upCause
							.getUpstreamBuild()));
				} else if (cause instanceof RemoteCause) {
					try {
						final RemoteCause remoteCause = (RemoteCause) cause;
						final Field hostField = remoteCause.getClass().getDeclaredField("addr");
						hostField.setAccessible(true);
						final Field noteField = remoteCause.getClass().getDeclaredField("note");
						noteField.setAccessible(true);
						triggers.add(new RemoteTrigger((String) hostField.get(remoteCause), (String) noteField
							.get(remoteCause)));
					} catch (Exception e) {
						triggers.add(new RemoteTrigger());
					}
				} else if (cause instanceof TimerTriggerCause) {
					triggers.add(new TimeTrigger());
				} else if (cause instanceof SCMTriggerCause) {
					triggers.add(new SCMTrigger());
				}
			}
		}
		build.setTriggers(triggers);
		final Set<String> relatedIssueKeys = new HashSet<String>();
		for (Entry entry : hudsonBuild.getChangeSet()) {
			Pattern pattern = null;
			if (ProjectUtils.getJiraProjectKeyPropertyOfProject(hudsonBuild.getProject()) != null) {
				pattern =
					ProjectUtils.getJiraProjectKeyPropertyOfProject(hudsonBuild.getProject()).getIssueKeyPattern();
			}
			relatedIssueKeys.addAll(JiraKeyUtils.getJiraIssueKeysFromText(entry.getMsg(), pattern));
		}
		build.setRelatedIssueKeys(relatedIssueKeys);
		return build;
	}

	/**
	 * Get the artifacts from the given build including module builds with the same build number
	 * 
	 * @param build the Build to get all the artifacts from, including modules
	 * @return {@link List} of artifacts
	 */
	@SuppressWarnings("unchecked")
	public static List<BuildArtifact> getArtifactsFromModuleBuilds(AbstractBuild<?, ?> build) {
		final List<BuildArtifact> artifacts = new ArrayList<BuildArtifact>();
		final AbstractProject<?, ?> project = (AbstractProject<?, ?>) build.getProject();
		if (project instanceof ItemGroup) {
			final ItemGroup<?> parent = (ItemGroup<?>) project;
			for (Item item : parent.getItems()) {
				final AbstractProject<?, ?> module = (AbstractProject<?, ?>) item;
				final AbstractBuild<?, ?> moduleBuild = module.getBuildByNumber(build.getNumber());
				if (moduleBuild != null) {
					for (Artifact artifact : moduleBuild.getArtifacts()) {
						artifacts.add(new BuildArtifact(artifact.getFileName(), moduleBuild.getUrl() + "artifact/"
							+ artifact.relativePath));
					}
				}
			}
		}
		return artifacts;
	}

}
