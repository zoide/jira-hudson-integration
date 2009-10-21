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

package com.marvelution.jira.plugins.hudson.web.action;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.atlassian.core.util.DateUtils;
import com.atlassian.jira.bc.project.component.ProjectComponent;
import com.atlassian.jira.bc.project.component.ProjectComponentManager;
import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.issue.search.SearchProvider;
import com.atlassian.jira.project.Project;
import com.atlassian.jira.project.ProjectManager;
import com.atlassian.jira.project.version.Version;
import com.atlassian.jira.project.version.VersionManager;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.security.PermissionManager;
import com.atlassian.jira.security.Permissions;
import com.atlassian.jira.user.util.UserUtil;
import com.atlassian.jira.util.I18nHelper;
import com.marvelution.jira.plugins.hudson.api.model.Build;
import com.marvelution.jira.plugins.hudson.service.HudsonServer;
import com.marvelution.jira.plugins.hudson.service.HudsonServerAccessor;
import com.marvelution.jira.plugins.hudson.service.HudsonServerAccessorException;
import com.marvelution.jira.plugins.hudson.service.HudsonServerManager;
import com.opensymphony.user.User;

import webwork.action.ActionContext;
import webwork.action.ActionSupport;


/**
 * TestCase for
 *  
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class ViewHudsonServerPanelContentTest {

	private ViewHudsonServerPanelContent panelContent;

	@Mock
	protected HudsonServer server;

	@Mock
	protected HudsonServerManager serverManager;

	@Mock
	protected HudsonServerAccessor serverAccessor;

	@Mock
	protected PermissionManager permissionManager;

	@Mock
	protected HttpServletRequest httpRequest;

	@Mock
	protected HttpServletResponse httpResponse;

	protected ActionSupport webworkAction;

	@Mock
	private JiraAuthenticationContext authenticationContext;

	@Mock
	private I18nHelper i18n;

	@Mock
	private ProjectManager projectManager;

	@Mock
	private ProjectComponentManager componentManager;

	@Mock
	private IssueManager issueManager;

	@Mock
	private VersionManager versionManager;

	@Mock
	private UserUtil userUtil;

	@Mock
	private SearchProvider searchProvider;

	@Mock
	private Project project;

	@Mock
	private Version version;

	@Mock
	private MutableIssue issue;

	@Mock
	private ProjectComponent component;

	/**
	 * Setup the tests
	 * 
	 * @throws Exception in case of preparation exceptions
	 */
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		webworkAction = createWebworkAction();
		ActionContext.setRequest(httpRequest);
		ActionContext.setResponse(httpResponse);
	}

	/**
	 * Cleanup after tests
	 * 
	 * @throws Exception in case of cleanup exceptions
	 */
	@After
	public void tearDown() throws Exception {
		ActionContext.setRequest(null);
		ActionContext.setResponse(null);
	}

	/**
	 * Setup the {@link ActionSupport}
	 * 
	 * @return {@link ActionSupport}
	 */
	protected ActionSupport createWebworkAction() {
		final Answer<String> answer = new Answer<String>() {

			public String answer(InvocationOnMock invocation) throws Throwable {
				return invocation.getArguments()[0].toString();
			}

		};
		when(i18n.getText(anyString())).thenAnswer(answer);
		when(i18n.getText(anyString(), anyObject())).thenAnswer(answer);
		when(i18n.getText(anyString(), anyString())).thenAnswer(answer);
		when(i18n.getLocale()).thenReturn(Locale.ENGLISH);
		when(authenticationContext.getI18nHelper()).thenReturn(i18n);
		panelContent =
			new ViewHudsonServerPanelContent(authenticationContext, permissionManager, projectManager,
				componentManager, issueManager, versionManager, userUtil, serverAccessor, serverManager,
				searchProvider);
		return panelContent;
	}

	/**
	 * Test doExecute execution with no servers configured
	 * 
	 * @throws Exception in case of test failures
	 */
	@Test
	public void testGetPanelContentNoServers() throws Exception {
		when(serverManager.hasServers()).thenReturn(false);
		assertEquals("error", panelContent.doExecute());
		assertTrue(panelContent.getErrorMessages().contains("hudson.panel.error.not.configured"));
		verify(serverManager, VerificationModeFactory.times(1)).hasServers();
	}

	/**
	 * Test doExecute execution with {@link #serverAccessor} throwing a {@link HudsonServerAccessorException}
	 * 
	 * @throws Exception in case of test failures
	 */
	@Test
	public void testGetPanelContentHudsonAccessorException() throws Exception {
		when(serverManager.hasServers()).thenReturn(true);
		when(serverManager.getDefaultServer()).thenReturn(server);
		when(projectManager.getProjectObjByKey(eq("MARVADMIN"))).thenReturn(project);
		when(permissionManager.hasPermission(eq(Permissions.VIEW_VERSION_CONTROL), eq(project), (User) eq(null)))
			.thenReturn(true);
		when(serverManager.getServerByJiraProject(eq(project))).thenReturn(server);
		when(serverAccessor.getBuilds(eq(server), eq(project))).thenThrow(
			new HudsonServerAccessorException("Failure", new Exception()));
		panelContent.setProjectKey("MARVADMIN");
		assertEquals("error", panelContent.doExecute());
		assertTrue(panelContent.getErrorMessages().contains("hudson.panel.error.cannot.connect"));
		verify(serverManager, VerificationModeFactory.times(1)).hasServers();
		verify(serverManager, VerificationModeFactory.times(1)).getDefaultServer();
		verify(serverManager, VerificationModeFactory.times(1)).getServerByJiraProject(project);
		verify(serverAccessor, VerificationModeFactory.times(1)).getBuilds(server, project);
		verify(projectManager, VerificationModeFactory.times(1)).getProjectObjByKey("MARVADMIN");
		verify(permissionManager, VerificationModeFactory.times(1)).hasPermission(Permissions.VIEW_VERSION_CONTROL,
			project, null);
	}

	/**
	 * Test get panel content for a project with permission
	 * 
	 * @throws Exception in case of test failures
	 */
	@Test
	public void testGetPanelContentForProjectWithPermission() throws Exception {
		when(serverManager.hasServers()).thenReturn(true);
		when(serverManager.getDefaultServer()).thenReturn(server);
		when(serverManager.getServerByJiraProject(eq(project))).thenReturn(server);
		when(projectManager.getProjectObjByKey(eq("MARVADMIN"))).thenReturn(project);
		when(permissionManager.hasPermission(eq(Permissions.VIEW_VERSION_CONTROL), eq(project), (User) eq(null)))
			.thenReturn(true);
		setupServerAccessorAndBuild();
		panelContent.setProjectKey("MARVADMIN");
		assertEquals("success", panelContent.doExecute());
		validateServerAccessorResults();
		verify(serverManager, VerificationModeFactory.times(1)).hasServers();
		verify(serverManager, VerificationModeFactory.times(1)).getDefaultServer();
		verify(serverManager, VerificationModeFactory.times(1)).getServerByJiraProject(project);
		verify(permissionManager, VerificationModeFactory.times(1)).hasPermission(Permissions.VIEW_VERSION_CONTROL,
			project, null);
	}

	/**
	 * Test get panel content for a project with out permission
	 * 
	 * @throws Exception in case of test failures
	 */
	@Test
	public void testGetPanelContentForProjectWithoutPermission() throws Exception {
		when(serverManager.hasServers()).thenReturn(true);
		when(serverManager.getDefaultServer()).thenReturn(server);
		when(projectManager.getProjectObjByKey(eq("MARVADMIN"))).thenReturn(project);
		when(permissionManager.hasPermission(eq(Permissions.VIEW_VERSION_CONTROL), eq(project), (User) eq(null)))
			.thenReturn(false);
		panelContent.setProjectKey("MARVADMIN");
		assertEquals("error", panelContent.doExecute());
		assertTrue(panelContent.getErrorMessages().contains("hudson.panel.error.no.permission"));
		verify(serverManager, VerificationModeFactory.times(1)).hasServers();
		verify(serverManager, VerificationModeFactory.times(1)).getDefaultServer();
		verify(serverManager, VerificationModeFactory.times(0)).getServerByJiraProject(project);
		verify(permissionManager, VerificationModeFactory.times(1)).hasPermission(Permissions.VIEW_VERSION_CONTROL,
			project, null);
	}

	/**
	 * Test get panel content for a project version with permission
	 * 
	 * @throws Exception in case of test failures
	 */
	@Test
	public void testGetPanelContentForVersionWithPermission() throws Exception {
		when(serverManager.hasServers()).thenReturn(true);
		when(serverManager.getDefaultServer()).thenReturn(server);
		when(serverManager.getServerByJiraProject(eq(project))).thenReturn(server);
		when(versionManager.getVersion(eq(1000L))).thenReturn(version);
		when(version.getProjectObject()).thenReturn(project);
		when(permissionManager.hasPermission(eq(Permissions.VIEW_VERSION_CONTROL), eq(project), (User) eq(null)))
			.thenReturn(true);
		setupServerAccessorAndBuild();
		panelContent.setVersionId(1000L);
		assertEquals("success", panelContent.doExecute());
		validateServerAccessorResults();
		verify(serverManager, VerificationModeFactory.times(1)).hasServers();
		verify(serverManager, VerificationModeFactory.times(1)).getDefaultServer();
		verify(serverManager, VerificationModeFactory.times(1)).getServerByJiraProject(project);
		verify(permissionManager, VerificationModeFactory.times(1)).hasPermission(Permissions.VIEW_VERSION_CONTROL,
			project, null);
	}

	/**
	 * Test get panel content for a project version with out permission
	 * 
	 * @throws Exception in case of test failures
	 */
	@Test
	public void testGetPanelContentForVersionWithoutPermission() throws Exception {
		when(serverManager.hasServers()).thenReturn(true);
		when(serverManager.getDefaultServer()).thenReturn(server);
		when(versionManager.getVersion(eq(1000L))).thenReturn(version);
		when(version.getProjectObject()).thenReturn(project);
		when(permissionManager.hasPermission(eq(Permissions.VIEW_VERSION_CONTROL), eq(project), (User) eq(null)))
			.thenReturn(false);
		panelContent.setVersionId(1000L);
		assertEquals("error", panelContent.doExecute());
		assertTrue(panelContent.getErrorMessages().contains("hudson.panel.error.no.permission"));
		verify(serverManager, VerificationModeFactory.times(1)).hasServers();
		verify(serverManager, VerificationModeFactory.times(1)).getDefaultServer();
		verify(serverManager, VerificationModeFactory.times(0)).getServerByJiraProject(project);
		verify(permissionManager, VerificationModeFactory.times(1)).hasPermission(Permissions.VIEW_VERSION_CONTROL,
			project, null);
	}

	/**
	 * Test get panel content for a project component with out permission
	 * 
	 * @throws Exception in case of test failures
	 */
	@Test
	public void testGetPanelContentForComponentWithoutPermission() throws Exception {
		when(serverManager.hasServers()).thenReturn(true);
		when(serverManager.getDefaultServer()).thenReturn(server);
		when(componentManager.find(eq(1000L))).thenReturn(component);
		when(component.getProjectId()).thenReturn(1024L);
		when(projectManager.getProjectObj(eq(1024L))).thenReturn(project);
		when(permissionManager.hasPermission(eq(Permissions.VIEW_VERSION_CONTROL), eq(project), (User) eq(null)))
			.thenReturn(false);
		panelContent.setComponentId(1000L);
		assertEquals("error", panelContent.doExecute());
		assertTrue(panelContent.getErrorMessages().contains("hudson.panel.error.no.permission"));
		verify(serverManager, VerificationModeFactory.times(1)).hasServers();
		verify(serverManager, VerificationModeFactory.times(1)).getDefaultServer();
		verify(serverManager, VerificationModeFactory.times(0)).getServerByJiraProject(project);
		verify(permissionManager, VerificationModeFactory.times(1)).hasPermission(Permissions.VIEW_VERSION_CONTROL,
			project, null);
	}

	/**
	 * Test get panel content for a issue with permission
	 * 
	 * @throws Exception in case of test failures
	 */
	@Test
	public void testGetPanelContentForIssueWithPermission() throws Exception {
		when(serverManager.hasServers()).thenReturn(true);
		when(serverManager.getDefaultServer()).thenReturn(server);
		when(serverManager.getServerByJiraProject(eq(project))).thenReturn(server);
		when(issueManager.getIssueObject(eq("MARVADMIN-1"))).thenReturn(issue);
		when(issue.getProjectObject()).thenReturn(project);
		when(permissionManager.hasPermission(eq(Permissions.VIEW_VERSION_CONTROL), eq(issue), (User) eq(null)))
			.thenReturn(true);
		setupServerAccessorAndBuild();
		panelContent.setIssueKey("MARVADMIN-1");
		assertEquals("success", panelContent.doExecute());
		validateServerAccessorResults();
		verify(serverManager, VerificationModeFactory.times(1)).hasServers();
		verify(serverManager, VerificationModeFactory.times(1)).getDefaultServer();
		verify(serverManager, VerificationModeFactory.times(1)).getServerByJiraProject(project);
		verify(permissionManager, VerificationModeFactory.times(1)).hasPermission(Permissions.VIEW_VERSION_CONTROL,
			issue, null);
	}

	/**
	 * Test get panel content for a issue with out permission
	 * 
	 * @throws Exception in case of test failures
	 */
	@Test
	public void testGetPanelContentForIssueWithoutPermission() throws Exception {
		when(serverManager.hasServers()).thenReturn(true);
		when(serverManager.getDefaultServer()).thenReturn(server);
		when(issueManager.getIssueObject(eq("MARVADMIN-1"))).thenReturn(issue);
		when(issue.getProjectObject()).thenReturn(project);
		when(permissionManager.hasPermission(eq(Permissions.VIEW_VERSION_CONTROL), eq(issue), (User) eq(null)))
			.thenReturn(false);
		panelContent.setIssueKey("MARVADMIN-1");
		assertEquals("error", panelContent.doExecute());
		assertTrue(panelContent.getErrorMessages().contains("hudson.panel.error.no.permission"));
		verify(serverManager, VerificationModeFactory.times(1)).hasServers();
		verify(serverManager, VerificationModeFactory.times(1)).getDefaultServer();
		verify(serverManager, VerificationModeFactory.times(0)).getServerByJiraProject(project);
		verify(permissionManager, VerificationModeFactory.times(1)).hasPermission(Permissions.VIEW_VERSION_CONTROL,
			issue, null);
	}

	/**
	 * Test invalid command execution
	 * 
	 * @throws Exception in case of test failures
	 */
	@Test
	public void testInvalidCommand() throws Exception {
		when(serverManager.hasServers()).thenReturn(true);
		assertEquals("error", panelContent.doExecute());
		assertTrue(panelContent.getErrorMessages().contains("hudson.panel.error.invalid.request"));
		verify(serverManager, VerificationModeFactory.times(1)).hasServers();
	}

	/**
	 * Setup the HudsonServerAccessor
	 * 
	 * @throws Exception in case of test failures
	 */
	@SuppressWarnings("unchecked")
	private void setupServerAccessorAndBuild() throws Exception {
		final List<Build> builds = new ArrayList<Build>();
		builds.add(new Build(1, "Marvelution"));
		builds.add(new Build(2, "Marvelution"));
		builds.get(0).setTimestamp(Calendar.getInstance().getTimeInMillis() - DateUtils.DAY_MILLIS);
		builds.get(0).getRelatedIssueKeys().add("MARVADMIN-1");
		builds.get(1).setTimestamp(Calendar.getInstance().getTimeInMillis() - DateUtils.HOUR_MILLIS);
		builds.get(1).getRelatedIssueKeys().add("MARVADMIN-1");
		builds.get(1).getRelatedIssueKeys().add("MARVADMIN-2");
		when(serverAccessor.getBuilds(eq(server), eq(project))).thenReturn(builds);
		when(serverAccessor.getBuilds(eq(server), eq(version))).thenReturn(builds);
		when(serverAccessor.getBuilds(eq(server), any(List.class))).thenReturn(builds);
		when(issueManager.getIssueObject(eq("MARVADMIN-1"))).thenReturn(issue);
	}

	/**
	 * Validate the ServerAccessor Results
	 */
	public void validateServerAccessorResults() {
		assertEquals(server, panelContent.getResults().getServer());
		assertTrue(panelContent.getResults().hasBuilds());
		assertEquals(2, panelContent.getResults().getBuilds().size());
		final List<Build> builds = panelContent.getResults().getBuilds();
		assertEquals(2, builds.get(0).getNumber());
		assertEquals(1, builds.get(1).getNumber());
		assertTrue(builds.get(1).getRelatedIssueKeys().contains("MARVADMIN-1"));
		assertTrue(builds.get(1).getRelatedIssueKeys().contains("MARVADMIN-1"));
		assertFalse(builds.get(0).getRelatedIssueKeys().contains("MARVADMIN-2"));
		assertEquals(panelContent.getResults().getServerLargeImageUrl(), panelContent.getResults()
			.getServerImageUrl());
	}

}
