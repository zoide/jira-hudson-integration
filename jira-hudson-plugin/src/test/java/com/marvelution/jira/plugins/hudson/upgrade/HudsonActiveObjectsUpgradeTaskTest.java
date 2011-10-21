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

package com.marvelution.jira.plugins.hudson.upgrade;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Map.Entry;
import java.util.Properties;

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
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.atlassian.activeobjects.ao.ActiveObjectsFieldNameConverter;
import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.activeobjects.test.TestActiveObjects;
import com.atlassian.jira.bc.whitelist.WhitelistManager;
import com.atlassian.jira.portal.PortletConfiguration;
import com.atlassian.jira.portal.PortletConfigurationStore;
import com.atlassian.jira.util.collect.CollectionEnclosedIterable;
import com.atlassian.jira.util.collect.EnclosedIterable;
import com.atlassian.sal.api.upgrade.PluginUpgradeTask;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.marvelution.jira.plugins.hudson.services.associations.HudsonAssociation;
import com.marvelution.jira.plugins.hudson.services.associations.HudsonAssociationManager;
import com.marvelution.jira.plugins.hudson.services.associations.HudsonAssociationManagerService;
import com.marvelution.jira.plugins.hudson.services.servers.HudsonServer;
import com.marvelution.jira.plugins.hudson.services.servers.HudsonServerManager;
import com.marvelution.jira.plugins.hudson.services.servers.HudsonServerManagerService;
import com.marvelution.jira.plugins.hudson.services.servers.HudsonServerUtils;
import com.marvelution.jira.plugins.hudson.utils.JiraPluginUtils;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.module.propertyset.PropertySetManager;

/**
 * Test class for the {@link HudsonActiveObjectsUpgradeTask} implementation
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
@RunWith(ActiveObjectsJUnitRunner.class)
@Data(HudsonActiveObjectsUpgradeTaskTest.HudsonActiveObjectsUpgradeTaskTestDatabaseUpdater.class)
@NameConverters(field = ActiveObjectsFieldNameConverter.class)
public class HudsonActiveObjectsUpgradeTaskTest {

	@Mock private PortletConfigurationStore portletConfigurationStore;
	@Mock private WhitelistManager whitelistManager;

	private EntityManager entityManager;
	private ActiveObjects objects;
	private HudsonServerManager serverManager;
	private HudsonAssociationManager associationManager;
	private PluginUpgradeTask upgradeTask;

	/**
	 * Set-Up the tests
	 * 
	 * @throws Exception in case of errors
	 */
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		when(portletConfigurationStore.getAllPortletConfigurations()).thenAnswer(
			new Answer<EnclosedIterable<PortletConfiguration>>() {

				/**
				 * {@inheritDoc}
				 */
				@Override
				public EnclosedIterable<PortletConfiguration> answer(InvocationOnMock invocation) throws Throwable {
					Collection<PortletConfiguration> collection = Lists.newArrayList();
					return CollectionEnclosedIterable.from(collection);
				}
			
		});
		assertNotNull(entityManager);
		objects = new TestActiveObjects(entityManager);
		serverManager = new HudsonServerManagerService(objects, whitelistManager);
		associationManager = new HudsonAssociationManagerService(objects, serverManager);
		upgradeTask = new HudsonActiveObjectsUpgradeTask(serverManager, associationManager) {

			/**
			 * {@inheritDoc}
			 */
			@Override
			protected PropertySet loadPropertySet() {
				PropertySet propertySet = PropertySetManager.getInstance("memory", Maps.newHashMap());
				Properties properties = new Properties();
				InputStream input = getClass().getResourceAsStream("HudsonActiveObjectsUpgradeTaskTest.properties");
				try {
					properties.load(input);
				} catch (IOException e) {
					throw new RuntimeException("Failed to load Test Data", e);
				}
				for (Entry<Object, Object> entry : properties.entrySet()) {
					String key = (String) entry.getKey();
					String value = (String) entry.getValue();
					if (key.toLowerCase().endsWith(".pid")) {
						propertySet.setLong(key, Long.parseLong(value));
					} else if (key.toLowerCase().endsWith("id")) {
						propertySet.setInt(key, Integer.parseInt(value));
					} else {
						propertySet.setString(key, value);
					}
				}
				return propertySet;
			}

			/**
			 * {@inheritDoc}
			 */
			@Override
			protected PortletConfigurationStore getPortletConfigurationStore() {
				return portletConfigurationStore;
			}

		};
	}

	/**
	 * Test {@link HudsonActiveObjectsUpgradeTask#getBuildNumber()}
	 */
	@Test
	public void testGetBuildNumber() {
		assertEquals(6, upgradeTask.getBuildNumber());
	}

	/**
	 * Test {@link HudsonActiveObjectsUpgradeTask#getShortDescription()}
	 */
	@Test
	public void testGetShortDescription() {
		assertEquals("Upgrade the PropertySet storage to the ActiveObjects database",
			upgradeTask.getShortDescription());
	}

	/**
	 * Test {@link HudsonActiveObjectsUpgradeTask#getPluginKey()}
	 */
	@Test
	public void testGetPluginKey() {
		assertEquals(JiraPluginUtils.getPluginKey(), upgradeTask.getPluginKey());
	}

	/**
	 * Test {@link HudsonActiveObjectsUpgradeTask#doUpgrade()}
	 * 
	 * @throws Exception in case of errors
	 */
	@Test
	public void testDoUpgrade() throws Exception {
		assertEquals(0, objects.count(HudsonServer.class));
		assertEquals(0, objects.count(HudsonAssociation.class));
		assertEquals(0, serverManager.getServers().size());
		assertEquals(0, associationManager.getAssociations().size());
		assertEquals(null, upgradeTask.doUpgrade());
		assertEquals(3, objects.count(HudsonServer.class));
		assertEquals(3, serverManager.getServers().size());
		HudsonServer server = serverManager.getServer(1);
		assertEquals("Hudson CI", server.getName());
		assertEquals("Hudson Instance", server.getDescription());
		assertEquals("http://localhost:8090/hudson", server.getHost());
		assertEquals("http://localhost:8090/hudson", server.getPublicHost());
		assertEquals(false, server.isDefaultServer());
		assertEquals(false, HudsonServerUtils.isServerSecured(server));
		assertEquals(true, server.isIncludeInStreams());
		server = serverManager.getServer(2);
		assertEquals("Jenkins CI", server.getName());
		assertEquals("Jenkins Instance", server.getDescription());
		assertEquals("http://localhost:8090/jenkins", server.getHost());
		assertEquals("http://localhost:8090", server.getPublicHost());
		assertEquals(true, server.isDefaultServer());
		assertEquals(2, serverManager.getDefaultServer().getID());
		assertEquals(false, HudsonServerUtils.isServerSecured(server));
		assertEquals(true, server.isIncludeInStreams());
		server = serverManager.getServer(3);
		assertEquals("Building", server.getName());
		assertEquals("Jenkins Instance", server.getDescription());
		assertEquals("http://localhost:8090", server.getHost());
		assertEquals("http://localhost:8090", server.getPublicHost());
		assertEquals(false, server.isDefaultServer());
		assertEquals(true, HudsonServerUtils.isServerSecured(server));
		assertEquals(true, server.isIncludeInStreams());
		assertEquals("admin", server.getUsername());
		assertEquals("admin", server.getPassword());
		assertEquals(2, objects.count(HudsonAssociation.class));
		assertEquals(2, associationManager.getAssociations().size());
		HudsonAssociation association = associationManager.getAssociation(1);
		assertEquals(1, association.getServer().getID());
		assertEquals(3L, association.getProjectId());
		assertEquals("JIRA Hudson Integration", association.getJobName());
		association = associationManager.getAssociation(2);
		assertEquals(2, association.getServer().getID());
		assertEquals(4L, association.getProjectId());
		assertEquals("JIRA Hudson Plugin", association.getJobName());
	}

	/**
	 * {@link DatabaseUpdater} implementation
	 * 
	 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
	 */
	public static class HudsonActiveObjectsUpgradeTaskTestDatabaseUpdater implements DatabaseUpdater {

		/**
		 * {@inheritDoc}
		 */
		@SuppressWarnings("unchecked")
		@Override
		public void update(EntityManager entityManager) throws Exception {
			entityManager.migrate(HudsonServer.class, HudsonAssociation.class);
		}

	}

}
