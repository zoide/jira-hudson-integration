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
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.atlassian.jira.config.properties.ApplicationProperties;
import com.atlassian.jira.portal.PortletConfiguration;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.security.PermissionManager;
import com.atlassian.jira.user.util.UserUtil;
import com.atlassian.jira.web.bean.I18nBean;
import com.atlassian.plugin.webresource.WebResourceManager;
import com.marvelution.jira.plugins.hudson.model.HudsonStatusPortletResult;
import com.marvelution.jira.plugins.hudson.model.Job;
import com.marvelution.jira.plugins.hudson.panels.HudsonBuildsTabPanelHelper;
import com.marvelution.jira.plugins.hudson.service.HudsonServer;
import com.marvelution.jira.plugins.hudson.service.HudsonServerAccessor;
import com.marvelution.jira.plugins.hudson.service.HudsonServerAccessorException;
import com.marvelution.jira.plugins.hudson.service.HudsonServerManager;

/**
 * TestCase for {@link HudsonStatusPortlet}
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class HudsonStatusPortletTest {

	private HudsonStatusPortlet portlet;

	@Mock
	private JiraAuthenticationContext authenticationContext;

	@Mock
	private I18nBean i18n;

	@Mock
	private UserUtil userUtil;

	@Mock
	private WebResourceManager webResourceManager;

	@Mock
	private PermissionManager permissionManager;

	@Mock
	private ApplicationProperties applicationProperties;

	@Mock
	private HudsonServerAccessor serverAccessor;

	@Mock
	private HudsonServerManager serverManager;

	@Mock
	private HudsonServer server;

	@Mock
	private PortletConfiguration portletConfiguration;
	
	/**
	 * Setup the test variables
	 * 
	 * @throws Exception in case of failures
	 */
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		when(authenticationContext.getI18nBean()).thenReturn(i18n);
		when(authenticationContext.getI18nBean(anyString())).thenReturn(i18n);
		when(i18n.getText(anyString())).thenAnswer(new Answer<String>() {

			public String answer(InvocationOnMock invocation) throws Throwable {
				return invocation.getArguments()[0].toString();
			}
			
		});
		when(serverManager.getServers()).thenReturn(Collections.singletonList(server));
		portlet =
			new HudsonStatusPortlet(authenticationContext, userUtil, webResourceManager, permissionManager,
				applicationProperties, serverAccessor, serverManager) {

			/**
			 * Empty implementation to get the velocity parameters from he super class
			 *  
			 * @param configuration the {@link PortletConfiguration} instance
			 * @return Empty map
			 */
			protected Map<String, Object> getSuperClassVelocityParams(PortletConfiguration configuration) {
				return new HashMap<String, Object>();
			}

		};
	}

	/**
	 * Test setting all the velocity parameters
	 * 
	 * @throws Exception in case of failures
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testGetVelocityParamsWithHudsonConfigured() throws Exception {
		when(serverManager.isHudsonConfigured()).thenReturn(true);
		final List<Job> jobs = new ArrayList<Job>();
		jobs.add(new Job("Marvelution", "job/Marvelution"));
		jobs.add(new Job("Marvelution-util", "job/Marvelution-util"));
		when(serverAccessor.getProjects(eq(server))).thenReturn(jobs);
		final Map<String, Object> params = portlet.getVelocityParams(portletConfiguration);
		assertTrue(params.containsKey("isHudsonConfigured"));
		assertTrue((Boolean) params.get("isHudsonConfigured"));
		assertTrue(params.containsKey("dateTimeUtils"));
		assertTrue(params.containsKey("buildUtils"));
		assertTrue(params.containsKey("jobUtils"));
		assertTrue(params.containsKey("sorter"));
		assertTrue(params.containsKey("buildTriggerParser"));
		assertTrue(params.containsKey("results"));
		final List<HudsonStatusPortletResult> results = (List<HudsonStatusPortletResult>) params.get("results");
		assertFalse(results.isEmpty());
		assertEquals(1, results.size());
		assertFalse(results.get(0).hasError());
		assertEquals(2, results.get(0).getJobs().size());
		verify(serverManager, VerificationModeFactory.times(2)).isHudsonConfigured();
		verify(webResourceManager, VerificationModeFactory.times(1)).requireResource(
			HudsonBuildsTabPanelHelper.HUDSON_BUILD_PLUGIN + ":portlet-css");
	}

	/**
	 * Test setting all the velocity parameters
	 * 
	 * @throws Exception in case of failures
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testGetVelocityParamsWithHudsonConfiguredThrowingException() throws Exception {
		when(serverManager.isHudsonConfigured()).thenReturn(true);
		when(serverAccessor.getProjects(eq(server))).thenThrow(new HudsonServerAccessorException("Failure"));
		final Map<String, Object> params = portlet.getVelocityParams(portletConfiguration);
		assertTrue(params.containsKey("isHudsonConfigured"));
		assertTrue((Boolean) params.get("isHudsonConfigured"));
		assertTrue(params.containsKey("dateTimeUtils"));
		assertTrue(params.containsKey("buildUtils"));
		assertTrue(params.containsKey("jobUtils"));
		assertTrue(params.containsKey("sorter"));
		assertTrue(params.containsKey("buildTriggerParser"));
		assertTrue(params.containsKey("results"));
		final List<HudsonStatusPortletResult> results = (List<HudsonStatusPortletResult>) params.get("results");
		assertFalse(results.isEmpty());
		assertEquals(1, results.size());
		assertTrue(results.get(0).hasError());
		verify(serverManager, VerificationModeFactory.times(2)).isHudsonConfigured();
		verify(webResourceManager, VerificationModeFactory.times(1)).requireResource(
			HudsonBuildsTabPanelHelper.HUDSON_BUILD_PLUGIN + ":portlet-css");
	}

	/**
	 * Test setting all the velocity parameters
	 * 
	 * @throws Exception in case of failures
	 */
	@Test
	public void testGetVelocityParamsWithoutHudsonConfigured() throws Exception {
		when(serverManager.isHudsonConfigured()).thenReturn(false);
		final List<Job> jobs = new ArrayList<Job>();
		jobs.add(new Job("Marvelution", "job/Marvelution"));
		jobs.add(new Job("Marvelution-util", "job/Marvelution-util"));
		when(serverAccessor.getProjects(eq(server))).thenReturn(jobs);
		final Map<String, Object> params = portlet.getVelocityParams(portletConfiguration);
		assertTrue(params.containsKey("isHudsonConfigured"));
		assertFalse((Boolean) params.get("isHudsonConfigured"));
		assertTrue(params.containsKey("errorMessage"));
		assertFalse(params.containsKey("dateTimeUtils"));
		assertFalse(params.containsKey("buildUtils"));
		assertFalse(params.containsKey("jobUtils"));
		assertFalse(params.containsKey("sorter"));
		assertFalse(params.containsKey("buildTriggerParser"));
		assertFalse(params.containsKey("results"));
		verify(serverManager, VerificationModeFactory.times(2)).isHudsonConfigured();
		verify(webResourceManager, VerificationModeFactory.times(1)).requireResource(
			HudsonBuildsTabPanelHelper.HUDSON_BUILD_PLUGIN + ":portlet-css");
	}

}
