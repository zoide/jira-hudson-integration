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

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.internal.verification.VerificationModeFactory;

import com.atlassian.jira.project.Project;
import com.atlassian.jira.project.ProjectManager;

import webwork.action.ActionSupport;

/**
 * TestCase for
 *  
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class AssociateSpecificHudsonServerTest extends AbstractHudsonWebActionSupportTest {

	private AssociateSpecificHudsonServer associateSpecificHudsonServer;

	@Mock
	private ProjectManager projectManager;

	@Mock
	private Project project;

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected ActionSupport createWebworkAction() {
		when(projectManager.getProjectObjByKey(eq("MARVADMIN"))).thenReturn(project);
		when(serverManager.getServerByJiraProject(eq(project))).thenReturn(server);
		when(serverManager.getServer(eq(1))).thenReturn(server);
		when(project.getKey()).thenReturn("MARVADMIN");
		when(server.getServerId()).thenReturn(1);
		associateSpecificHudsonServer =
			new AssociateSpecificHudsonServer(permissionManager, projectManager, serverManager) {

			private static final long serialVersionUID = 1L;

			public String getText(String key) {
				return key;
			}

		};
		return associateSpecificHudsonServer;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void testDoDefaultWithoutPermission() throws Exception {
		associateSpecificHudsonServer.setProjectKey("MARVADMIN");
		super.testDoDefaultWithoutPermission();
		verify(projectManager, VerificationModeFactory.atMost(1)).getProjectObjByKey("MARVADMIN");
		verify(serverManager, VerificationModeFactory.atMost(1)).getServerByJiraProject(project);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void testDoDefaultWithPermission() throws Exception {
		associateSpecificHudsonServer.setProjectKey("MARVADMIN");
		super.testDoDefaultWithPermission();
		verify(projectManager, VerificationModeFactory.atMost(1)).getProjectObjByKey("MARVADMIN");
		verify(serverManager, VerificationModeFactory.atMost(1)).getServerByJiraProject(project);
	}

	/**
	 * {@inheritDoc}
	 */
	@Test
	public void testDoDefaultNoProjectKey() throws Exception {
		verify(projectManager, VerificationModeFactory.atMost(1)).getProjectObjByKey("MARVADMIN");
		assertEquals("none", webworkAction.doDefault());
	}

	/**
	 * Test the execution with errors
	 * 
	 * @throws Exception in case of test failures
	 */
	@Test
	public void testDoExecuteWithErrors() throws Exception {
		associateSpecificHudsonServer.addErrorMessage("hudson.config.error.hudson.server.required");
		associateSpecificHudsonServer.setHudsonServerId(1);
		assertEquals("input", associateSpecificHudsonServer.doExecute());
	}

	/**
	 * Test the execution without errors
	 * 
	 * @throws Exception in case of test failures
	 */
	@Test
	public void testDoExecuteWithoutErrors() throws Exception {
		associateSpecificHudsonServer.setProjectKey("MARVADMIN");
		associateSpecificHudsonServer.setHudsonServerId(1);
		assertEquals("none", associateSpecificHudsonServer.doExecute());
		verify(serverManager, VerificationModeFactory.atMost(1)).getServer(1);
		verify(serverManager, VerificationModeFactory.atMost(1)).put(server);
		verify(server).addAssociatedProjectKey("MARVADMIN");
	}

	/**
	 * Test the get current server id
	 */
	@Test
	public void testGetCurrentAssociatedHudsonServerId() {
		associateSpecificHudsonServer.setProjectKey("MARVADMIN");
		assertEquals(1, associateSpecificHudsonServer.getCurrentAssociatedHudsonServerId());
		verify(serverManager, VerificationModeFactory.atMost(1)).getServerByJiraProject(project);
		verify(projectManager, VerificationModeFactory.atMost(1)).getProjectObjByKey("MARVADMIN");
		verify(server, VerificationModeFactory.atMost(1)).getServerId();
	}

	/**
	 * Test validation with invalid server id
	 */
	@Test
	public void testDoValidationInvalidServerId() {
		associateSpecificHudsonServer.setHudsonServerId(-1);
		associateSpecificHudsonServer.doValidation();
		assertFalse(associateSpecificHudsonServer.getErrorMessages().isEmpty());
		assertTrue(associateSpecificHudsonServer.getErrorMessages().contains(
			"hudson.config.error.hudson.server.required"));
	}

	/**
	 * Test validation with no server found
	 */
	@Test
	public void testDoValidationNoServer() {
		associateSpecificHudsonServer.setHudsonServerId(2);
		associateSpecificHudsonServer.doValidation();
		assertFalse(associateSpecificHudsonServer.getErrorMessages().isEmpty());
		assertTrue(associateSpecificHudsonServer.getErrorMessages().contains(
			"hudson.config.error.hudson.server.required"));
		
	}

	/**
	 * Test validation
	 */
	@Test
	public void testDoValidation() {
		associateSpecificHudsonServer.setHudsonServerId(1);
		associateSpecificHudsonServer.doValidation();
		assertTrue(associateSpecificHudsonServer.getErrorMessages().isEmpty());
	}

}
