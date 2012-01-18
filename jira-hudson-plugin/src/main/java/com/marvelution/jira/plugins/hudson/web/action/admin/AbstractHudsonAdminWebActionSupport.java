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

package com.marvelution.jira.plugins.hudson.web.action.admin;

import org.apache.log4j.Logger;
import org.apache.velocity.tools.generic.SortTool;

import com.atlassian.jira.security.Permissions;
import com.marvelution.jira.plugins.hudson.services.servers.HudsonServerManager;
import com.marvelution.jira.plugins.hudson.web.action.HudsonWebActionSupport;

/**
 * Abstract {@link HudsonWebActionSupport} implementation for the Administrator actions of the Hudson plugin
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class AbstractHudsonAdminWebActionSupport extends HudsonWebActionSupport {

	private static final long serialVersionUID = 1L;
	
	private SortTool sorter;
	
	protected static final Logger LOGGER = Logger.getLogger(AbstractHudsonAdminWebActionSupport.class);
	protected static final String ADMINISTER_SERVERS = "AdministerServers.jspa";
	protected static final String ADMINISTER_ASSOCIATIONS= "AdministerAssociations.jspa?context=";
	
	protected final HudsonServerManager serverManager;

	/**
	 * Constructor
	 * 
	 * @param serverManager the {@link HudsonServerManager} implementation
	 */
	protected AbstractHudsonAdminWebActionSupport(HudsonServerManager serverManager) {
		this.serverManager = serverManager;
		this.sorter = new SortTool();
	}

	/**
	 * {@link Permissions#ADMINISTER} permissions checker for current user
	 * 
	 * @return <code>true</code> if the current user has the {@link Permissions#ADMINISTER} permissions,
	 *         <code>false</code> otherwise
	 */
	public boolean hasPermissions() {
		return getPermissionManager().hasPermission(Permissions.ADMINISTER, getLoggedInUser());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String doDefault() throws Exception {
		return hasPermissions() ? INPUT : PERMISSION_VIOLATION_RESULT;
	}

	/**
	 * Getter for the {@link SortTool}
	 * 
	 * @return the {@link SortTool}
	 */
	public SortTool getSorter() {
		return sorter;
	}

	/**
	 * Getter for the {@link HudsonServerManager}
	 * 
	 * @return the {@link HudsonServerManager} implementation
	 */
	public HudsonServerManager getServerManager() {
		return serverManager;
	}

}
