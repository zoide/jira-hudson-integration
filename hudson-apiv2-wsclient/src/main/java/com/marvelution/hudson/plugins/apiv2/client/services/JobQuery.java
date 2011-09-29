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

import com.marvelution.hudson.plugins.apiv2.resources.model.job.Job;
import com.marvelution.hudson.plugins.apiv2.resources.model.job.Jobs;

/**
 * {@link Query} implementation for {@link Job} objects
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class JobQuery extends AbstractListableQuery<Job, Jobs> {

	private String name = null;
	private JobQueryType type;

	/**
	 * Private constructor to force the use of the static method below
	 */
	private JobQuery(JobQueryType type) {
		super(Job.class, Jobs.class, QueryType.GET);
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
	public JobQueryType getType() {
		return type;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getSpecificUrl() {
		final StringBuilder url = new StringBuilder();
		url.append("jobs");
		if (JobQueryType.SPECIFIC.equals(getType()) && name != null) {
			url.append("?name=").append(urlEncode(name));
		} else if (JobQueryType.STATUS.equals(getType()) && name != null) {
			url.append("/status?name=").append(urlEncode(name));
		} else if (JobQueryType.LIST.equals(getType()) || JobQueryType.NAME_ONLY.equals(getType())) {
			url.append("/list");
			if (JobQueryType.NAME_ONLY.equals(getType())) {
				url.append("?nameOnly=true");
			}
		} else if (JobQueryType.ALL.equals(getType())) {
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
		JobQuery query = new JobQuery(JobQueryType.SPECIFIC);
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
		JobQuery query = new JobQuery(JobQueryType.STATUS);
		query.setName(jobName);
		return query;
	}

	/**
	 * Method to create {@link JobQuery} that will result in a {@link List} of all {@link Job} objects on the
	 * Hudson server, but with minimal {@link Job} data
	 * 
	 * @param nameOnly {@link Boolean} flag to specify if you want Job Names only or a more complete mapping of each
	 * 				   Job
	 * @return the {@link JobQuery}
	 */
	public static JobQuery createForJobList(boolean nameOnly) {
		JobQuery query;
		if (nameOnly) {
			query = new JobQuery(JobQueryType.NAME_ONLY);
		} else {
			query = new JobQuery(JobQueryType.LIST);
		}
		return query;
	}

	/**
	 * Method to create a {@link JobQuery} that will result in a {@link List} of all {@link Job} objects on the
	 * Hudson server
	 * 
	 * @return the {@link JobQuery}
	 */
	public static JobQuery createForAllJobs() {
		return new JobQuery(JobQueryType.ALL);
	}

	/**
	 * Enumeration of the different Joq QueryTypes supported
	 * 
	 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
	 */
	private static enum JobQueryType {
		SPECIFIC, LIST, ALL, STATUS, NAME_ONLY;
	}

}
