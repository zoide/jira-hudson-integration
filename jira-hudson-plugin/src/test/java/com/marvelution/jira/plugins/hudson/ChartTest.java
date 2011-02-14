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

package com.marvelution.jira.plugins.hudson;

import static org.mockito.Mockito.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.atlassian.jira.charts.jfreechart.ChartHelper;
import com.atlassian.jira.util.I18nHelper;
import com.marvelution.hudson.plugins.apiv2.client.Host;
import com.marvelution.hudson.plugins.apiv2.client.HudsonClient;
import com.marvelution.hudson.plugins.apiv2.client.connectors.HttpClient3Connector;
import com.marvelution.hudson.plugins.apiv2.client.services.JobQuery;
import com.marvelution.hudson.plugins.apiv2.resources.model.build.Build;
import com.marvelution.hudson.plugins.apiv2.resources.model.build.Result;
import com.marvelution.hudson.plugins.apiv2.resources.model.build.TestResult;
import com.marvelution.hudson.plugins.apiv2.resources.model.job.Job;
import com.marvelution.jira.plugins.hudson.charts.BuildResultsRatioChartGenerator;
import com.marvelution.jira.plugins.hudson.charts.BuildTestResultsRatioChartGenerator;
import com.marvelution.jira.plugins.hudson.services.servers.HudsonServer;
import com.marvelution.jira.plugins.hudson.services.servers.impl.DefaultHudsonServer;

/**
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class ChartTest {

	private HudsonServer server;
	private HudsonClient client;
	private Job job;
	@Mock
	private I18nHelper i18nHelper;

	@Before
	public void before() {
//		MockitoAnnotations.initMocks(this);
//		server = new DefaultHudsonServer("Test", "http://localhost:8090");
//		client = new HudsonClient(new HttpClient3Connector(new Host(server.getHost())));
//		job = client.find(JobQuery.createForJobByName("Maven 2"));
//		when(i18nHelper.getText(anyString())).thenAnswer(new Answer<String>() {
//
//			@Override
//			public String answer(InvocationOnMock invocation) throws Throwable {
//				return String.valueOf(invocation.getArguments()[0]);
//			}
//		});
//		when(i18nHelper.getText(eq("hudson.charts.tests.tooltip"), anyString(), anyString())).thenAnswer(new Answer<String>() {
//
//			@Override
//			public String answer(InvocationOnMock invocation) throws Throwable {
//				return "#" + invocation.getArguments()[2] + " " + invocation.getArguments()[1];
//			}
//		});
	}

	@Test
	public void testBuildResultsRatioChart() throws IOException {
//		for (Build build : job.getBuilds()) {
//			switch (build.getBuildNumber()) {
//			case 1:
//				build.setResult(Result.SUCCESSFUL);
//				break;
//			case 2:
//				build.setResult(Result.FAILED);
//				break;
//			case 3:
//				build.setResult(Result.UNSTABLE);
//				break;
//			case 4:
//				build.setResult(Result.ABORTED);
//				break;
//			}
//			build.setDuration(build.getDuration() * 10);
//		}
//		ChartHelper chart = new BuildResultsRatioChartGenerator(server, job, i18nHelper).generateChart();
//		chart.generate(400, 400);
//		System.out.println(chart.getImageMap());
//		System.out.println(System.getProperty("java.io.tmpdir") + chart.getLocation());
	}

	@Test
	public void testBuildTestResultsRatioChart() throws IOException {
//		for (Build build : job.getBuilds()) {
//			build.setTestResult(new TestResult());
//			build.getTestResult().setTotal(50);
//			build.getTestResult().setFailed((build.getBuildNumber() - 1) * 4);
//			build.getTestResult().setSkipped((build.getBuildNumber() - 1) * 2);
//		}
//		ChartHelper chart = new BuildTestResultsRatioChartGenerator(server, job, i18nHelper).generateChart();
//		chart.generate(400, 400);
//		System.out.println(chart.getImageMap());
//		System.out.println(System.getProperty("java.io.tmpdir") + chart.getLocation());
	}

}
