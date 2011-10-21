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

package com.marvelution.jira.plugins.hudson.web.action.admin.servers;

import java.util.Collection;

import org.apache.commons.lang.StringUtils;

import com.marvelution.hudson.plugins.apiv2.client.ClientException;
import com.marvelution.hudson.plugins.apiv2.client.Host;
import com.marvelution.hudson.plugins.apiv2.client.HudsonClient;
import com.marvelution.hudson.plugins.apiv2.client.services.VersionQuery;
import com.marvelution.jira.plugins.hudson.services.servers.HudsonClientFactory;
import com.marvelution.jira.plugins.hudson.services.servers.HudsonServerManager;
import com.marvelution.jira.plugins.hudson.web.action.admin.AbstractHudsonAdminWebActionSupport;
import com.marvelution.jira.plugins.hudson.web.action.admin.KeyValuePair;
import com.marvelution.jira.plugins.hudson.web.action.admin.ModifyActionType;

/**
 * Abstract Modify Server WebAction
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
@SuppressWarnings("unchecked")
public abstract class AbstractModifyServer extends AbstractHudsonAdminWebActionSupport {

	private static final long serialVersionUID = 1L;

	private String name;
	private String description;
	private String host;
	private String publicHost;
	private String username;
	private String password;
	private boolean includeInStreams;

	protected final HudsonClientFactory clientFactory;

	/**
	 * Constructor
	 * 
	 * @param serverManager the {@link HudsonServerManager} implementation
	 * @param clientFactory the {@link HudsonClientFactory} implementation
	 */
	protected AbstractModifyServer(HudsonServerManager serverManager, HudsonClientFactory clientFactory) {
		super(serverManager);
		this.clientFactory = clientFactory;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doValidation() {
		if (StringUtils.isBlank(getName())) {
			addError("name", getText("hudson.server.name.required"));
		}
		if (StringUtils.isBlank(getHost())) {
			addError("host", getText("hudson.server.host.required"));
		} else if ((!getHost().startsWith("http://")) && (!getHost().startsWith("https://"))) {
			addError("host", getText("hudson.server.host.invalid"));
		} else {
			HudsonClient client = clientFactory.create(new Host(getHost(), getUsername(), getPassword()));
			try {
				if (client.find(VersionQuery.createForPluginVersion()) == null) {
					addError("host", getText("hudson.server.host.apiv2.failed"));
				}
			} catch (ClientException e) {
				addError("host", getText("hudson.server.host.apiv2.failed"));
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String doExecute() throws Exception {
		if (hasAnyErrors()) {
			return INPUT;
		}
		saveServer(getName(), getDescription(), getHost(), getPublicHost(), getUsername(), getPassword(),
			isIncludeInStreams());
		return getRedirect(ADMINISTER_SERVERS);
	}

	/**
	 * Internal method to save the HudsonServer data
	 * 
	 * @param name the name of the server
	 * @param description the description of the server
	 * @param host the base private host url
	 * @param publicHost the base public host url
	 * @param username the username for secured instances
	 * @param password the password for secured instances
	 * @param includeInStreams flag whether the server is includable in the activity streams
	 */
	protected abstract void saveServer(String name, String description, String host, String publicHost,
			String username, String password, boolean includeInStreams);

	/**
	 * Getter for the action type eg: Add/Update
	 * 
	 * @return the {@link ModifyActionType} action
	 */
	public abstract ModifyActionType getActionType();

	/**
	 * Getter for a {@link Collection} of {@link KeyValuePair} objects to be added to the form in hidden inputs
	 * 
	 * @return the {@link Collection} of {@link KeyValuePair} objects
	 */
	public abstract Collection<KeyValuePair> getExtraHiddenInput();

	/**
	 * Getter for name
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Setter for name
	 * 
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Getter for description
	 * 
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Setter for description
	 * 
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Getter for host
	 * 
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * Setter for host
	 * 
	 * @param host the host to set
	 */
	public void setHost(String host) {
		if (host.endsWith("/")) {
			this.host = host.substring(0, host.length() - 1);
		} else {
			this.host = host;
		}
	}

	/**
	 * Getter for public host
	 * 
	 * @return the public host
	 */
	public String getPublicHost() {
		return publicHost;
	}

	/**
	 * Setter for public host
	 * 
	 * @param publicH the public host to set
	 */
	public void setPublicHost(String publicHost) {
		if (publicHost.endsWith("/")) {
			this.publicHost = publicHost.substring(0, publicHost.length() - 1);
		} else {
			this.publicHost = publicHost;
		}
	}

	/**
	 * Getter for username
	 * 
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Setter for username
	 * 
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Getter for password
	 * 
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Setter for password
	 * 
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Getter for includeInStreams
	 *
	 * @return the includeInStreams
	 */
	public boolean isIncludeInStreams() {
		return includeInStreams;
	}

	/**
	 * Setter for includeInStreams
	 *
	 * @param includeInStreams the includeInStreams to set
	 */
	public void setIncludeInStreams(boolean includeInStreams) {
		this.includeInStreams = includeInStreams;
	}

}
