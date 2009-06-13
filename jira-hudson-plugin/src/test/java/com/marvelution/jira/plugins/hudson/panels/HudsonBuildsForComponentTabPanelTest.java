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

package com.marvelution.jira.plugins.hudson.panels;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.verification.VerificationModeFactory;

import com.atlassian.jira.bc.project.component.ProjectComponent;
import com.atlassian.jira.plugin.componentpanel.ComponentContext;
import com.atlassian.jira.project.Project;
import com.atlassian.jira.project.ProjectManager;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.security.PermissionManager;
import com.atlassian.jira.security.Permissions;
import com.atlassian.plugin.webresource.WebResourceManager;
import com.marvelution.jira.plugins.hudson.DefaultHudsonServerImpl;
import com.marvelution.jira.plugins.hudson.service.HudsonServer;
import com.marvelution.jira.plugins.hudson.service.HudsonServerManager;
import com.opensymphony.user.User;

/**
 * TestCase for {@link HudsonBuildsForComponentTabPanel}
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class HudsonBuildsForComponentTabPanelTest {

	private HudsonBuildsForComponentTabPanel panel;

	@Mock
	private ProjectManager projectManager;

	@Mock
	private JiraAuthenticationContext authenticationContext;

	@Mock
	private PermissionManager permissionManager;

	@Mock
	private HudsonServerManager serverManager;

	private HudsonServer server;

	private HudsonBuildsTabPanelHelper tabPanelHelper;

	@Mock
	private ComponentContext componentContext;

	@Mock
	private ProjectComponent component;

	@Mock
	private Project project;

	@Mock
	private WebResourceManager webResourceManager;

	/**
	 * Setup the test variables
	 * 
	 * @throws Exception in case of failures
	 */
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		tabPanelHelper = new HudsonBuildsTabPanelHelper(projectManager, serverManager, webResourceManager);
		panel =
			new HudsonBuildsForComponentTabPanel(projectManager, authenticationContext, permissionManager,
				serverManager, tabPanelHelper);
		when(componentContext.getComponent()).thenReturn(component);
		when(component.getId()).thenReturn(1024L);
		when(component.getProjectId()).thenReturn(1000L);
		when(projectManager.getProjectObj(eq(1000L))).thenReturn(project);
		when(serverManager.isHudsonConfigured()).thenReturn(true);
		server = new DefaultHudsonServerImpl();
		server.setName("Hudson CI");
		server.setHost("http://hudson.marvelution.com");
		when(serverManager.getServerByJiraProject(eq(project))).thenReturn(server);
	}

	/**
	 * Test creating the velocity parameters
	 */
	@Test
	public void testCreateVelocityParams() {
		final Map<?, ?> params = panel.createVelocityParams(componentContext);
		assertTrue(params.containsKey("project"));
		assertTrue(params.containsKey("componentContext"));
		assertTrue(params.containsKey("componentId"));
		assertEquals(1024L, params.get("componentId"));
		assertTrue(params.containsKey("hudsonServer"));
		assertEquals(server, params.get("hudsonServer"));
		assertTrue(params.containsKey("querySection"));
		assertEquals("componentId=1024", params.get("querySection"));
		assertTrue(params.containsKey("moduleKey"));
		assertEquals("component", params.get("moduleKey"));
		assertTrue(params.containsKey("baseResourceUrl"));
		assertEquals("/download/resources/" + HudsonBuildsTabPanelHelper.HUDSON_BUILD_PLUGIN
			+ ":hudson-component-tabpanel", params.get("baseResourceUrl"));
		verify(webResourceManager, VerificationModeFactory.times(1)).requireResource(
			HudsonBuildsTabPanelHelper.HUDSON_BUILD_PLUGIN + ":tabpanel-css");
		verify(webResourceManager, VerificationModeFactory.times(1)).requireResource("jira.webresources:prototype");
	}

	/**
	 * Test showPanel with permission
	 */
	@Test
	public void testShowPanelWithPermission() {
		when(permissionManager.hasPermission(eq(Permissions.VIEW_VERSION_CONTROL), eq(project), (User) eq(null)))
			.thenReturn(true);
		assertTrue(panel.showPanel(componentContext));
	}

	/**
	 * Test showPanel with out permission
	 */
	@Test
	public void testShowPanelWithoutPermission() {
		when(permissionManager.hasPermission(eq(Permissions.VIEW_VERSION_CONTROL), eq(project), (User) eq(null)))
			.thenReturn(false);
		assertFalse(panel.showPanel(componentContext));
	}

}
