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
import com.marvelution.hudson.plugins.apiv2.utils.HudsonPluginUtils;

/**
 * {@link HudsonTestCase} implementation to test the /apiv2/plugin resource
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 * 
 * @since 4.2.0
 */
public class PluginRestResourceImplTest extends TestBastRestResource {

	/**
	 * Test the apiv2/plugin/version GET request with anonymous having full control in Hudson
	 * 
	 * @throws Exception in case of error
	 */
	@Test
	@TestData(DataSet.ANONYMOUS_FULL_CONTROL)
	public void testAnonymousFullControl() throws Exception {
		WebClient client = createWebClient();
		Page page = client.getPage(new URL(client.getContextPath() + "apiv2/plugin/version"));
		assertThat(page.getWebResponse().getContentType(), is("application/json"));
		assertThat(JSONUtils.mayBeJSON(page.getWebResponse().getContentAsString()), is(true));
		JSONObject json = (JSONObject) JSONSerializer.toJSON(page.getWebResponse().getContentAsString());
		assertPluginVersionResponse(json);
	}

	/**
	 * Test the apiv2/plugin/version GET request with anonymous having read only access in Hudson
	 * 
	 * @throws Exception in case of error
	 */
	@Test
	@TestData(DataSet.ANONYMOUS_READ_ONLY)
	public void testAnonymousReadOnly() throws Exception {
		WebClient client = createWebClient();
		Page page = client.getPage(new URL(client.getContextPath() + "apiv2/plugin/version"));
		assertThat(page.getWebResponse().getContentType(), is("application/json"));
		assertThat(JSONUtils.mayBeJSON(page.getWebResponse().getContentAsString()), is(true));
		JSONObject json = (JSONObject) JSONSerializer.toJSON(page.getWebResponse().getContentAsString());
		assertPluginVersionResponse(json);
	}

	/**
	 * Test the apiv2/plugin/version GET request with anonymous having read only access in Hudson and logging in with
	 * user admin
	 * 
	 * @throws Exception in case of error
	 */
	@Test
	@TestData(DataSet.ANONYMOUS_READ_ONLY)
	public void testAnonymousReadOnlyWithAccount() throws Exception {
		WebClient client = createWebClient();
		client.login("admin");
		Page page = client.getPage(new URL(client.getContextPath() + "apiv2/plugin/version"));
		assertThat(page.getWebResponse().getContentType(), is("application/json"));
		assertThat(JSONUtils.mayBeJSON(page.getWebResponse().getContentAsString()), is(true));
		JSONObject json = (JSONObject) JSONSerializer.toJSON(page.getWebResponse().getContentAsString());
		assertPluginVersionResponse(json);
	}

	/**
	 * Test the apiv2/plugin/version GET request with anonymous having no access in Hudson
	 * 
	 * @throws Exception in case of error
	 */
	@Test
	@TestData(DataSet.ANONYMOUS_NO_ACCESS)
	public void testAnonymousNoAccess() throws Exception {
		WebClient client = createWebClient();
		try {
			client.getPage(new URL(client.getContextPath() + "apiv2/plugin/version"));
			fail("The Hudson instance is secured without any anonymous access, look at the test data!");
		} catch (FailingHttpStatusCodeException e) {
			assertThat(e.getStatusCode(), is(HttpStatus.ORDINAL_403_Forbidden));
		}
	}

	/**
	 * Test the apiv2/plugin/version GET request with anonymous having no access in Hudson and logging in with
	 * user admin
	 * 
	 * @throws Exception in case of error
	 */
	@Test
	@TestData(DataSet.ANONYMOUS_NO_ACCESS)
	public void testAnonymousNoAccessWithAccount() throws Exception {
		WebClient client = createWebClient();
		client.login("admin");
		Page page = client.getPage(new URL(client.getContextPath() + "apiv2/plugin/version"));
		assertThat(page.getWebResponse().getContentType(), is("application/json"));
		assertThat(JSONUtils.mayBeJSON(page.getWebResponse().getContentAsString()), is(true));
		JSONObject json = (JSONObject) JSONSerializer.toJSON(page.getWebResponse().getContentAsString());
		assertPluginVersionResponse(json);
	}

	/**
	 * Helper method to assert the {@link JSONObject} response
	 * 
	 * @param json the {@link JSONObject} response to check
	 */
	private void assertPluginVersionResponse(JSONObject json) {
		assertThat(json.getString("hudsonSystem"), is("HUDSON"));
		assertThat(json.getString("hudsonVersion"), is("1.395"));
		assertThat(json.getString("pluginGroupId"), is(HudsonPluginUtils.getPluginGroupId()));
		assertThat(json.getString("pluginArtifactId"), is(HudsonPluginUtils.getPluginArifactId()));
		assertThat(json.getString("pluginVersion"), is(HudsonPluginUtils.getPluginVersion()));
	}

}
