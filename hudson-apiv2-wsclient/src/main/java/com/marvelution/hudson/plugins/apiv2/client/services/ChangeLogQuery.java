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

package com.marvelution.hudson.plugins.apiv2.client.services;

import com.marvelution.hudson.plugins.apiv2.resources.model.build.ChangeLog;

/**
 * {@link AbstractListableQuery} implementation to get {@link ChangeLog} objects
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class ChangeLogQuery extends AbstractListableQuery<ChangeLog.Entry, ChangeLog> {

	private final String jobname;
	private final Integer buildNumber;

	/**
	 * Constructor
	 * 
	 * @param jobname the job name
	 * @param buildNumber the build number
	 */
	private ChangeLogQuery(String jobname, Integer buildNumber) {
		super(ChangeLog.Entry.class, ChangeLog.class);
		this.jobname = jobname;
		this.buildNumber = buildNumber;
	}

	/**
	 * Getter for jobname
	 * 
	 * @return the jobname
	 */
	public String getJobname() {
		return jobname;
	}

	/**
	 * Getter for buildNumber
	 * 
	 * @return the buildNumber
	 */
	public Integer getBuildNumber() {
		return buildNumber;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getSpecificUrl() {
		final StringBuilder url = new StringBuilder();
		url.append("changelog/").append(jobname);
		url.append("?buildNumber=").append(buildNumber);
		return url.toString();
	}

	/**
	 * Create a {@link ChangeLogQuery} object for a specific job name and a build number
	 * 
	 * @param jobname the job name
	 * @param buildNumber the build number
	 * @return the {@link ChangeLogQuery}
	 */
	public static ChangeLogQuery createForJobAndBuild(String jobname, Integer buildNumber) {
		return new ChangeLogQuery(jobname, buildNumber);
	}

}
