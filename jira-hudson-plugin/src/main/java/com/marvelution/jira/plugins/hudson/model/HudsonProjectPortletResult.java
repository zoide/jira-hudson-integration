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

import com.atlassian.jira.project.Project;
import com.marvelution.jira.plugins.hudson.service.HudsonServer;

/**
 * {@link HudsonStatusPortlet} Result model
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class HudsonProjectPortletResult extends HudsonServerResult<Job> {

	private Project project;

	/**
	 * Constructor
	 * 
	 * @param server the {@link HudsonServer}
	 * @param project the Jira {@link Project}
	 */
	public HudsonProjectPortletResult(HudsonServer server, Project project) {
		super(server);
		setProject(project);
	}

	/**
	 * Gets the {@link Job}
	 * 
	 * @return {@link Job}
	 */
	public Job getJob() {
		return getResults();
	}

	/**
	 * Check if the result has a {@link Job}
	 * 
	 * @return <code>true</code> if {@link #getJob()} returns an object other then <code>null</code>
	 */
	public boolean hasJob() {
		return getJob() != null;
	}

	/**
	 * Gets the {@link Job}
	 * 
	 * @param job the {@link Job}
	 */
	public void setJob(Job job) {
		setResults(job);
	}

	/**
	 * Gets the {@link Project}
	 * 
	 * @return the {@link Project}
	 */
	public Project getProject() {
		return project;
	}

	/**
	 * Sets the {@link Project}
	 * 
	 * @param project the {@link Project}
	 */
	public void setProject(Project project) {
		this.project = project;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getServerImageUrl() {
		return getServerLargeImageUrl();
	}

}
