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

package com.marvelution.hudson.plugins.jirareporter.utils;

import com.marvelution.hudson.plugins.jirareporter.JIRASite;

import hudson.model.AbstractBuild;
import hudson.model.Cause;
import hudson.model.Hudson;
import hudson.model.Result;

/**
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class IssueTextUtils {

	private static final String NEW_LINE = "\n";

	/**
	 * Helper method to create the Summary text
	 * 
	 * @param build the {@link AbstractBuild}
	 * @param site the {@link JIRASite} configuration
	 * @return the summary text
	 */
	public static String createIssueSummary(AbstractBuild<?, ?> build, JIRASite site) {
		StringBuilder summary = new StringBuilder();
		summary.append(build.getProject().getDisplayName());
		summary.append(" build #");
		summary.append(build.getNumber());
		summary.append(" ");
		if (build.getResult().isWorseThan(Result.SUCCESS)) {
			summary.append("failed");
		} else {
			summary.append("succeeded");
		}
		return summary.toString();
	}

	/**
	 * Helper method to create the Description text
	 * 
	 * @param build the {@link AbstractBuild}
	 * @param site the {@link JIRASite} configuration
	 * @return the description text
	 */
	public static String createIssueDescription(AbstractBuild<?, ?> build, JIRASite site) {
		StringBuilder description = new StringBuilder();
		// TODO Auto-generated method stub
		return description.toString();
	}

	/**
	 * Helper method to create the Environment text
	 * 
	 * @param build the {@link AbstractBuild}
	 * @param site the {@link JIRASite} configuration
	 * @return the environment text
	 */
	public static String createIssueEnvironment(AbstractBuild<?, ?> build, JIRASite site) {
		StringBuilder environment = new StringBuilder();
		try {
			Class.forName("jenkins.model.Jenkins");
			environment.append("Jenkins");
		} catch (ClassNotFoundException e) {
			environment.append("Hudson");
		}
		environment.append(" version: ").append(Hudson.getVersion().toString()).append(NEW_LINE);
		environment.append("Job: ").append(build.getProject().getDisplayName()).append(NEW_LINE);
		environment.append("Build: ").append(build.getNumber()).append(NEW_LINE);
		for (Cause cause : build.getCauses()) {
			environment.append("Build Trigger: ").append(cause.getShortDescription());
		}
		return environment.toString();
	}

}
