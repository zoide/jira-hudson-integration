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

package com.marvelution.jira.plugins.hudson.web.action;

import com.atlassian.jira.project.Project;
import com.atlassian.jira.project.ProjectManager;
import com.marvelution.jira.plugins.hudson.service.HudsonServer;
import com.marvelution.jira.plugins.hudson.service.HudsonServerManager;

/**
 * {@link AbstractEditHudsonServer} implementation for associating a given Project to the specific {@link HudsonServer}
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class AssociateSpecificHudsonServer extends AbstractHudsonWebActionSupport {

	private static final long serialVersionUID = 1L;

	private final ProjectManager projectManager;

	private int hudsonServerId = -1;

	private String projectKey;

	/**
	 * Constructor
	 * 
	 * @param projectManager the {@link ProjectManager} implementation
	 * @param hudsonServerManager the {@link HudsonServerManager} implementation
	 */
	public AssociateSpecificHudsonServer(ProjectManager projectManager, HudsonServerManager hudsonServerManager) {
		super(hudsonServerManager);
		this.projectManager = projectManager;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doValidation() {
		super.doValidation();
		if (hudsonServerId != -1 && (hudsonServerId < 1 || hudsonServerManager.getServer(hudsonServerId) == null)) {
			addErrorMessage(getText("hudson.config.error.hudson.server.required"));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String doDefault() throws Exception {
		final Project project = getProject();
		if (project == null) {
			return getRedirect("/secure/BrowseProject.jspa");
		}
		return super.doDefault();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String doExecute() throws Exception {
		if (hasAnyErrors()) {
			return "input";
		}
		final HudsonServer server = hudsonServerManager.getServer(hudsonServerId);
		server.addAssociatedProjectKey(projectKey);
		hudsonServerManager.put(server);
		return getRedirect("/secure/BrowseProject.jspa");
	}

	/**
	 * Gets the {@link HudsonServerManager} implementation
	 * 
	 * @return the {@link HudsonServerManager} implementation
	 */
	public HudsonServerManager getServerManager() {
		return hudsonServerManager;
	}

	/**
	 * Gets the current associated Hudson server id
	 * 
	 * @return the id of the current associated Hudson server
	 */
	public int getCurrentAssociatedHudsonServerId() {
		return hudsonServerManager.getServerByJiraProject(getProject()).getServerId();
	}

	/**
	 * Gets the project selected for association with a {@link HudsonServer}
	 * 
	 * @return the {@link Project} selected for association
	 */
	public Project getProject() {
		return projectManager.getProjectObjByKey(projectKey);
	}

	/**
	 * Gets the hudson server id
	 * 
	 * @return the hudson server id
	 */
	public int getHudsonServerId() {
		return hudsonServerId;
	}

	/**
	 * Sets the hudson server id
	 * 
	 * @param hudsonServerId the hudson server id
	 */
	public void setHudsonServerId(int hudsonServerId) {
		this.hudsonServerId = hudsonServerId;
	}

	/**
	 * Gets the project key
	 * 
	 * @return the project key
	 */
	public String getProjectKey() {
		return projectKey;
	}

	/**
	 * Sets the project key
	 * 
	 * @param projectKey the project key
	 */
	public void setProjectKey(String projectKey) {
		this.projectKey = projectKey;
	}

}
