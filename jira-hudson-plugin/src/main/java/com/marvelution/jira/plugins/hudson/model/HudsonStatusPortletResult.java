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

import java.util.Collection;

import org.apache.commons.lang.StringUtils;

import com.marvelution.jira.plugins.hudson.service.HudsonServer;

/**
 * {@link HudsonStatusPortlet} Result model
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class HudsonStatusPortletResult {

	private HudsonServer server;

	private Collection<Job> jobs;

	private String error;

	/**
	 * Constructor
	 * 
	 * @param server the {@link HudsonServer}
	 */
	public HudsonStatusPortletResult(HudsonServer server) {
		this.server = server;
	}

	/**
	 * Constructor
	 * 
	 * @param server the {@link HudsonServer}
	 * @param jobs the {@link Collection} of {@link Job} objects
	 */
	public HudsonStatusPortletResult(HudsonServer server, Collection<Job> jobs) {
		this.server = server;
		this.jobs = jobs;
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
	 * Gets the {@link HudsonServer} Image URL
	 * 
	 * @return the Image URL
	 */
	public String getServerImageUrl() {
		return server.getHost() + HudsonServer.MEDIUM_IMAGE_LOCATION;
	}

	/**
	 * Gets the {@link Collection} of Jobs that run on the {@link HudsonServer}
	 * 
	 * @return {@link Collection} of {@link Job} objects
	 */
	public Collection<Job> getJobs() {
		return jobs;
	}

	/**
	 * Sets the {@link Collection} of {@link Job} object that are associated this Result
	 * 
	 * @param jobs the {@link Collection} of {@link Job} objects
	 */
	public void setJobs(Collection<Job> jobs) {
		this.jobs = jobs;
	}
}
