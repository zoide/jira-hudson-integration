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

import com.marvelution.jira.plugins.hudson.api.model.Build;
import com.marvelution.jira.plugins.hudson.api.model.Result;

/**
 * TestCase for {@link BuildUtils}
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class BuildUtilsTest {

	private Build build;

	private BuildUtils buildUtils;

	/**
	 * Setup {@link Build} to test
	 */
	@Before
	public void beforeTests() {
		buildUtils = new BuildUtils();
		build = new Build(1, "Test");
		build.setDuration(32L);
		build.setTimestamp(Calendar.getInstance().getTimeInMillis());
	}

	/**
	 * Test valid and invalid build
	 */
	@Test
	public void testIsValidBuild() {
		assertTrue(buildUtils.isValidBuild(build));
		build.setDuration(0L);
		assertFalse(buildUtils.isValidBuild(build));
	}

	/**
	 * Test build
	 */
	@Test
	public void testIsSuccessfulBuild() {
		build.setResult(Result.SUCCESS);
		assertTrue(buildUtils.isSuccessfulBuild(build));
		build.setResult(Result.FAILURE);
		assertFalse(buildUtils.isSuccessfulBuild(build));
	}

	/**
	 * Test build
	 */
	@Test
	public void testIsFailedBuild() {
		build.setResult(Result.FAILURE);
		assertTrue(buildUtils.isFailedBuild(build));
		build.setResult(Result.SUCCESS);
		assertFalse(buildUtils.isFailedBuild(build));
	}

	/**
	 * Test build
	 */
	@Test
	public void testIsUnstableBuild() {
		build.setResult(Result.UNSTABLE);
		assertTrue(buildUtils.isUnstableBuild(build));
		build.setResult(Result.FAILURE);
		assertFalse(buildUtils.isUnstableBuild(build));
	}

	/**
	 * Test build
	 */
	@Test
	public void testIsAbortedBuild() {
		build.setResult(Result.ABORTED);
		assertTrue(buildUtils.isAbortedBuild(build));
		build.setResult(Result.FAILURE);
		assertFalse(buildUtils.isAbortedBuild(build));
	}

	/**
	 * Test build
	 */
	@Test
	public void testIsNotBuild() {
		build.setResult(Result.NOT_BUILT);
		assertTrue(buildUtils.isNotBuild(build));
		build.setResult(Result.FAILURE);
		assertFalse(buildUtils.isNotBuild(build));
	}

}
