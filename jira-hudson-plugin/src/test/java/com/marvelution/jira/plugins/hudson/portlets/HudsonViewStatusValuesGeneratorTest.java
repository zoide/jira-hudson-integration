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

package com.marvelution.jira.plugins.hudson.portlets;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.hamcrest.CoreMatchers.*;

import java.util.Collections;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.verification.VerificationModeFactory;

import com.marvelution.jira.plugins.hudson.model.HudsonView;
import com.marvelution.jira.plugins.hudson.service.HudsonServer;
import com.marvelution.jira.plugins.hudson.service.HudsonServerAccessDeniedException;
import com.marvelution.jira.plugins.hudson.service.HudsonServerAccessor;
import com.marvelution.jira.plugins.hudson.service.HudsonServerAccessorException;
import com.marvelution.jira.plugins.hudson.service.HudsonServerManager;

/**
 * TestCase for {@link HudsonViewStatusValuesGenerator}
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class HudsonViewStatusValuesGeneratorTest {

	@Mock
	private HudsonServerManager serverManager;

	@Mock
	private HudsonServerAccessor serverAccessor;

	@Mock
	private HudsonServer server;

	@Mock
	private HudsonView view;

	private TestHudsonViewStatusValuesGenerator generator = new TestHudsonViewStatusValuesGenerator();

	/**
	 * Setup test variables
	 * 
	 * @throws Exception in case of failures
	 */
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		when(serverManager.getServers()).thenReturn(Collections.singletonList(server));
		when(server.getName()).thenReturn("Hudson Server");
		when(server.getServerId()).thenReturn(1);
		when(view.getName()).thenReturn("All");
	}

	/**
	 * Test getValues from ValuesGenerator
	 * 
	 * @throws Exception in case of exceptions that fail the test
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testGetValues() throws Exception {
		when(serverAccessor.getViewsList(server)).thenReturn(Collections.singletonList(view));
		final Map values = generator.getValues(Collections.EMPTY_MAP);
		assertFalse(values.isEmpty());
		assertThat(values.size(), is(1));
		assertTrue(values.containsKey("1;view:All"));
		assertThat((String) values.get("1;view:All"), is("Hudson Server - All"));
	}

	/**
	 * Test Accessor Access Denied exception
	 * 
	 * @throws Exception in case of exceptions that fail the test
	 */
	@Test
	public void testAccessorAccesDeniedException() throws Exception {
		when(serverAccessor.getViewsList(server)).thenThrow(new HudsonServerAccessDeniedException("Access Denied"));
		assertTrue(generator.getValues(Collections.EMPTY_MAP).isEmpty());
		verify(serverManager, VerificationModeFactory.times(1)).getServers();
		verify(serverAccessor, VerificationModeFactory.times(1)).getViewsList(server);
		verify(server, VerificationModeFactory.times(1)).getName();
	}

	/**
	 * Test Accessor Access Denied exception
	 * 
	 * @throws Exception in case of exceptions that fail the test
	 */
	@Test
	public void testAccessorException() throws Exception {
		when(serverAccessor.getViewsList(server)).thenThrow(new HudsonServerAccessorException("Access Denied"));
		assertTrue(generator.getValues(Collections.EMPTY_MAP).isEmpty());
		verify(serverManager, VerificationModeFactory.times(1)).getServers();
		verify(serverAccessor, VerificationModeFactory.times(1)).getViewsList(server);
		verify(server, VerificationModeFactory.times(1)).getName();
	}

	/**
	 * Testcase implementation of the {@link HudsonViewStatusValuesGenerator}
	 * 
	 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
	 */
	public class TestHudsonViewStatusValuesGenerator extends HudsonViewStatusValuesGenerator {

		/**
		 * {@inheritDoc}
		 */
		@Override
		HudsonServerManager getServerManager() {
			return serverManager;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		HudsonServerAccessor getServerAccessor() {
			return serverAccessor;
		}

	}

}
