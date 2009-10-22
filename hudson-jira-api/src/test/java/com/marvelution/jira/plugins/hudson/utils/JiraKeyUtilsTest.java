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

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.Test;

/**
 * TestCase for {@link JiraKeyUtils}
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class JiraKeyUtilsTest {

	/**
	 * Test validating a valid project key
	 */
	@Test
	public void testValidProjectKey() {
		assertTrue(JiraKeyUtils.isValidProjectKey("MARVADMIN"));
	}

	/**
	 * Test invalidating a valid project key
	 */
	@Test
	public void testInvalidProjectKey() {
		assertFalse(JiraKeyUtils.isValidProjectKey("MARVADMIN2"));
		assertFalse(JiraKeyUtils.isValidProjectKey("MARVADMIN-2"));
		assertFalse(JiraKeyUtils.isValidProjectKey("marvADMIN"));
	}

	/**
	 * Test get all Project keys for text
	 */
	@Test
	public void testGetProjectKeysFromText() {
		final String text = "This text contains two Project keys: MARVADMIN, MARVSITE";
		final Set<String> keys = JiraKeyUtils.getJiraProjectKeysFromText(text);
		assertTrue(keys.size() == 2);
		assertTrue(keys.contains("MARVADMIN"));
		assertTrue(keys.contains("MARVSITE"));
	}

	/**
	 * Test validating a valid issue key
	 */
	@Test
	public void testValidIssueKey() {
		assertTrue(JiraKeyUtils.isValidIssueKey("MARVADMIN-2"));
		assertTrue(JiraKeyUtils.isValidIssueKey("MARVADMIN-20"));
		assertFalse(JiraKeyUtils.isValidIssueKey("MARVadmin2"));
	}

	/**
	 * Test validating a invalid issue key
	 */
	@Test
	public void testInvalidIssueKey() {
		assertFalse(JiraKeyUtils.isValidIssueKey("MARVADMIN2"));
		assertFalse(JiraKeyUtils.isValidIssueKey("2-MARVADMIN"));
	}

	/**
	 * Test get all Issue keys for text
	 */
	@Test
	public void testGetIssueKeysFromText() {
		final String text = "This text contains two Issue keys: MARVADMIN-1, MARVSITE-10, marvadmin-2";
		final Set<String> keys = JiraKeyUtils.getJiraIssueKeysFromText(text);
		assertTrue(keys.size() == 3);
		assertTrue(keys.contains("MARVADMIN-1"));
		assertTrue(keys.contains("MARVSITE-10"));
		assertTrue(keys.contains("marvadmin-2"));
	}

}
