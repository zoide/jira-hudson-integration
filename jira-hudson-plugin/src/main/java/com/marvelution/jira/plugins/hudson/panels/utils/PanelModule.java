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

package com.marvelution.jira.plugins.hudson.panels.utils;

import com.atlassian.jira.bc.project.component.ProjectComponent;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.project.Project;
import com.atlassian.jira.project.version.Version;
import com.marvelution.jira.plugins.hudson.utils.JiraPluginUtils;

/**
 * Enum for all the different Panel Modules
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public enum PanelModule {

	PROJECT(Project.class, true),
	VERSION(Version.class, true),
	COMPONENT(ProjectComponent.class, false),
	ISSUE(Issue.class, false);

	private Class<?> objectType;
	private boolean supportsTabs;

	/**
	 * Constructor
	 */
	private PanelModule(Class<?> objectType, boolean supportsTabs) {
		this.objectType = objectType;
		this.supportsTabs = supportsTabs;
	}

	/**
	 * Getter for the function name of the PanelModule
	 * 
	 * @return the functional Name
	 */
	public String getName() {
		return name().toLowerCase();
	}

	/**
	 * Getter for the key of the PanelModule
	 * 
	 * @return the key
	 */
	public String getKey() {
		return "hudson-" + getName() + "-panel";
	}

	/**
	 * Getter for the full key of the PanelModule
	 * 
	 * @return the full key
	 */
	public String getFullKey() {
		return JiraPluginUtils.getPluginKey() + ":" + getKey();
	}

	/**
	 * Getter for supportsTabs
	 * 
	 * @return the supportsTabs
	 */
	public boolean supportsTabs() {
		return supportsTabs;
	}

	/**
	 * Getter for the Object Type {@link Class}
	 * 
	 * @return the {@link Class} type
	 */
	public Class<?> getObjectType() {
		return objectType;
	}

}
