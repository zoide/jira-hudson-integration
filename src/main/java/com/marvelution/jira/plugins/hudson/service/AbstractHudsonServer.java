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

package com.marvelution.jira.plugins.hudson.service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

/**
 * Abstract implementation for the {@link HudsonServer} interface
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public abstract class AbstractHudsonServer implements HudsonServer, Comparable<HudsonServer> {

	private int serverId;

	private String name;

	private String description;

	private String host;

	private String username;

	private String password;

	private Set<String> associatedProjectKeys;

	/**
	 * Default Constructor
	 */
	public AbstractHudsonServer() {
		setServerId(0);
		setName("");
		setDescription("");
		setHost("");
		setUsername("");
		setPassword("");
		setAssociatedProjectKeys(new HashSet<String>());
	}

	/**
	 * Constructor
	 * 
	 * @param hudsonServer {@link HudsonServer} to copy data from
	 */
	public AbstractHudsonServer(HudsonServer hudsonServer) {
		setServerId(hudsonServer.getServerId());
		setName(hudsonServer.getName());
		setDescription(hudsonServer.getDescription());
		setHost(hudsonServer.getHost());
		setUsername(hudsonServer.getUsername());
		setPassword(hudsonServer.getPassword());
		setAssociatedProjectKeys(hudsonServer.getAssociatedProjectKeys());
	}

	/**
	 * {@inheritDoc}
	 */
	public int getServerId() {
		return serverId;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setServerId(int serverId) {
		this.serverId = serverId;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getName() {
		return name;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getHost() {
		return host;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * {@inheritDoc}
	 */
	public Collection<String> getAssociatedProjectKeys() {
		return associatedProjectKeys;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setAssociatedProjectKeys(Collection<String> projectKeys) {
		associatedProjectKeys = new HashSet<String>();
		associatedProjectKeys.addAll(projectKeys);
	}

	/**
	 * {@inheritDoc}
	 */
	public void removeAssociatedProjectKey(String projectKey) {
		if (associatedProjectKeys.contains(projectKey)) {
			associatedProjectKeys.remove(projectKey);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void addAssociatedProjectKey(String projectKey) {
		getAssociatedProjectKeys().add(projectKey);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isSecuredHudsonServer() {
		return (StringUtils.isNotEmpty(getUsername()) && StringUtils.isNotEmpty(getPassword()));
	}

	/**
	 * {@inheritDoc}
	 */
	public String getLargeImageUrl() {
		return getImageUrl(HudsonServer.LARGE_IMAGE_SIZE);
	}

	/**
	 * {@inheritDoc}
	 */
	public String getMediumImageUrl() {
		return getImageUrl(HudsonServer.MEDIUM_IMAGE_SIZE);
	}

	/**
	 * {@inheritDoc}
	 */
	public String getSmallImageUrl() {
		return getImageUrl(HudsonServer.SMALL_IMAGE_SIZE);
	}

	/**
	 * Gets the image url for a specific size
	 * 
	 * @param size the size of the image
	 * @return the image url (ends with '/')
	 */
	public String getImageUrl(int size) {
		return getHost() + "/images/" + size + 'x' + size + '/';
	}

}
