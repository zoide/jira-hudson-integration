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

import com.marvelution.jira.plugins.hudson.model.Build;
import com.marvelution.jira.plugins.hudson.model.Result;

/**
 * {@link Build} Model helper class
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class BuildUtils {

	/**
	 * Check if the given {@link Build} is valid
	 * 
	 * @param build the {@link Build} to check
	 * @return {@link Boolean#TRUE} if valid, {@link Boolean#FALSE} otherwise
	 */
	public boolean isValidBuild(Build build) {
		return (build != null && build.getTimestamp() > 0L && build.getDuration() > 0L);
	}

	/**
	 * Check if the given {@link Build} is successful
	 * 
	 * @param build the {@link Build} to check
	 * @return <code>true</code> if successful, <code>false</code> otherwise
	 */
	public boolean isSuccessfulBuild(Build build) {
		return isValidBuild(build) && Result.SUCCESS == build.getResult();
	}

	/**
	 * Check if the given {@link Build} is failed
	 * 
	 * @param build the {@link Build} to check
	 * @return <code>true</code> if failed, <code>false</code> otherwise
	 */
	public boolean isFailedBuild(Build build) {
		return isValidBuild(build) && Result.FAILURE == build.getResult();
	}

	/**
	 * Check if the given {@link Build} is unstable
	 * 
	 * @param build the {@link Build} to check
	 * @return <code>true</code> if unstable, <code>false</code> otherwise
	 */
	public boolean isUnstableBuild(Build build) {
		return isValidBuild(build) && Result.UNSTABLE == build.getResult();
	}

	/**
	 * Check if the given {@link Build} is aborted
	 * 
	 * @param build the {@link Build} to check
	 * @return <code>true</code> if aborted, <code>false</code> otherwise
	 */
	public boolean isAbortedBuild(Build build) {
		return isValidBuild(build) && Result.ABORTED == build.getResult();
	}

	/**
	 * Check if the given {@link Build} is not build
	 * 
	 * @param build the {@link Build} to check
	 * @return <code>true</code> if not build, <code>false</code> otherwise
	 */
	public boolean isNotBuild(Build build) {
		return isValidBuild(build) && Result.NOT_BUILT == build.getResult();
	}

}
