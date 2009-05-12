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

/**
 * Interface that represents a Hudson Server configured in JIRA
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public interface HudsonServer {

	String HUDSON_IMAGE_LOCATION = "/images/24x24";

	/**
	 * Gets the Hudson Server Name
	 * 
	 * @return the server name
	 */
	String getServername();

	/**
	 * Gets the Hudson server host
	 * 
	 * @return the Hudson server host 
	 */
	String getHost();

	/**
	 * Gets the image URL of the Hudson server
	 * 
	 * @return the image URL
	 */
	String getImageUrl();

	/**
	 * Gets the username to use to authenticate with
	 * 
	 * @return the username
	 */
	String getUsername();

	/**
	 * Gets the password for the username
	 * 
	 * @return the password
	 * @see HudsonServer#getUsername()
	 */
	String getPassword();

	/**
	 * Sets the Hudson Server Name
	 * 
	 * @param servername the server name
	 */
	void setServername(String servername);

	/**
	 * Sets the Hudson server host
	 * 
	 * @param host the Hudson host
	 */
	void setHost(String host);

	/**
	 * Sets the username to authenticate with
	 * 
	 * @param username the username
	 */
	void setUsername(String username);

	/**
	 * Sets the password for the username
	 * 
	 * @param password the password
	 * @see #setUsername(String)
	 */
	void setPassword(String password);

	/**
	 * Check if a Hudson server is configured
	 * 
	 * @return <code>true</code> if a Hudson server is configured, <code>false</code> otherwise
	 */
	boolean isHudsonConfigured();
	
	/**
	 * Check if the configured Hudson server is a secured server
	 * 
	 * @return <code>true</code> if secured, <code>false</code> otherwise
	 */
	boolean isSecuredHudsonServer();

}
