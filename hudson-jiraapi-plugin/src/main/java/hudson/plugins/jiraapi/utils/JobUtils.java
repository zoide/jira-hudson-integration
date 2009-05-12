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

package hudson.plugins.jiraapi.utils;

import java.util.List;

import hudson.model.Hudson;
import hudson.model.Job;
import hudson.plugins.jiraapi.JiraProjectKeyJobProperty;

/**
 * Helper class for Hudson Jobs
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class JobUtils {

	/**
	 * Get the Hudson {@link Job} by Jira Project Key
	 * 
	 * @param key the Jira project key
	 * @return the {@link Job}, may be <code>null</code> if no {@link Job} can be found
	 */
	@SuppressWarnings("unchecked")
	public static Job<?, ?> getJobByJiraProjectKey(final String key) {
		final List<Job> jobs = Hudson.getInstance().getAllItems(Job.class);
		for (Job job : jobs) {
			if (job.getProperty(JiraProjectKeyJobProperty.class) != null) {
				final JiraProjectKeyJobProperty jiraProperty =
					(JiraProjectKeyJobProperty) job.getProperty(JiraProjectKeyJobProperty.class);
				if (key.equals(jiraProperty.getKey())) {
					return job;
				}
			}
		}
		return null;
	}
}
