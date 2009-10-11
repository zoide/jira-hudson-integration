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

package com.marvelution.jira.plugins.hudson;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;

import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.verification.VerificationModeFactory;

import com.atlassian.jira.project.Project;
import com.atlassian.jira.project.ProjectManager;
import com.marvelution.jira.plugins.hudson.service.HudsonPropertyManager;
import com.marvelution.jira.plugins.hudson.service.HudsonServer;
import com.marvelution.jira.plugins.hudson.service.HudsonServerFactory;
import com.opensymphony.module.propertyset.PropertySet;

/**
 * TestCase for the {@link DefaultHudsonServerManagerImpl}
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class DefaultHudsonServerManagerImplTest {

	private DefaultHudsonServerManagerImpl serverManager;

	private HudsonServerFactory serverFactory;

	@Mock
	private HudsonPropertyManager propertyManager;

	@Mock
	private PropertySet propertySet;

	@Mock
	private ProjectManager projectManager;

	@Mock
	private Project project;

	/**
	 * Setup the test variables
	 * 
	 * @throws Exception in case of failures
	 */
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		serverFactory = new DefaultHudsonServerFactoryImpl();
		when(propertyManager.getPropertySet()).thenReturn(propertySet);
		when(projectManager.getProjectObjByKey(eq("MARVADMIN"))).thenReturn(project);
	}

	/**
	 * Test if hudson servers are configured
	 */
	@Test
	public void testHudsonConfigured() {
		setUpServerManagerWithServersAndDefaultConfigured();
		assertTrue(serverManager.isHudsonConfigured());
	}

	/**
	 * Test if hudson servers are configured
	 */
	@Test
	public void testHudsonNotConfigured() {
		serverManager = new DefaultHudsonServerManagerImpl(propertyManager, serverFactory, projectManager);
		assertFalse(serverManager.isHudsonConfigured());
	}

	/**
	 * Test has servers
	 */
	@Test
	public void testHasServers() {
		setUpServerManagerWithServers();
		assertTrue(serverManager.hasServers());
	}

	/**
	 * Test has servers
	 */
	@Test
	public void testHasNoServers() {
		serverManager = new DefaultHudsonServerManagerImpl(propertyManager, serverFactory, projectManager);
		assertTrue(serverManager.getServers().isEmpty());
	}

	/**
	 * Test has server
	 */
	@Test
	public void testHasServer() {
		setUpServerManagerWithServers();
		assertTrue(serverManager.hasServer("Hudson CI"));
	}

	/**
	 * Test has server
	 */
	@Test
	public void testHasNoServer() {
		serverManager = new DefaultHudsonServerManagerImpl(propertyManager, serverFactory, projectManager);
		assertFalse(serverManager.hasServer("Hudson CI"));
	}

	/**
	 * Test has server
	 */
	@Test
	public void testHasServerEmptyName() {
		serverManager = new DefaultHudsonServerManagerImpl(propertyManager, serverFactory, projectManager);
		assertFalse(serverManager.hasServer(""));
	}

	/**
	 * Test get servers
	 */
	@Test
	public void testGetServers() {
		setUpServerManagerWithServers();
		assertFalse(serverManager.getServers().isEmpty());
		assertEquals(2, serverManager.getServers().size());
	}

	/**
	 * Test get server by Id
	 */
	@Test
	public void testGetServerById() {
		setUpServerManagerWithServers();
		assertNotNull(serverManager.getServer(1));
		assertNull(serverManager.getServer(3));
		assertNull(serverManager.getServer(0));
	}

	/**
	 * Test get server by name
	 */
	@Test
	public void testGetServerByName() {
		setUpServerManagerWithServers();
		assertNotNull(serverManager.getServer("Hudson CI"));
		assertNull(serverManager.getServer("ABC"));
	}

	/**
	 * Test get server by project
	 */
	@Test
	public void testGetServerByJiraProject() {
		when(project.getKey()).thenReturn("MARVADMIN");
		setUpServerManagerWithServers();
		final HudsonServer server = serverManager.getServerByJiraProject(project);
		assertNotNull(server);
		assertEquals(1, server.getServerId());
		assertTrue(server.getAssociatedProjectKeys().contains("MARVADMIN"));
	}

	/**
	 * Test get server by project
	 */
	@Test
	public void testGetServerByJiraProjectNoAssociationForProject() {
		setUpServerManagerWithServersAndDefaultConfigured();
		final HudsonServer server = serverManager.getServerByJiraProject(project);
		assertNotNull(server);
		assertTrue(serverManager.isDefaultServer(server));
	}

	/**
	 * Test set default server
	 */
	@Test
	public void testSetDefaultServer() {
		setUpServerManagerWithServers();
		serverManager.setDefaultServer(serverManager.getServer(2));
		verify(propertySet, VerificationModeFactory.times(1)).setInt(
			DefaultHudsonServerManagerImpl.CONFIG_DEFAULT_SERVER_ID, 2);
	}

	/**
	 * Test set default server
	 */
	@Test
	public void testSetDefaultServerInvalidServer() {
		setUpServerManagerWithServers();
		try {
			serverManager.setDefaultServer(null);
			fail("This test should fail");
		} catch (IllegalArgumentException e) {
			assertEquals("hudsonServer may not be null", e.getMessage());
		}
		verify(propertySet, VerificationModeFactory.times(0)).setInt(
			DefaultHudsonServerManagerImpl.CONFIG_DEFAULT_SERVER_ID, 2);
	}

	/**
	 * Test adding a server
	 */
	@Test
	public void testPutInvalidServer() {
		setUpServerManagerWithServers();
		try {
			serverManager.put(null);
			fail("This test should fail");
		} catch (IllegalArgumentException e) {
			assertEquals("hudsonServer may not be null", e.getMessage());
		}
	}

	/**
	 * Test adding a server
	 */
	@Test
	public void testPutAvailableNextId() {
		when(propertySet.exists(DefaultHudsonServerManagerImpl.CONFIG_NEXT_SERVER_ID)).thenReturn(true);
		when(propertySet.getInt(DefaultHudsonServerManagerImpl.CONFIG_NEXT_SERVER_ID)).thenReturn(3);
		when(projectManager.getProjectObjByKey("MARVADMIN")).thenReturn(project);
		when(project.getKey()).thenReturn("MARVADMIN");
		setUpServerManagerWithServers();
		final HudsonServer server = serverFactory.createHudsonServer();
		server.setName("Addning New");
		server.setHost("http://hudson.marvelution.com");
		server.addAssociatedProjectKey("MARVADMIN");
		serverManager.put(server);
		verify(propertySet, VerificationModeFactory.times(1)).exists(
			DefaultHudsonServerManagerImpl.CONFIG_NEXT_SERVER_ID);
		verify(propertySet, VerificationModeFactory.times(1)).getInt(
			DefaultHudsonServerManagerImpl.CONFIG_NEXT_SERVER_ID);
		verify(propertySet, VerificationModeFactory.times(1)).setInt(
			DefaultHudsonServerManagerImpl.CONFIG_NEXT_SERVER_ID, 4);
		verify(propertySet, VerificationModeFactory.times(1)).setInt(
			DefaultHudsonServerManagerImpl.CONFIG_SERVER_KEY_PREFIX + 3
				+ DefaultHudsonServerManagerImpl.CONFIG_SERVER_ID_KEY_SUFFIX, server.getServerId());
		verify(propertySet, VerificationModeFactory.times(1)).setString(
			DefaultHudsonServerManagerImpl.CONFIG_SERVER_KEY_PREFIX + 3
				+ DefaultHudsonServerManagerImpl.CONFIG_SERVER_NAME_KEY_SUFFIX, server.getName());
		verify(propertySet, VerificationModeFactory.times(1)).setString(
			DefaultHudsonServerManagerImpl.CONFIG_SERVER_KEY_PREFIX + 3
				+ DefaultHudsonServerManagerImpl.CONFIG_SERVER_DESCRIPTION_KEY_SUFFIX, server.getDescription());
		verify(propertySet, VerificationModeFactory.times(1)).setString(
			DefaultHudsonServerManagerImpl.CONFIG_SERVER_KEY_PREFIX + 3
				+ DefaultHudsonServerManagerImpl.CONFIG_SERVER_HOST_KEY_SUFFIX, server.getHost());
		verify(propertySet, VerificationModeFactory.times(1)).setString(
			DefaultHudsonServerManagerImpl.CONFIG_SERVER_KEY_PREFIX + 3
				+ DefaultHudsonServerManagerImpl.CONFIG_SERVER_USERNAME_KEY_SUFFIX, server.getUsername());
		verify(propertySet, VerificationModeFactory.times(1)).setString(
			DefaultHudsonServerManagerImpl.CONFIG_SERVER_KEY_PREFIX + 3
				+ DefaultHudsonServerManagerImpl.CONFIG_SERVER_PASSWORD_KEY_SUFFIX, "");
		final String projects = StringUtils.join(server.getAssociatedProjectKeys(), ',');
		verify(propertySet, VerificationModeFactory.times(1)).setString(
			DefaultHudsonServerManagerImpl.CONFIG_SERVER_KEY_PREFIX + 3
				+ DefaultHudsonServerManagerImpl.CONFIG_SERVER_PROJECTS_KEY_SUFFIX, projects);
	}

	/**
	 * Test adding a server
	 */
	@Test
	public void testPut() {
		when(projectManager.getProjectObjByKey("MARVADMIN")).thenReturn(project);
		when(project.getKey()).thenReturn("MARVADMIN");
		setUpServerManagerWithServers();
		final HudsonServer server = serverFactory.createHudsonServer();
		server.setName("Addning New");
		server.setHost("http://hudson.marvelution.com");
		server.addAssociatedProjectKey("MARVADMIN");
		serverManager.put(server);
		verify(propertySet, VerificationModeFactory.times(1)).exists(
			DefaultHudsonServerManagerImpl.CONFIG_NEXT_SERVER_ID);
		verify(propertySet, VerificationModeFactory.times(1)).setInt(
			DefaultHudsonServerManagerImpl.CONFIG_NEXT_SERVER_ID, 2);
		verify(propertySet, VerificationModeFactory.times(1)).setInt(
			DefaultHudsonServerManagerImpl.CONFIG_SERVER_KEY_PREFIX + 1
				+ DefaultHudsonServerManagerImpl.CONFIG_SERVER_ID_KEY_SUFFIX, server.getServerId());
		verify(propertySet, VerificationModeFactory.times(1)).setString(
			DefaultHudsonServerManagerImpl.CONFIG_SERVER_KEY_PREFIX + 1
				+ DefaultHudsonServerManagerImpl.CONFIG_SERVER_NAME_KEY_SUFFIX, server.getName());
		verify(propertySet, VerificationModeFactory.times(1)).setString(
			DefaultHudsonServerManagerImpl.CONFIG_SERVER_KEY_PREFIX + 1
				+ DefaultHudsonServerManagerImpl.CONFIG_SERVER_DESCRIPTION_KEY_SUFFIX, server.getDescription());
		verify(propertySet, VerificationModeFactory.times(1)).setString(
			DefaultHudsonServerManagerImpl.CONFIG_SERVER_KEY_PREFIX + 1
				+ DefaultHudsonServerManagerImpl.CONFIG_SERVER_HOST_KEY_SUFFIX, server.getHost());
		verify(propertySet, VerificationModeFactory.times(1)).setString(
			DefaultHudsonServerManagerImpl.CONFIG_SERVER_KEY_PREFIX + 1
				+ DefaultHudsonServerManagerImpl.CONFIG_SERVER_USERNAME_KEY_SUFFIX, server.getUsername());
		verify(propertySet, VerificationModeFactory.times(1)).setString(
			DefaultHudsonServerManagerImpl.CONFIG_SERVER_KEY_PREFIX + 1
				+ DefaultHudsonServerManagerImpl.CONFIG_SERVER_PASSWORD_KEY_SUFFIX, "");
		final String projects = StringUtils.join(server.getAssociatedProjectKeys(), ',');
		verify(propertySet, VerificationModeFactory.times(1)).setString(
			DefaultHudsonServerManagerImpl.CONFIG_SERVER_KEY_PREFIX + 1
				+ DefaultHudsonServerManagerImpl.CONFIG_SERVER_PROJECTS_KEY_SUFFIX, projects);
	}

	/**
	 * Test removing a server
	 */
	@Test
	public void testRemoveServerInvalidServerId() {
		setUpServerManagerWithServers();
		try {
			serverManager.remove(0);
			fail("This test should fail");
		} catch (IllegalArgumentException e) {
			assertEquals("serverId may not be less then or equal to 0 (zero)", e.getMessage());
		}
	}

	/**
	 * Test removing a server
	 */
	@Test
	public void testRemoveServerUnknownServer() {
		setUpServerManagerWithServers();
		try {
			serverManager.remove(3);
			fail("This test should fail");
		} catch (IllegalStateException e) {
			assertEquals("No Hudson Server configured with Id: 3", e.getMessage());
		}
	}

	/**
	 * Test removing a server
	 */
	@Test
	public void testRemoveServer() {
		setUpServerManagerWithServers();
		serverManager.remove(1);
		assertFalse(serverManager.hasServer("Hudson CI"));
		assertNull(serverManager.getServer(1));
		verify(propertySet, VerificationModeFactory.times(1)).remove(
			DefaultHudsonServerManagerImpl.CONFIG_SERVER_KEY_PREFIX + 1
				+ DefaultHudsonServerManagerImpl.CONFIG_SERVER_ID_KEY_SUFFIX);
		verify(propertySet, VerificationModeFactory.times(1)).remove(
			DefaultHudsonServerManagerImpl.CONFIG_SERVER_KEY_PREFIX + 1
				+ DefaultHudsonServerManagerImpl.CONFIG_SERVER_NAME_KEY_SUFFIX);
		verify(propertySet, VerificationModeFactory.times(1)).remove(
			DefaultHudsonServerManagerImpl.CONFIG_SERVER_KEY_PREFIX + 1
				+ DefaultHudsonServerManagerImpl.CONFIG_SERVER_DESCRIPTION_KEY_SUFFIX);
		verify(propertySet, VerificationModeFactory.times(1)).remove(
			DefaultHudsonServerManagerImpl.CONFIG_SERVER_KEY_PREFIX + 1
				+ DefaultHudsonServerManagerImpl.CONFIG_SERVER_HOST_KEY_SUFFIX);
		verify(propertySet, VerificationModeFactory.times(1)).remove(
			DefaultHudsonServerManagerImpl.CONFIG_SERVER_KEY_PREFIX + 1
				+ DefaultHudsonServerManagerImpl.CONFIG_SERVER_USERNAME_KEY_SUFFIX);
		verify(propertySet, VerificationModeFactory.times(1)).remove(
			DefaultHudsonServerManagerImpl.CONFIG_SERVER_KEY_PREFIX + 1
				+ DefaultHudsonServerManagerImpl.CONFIG_SERVER_PASSWORD_KEY_SUFFIX);
		verify(propertySet, VerificationModeFactory.times(1)).remove(
			DefaultHudsonServerManagerImpl.CONFIG_SERVER_KEY_PREFIX + 1
				+ DefaultHudsonServerManagerImpl.CONFIG_SERVER_PROJECTS_KEY_SUFFIX);
	}

	/**
	 * Test removing a default server
	 */
	@Test
	public void testRemoveDefaultServer() {
		setUpServerManagerWithServersAndDefaultConfigured();
		serverManager.remove(2);
		assertFalse(serverManager.hasServer("Hudson 2"));
		assertNull(serverManager.getServer(2));
		verify(propertySet, VerificationModeFactory.times(2)).setInt(
			DefaultHudsonServerManagerImpl.CONFIG_DEFAULT_SERVER_ID, 1);
		verify(propertySet, VerificationModeFactory.times(1)).remove(
			DefaultHudsonServerManagerImpl.CONFIG_SERVER_KEY_PREFIX + 2
				+ DefaultHudsonServerManagerImpl.CONFIG_SERVER_ID_KEY_SUFFIX);
		verify(propertySet, VerificationModeFactory.times(1)).remove(
			DefaultHudsonServerManagerImpl.CONFIG_SERVER_KEY_PREFIX + 2
				+ DefaultHudsonServerManagerImpl.CONFIG_SERVER_NAME_KEY_SUFFIX);
		verify(propertySet, VerificationModeFactory.times(1)).remove(
			DefaultHudsonServerManagerImpl.CONFIG_SERVER_KEY_PREFIX + 2
				+ DefaultHudsonServerManagerImpl.CONFIG_SERVER_DESCRIPTION_KEY_SUFFIX);
		verify(propertySet, VerificationModeFactory.times(1)).remove(
			DefaultHudsonServerManagerImpl.CONFIG_SERVER_KEY_PREFIX + 2
				+ DefaultHudsonServerManagerImpl.CONFIG_SERVER_HOST_KEY_SUFFIX);
		verify(propertySet, VerificationModeFactory.times(1)).remove(
			DefaultHudsonServerManagerImpl.CONFIG_SERVER_KEY_PREFIX + 2
				+ DefaultHudsonServerManagerImpl.CONFIG_SERVER_USERNAME_KEY_SUFFIX);
		verify(propertySet, VerificationModeFactory.times(1)).remove(
			DefaultHudsonServerManagerImpl.CONFIG_SERVER_KEY_PREFIX + 2
				+ DefaultHudsonServerManagerImpl.CONFIG_SERVER_PASSWORD_KEY_SUFFIX);
		verify(propertySet, VerificationModeFactory.times(1)).remove(
			DefaultHudsonServerManagerImpl.CONFIG_SERVER_KEY_PREFIX + 2
				+ DefaultHudsonServerManagerImpl.CONFIG_SERVER_PROJECTS_KEY_SUFFIX);
	}

	/**
	 * Test removing last default server
	 */
	@Test
	public void testRemoveLastDefaultServer() {
		setUpServerManagerWithServersAndDefaultConfigured();
		serverManager.remove(1);
		serverManager.remove(2);
		assertFalse(serverManager.hasServer("Hudson 2"));
		assertNull(serverManager.getServer(2));
		verify(propertySet, VerificationModeFactory.times(1)).remove(
			DefaultHudsonServerManagerImpl.CONFIG_DEFAULT_SERVER_ID);
	}

	/**
	 * Setup the serverManager with servers
	 */
	private void setUpServerManagerWithServers() {
		final String[] propertyKeys =
			new String[] {
				DefaultHudsonServerManagerImpl.CONFIG_SERVER_KEY_PREFIX + 1
					+ DefaultHudsonServerManagerImpl.CONFIG_SERVER_ID_KEY_SUFFIX,
				DefaultHudsonServerManagerImpl.CONFIG_SERVER_KEY_PREFIX + 1
					+ DefaultHudsonServerManagerImpl.CONFIG_SERVER_NAME_KEY_SUFFIX,
				DefaultHudsonServerManagerImpl.CONFIG_SERVER_KEY_PREFIX + 1
					+ DefaultHudsonServerManagerImpl.CONFIG_SERVER_DESCRIPTION_KEY_SUFFIX,
				DefaultHudsonServerManagerImpl.CONFIG_SERVER_KEY_PREFIX + 1
					+ DefaultHudsonServerManagerImpl.CONFIG_SERVER_HOST_KEY_SUFFIX,
				DefaultHudsonServerManagerImpl.CONFIG_SERVER_KEY_PREFIX + 1
					+ DefaultHudsonServerManagerImpl.CONFIG_SERVER_USERNAME_KEY_SUFFIX,
				DefaultHudsonServerManagerImpl.CONFIG_SERVER_KEY_PREFIX + 1
					+ DefaultHudsonServerManagerImpl.CONFIG_SERVER_PASSWORD_KEY_SUFFIX,
				DefaultHudsonServerManagerImpl.CONFIG_SERVER_KEY_PREFIX + 1
					+ DefaultHudsonServerManagerImpl.CONFIG_SERVER_PROJECTS_KEY_SUFFIX,
				DefaultHudsonServerManagerImpl.CONFIG_SERVER_KEY_PREFIX + 2
					+ DefaultHudsonServerManagerImpl.CONFIG_SERVER_ID_KEY_SUFFIX,
				DefaultHudsonServerManagerImpl.CONFIG_SERVER_KEY_PREFIX + 2
					+ DefaultHudsonServerManagerImpl.CONFIG_SERVER_NAME_KEY_SUFFIX,
				DefaultHudsonServerManagerImpl.CONFIG_SERVER_KEY_PREFIX + 2
					+ DefaultHudsonServerManagerImpl.CONFIG_SERVER_DESCRIPTION_KEY_SUFFIX,
				DefaultHudsonServerManagerImpl.CONFIG_SERVER_KEY_PREFIX + 2
					+ DefaultHudsonServerManagerImpl.CONFIG_SERVER_HOST_KEY_SUFFIX,
				DefaultHudsonServerManagerImpl.CONFIG_SERVER_KEY_PREFIX + 2
					+ DefaultHudsonServerManagerImpl.CONFIG_SERVER_USERNAME_KEY_SUFFIX,
				DefaultHudsonServerManagerImpl.CONFIG_SERVER_KEY_PREFIX + 2
					+ DefaultHudsonServerManagerImpl.CONFIG_SERVER_PASSWORD_KEY_SUFFIX,
				DefaultHudsonServerManagerImpl.CONFIG_SERVER_KEY_PREFIX + 2
					+ DefaultHudsonServerManagerImpl.CONFIG_SERVER_PROJECTS_KEY_SUFFIX,
		};
		when(propertySet.getKeys(DefaultHudsonServerManagerImpl.CONFIG_SERVER_KEY_PREFIX)).thenReturn(
			Arrays.asList(propertyKeys));
		when(
			propertySet.getInt(DefaultHudsonServerManagerImpl.CONFIG_SERVER_KEY_PREFIX + 1
				+ DefaultHudsonServerManagerImpl.CONFIG_SERVER_ID_KEY_SUFFIX)).thenReturn(1);
		when(
			propertySet.getString(DefaultHudsonServerManagerImpl.CONFIG_SERVER_KEY_PREFIX + 1
				+ DefaultHudsonServerManagerImpl.CONFIG_SERVER_NAME_KEY_SUFFIX)).thenReturn("Hudson CI");
		when(
			propertySet.getString(DefaultHudsonServerManagerImpl.CONFIG_SERVER_KEY_PREFIX + 1
				+ DefaultHudsonServerManagerImpl.CONFIG_SERVER_DESCRIPTION_KEY_SUFFIX)).thenReturn("");
		when(
			propertySet.getString(DefaultHudsonServerManagerImpl.CONFIG_SERVER_KEY_PREFIX + 1
				+ DefaultHudsonServerManagerImpl.CONFIG_SERVER_HOST_KEY_SUFFIX)).thenReturn("http://hudson.com");
		when(
			propertySet.getString(DefaultHudsonServerManagerImpl.CONFIG_SERVER_KEY_PREFIX + 1
				+ DefaultHudsonServerManagerImpl.CONFIG_SERVER_USERNAME_KEY_SUFFIX)).thenReturn("");
		when(
			propertySet.getString(DefaultHudsonServerManagerImpl.CONFIG_SERVER_KEY_PREFIX + 1
				+ DefaultHudsonServerManagerImpl.CONFIG_SERVER_PASSWORD_KEY_SUFFIX)).thenReturn("");
		when(
			propertySet.getString(DefaultHudsonServerManagerImpl.CONFIG_SERVER_KEY_PREFIX + 1
				+ DefaultHudsonServerManagerImpl.CONFIG_SERVER_PROJECTS_KEY_SUFFIX)).thenReturn("MARVADMIN");
		when(
			propertySet.getInt(DefaultHudsonServerManagerImpl.CONFIG_SERVER_KEY_PREFIX + 2
				+ DefaultHudsonServerManagerImpl.CONFIG_SERVER_ID_KEY_SUFFIX)).thenReturn(2);
		when(
			propertySet.getString(DefaultHudsonServerManagerImpl.CONFIG_SERVER_KEY_PREFIX + 2
				+ DefaultHudsonServerManagerImpl.CONFIG_SERVER_NAME_KEY_SUFFIX)).thenReturn("Hudson 2");
		when(
			propertySet.getString(DefaultHudsonServerManagerImpl.CONFIG_SERVER_KEY_PREFIX + 2
				+ DefaultHudsonServerManagerImpl.CONFIG_SERVER_DESCRIPTION_KEY_SUFFIX)).thenReturn("Some server");
		when(
			propertySet.getString(DefaultHudsonServerManagerImpl.CONFIG_SERVER_KEY_PREFIX + 2
				+ DefaultHudsonServerManagerImpl.CONFIG_SERVER_HOST_KEY_SUFFIX)).thenReturn("http://ci.hudson.com");
		when(
			propertySet.getString(DefaultHudsonServerManagerImpl.CONFIG_SERVER_KEY_PREFIX + 2
				+ DefaultHudsonServerManagerImpl.CONFIG_SERVER_USERNAME_KEY_SUFFIX)).thenReturn("admin");
		when(
			propertySet.getString(DefaultHudsonServerManagerImpl.CONFIG_SERVER_KEY_PREFIX + 2
				+ DefaultHudsonServerManagerImpl.CONFIG_SERVER_PASSWORD_KEY_SUFFIX)).thenReturn("tH9fJgeL6Rw=");
		when(
			propertySet.getString(DefaultHudsonServerManagerImpl.CONFIG_SERVER_KEY_PREFIX + 2
				+ DefaultHudsonServerManagerImpl.CONFIG_SERVER_PROJECTS_KEY_SUFFIX)).thenReturn("MARV");
		serverManager = new DefaultHudsonServerManagerImpl(propertyManager, serverFactory, projectManager);
		verify(propertySet, VerificationModeFactory.times(1)).getKeys(
			DefaultHudsonServerManagerImpl.CONFIG_SERVER_KEY_PREFIX);
		verify(propertySet, VerificationModeFactory.times(1)).getInt(
			DefaultHudsonServerManagerImpl.CONFIG_SERVER_KEY_PREFIX + 1
				+ DefaultHudsonServerManagerImpl.CONFIG_SERVER_ID_KEY_SUFFIX);
		verify(propertySet, VerificationModeFactory.times(1)).getString(
			DefaultHudsonServerManagerImpl.CONFIG_SERVER_KEY_PREFIX + 1
				+ DefaultHudsonServerManagerImpl.CONFIG_SERVER_NAME_KEY_SUFFIX);
		verify(propertySet, VerificationModeFactory.times(1)).getString(
			DefaultHudsonServerManagerImpl.CONFIG_SERVER_KEY_PREFIX + 1
				+ DefaultHudsonServerManagerImpl.CONFIG_SERVER_DESCRIPTION_KEY_SUFFIX);
		verify(propertySet, VerificationModeFactory.times(1)).getString(
			DefaultHudsonServerManagerImpl.CONFIG_SERVER_KEY_PREFIX + 1
				+ DefaultHudsonServerManagerImpl.CONFIG_SERVER_HOST_KEY_SUFFIX);
		verify(propertySet, VerificationModeFactory.times(1)).getString(
			DefaultHudsonServerManagerImpl.CONFIG_SERVER_KEY_PREFIX + 1
				+ DefaultHudsonServerManagerImpl.CONFIG_SERVER_USERNAME_KEY_SUFFIX);
		verify(propertySet, VerificationModeFactory.times(1)).getString(
			DefaultHudsonServerManagerImpl.CONFIG_SERVER_KEY_PREFIX + 1
				+ DefaultHudsonServerManagerImpl.CONFIG_SERVER_PASSWORD_KEY_SUFFIX);
		verify(propertySet, VerificationModeFactory.times(1)).getString(
			DefaultHudsonServerManagerImpl.CONFIG_SERVER_KEY_PREFIX + 1
				+ DefaultHudsonServerManagerImpl.CONFIG_SERVER_PROJECTS_KEY_SUFFIX);
		verify(propertySet, VerificationModeFactory.times(1)).getInt(
			DefaultHudsonServerManagerImpl.CONFIG_SERVER_KEY_PREFIX + 2
				+ DefaultHudsonServerManagerImpl.CONFIG_SERVER_ID_KEY_SUFFIX);
		verify(propertySet, VerificationModeFactory.times(1)).getString(
			DefaultHudsonServerManagerImpl.CONFIG_SERVER_KEY_PREFIX + 2
				+ DefaultHudsonServerManagerImpl.CONFIG_SERVER_NAME_KEY_SUFFIX);
		verify(propertySet, VerificationModeFactory.times(1)).getString(
			DefaultHudsonServerManagerImpl.CONFIG_SERVER_KEY_PREFIX + 2
				+ DefaultHudsonServerManagerImpl.CONFIG_SERVER_DESCRIPTION_KEY_SUFFIX);
		verify(propertySet, VerificationModeFactory.times(1)).getString(
			DefaultHudsonServerManagerImpl.CONFIG_SERVER_KEY_PREFIX + 2
				+ DefaultHudsonServerManagerImpl.CONFIG_SERVER_HOST_KEY_SUFFIX);
		verify(propertySet, VerificationModeFactory.times(1)).getString(
			DefaultHudsonServerManagerImpl.CONFIG_SERVER_KEY_PREFIX + 2
				+ DefaultHudsonServerManagerImpl.CONFIG_SERVER_USERNAME_KEY_SUFFIX);
		verify(propertySet, VerificationModeFactory.times(1)).getString(
			DefaultHudsonServerManagerImpl.CONFIG_SERVER_KEY_PREFIX + 2
				+ DefaultHudsonServerManagerImpl.CONFIG_SERVER_PASSWORD_KEY_SUFFIX);
		verify(propertySet, VerificationModeFactory.times(1)).getString(
			DefaultHudsonServerManagerImpl.CONFIG_SERVER_KEY_PREFIX + 2
				+ DefaultHudsonServerManagerImpl.CONFIG_SERVER_PROJECTS_KEY_SUFFIX);
	}

	/**
	 * Setup the serverManager with servers
	 */
	private void setUpServerManagerWithServersAndDefaultConfigured() {
		when(propertySet.exists(DefaultHudsonServerManagerImpl.CONFIG_DEFAULT_SERVER_ID)).thenReturn(true);
		when(propertySet.getInt(DefaultHudsonServerManagerImpl.CONFIG_DEFAULT_SERVER_ID)).thenReturn(2);
		setUpServerManagerWithServers();
		verify(propertySet, VerificationModeFactory.times(1)).exists(
			DefaultHudsonServerManagerImpl.CONFIG_DEFAULT_SERVER_ID);
		verify(propertySet, VerificationModeFactory.times(2)).getInt(
			DefaultHudsonServerManagerImpl.CONFIG_DEFAULT_SERVER_ID);
	}

}
