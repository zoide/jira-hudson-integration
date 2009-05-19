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

import java.util.ArrayList;
import java.util.List;

import com.marvelution.jira.plugins.hudson.xstream.converters.ResultConverter;
import com.marvelution.jira.plugins.hudson.xstream.converters.StateConverter;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

/**
 * Model class for Hudson Builds
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
@XStreamAlias("build")
public class Build implements HudsonServerAware, Comparable<Build> {

	@XStreamOmitField
	private int hudsonServerId;

	private int number;

	private String url;

	private String jobName;

	@XStreamImplicit(itemFieldName = "trigger")
	private List<String> triggers;

	@XStreamImplicit(itemFieldName = "relatedIssueKey")
	private List<String> relatedIssueKeys;

	@XStreamImplicit(itemFieldName = "artifact")
	private List<String> artifacts;

	@XStreamAlias("testResult")
	private TestResult testResult;

	@XStreamAlias("healthReport")
	private HealthReport healthReport = HealthReport.NO_RECENT_BUILDS;
	
	private long duration = 0L;
	
	private long timestamp = 0L;

	@XStreamConverter(ResultConverter.class)
	@XStreamAlias("result")
	private Result result;

	@XStreamConverter(StateConverter.class)
	@XStreamAlias("state")
	private State state;

	/**
	 * Constructor
	 * 
	 * @param number the build number
	 * @param jobName the Job name of this build
	 */
	public Build(int number, String jobName) {
		setNumber(number);
		setJobName(jobName);
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
	 * Gets the build number of this build
	 * 
	 * @return the build number
	 */
	public int getNumber() {
		return number;
	}

	/**
	 * Sets the build number
	 * 
	 * @param number the build number
	 */
	public void setNumber(int number) {
		this.number = number;
	}

	/**
	 * Gets the Build URL
	 * 
	 * @return the URL
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Sets the Build URL
	 * 
	 * @param url the URL
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * Gets the job name of this build
	 * 
	 * @return the job name of this build
	 */
	public String getJobName() {
		return jobName;
	}

	/**
	 * Sets the job name of this build
	 * 
	 * @param jobName the job name of this build
	 */
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	/**
	 * Gets the trigger messages of this build
	 * 
	 * @return the trigger messages
	 */
	public List<String> getTriggers() {
		if (triggers == null) {
			triggers = new ArrayList<String>();
		}
		return triggers;
	}

	/**
	 * Sets the trigger messages
	 * 
	 * @param triggers the trigger messages
	 */
	public void setTriggers(List<String> triggers) {
		this.triggers = triggers;
	}

	/**
	 * Gets the duration of the Build
	 * 
	 * @return the duration
	 */
	public long getDuration() {
		return duration;
	}

	/**
	 * Sets the duration of the Build
	 * 
	 * @param duration the duration of the build
	 */
	public void setDuration(long duration) {
		this.duration = duration;
	}

	/**
	 * Gets the timestamp when the Build executed
	 * 
	 * @return the timestamp
	 */
	public long getTimestamp() {
		return timestamp;
	}

	/**
	 * Sets the timestamp when the Build was executed
	 * 
	 * @param timestamp the timestamp
	 */
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
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
	 * Sets the {@link Result} for this build
	 * 
	 * @param result the {@link Result}
	 */
	public void setResult(Result result) {
		this.result = result;
	}

	/**
	 * Gets the Build {@link State}
	 * 
	 * @return the Build {@link State}
	 */
	public State getState() {
		return state;
	}

	/**
	 * Sets the Build {@link State}
	 * 
	 * @param state the Build {@link State}
	 */
	public void setState(State state) {
		this.state = state;
	}

	/**
	 * Gets the related Jira issue keys
	 * 
	 * @return the related Jira issue keys
	 */
	public List<String> getRelatedIssueKeys() {
		if (relatedIssueKeys == null) {
			relatedIssueKeys = new ArrayList<String>();
		}
		return relatedIssueKeys;
	}

	/**
	 * Sets the related Jira issue keys
	 * 
	 * @param relatedIssueKeys the related Jira issue keys
	 */
	public void setRelatedIssueKeys(List<String> relatedIssueKeys) {
		this.relatedIssueKeys = relatedIssueKeys;
	}

	/**
	 * Gets the artifacts resulting from the build
	 * 
	 * @return the artifacts
	 */
	public List<String> getArtifacts() {
		if (artifacts == null) {
			artifacts = new ArrayList<String>();
		}
		return artifacts;
	}

	/**
	 * Sets the artifacts resulting from the build
	 * 
	 * @param artifacts the artifacts
	 */
	public void setArtifacts(List<String> artifacts) {
		this.artifacts = artifacts;
	}

	/**
	 * Gets the {@link TestResult} of the build
	 * 
	 * @return the {@link TestResult}
	 */
	public TestResult getTestResult() {
		return testResult;
	}

	/**
	 * Sets the {@link TestResult} of the build
	 * 
	 * @param testResult {@link TestResult}
	 */
	public void setTestResult(TestResult testResult) {
		this.testResult = testResult;
	}

	/**
	 * Gets the {@link HealthReport} of the build
	 * 
	 * @return the {@link HealthReport}
	 */
	public HealthReport getHealthReport() {
		return healthReport;
	}

	/**
	 * Sets the {@link HealthReport} of the build
	 * 
	 * @param healthReport {@link HealthReport}
	 */
	public void setHealthReport(HealthReport healthReport) {
		this.healthReport = healthReport;
	}

	/**
	 * {@inheritDoc}
	 */
	public int compareTo(Build other) {
		return Long.valueOf(getTimestamp()).compareTo(Long.valueOf(other.getTimestamp()));
	}

}
