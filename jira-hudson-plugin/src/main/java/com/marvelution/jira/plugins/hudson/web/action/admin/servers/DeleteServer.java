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

import com.marvelution.jira.plugins.hudson.services.servers.HudsonServer;
import com.marvelution.jira.plugins.hudson.services.servers.HudsonServerManager;
import com.marvelution.jira.plugins.hudson.web.action.admin.AbstractHudsonAdminWebActionSupport;

/**
 * Delete {@link HudsonServer} WebAction implementation
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class DeleteServer extends AbstractHudsonAdminWebActionSupport {

	private static final long serialVersionUID = 1L;

	private int serverId;
	private HudsonServer server;

	/**
	 * Constructor
	 * 
	 * @param serverManager the {@link HudsonServerManager} implementation
	 */
	protected DeleteServer(HudsonServerManager serverManager) {
		super(serverManager);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String doDefault() throws Exception {
		server = serverManager.getServer(serverId);
		return super.doDefault();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String doExecute() throws Exception {
		serverManager.removeServer(serverId);
		return getRedirect(ADMINISTER_SERVERS);
	}

	/**
	 * Getter for serverId
	 * 
	 * @return the serverId
	 */
	public int getSid() {
		return serverId;
	}

	/**
	 * Setter for server
	 * 
	 * @param server the server to set
	 */
	public void setSid(int sid) {
		serverId = sid;
	}

	/**
	 * Getter for server
	 * 
	 * @return the server
	 */
	public String getName() {
		return server.getName();
	}

}
