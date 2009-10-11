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

import java.util.HashMap;
import java.util.Map;

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
import com.atlassian.jira.project.Project;
import com.atlassian.jira.project.ProjectManager;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.security.PermissionManager;
import com.atlassian.jira.user.util.UserUtil;
import com.atlassian.jira.util.I18nHelper;
import com.atlassian.plugin.webresource.WebResourceManager;
import com.marvelution.jira.plugins.hudson.model.HudsonProjectPortletResult;
import com.marvelution.jira.plugins.hudson.model.Job;
import com.marvelution.jira.plugins.hudson.panels.HudsonBuildsTabPanelHelper;
import com.marvelution.jira.plugins.hudson.service.HudsonServer;
import com.marvelution.jira.plugins.hudson.service.HudsonServerAccessDeniedException;
import com.marvelution.jira.plugins.hudson.service.HudsonServerAccessor;
import com.marvelution.jira.plugins.hudson.service.HudsonServerAccessorException;
import com.marvelution.jira.plugins.hudson.service.HudsonServerManager;


/**
 * TestCase for {@link HudsonProjectStatusPortlet}
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class HudsonProjectStatusPortletTest {

	private HudsonProjectStatusPortlet portlet;

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
	private ProjectManager projectManager;
	
	@Mock
	private Project project;

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
	 * Setup test variables
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
		when(serverManager.getServerByJiraProject(eq(project))).thenReturn(server);
		portlet =
			new HudsonProjectStatusPortlet(authenticationContext, userUtil, webResourceManager, permissionManager,
				applicationProperties, projectManager, serverAccessor, serverManager) {

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
	@Test
	public void testGetVelocityParamsWithHudsonConfigured() throws Exception {
		when(serverManager.isHudsonConfigured()).thenReturn(true);
		when(serverAccessor.getProject(eq(server), eq(project))).thenReturn(
			new Job("Marvelution", "job/Marvelution/"));
		when(portletConfiguration.getLongProperty("projectId")).thenReturn(1000L);
		when(projectManager.getProjectObj(1000L)).thenReturn(project);
		final Map<String, Object> params = portlet.getVelocityParams(portletConfiguration);
		assertTrue(params.containsKey("isHudsonConfigured"));
		assertTrue((Boolean) params.get("isHudsonConfigured"));
		assertTrue(params.containsKey("dateTimeUtils"));
		assertTrue(params.containsKey("buildUtils"));
		assertTrue(params.containsKey("jobUtils"));
		assertTrue(params.containsKey("sorter"));
		assertTrue(params.containsKey("buildTriggerParser"));
		assertTrue(params.containsKey("result"));
		final HudsonProjectPortletResult result = (HudsonProjectPortletResult) params.get("result");
		assertFalse(result.hasError());
		assertTrue(result.hasJob());
		assertEquals("Marvelution", result.getJob().getName());
		assertEquals(project, result.getProject());
		assertEquals(result.getServerLargeImageUrl(), result.getServerImageUrl());
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
	public void testGetVelocityParamsWithHudsonConfiguredNoJob() throws Exception {
		when(serverManager.isHudsonConfigured()).thenReturn(true);
		when(serverAccessor.getProject(eq(server), eq(project))).thenReturn(null);
		when(portletConfiguration.getLongProperty("projectId")).thenReturn(1000L);
		when(projectManager.getProjectObj(1000L)).thenReturn(project);
		final Map<String, Object> params = portlet.getVelocityParams(portletConfiguration);
		assertTrue(params.containsKey("isHudsonConfigured"));
		assertTrue((Boolean) params.get("isHudsonConfigured"));
		assertTrue(params.containsKey("dateTimeUtils"));
		assertTrue(params.containsKey("buildUtils"));
		assertTrue(params.containsKey("jobUtils"));
		assertTrue(params.containsKey("sorter"));
		assertTrue(params.containsKey("buildTriggerParser"));
		assertTrue(params.containsKey("result"));
		final HudsonProjectPortletResult result = (HudsonProjectPortletResult) params.get("result");
		assertTrue(result.hasError());
		assertFalse(result.hasJob());
		assertEquals("hudson.error.portlet.no.job.found", result.getError());
		assertEquals(project, result.getProject());
		assertEquals(result.getServerLargeImageUrl(), result.getServerImageUrl());
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
	public void testGetVelocityParamsWithHudsonConfiguredThrowingHudsonServerAccessorException() throws Exception {
		when(serverManager.isHudsonConfigured()).thenReturn(true);
		when(serverAccessor.getProject(eq(server), eq(project))).thenThrow(
			new HudsonServerAccessorException("Failure"));
		when(portletConfiguration.getLongProperty("projectId")).thenReturn(1000L);
		when(projectManager.getProjectObj(1000L)).thenReturn(project);
		final Map<String, Object> params = portlet.getVelocityParams(portletConfiguration);
		assertTrue(params.containsKey("isHudsonConfigured"));
		assertTrue((Boolean) params.get("isHudsonConfigured"));
		assertTrue(params.containsKey("dateTimeUtils"));
		assertTrue(params.containsKey("buildUtils"));
		assertTrue(params.containsKey("jobUtils"));
		assertTrue(params.containsKey("sorter"));
		assertTrue(params.containsKey("buildTriggerParser"));
		assertTrue(params.containsKey("result"));
		final HudsonProjectPortletResult result = (HudsonProjectPortletResult) params.get("result");
		assertTrue(result.hasError());
		assertEquals("hudson.error.cannot.connect", result.getError());
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
	public void testGetVelocityParamsWithHudsonConfiguredThrowingHudsonServerAccessDeniedException() throws Exception {
		when(serverManager.isHudsonConfigured()).thenReturn(true);
		when(serverAccessor.getProject(eq(server), eq(project))).thenThrow(
			new HudsonServerAccessDeniedException("Failure"));
		when(portletConfiguration.getLongProperty("projectId")).thenReturn(1000L);
		when(projectManager.getProjectObj(1000L)).thenReturn(project);
		final Map<String, Object> params = portlet.getVelocityParams(portletConfiguration);
		assertTrue(params.containsKey("isHudsonConfigured"));
		assertTrue((Boolean) params.get("isHudsonConfigured"));
		assertTrue(params.containsKey("dateTimeUtils"));
		assertTrue(params.containsKey("buildUtils"));
		assertTrue(params.containsKey("jobUtils"));
		assertTrue(params.containsKey("sorter"));
		assertTrue(params.containsKey("buildTriggerParser"));
		assertTrue(params.containsKey("result"));
		final HudsonProjectPortletResult result = (HudsonProjectPortletResult) params.get("result");
		assertTrue(result.hasError());
		assertEquals("hudson.error.access.denied", result.getError());
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
	public void testGetVelocityParamsWithHudsonConfiguredThrowingObjectConfigurationException() throws Exception {
		when(serverManager.isHudsonConfigured()).thenReturn(true);
		when(portletConfiguration.getLongProperty("projectId")).thenThrow(new ObjectConfigurationException("Failure"));
		final Map<String, Object> params = portlet.getVelocityParams(portletConfiguration);
		assertTrue(params.containsKey("isHudsonConfigured"));
		assertTrue((Boolean) params.get("isHudsonConfigured"));
		assertTrue(params.containsKey("dateTimeUtils"));
		assertTrue(params.containsKey("buildUtils"));
		assertTrue(params.containsKey("jobUtils"));
		assertTrue(params.containsKey("sorter"));
		assertTrue(params.containsKey("buildTriggerParser"));
		assertTrue(params.containsKey("result"));
		final HudsonProjectPortletResult result = (HudsonProjectPortletResult) params.get("result");
		assertTrue(result.hasError());
		assertEquals("hudson.error.portlet.invalid.configuration", result.getError());
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
		final Map<String, Object> params = portlet.getVelocityParams(portletConfiguration);
		assertTrue(params.containsKey("isHudsonConfigured"));
		assertFalse((Boolean) params.get("isHudsonConfigured"));
		assertTrue(params.containsKey("errorMessage"));
		assertFalse(params.containsKey("dateTimeUtils"));
		assertFalse(params.containsKey("buildUtils"));
		assertFalse(params.containsKey("jobUtils"));
		assertFalse(params.containsKey("sorter"));
		assertFalse(params.containsKey("buildTriggerParser"));
		assertFalse(params.containsKey("result"));
		verify(serverManager, VerificationModeFactory.times(2)).isHudsonConfigured();
		verify(webResourceManager, VerificationModeFactory.times(1)).requireResource(
			HudsonBuildsTabPanelHelper.HUDSON_BUILD_PLUGIN + ":portlet-css");
	}

}
