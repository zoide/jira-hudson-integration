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

import org.junit.Before;
import org.junit.Test;

import com.marvelution.jira.plugins.hudson.service.HudsonServer;
import com.marvelution.jira.plugins.hudson.service.HudsonServerFactory;

/**
 * TestCase for the {@link DefaultHudsonServerFactoryImpl}
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class DefaultHudsonServerFactoryImplTest {

	private HudsonServerFactory defaultFactory;

	/**
	 * Setup the tests variables
	 * 
	 * @throws Exception in case of setup failures
	 */
	@Before
	public void setUp() throws Exception {
		defaultFactory = new DefaultHudsonServerFactoryImpl();
	}

	/**
	 * Test creating a new {@link HudsonServer} from another one
	 */
	@Test
	public void testCreateHudsonServer() {
		final HudsonServer server = defaultFactory.createHudsonServer();
		assertNotNull(server);
		assertEquals(0, server.getServerId());
		assertEquals("", server.getName());
		assertEquals("", server.getDescription());
		assertEquals("", server.getHost());
		assertEquals("", server.getUsername());
		assertEquals("", server.getPassword());
		assertNotNull(server.getAssociatedProjectKeys());
		assertTrue(server.getAssociatedProjectKeys().isEmpty());
		assertEquals("/images/32x32/", server.getLargeImageUrl());
		assertEquals("/images/24x24/", server.getMediumImageUrl());
		assertEquals("/images/16x16/", server.getSmallImageUrl());
		assertFalse(server.isSecuredHudsonServer());
	}

	/**
	 * Test creating a new {@link HudsonServer}
	 */
	@Test
	public void testCreateHudsonServerCopy() {
		final HudsonServer server = defaultFactory.createHudsonServer();
		server.setServerId(1);
		server.setName("Hudson CI");
		server.setDescription("Hudson CI Server");
		server.setHost("http://hudson.marvelution.com");
		server.setUsername("admin");
		server.setPassword("admin");
		final HudsonServer copy = defaultFactory.createHudsonServer(server);
		assertNotNull(copy);
		assertEquals(1, copy.getServerId());
		assertEquals("Hudson CI", copy.getName());
		assertEquals("Hudson CI Server", copy.getDescription());
		assertEquals("http://hudson.marvelution.com", copy.getHost());
		assertEquals("admin", copy.getUsername());
		assertEquals("admin", copy.getPassword());
		assertNotNull(copy.getAssociatedProjectKeys());
		assertTrue(copy.getAssociatedProjectKeys().isEmpty());
		assertEquals("http://hudson.marvelution.com/images/32x32/", copy.getLargeImageUrl());
		assertEquals("http://hudson.marvelution.com/images/24x24/", copy.getMediumImageUrl());
		assertEquals("http://hudson.marvelution.com/images/16x16/", copy.getSmallImageUrl());
		assertTrue(copy.isSecuredHudsonServer());
	}

}
