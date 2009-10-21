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

package com.marvelution.jira.plugins.hudson.api.model;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;

import org.junit.Test;

import com.marvelution.jira.plugins.hudson.api.model.Build;

/**
 * TestCase for {@link Build}
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class BuildTest {

	/**
	 * Test {@link Build#compareTo(Build)}
	 */
	@Test
	public void testCompareLess() {
		final Build build = new Build(10, "Test");
		final Build other = new Build(10, "Test");
		final Calendar cal = Calendar.getInstance();
		other.setTimestamp(cal.getTimeInMillis());
		cal.add(Calendar.DAY_OF_YEAR, -1);
		build.setTimestamp(cal.getTimeInMillis());
		assertEquals(-1, build.compareTo(other));
	}

	/**
	 * Test {@link Build#compareTo(Build)}
	 */
	@Test
	public void testCompareEqual() {
		final Build build = new Build(10, "Test");
		final Build other = new Build(10, "Test");
		final Calendar cal = Calendar.getInstance();
		other.setTimestamp(cal.getTimeInMillis());
		build.setTimestamp(cal.getTimeInMillis());
		assertEquals(0, build.compareTo(other));
	}

	/**
	 * Test {@link Build#compareTo(Build)}
	 */
	@Test
	public void testCompareGreater() {
		final Build build = new Build(10, "Test");
		final Build other = new Build(10, "Test");
		final Calendar cal = Calendar.getInstance();
		build.setTimestamp(cal.getTimeInMillis());
		cal.add(Calendar.DAY_OF_YEAR, -1);
		other.setTimestamp(cal.getTimeInMillis());
		assertEquals(1, build.compareTo(other));
	}

}
