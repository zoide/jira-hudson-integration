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

import java.util.Collection;

/**
 * {@link HudsonServer} Manager interface
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public interface HudsonServerManager {

	/**
	 * Get if at least one {@link HudsonServer} is configured
	 * 
	 * @return <code>true</code> if at least one {@link HudsonServer} is configured, <code>false</code> otherwise
	 * @see #hasServers()
	 */
	boolean isConfigured();

	/**
	 * Check if there are any {@link HudsonServer} objects available
	 * 
	 * @return <code>true</code> if there are {@link HudsonServer} available, <code>false</code> otherwise
	 */
	boolean hasServers();

	/**
	 * Check if there is a {@link HudsonServer} with the given Id
	 * 
	 * @param serverId the {@link HudsonServer} Id to check
	 * @return <code>true</code> if the server with the given Id exists, <code>false</code> otherwise
	 */
	boolean hasServer(int serverId);

	/**
	 * Check if there is a {@link HudsonServer} with the given Name
	 * 
	 * @param name the {@link HudsonServer} Name to check
	 * @return <code>true</code> if the server with the given Name exists, <code>false</code> otherwise
	 */
	boolean hasServer(String name);

	/**
	 * Getter for the {@link Collection} of all configured {@link HudsonServer} objects
	 * 
	 * @return the {@link Collection} of {@link HudsonServer} objects
	 */
	Collection<HudsonServer> getServers();

	/**
	 * Getter for a {@link HudsonServer} by its Id
	 * 
	 * @param serverId the {@link HudsonServer} Id
	 * @return the {@link HudsonServer}, may be <code>null</code>
	 */
	HudsonServer getServer(int serverId);

	/**
	 * Getter for a {@link HudsonServer} by its Name
	 * 
	 * @param name the {@link HudsonServer} name
	 * @return the {@link HudsonServer}, may be <code>null</code>
	 */
	HudsonServer getServer(String name);

	/**
	 * Getter for the default {@link HudsonServer}
	 * 
	 * @return the default {@link HudsonServer}
	 * @see HudsonServerManager#getServer(int)
	 */
	HudsonServer getDefaultServer();

	/**
	 * Setter for the default {@link HudsonServer} by server Id
	 * 
	 * @param serverId the server Id of the new default {@link HudsonServer}
	 */
	void setDefaultServer(int serverId);

	/**
	 * Setter for the default {@link HudsonServer}
	 * 
	 * @param server the new {@link HudsonServer} object
	 * @see #setDefaultServer(int)
	 */
	void setDefaultServer(HudsonServer server);

	/**
	 * Check if the given {@link HudsonServer} is the default
	 * 
	 * @param server the {@link HudsonServer} to check
	 * @return <code>true</code> if the given {@link HudsonServer} is the default {@link HudsonServer},
	 *         <code>false</code> otherwise.
	 */
	boolean isDefaultServer(HudsonServer server);

	/**
	 * Add a {@link HudsonServer} to the {@link Collection} of configured {@link HudsonServer}s
	 * 
	 * @param server the {@link HudsonServer} to add
	 * @throws IllegalArgumentException in case server is <code>null</code>
	 */
	void addServer(HudsonServer server);

	/**
	 * Remove a {@link HudsonServer} by its server Id
	 * 
	 * @param serverId the Id of the {@link HudsonServer} to remove
	 */
	void removeServer(int serverId);

	/**
	 * Remove a {@link HudsonServer}
	 * 
	 * @param server the {@link HudsonServer} to remove
	 * @see #removeServer(int)
	 */
	void removeServer(HudsonServer server);

}
