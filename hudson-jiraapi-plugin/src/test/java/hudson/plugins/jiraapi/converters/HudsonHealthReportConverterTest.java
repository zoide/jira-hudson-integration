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

package hudson.plugins.jiraapi.converters;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.jvnet.localizer.Localizable;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.marvelution.jira.plugins.hudson.api.model.HealthReport;

/**
 * Testcase for {@link HudsonHealthReportConverter}
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class HudsonHealthReportConverterTest {

	@Mock
	private Localizable localizable;

	/**
	 * Setup test variabels
	 * 
	 * @throws Exception in case of errors
	 */
	@Before
	public void before() throws Exception {
		MockitoAnnotations.initMocks(this);
		when(localizable.toString()).thenReturn("Description");
	}

	/**
	 * Test {@link HudsonHealthReportConverter#convertHudsonHealthReport(hudson.model.HealthReport)}
	 * 
	 * @throws Exception in case of errors
	 */
	@Test
	public void testConvertHudsonHealthReport() throws Exception {
		final hudson.model.HealthReport healthReport = new hudson.model.HealthReport(100, localizable);
		final HealthReport report = HudsonHealthReportConverter.convertHudsonHealthReport(healthReport);
		assertThat(report.getScore(), is(100));
		assertThat(report.getIcon(), is("health-80plus.gif"));
		assertThat(report.getDescription(), is("Description"));
	}

}
