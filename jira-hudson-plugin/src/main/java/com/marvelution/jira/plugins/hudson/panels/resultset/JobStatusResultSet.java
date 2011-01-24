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

package com.marvelution.jira.plugins.hudson.panels.resultset;

import com.marvelution.hudson.plugins.apiv2.resources.model.Job;
import com.marvelution.jira.plugins.hudson.services.servers.HudsonServer;

/**
 * {@link ResultSet} implementation specific for the Job Status view on the panels
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class JobStatusResultSet implements ResultSet<Job> {

	private final HudsonServer server;
	private final Job job;

	/**
	 * Constructor
	 * 
	 * @param server the {@link HudsonServer}
	 * @param job the {@link Job}
	 */
	public JobStatusResultSet(HudsonServer server, Job job) {
		this.server = server;
		this.job = job;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResultViews getView() {
		return ResultViews.JOBSTATUS;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HudsonServer getServer() {
		return server;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Job getResults() {
		return job;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasResults() {
		return job != null;
	}

}
