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

import java.util.Arrays;

import org.apache.commons.lang.StringUtils;

import com.atlassian.jira.security.PermissionManager;
import com.marvelution.jira.plugins.hudson.service.HudsonServer;
import com.marvelution.jira.plugins.hudson.service.HudsonServerAccessor;
import com.marvelution.jira.plugins.hudson.service.HudsonServerFactory;
import com.marvelution.jira.plugins.hudson.service.HudsonServerManager;

/**
 * {@link AbstractEditHudsonServer} implementation for updating HudsonServers
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class UpdateHudsonServer extends AbstractEditHudsonServer {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 * 
	 * @param permissionManager the {@link PermissionManager} implementation
	 * @param hudsonServerManager the {@link HudsonServerManager} implementation
	 * @param hudsonServerFactory the {@link HudsonServerFactory} implementation
	 * @param hudsonServerAccessor the {@link HudsonServerAccessor} implementation
	 */
	public UpdateHudsonServer(PermissionManager permissionManager, HudsonServerManager hudsonServerManager,
								HudsonServerFactory hudsonServerFactory, HudsonServerAccessor hudsonServerAccessor) {
		super(permissionManager, hudsonServerManager, hudsonServerFactory, hudsonServerAccessor);
	}

	/**
	 * {@inheritDoc}
	 */
	public void doValidation() {
		super.doValidation();
		final HudsonServer server = hudsonServerManager.getServer(getName());
		if ((server != null) && (server.getServerId() != getHudsonServerId())) {
			addError("name", getText("hudson.config.serverName.duplicate"));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public String doDefault() throws Exception {
		final HudsonServer serverToEdit = hudsonServerManager.getServer(getHudsonServerId());
		if (serverToEdit == null) {
			return getRedirect("ViewHudsonServers.jspa");
		}
		hudsonServer = hudsonServerFactory.createHudsonServer(serverToEdit);
		return super.doDefault();
	}

	/**
	 * {@inheritDoc}
	 */
	public String doExecute() throws Exception {
		if (hasAnyErrors()) {
			return "input";
		}
		hudsonServerManager.put(hudsonServer);
		return getRedirect("ViewHudsonServers.jspa");
	}

	/**
	 * Execute Set As Default operation
	 * 
	 * @return redirect url
	 * @throws Exception in case of errors
	 */
	public String doSetAsDefault() throws Exception {
		final HudsonServer defaultServer = hudsonServerManager.getServer(getHudsonServerId());
		if (defaultServer != null) {
			hudsonServerManager.setDefaultServer(defaultServer);
		}
		return getRedirect("ViewHudsonServers.jspa");
	}

	/**
	 * Execute Update Crumb operation
	 * 
	 * @return redirect url
	 * @throws Exception in case of errors
	 */
	public String doUpdateCrumb() throws Exception {
		final HudsonServer server = hudsonServerManager.getServer(getHudsonServerId());
		if (server != null) {
			hudsonServerAccessor.getCrumb(server);
		}
		return getRedirect("ViewHudsonServers.jspa");
	}

	/**
	 * Gets the associated project keys
	 * 
	 * @return the associated project keys
	 */
	public String getAssociatedProjectKeys() {
		return StringUtils.join(hudsonServer.getAssociatedProjectKeys(), ',');
	}

	/**
	 * Sets the associated project keys
	 * 
	 * @param projectKeys the associated project keys
	 */
	public void setAssociatedProjectKeys(String projectKeys) {
		hudsonServer.setAssociatedProjectKeys(Arrays.asList(projectKeys.split(",")));
	}

}
