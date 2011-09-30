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

import java.net.URL;
import java.util.Iterator;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import net.sf.json.util.JSONUtils;

import org.junit.Test;
import org.jvnet.hudson.test.HudsonTestCase;
import org.jvnet.hudson.test.recipes.TestData;
import org.jvnet.hudson.test.recipes.TestData.DataSet;
import org.mortbay.jetty.HttpStatus;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.Page;

/**
 * {@link HudsonTestCase} implementation for the /apiv2/views resource
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 *
 *@since 4.2.0
 */
public class ViewRestResourceImplTest extends TestBastRestResource {

	/**
	 * Test the apiv2/views?name=All GET request with anonymous having read only access in Hudson
	 * 
	 * @throws Exception in case of error
	 */
	@Test
	@TestData(DataSet.ANONYMOUS_READ_ONLY_WITH_JOBS)
	public void testGetViewAllAnonymousReadAccess() throws Exception {
		WebClient client = createWebClient();
		Page page = client.getPage(new URL(client.getContextPath() + "apiv2/views?name=All"));
		assertThat(page.getWebResponse().getContentType(), is("application/json"));
		assertThat(JSONUtils.mayBeJSON(page.getWebResponse().getContentAsString()), is(true));
		JSONObject json = (JSONObject) JSONSerializer.toJSON(page.getWebResponse().getContentAsString());
		assertAllView(json);
	}

	/**
	 * Test the apiv2/views?name=Test GET request with anonymous having read only access in Hudson
	 * 
	 * @throws Exception in case of error
	 */
	@Test
	@TestData(DataSet.ANONYMOUS_READ_ONLY_WITH_JOBS)
	public void testGetViewTestAnonymousReadAccess() throws Exception {
		WebClient client = createWebClient();
		Page page = client.getPage(new URL(client.getContextPath() + "apiv2/views?name=Test"));
		assertThat(page.getWebResponse().getContentType(), is("application/json"));
		assertThat(JSONUtils.mayBeJSON(page.getWebResponse().getContentAsString()), is(true));
		JSONObject json = (JSONObject) JSONSerializer.toJSON(page.getWebResponse().getContentAsString());
		assertTestView(json);
	}

	/**
	 * Test the apiv2/views?name=All GET request with anonymous having no access in Hudson
	 * 
	 * @throws Exception in case of error
	 */
	@Test
	@TestData(DataSet.ANONYMOUS_NO_ACCESS_WITH_JOBS)
	public void testGetViewAnonymousNoAccess() throws Exception {
		WebClient client = createWebClient();
		try {
			client.getPage(new URL(client.getContextPath() + "apiv2/views?name=All"));
			fail("The Hudson instance is secured without any anonymous access, look at the test data!");
		} catch (FailingHttpStatusCodeException e) {
			assertThat(e.getStatusCode(), is(HttpStatus.ORDINAL_403_Forbidden));
		}
	}

	/**
	 * Test the apiv2/views?name=All GET request with anonymous having no access in Hudson and using account admin
	 * 
	 * @throws Exception in case of error
	 */
	@Test
	@TestData(DataSet.ANONYMOUS_NO_ACCESS_WITH_JOBS)
	public void testGetViewAnonymousNoAccessWithAccount() throws Exception {
		WebClient client = createWebClient();
		client.login("admin");
		Page page = client.getPage(new URL(client.getContextPath() + "apiv2/views?name=All"));
		assertThat(page.getWebResponse().getContentType(), is("application/json"));
		assertThat(JSONUtils.mayBeJSON(page.getWebResponse().getContentAsString()), is(true));
		JSONObject json = (JSONObject) JSONSerializer.toJSON(page.getWebResponse().getContentAsString());
		assertAllView(json);
	}

	/**
	 * Test the apiv2/views?name=XYZ GET request with anonymous having read only access in Hudson
	 * But with an invalid view name
	 * 
	 * @throws Exception in case of error
	 */
	@Test
	@TestData(DataSet.ANONYMOUS_READ_ONLY_WITH_JOBS)
	public void testGetViewInvalidViewName() throws Exception {
		WebClient client = createWebClient();
		try {
			client.getPage(new URL(client.getContextPath() + "apiv2/views?name=XYZ"));
			fail("The Hudson instance doesn't have the view named XYZ, look at the test data!");
		} catch (FailingHttpStatusCodeException e) {
			assertThat(e.getStatusCode(), is(HttpStatus.ORDINAL_404_Not_Found));
		}
	}

	/**
	 * Helper method to assert the All view response
	 * 
	 * @param json the {@link JSONObject} to assert
	 */
	private void assertAllView(JSONObject json) {
		assertThat(json.getString("name"), is("All"));
		assertThat(json.getString("url"), is(""));
		JSONArray jobs = json.getJSONArray("Jobs");
		assertThat(jobs.size(), is(3));
		@SuppressWarnings("unchecked")
		Iterator<JSONObject> iterator = jobs.iterator();
		while (iterator.hasNext()) {
			JSONObject job = iterator.next();
			assertJob(job);
		}
	}

	/**
	 * Helper method to assert the Test view response
	 * 
	 * @param json the {@link JSONObject} to assert
	 */
	private void assertTestView(JSONObject json) {
		assertThat(json.getString("name"), is("Test"));
		assertThat(json.getString("url"), is("view/Test/"));
		JSONArray jobs = json.getJSONArray("Jobs");
		assertThat(jobs.size(), is(0));
	}

}
