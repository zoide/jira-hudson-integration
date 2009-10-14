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
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.verification.VerificationModeFactory;

import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.plugin.issuetabpanel.IssueTabPanelModuleDescriptor;
import com.atlassian.jira.project.ProjectManager;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.plugin.webresource.WebResourceManager;
import com.marvelution.jira.plugins.hudson.service.HudsonServer;
import com.marvelution.jira.plugins.hudson.service.HudsonServerManager;

/**
 * TestCase for {@link HudsonBuildsForIssueTabPanelAction}
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class HudsonBuildsForIssueTabPanelActionTest {

	private HudsonBuildsForIssueTabPanelAction action;

	private HudsonBuildsTabPanelHelper tabPanelHelper;

	@Mock
	private ProjectManager projectManager;

	@Mock
	private JiraAuthenticationContext authenticationContext;

	@Mock
	private HudsonServerManager serverManager;

	private HudsonServer server;

	@Mock
	private Issue issue;

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
		action =
			new HudsonBuildsForIssueTabPanelAction(issue, new IssueTabPanelModuleDescriptor(authenticationContext),
				tabPanelHelper);
		when(issue.getKey()).thenReturn("MARVADMIN-1");
	}

	/**
	 * Test that the isDisplayActionAllTab is false
	 */
	@Test
	public void testIsDisplayActionAllTab() {
		assertFalse(action.isDisplayActionAllTab());
	}

	/**
	 * Test that the getTimePerformed is unsupported
	 */
	@Test
	public void testGetTimePerformed() {
		try {
			action.getTimePerformed();
			fail("This test should fail");
		} catch (UnsupportedOperationException e) {
			assertNotNull(e);
		}
	}

	/**
	 * Test get the velocity parameters
	 */
	@Test
	public void testPopulateVelocityParamsMap() {
		final Map<String, Object> params = new HashMap<String, Object>();
		action.populateVelocityParams(params);
		assertTrue(params.containsKey("issueKey"));
		assertEquals("MARVADMIN-1", params.get("issueKey"));
		assertTrue(params.containsKey("hudsonServer"));
		assertEquals(server, params.get("hudsonServer"));
		assertTrue(params.containsKey("querySection"));
		assertEquals("issueKey=MARVADMIN-1", params.get("querySection"));
		assertTrue(params.containsKey("moduleKey"));
		assertEquals("issue", params.get("moduleKey"));
		assertTrue(params.containsKey("baseResourceUrl"));
		assertEquals("/download/resources/" + HudsonBuildsTabPanelHelper.HUDSON_BUILD_PLUGIN
			+ ":hudson-issue-tabpanel", params.get("baseResourceUrl"));
		verify(webResourceManager, VerificationModeFactory.times(1)).requireResource(
			HudsonBuildsTabPanelHelper.HUDSON_BUILD_PLUGIN + ":panel-css");
	}

}
