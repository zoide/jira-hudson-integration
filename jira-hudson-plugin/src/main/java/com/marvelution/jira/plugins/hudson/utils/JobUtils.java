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

import com.marvelution.hudson.plugins.apiv2.resources.model.build.Build;
import com.marvelution.hudson.plugins.apiv2.resources.model.build.Result;
import com.marvelution.hudson.plugins.apiv2.resources.model.job.Job;
import com.marvelution.hudson.plugins.apiv2.resources.model.job.Jobs;

/**
 * Utility class for {@link Jobs} and {@link Job} objects
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class JobUtils {

	/**
	 * Check if the given {@link Job} has a valid first build
	 * 
	 * @param job the {@link Job} to check
	 * @return <code>true</code> if it has a valid first build, <code>false</code> otherwise
	 * @see BuildUtils#isValidBuild(com.marvelution.hudson.plugins.apiv2.resources.model.Build)
	 */
	public static boolean hasValidFirstBuild(Job job) {
		return BuildUtils.isValidBuild(job.getFirstBuild());
	}

	/**
	 * Check if the given {@link Job} has a valid last build
	 * 
	 * @param job the {@link Job} to check
	 * @return <code>true</code> if it has a valid last build, <code>false</code> otherwise
	 * @see BuildUtils#isValidBuild(com.marvelution.hudson.plugins.apiv2.resources.model.Build)
	 */
	public static boolean hasValidLastBuild(Job job) {
		return BuildUtils.isValidBuild(job.getLastBuild());
	}

	/**
	 * Check if the given {@link Job} has a valid last successful build
	 * 
	 * @param job the {@link Job} to check
	 * @return <code>true</code> if it has a valid last successful build, <code>false</code> otherwise
	 * @see BuildUtils#isSuccessfulBuild(com.marvelution.hudson.plugins.apiv2.resources.model.Build)
	 */
	public static boolean hasValidLastSuccessfulBuild(Job job) {
		return BuildUtils.isSuccessfulBuild(job.getLastSuccessfulBuild());
	}

	/**
	 * Check if the given {@link Job} has a valid last failed build
	 * 
	 * @param job the {@link Job} to check
	 * @return <code>true</code> if it has a valid last failed build, <code>false</code> otherwise
	 * @see BuildUtils#isFailedBuild(com.marvelution.hudson.plugins.apiv2.resources.model.Build)
	 */
	public static boolean hasValidLastFailedBuild(Job job) {
		return BuildUtils.isFailedBuild(job.getLastFailedBuild());
	}

	/**
	 * Check if the given {@link Job} has a valid last unstable build
	 * 
	 * @param job the {@link Job} to check
	 * @return <code>true</code> if it has a valid last unstable build, <code>false</code> otherwise
	 * @see BuildUtils#isUnstableBuild(com.marvelution.hudson.plugins.apiv2.resources.model.Build)
	 */
	public static boolean hasValidLastUnstableBuild(Job job) {
		return BuildUtils.isUnstableBuild(job.getLastUnstableBuild());
	}

	/**
	 * Check if the given {@link Job} has a valid last stable build
	 * 
	 * @param job the {@link Job} to check
	 * @return <code>true</code> if it has a valid last stable build, <code>false</code> otherwise
	 * @see BuildUtils#isValidBuild(com.marvelution.hudson.plugins.apiv2.resources.model.Build)
	 * @see BuildUtils#isUnstableBuild(com.marvelution.hudson.plugins.apiv2.resources.model.Build)
	 */
	public static boolean hasValidLastStableBuild(Job job) {
		return BuildUtils.isValidBuild(job.getLastStableBuild()) &&
			BuildUtils.isUnstableBuild(job.getLastStableBuild());
	}

	/**
	 * Check if the given {@link Job} has a valid last completed build
	 * 
	 * @param job the {@link Job} to check
	 * @return <code>true</code> if it has a valid last completed build, <code>false</code> otherwise
	 * @see BuildUtils#isValidBuild(com.marvelution.hudson.plugins.apiv2.resources.model.Build)
	 */
	public static boolean hasValidLastCompletedBuild(Job job) {
		return BuildUtils.isValidBuild(job.getLastCompletedBuild());
	}

	/**
	 * Calculate the success ration of the {@link Job}
	 * 
	 * @param job the {@link Job}
	 * @return the success ratio
	 */
	public static int getJobSuccessRatio(Job job) {
		double count = 0, success = 0;
		for (Build build : job.getBuilds()) {
			count++;
			if (Result.SUCCESSFUL.equals(build.getResult())) {
				success++;
			}
		}
		return Double.valueOf(success / count * 100).intValue();
	}

}
