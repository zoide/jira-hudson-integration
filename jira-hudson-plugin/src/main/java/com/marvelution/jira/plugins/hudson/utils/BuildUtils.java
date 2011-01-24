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

import com.marvelution.hudson.plugins.apiv2.resources.model.Build;
import com.marvelution.hudson.plugins.apiv2.resources.model.Builds;
import com.marvelution.hudson.plugins.apiv2.resources.model.Result;

/**
 * Utility class for {@link Builds} and {@link Build} objects
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class BuildUtils {

	/**
	 * Check if the given {@link Build} object is a valid {@link Build}.
	 * A {@link Build} can only be value its
	 * <ul>
	 *   <li>not <code>null</code>,</li>
	 *   <li>the build number is larger then 0 (zero) and</li>
	 *   <li>the build time-stamp is larger then 0 (zero)</li>
	 * </ul>
	 * 
	 * @param build the {@link Build} to check
	 * @return <code>true</code> if valid, <code>false</code> otherwise
	 */
	public static boolean isValidBuild(Build build) {
		return (build != null && build.getBuildNumber() > 0 && build.getTimestamp() > 0);
	}

	/**
	 * Check if the given {@link Build} is a successful build
	 * A {@link Build} can only be successful if:
	 * <ul>
	 *   <li>the build is valid (@see {@link #isValid(Build)})</li>
	 *   <li>the build result equals {@link Result#SUCCESS}</li>
	 * </ul>
	 * 
	 * @param build the {@link Build} to check
	 * @return <code>true</code> if the build is successful, <code>false</code> otherwise
	 */
	public static boolean isSuccessfulBuild(Build build) {
		return isValidBuild(build) && Result.SUCCESSFUL.equals(build.getResult());
	}

	/**
	 * Check if the given {@link Build} is a failed build
	 * A {@link Build} can only be failed if:
	 * <ul>
	 *   <li>the build is valid (@see {@link #isValid(Build)})</li>
	 *   <li>the build result equals {@link Result#FAILURE}</li>
	 * </ul>
	 * 
	 * @param build the {@link Build} to check
	 * @return <code>true</code> if the build is failed, <code>false</code> otherwise
	 */
	public static boolean isFailedBuild(Build build) {
		return isValidBuild(build) && Result.FAILED.equals(build.getResult());
	}

	/**
	 * Check if the given {@link Build} is a unstable build
	 * A {@link Build} can only be unstable if:
	 * <ul>
	 *   <li>the build is valid (@see {@link #isValid(Build)})</li>
	 *   <li>the build result equals {@link Result#UNSTABLE}</li>
	 * </ul>
	 * 
	 * @param build the {@link Build} to check
	 * @return <code>true</code> if the build is unstable, <code>false</code> otherwise
	 */
	public static boolean isUnstableBuild(Build build) {
		return isValidBuild(build) && Result.UNSTABLE.equals(build.getResult());
	}

	/**
	 * Check if the given {@link Build} is a aborted build
	 * A {@link Build} can only be aborted if:
	 * <ul>
	 *   <li>the build is valid (@see {@link #isValid(Build)})</li>
	 *   <li>the build result equals {@link Result#ABORTED}</li>
	 * </ul>
	 * 
	 * @param build the {@link Build} to check
	 * @return <code>true</code> if the build is aborted, <code>false</code> otherwise
	 */
	public static boolean isAbortedBuild(Build build) {
		return isValidBuild(build) && Result.ABORTED.equals(build.getResult());
	}

	/**
	 * Check if the given {@link Build} is a 'not build' build
	 * A {@link Build} can only be 'not build' if:
	 * <ul>
	 *   <li>the build is valid (@see {@link #isValid(Build)})</li>
	 *   <li>the build result equals {@link Result#NOTBUILD}</li>
	 * </ul>
	 * 
	 * @param build the {@link Build} to check
	 * @return <code>true</code> if the build is 'not build', <code>false</code> otherwise
	 */
	public static boolean isNotBuildBuild(Build build) {
		return isValidBuild(build) && Result.NOTBUILD.equals(build.getResult());
	}

}
