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

package com.marvelution.jira.plugins.hudson.gadgets;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import java.util.Collections;
import java.util.List;

import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.verification.VerificationModeFactory;

import com.marvelution.jira.plugins.hudson.gadgets.HudsonServersResource.HudsonServers;
import com.marvelution.jira.plugins.hudson.gadgets.model.OptionResource;
import com.marvelution.jira.plugins.hudson.service.HudsonServer;
import com.marvelution.jira.plugins.hudson.service.HudsonServerManager;

/**
 * Testcase for {@link HudsonServersResource}
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class HudsonServersResourceTest {

	@Mock
	private HudsonServerManager serverManager;

	@Mock
	private HudsonServer server;

	private HudsonServersResource serversResource;

	/**
	 * Setup the test variables
	 * 
	 * @throws Exception in case of setup errors
	 */
	@Before
	public void before() throws Exception {
		MockitoAnnotations.initMocks(this);
		serversResource = new HudsonServersResource(serverManager);
		when(serverManager.getServers()).thenReturn(Collections.singletonList(server));
	}

	/**
	 * Test {@link HudsonServersResource#generate()}
	 * 
	 * @throws Exception in case of errors
	 */
	@Test
	public void testGenerate() throws Exception {
		when(server.getName()).thenReturn("Hudson CI");
		when(server.getServerId()).thenReturn(1);
		final Response response = serversResource.generate();
		assertThat(response.getEntity(), is(HudsonServers.class));
		final HudsonServers servers = (HudsonServers) response.getEntity();
		assertThat(servers.getServers().isEmpty(), is(false));
		assertThat(servers.getServers().size(), is(1));
		final OptionResource option = ((List<OptionResource>) servers.getServers()).get(0);
		assertThat(option.getLabel(), is("Hudson CI"));
		assertThat(option.getValue(), is("1"));
		verify(server, VerificationModeFactory.times(1)).getName();
		verify(server, VerificationModeFactory.times(1)).getServerId();
		verify(serverManager, VerificationModeFactory.times(1)).getServers();
	}

}
