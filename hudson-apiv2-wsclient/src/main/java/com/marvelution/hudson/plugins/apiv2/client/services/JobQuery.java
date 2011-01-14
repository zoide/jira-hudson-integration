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

import java.util.List;

import com.marvelution.hudson.plugins.apiv2.resources.model.Job;
import com.marvelution.hudson.plugins.apiv2.resources.model.Jobs;

/**
 * {@link Query} implementation for {@link Job} objects
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class JobQuery extends AbstractListableQuery<Job, Jobs> {

	private String name = null;
	private QueryType type;

	/**
	 * Private constructor to force the use of the static method below
	 */
	private JobQuery(QueryType type) {
		super(Job.class, Jobs.class);
		this.type = type;
	}

	/**
	 * Getter for the name
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Setter for the name
	 * 
	 * @param name the name to set
	 */
	private void setName(String name) {
		this.name = name;
	}

	/**
	 * Getter for type
	 * 
	 * @return the type
	 */
	public QueryType getType() {
		return type;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getUrl() {
		StringBuilder url = new StringBuilder(super.getUrl());
		url.append("/jobs");
		if (QueryType.SPECIFIC.equals(getType()) && name != null) {
			url.append("?name=").append(name);
		} else if (QueryType.STATUS.equals(getType()) && name != null) {
			url.append("/status?name=").append(name);
		} else if (QueryType.LIST.equals(getType())) {
			url.append("/list");
		} else if (QueryType.ALL.equals(getType())) {
			url.append("/all");
		}
		return url.toString();
	}

	/**
	 * Method to create a {@link JobQuery} that will result in a {@link Job} from the Hudson server
	 * 
	 * @param jobName the name of the {@link Job} on the Hudson server
	 * @return the {@link JobQuery}
	 */
	public static JobQuery createForJobByName(String jobName) {
		JobQuery query = new JobQuery(QueryType.SPECIFIC);
		query.setName(jobName);
		return query;
	}

	/**
	 * Method to create a {@link JobQuery} that will result in a {@link Job} status overview from the Hudson server
	 * 
	 * @param jobName the name of the {@link Job} on the Hudson server
	 * @return the {@link JobQuery}
	 */
	public static JobQuery createForJobStatusByName(String jobName) {
		JobQuery query = new JobQuery(QueryType.STATUS);
		query.setName(jobName);
		return query;
	}

	/**
	 * Method to create {@link JobQuery} that will result in a {@link List} of all {@link Job} objects on the
	 * Hudson server, but with minimal {@link Job} data
	 * 
	 * @return the {@link JobQuery}
	 */
	public static JobQuery createForJobList() {
		JobQuery query = new JobQuery(QueryType.LIST);
		return query;
	}

	/**
	 * Method to create a {@link JobQuery} that will result in a {@link List} of all {@link Job} objects on the
	 * Hudson server
	 * 
	 * @return the {@link JobQuery}
	 */
	public static JobQuery createForAllJobs() {
		return new JobQuery(QueryType.ALL);
	}

	private static enum QueryType {
		SPECIFIC, LIST, ALL, STATUS;
	}

}
