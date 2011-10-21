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

import com.google.common.collect.Lists;
import com.marvelution.jira.plugins.hudson.services.servers.HudsonClientFactory;
import com.marvelution.jira.plugins.hudson.services.servers.HudsonServer;
import com.marvelution.jira.plugins.hudson.services.servers.HudsonServerManager;
import com.marvelution.jira.plugins.hudson.web.action.admin.KeyValuePair;
import com.marvelution.jira.plugins.hudson.web.action.admin.ModifyActionType;

/**
 * Update {@link HudsonServer} Web Action implementation
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
@SuppressWarnings("unchecked")
public class UpdateServer extends AbstractModifyServer {

	private static final long serialVersionUID = 1L;

	private HudsonServer server;
	private int sid;
	private boolean defaultServer;

	/**
	 * Constructor
	 * 
	 * @param serverManager the {@link HudsonServerManager} implementation
	 * @param clientFactory the {@link HudsonClientFactory} implementation
	 */
	protected UpdateServer(HudsonServerManager serverManager, HudsonClientFactory clientFactory) {
		super(serverManager, clientFactory);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String doDefault() throws Exception {
		if (!serverManager.hasServer(getSid())) {
			return getRedirect(ADMINISTER_SERVERS);
		}
		server = serverManager.getServer(getSid());
		defaultServer = server.isDefaultServer();
		return super.doDefault();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doValidation() {
		super.doValidation();
		HudsonServer other = serverManager.getServer(getName());
		if (other != null && other.getID() != getSid()) {
			addError("name", getText("hudson.server.name.duplicate", getName()));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveServer(String name, String description, String host, String publicHost, String username,
				String password, boolean includeInStreams) {
		serverManager.updateServer(getSid(), name, description, host, publicHost, username, password,
			includeInStreams, isDefaultServer());
	}

	/**
	 * Action support web method to set a {@link HudsonServer} as default server
	 * 
	 * @return the return URL {@link String}
	 */
	public String doSetAsDefault() {
		if (serverManager.hasServer(getSid())) {
			serverManager.setDefaultServer(getSid());
		}
		return getRedirect(ADMINISTER_SERVERS);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ModifyActionType getActionType() {
		return ModifyActionType.UPDATE_SERVER;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<KeyValuePair> getExtraHiddenInput() {
		return Lists.newArrayList(new KeyValuePair("defaultServer", String.valueOf(isDefaultServer())));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getSid() {
		return sid;
	}

	/**
	 * Setter for sid
	 *
	 * @param sid the sid to set
	 */
	public void setSid(int sid) {
		this.sid = sid;
	}

	
	/**
	 * Getter for defaultServer
	 *
	 * @return the defaultServer
	 */
	public boolean isDefaultServer() {
		return defaultServer;
	}

	
	/**
	 * Setter for defaultServer
	 *
	 * @param defaultServer the defaultServer to set
	 */
	public void setDefaultServer(boolean defaultServer) {
		this.defaultServer = defaultServer;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		if (StringUtils.isNotBlank(super.getName())) {
			return super.getName();
		}
		return server.getName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDescription() {
		if (StringUtils.isNotBlank(super.getDescription())) {
			return super.getDescription();
		}
		return server.getDescription();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getHost() {
		if (StringUtils.isNotBlank(super.getHost())) {
			return super.getHost();
		}
		return server.getHost();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getPublicHost() {
		if (StringUtils.isNotBlank(super.getPublicHost())) {
			return super.getPublicHost();
		}
		return server.getPublicHost();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getUsername() {
		if (StringUtils.isNotBlank(super.getUsername())) {
			return super.getUsername();
		}
		return server.getUsername();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getPassword() {
		if (StringUtils.isNotBlank(super.getPassword())) {
			return super.getPassword();
		}
		return server.getPassword();
	}

}
