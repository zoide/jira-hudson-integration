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

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.security.PermissionManager;
import com.atlassian.jira.security.Permissions;
import com.marvelution.jira.plugins.hudson.service.HudsonConfigurationManager;
import com.marvelution.jira.plugins.hudson.service.HudsonServerManager;
import com.opensymphony.user.User;

/**
 * TestCase for {@link HudsonBuildsForIssueTabPanel}
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class HudsonBuildsForIssueTabPanelTest {

	private HudsonBuildsForIssueTabPanel panel;

	@Mock
	private PermissionManager permissionManager;

	@Mock
	private HudsonServerManager serverManager;

	private HudsonBuildsTabPanelHelper tabPanelHelper;

	@Mock
	private Issue issue;

	@Mock
	private HudsonConfigurationManager configurationManager;

	/**
	 * Setup the test variables
	 * 
	 * @throws Exception in case of failures
	 */
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		panel =
			new HudsonBuildsForIssueTabPanel(permissionManager, serverManager, tabPanelHelper, configurationManager);
		when(serverManager.isHudsonConfigured()).thenReturn(true);
	}

	/**
	 * Test getActions
	 */
	@Test
	public void testGetActions() {
		final List<?> actions = panel.getActions(issue, null);
		assertFalse(actions.isEmpty());
		assertEquals(1, actions.size());
		assertTrue(actions.get(0) instanceof HudsonBuildsForIssueTabPanelAction);
	}

	/**
	 * Test show panel with permission
	 */
	@Test
	public void testShowPanelWithPermission() {
		when(permissionManager.hasPermission(eq(Permissions.VIEW_VERSION_CONTROL), eq(issue), (User) eq(null)))
			.thenReturn(true);
		assertTrue(panel.showPanel(issue, null));
	}

	/**
	 * Test show panel with out permission
	 */
	@Test
	public void testShowPanelWithoutPermission() {
		when(permissionManager.hasPermission(eq(Permissions.VIEW_VERSION_CONTROL), eq(issue), (User) eq(null)))
			.thenReturn(false);
		assertFalse(panel.showPanel(issue, null));
	}

}
