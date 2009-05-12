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

import java.util.regex.Pattern;

/**
 * Helper class for Jira Issue and Project Keys
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public final class JiraKeyUtils {
	
	private static final Pattern JIRA_PROJECT_KEY_PATTERN = Pattern.compile("\\b[A-Z]([A-Z0-9]+)\\b");
	
	private static final Pattern JIRA_ISSUE_KEY_PATTERN = Pattern.compile("\\b[A-Z]([A-Z0-9]+)-([0-9]+)\\b");

	/**
	 * Validates if the given key {@link String} is a valid JIRA Project Key
	 * 
	 * @param key the project key to validate
	 * @return <code>true</code> if valid, <code>false</code> otherwise
	 */
	public static boolean isValidProjectKey(String key) {
		return JIRA_PROJECT_KEY_PATTERN.matcher(key).find();
	}

	/**
	 * Validates if the given key {@link String} is a valid JIRA Issue Key
	 * 
	 * @param key the issue key to validate
	 * @return <code>true</code> if valid, <code>false</code> otherwise
	 */
	public static boolean isValidIssueKey(String key) {
		return JIRA_ISSUE_KEY_PATTERN.matcher(key).find();
	}
	
}
