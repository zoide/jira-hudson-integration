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

import com.marvelution.hudson.plugins.apiv2.resources.model.build.Build;
import com.marvelution.hudson.plugins.apiv2.resources.model.build.Builds;

/**
 * {@link Query} implementation for {@link Job} objects
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class BuildQuery extends AbstractListableQuery<Build, Builds> {

	private final String jobName;
	private Integer buildNumber = 0;
	private BuildType buildType = null;
	private Long from = -1L;
	private Long to = -1L;

	/**
	 * Private constructor to force the use of the static methods below
	 * 
	 * @param jobName the name of the Job on hudson to get the builds for
	 */
	private BuildQuery(String jobName) {
		super(Build.class, Builds.class);
		this.jobName = jobName;
	}

	/**
	 * Getter for jobName
	 * 
	 * @return the jobName
	 */
	public String getJobName() {
		return jobName;
	}

	/**
	 * Getter for from
	 * 
	 * @return the from
	 */
	public Long getFrom() {
		return from;
	}

	/**
	 * Getter for to
	 * 
	 * @return the to
	 */
	public Long getTo() {
		return to;
	}

	/**
	 * Setter for between times
	 * 
	 * @param from the from to set
	 * @param to the to to set
	 */
	private void setBetween(Long from, Long to) {
		if (from < 0L) {
			throw new IllegalArgumentException("From variable must be 0 (zero) or large");
		} else if (to < from) {
			throw new IllegalArgumentException("To variable must be larger then the from variable");
		} else {
			this.from = from;
			this.to = to;
		}
	}

	/**
	 * Setter for the after time
	 * 
	 * @param from the from to set
	 */
	private void setAfter(Long from) {
		if (from < 0L) {
			throw new IllegalArgumentException("From variable must be 0 (zero) or large");
		} else {
			this.from = from;
			this.to = -1L;
		}
	}

	/**
	 * Getter for the buildNumber
	 * 
	 * @return the buildNumber
	 */
	public Integer getBuildNumber() {
		return buildNumber;
	}

	/**
	 * Setter for the buildNUmber
	 * 
	 * @param buildNumber the buildNumber to set
	 */
	private void setBuildNumber(Integer buildNumber) {
		if (buildNumber < 1) {
			throw new IllegalArgumentException("The Build Number must be larger then zero (0)");
		}
		this.buildNumber = buildNumber;
	}

	/**
	 * Getter for the {@link BuildType}
	 * 
	 * @return the buildType
	 */
	public BuildType getBuildType() {
		return buildType;
	}

	/**
	 * Setter for the {@link BuildType}
	 * 
	 * @param buildType the buildType to set
	 */
	private void setBuildType(BuildType buildType) {
		this.buildType = buildType;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getSpecificUrl() {
		final StringBuilder url = new StringBuilder();
		url.append("builds/").append(jobName);
		if (buildNumber > 0) {
			url.append("?buildNumber=").append(buildNumber);
		} else if (from >= 0L && to == -1L) {
			url.append("/after?from=").append(from);
		} else if (from >= 0L && to >= from) {
			url.append("/between?from=").append(from);
			url.append("&to=").append(to);
		} else if (buildType != null) {
			url.append(buildType.endpoint);
		} else {
			url.append("/all");
		}
		return url.toString();
	}

	/**
	 * Method to create a {@link BuildQuery} for a specific {@link Build} of a specific job
	 * 
	 * @param jobName the name of the job
	 * @param buildNumber the number of the build
	 * @return the {@link BuildQuery}
	 */
	public static BuildQuery createForSpecificBuild(String jobName, Integer buildNumber) {
		BuildQuery query = new BuildQuery(jobName);
		query.setBuildNumber(buildNumber);
		return query;
	}

	/**
	 * Method to create a {@link BuildQuery} for all {@link Builds} of a specific job
	 * 
	 * @param jobName the name of the job
	 * @return the {@link BuildQuery}
	 */
	public static BuildQuery createForAllBuilds(String jobName) {
		return new BuildQuery(jobName);
	}

	/**
	 * Method to create a {@link BuildQuery} for the First Build of a specified Job
	 * 
	 * @param jobName the name of the Job to get the First build for
	 * @return the {@link BuildQuery}
	 */
	public static BuildQuery createForFirstBuild(String jobName) {
		BuildQuery query = new BuildQuery(jobName);
		query.setBuildType(BuildType.FIRST);
		return query;
	}

	/**
	 * Method to create a {@link BuildQuery} for the Last Build of a specified Job
	 * 
	 * @param jobName the name of the Job to get the Last build for
	 * @return the {@link BuildQuery}
	 */
	public static BuildQuery createForLastBuild(String jobName) {
		BuildQuery query = new BuildQuery(jobName);
		query.setBuildType(BuildType.LAST);
		return query;
	}

	/**
	 * Method to create a {@link BuildQuery} for the Last Successful Build of a specified Job
	 * 
	 * @param jobName the name of the Job to get the Last Successful build for
	 * @return the {@link BuildQuery}
	 */
	public static BuildQuery createForLastSuccessfulBuild(String jobName) {
		BuildQuery query = new BuildQuery(jobName);
		query.setBuildType(BuildType.LAST_SUCCESSFUL);
		return query;
	}

	/**
	 * Method to create a {@link BuildQuery} for the Last Failed Build of a specified Job
	 * 
	 * @param jobName the name of the Job to get the Last Failed build for
	 * @return the {@link BuildQuery}
	 */
	public static BuildQuery createForLastFailedBuild(String jobName) {
		BuildQuery query = new BuildQuery(jobName);
		query.setBuildType(BuildType.LAST_FAILED);
		return query;
	}

	/**
	 * Method to create a {@link BuildQuery} for the Last Stable Build of a specified Job
	 * 
	 * @param jobName the name of the Job to get the Last Stable build for
	 * @return the {@link BuildQuery}
	 */
	public static BuildQuery createForLastStableBuild(String jobName) {
		BuildQuery query = new BuildQuery(jobName);
		query.setBuildType(BuildType.LAST_STABLE);
		return query;
	}

	/**
	 * Method to create a {@link BuildQuery} for the Last Unstable Build of a specified Job
	 * 
	 * @param jobName the name of the Job to get the Last Unstable build for
	 * @return the {@link BuildQuery}
	 */
	public static BuildQuery createForLastUnstableBuild(String jobName) {
		BuildQuery query = new BuildQuery(jobName);
		query.setBuildType(BuildType.LAST_UNSTABLE);
		return query;
	}

	/**
	 * Method to create a {@link BuildQuery} for the Last Completed Build of a specified Job
	 * 
	 * @param jobName the name of the Job to get the Last Completed build for
	 * @return the {@link BuildQuery}
	 */
	public static BuildQuery createForLastCompletedBuild(String jobName) {
		BuildQuery query = new BuildQuery(jobName);
		query.setBuildType(BuildType.LAST_COMLETED);
		return query;
	}

	/**
	 * Method to create a {@link BuildQuery} for the builds between the given from and to times
	 * 
	 * @param jobName the Job name to get the builds for
	 * @param from the time (in milliseconds) from the point where builds will be included
	 * @param to the time (in milliseconds) till the point where builds will be included
	 * @return the {@link BuildQuery}
	 */
	public static BuildQuery createForBetweenTimes(String jobName, Long from, Long to) {
		BuildQuery query = new BuildQuery(jobName);
		query.setBetween(from, to);
		return query;
	}

	/**
	 * Method to create a {@link BuildQuery} for the builds between the given from and to times
	 * 
	 * @param jobName the Job name to get the builds for
	 * @param from the time (in milliseconds) from the point where builds will be included
	 * @return the {@link BuildQuery}
	 */
	public static BuildQuery createForAfterFrom(String jobName, Long from) {
		BuildQuery query = new BuildQuery(jobName);
		query.setAfter(from);
		return query;
	}

	/**
	 * The Build Type enumeration
	 * 
	 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
	 */
	public static enum BuildType {

		FIRST("/first"),
		LAST("/last"),
		LAST_SUCCESSFUL("/lastSuccessful"),
		LAST_FAILED("/lastFailed"),
		LAST_STABLE("/lastStable"),
		LAST_UNSTABLE("/lastUnstable"),
		LAST_COMLETED("/lastCompleted");
		
		private String endpoint;

		/**
		 * Constructor
		 * 
		 * @param endpoint the endpoint of the build type rest resource
		 */
		private BuildType(String endpoint) {
			this.endpoint = endpoint;
		}
	}

}
