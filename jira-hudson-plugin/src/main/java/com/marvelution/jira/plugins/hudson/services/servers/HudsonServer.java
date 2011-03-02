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

import com.marvelution.hudson.plugins.apiv2.client.Host;

/**
 * Hudson Server Interface
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public interface HudsonServer {

	/**
	 * Getter for the Server Id
	 * 
	 * @return the Server Id
	 */
	int getServerId();

	/**
	 * Setter for the Server Id
	 * 
	 * @param serverId the Server Id
	 */
	void setServerId(int serverId);

	/**
	 * Getter for the Server name
	 * 
	 * @return the name of the server
	 */
	String getName();

	/**
	 * Setter for the Server name
	 * 
	 * @param name the server name
	 */
	void setName(String name);

	/**
	 * Getter for the Server description
	 * 
	 * @return the Server description
	 */
	String getDescription();

	/**
	 * Setter for the Server description
	 * 
	 * @param description the server description
	 */
	void setDescription(String description);

	/**
	 * Getter for the Server Host
	 * 
	 * @return the Server Host
	 */
	String getHost();

	/**
	 * Setter for the Server Host
	 * 
	 * @param host the Server Host
	 */
	void setHost(String host);

	/**
	 * Getter for the Server Public Host
	 * 
	 * @return the Server Public Host
	 */
	String getPublicHost();

	/**
	 * Setter for the Server Public Host
	 * 
	 * @param publicHost the Server Public Host
	 */
	void setPublicHost(String publicHost);

	/**
	 * Getter for the username
	 * 
	 * @return the username
	 */
	String getUsername();

	/**
	 * Setter for the username
	 * 
	 * @param username the username
	 */
	void setUsername(String username);

	/**
	 * Getter for the password
	 * 
	 * @return the password
	 */
	String getPassword();

	/**
	 * Setter for the password
	 * 
	 * @param password the password
	 */
	void setPassword(String password);

	/**
	 * Getter for the secured {@link Host} setting
	 * 
	 * @return <code>true</code> is the {@link Host} is secure, <code>false</code> otherwise
	 */
	boolean isSecured();

}
