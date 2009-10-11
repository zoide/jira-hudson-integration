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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.internal.verification.VerificationModeFactory;

import com.atlassian.jira.project.Project;
import com.atlassian.jira.project.ProjectManager;

import webwork.action.ActionContext;
import webwork.action.ActionSupport;


/**
 * TestCase for
 *  
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class UpdateHudsonServerProjectAssociationsTest extends AbstractHudsonWebActionSupportTest {

	private UpdateHudsonServerProjectAssociations serverProjectAssociations;

	@Mock
	private ProjectManager projectManager;

	@Mock
	private Project project;

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected ActionSupport createWebworkAction() {
		when(serverManager.getServer(eq(1))).thenReturn(server);
		when(server.getName()).thenReturn("Hudson CI");
		serverProjectAssociations =
			new UpdateHudsonServerProjectAssociations(permissionManager, projectManager, serverManager);
		serverProjectAssociations.setHudsonServerId(1);
		return serverProjectAssociations;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void testDoDefaultWithoutPermission() throws Exception {
		super.testDoDefaultWithoutPermission();
		assertEquals("Hudson CI", serverProjectAssociations.getName());
		verify(serverManager, VerificationModeFactory.times(1)).getServer(1);
		verify(server, VerificationModeFactory.times(1)).getName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void testDoDefaultWithPermission() throws Exception {
		super.testDoDefaultWithPermission();
		assertEquals("Hudson CI", serverProjectAssociations.getName());
		verify(serverManager, VerificationModeFactory.times(1)).getServer(1);
		verify(server, VerificationModeFactory.times(1)).getName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Test
	public void testDoDefaultWithoutServerId() throws Exception {
		when(serverManager.getServer(eq(1))).thenReturn(null);
		assertEquals("none", serverProjectAssociations.doDefault());
		verify(serverManager, VerificationModeFactory.times(1)).getServer(1);
	}

	/**
	 * Test the execution of doExecute with any button being clicked
	 * 
	 * @throws Exception in case of test failures
	 */
	@Test
	public void testDoExecutionNoButtonClicked() throws Exception {
		assertEquals("input", serverProjectAssociations.doExecute());
		verify(serverManager, VerificationModeFactory.times(1)).getServer(1);
	}

	/**
	 * Test adding project associations
	 * 
	 * @throws Exception in case of test failures
	 */
	@Test
	public void testAddAvailableWithProjectsSelected() throws Exception {
		ActionContext.setParameters(Collections.singletonMap("addButton.x", "Clicked Add Button"));
		serverProjectAssociations.setAvailableProjectKeys(new String[] {"MARVUTIL", "MARVADMIN"});
		assertEquals("input", serverProjectAssociations.doExecute());
		verify(serverManager, VerificationModeFactory.times(1)).getServer(1);
		verify(serverManager, VerificationModeFactory.times(1)).put(server);
		verify(server, VerificationModeFactory.times(1)).addAssociatedProjectKey("MARVUTIL");
		verify(server, VerificationModeFactory.times(1)).addAssociatedProjectKey("MARVADMIN");
	}

	/**
	 * Test adding project associations
	 * 
	 * @throws Exception in case of test failures
	 */
	@Test
	public void testAddAvailableWithoutProjectsSelected() throws Exception {
		ActionContext.setParameters(Collections.singletonMap("addButton.x", "Clicked Add Button"));
		when(httpRequest.getParameter("addButton.x")).thenReturn("Clicked Add Button");
		assertEquals("input", serverProjectAssociations.doExecute());
		verify(serverManager, VerificationModeFactory.times(1)).getServer(1);
		verify(serverManager, VerificationModeFactory.times(0)).put(server);
	}

	/**
	 * Test adding all projects associations
	 * 
	 * @throws Exception in case of test failures
	 */
	@Test
	public void testAddAllAvailableProjects() throws Exception {
		when(projectManager.getProjectObjects()).thenReturn(Collections.singletonList(project));
		when(project.getKey()).thenReturn("MARVADMIN");
		ActionContext.setParameters(Collections.singletonMap("addAllButton.x", "Clicked All Add Button"));
		assertEquals("input", serverProjectAssociations.doExecute());
		verify(server, VerificationModeFactory.times(1)).addAssociatedProjectKey("MARVADMIN");
		verify(serverManager, VerificationModeFactory.times(1)).getServer(1);
		verify(projectManager, VerificationModeFactory.times(1)).getProjectObjects();
		verify(project, VerificationModeFactory.times(1)).getKey();
	}

	/**
	 * Test removing project associations
	 * 
	 * @throws Exception in case of test failures
	 */
	@Test
	public void testRemoveAssociationWithProjectsSelected() throws Exception {
		ActionContext.setParameters(Collections.singletonMap("removeButton.x", "Clicked Remove Button"));
		serverProjectAssociations.setAssociatedProjectKeys(new String[] {"MARVADMIN", "MARVUTIL"});
		assertEquals("input", serverProjectAssociations.doExecute());
		verify(serverManager, VerificationModeFactory.times(1)).getServer(1);
		verify(serverManager, VerificationModeFactory.times(1)).put(server);
		verify(server, VerificationModeFactory.times(1)).removeAssociatedProjectKey("MARVUTIL");
		verify(server, VerificationModeFactory.times(1)).removeAssociatedProjectKey("MARVADMIN");
	}

	/**
	 * Test removing project associations
	 * 
	 * @throws Exception in case of test failures
	 */
	@Test
	public void testRemoveAssociationWithoutProjectsSelected() throws Exception {
		ActionContext.setParameters(Collections.singletonMap("removeButton.x", "Clicked Remove Button"));
		serverProjectAssociations.setHudsonServerId(1);
		assertEquals("input", serverProjectAssociations.doExecute());
		verify(serverManager, VerificationModeFactory.times(1)).getServer(1);
		verify(serverManager, VerificationModeFactory.times(0)).put(server);
	}

	/**
	 * Test removing all projects associations
	 * 
	 * @throws Exception in case of test failures
	 */
	@Test
	public void testRemoveAllAssociationProjects() throws Exception {
		ActionContext.setParameters(Collections.singletonMap("removeAllButton.x", "Clicked All Remove Button"));
		serverProjectAssociations.setHudsonServerId(1);
		assertEquals("input", serverProjectAssociations.doExecute());
		verify(server, VerificationModeFactory.times(1)).setAssociatedProjectKeys(new HashSet<String>());
		verify(serverManager, VerificationModeFactory.times(1)).getServer(1);
	}

	/**
	 * Test get associated projects
	 * 
	 * @throws Exception in case of test failures
	 */
	@Test
	public void testGetAssociatedProjects() throws Exception {
		setServerFieldInServerProjectAssociations();
		when(projectManager.getProjectObjByKey(eq("MARVADMIN"))).thenReturn(project);
		when(project.getKey()).thenReturn("MARVADMIN");
		when(server.getAssociatedProjectKeys()).thenReturn(Collections.singletonList("MARVADMIN"));
		final Collection<Project> projects = serverProjectAssociations.getAssociatedProjects();
		assertFalse(projects.isEmpty());
		assertEquals(1, projects.size());
		assertTrue(projects.contains(project));
		verify(projectManager, VerificationModeFactory.times(1)).getProjectObjByKey("MARVADMIN");
		verify(server, VerificationModeFactory.times(1)).getAssociatedProjectKeys();
	}

	/**
	 * Test get available projects no exclusions
	 * 
	 * @throws Exception in case of test failures
	 */
	@Test
	public void testGetAvailableProjects() throws Exception {
		setServerFieldInServerProjectAssociations();
		when(projectManager.getProjectObjects()).thenReturn(Collections.singletonList(project));
		when(project.getKey()).thenReturn("MARVADMIN");
		final Collection<Project> values = serverProjectAssociations.getAvailableProjects();
		assertFalse(values.isEmpty());
		assertEquals(1, values.size());
		assertTrue(values.contains(project));
		verify(projectManager, VerificationModeFactory.times(1)).getProjectObjects();
		verify(project, VerificationModeFactory.times(1)).getKey();
	}

	/**
	 * Test get available projects with exclusions
	 * 
	 * @throws Exception in case of test failures
	 */
	@Test
	public void testGetEmptyAvailableProjects() throws Exception {
		setServerFieldInServerProjectAssociations();
		when(projectManager.getProjectObjects()).thenReturn(Collections.singletonList(project));
		when(project.getKey()).thenReturn("MARVADMIN");
		when(server.getAssociatedProjectKeys()).thenReturn(Collections.singletonList("MARVADMIN"));
		final Collection<Project> values = serverProjectAssociations.getAvailableProjects();
		assertTrue(values.isEmpty());
		verify(projectManager, VerificationModeFactory.times(1)).getProjectObjects();
		verify(project, VerificationModeFactory.times(1)).getKey();
		verify(server, VerificationModeFactory.times(1)).getAssociatedProjectKeys();
	}

	/**
	 * Try to set the hudsonServer field of the {@link #serverProjectAssociations} instance
	 * 
	 * @throws Exception in case the setting of the {@link Field} failes
	 */
	private void setServerFieldInServerProjectAssociations() throws Exception {
		final Field field = serverProjectAssociations.getClass().getDeclaredField("hudsonServer");
		field.setAccessible(true);
		field.set(serverProjectAssociations, server);
	}

}
