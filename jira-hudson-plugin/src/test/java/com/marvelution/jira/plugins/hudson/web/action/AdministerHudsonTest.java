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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.Test;
import org.mockito.internal.verification.VerificationModeFactory;

import com.marvelution.jira.plugins.hudson.service.HudsonServer;

import webwork.action.ActionSupport;


/**
 * TestCase for
 *  
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class AdministerHudsonTest extends AbstractHudsonWebActionSupportTest {

	private AdministerHudson viewHudsonServers;

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected ActionSupport createWebworkAction() {
		viewHudsonServers = new AdministerHudson(permissionManager, serverManager);
		return viewHudsonServers;
	}

	/**
	 * Test the get servers
	 * 
	 * @throws Exception in case of test failures
	 */
	@Test
	public void testGetServers() throws Exception {
		when(serverManager.getServers()).thenReturn(Arrays.asList(new HudsonServer[] {server}));
		assertFalse(viewHudsonServers.getServers().isEmpty());
		assertEquals(1, viewHudsonServers.getServers().size());
		verify(serverManager, VerificationModeFactory.atMost(2)).getServers();
	}

}
