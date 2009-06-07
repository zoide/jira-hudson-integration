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

import java.util.Calendar;

import org.junit.Before;
import org.junit.Test;

import com.marvelution.jira.plugins.hudson.model.Build;
import com.marvelution.jira.plugins.hudson.model.Job;

/**
 * TestCase for {@link JobUtils}
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class JobUtilsTest {

	private Job job;

	private JobUtils utils;

	/**
	 * Prepare tests
	 */
	@Before
	public void beforeTests() {
		utils = new JobUtils();
		job = new Job("Test", "Test Project");
		final Build build = new Build(1, "Test");
		build.setDuration(32L);
		build.setTimestamp(Calendar.getInstance().getTimeInMillis());
		job.setFirstBuild(build);
		job.setLastBuild(build);
		job.setLastCompletedBuild(build);
		job.setLastFailedBuild(build);
		job.setLastStableBuild(build);
		job.setLastSuccessfulBuild(build);
		job.setLastUnstableBuild(build);
	}

	/**
	 * Test valid/invalid first build
	 */
	@Test
	public void testHasValidFirstBuild() {
		assertTrue(utils.hasValidFirstBuild(job));
		job.setFirstBuild(null);
		assertFalse(utils.hasValidFirstBuild(job));
	}

	/**
	 * Test valid/invalid last build
	 */
	@Test
	public void testHasValidLastBuild() {
		assertTrue(utils.hasValidLastBuild(job));
		job.setLastBuild(null);
		assertFalse(utils.hasValidLastBuild(job));
	}

	/**
	 * Test valid/invalid last successful build
	 */
	@Test
	public void testHasValidLastSuccessfulBuild() {
		assertTrue(utils.hasValidLastSuccessfulBuild(job));
		job.setLastSuccessfulBuild(null);
		assertFalse(utils.hasValidLastSuccessfulBuild(job));
	}

	/**
	 * Test valid/invalid last failed build
	 */
	@Test
	public void testHasValidLastFailedBuild() {
		assertTrue(utils.hasValidLastFailedBuild(job));
		job.setLastFailedBuild(null);
		assertFalse(utils.hasValidLastFailedBuild(job));
	}

	/**
	 * Test valid/invalid last stable build
	 */
	@Test
	public void testHasValidLastStableBuild() {
		assertTrue(utils.hasValidLastStableBuild(job));
		job.setLastStableBuild(null);
		assertFalse(utils.hasValidLastStableBuild(job));
	}

	/**
	 * Test valid/invalid last unstable build
	 */
	@Test
	public void testHasValidLastUnstableBuild() {
		assertTrue(utils.hasValidLastUnstableBuild(job));
		job.setLastUnstableBuild(null);
		assertFalse(utils.hasValidLastUnstableBuild(job));
	}

	/**
	 * Test valid/invalid last completed build
	 */
	@Test
	public void testHasValidLastCompletedBuild() {
		assertTrue(utils.hasValidLastCompletedBuild(job));
		job.setLastCompletedBuild(null);
		assertFalse(utils.hasValidLastCompletedBuild(job));
	}

}
