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

package com.marvelution.hudson.plugins.apiv2.utils;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Testcase for {@link JiraKeyUtils}
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 *
 * @since 4.4.0
 */
public class JiraKeyUtilsTest {

	private static final String[] ISSUE_KEYS = new String[] {"MARVJIRAHUDSON-1000", "MarvJIRAHudson-100", "marvjirahudson-1"}; 

	/**
	 * Test {@link JiraKeyUtils#isValidProjectKey(String)}
	 */
	@Test
	public void testIsValidProjectKey() {
		assertTrue("Key 'MARVJIRAHUDSON' is not a valid JIRA Project Key", JiraKeyUtils.isValidProjectKey("MARVJIRAHUDSON"));
	}

	/**
	 * Test {@link JiraKeyUtils#isValidProjectKey(String, java.util.regex.Pattern)}
	 */
	@Test
	public void testIsValidProjectKeyDefaultPattern() {
		assertTrue("Key 'MARVJIRAHUDSON' is not a valid JIRA Project Key",
			JiraKeyUtils.isValidProjectKey("MARVJIRAHUDSON", JiraKeyUtils.DEFAULT_JIRA_PROJECT_KEY_PATTERN));
	}

	/**
	 * Test {@link JiraKeyUtils#isValidIssueKey(String)}
	 */
	@Test
	public void testIsValidIssueKey() {
		for (String key : ISSUE_KEYS) {
			assertTrue("Key '" + key + "' is not a valid JIRA Issue Key", JiraKeyUtils.isValidIssueKey(key));
		}
	}

	/**
	 * Test {@link JiraKeyUtils#isValidIssueKey(String)}
	 */
	@Test
	public void testIsValidIssueKeyInvalidKey() {
		assertFalse("Key '11-jira' is not a valid JIRA Issue Key", JiraKeyUtils.isValidIssueKey("11-jira"));
	}

	/**
	 * Test {@link JiraKeyUtils#isValidIssueKey(String, java.util.regex.Pattern)}
	 */
	@Test
	public void testIsValidIssueKeyDefaultPattern() {
		for (String key : ISSUE_KEYS) {
			assertTrue("Key '" + key + "' is not a valid JIRA Issue Key",
				JiraKeyUtils.isValidIssueKey(key, JiraKeyUtils.DEFAULT_JIRA_ISSUE_KEY_PATTERN));
		}
	}

}
