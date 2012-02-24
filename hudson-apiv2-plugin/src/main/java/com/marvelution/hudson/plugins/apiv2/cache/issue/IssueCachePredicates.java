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

package com.marvelution.hudson.plugins.apiv2.cache.issue;

import com.google.common.base.Predicate;

/**
 * Predicates related to teh {@link IssueCache}
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 *
 * @since 4.4.0
 */
public class IssueCachePredicates {

	/**
	 * Getter fort he "is issue cache related to the given Hudson Job" predicate
	 * 
	 * @param job the Hudson job name
	 * @return the {@link Predicate}
	 */
	public static Predicate<IssueCache> isRelatedToHudsonJob(final String job) {
		return new Predicate<IssueCache>() {
			@Override
			public boolean apply(IssueCache input) {
				return input.getJob().equals(job) || input.getJob().endsWith("/" + job);
			}
		};
	}

	/**
	 * Getter fort he "is issue cache related to the given JIRA Issue" predicate
	 * 
	 * @param projectKey the JIRA Issue Key
	 * @return the {@link Predicate}
	 */
	public static Predicate<IssueCache> isRelatedToJIRAIssue(final String issueKey) {
		return new Predicate<IssueCache>() {
			@Override
			public boolean apply(IssueCache input) {
				return input.getIssueKey().equals(issueKey);
			}
		};
	}

	/**
	 * Getter fort he "is issue cache related to the given JIRA Project" predicate
	 * 
	 * @param projectKey the JIRA Project Key
	 * @return the {@link Predicate}
	 */
	public static Predicate<IssueCache> isRelatedToJIRAProject(final String projectKey) {
		return new Predicate<IssueCache>() {
			@Override
			public boolean apply(IssueCache input) {
				return input.getIssueKey().getProject().equals(projectKey);
			}
		};
	}

}
