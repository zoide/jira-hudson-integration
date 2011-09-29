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

import java.util.ArrayList;
import java.util.Collection;

import com.marvelution.jira.plugins.hudson.services.HudsonClientFactory;
import com.marvelution.jira.plugins.hudson.services.servers.HudsonServer;
import com.marvelution.jira.plugins.hudson.services.servers.HudsonServerFactory;
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

	/**
	 * Constructor
	 * 
	 * @param serverManager the {@link HudsonServerManager} implementation
	 * @param serverFactory the {@link HudsonServerFactory} implementation
	 * @param clientFactory the {@link HudsonClientFactory} implementation
	 */
	protected UpdateServer(HudsonServerManager serverManager, HudsonServerFactory serverFactory,
			HudsonClientFactory clientFactory) {
		super(serverManager, serverFactory, clientFactory);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String doDefault() throws Exception {
		if (!serverManager.hasServer(getSid())) {
			return getRedirect(ADMINISTER_SERVERS);
		}
		server = serverFactory.create(serverManager.getServer(getSid()));
		return super.doDefault();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doValidation() {
		super.doValidation();
		HudsonServer other = serverManager.getServer(getName());
		if (other != null && other.getServerId() != getSid()) {
			addError("name", getText("hudson.server.name.duplicate", getName()));
		}
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
		return new ArrayList<KeyValuePair>();
	}

}
