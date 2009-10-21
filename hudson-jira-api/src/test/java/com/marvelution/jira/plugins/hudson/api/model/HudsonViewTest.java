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

import static org.junit.Assert.*;

import org.junit.Test;

import com.marvelution.jira.plugins.hudson.api.model.HudsonView;

/**
 * TestCase for {@link HudsonView}
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class HudsonViewTest {

	/**
	 * Test equals
	 */
	@Test
	public void testEqualsFalse() {
		final HudsonView view1 = new HudsonView("View1", "");
		final HudsonView view2 = new HudsonView("View2", "");
		assertFalse(view1.equals(view2));
	}

	/**
	 * Test equals
	 */
	@Test
	public void testEqualsFalse2() {
		final HudsonView view = new HudsonView("View", "");
		assertFalse(view.equals(new Object()));
	}

	/**
	 * Test equals
	 */
	@Test
	public void testEqualsTrue() {
		final HudsonView view1 = new HudsonView("View1", "");
		final HudsonView view2 = new HudsonView("View1", "");
		assertTrue(view1.equals(view2));
	}

	/**
	 * Test hashCode
	 */
	@Test
	public void testHashCode() {
		final HudsonView view = new HudsonView("View", "");
		assertEquals("View".hashCode(), view.hashCode());
	}

	/**
	 * Test toString
	 */
	@Test
	public void testToString() {
		final HudsonView view = new HudsonView("View", "");
		assertEquals("View", view.toString());
	}

	/**
	 * Test compare
	 */
	@Test
	public void testCompareEquals() {
		final HudsonView view1 = new HudsonView("View 1", "");
		final HudsonView view2 = new HudsonView("View 2", "");
		assertEquals(1, view2.compareTo(view1));
	}

	/**
	 * Test compare
	 */
	@Test
	public void testCompareGreater() {
		final HudsonView view1 = new HudsonView("View 1", "");
		final HudsonView view2 = new HudsonView("View 1", "");
		assertEquals(0, view1.compareTo(view2));
	}

	/**
	 * Test compare
	 */
	@Test
	public void testCompareLess() {
		final HudsonView view1 = new HudsonView("View 10", "");
		final HudsonView view2 = new HudsonView("View 2", "");
		assertEquals(-1, view1.compareTo(view2));
	}

}
