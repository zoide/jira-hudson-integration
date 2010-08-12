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

import com.atlassian.jira.plugin.projectpanel.ProjectTabPanelModuleDescriptor;
import com.atlassian.jira.project.Project;
import com.atlassian.jira.project.ProjectManager;
import com.atlassian.jira.project.browse.BrowseContext;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.security.PermissionManager;
import com.atlassian.jira.security.Permissions;
import com.atlassian.plugin.webresource.WebResourceManager;
import com.marvelution.jira.plugins.hudson.DefaultHudsonServerImpl;
import com.marvelution.jira.plugins.hudson.service.HudsonConfigurationManager;
import com.marvelution.jira.plugins.hudson.service.HudsonServer;
import com.marvelution.jira.plugins.hudson.service.HudsonServerManager;
import com.opensymphony.user.User;

/**
 * TestCase for {@link HudsonBuildsForProjectTabPanel}
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class HudsonBuildsForProjectTabPanelTest {

	private HudsonBuildsForProjectTabPanel panel;

	private HudsonBuildsTabPanelHelper tabPanelHelper;

	private HudsonServer server;

	@Mock
	private JiraAuthenticationContext authenticationContext;

	@Mock
	private PermissionManager permissionManager;

	@Mock
	private HudsonServerManager serverManager;

	@Mock
	private ProjectManager projectManager;

	@Mock
	private WebResourceManager webResourceManager;

	@Mock
	private BrowseContext browseContext;

	@Mock
	private Project project;

	private Map<String, Object> velocityParams;

	@Mock
	private HudsonConfigurationManager configurationManager;

	/**
	 * Setup test variables
	 * 
	 * @throws Exception in case of failures
	 */
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		tabPanelHelper = new HudsonBuildsTabPanelHelper(projectManager, serverManager, webResourceManager);
		panel = new HudsonBuildsForProjectTabPanel(authenticationContext, permissionManager, serverManager,
				tabPanelHelper, configurationManager);
		panel.init(new ProjectTabPanelModuleDescriptor(authenticationContext) {

			@SuppressWarnings({ "unchecked", "rawtypes" })
			@Override
			public String getHtml(String resourceName, Map startingParams) {
				velocityParams = startingParams;
				return "fake html for " + resourceName;
			}

		});
		when(serverManager.isHudsonConfigured()).thenReturn(true);
		server = new DefaultHudsonServerImpl();
		server.setName("Hudson CI");
		server.setHost("http://hudson.marvelution.com");
		when(serverManager.getServerByJiraProject(eq(project))).thenReturn(server);
		when(browseContext.getProject()).thenReturn(project);
		when(project.getKey()).thenReturn("MARVADMIN");
	}

	/**
	 * Test getHtml
	 */
	@Test
	public void testGetHtml() {
		assertEquals("fake html for view", panel.getHtml(browseContext));
		assertTrue(velocityParams.containsKey("projectKey"));
		assertEquals("MARVADMIN", velocityParams.get("projectKey"));
		assertTrue(velocityParams.containsKey("hudsonServer"));
		assertEquals(server, velocityParams.get("hudsonServer"));
		assertTrue(velocityParams.containsKey("querySection"));
		assertEquals("projectKey=MARVADMIN", velocityParams.get("querySection"));
		assertTrue(velocityParams.containsKey("moduleKey"));
		assertEquals("project", velocityParams.get("moduleKey"));
		assertTrue(velocityParams.containsKey("baseResourceUrl"));
		assertEquals("/download/resources/" + HudsonBuildsTabPanelHelper.HUDSON_BUILD_PLUGIN
			+ ":hudson-project-tabpanel", velocityParams.get("baseResourceUrl"));
		verify(webResourceManager, VerificationModeFactory.times(1)).requireResource(
			HudsonBuildsTabPanelHelper.HUDSON_BUILD_PLUGIN + ":panel-css");
	}

	/**
	 * Test show panel with permission
	 * 
	 * @throws Exception in case of failures
	 */
	@Test
	public void testShowPanelWithPermission() throws Exception {
		when(permissionManager.hasPermission(eq(Permissions.VIEW_VERSION_CONTROL), eq(project), (User) eq(null)))
			.thenReturn(true);
		assertTrue(panel.showPanel(browseContext));
	}

	/**
	 * Test show panel with out permission
	 * 
	 * @throws Exception in case of failures
	 */
	@Test
	public void testShowPanelWithoutPermission() throws Exception {
		when(permissionManager.hasPermission(eq(Permissions.VIEW_VERSION_CONTROL), eq(project), (User) eq(null)))
			.thenReturn(false);
		assertFalse(panel.showPanel(browseContext));
	}

	/**
	 * Test show panel throwing {@link PermissionException}
	 * 
	 * @throws Exception in case of failures
	 */
	@Test
	public void testShowPanelThrowingPermissionException() throws Exception {
		when(permissionManager.hasPermission(eq(Permissions.VIEW_VERSION_CONTROL), eq(project), (User) eq(null)))
			.thenReturn(false);
		assertFalse(panel.showPanel(browseContext));
	}

}
