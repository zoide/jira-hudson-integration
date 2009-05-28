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

import org.apache.commons.lang.StringUtils;

import com.marvelution.jira.plugins.hudson.JiraApi;
import com.marvelution.jira.plugins.hudson.model.ApiImplementation;
import com.marvelution.jira.plugins.hudson.service.HudsonServer;
import com.marvelution.jira.plugins.hudson.service.HudsonServerAccessor;
import com.marvelution.jira.plugins.hudson.service.HudsonServerAccessorException;
import com.marvelution.jira.plugins.hudson.service.HudsonServerFactory;
import com.marvelution.jira.plugins.hudson.service.HudsonServerManager;

/**
 * Abstract {@link AbstractHudsonWebActionSupport} implementation for editing a {@link HudsonServer} 
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public abstract class AbstractEditHudsonServer extends AbstractHudsonWebActionSupport {

	private static final long serialVersionUID = 1L;

	protected final HudsonServerFactory hudsonServerFactory;

	protected final HudsonServerAccessor hudsonServerAccessor;

	protected HudsonServer hudsonServer;

	/**
	 * Constructor
	 * 
	 * @param hudsonServerManager the {@link HudsonServerManager} implementation
	 * @param hudsonServerFactory the {@link HudsonServerFactory} implementation
	 * @param hudsonServerAccessor the {@link HudsonServerAccessor} implementation
	 */
	public AbstractEditHudsonServer(HudsonServerManager hudsonServerManager, HudsonServerFactory hudsonServerFactory,
									HudsonServerAccessor hudsonServerAccessor) {
		super(hudsonServerManager);
		this.hudsonServerFactory = hudsonServerFactory;
		this.hudsonServerAccessor = hudsonServerAccessor;
		hudsonServer = hudsonServerFactory.createHudsonServer();
	}

	/**
	 * {@inheritDoc}
	 */
	public void doValidation() {
		if (StringUtils.isBlank(getName())) {
			addError("name", getText("hudson.config.serverName.required"));
		}
		if (StringUtils.isBlank(getHost())) {
			addError("host", getText("hudson.config.host.required"));
		} else if ((!(getHost().startsWith("http://"))) && (!(getHost().startsWith("https://")))) {
			addError("host", getText("hudson.config.host.invalid"));
		} else {
			try {
				final ApiImplementation version = hudsonServerAccessor.getApiVersion(hudsonServer);
				if (!JiraApi.getVersion().equals(version)) {
					addError("host", getText("hudson.config.host.incompatible.api.version", JiraApi.getVersion()));
				}
			} catch (HudsonServerAccessorException e) {
				addError("host", getText("hudson.config.host.connection.error", JiraApi.getVersion()));
			}
		}
	}

	/**
	 * Get the Server Id
	 * 
	 * @return the Server Id
	 */
	public int getId() {
		return hudsonServer.getServerId();
	}

	/**
	 * Set the Server Id
	 * 
	 * @param serverId the Server Id
	 */
	public void setId(int serverId) {
		hudsonServer.setServerId(serverId);
	}

	/**
	 * Gets the Server Name
	 * 
	 * @return the Server Name
	 */
	public String getName() {
		return hudsonServer.getName();
	}

	/**
	 * Sets the Server Name
	 * 
	 * @param name the Server Name
	 */
	public void setName(String name) {
		hudsonServer.setName(name);
	}

	/**
	 * Gets the Server Description
	 * 
	 * @return the Server Description
	 */
	public String getDescription() {
		return hudsonServer.getDescription();
	}

	/**
	 * Sets the Server Description
	 * 
	 * @param description the Server Description
	 */
	public void setDescription(String description) {
		hudsonServer.setDescription(description);
	}

	/**
	 * Gets the Server Host
	 * 
	 * @return the Server Host
	 */
	public String getHost() {
		return hudsonServer.getHost();
	}

	/**
	 * Sets the Server Host
	 * 
	 * @param host the Server Host
	 */
	public void setHost(String host) {
		if (host.endsWith("/")) {
			hudsonServer.setHost(host.substring(0, host.length() - 1));
		} else {
			hudsonServer.setHost(host);
		}
	}

	/**
	 * Gets the username
	 * 
	 * @return the username
	 */
	public String getUsername() {
		return hudsonServer.getUsername();
	}

	/**
	 * Sets the username
	 * 
	 * @param username the username
	 */
	public void setUsername(String username) {
		hudsonServer.setUsername(username);
	}

	/**
	 * Gets the password
	 * 
	 * @return the password
	 */
	public String getPassword() {
		return hudsonServer.getPassword();
	}

	/**
	 * Sets the password
	 * 
	 * @param password the password
	 */
	public void setPassword(String password) {
		hudsonServer.setPassword(password);
	}

}
