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

import com.marvelution.jira.plugins.hudson.services.servers.HudsonServer;
import com.marvelution.jira.plugins.hudson.services.servers.HudsonServerManager;
import com.marvelution.jira.plugins.hudson.services.servers.HudsonServerUtils;
import com.marvelution.jira.plugins.hudson.web.action.admin.AbstractHudsonAdminWebActionSupport;

/**
 * {@link AbstractHudsonAdminWebActionSupport} implementation to administer Hudson server
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class AdministerServers extends AbstractHudsonAdminWebActionSupport {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 * 
	 * @param serverManager the {@link HudsonServerManager} implementation
	 */
	protected AdministerServers(HudsonServerManager serverManager) {
		super(serverManager);
	}

	/**
	 * Getter for all the {@link HudsonServer}s
	 * 
	 * @return {@link Collection} with all the configured {@link HudsonServer} objects
	 */
	public Collection<HudsonServer> getServers() {
		return serverManager.getServers();
	}

	/**
	 * Getter for the {@link HudsonServerUtils} helper
	 * 
	 * @return the {@link HudsonServerUtils} helper
	 */
	public HudsonServerUtils getServerUtils() {
		return new HudsonServerUtils();
	}

}
