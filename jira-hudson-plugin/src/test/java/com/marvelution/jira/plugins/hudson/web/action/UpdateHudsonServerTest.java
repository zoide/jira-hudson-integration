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

import java.util.Arrays;

import org.junit.Test;
import org.mockito.internal.verification.VerificationModeFactory;

import com.atlassian.jira.security.Permissions;
import com.marvelution.jira.plugins.hudson.DefaultHudsonServerFactoryImpl;
import com.marvelution.jira.plugins.hudson.JiraApi;
import com.marvelution.jira.plugins.hudson.service.HudsonServer;
import com.opensymphony.user.User;

import webwork.action.ActionSupport;


/**
 * TestCase for
 *  
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class UpdateHudsonServerTest extends AbstractEditHudsonServerTest {

	private UpdateHudsonServer updateHudsonServer;

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected ActionSupport createWebworkAction() {
		updateHudsonServer =
			new UpdateHudsonServer(permissionManager, serverManager, new DefaultHudsonServerFactoryImpl(),
				serverAccessor) {

			private static final long serialVersionUID = 1L;

			public String getText(String key) {
				return key;
			}

			public String getText(String key, Object obj) {
				return key;
			}

		};
		return updateHudsonServer;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void testDoDefaultWithoutPermission() throws Exception {
		updateHudsonServer.setHudsonServerId(1);
		when(serverManager.getServer(eq(1))).thenReturn(server);
		super.testDoDefaultWithoutPermission();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void testDoDefaultWithPermission() throws Exception {
		updateHudsonServer.setHudsonServerId(1);
		when(serverManager.getServer(eq(1))).thenReturn(server);
		super.testDoDefaultWithPermission();
	}

	/**
	 * {@inheritDoc}
	 */
	@Test
	public void testDoDefaultNoServerId() throws Exception {
		when(permissionManager.hasPermission(eq(Permissions.ADMINISTER), (User) eq(null))).thenReturn(true);
		assertEquals("none", webworkAction.doDefault());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void testDoExecuteWithoutErros() throws Exception {
		assertEquals("none", updateHudsonServer.doExecute());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void testDoExecuteWithErros() throws Exception {
		webworkAction.addError("name", "Invalid server name");
		assertEquals("input", updateHudsonServer.doExecute());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void testDoValidationWithErrors() throws Exception {
		updateHudsonServer.setName("Hudson CI");
		updateHudsonServer.setHost("http://hudson.marvelution.com");
		updateHudsonServer.setId(2);
		when(serverAccessor.getApiImplementation(any(HudsonServer.class))).thenReturn(JiraApi.getApiImplementation());
		when(serverManager.getServer(eq("Hudson CI"))).thenReturn(server);
		when(server.getServerId()).thenReturn(1);
		updateHudsonServer.doValidation();
		assertTrue(webworkAction.getHasErrors());
		assertTrue(webworkAction.getErrors().containsKey("name"));
		assertEquals("hudson.config.serverName.duplicate", webworkAction.getErrors().get("name"));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void testDoValidationWithoutErrors() throws Exception {
		updateHudsonServer.setName("Hudson CI");
		updateHudsonServer.setHost("http://hudson.marvelution.com");
		updateHudsonServer.setId(1);
		when(serverAccessor.getApiImplementation(any(HudsonServer.class))).thenReturn(JiraApi.getApiImplementation());
		when(serverManager.getServer(eq("Hudson CI"))).thenReturn(server);
		when(server.getServerId()).thenReturn(1);
		updateHudsonServer.doValidation();
		assertFalse(webworkAction.getHasErrors());
	}

	/**
	 * {@inheritDoc}
	 */
	@Test
	public void testDoSetAsDefault() throws Exception {
		updateHudsonServer.setHudsonServerId(1);
		when(serverManager.getServer(eq(1))).thenReturn(server);
		assertEquals("none", updateHudsonServer.doSetAsDefault());
		verify(serverManager, VerificationModeFactory.atMost(1)).setDefaultServer(server);
	}

	/**
	 * {@inheritDoc}
	 */
	@Test
	public void testDoSetAsDefaultNoServerId() throws Exception {
		updateHudsonServer.doSetAsDefault();
		assertEquals("none", updateHudsonServer.doSetAsDefault());
		verify(serverManager, VerificationModeFactory.times(0)).setDefaultServer(server);
	}

	/**
	 * Test get associated project keys
	 */
	@Test
	public void testGetAssociatedProjectKeys() {
		updateHudsonServer.setAssociatedProjectKeys("MARVADMIN,MARVUTIL");
		assertEquals("MARVUTIL,MARVADMIN", updateHudsonServer.getAssociatedProjectKeys());
	}

	/**
	 * Test set associated project keys
	 */
	@Test
	public void testSetAssociatedProjectKeys() {
		updateHudsonServer.setAssociatedProjectKeys("MARVADMIN,MARVUTIL");
		verify(server, VerificationModeFactory.atMost(1)).setAssociatedProjectKeys(
			Arrays.asList(new String[] {"MARVADMIN", "MARVUTIL"}));
	}

}
