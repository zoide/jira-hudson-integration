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

/**
 * Interface that represents a Hudson Server configured in JIRA
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public interface HudsonServer {

	int SMALL_IMAGE_SIZE = 16;

	int MEDIUM_IMAGE_SIZE = 24;

	int LARGE_IMAGE_SIZE = 32;

	/**
	 * Gets the Id of the Hudson Server
	 * 
	 * @return the id of the Hudson Server
	 */
	int getServerId();

	/**
	 * Sets the Id of the Hudson Server
	 * 
	 * @param serverId of the Hudson Server
	 */
	void setServerId(int serverId);

	/**
	 * Gets the Hudson Server Name
	 * 
	 * @return the server name
	 */
	String getName();

	/**
	 * Sets the Hudson Server Name
	 * 
	 * @param serverName the server name
	 */
	void setName(String serverName);

	/**
	 * Gets the Hudson Server Description
	 * 
	 * @return the server description
	 */
	String getDescription();

	/**
	 * Sets the Hudson Server Description
	 * 
	 * @param description the server description
	 */
	void setDescription(String description);

	/**
	 * Gets the Hudson server host
	 * 
	 * @return the Hudson server host 
	 */
	String getHost();

	/**
	 * Sets the Hudson server host
	 * 
	 * @param host the Hudson host
	 */
	void setHost(String host);

	/**
	 * Gets the username to use to authenticate with
	 * 
	 * @return the username
	 */
	String getUsername();

	/**
	 * Sets the username to authenticate with
	 * 
	 * @param username the username
	 */
	void setUsername(String username);

	/**
	 * Gets the password for the username
	 * 
	 * @return the password
	 * @see HudsonServer#getUsername()
	 */
	String getPassword();

	/**
	 * Sets the password for the username
	 * 
	 * @param password the password
	 * @see #setUsername(String)
	 */
	void setPassword(String password);

	/**
	 * Gets all the associated project keys
	 * 
	 * @return {@link Collection} of {@link String} project keys
	 */
	Collection<String> getAssociatedProjectKeys();

	/**
	 * Sets all the associated project keys
	 * 
	 * @param projectKeys the {@link Collection} of {@link String} project keys
	 */
	void setAssociatedProjectKeys(Collection<String> projectKeys);

	/**
	 * Adds a project key to the project association {@link Collection}
	 * 
	 * @param projectKey the {@link String} project key to add
	 */
	void addAssociatedProjectKey(String projectKey);

	/**
	 * Removes a project key from the project association {@link Collection}
	 * 
	 * @param projectKey the {@link String} project key to remove
	 */
	void removeAssociatedProjectKey(String projectKey);
	
	/**
	 * Check if the Hudson server is a secured server
	 * 
	 * @return <code>true</code> if secured, <code>false</code> otherwise
	 */
	boolean isSecuredHudsonServer();

	/**
	 * Gets the image url for small images
	 * 
	 * @return the url
	 */
	String getSmallImageUrl();

	/**
	 * Gets the image url for small images
	 * 
	 * @return the url
	 */
	String getMediumImageUrl();

	/**
	 * Gets the image url for small images
	 * 
	 * @return the url
	 */
	String getLargeImageUrl();

	/**
	 * Gets the crumb
	 * 
	 * @return the crumb
	 */
	String getCrumb();

	/**
	 * Sets the crumb
	 * 
	 * @param crumb the crumb
	 */
	void setCrumb(String crumb);

	/**
	 * Gets the crumb-fiels
	 * 
	 * @return the crumb-fiels
	 */
	String getCrumbField();

	/**
	 * Sets the crumb-fiels
	 * 
	 * @param crumbField the crumb-fiels
	 */
	void setCrumbField(String crumbField);

}
