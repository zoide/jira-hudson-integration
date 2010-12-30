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

package com.marvelution.hudson.plugins.apiv2.resources.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.marvelution.hudson.plugins.apiv2.resources.model.triggers.Trigger;
import com.marvelution.hudson.plugins.apiv2.resources.utils.NameSpaceUtils;

/**
 * Build XML object
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld<a/>
 */
@XmlType(name = "BuildType", namespace = NameSpaceUtils.BUILD_NAMESPACE)
@XmlRootElement(name = "Build", namespace = NameSpaceUtils.BUILD_NAMESPACE)
@XmlAccessorType(XmlAccessType.FIELD)
public class Build extends Model {

	@XmlElement(name = "number", required = true)
	private Integer buildNumber;
	@XmlElement(name = "url", required = true)
	private String url;
	@XmlElement(name = "jobName", required = true)
	private String jobName;
	@XmlElement(name = "jobUrl", required = true)
	private String jobUrl;
	@XmlElement(name = "duration", required = true)
	private long dureation;
	@XmlElement(name = "timestamp", required = true)
	private long timestamp;
	@XmlElement(name = "testResult")
	private TestResult testResult;
	@XmlElement(name = "result", required = true)
	private Result result;
	@XmlElement(name = "state", required = true)
	private State state;
	@XmlElement(name = "buildArtifact")
	private List<BuildArtifact> buildArtifacts;
	@XmlElement(name = "trigger", required = true)
	@XmlElementWrapper(name = "triggers")
	private List<Trigger> triggers;
	@XmlElement(name = "relatedIssueKey", required = true)
	@XmlElementWrapper(name = "relatedIssueKeys")
	private List<String> relatedIssueKeys;

	/**
	 * Default Constructor
	 */
	public Build() {
	}

	/**
	 * Constructor
	 * 
	 * @param buildNumber the Build Id
	 */
	public Build(int buildNumber) {
		setBuildNumber(buildNumber);
	}

	/**
	 * Getter for the buildNumber
	 * 
	 * @return the Build Number
	 */
	public int getBuildNumber() {
		return buildNumber;
	}

	public void setBuildNumber(int buildNumber) {
		setModelId("Build-" + String.valueOf(buildNumber));
		this.buildNumber = buildNumber;
	}

	/**
	 * Getter for url
	 *
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Setter for url
	 * 
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * Getter for jobName
	 *
	 * @return the jobName
	 */
	public String getJobName() {
		return jobName;
	}

	/**
	 * Setter for jobName
	 * 
	 * @param jobName the jobName to set
	 */
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	/**
	 * Getter for jobUrl
	 *
	 * @return the jobUrl
	 */
	public String getJobUrl() {
		return jobUrl;
	}

	/**
	 * Setter for jobUrl
	 * 
	 * @param jobUrl the jobUrl to set
	 */
	public void setJobUrl(String jobUrl) {
		this.jobUrl = jobUrl;
	}

	/**
	 * Getter for dureation
	 *
	 * @return the dureation
	 */
	public long getDureation() {
		return dureation;
	}

	/**
	 * Setter for dureation
	 * 
	 * @param dureation the dureation to set
	 */
	public void setDureation(long dureation) {
		this.dureation = dureation;
	}

	/**
	 * Getter for timestamp
	 *
	 * @return the timestamp
	 */
	public long getTimestamp() {
		return timestamp;
	}

	/**
	 * Setter for timestamp
	 * 
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * Getter for testResult
	 *
	 * @return the testResult
	 */
	public TestResult getTestResult() {
		return testResult;
	}

	/**
	 * Setter for testResult
	 * 
	 * @param testResult the testResult to set
	 */
	public void setTestResult(TestResult testResult) {
		this.testResult = testResult;
	}

	/**
	 * Getter for result
	 *
	 * @return the result
	 */
	public Result getResult() {
		return result;
	}

	/**
	 * Setter for result
	 * 
	 * @param result the result to set
	 */
	public void setResult(Result result) {
		this.result = result;
	}

	/**
	 * Getter for state
	 *
	 * @return the state
	 */
	public State getState() {
		return state;
	}

	/**
	 * Setter for state
	 * 
	 * @param state the state to set
	 */
	public void setState(State state) {
		this.state = state;
	}

	/**
	 * Getter for buildArtifacts
	 *
	 * @return the buildArtifacts
	 */
	public List<BuildArtifact> getBuildArtifacts() {
		return buildArtifacts;
	}

	/**
	 * Setter for buildArtifacts
	 * 
	 * @param buildArtifacts the buildArtifacts to set
	 */
	public void setBuildArtifacts(List<BuildArtifact> buildArtifacts) {
		this.buildArtifacts = buildArtifacts;
	}

	/**
	 * Getter for triggers
	 *
	 * @return the triggers
	 */
	public List<Trigger> getTriggers() {
		return triggers;
	}

	/**
	 * Setter for triggers
	 * 
	 * @param triggers the triggers to set
	 */
	public void setTriggers(List<Trigger> triggers) {
		this.triggers = triggers;
	}

	/**
	 * Getter for relatedIssueKeys
	 *
	 * @return the relatedIssueKeys
	 */
	public List<String> getRelatedIssueKeys() {
		return relatedIssueKeys;
	}

	/**
	 * Setter for relatedIssueKeys
	 * 
	 * @param relatedIssueKeys the relatedIssueKeys to set
	 */
	public void setRelatedIssueKeys(List<String> relatedIssueKeys) {
		this.relatedIssueKeys = relatedIssueKeys;
	}

}
