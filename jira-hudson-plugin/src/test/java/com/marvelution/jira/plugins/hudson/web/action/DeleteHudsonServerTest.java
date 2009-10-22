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
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.internal.verification.VerificationModeFactory;

import webwork.action.ActionSupport;


/**
 * TestCase for
 *  
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class DeleteHudsonServerTest extends AbstractHudsonWebActionSupportTest {

	private DeleteHudsonServer deleteHudsonServer;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setUp() throws Exception {
		super.setUp();
		when(serverManager.getServer(eq(1))).thenReturn(server);
		when(server.getServerId()).thenReturn(1);
		when(server.getName()).thenReturn("Hudson CI");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected ActionSupport createWebworkAction() {
		deleteHudsonServer = new DeleteHudsonServer(permissionManager, serverManager) {

			private static final long serialVersionUID = 1L;

			public String getText(String key) {
				return key;
			}

		};
		return deleteHudsonServer;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void testDoDefaultWithoutPermission() throws Exception {
		deleteHudsonServer.setHudsonServerId(1);
		super.testDoDefaultWithoutPermission();
		assertEquals("Hudson CI", deleteHudsonServer.getName());
		verify(serverManager, VerificationModeFactory.atMost(1)).getServer(1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void testDoDefaultWithPermission() throws Exception {
		deleteHudsonServer.setHudsonServerId(1);
		super.testDoDefaultWithPermission();
		assertEquals("Hudson CI", deleteHudsonServer.getName());
		verify(serverManager, VerificationModeFactory.atMost(1)).getServer(1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Test
	public void testDoExecute() throws Exception {
		deleteHudsonServer.setHudsonServerId(1);
		assertEquals("none", deleteHudsonServer.doExecute());
		verify(serverManager, VerificationModeFactory.atMost(1)).remove(1);
	}

}
