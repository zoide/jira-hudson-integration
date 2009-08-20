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
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.tools.generic.SortTool;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.atlassian.jira.security.PermissionManager;
import com.atlassian.jira.security.Permissions;
import com.marvelution.jira.plugins.hudson.service.HudsonServer;
import com.marvelution.jira.plugins.hudson.service.HudsonServerAccessor;
import com.marvelution.jira.plugins.hudson.service.HudsonServerManager;
import com.opensymphony.user.User;

import webwork.action.ActionContext;
import webwork.action.ActionSupport;

/**
 * Abstract test case for the WebActionSupport classes
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public abstract class AbstractHudsonWebActionSupportTest {

	@Mock
	protected HudsonServer server;

	@Mock
	protected HudsonServerManager serverManager;

	@Mock
	protected HudsonServerAccessor serverAccessor;

	@Mock
	protected PermissionManager permissionManager;

	@Mock
	protected HttpServletRequest httpRequest;

	@Mock
	protected HttpServletResponse httpResponse;

	protected ActionSupport webworkAction;

	/**
	 * Setup the tests
	 * 
	 * @throws Exception in case of preparation exceptions
	 */
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		webworkAction = createWebworkAction();
		ActionContext.setRequest(httpRequest);
		ActionContext.setResponse(httpResponse);
	}

	/**
	 * Cleanup after tests
	 * 
	 * @throws Exception in case of cleanup exceptions
	 */
	@After
	public void tearDown() throws Exception {
		ActionContext.setRequest(null);
		ActionContext.setResponse(null);
	}

	/**
	 * Create the {@link ActionSupport} Webwork Action to be tested
	 * 
	 * @return {@link ActionSupport}
	 */
	protected abstract ActionSupport createWebworkAction();

	/**
	 * Test the execution of doDefault() with a user that has permission
	 * 
	 * @throws Exception in case of test exceptions
	 */
	@Test
	public void testDoDefaultWithPermission() throws Exception {
		when(permissionManager.hasPermission(eq(Permissions.ADMINISTER), (User) eq(null))).thenReturn(true);
		assertEquals("input", webworkAction.doDefault());
	}

	/**
	 * Test the execution of doDefault() with a user that has no permission
	 * 
	 * @throws Exception in case of test exceptions
	 */
	@Test
	public void testDoDefaultWithoutPermission() throws Exception {
		when(permissionManager.hasPermission(eq(Permissions.ADMINISTER), (User) eq(null))).thenReturn(false);
		assertEquals("permissionviolation", webworkAction.doDefault());
	}

	/**
	 * Test Soter instance
	 */
	@Test
	public void testGetSorter() {
		assertTrue(((AbstractHudsonWebActionSupport) webworkAction).getSorter() instanceof SortTool);
	}

}
