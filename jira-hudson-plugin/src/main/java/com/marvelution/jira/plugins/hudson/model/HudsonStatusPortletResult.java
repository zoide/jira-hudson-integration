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

import java.util.List;

import com.marvelution.jira.plugins.hudson.service.HudsonServer;

/**
 * {@link HudsonStatusPortlet} Result model
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class HudsonStatusPortletResult extends HudsonServerResult<List<Job>> {

	/**
	 * Constructor
	 * 
	 * @param server the {@link HudsonServer}
	 */
	public HudsonStatusPortletResult(HudsonServer server) {
		super(server);
	}

	/**
	 * Gets the {@link List} of Jobs that run on the {@link HudsonServer}
	 * 
	 * @return {@link List} of {@link Job} objects
	 */
	public List<Job> getJobs() {
		return getResults();
	}

	/**
	 * Sets the {@link List} of {@link Job} object that are associated this Result
	 * 
	 * @param jobs the {@link List} of {@link Job} objects
	 */
	public void setJobs(List<Job> jobs) {
		setResults(jobs);
	}

}
