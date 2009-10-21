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

package com.marvelution.jira.plugins.hudson.api.model.triggers;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Project {@link Trigger}. {@link Trigger} that indicates that another project triggered the build
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
@XStreamAlias("projectTrigger")
public class ProjectTrigger implements Trigger {

	private String projectName;

	private String projectUrl;

	private String projectKey;

	private Integer buildNumber;

	/**
	 * Default no-argument constructor for XStream
	 */
	public ProjectTrigger() {
	}

	/**
	 * Constructor
	 * 
	 * @param projectName the project name
	 * @param projectUrl the project url
	 * @param buildNumber the project build number
	 */
	public ProjectTrigger(String projectName, String projectUrl, Integer buildNumber) {
		setProjectName(projectName);
		setProjectUrl(projectUrl);
		setBuildNumber(buildNumber);
	}

	/**
	 * Gets the project name
	 * 
	 * @return the project name
	 */
	public String getProjectName() {
		return projectName;
	}

	/**
	 * Sets the project name
	 * 
	 * @param projectName the project name
	 */
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	/**
	 * Gets the project url
	 * 
	 * @return the project url
	 */
	public String getProjectUrl() {
		return projectUrl;
	}

	/**
	 * Sets the project url
	 * 
	 * @param projectUrl the project url
	 */
	public void setProjectUrl(String projectUrl) {
		this.projectUrl = projectUrl;
	}

	/**
	 * Gets the project Jira key
	 * 
	 * @return the project Jira key
	 */
	public String getProjectKey() {
		return projectKey;
	}

	/**
	 * Sets the project Jira key
	 * 
	 * @param prjectKey the project Jira key
	 */
	public void setProjectKey(String prjectKey) {
		this.projectKey = prjectKey;
	}

	/**
	 * Gets the project build number
	 * 
	 * @return the project build number
	 */
	public Integer getBuildNumber() {
		return buildNumber;
	}

	/**
	 * Sets the project build number
	 * 
	 * @param buildNumber the project build number
	 */
	public void setBuildNumber(Integer buildNumber) {
		this.buildNumber = buildNumber;
	}

}
