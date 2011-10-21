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

package com.marvelution.jira.plugins.hudson.services.servers;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.java.ao.Query;

import org.apache.log4j.Logger;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.jira.bc.whitelist.WhitelistManager;
import com.google.common.collect.Lists;

/**
 * Default {@link HudsonServerManager} implementation
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class HudsonServerManagerService implements HudsonServerManager {

	private static final Logger LOGGER = Logger.getLogger(HudsonServerManagerService.class);

	private final ActiveObjects objects;
	private final WhitelistManager whitelistManager;

	/**
	 * Constructor
	 * 
	 * 
	 * @param propertyManager the {@link HudsonPropertyManager} implementation
	 * @param serverFactory the {@link HudsonServerFactory} implementation
	 * @param whitelistManager the {@link WhitelistManager} implementation
	 */
	public HudsonServerManagerService(ActiveObjects objects, WhitelistManager whitelistManager) {
		this.objects = checkNotNull(objects, "activeObjects");
		this.whitelistManager = checkNotNull(whitelistManager, "whitelistManager");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isConfigured() {
		return hasServers();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasServers() {
		return objects.count(HudsonServer.class) > 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasServer(int serverId) {
		return getServer(serverId) != null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasServer(String name) {
		return getServer(name) != null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<HudsonServer> getServers() {
		return Lists.newArrayList(objects.find(HudsonServer.class));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HudsonServer getServer(int serverId) {
		return objects.get(HudsonServer.class, serverId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HudsonServer getServer(String name) {
		HudsonServer[] matches = objects.find(HudsonServer.class, Query.select().where("NAME = ?", name).limit(1));
		if (matches != null && matches.length == 1) {
			return matches[0];
		} else {
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HudsonServer getDefaultServer() {
		HudsonServer[] matches = objects.find(HudsonServer.class, Query.select().where("DEFAULT_SERVER = ?", true)
			.limit(1));
		if (matches != null && matches.length == 1) {
			return matches[0];
		} else {
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDefaultServer(int serverId) {
		setDefaultServer(getServer(serverId));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDefaultServer(HudsonServer server) {
		if (server != null && !server.isDefaultServer()) {
			HudsonServer currentDefault = getDefaultServer();
			if (currentDefault != null) {
				currentDefault.setDefaultServer(false);
				currentDefault.save();
			}
			server.setDefaultServer(true);
			server.save();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isDefaultServer(HudsonServer server) {
		if (server == null) {
			return false;
		}
		return server.isDefaultServer();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HudsonServer addServer(String name, String description, String host) {
		return addServer(name, description, host, host);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HudsonServer addServer(String name, String description, String host, String publicHost) {
		return addServer(name, description, host, publicHost, null, null, true, false);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HudsonServer addServer(String name, String description, String host, String username, String password) {
		return addServer(name, description, host, host, username, password, true, false);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HudsonServer addServer(String name, String description, String host, String publicHost, String username,
					String password, boolean includeInStreams, boolean isDefault) {
		HudsonServer server = objects.create(HudsonServer.class);
		server.setName(name);
		server.setHost(host);
		server.setPublicHost(publicHost);
		server.setDescription(description);
		server.setUsername(username);
		server.setPassword(password);
		server.setDefaultServer(isDefault);
		server.setIncludeInStreams(includeInStreams);
		server.save();
		List<String> rules = whitelistManager.getRules();
		if (!rules.contains(HudsonServerUtils.getHostWhitelistUrl(server))) {
			List<String> newRules = new ArrayList<String>(rules);
			newRules.add(HudsonServerUtils.getHostWhitelistUrl(server));
			if (!rules.contains(HudsonServerUtils.getPublicHostWhitelistUrl(server))) {
				newRules.add(HudsonServerUtils.getPublicHostWhitelistUrl(server));
			}
			whitelistManager.updateRules(newRules, whitelistManager.isDisabled());
		}
		return server;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HudsonServer addServer(HudsonServer server) {
		checkNotNull(server, "server argument may NOT be null");
		return addServer(server.getName(), server.getDescription(), server.getHost(), server.getPublicHost(),
			server.getUsername(), server.getPassword(), true, false);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HudsonServer updateServer(int serverId, String name, String description, String host, String publicHost,
					String username, String password, boolean includeInStreams, boolean isDefault) {
		HudsonServer server = getServer(serverId);
		checkNotNull(server, "server");
		server.setName(name);
		server.setDescription(description);
		server.setHost(host);
		server.setPublicHost(publicHost);
		server.setUsername(username);
		server.setPassword(password);
		server.setDefaultServer(isDefault);
		server.setIncludeInStreams(includeInStreams);
		server.save();
		return server;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HudsonServer updateServer(HudsonServer server) {
		checkNotNull(server, "server argument may NOT be null");
		// This one is easy, just save and return
		server.save();
		return server;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeServer(int serverId) {
		HudsonServer server = getServer(serverId);
		checkNotNull(server, "No HudsonServer configured with Id: " + serverId);
		removeServer(server);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeServer(HudsonServer server) {
		checkNotNull(server, "hudsonServer");
		List<String> rules = whitelistManager.getRules();
		if (rules.contains(HudsonServerUtils.getHostWhitelistUrl(server))) {
			List<String> newRules = new ArrayList<String>(rules);
			newRules.remove(HudsonServerUtils.getHostWhitelistUrl(server));
			if (rules.contains(HudsonServerUtils.getPublicHostWhitelistUrl(server))) {
				newRules.remove(HudsonServerUtils.getPublicHostWhitelistUrl(server));
			}
			whitelistManager.updateRules(newRules, whitelistManager.isDisabled());
		}
		if (isDefaultServer(server) && objects.count(HudsonServer.class) > 1) {
			LOGGER.info("Deleted the Default HudsonServer, making sure another will be set as default");
			setDefaultServer(objects.find(HudsonServer.class)[0]);
		}
		objects.delete(server);
	}

}
