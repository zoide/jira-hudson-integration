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
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.internal.verification.VerificationModeFactory;

import com.marvelution.jira.plugins.hudson.DefaultHudsonServerFactoryImpl;
import com.marvelution.jira.plugins.hudson.api.model.ApiImplementation;
import com.marvelution.jira.plugins.hudson.service.HudsonServer;
import com.marvelution.jira.plugins.hudson.service.HudsonServerFactory;

import webwork.action.ActionSupport;

/**
 * TestCase for {@link AddHudsonServer}
 *  
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class AddHudsonServerTest extends AbstractEditHudsonServerTest {

	private AddHudsonServer addHudsonServer;

	@Mock
	private HudsonServerFactory serverFactory;

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected ActionSupport createWebworkAction() {
		addHudsonServer = new AddHudsonServer(permissionManager, serverManager, new DefaultHudsonServerFactoryImpl(),
			serverAccessor) {

			private static final long serialVersionUID = 1L;

			public String getText(String key) {
				return key;
			}

			public String getText(String key, Object obj) {
				return key;
			}

		};
		return addHudsonServer;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void testDoExecuteWithoutErros() throws Exception {
		assertEquals("none", addHudsonServer.doExecute());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void testDoExecuteWithErros() throws Exception {
		webworkAction.addError("name", "Invalid server name");
		assertEquals("input", addHudsonServer.doExecute());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void testDoValidationWithErrors() throws Exception {
		when(serverAccessor.getApiImplementation(any(HudsonServer.class))).thenReturn(
			ApiImplementation.getApiImplementation());
		when(serverManager.hasServer(eq("Hudson CI"))).thenReturn(true);
		addHudsonServer.setName("Hudson CI");
		addHudsonServer.setHost("http://hudson.marvelution.com");
		addHudsonServer.doValidation();
		assertTrue(webworkAction.getHasErrors());
		assertTrue(webworkAction.getErrors().containsKey("name"));
		assertEquals("hudson.config.serverName.duplicate", webworkAction.getErrors().get("name"));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void testDoValidationWithoutErrors() throws Exception {
		when(serverAccessor.getApiImplementation(any(HudsonServer.class))).thenReturn(
			ApiImplementation.getApiImplementation());
		when(serverManager.hasServer(eq("Hudson CI"))).thenReturn(false);
		addHudsonServer.setName("Hudson CI");
		addHudsonServer.setHost("http://hudson.marvelution.com");
		addHudsonServer.doValidation();
		assertFalse(webworkAction.getHasErrors());
	}

	/**
	 * Test setting hudson server data
	 */
	@Test
	public void testSettingHudsonServerData() {
		when(serverFactory.createHudsonServer()).thenReturn(server);
		final AddHudsonServer action =
			new AddHudsonServer(permissionManager, serverManager, serverFactory, serverAccessor);
		action.setName("Hudson CI");
		action.setHost("http://hudson.marvelution.com");
		action.setDescription("Hudson Server");
		action.setUsername("admin");
		action.setPassword("admin");
		verify(server, VerificationModeFactory.atMost(1)).setName("Hudson CI");
		verify(server, VerificationModeFactory.atMost(1)).setDescription("Hudson Server");
		verify(server, VerificationModeFactory.atMost(1)).setHost("http://hudson.marvelution.com");
		verify(server, VerificationModeFactory.atMost(1)).setUsername("admin");
		verify(server, VerificationModeFactory.atMost(1)).setPassword("admin");
	}

	/**
	 * Test getting hudson server data
	 */
	@Test
	public void testGettingHudsonServerData() {
		when(serverFactory.createHudsonServer()).thenReturn(server);
		when(server.getName()).thenReturn("Hudson CI");
		when(server.getHost()).thenReturn("http://hudson.marvelution.com");
		when(server.getDescription()).thenReturn("Hudson Server");
		when(server.getUsername()).thenReturn("admin");
		when(server.getPassword()).thenReturn("admin");
		final AddHudsonServer action =
			new AddHudsonServer(permissionManager, serverManager, serverFactory, serverAccessor);
		assertEquals("Hudson CI", action.getName());
		assertEquals("http://hudson.marvelution.com", action.getHost());
		assertEquals("Hudson Server", action.getDescription());
		assertEquals("admin", action.getUsername());
		assertEquals("admin", action.getPassword());
		verify(server, VerificationModeFactory.atMost(1)).getName();
		verify(server, VerificationModeFactory.atMost(1)).getDescription();
		verify(server, VerificationModeFactory.atMost(1)).getHost();
		verify(server, VerificationModeFactory.atMost(1)).getUsername();
		verify(server, VerificationModeFactory.atMost(1)).getPassword();
	}

}
