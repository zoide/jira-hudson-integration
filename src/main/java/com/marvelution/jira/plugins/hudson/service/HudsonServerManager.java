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

import com.atlassian.jira.project.Project;

/**
 * {@link HudsonServer} Manager interface
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public interface HudsonServerManager {

	/**
	 * Checks if a default {@link HudsonServer} is configured
	 * 
	 * @return <code>true</code> if a default {@link HudsonServer} is configured, <code>false</code> otherwise
	 */
	boolean isHudsonConfigured();

	/**
	 * Checks if {@link HudsonServer} objects are configure
	 * 
	 * @return <code>true</code> if there are Servers configured, <code>false</code> otherwise
	 */
	boolean hasServers();

	/**
	 * Checks if a {@link HudsonServer} is configure with the given name
	 * 
	 * @param serverName the server name to check
	 * @return <code>true</code> if a {@link HudsonServer} is configured with the given name, <code>false</code>
	 *         otherwise
	 */
	boolean hasServer(String serverName);

	/**
	 * Gets all the configure {@link HudsonServer} objects
	 * 
	 * @return the {@link Collection} of {@link HudsonServer} objects
	 */
	Collection<HudsonServer> getServers();

	/**
	 * Get {@link HudsonServer} by Id
	 * 
	 * @param serverId the server id
	 * @return the {@link HudsonServer}, may be <code>null</code> if no server is configured for the id
	 */
	HudsonServer getServer(int serverId);

	/**
	 * Get {@link HudsonServer} by Name
	 * 
	 * @param serverName the server name
	 * @return the {@link HudsonServer}, may be <code>null</code> if no server is configured for the name
	 */
	HudsonServer getServer(String serverName);

	/**
	 * Get {@link HudsonServer} by {@link Project}
	 * 
	 * @param project the project
	 * @return the {@link HudsonServer}, may be <code>null</code> if no server is configured for the project
	 */
	HudsonServer getServerByJiraProject(Project project);

	/**
	 * Gets the default {@link HudsonServer}
	 * 
	 * @return the default {@link HudsonServer}
	 */
	HudsonServer getDefaultServer();

	/**
	 * Sets the default {@link HudsonServer}
	 * 
	 * @param hudsonServer the {@link HudsonServer} to set default
	 */
	void setDefaultServer(HudsonServer hudsonServer);

	/**
	 * Check if the given {@link HudsonServer} is configured as default
	 * 
	 * @param hudsonServer the {@link HudsonServer} to check
	 * @return <code>true</code> if the given {@link HudsonServer} is default, <code>false</code> otherwise
	 */
	boolean isDefaultServer(HudsonServer hudsonServer);

	/**
	 * Add a {@link HudsonServer} to the server list
	 * 
	 * @param hudsonServer the {@link HudsonServer} to add
	 */
	void put(HudsonServer hudsonServer);

	/**
	 * Remove a {@link HudsonServer} from the list
	 * 
	 * @param serverId the server id to remvoe
	 */
	void remove(int serverId);

}
