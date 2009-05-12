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

package com.marvelution.jira.web.action;

import org.apache.commons.lang.StringUtils;

import com.atlassian.jira.web.action.JiraWebActionSupport;
import com.marvelution.jira.plugins.hudson.service.HudsonServer;

/**
 * Jira WebAction to configure a Hudson server from the Administrator Control panel
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class HudsonConfiguration extends JiraWebActionSupport {

	private static final long serialVersionUID = 1L;

	private String serverName;

	private String host;

	private String username;

	private String password;

	private boolean viewMode;

	private HudsonServer hudsonServer;

	/**
	 * Constructor
	 * 
	 * @param hudsonServer the {@link HudsonServer}
	 */
	public HudsonConfiguration(HudsonServer hudsonServer) {
		this.hudsonServer = hudsonServer;
	}

	/**
	 * {@inheritDoc}
	 */
	public void doValidation() {
		if (StringUtils.isBlank(getServerName())) {
			addError("serverName", getText("hudson.serverName.required"));
		}
		if (StringUtils.isBlank(getHost())) {
			addError("host", getText("hudson.host.required"));
		} else if (!(getHost().startsWith("http://") || getHost().startsWith("https://"))) {
			addError("host", getText("hudson.host.invalid"));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public String doEdit() throws Exception {
		setServerName(hudsonServer.getServername());
		setHost(hudsonServer.getHost());
		setUsername(hudsonServer.getUsername());
		setPassword(hudsonServer.getPassword());
		setViewMode(false);
		return INPUT;
	}

	/**
	 * {@inheritDoc}
	 */
	public String doExecute() throws Exception {
		if (hasAnyErrors()) {
			setViewMode(false);
			return INPUT;
		} else {
			hudsonServer.setServername(getServerName());
			hudsonServer.setHost(getHost());
			hudsonServer.setUsername(getUsername());
			hudsonServer.setPassword(getPassword());
			setViewMode(true);
			return SUCCESS;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public String doView() throws Exception {
		if (isHudsonConfigured()) {
			setServerName(hudsonServer.getServername());
			setHost(hudsonServer.getHost());
			setUsername(hudsonServer.getUsername());
			setPassword(hudsonServer.getPassword());
			setViewMode(true);
			return SUCCESS;
		} else {
			setViewMode(false);
			return INPUT;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public String doDelete() throws Exception {
		hudsonServer.setServername(null);
		hudsonServer.setHost(null);
		hudsonServer.setUsername(null);
		hudsonServer.setPassword(null);
		setViewMode(false);
		return SUCCESS;
	}

	/**
	 * Check is a Hudson server is configured
	 * 
	 * @return <code>true</code> if a Hudson server is configured, <code>false</code> otherwise
	 */
	private boolean isHudsonConfigured() {
		return hudsonServer.isHudsonConfigured();
	}

	/**
	 * Gets the Hudson Server Name
	 * 
	 * @return the server name
	 */
	public String getServerName() {
		return serverName;
	}

	/**
	 * Sets the Hudson Server Name
	 * 
	 * @param serverName the server name
	 */
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	/**
	 * Gets the Hudson server host
	 * 
	 * @return the Hudson server host 
	 */
	public String getHost() {
		return host;
	}

	/**
	 * Sets the Hudson server host
	 * 
	 * @param host the Hudson host
	 */
	public void setHost(String host) {
		if (host.endsWith("/")) {
			host = host.substring(0, host.length() - 1);
		}
		this.host = host;
	}

	/**
	 * Gets the username to use to authenticate with
	 * 
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Sets the username to authenticate with
	 * 
	 * @param username the username
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * Gets the password for the username
	 * 
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Sets the password for the username
	 * 
	 * @param password the password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Gets the view mode
	 * 
	 * @return <code>true</code> for viewing, <code>false</code> for editing
	 */
	public boolean isViewMode() {
		return viewMode;
	}

	/**
	 * Sets the view mode
	 * 
	 * @param viewMode <code>true</code> for viewing, <code>false</code> for editing
	 */
	public void setViewMode(boolean viewMode) {
		this.viewMode = viewMode;
	}
}
