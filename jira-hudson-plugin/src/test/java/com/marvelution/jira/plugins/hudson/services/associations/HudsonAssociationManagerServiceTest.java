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

package com.marvelution.jira.plugins.hudson.services.associations;

import static org.junit.Assert.*;

import net.java.ao.EntityManager;
import net.java.ao.test.converters.NameConverters;
import net.java.ao.test.jdbc.Data;
import net.java.ao.test.jdbc.DatabaseUpdater;
import net.java.ao.test.junit.ActiveObjectsJUnitRunner;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.atlassian.activeobjects.ao.ActiveObjectsFieldNameConverter;
import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.activeobjects.test.TestActiveObjects;
import com.atlassian.jira.bc.whitelist.WhitelistManager;
import com.marvelution.jira.plugins.hudson.services.servers.HudsonServer;
import com.marvelution.jira.plugins.hudson.services.servers.HudsonServerManager;
import com.marvelution.jira.plugins.hudson.services.servers.HudsonServerManagerService;

/**
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
@RunWith(ActiveObjectsJUnitRunner.class)
@Data(HudsonAssociationManagerServiceTest.HudosnAssociationManagerServiceTestDatabaseUpdater.class)
@NameConverters(field = ActiveObjectsFieldNameConverter.class)
public class HudsonAssociationManagerServiceTest {

	private EntityManager entityManager;
	private ActiveObjects objects;
	private HudsonAssociationManager associationManager;
	private HudsonServerManager serverManager;
	@Mock private WhitelistManager whitelistManager;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		assertNotNull(entityManager);
		objects = new TestActiveObjects(entityManager);
		serverManager = new HudsonServerManagerService(objects, whitelistManager);
		associationManager = new HudsonAssociationManagerService(objects, serverManager);
	}

	@Test
	public void testAddAssocation() throws Exception {
		assertEquals(1, objects.count(HudsonServer.class));
		assertEquals(0, objects.count(HudsonAssociation.class));
		assertEquals(1, serverManager.getServers().size());
		assertEquals(0, associationManager.getAssociations().size());
		HudsonServer server = serverManager.getServer(1);
		HudsonAssociation association = associationManager.addAssociation(server, 1L, "Test Job");
		assertEquals(1, association.getID());
		assertEquals(1, association.getServer().getID());
		assertEquals("Test Job", association.getJobName());
		assertEquals(1, objects.count(HudsonServer.class));
		assertEquals(1, objects.count(HudsonAssociation.class));
		assertEquals(1, serverManager.getServers().size());
		assertEquals(1, associationManager.getAssociations().size());
	}

	/**
	 * {@link DatabaseUpdater} implementation
	 * 
	 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
	 */
	public static class HudosnAssociationManagerServiceTestDatabaseUpdater implements DatabaseUpdater {

		/**
		 * {@inheritDoc}
		 */
		@SuppressWarnings("unchecked")
		@Override
		public void update(EntityManager entityManager) throws Exception {
			entityManager.migrate(HudsonServer.class, HudsonAssociation.class);
			HudsonServer server = entityManager.create(HudsonServer.class);
			server.setName("Test Hudson CI");
			server.setHost("http://localhost:8090");
			server.save();
		}

	}

}
