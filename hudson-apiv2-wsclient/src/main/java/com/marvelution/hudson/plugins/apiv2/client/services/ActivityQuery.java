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

import java.util.Collection;

import com.marvelution.hudson.plugins.apiv2.resources.model.activity.Activities;
import com.marvelution.hudson.plugins.apiv2.resources.model.activity.Activity;
import com.marvelution.hudson.plugins.apiv2.resources.model.activity.ActivityType;

/**
 * {@link Query} implementation for the {@link Activity} objects
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 * @since 4.2.0
 */
public class ActivityQuery extends AbstractListableQuery<Activity, Activities> {

	private final ActivityType[] types;
	private int maxResults = 10;
	private String[] userIds;
	private String[] jobs;

	/**
	 * Constructor
	 *
	 * @param types the {@link ActivityType} array to get
	 */
	private ActivityQuery(ActivityType[] types) {
		super(Activity.class, Activities.class, QueryType.GET);
		this.types = types;
	}

	/**
	 * Getter for maxResults
	 *
	 * @return the maxResults
	 */
	public int getMaxResults() {
		return maxResults;
	}

	/**
	 * Setter for maxResults
	 *
	 * @param maxResults the maxResults to set
	 * @return this {@link ActivityQuery}
	 */
	public ActivityQuery setMaxResults(int maxResults) {
		this.maxResults = maxResults;
		return this;
	}

	/**
	 * Getter for jobs
	 *
	 * @return the jobs
	 */
	public String[] getJobs() {
		return jobs;
	}

	/**
	 * Setter for jobs
	 *
	 * @param jobs the jobs to set
	 * @return this {@link ActivityQuery}
	 */
	public ActivityQuery setJobs(String[] jobs) {
		this.jobs = jobs;
		return this;
	}

	/**
	 * Setter for jobs
	 *
	 * @param jobs the jobs to set
	 * @return this {@link ActivityQuery}
	 */
	public ActivityQuery setJobs(Collection<String> jobs) {
		this.jobs = jobs.toArray(new String[jobs.size()]);
		return this;
	}

	/**
	 * Getter for userIds
	 *
	 * @return the userIds
	 */
	public String[] getUserIds() {
		return userIds;
	}

	/**
	 * Setter for userIds
	 *
	 * @param userIds the userIds to set
	 * @return this {@link ActivityQuery}
	 */
	public ActivityQuery setUserIds(String[] userIds) {
		this.userIds = userIds;
		return this;
	}

	/**
	 * Setter for userIds
	 *
	 * @param userIds the userIds to set
	 * @return this {@link ActivityQuery}
	 */
	public ActivityQuery setUserIds(Collection<String> userIds) {
		this.userIds = userIds.toArray(new String[userIds.size()]);
		return this;
	}

	/**
	 * Getter for types
	 *
	 * @return the types
	 */
	public ActivityType[] getTypes() {
		return types;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getSpecificUrl() {
		StringBuilder url = new StringBuilder("activity?");
		addUrlParameter(url, "type", types);
		addUrlParameter(url, "user", userIds);
		addUrlParameter(url, "job", jobs);
		addUrlParameter(url, "max", maxResults);
		return url.toString();
	}

	/**
	 * Create an {@link ActivityQuery} without any filters
	 * 
	 * @return the {@link ActivityQuery}
	 */
	public static ActivityQuery createForAllActivities() {
		return new ActivityQuery(ActivityType.values());
	}

	/**
	 * Create an {@link ActivityQuery} with only {@link ActivityType} filter
	 * 
	 * @param types the {@link ActivityType} array to filter by
	 * @return the {@link ActivityQuery}
	 */
	public static ActivityQuery createForActivities(ActivityType[] types) {
		return new ActivityQuery(types);
	}

	/**
	 * Create an {@link ActivityQuery} with only {@link ActivityType} filter
	 * 
	 * @param types the {@link ActivityType} collection to filter by
	 * @return the {@link ActivityQuery}
	 */
	public static ActivityQuery createForActivities(Collection<ActivityType> types) {
		return new ActivityQuery(types.toArray(new ActivityType[types.size()]));
	}

}
