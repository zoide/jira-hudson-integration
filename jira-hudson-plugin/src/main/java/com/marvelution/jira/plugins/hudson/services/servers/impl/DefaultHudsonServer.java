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

package com.marvelution.jira.plugins.hudson.services.servers.impl;

import org.apache.commons.lang.StringUtils;

import com.marvelution.hudson.plugins.apiv2.client.Host;
import com.marvelution.jira.plugins.hudson.services.servers.HudsonServer;

/**
 * Default {@link HudsonServer} implementation
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class DefaultHudsonServer extends Host implements HudsonServer, Comparable<HudsonServer> {

	private int serverId = 0;
	private String name;
	private String description;
	private String publicHost;

	/**
	 * Default Constructor
	 */
	public DefaultHudsonServer() {
		super("");
	}

	/**
	 * Constructor
	 * 
	 * @param name the server name
	 * @param url the server base url
	 */
	public DefaultHudsonServer(String name, String url) {
		super(url);
		setName(name);
		setDescription("");
	}

	/**
	 * Constructor
	 * 
	 * @param name the server name
	 * @param url the server base url
	 * @param username the server username
	 * @param password the server password
	 */
	public DefaultHudsonServer(String name, String url, String username, String password) {
		this(name, url);
		setUsername(username);
		setPassword(password);
	}

	/**
	 * Constructor
	 * 
	 * @param name the server name
	 * @param description the server description
	 * @param url the server base url
	 * @param username the server username
	 * @param password the server password
	 */
	public DefaultHudsonServer(String name, String description, String url, String username, String password) {
		this(name, url, username, password);
		setDescription(description);
	}

	/**
	 * Constructor
	 * 
	 * @param server a {@link HudsonServer} to use as a base to copy the fields from
	 */
	public DefaultHudsonServer(HudsonServer server) {
		super(server.getHost(), server.getUsername(), server.getPassword());
		setServerId(server.getServerId());
		setName(server.getName());
		setDescription(server.getDescription());
		setPublicHost(server.getPublicHost());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getServerId() {
		return serverId;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setServerId(int serverId) {
		this.serverId = serverId;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDescription() {
		return description;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getHostWhitelistUrl() {
		if (getHost().endsWith("/")) {
			return getHost() + "*";
		} else {
			return getHost() + "/*";
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getPublicHostWhitelistUrl() {
		if (getPublicHost().endsWith("/")) {
			return getPublicHost() + "*";
		} else {
			return getPublicHost() + "/*";
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getPublicHost() {
		if (StringUtils.isNotBlank(publicHost)) {
			return publicHost;
		}
		return getHost();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setPublicHost(String publicHost) {
		if (publicHost != null && !publicHost.equals(getHost())) {
			this.publicHost = publicHost;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof HudsonServer) {
			HudsonServer other = (HudsonServer) obj;
			if (other.getServerId() > 0 && getServerId() > 0) {
				// ID's are set, compare using those
				if (getServerId() == other.getServerId()) {
					return true;
				} else {
					return false;
				}
			}
			// TODO Implement other equals?
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return getServerId() + getName().hashCode() + getDescription().hashCode();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "[HudsonServer: " + getServerId() + "; name: " + getName() + "; host: " + getHost() + "]";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int compareTo(HudsonServer otherServer) {
		if (otherServer == null) {
			throw new IllegalArgumentException("otherServer may not be null");
		}
		return getName().compareTo(otherServer.getName());
	}

}
