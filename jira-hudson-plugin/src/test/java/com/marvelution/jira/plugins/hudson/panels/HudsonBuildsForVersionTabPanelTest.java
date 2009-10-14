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

import com.atlassian.jira.issue.search.SearchProvider;
import com.atlassian.jira.plugin.versionpanel.BrowseVersionContext;
import com.atlassian.jira.project.Project;
import com.atlassian.jira.project.ProjectManager;
import com.atlassian.jira.project.version.Version;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.security.PermissionManager;
import com.atlassian.jira.security.Permissions;
import com.atlassian.plugin.webresource.WebResourceManager;
import com.marvelution.jira.plugins.hudson.DefaultHudsonServerImpl;
import com.marvelution.jira.plugins.hudson.service.HudsonServer;
import com.marvelution.jira.plugins.hudson.service.HudsonServerManager;
import com.opensymphony.user.User;

/**
 * TestCase for {@link HudsonBuildsForVersionTabPanel}
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class HudsonBuildsForVersionTabPanelTest {

	private HudsonBuildsForVersionTabPanel panel;

	@Mock
	private ProjectManager projectManager;

	@Mock
	private JiraAuthenticationContext authenticationContext;

	@Mock
	private SearchProvider searchProvider;

	@Mock
	private PermissionManager permissionManager;

	@Mock
	private HudsonServerManager serverManager;

	private HudsonServer server;

	private HudsonBuildsTabPanelHelper tabPanelHelper;

	@Mock
	private BrowseVersionContext versionContext;

	@Mock
	private Version version;

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
			new HudsonBuildsForVersionTabPanel(authenticationContext, searchProvider, permissionManager,
				serverManager, tabPanelHelper);
		when(versionContext.getVersion()).thenReturn(version);
		when(versionContext.getProject()).thenReturn(project);
		when(version.getProjectObject()).thenReturn(project);
		when(version.getId()).thenReturn(1024L);
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
		final Map<?, ?> params = panel.createVelocityParams(versionContext);
		assertTrue(params.containsKey("project"));
		assertTrue(params.containsKey("versionContext"));
		assertTrue(params.containsKey("versionId"));
		assertTrue(params.containsKey("hudsonServer"));
		assertEquals(server, params.get("hudsonServer"));
		assertTrue(params.containsKey("querySection"));
		assertEquals("versionId=1024", params.get("querySection"));
		assertTrue(params.containsKey("moduleKey"));
		assertEquals("version", params.get("moduleKey"));
		assertTrue(params.containsKey("baseResourceUrl"));
		assertEquals("/download/resources/" + HudsonBuildsTabPanelHelper.HUDSON_BUILD_PLUGIN
			+ ":hudson-version-tabpanel", params.get("baseResourceUrl"));
		verify(webResourceManager, VerificationModeFactory.times(1)).requireResource(
			HudsonBuildsTabPanelHelper.HUDSON_BUILD_PLUGIN + ":panel-css");
	}

	/**
	 * Test creating the velocity parameters
	 */
	@Test
	public void testCreateVelocityParamsReleasedVersion() {
		when(version.isReleased()).thenReturn(true);
		final Map<?, ?> params = panel.createVelocityParams(versionContext);
		assertTrue(params.containsKey("extraDescriptionKey"));
		assertEquals("released.", params.get("extraDescriptionKey"));
		assertTrue(params.containsKey("project"));
		assertTrue(params.containsKey("versionContext"));
		assertTrue(params.containsKey("versionId"));
		assertTrue(params.containsKey("hudsonServer"));
		assertEquals(server, params.get("hudsonServer"));
		assertTrue(params.containsKey("querySection"));
		assertEquals("versionId=1024", params.get("querySection"));
		assertTrue(params.containsKey("moduleKey"));
		assertEquals("version", params.get("moduleKey"));
		assertTrue(params.containsKey("baseResourceUrl"));
		assertEquals("/download/resources/" + HudsonBuildsTabPanelHelper.HUDSON_BUILD_PLUGIN
			+ ":hudson-version-tabpanel", params.get("baseResourceUrl"));
		verify(webResourceManager, VerificationModeFactory.times(1)).requireResource(
			HudsonBuildsTabPanelHelper.HUDSON_BUILD_PLUGIN + ":panel-css");
	}

	/**
	 * Test showPanel with permission
	 */
	@Test
	public void testShowPanelWithPermission() {
		when(permissionManager.hasPermission(eq(Permissions.VIEW_VERSION_CONTROL), eq(project), (User) eq(null)))
			.thenReturn(true);
		assertTrue(panel.showPanel(versionContext));
	}

	/**
	 * Test showPanel with out permission
	 */
	@Test
	public void testShowPanelWithoutPermission() {
		when(permissionManager.hasPermission(eq(Permissions.VIEW_VERSION_CONTROL), eq(project), (User) eq(null)))
			.thenReturn(false);
		assertFalse(panel.showPanel(versionContext));
	}

}
