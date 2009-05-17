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

package com.marvelution.jira.plugins.hudson.model;

import java.util.List;

import com.marvelution.jira.plugins.hudson.xstream.converters.ResultConverter;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

/**
 * Model class for Hudson Jobs
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
@XStreamAlias("job")
public class Job implements HudsonServerAware, Comparable<Job> {

	@XStreamOmitField
	private int hudsonServerId;

	private String jiraKey;

	private String name;

	private String description;

	private boolean buildable;

	private Builds builds;

	private Build firstBuild;

	private Build lastBuild;

	private Build lastCompletedBuild;

	private Build lastFailedBuild;

	private Build lastStableBuild;

	private Build lastUnstableBuild;

	private Build lastSuccessfulBuild;

	private int nextBuildNumber;

	@XStreamImplicit(itemFieldName = "healthReport")
	private List<HealthReport> healthReports;

	@XStreamConverter(ResultConverter.class)
	@XStreamAlias("result")
	private Result result;

	/**
	 * Constructor
	 * 
	 * @param name the job name
	 * @param description the job description
	 */
	public Job(String name, String description) {
		setName(name);
		setDescription(description);
	}

	/**
	 * Gets the Hudson Server Id
	 * 
	 * @return the Hudson Server Id
	 */
	public int getHudsonServerId() {
		return hudsonServerId;
	}

	/**
	 * Sets the Hudson Server Id
	 * 
	 * @param hudsonServerId the Hudson Server Id
	 */
	public void setHudsonServerId(int hudsonServerId) {
		this.hudsonServerId = hudsonServerId;
	}

	/**
	 * Gets the name
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name
	 * 
	 * @param name the name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the Jira project key
	 * 
	 * @return the Jira project key
	 */
	public String getJiraKey() {
		return jiraKey;
	}

	/**
	 * Sets the Jira project key
	 * 
	 * @param jiraKey the Jira project key
	 */
	public void setJiraKey(String jiraKey) {
		this.jiraKey = jiraKey;
	}

	/**
	 * Gets the description
	 * 
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description
	 * 
	 * @param description the description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Gets if the job is buildable
	 * 
	 * @return <code>true</code> if buildable, <code>false</code> otherwise
	 */
	public boolean isBuildable() {
		return buildable;
	}

	/**
	 * Sets if the job is buildable
	 * 
	 * @param buildable <code>true</code> if buildable, <code>false</code> otherwise
	 */
	public void setBuildable(boolean buildable) {
		this.buildable = buildable;
	}

	/**
	 * Gets the {@link Builds}
	 * 
	 * @return the {@link Builds}
	 */
	public Builds getBuilds() {
		return builds;
	}

	/**
	 * Sets the {@link Builds}
	 * 
	 * @param builds the {@link Builds}
	 */
	public void setBuilds(Builds builds) {
		this.builds = builds;
	}

	/**
	 * Gets the First {@link Build}
	 * 
	 * @return the {@link Build}
	 */
	public Build getFirstBuild() {
		return firstBuild;
	}

	/**
	 * Sets the first build
	 * 
	 * @param firstBuild the first {@link Build}
	 */
	public void setFirstBuild(Build firstBuild) {
		this.firstBuild = firstBuild;
	}

	/**
	 * Gets the Last {@link Build}
	 * 
	 * @return the {@link Build}
	 */
	public Build getLastBuild() {
		return lastBuild;
	}

	/**
	 * Sets the last {@link Build}
	 * 
	 * @param lastBuild the last {@link Build}
	 */
	public void setLastBuild(Build lastBuild) {
		this.lastBuild = lastBuild;
	}

	/**
	 * Gets the Last Completed {@link Build}
	 * 
	 * @return the {@link Build}
	 */
	public Build getLastCompletedBuild() {
		return lastCompletedBuild;
	}

	/**
	 * Sets the last completed {@link Build}
	 * 
	 * @param lastCompletedBuild the last completed {@link Build}
	 */
	public void setLastCompletedBuild(Build lastCompletedBuild) {
		this.lastCompletedBuild = lastCompletedBuild;
	}

	/**
	 * Gets the Last Failed {@link Build}
	 * 
	 * @return the {@link Build}
	 */
	public Build getLastFailedBuild() {
		return lastFailedBuild;
	}

	/**
	 * Sets the last failed {@link Build}
	 * 
	 * @param lastFailedBuild the last failed {@link Build}
	 */
	public void setLastFailedBuild(Build lastFailedBuild) {
		this.lastFailedBuild = lastFailedBuild;
	}

	/**
	 * Gets the Last Stable {@link Build}
	 * 
	 * @return the {@link Build}
	 */
	public Build getLastStableBuild() {
		return lastStableBuild;
	}

	/**
	 * Sets the last stable {@link Build}
	 * 
	 * @param lastStableBuild the last stable {@link Build}
	 */
	public void setLastStableBuild(Build lastStableBuild) {
		this.lastStableBuild = lastStableBuild;
	}

	/**
	 * Gets the Last unstable {@link Build}
	 * 
	 * @return the {@link Build}
	 */
	public Build getLastUnstableBuild() {
		return lastUnstableBuild;
	}

	/**
	 * Sets the last unstable {@link Build}
	 * 
	 * @param lastUnstableBuild the last unstable {@link Build}
	 */
	public void setLastUnstableBuild(Build lastUnstableBuild) {
		this.lastUnstableBuild = lastUnstableBuild;
	}

	/**
	 * Gets the Last Successful {@link Build}
	 * 
	 * @return the {@link Build}
	 */
	public Build getLastSuccessfulBuild() {
		return lastSuccessfulBuild;
	}

	/**
	 * Sets the last successful {@link Build}
	 * 
	 * @param lastSuccessfulBuild the last successful {@link Build}
	 */
	public void setLastSuccessfulBuild(Build lastSuccessfulBuild) {
		this.lastSuccessfulBuild = lastSuccessfulBuild;
	}

	/**
	 * Gets the next build number
	 * 
	 * @return the build number
	 */
	public int getNextBuildNumber() {
		return nextBuildNumber;
	}

	/**
	 * Sets the next build number
	 * 
	 * @param nextBuildNumber the next build number
	 */
	public void setNextBuildNumber(int nextBuildNumber) {
		this.nextBuildNumber = nextBuildNumber;
	}

	/**
	 * Gets the {@link List} of {@link HealthReport}
	 * 
	 * @return the {@link List} of {@link HealthReport}
	 */
	public List<HealthReport> getHealthReports() {
		return healthReports;
	}

	/**
	 * Sets the {@link List} of {@link HealthReport}
	 * 
	 * @param healthReports the {@link List} of {@link HealthReport}
	 */
	public void setHealthReports(List<HealthReport> healthReports) {
		this.healthReports = healthReports;
	}

	/**
	 * Gets the {@link Result}
	 * 
	 * @return the {@link Result}
	 */
	public Result getResult() {
		return result;
	}

	/**
	 * Sets the build result
	 * 
	 * @param result the {@link Result}
	 */
	public void setResult(Result result) {
		this.result = result;
	}

	/**
	 * {@inheritDoc}
	 */
	public int compareTo(Job other) {
		return getName().compareTo(other.getName());
	}

}
