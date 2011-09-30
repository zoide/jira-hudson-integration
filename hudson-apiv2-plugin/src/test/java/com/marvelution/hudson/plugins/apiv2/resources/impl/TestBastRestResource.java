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

package com.marvelution.hudson.plugins.apiv2.resources.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import net.sf.json.JSONObject;

import org.jvnet.hudson.test.HudsonTestCase;

/**
 * Base {@link HudsonTestCase} for all RestREsource tests
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 *
 * @since 4.2.0
 */
public abstract class TestBastRestResource extends HudsonTestCase {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public WebClient createWebClient() {
		WebClient client = super.createWebClient();
		client.setPrintContentOnFailingStatusCode(false);
		return client;
	}

	/**
	 * Assert the given JSON Job
	 * 
	 * @param json the {@link JSONObject} representing a Job
	 */
	protected void assertJob(JSONObject json) {
		if (json.getString("name").equalsIgnoreCase("free-style")) {
			assertFreeStyleJob(json);
		} else if (json.getString("name").equalsIgnoreCase("maven2")) {
			assertMaven2Job(json);
		}else if (json.getString("name").equalsIgnoreCase("no-builds")) {
			assertNoBuildsJob(json);
		} else {
			fail("Unsupported Job: " + json.getString("name"));
		}
	}

	/**
	 * Assert the test data free-style job
	 * 
	 * @param json the {@link JSONObject} to assert
	 */
	protected void assertFreeStyleJob(JSONObject json) {
		assertThat(json.getString("name"), is("free-style"));
		assertThat(json.getString("description"), is(""));
		assertThat(json.getString("url"), is("job/free-style/"));
		assertThat(json.getBoolean("buildable"), is(true));
		assertThat(json.getJSONArray("Builds").size(), is(2));
	}

	/**
	 * Assert the test data maven2 job
	 * 
	 * @param json the {@link JSONObject} to assert
	 */
	protected void assertMaven2Job(JSONObject json) {
		assertThat(json.getString("name"), is("maven2"));
		assertThat(json.getString("description"), is(""));
		assertThat(json.getString("url"), is("job/maven2/"));
		assertThat(json.getBoolean("buildable"), is(true));
		assertThat(json.getJSONArray("Builds").size(), is(1));
	}

	/**
	 * Assert the test data no-builds job
	 * 
	 * @param json the {@link JSONObject} to assert
	 */
	protected void assertNoBuildsJob(JSONObject json) {
		assertThat(json.getString("name"), is("no-builds"));
		assertThat(json.getString("description"), is(""));
		assertThat(json.getString("url"), is("job/no-builds/"));
		assertThat(json.getBoolean("buildable"), is(true));
		assertThat(json.getJSONArray("Builds").size(), is(0));
	}

}
