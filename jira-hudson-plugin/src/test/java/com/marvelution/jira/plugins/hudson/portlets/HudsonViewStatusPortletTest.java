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
import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.velocity.tools.generic.SortTool;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.atlassian.configurable.ObjectConfigurationException;
import com.atlassian.jira.config.properties.ApplicationProperties;
import com.atlassian.jira.portal.PortletConfiguration;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.security.PermissionManager;
import com.atlassian.jira.user.util.UserUtil;
import com.atlassian.jira.util.I18nHelper;
import com.atlassian.plugin.webresource.WebResourceManager;
import com.marvelution.jira.plugins.hudson.model.HudsonView;
import com.marvelution.jira.plugins.hudson.model.HudsonViewStatusPortletResult;
import com.marvelution.jira.plugins.hudson.panels.HudsonBuildsTabPanelHelper;
import com.marvelution.jira.plugins.hudson.service.HudsonServer;
import com.marvelution.jira.plugins.hudson.service.HudsonServerAccessDeniedException;
import com.marvelution.jira.plugins.hudson.service.HudsonServerAccessor;
import com.marvelution.jira.plugins.hudson.service.HudsonServerAccessorException;
import com.marvelution.jira.plugins.hudson.service.HudsonServerManager;
import com.marvelution.jira.plugins.hudson.utils.BuildUtils;
import com.marvelution.jira.plugins.hudson.utils.DateTimeUtils;
import com.marvelution.jira.plugins.hudson.utils.HudsonBuildTriggerParser;
import com.marvelution.jira.plugins.hudson.utils.JobUtils;

/**
 * TestCase for {@link HudsonViewStatusPortlet}
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class HudsonViewStatusPortletTest {

	@Mock
	private JiraAuthenticationContext authenticationContext;

	@Mock
	private I18nHelper i18n;

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
	private HudsonView view;

	@Mock
	private PortletConfiguration portletConfiguration;

	private HudsonViewStatusPortlet portlet;

	/**
	 * Setup the test variables
	 * 
	 * @throws Exception in case of failures
	 */
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		when(authenticationContext.getI18nHelper()).thenReturn(i18n);
		when(i18n.getText(anyString())).thenAnswer(new Answer<String>() {

			public String answer(InvocationOnMock invocation) throws Throwable {
				return invocation.getArguments()[0].toString();
			}
			
		});
		portlet =
			new HudsonViewStatusPortlet(authenticationContext, userUtil, webResourceManager, permissionManager,
				applicationProperties, serverAccessor, serverManager) {

			/**
			 * Empty implementation to get the velocity parameters from he super class
			 *  
			 * @param configuration the {@link PortletConfiguration} instance
			 * @return Empty map
			 */
			@Override
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
	public void testGetVelocityParams() throws Exception {
		when(serverManager.isHudsonConfigured()).thenReturn(true);
		when(serverManager.getServer(1)).thenReturn(server);
		when(portletConfiguration.getProperty("hudsonView")).thenReturn("1;view:All");
		when(serverAccessor.getView(server, "All")).thenReturn(view);
		when(view.getName()).thenReturn("All");
		when(view.getDescription()).thenReturn("");
		when(view.getJobs()).thenReturn(Collections.EMPTY_LIST);
		final Map<String, Object> params = portlet.getVelocityParams(portletConfiguration);
		assertTrue(params.containsKey("result"));
		assertThat(params.get("result"), is(HudsonViewStatusPortletResult.class));
		final HudsonViewStatusPortletResult result = (HudsonViewStatusPortletResult) params.get("result");
		assertFalse(result.hasError());
		assertThat(result.getViewName(), is("All"));
		assertThat(result.getViewDescription(), is(""));
		assertThat(result.getServer(), is(server));
		verifyCommonVelocityParameters(params);
		verify(portletConfiguration, VerificationModeFactory.times(1)).getProperty("hudsonView");
		verify(serverManager, VerificationModeFactory.times(3)).isHudsonConfigured();
		verify(serverManager, VerificationModeFactory.times(1)).getServer(1);
		verify(serverAccessor, VerificationModeFactory.times(1)).getView(server, "All");
		verify(view, VerificationModeFactory.times(2)).getName();
		verify(view, VerificationModeFactory.times(1)).getDescription();
		verify(view, VerificationModeFactory.times(1)).getJobs();
		verify(webResourceManager, VerificationModeFactory.times(1)).requireResource(
			HudsonBuildsTabPanelHelper.HUDSON_BUILD_PLUGIN + ":portlet-css");
	}

	/**
	 * Test setting all the velocity parameters
	 * 
	 * @throws Exception in case of failures
	 */
	@Test
	public void testGetVelocityParamsViewNotFound() throws Exception {
		when(serverManager.isHudsonConfigured()).thenReturn(true);
		when(serverManager.getServer(1)).thenReturn(server);
		when(portletConfiguration.getProperty("hudsonView")).thenReturn("1;view:All");
		when(serverAccessor.getView(server, "All")).thenReturn(view);
		when(view.getName()).thenReturn(null);
		final Map<String, Object> params = portlet.getVelocityParams(portletConfiguration);
		assertTrue(params.containsKey("result"));
		assertThat(params.get("result"), is(HudsonViewStatusPortletResult.class));
		final HudsonViewStatusPortletResult result = (HudsonViewStatusPortletResult) params.get("result");
		assertTrue(result.hasError());
		assertThat(result.getError(), is("hudson.error.portlet.no.view.found"));
		assertThat(result.getViewName(), nullValue());
		assertThat(result.getViewDescription(), nullValue());
		assertThat(result.getServer(), is(server));
		verifyCommonVelocityParameters(params);
		verify(portletConfiguration, VerificationModeFactory.times(1)).getProperty("hudsonView");
		verify(serverManager, VerificationModeFactory.times(3)).isHudsonConfigured();
		verify(serverManager, VerificationModeFactory.times(1)).getServer(1);
		verify(serverAccessor, VerificationModeFactory.times(1)).getView(server, "All");
		verify(view, VerificationModeFactory.times(1)).getName();
		verify(view, VerificationModeFactory.times(0)).getDescription();
		verify(view, VerificationModeFactory.times(0)).getJobs();
		verify(webResourceManager, VerificationModeFactory.times(1)).requireResource(
			HudsonBuildsTabPanelHelper.HUDSON_BUILD_PLUGIN + ":portlet-css");
	}

	/**
	 * Test setting all the velocity parameters
	 * 
	 * @throws Exception in case of failures
	 */
	@Test
	public void testGetVelocityParamsWithoutHudsonConfigurated() throws Exception {
		when(serverManager.isHudsonConfigured()).thenReturn(false);
		final Map<String, Object> params = portlet.getVelocityParams(portletConfiguration);
		assertFalse(params.containsKey("result"));
		verifyCommonVelocityParameters(params);
		verify(serverManager, VerificationModeFactory.times(3)).isHudsonConfigured();
		verify(webResourceManager, VerificationModeFactory.times(1)).requireResource(
			HudsonBuildsTabPanelHelper.HUDSON_BUILD_PLUGIN + ":portlet-css");
	}

	/**
	 * Test setting all the velocity parameters
	 * 
	 * @throws Exception in case of failures
	 */
	@Test
	public void testGetVelocityParamsObjectConfigurationException() throws Exception {
		when(serverManager.isHudsonConfigured()).thenReturn(true);
		when(portletConfiguration.getProperty("hudsonView")).thenThrow(new ObjectConfigurationException("Failure"));
		final Map<String, Object> params = portlet.getVelocityParams(portletConfiguration);
		assertTrue(params.containsKey("result"));
		assertThat(params.get("result"), is(HudsonViewStatusPortletResult.class));
		final HudsonViewStatusPortletResult result = (HudsonViewStatusPortletResult) params.get("result");
		assertTrue(result.hasError());
		assertThat(result.getError(), is("hudson.error.portlet.invalid.configuration"));
		assertThat(result.getViewName(), nullValue());
		assertThat(result.getViewDescription(), nullValue());
		assertThat(result.getServer(), nullValue());
		verifyCommonVelocityParameters(params);
		verify(portletConfiguration, VerificationModeFactory.times(1)).getProperty("hudsonView");
		verify(serverManager, VerificationModeFactory.times(3)).isHudsonConfigured();
		verify(serverManager, VerificationModeFactory.noMoreInteractions()).getServer(1);
		verify(webResourceManager, VerificationModeFactory.times(1)).requireResource(
			HudsonBuildsTabPanelHelper.HUDSON_BUILD_PLUGIN + ":portlet-css");
	}

	/**
	 * Test setting all the velocity parameters
	 * 
	 * @throws Exception in case of failures
	 */
	@Test
	public void testGetVelocityParamsNullPointerException() throws Exception {
		when(serverManager.isHudsonConfigured()).thenReturn(true);
		when(portletConfiguration.getProperty("hudsonView")).thenReturn("INVALID;view:All");
		final Map<String, Object> params = portlet.getVelocityParams(portletConfiguration);
		assertTrue(params.containsKey("result"));
		assertThat(params.get("result"), is(HudsonViewStatusPortletResult.class));
		final HudsonViewStatusPortletResult result = (HudsonViewStatusPortletResult) params.get("result");
		assertTrue(result.hasError());
		assertThat(result.getError(), is("hudson.error.portlet.invalid.configuration"));
		assertThat(result.getViewName(), nullValue());
		assertThat(result.getViewDescription(), nullValue());
		assertThat(result.getServer(), nullValue());
		verifyCommonVelocityParameters(params);
		verify(portletConfiguration, VerificationModeFactory.times(1)).getProperty("hudsonView");
		verify(serverManager, VerificationModeFactory.times(3)).isHudsonConfigured();
		verify(serverManager, VerificationModeFactory.noMoreInteractions()).getServer(1);
		verify(webResourceManager, VerificationModeFactory.times(1)).requireResource(
			HudsonBuildsTabPanelHelper.HUDSON_BUILD_PLUGIN + ":portlet-css");
	}

	/**
	 * Test setting all the velocity parameters
	 * 
	 * @throws Exception in case of failures
	 */
	@Test
	public void testGetVelocityParamsHudsonServerAccessorException() throws Exception {
		when(serverManager.isHudsonConfigured()).thenReturn(true);
		when(serverManager.getServer(1)).thenReturn(server);
		when(portletConfiguration.getProperty("hudsonView")).thenReturn("1;view:All");
		when(serverAccessor.getView(server, "All")).thenThrow(new HudsonServerAccessorException("Failure"));
		final Map<String, Object> params = portlet.getVelocityParams(portletConfiguration);
		assertTrue(params.containsKey("result"));
		assertThat(params.get("result"), is(HudsonViewStatusPortletResult.class));
		final HudsonViewStatusPortletResult result = (HudsonViewStatusPortletResult) params.get("result");
		assertTrue(result.hasError());
		assertThat(result.getError(), is("hudson.error.cannot.connect"));
		assertThat(result.getViewName(), nullValue());
		assertThat(result.getViewDescription(), nullValue());
		assertThat(result.getServer(), is(server));
		verifyCommonVelocityParameters(params);
		verify(portletConfiguration, VerificationModeFactory.times(1)).getProperty("hudsonView");
		verify(serverManager, VerificationModeFactory.times(3)).isHudsonConfigured();
		verify(serverManager, VerificationModeFactory.times(1)).getServer(1);
		verify(serverAccessor, VerificationModeFactory.times(1)).getView(server, "All");
		verify(webResourceManager, VerificationModeFactory.times(1)).requireResource(
			HudsonBuildsTabPanelHelper.HUDSON_BUILD_PLUGIN + ":portlet-css");
	}

	/**
	 * Test setting all the velocity parameters
	 * 
	 * @throws Exception in case of failures
	 */
	@Test
	public void testGetVelocityParamsHudsonServerAccessDeniedException() throws Exception {
		when(serverManager.isHudsonConfigured()).thenReturn(true);
		when(serverManager.getServer(1)).thenReturn(server);
		when(portletConfiguration.getProperty("hudsonView")).thenReturn("1;view:All");
		when(serverAccessor.getView(server, "All")).thenThrow(new HudsonServerAccessDeniedException("Failure"));
		final Map<String, Object> params = portlet.getVelocityParams(portletConfiguration);
		assertTrue(params.containsKey("result"));
		assertThat(params.get("result"), is(HudsonViewStatusPortletResult.class));
		final HudsonViewStatusPortletResult result = (HudsonViewStatusPortletResult) params.get("result");
		assertTrue(result.hasError());
		assertThat(result.getError(), is("hudson.error.access.denied"));
		assertThat(result.getViewName(), nullValue());
		assertThat(result.getViewDescription(), nullValue());
		assertThat(result.getServer(), is(server));
		verifyCommonVelocityParameters(params);
		verify(portletConfiguration, VerificationModeFactory.times(1)).getProperty("hudsonView");
		verify(serverManager, VerificationModeFactory.times(3)).isHudsonConfigured();
		verify(serverManager, VerificationModeFactory.times(1)).getServer(1);
		verify(serverAccessor, VerificationModeFactory.times(1)).getView(server, "All");
		verify(webResourceManager, VerificationModeFactory.times(1)).requireResource(
			HudsonBuildsTabPanelHelper.HUDSON_BUILD_PLUGIN + ":portlet-css");
	}

	/**
	 * Verify the common velocity parameters
	 * 
	 * @param params the Velocity Parameters to verify
	 */
	private void verifyCommonVelocityParameters(Map<String, Object> params) {
		if (serverManager.isHudsonConfigured()) {
			assertTrue(params.containsKey("isHudsonConfigured"));
			assertThat((Boolean) params.get("isHudsonConfigured"), is(true));
			assertTrue(params.containsKey("dateTimeUtils"));
			assertThat(params.get("dateTimeUtils"), is(DateTimeUtils.class));
			assertTrue(params.containsKey("buildUtils"));
			assertThat(params.get("buildUtils"), is(BuildUtils.class));
			assertTrue(params.containsKey("jobUtils"));
			assertThat(params.get("jobUtils"), is(JobUtils.class));
			assertTrue(params.containsKey("sorter"));
			assertThat(params.get("sorter"), is(SortTool.class));
			assertTrue(params.containsKey("buildTriggerParser"));
			assertThat(params.get("buildTriggerParser"), is(HudsonBuildTriggerParser.class));
		} else {
			assertTrue(params.containsKey("isHudsonConfigured"));
			assertThat((Boolean) params.get("isHudsonConfigured"), is(false));
			assertTrue(params.containsKey("errorMessage"));
			assertThat((String) params.get("errorMessage"), is("hudson.error.not.configured"));
		}
	}

}
