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

import com.marvelution.hudson.plugins.apiv2.client.HudsonClient;
import com.marvelution.hudson.plugins.apiv2.client.services.JobQuery;
import com.marvelution.jira.plugins.hudson.services.HudsonClientFactory;
import com.marvelution.jira.plugins.hudson.services.servers.HudsonServer;
import com.marvelution.jira.plugins.hudson.services.servers.HudsonServerFactory;
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
public abstract class AbstractModityServer extends AbstractHudsonAdminWebActionSupport {

	private static final long serialVersionUID = 1L;

	protected final HudsonServerFactory serverFactory;
	protected final HudsonClientFactory clientFactory;
	protected HudsonServer server;

	/**
	 * Constructor
	 * 
	 * @param serverManager the {@link HudsonServerManager} implementation
	 * @param serverFactory the {@link HudsonServerFactory} implementation
	 * @param clientFactory the {@link HudsonClientFactory} implementation
	 */
	protected AbstractModityServer(HudsonServerManager serverManager, HudsonServerFactory serverFactory,
			HudsonClientFactory clientFactory) {
		super(serverManager);
		this.serverFactory = serverFactory;
		this.clientFactory = clientFactory;
		server = serverFactory.create();
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
			HudsonClient client = clientFactory.create(server);
			if (client.findAll(JobQuery.createForJobList()) == null) {
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
		serverManager.addServer(server);
		return getRedirect(ADMINISTER_SERVERS);
	}

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
	 * Getter for sid
	 * 
	 * @return the sid
	 */
	public int getSid() {
		return server.getServerId();
	}

	/**
	 * Setter for sid
	 * 
	 * @param sid the sid to set
	 */
	public void setSid(int sid) {
		server.setServerId(sid);
	}

	/**
	 * Getter for name
	 * 
	 * @return the name
	 */
	public String getName() {
		return server.getName();
	}

	/**
	 * Setter for name
	 * 
	 * @param name the name to set
	 */
	public void setName(String name) {
		server.setName(name);
	}

	/**
	 * Getter for description
	 * 
	 * @return the description
	 */
	public String getDescription() {
		return server.getDescription();
	}

	/**
	 * Setter for description
	 * 
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		server.setDescription(description);
	}

	/**
	 * Getter for host
	 * 
	 * @return the host
	 */
	public String getHost() {
		return server.getHost();
	}

	/**
	 * Setter for host
	 * 
	 * @param host the host to set
	 */
	public void setHost(String host) {
		if (host.endsWith("/")) {
			server.setHost(host.substring(0, host.length() - 1));
		} else {
			server.setHost(host);
		}
	}

	/**
	 * Getter for public host
	 * 
	 * @return the public host
	 */
	public String getPublicHost() {
		return server.getPublicHost();
	}

	/**
	 * Setter for public host
	 * 
	 * @param publicH the public host to set
	 */
	public void setPublicHost(String publicHost) {
		if (publicHost.endsWith("/")) {
			server.setPublicHost(publicHost.substring(0, publicHost.length() - 1));
		} else {
			server.setPublicHost(publicHost);
		}
	}

	/**
	 * Getter for username
	 * 
	 * @return the username
	 */
	public String getUsername() {
		return server.getUsername();
	}

	/**
	 * Setter for username
	 * 
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		server.setUsername(username);
	}

	/**
	 * Getter for password
	 * 
	 * @return the password
	 */
	public String getPassword() {
		return server.getPassword();
	}

	/**
	 * Setter for password
	 * 
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		server.setPassword(password);
	}

}
