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

package com.marvelution.jira.plugins.hudson.services.servers;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.atlassian.activeobjects.ao.ActiveObjectsFieldNameConverter;
import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.activeobjects.test.TestActiveObjects;
import com.atlassian.jira.bc.whitelist.WhitelistManager;

import net.java.ao.EntityManager;
import net.java.ao.test.converters.NameConverters;
import net.java.ao.test.jdbc.Data;
import net.java.ao.test.jdbc.DatabaseUpdater;
import net.java.ao.test.junit.ActiveObjectsJUnitRunner;

/**
 * Testcase implementation for the {@link HudsonServerManagerService}
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
@RunWith(ActiveObjectsJUnitRunner.class)
@Data(HudsonServerManagerServiceTest.HudosnServerManagerServiceTestDatabaseUpdater.class)
@NameConverters(field = ActiveObjectsFieldNameConverter.class)
public class HudsonServerManagerServiceTest {

	private EntityManager entityManager;
	private ActiveObjects objects;
	private HudsonServerManagerService serverManager;

	@Mock private WhitelistManager whitelistManager;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		assertNotNull(entityManager);
		objects = new TestActiveObjects(entityManager);
		serverManager = new HudsonServerManagerService(objects, whitelistManager);
	}

	@Test
	public void testAddServerNameDescriptionHost() throws Exception {
		assertEquals(0, objects.count(HudsonServer.class));
		assertEquals(0, serverManager.getServers().size());
		HudsonServer server = serverManager.addServer("Some Name", "Some Description", "http://localhost:8090");
		assertEquals(1, server.getID());
		assertEquals("Some Name", server.getName());
		assertEquals("Some Description", server.getDescription());
		assertEquals("http://localhost:8090", server.getHost());
		assertEquals("http://localhost:8090", server.getPublicHost());
		assertEquals(false, server.isDefaultServer());
		assertEquals(false, HudsonServerUtils.isServerSecured(server));
		assertEquals(true, server.isIncludeInStreams());
		assertEquals(1, serverManager.getServers().size());
		assertEquals(1, objects.count(HudsonServer.class));
	}

	/**
	 * {@link DatabaseUpdater} implementation
	 * 
	 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
	 */
	public static class HudosnServerManagerServiceTestDatabaseUpdater implements DatabaseUpdater {

		/**
		 * {@inheritDoc}
		 */
		@SuppressWarnings("unchecked")
		@Override
		public void update(EntityManager entityManager) throws Exception {
			entityManager.migrate(HudsonServer.class);
		}

	}

}
