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

package com.marvelution.jira.plugins.hudson.utils;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.user.util.UserUtil;
import com.atlassian.jira.util.I18nHelper;
import com.marvelution.jira.plugins.hudson.api.model.triggers.LegacyCodeTrigger;
import com.marvelution.jira.plugins.hudson.api.model.triggers.ProjectTrigger;
import com.marvelution.jira.plugins.hudson.api.model.triggers.RemoteTrigger;
import com.marvelution.jira.plugins.hudson.api.model.triggers.SCMTrigger;
import com.marvelution.jira.plugins.hudson.api.model.triggers.TimeTrigger;
import com.marvelution.jira.plugins.hudson.api.model.triggers.Trigger;
import com.marvelution.jira.plugins.hudson.api.model.triggers.UserTrigger;
import com.marvelution.jira.plugins.hudson.service.HudsonServer;

/**
 * TestCase for {@link HudsonBuildTriggerParser}
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class HudsonBuildTriggerParserTest {

	@Mock
	private JiraAuthenticationContext authenticationContext;

	@Mock
	private I18nHelper i18n;

	@Mock
	private UserUtil userUtil;

	@Mock
	private HudsonServer server;

	private HudsonBuildTriggerParser triggerParser;

	/**
	 * Test setup
	 * 
	 * @throws Exception in case the setup fails
	 */
	@Before
	public void setUp() throws Exception {
		final Answer<String> answer = new Answer<String>() {

			public String answer(InvocationOnMock invocation) throws Throwable {
				return invocation.getArguments()[0].toString();
			}

		};
		MockitoAnnotations.initMocks(this);
		when(i18n.getText(anyString())).thenAnswer(answer);
		when(i18n.getText(anyString(), anyString())).thenAnswer(answer);
		when(i18n.getText(anyString(), anyString(), anyString())).thenAnswer(answer);
		when(i18n.getText(anyString(), anyString(), anyString(), anyString())).thenAnswer(answer);
		when(i18n.getText(anyString(), anyString(), anyString(), anyString(), anyString())).thenAnswer(answer);
		when(i18n.getText(anyString(), anyString(), anyString(), anyString(), anyString())).thenAnswer(answer);
		when(i18n.getLocale()).thenReturn(Locale.ENGLISH);
		when(authenticationContext.getI18nHelper()).thenReturn(i18n);
		when(server.getHost()).thenReturn("http://localhost:8080");
		triggerParser = new HudsonBuildTriggerParser(authenticationContext, userUtil, server);
	}

	/**
	 * Test formatting a {@link UserTrigger} with a Hudson user
	 */
	@Test
	public void testHudsonUserTrigger() {
		triggerParser.setServer(server);
		final Trigger trigger = new UserTrigger("markrekveld");
		assertEquals("hudson.trigger.hudson.user", triggerParser.parse(trigger));
	}

	/**
	 * Test formatting a {@link ProjectTrigger}
	 */
	@Test
	public void testProjectTrigger() {
		final Trigger trigger = new ProjectTrigger("Marvelution", "job/Marvelution/", 1);
		assertEquals("hudson.trigger.project", triggerParser.parse(trigger));
	}

	/**
	 * Test formatting a {@link LegacyCodeTrigger}
	 */
	@Test
	public void testLegacyCodeTrigger() {
		assertEquals("hudson.trigger.legacy", triggerParser.parse(new LegacyCodeTrigger()));
	}

	/**
	 * Test formatting a {@link RemoteTrigger} with Host and Note information
	 */
	@Test
	public void testRemoteTriggerWithHostAndNote() {
		assertEquals("hudson.trigger.remote.host.note", triggerParser.parse(new RemoteTrigger("localhost", "note")));
	}

	/**
	 * Test formatting a {@link RemoteTrigger} with Host information
	 */
	@Test
	public void testRemoteTriggerWithHost() {
		assertEquals("hudson.trigger.remote.host", triggerParser.parse(new RemoteTrigger("localhost")));
	}

	/**
	 * Test formatting a {@link RemoteTrigger} with no information on the remote host
	 */
	@Test
	public void testRemoteTriggerNoInforation() {
		assertEquals("hudson.trigger.remote.no.information", triggerParser.parse(new RemoteTrigger()));
	}

	/**
	 * Test formatting a {@link SCMTrigger}
	 */
	@Test
	public void testSCMTrigger() {
		assertEquals("hudson.trigger.scm", triggerParser.parse(new SCMTrigger()));
	}

	/**
	 * Test formatting a {@link TimeTrigger}
	 */
	@Test
	public void testTimeTrigger() {
		assertEquals("hudson.trigger.time", triggerParser.parse(new TimeTrigger()));
	}

	/**
	 * Test formatting a Unknown trigger
	 */
	@Test
	public void testUnknown() {
		assertEquals("hudson.trigger.unknown", triggerParser.parse(null));
	}

}
