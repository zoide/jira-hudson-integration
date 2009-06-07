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

package com.marvelution.jira.plugins.hudson.portlets;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * TestCase for {@link HudsonProjectStatusShowBuildsValuesGenerator}
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class HudsonProjectStatusShowBuildsValuesGeneratorTest {

	/**
	 * Test the getValues
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testGetValues() {
		final HudsonProjectStatusShowBuildsValuesGenerator generator =
			new TestHudsonProjectStatusShowBuildsValuesGenerator();
		final Map values = generator.getValues(new HashMap());
		assertTrue(values.containsKey("all"));
		assertTrue(values.containsKey("last_build"));
		assertTrue(values.containsKey("last_successful_build"));
		assertTrue(values.containsKey("last_failed_build"));
		assertTrue(values.containsKey("last_unstable_build"));
		assertTrue(values.containsKey("none"));
		assertEquals("hudson.portlet.configuration.show.builds.all", values.get("all"));
		assertEquals("hudson.portlet.configuration.show.builds.only.last", values.get("last_build"));
		assertEquals("hudson.portlet.configuration.show.builds.only.successful", values.get("last_successful_build"));
		assertEquals("hudson.portlet.configuration.show.builds.only.failed", values.get("last_failed_build"));
		assertEquals("hudson.portlet.configuration.show.builds.only.unstable", values.get("last_unstable_build"));
		assertEquals("hudson.portlet.configuration.show.builds.none", values.get("none"));
	}

	/**
	 * Test class to change the getText() implementation
	 * 
	 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
	 */
	public class TestHudsonProjectStatusShowBuildsValuesGenerator extends
																	HudsonProjectStatusShowBuildsValuesGenerator {

		/**
		 * {@inheritDoc}
		 */
		@Override
		String getText(String key) {
			return key;
		}
		
	}

}
