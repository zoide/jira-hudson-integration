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

import java.util.Collection;
import java.util.HashSet;

import org.ofbiz.core.entity.GenericValue;

import webwork.action.ActionContext;

import com.atlassian.jira.project.Project;
import com.marvelution.jira.plugins.hudson.service.HudsonServer;
import com.marvelution.jira.plugins.hudson.service.HudsonServerManager;

/**
 * {@link AbstractEditHudsonServer} implementation for updating project associations of HudsonServers
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class UpdateHudsonServerProjectAssociations extends AbstractHudsonWebActionSupport {

	private static final long serialVersionUID = 1L;

	private int hudsonServerId;

	private HudsonServer hudsonServer;

	private String[] availableProjectKeys;

	private String[] associatedProjectKeys;

	/**
	 * Constructor
	 * 
	 * @param hudsonServerManager the {@link HudsonServerManager} implementation
	 */
	public UpdateHudsonServerProjectAssociations(HudsonServerManager hudsonServerManager) {
		super(hudsonServerManager);
	}

	/**
	 * {@inheritDoc}
	 */
	public String doDefault() throws Exception {
		hudsonServer = hudsonServerManager.getServer(getHudsonServerId());
		if (hudsonServer == null) {
			return getRedirect("ViewHudsonServers.jspa");
		}
		return super.doDefault();
	}

	/**
	 * {@inheritDoc}
	 */
	public String doExecute() throws Exception {
		hudsonServer = hudsonServerManager.getServer(getHudsonServerId());
		if (buttonClicked("addButton")) {
			if (availableProjectKeys != null) {
				for (String key : availableProjectKeys) {
					hudsonServer.addAssociatedProjectKey(key);
					LOGGER.debug("Associated project key " + key + " with HudsonServer [" + getHudsonServerId() + "] "
						+ hudsonServer.getName());
				}
				hudsonServerManager.put(hudsonServer);
			}
		} else if (buttonClicked("addAllButton")) {
			for (Object entry : getProjectManager().getProjects()) {
				final GenericValue project = (GenericValue) entry;
				hudsonServer.addAssociatedProjectKey(project.getString("key"));
			}
			LOGGER.debug("Associated all projects with HudsonServer [" + getHudsonServerId() + "] "
				+ hudsonServer.getName());
			hudsonServerManager.put(hudsonServer);
		} else if (buttonClicked("removeButton")) {
			if (associatedProjectKeys != null) {
				for (String key : associatedProjectKeys) {
					hudsonServer.removeAssociatedProjectKey(key);
					LOGGER.debug("Removed associated project key " + key + " from HudsonServer ["
						+ getHudsonServerId() + "] " + hudsonServer.getName());
				}
				hudsonServerManager.put(hudsonServer);
			}
		} else if (buttonClicked("removeAllButton")) {
			hudsonServer.setAssociatedProjectKeys(new HashSet<String>());
			LOGGER.debug("Removed all associated projects from HudsonServer [" + getHudsonServerId() + "] "
				+ hudsonServer.getName());
			hudsonServerManager.put(hudsonServer);
		}
		return "input";
	}

	/**
	 * Checks if a button was clicked
	 * 
	 * @param buttonName the button name to check
	 * @return <code>true</code> if clicked, <code>false</code> otherwise
	 */
	private boolean buttonClicked(String buttonName) {
		return (ActionContext.getParameters().get(buttonName + ".x") != null);
	}

	/**
	 * Get the Hudson Server Id
	 * 
	 * @return the server Id
	 */
	public int getHudsonServerId() {
		return hudsonServerId;
	}

	/**
	 * Set the Hudson Server Id
	 * 
	 * @param hudsonServerId the server Id
	 */
	public void setHudsonServerId(int hudsonServerId) {
		this.hudsonServerId = hudsonServerId;
	}

	/**
	 * Get the Hudson Server Name
	 * 
	 * @return the server Name
	 */
	public String getName() {
		return hudsonServer.getName();
	}

	/**
	 * Gets the Associated Project Keys
	 * 
	 * @return the Associated Project Keys
	 */
	public String[] getAssociatedProjectKeys() {
		return associatedProjectKeys;
	}

	/**
	 * Sets the Associated Project Keys
	 * 
	 * @param associatedProjectKeys the Associated Project Keys
	 */
	public void setAssociatedProjectKeys(String[] associatedProjectKeys) {
		this.associatedProjectKeys = associatedProjectKeys;
	}

	/**
	 * Gets the Available Project Keys
	 * 
	 * @return the Available Project Keys
	 */
	public String[] getAvailableProjectKeys() {
		return availableProjectKeys;
	}

	/**
	 * Sets the Available Project Keys
	 * 
	 * @param availableProjectKeys the Available Project Keys
	 */
	public void setAvailableProjectKeys(String[] availableProjectKeys) {
		this.availableProjectKeys = availableProjectKeys;
	}

	/**
	 * Gets the Associated Projects {@link Collection}
	 * 
	 * @return the Associated Projects {@link Collection}
	 */
	public Collection<Project> getAssociatedProjects() {
		final Collection<Project> projects = new HashSet<Project>();
		for (String projectKey : hudsonServer.getAssociatedProjectKeys()) {
			projects.add(getProjectManager().getProjectObjByKey(projectKey));
		}
		return projects;
	}

	/**
	 * Gets all the available projects
	 * 
	 * @return the {@link Collection} of {@link Project} object
	 */
	public Collection<GenericValue> getAvailableProjects() {
		final Collection<GenericValue> projects = new HashSet<GenericValue>();
		for (Object entry : getProjectManager().getProjects()) {
			final GenericValue project = (GenericValue) entry;
			if (!hudsonServer.getAssociatedProjectKeys().contains(project.getString("key"))) {
				projects.add(project);
			}
		}
		return projects;
	}

}
