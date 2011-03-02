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

import com.marvelution.jira.plugins.hudson.services.servers.HudsonServer;

/**
 * Default {@link HudsonServer} implementation
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class DefaultHudsonServer implements HudsonServer, Comparable<HudsonServer> {

	private int serverId;
	private String name;
	private String description;
	private String host;
	private String publicHost;
	private String username;
	private String password;

	/**
	 * Default Constructor
	 */
	public DefaultHudsonServer() {
		setServerId(0);
	}

	/**
	 * Constructor
	 * 
	 * @param name the server name
	 * @param url the server base url
	 */
	public DefaultHudsonServer(String name, String url) {
		this();
		setName(name);
		setDescription("");
		setHost(url);
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
		setServerId(server.getServerId());
		setName(server.getName());
		setDescription(server.getDescription());
		setHost(server.getHost());
		setPublicHost(server.getPublicHost());
		setUsername(server.getUsername());
		setPassword(server.getPassword());
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
	public String getHost() {
		return host;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getPublicHost() {
		if (StringUtils.isNotBlank(publicHost)) {
			return publicHost;
		}
		return host;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setPublicHost(String publicHost) {
		if (publicHost != null && !publicHost.equals(host)) {
			this.publicHost = publicHost;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getUsername() {
		return username;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getPassword() {
		return password;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isSecured() {
		if (host == null) {
			return false;
		}
		return (StringUtils.isNotBlank(getUsername()) && StringUtils.isNotBlank(getPassword()));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof HudsonServer) {
			// TODO Write Equals
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
