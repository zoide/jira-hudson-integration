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

package com.marvelution.jira.plugins.hudson.model;

import org.apache.commons.lang.StringUtils;

import com.marvelution.jira.plugins.hudson.service.HudsonServer;

/**
 * Hudson Result model
 * 
 * @param <T> the type of results
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class HudsonServerResult<T> {

	private HudsonServer server;

	private String error;

	protected T results;

	/**
	 * Constructor
	 * 
	 * @param server the {@link HudsonServer}
	 */
	public HudsonServerResult(HudsonServer server) {
		this.server = server;
	}

	/**
	 * Constructor
	 * 
	 * @param server the {@link HudsonServer}
	 * @param results the Results
	 */
	public HudsonServerResult(HudsonServer server, T results) {
		this.server = server;
		this.results = results;
	}

	/**
	 * Gets the {@link HudsonServer}
	 * 
	 * @return the {@link HudsonServer}
	 */
	public HudsonServer getServer() {
		return server;
	}

	/**
	 * Sets the {@link HudsonServer}
	 * 
	 * @param server the {@link HudsonServer}
	 */
	public void setServer(HudsonServer server) {
		this.server = server;
	}

	/**
	 * Gets the results
	 * 
	 * @return the results
	 */
	public T getResults() {
		return results;
	}

	/**
	 * Sets the results
	 * 
	 * @param results teh results to set
	 */
	public void setResults(T results) {
		this.results = results;
	}

	/**
	 * Check if this result has an error
	 * 
	 * @return <code>true</code> if {@link #error} is <code>null</code> or <code>empty</code>, <code>false</code>
	 *         otherwise
	 */
	public boolean hasError() {
		return !StringUtils.isEmpty(error);
	}

	/**
	 * Gets the error message
	 * 
	 * @return the error message
	 */
	public String getError() {
		return error;
	}

	/**
	 * Sets the error message
	 * 
	 * @param error the error message
	 */
	public void setError(String error) {
		this.error = error;
	}

	/**
	 * Gets the {@link HudsonServer} Name
	 * 
	 * @return the name
	 */
	public String getServerName() {
		return server.getName();
	}

	/**
	 * Gets the {@link HudsonServer} URL
	 * 
	 * @return the URL
	 */
	public String getServerUrl() {
		return server.getHost();
	}

	/**
	 * Gets the {@link HudsonServer} Small Image URL
	 * 
	 * @return the Image URL
	 */
	public String getServerSmallImageUrl() {
		return server.getSmallImageUrl();
	}

	/**
	 * Gets the {@link HudsonServer} Medium Image URL
	 * 
	 * @return the Image URL
	 */
	public String getServerMediumImageUrl() {
		return server.getMediumImageUrl();
	}

	/**
	 * Gets the {@link HudsonServer} Large Image URL
	 * 
	 * @return the Image URL
	 */
	public String getServerLargeImageUrl() {
		return server.getLargeImageUrl();
	}

	/**
	 * Gets the {@link HudsonServer} Image URL
	 * 
	 * @return the Image URL
	 */
	public String getServerImageUrl() {
		return getServerMediumImageUrl();
	}

}
