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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.verification.VerificationModeFactory;

import com.marvelution.jira.plugins.hudson.gadgets.HudsonViewsResource.HudsonViews;
import com.marvelution.jira.plugins.hudson.gadgets.model.GroupResource;
import com.marvelution.jira.plugins.hudson.gadgets.model.OptionResource;
import com.marvelution.jira.plugins.hudson.model.HudsonView;
import com.marvelution.jira.plugins.hudson.service.HudsonServer;
import com.marvelution.jira.plugins.hudson.service.HudsonServerAccessDeniedException;
import com.marvelution.jira.plugins.hudson.service.HudsonServerAccessor;
import com.marvelution.jira.plugins.hudson.service.HudsonServerAccessorException;
import com.marvelution.jira.plugins.hudson.service.HudsonServerManager;

/**
 * Testcase for {@link HudsonViewsResource}
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class HudsonViewsResourceTest {

	@Mock
	private HudsonServerManager serverManager;

	@Mock
	private HudsonServerAccessor serverAccessor;

	@Mock
	private HudsonServer server;

	private HudsonViewsResource viewsResource;

	/**
	 * Setup the test variables
	 * 
	 * @throws Exception in case of errors
	 */
	@Before
	public void before() throws Exception {
		MockitoAnnotations.initMocks(this);
		when(serverManager.getServers()).thenReturn(Collections.singletonList(server));
		viewsResource = new HudsonViewsResource(serverManager, serverAccessor);
	}

	/**
	 * Test {@link HudsonViewsResource#generate()} with no exceptions
	 * 
	 * @throws Exception in case of errors
	 */
	@Test
	public void testGenerate() throws Exception {
		final List<HudsonView> views = new ArrayList<HudsonView>();
		views.add(new HudsonView("All", ""));
		views.add(new HudsonView("Marvelution", ""));
		when(server.getName()).thenReturn("Hudson CI");
		when(server.getServerId()).thenReturn(1);
		when(serverAccessor.getViewsList(server)).thenReturn(views);
		final Response response = viewsResource.generate();
		assertThat(response.getEntity(), is(HudsonViews.class));
		final HudsonViews hudsonViews = (HudsonViews) response.getEntity();
		assertThat(hudsonViews.getViews().isEmpty(), is(false));
		assertThat(hudsonViews.getViews().size(), is(1));
		final GroupResource group = ((List<GroupResource>) hudsonViews.getViews()).get(0);
		assertThat(group.getLabel(), is("Hudson CI"));
		assertThat(group.getOptions().isEmpty(), is(false));
		assertThat(group.getOptions().size(), is(2));
		OptionResource option = ((List<OptionResource>) group.getOptions()).get(0);
		assertThat(option.getLabel(), is("All"));
		assertThat(option.getValue(), is("1"));
		option = ((List<OptionResource>) group.getOptions()).get(1);
		assertThat(option.getLabel(), is("Marvelution"));
		assertThat(option.getValue(), is("1"));
		verify(server, VerificationModeFactory.times(1)).getName();
		verify(server, VerificationModeFactory.times(2)).getServerId();
		verify(serverManager, VerificationModeFactory.times(1)).getServers();
		verify(serverAccessor, VerificationModeFactory.times(1)).getViewsList(server);
	}

	/**
	 * Test {@link HudsonViewsResource#generate()} with {@link HudsonServerAccessorException}
	 * 
	 * @throws Exception in case of errors
	 */
	@Test
	public void testGenerateServerAccessorException() throws Exception {
		when(server.getName()).thenReturn("Hudson CI");
		when(server.getServerId()).thenReturn(1);
		when(serverAccessor.getViewsList(server)).thenThrow(new HudsonServerAccessorException("Failure"));
		final Response response = viewsResource.generate();
		assertThat(response.getEntity(), is(HudsonViews.class));
		assertThat(((HudsonViews) response.getEntity()).getViews().isEmpty(), is(true));
		verify(server, VerificationModeFactory.times(1)).getName();
		verify(server, VerificationModeFactory.times(0)).getServerId();
		verify(serverManager, VerificationModeFactory.times(1)).getServers();
		verify(serverAccessor, VerificationModeFactory.times(1)).getViewsList(server);
	}

	/**
	 * Test {@link HudsonViewsResource#generate()} with {@link HudsonServerAccessDeniedException}
	 * 
	 * @throws Exception in case of errors
	 */
	@Test
	public void testGenerateServerAccessDeniedException() throws Exception {
		when(server.getName()).thenReturn("Hudson CI");
		when(server.getServerId()).thenReturn(1);
		when(serverAccessor.getViewsList(server)).thenThrow(new HudsonServerAccessDeniedException("Failure"));
		final Response response = viewsResource.generate();
		assertThat(response.getEntity(), is(HudsonViews.class));
		assertThat(((HudsonViews) response.getEntity()).getViews().isEmpty(), is(true));
		verify(server, VerificationModeFactory.times(1)).getName();
		verify(server, VerificationModeFactory.times(0)).getServerId();
		verify(serverManager, VerificationModeFactory.times(1)).getServers();
		verify(serverAccessor, VerificationModeFactory.times(1)).getViewsList(server);
	}

}
