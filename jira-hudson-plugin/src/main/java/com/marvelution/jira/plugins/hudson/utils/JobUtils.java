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

package com.marvelution.jira.plugins.hudson.utils;

import com.marvelution.jira.plugins.hudson.api.model.Job;

/**
 * {@link Job} Model helper class
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class JobUtils {

	private BuildUtils buildUtils = new BuildUtils();

	/**
	 * Check if a {@link Job} has a First {@link Build}
	 * 
	 * @param job the {@link Job} to check
	 * @return {@link Boolean#TRUE} if the Job has a first build, {@link Boolean#FALSE} otherwise
	 */
	public Boolean hasValidFirstBuild(Job job) {
		return Boolean.valueOf(buildUtils.isValidBuild(job.getFirstBuild()));
	}

	/**
	 * Check if a {@link Job} has a Last {@link Build}
	 * 
	 * @param job the {@link Job} to check
	 * @return {@link Boolean#TRUE} if the Job has a last build, {@link Boolean#FALSE} otherwise
	 */
	public Boolean hasValidLastBuild(Job job) {
		return Boolean.valueOf(buildUtils.isValidBuild(job.getLastBuild()));
	}

	/**
	 * Check if a {@link Job} has a Last Successful {@link Build}
	 * 
	 * @param job the {@link Job} to check
	 * @return {@link Boolean#TRUE} if the Job has a last successful build, {@link Boolean#FALSE} otherwise
	 */
	public Boolean hasValidLastSuccessfulBuild(Job job) {
		return Boolean.valueOf(buildUtils.isValidBuild(job.getLastSuccessfulBuild()));
	}

	/**
	 * Check if a {@link Job} has a Last Failed {@link Build}
	 * 
	 * @param job the {@link Job} to check
	 * @return {@link Boolean#TRUE} if the Job has a last failed build, {@link Boolean#FALSE} otherwise
	 */
	public Boolean hasValidLastFailedBuild(Job job) {
		return Boolean.valueOf(buildUtils.isValidBuild(job.getLastFailedBuild()));
	}

	/**
	 * Check if a {@link Job} has a Last Stable {@link Build}
	 * 
	 * @param job the {@link Job} to check
	 * @return {@link Boolean#TRUE} if the Job has a last stable build, {@link Boolean#FALSE} otherwise
	 */
	public Boolean hasValidLastStableBuild(Job job) {
		return Boolean.valueOf(buildUtils.isValidBuild(job.getLastStableBuild()));
	}

	/**
	 * Check if a {@link Job} has a Last Unstable {@link Build}
	 * 
	 * @param job the {@link Job} to check
	 * @return {@link Boolean#TRUE} if the Job has a last unstable build, {@link Boolean#FALSE} otherwise
	 */
	public Boolean hasValidLastUnstableBuild(Job job) {
		return Boolean.valueOf(buildUtils.isValidBuild(job.getLastUnstableBuild()));
	}

	/**
	 * Check if a {@link Job} has a Last Completed {@link Build}
	 * 
	 * @param job the {@link Job} to check
	 * @return {@link Boolean#TRUE} if the Job has a last completed build, {@link Boolean#FALSE} otherwise
	 */
	public Boolean hasValidLastCompletedBuild(Job job) {
		return Boolean.valueOf(buildUtils.isValidBuild(job.getLastCompletedBuild()));
	}

}
