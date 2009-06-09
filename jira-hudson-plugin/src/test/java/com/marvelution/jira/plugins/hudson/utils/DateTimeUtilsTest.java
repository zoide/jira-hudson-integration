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
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.when;

import java.io.InputStream;
import java.util.Calendar;
import java.util.Locale;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.atlassian.core.util.DateUtils;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.web.bean.I18nBean;

/**
 * TestCase for {@link DateTimeUtils}
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class DateTimeUtilsTest {

	@Mock
	private JiraAuthenticationContext authenticationContext;

	@Mock
	private I18nBean i18n;

	private DateTimeUtils utils;

	private Properties i18nProps;

	/**
	 * Setup
	 * 
	 * @throws Exception in case the test cannot be prepared
	 */
	@Before
	public void setUp() throws Exception {
		final Answer<String> answer = new Answer<String>() {

			public String answer(InvocationOnMock invocation) throws Throwable {
				final String value = i18nProps.getProperty(invocation.getArguments()[0].toString());
				return value.replaceAll("\\{0\\}", invocation.getArguments()[1].toString());
			}

		};
		MockitoAnnotations.initMocks(this);
		InputStream input = null;
		try {
			input = Thread.currentThread().getContextClassLoader().getResourceAsStream(
					"com/marvelution/jira/plugins/hudson/hudson-datetime.properties");
			i18nProps = new Properties();
			i18nProps.load(input);
		} finally {
			IOUtils.closeQuietly(input);
		}
		when(i18n.getText(anyString(), anyObject())).thenAnswer(answer);
		when(i18n.getText(anyString(), anyString())).thenAnswer(answer);
		when(i18n.getLocale()).thenReturn(Locale.ENGLISH);
		when(authenticationContext.getI18nBean(anyString())).thenReturn(i18n);
		utils = new DateTimeUtils(authenticationContext);
	}

	/**
	 * Test get formatted duration time string
	 */
	@Test
	public void testGetTimeSpanString() {
		assertEquals("1 hr 1 min", utils.getTimeSpanString(DateUtils.HOUR_MILLIS + DateUtils.MINUTE_MILLIS));
		assertEquals("1 min 1 sec", utils.getTimeSpanString(DateUtils.MINUTE_MILLIS + DateUtils.SECOND_MILLIS));
		assertEquals("10 sec", utils.getTimeSpanString(DateUtils.SECOND_MILLIS * 10));
		assertEquals("2.0 sec", utils.getTimeSpanString(DateUtils.SECOND_MILLIS * 2));
		assertEquals("0.1 sec", utils.getTimeSpanString(100L));
		assertEquals("10 ms", utils.getTimeSpanString(10L));
	}

	/**
	 * Test get formatted past time string
	 */
	@Test
	public void testGetPastTimeString() {
		long time = Calendar.getInstance(Locale.ENGLISH).getTimeInMillis();
		assertEquals("1 yr 1 mo ago", utils.getPastTimeString(time - DateUtils.YEAR_MILLIS - DateUtils.MONTH_MILLIS));
		time = Calendar.getInstance(Locale.ENGLISH).getTimeInMillis();
		assertEquals("1 mo 1 day ago", utils.getPastTimeString(time - DateUtils.MONTH_MILLIS - DateUtils.DAY_MILLIS));
		time = Calendar.getInstance(Locale.ENGLISH).getTimeInMillis();
		assertEquals("1 mo 2 days ago", utils.getPastTimeString(time - DateUtils.MONTH_MILLIS
			- (DateUtils.DAY_MILLIS * 2)));
		time = Calendar.getInstance(Locale.ENGLISH).getTimeInMillis();
		assertEquals("1 day 1 hr ago", utils.getPastTimeString(time - DateUtils.DAY_MILLIS - DateUtils.HOUR_MILLIS));
		time = Calendar.getInstance(Locale.ENGLISH).getTimeInMillis();
		assertEquals("2 days 0 hr ago", utils.getPastTimeString(time - (DateUtils.DAY_MILLIS * 2)));
	}

	/**
	 * Test get time passed since given time
	 */
	@Test
	public void testGetTimePastSince() {
		final long time = Calendar.getInstance(Locale.ENGLISH).getTimeInMillis();
		assertTrue(utils.getTimePastSince(time - DateUtils.SECOND_MILLIS) >= DateUtils.SECOND_MILLIS);
	}

}
