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

package com.marvelution.jira.plugins.hudson.xstream;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import com.marvelution.jira.plugins.hudson.model.Build;
import com.marvelution.jira.plugins.hudson.model.BuildsList;
import com.marvelution.jira.plugins.hudson.model.ApiImplementation;
import com.marvelution.jira.plugins.hudson.model.HealthReport;
import com.marvelution.jira.plugins.hudson.model.Job;
import com.marvelution.jira.plugins.hudson.model.JobsList;
import com.marvelution.jira.plugins.hudson.model.Result;
import com.marvelution.jira.plugins.hudson.model.State;
import com.marvelution.jira.plugins.hudson.model.TestResult;
import com.marvelution.jira.plugins.hudson.model.triggers.SCMTrigger;
import com.marvelution.jira.plugins.hudson.model.triggers.Trigger;
import com.marvelution.jira.plugins.hudson.model.triggers.UserTrigger;

/**
 * TestCase for {@link XStreamMarshaller}
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class XStreamMarshallerTest {

	/**
	 * Test marshaling a <code>null</code>
	 * 
	 * @throws Exception in case of Exceptions that always fail the test
	 */
	@Test
	public void testMarshalNull() throws Exception {
		assertEquals("", XStreamMarshaller.marshal(null));
	}

	/**
	 * Test unmarshaling a empty string
	 * 
	 * @throws Exception in case of Exceptions that always fail the test
	 */
	@Test
	public void testUnmarshalEmptyString() throws Exception {
		assertNull(XStreamMarshaller.unmarshal("", ApiImplementation.class));
	}

	/**
	 * Test marshaling a {@link ApiImplementation} object into XML
	 * 
	 * @throws Exception in case of Exceptions that always fail the test
	 */
	@Test
	public void testMarshalJiraApi() throws Exception {
		final ApiImplementation api = new ApiImplementation();
		api.setVersion("1.0");
		assertEquals(getXML("JiraApi.xml"), XStreamMarshaller.marshal(api));
	}

	/**
	 * Test unmarshaling a XML into a {@link ApiImplementation} object
	 * 
	 * @throws Exception in case of Exceptions that always fail the test
	 */
	@Test
	public void testUnmarshalJiraApi() throws Exception {
		final String xml = getXML("JiraApi.xml");
		final ApiImplementation api = XStreamMarshaller.unmarshal(xml, ApiImplementation.class);
		assertEquals("1.0", api.getVersion());
	}

	/**
	 * Test marshaling a {@link Job} object into XML
	 * 
	 * @throws Exception in case of Exceptions that always fail the test
	 */
	@Test
	public void testMarshalJob() throws Exception {
		final String xml = getXML("Job.xml");
		final Job job = XStreamMarshaller.unmarshal(xml, Job.class);
		assertEquals(xml, XStreamMarshaller.marshal(job));
	}

	/**
	 * Test unmarshaling a XML into a {@link Job} object
	 * 
	 * @throws Exception in case of Exceptions that always fail the test
	 */
	@Test
	public void testUnmarshalJob() throws Exception {
		final String xml = getXML("Job.xml");
		final Job job = XStreamMarshaller.unmarshal(xml, Job.class);
		assertEquals(0, job.getHudsonServerId());
		assertEquals("Marvelution", job.getName());
		assertEquals("Marvelution".hashCode(), job.hashCode());
		assertEquals("Marvelution", job.toString());
		assertEquals("job/Marvelution/", job.getUrl());
		assertEquals("Marvelution organization Project Object Model", job.getDescription());
		assertEquals("MARVADMIN", job.getJiraKey());
		assertTrue(job.isBuildable());
		assertEquals(41, job.getNextBuildNumber());
		assertFalse(job.getHealthReports().isEmpty());
		final HealthReport report = job.getHealthReports().get(0);
		assertEquals("Build stability: 1 out of the last 5 builds failed.", report.getDescription());
		assertEquals("health-60to79.gif", report.getIcon());
		assertEquals(79, report.getScore());
		assertEquals(Result.SUCCESS, job.getResult());
		assertEquals(5, job.getBuilds().size());
		assertFalse(job.getModules().isEmpty());
		final Job module = job.getModules().get(0);
		assertEquals("com.marvelution:marvelution", module.getName());
		assertTrue(module.getUrl().startsWith(job.getUrl()));
		assertEquals("job/Marvelution/com.marvelution$marvelution/", module.getUrl());
		assertEquals(job.getFirstBuild().getNumber(), module.getFirstBuild().getNumber());
		assertEquals(job.getLastBuild().getNumber(), module.getLastBuild().getNumber());
		assertEquals(job.getLastCompletedBuild().getNumber(), module.getLastCompletedBuild().getNumber());
		assertEquals(job.getLastFailedBuild().getNumber(), module.getLastFailedBuild().getNumber());
		assertEquals(job.getLastStableBuild().getNumber(), module.getLastStableBuild().getNumber());
		assertEquals(job.getLastUnstableBuild().getNumber(), module.getLastUnstableBuild().getNumber());
		assertEquals(job.getLastSuccessfulBuild().getNumber(), module.getLastSuccessfulBuild().getNumber());
		assertEquals(job.getHealthReports().get(0), module.getHealthReports().get(0));
		assertEquals(job.getResult(), module.getResult());
	}

	/**
	 * Test marshaling a {@link JobsList} object into XML
	 * 
	 * @throws Exception in case of Exceptions that always fail the test
	 */
	@Test
	public void testMarshalJobsList() throws Exception {
		final String xml = getXML("JobsList.xml");
		final JobsList jobsList = XStreamMarshaller.unmarshal(xml, JobsList.class);
		assertEquals(xml, XStreamMarshaller.marshal(jobsList));
	}

	/**
	 * Test unmarshaling a XML into a {@link JobsList} object
	 * 
	 * @throws Exception in case of Exceptions that always fail the test
	 */
	@Test
	public void testUnmarshalJobsList() throws Exception {
		final String xml = getXML("JobsList.xml");
		final JobsList jobsList = XStreamMarshaller.unmarshal(xml, JobsList.class);
		assertFalse(jobsList.getJobs().isEmpty());
		Job job = jobsList.getJobs().get(0);
		assertEquals("Marvelution", job.getName());
		assertEquals("job/Marvelution/", job.getUrl());
		assertTrue(job.isBuildable());
		assertEquals(1, job.getNextBuildNumber());
		assertEquals(Result.SUCCESS, job.getResult());
		job = jobsList.getJobs().get(1);
		assertEquals("Marvelution-utils", job.getName());
		assertEquals("job/Marvelution-utils/", job.getUrl());
		assertFalse(job.isBuildable());
		assertEquals(1, job.getNextBuildNumber());
		assertEquals(Result.SUCCESS, job.getResult());
	}

	/**
	 * Test marshaling a {@link BuildsList} object into XML
	 * 
	 * @throws Exception in case of Exceptions that always fail the test
	 */
	@Test
	public void testMarshalBuildsList() throws Exception {
		final BuildsList builds = new BuildsList();
		final Build build = new Build(58, "Marvelution-utils");
		build.setUrl("job/Marvelution-utils/58/");
		build.setJobUrl("job/Marvelution-utils/");
		build.setResult(Result.SUCCESS);
		build.setState(State.NOT_STARTED);
		build.getTriggers().add(new UserTrigger("markrekveld"));
		builds.getBuilds().add(build);
		assertEquals(getXML("BuildsList.xml"), XStreamMarshaller.marshal(builds));
	}

	/**
	 * Test unmarshaling a XML into a {@link BuildsList} object
	 * 
	 * @throws Exception in case of Exceptions that always fail the test
	 */
	@Test
	public void testUnmarshalBuildsList() throws Exception {
		final String xml = getXML("BuildsList.xml");
		final BuildsList buildsList = XStreamMarshaller.unmarshal(xml, BuildsList.class);
		assertEquals(1, buildsList.getBuilds().size());
		final Build build = buildsList.getBuilds().get(0);
		assertEquals(0, build.getHudsonServerId());
		assertEquals(58, build.getNumber());
		assertEquals("job/Marvelution-utils/58/", build.getUrl());
		assertEquals("Marvelution-utils", build.getJobName());
		assertEquals("Marvelution-utils #58", build.toString());
		assertEquals(Integer.valueOf(build.getNumber()).hashCode(), build.hashCode());
		assertEquals("job/Marvelution-utils/", build.getJobUrl());
		assertTrue(build.getTriggers().get(0) instanceof UserTrigger);
		assertEquals("markrekveld", ((UserTrigger) build.getTriggers().get(0)).getUserName());
		assertEquals(Result.SUCCESS, build.getResult());
		assertEquals(State.NOT_STARTED, build.getState());
		assertTrue(build.getRelatedIssueKeys().isEmpty());
	}

	/**
	 * Test marshaling a {@link Build} object into XML
	 * 
	 * @throws Exception in case of Exceptions that always fail the test
	 */
	@Test
	public void testMarshalBuild() throws Exception {
		final Build build = new Build(58, "Marvelution-utils");
		build.setHudsonServerId(1);
		build.setUrl("job/Marvelution-utils/58/");
		build.setJobUrl("job/Marvelution-utils/");
		final List<Trigger> triggers = new ArrayList<Trigger>();
		triggers.add(new SCMTrigger());
		build.setTriggers(triggers);
		build.setDuration(114554L);
		build.setTimestamp(1241906926000L);
		build.setResult(Result.SUCCESS);
		final TestResult testResult = new TestResult();
		testResult.setFailed(0);
		testResult.setSkipped(0);
		testResult.setTotal(30);
		testResult.getFailedTests().add("TestCase.java");
		build.setTestResult(testResult);
		build.setState(State.COMPLETED);
		final Set<String> relatedIssueKeys = new HashSet<String>();
		relatedIssueKeys.add("ISSUE-1");
		relatedIssueKeys.add("ISSUE-2");
		build.setRelatedIssueKeys(relatedIssueKeys);
		final List<String> artifacts = new ArrayList<String>();
		artifacts.add("pom.xml");
		build.setArtifacts(artifacts);
		assertEquals(getXML("Build.xml"), XStreamMarshaller.marshal(build));
	}

	/**
	 * Test unmarshaling a XML into a {@link Build} object
	 * 
	 * @throws Exception in case of Exceptions that always fail the test
	 */
	@Test
	public void testUnmarshalBuild() throws Exception {
		final String xml = getXML("Build.xml");
		final Build build = XStreamMarshaller.unmarshal(xml, Build.class);
		assertEquals(0, build.getHudsonServerId());
		assertEquals(58, build.getNumber());
		assertEquals("job/Marvelution-utils/58/", build.getUrl());
		assertEquals("Marvelution-utils", build.getJobName());
		assertEquals("job/Marvelution-utils/", build.getJobUrl());
		assertTrue(build.getTriggers().get(0) instanceof SCMTrigger);
		assertTrue(build.getArtifacts().contains("pom.xml"));
		assertEquals(114554L, build.getDuration());
		assertEquals(1241906926000L, build.getTimestamp());
		assertEquals(Result.SUCCESS, build.getResult());
		final TestResult testResult = build.getTestResult();
		assertEquals(0, testResult.getFailed());
		assertEquals(0, testResult.getSkipped());
		assertEquals(30, testResult.getTotal());
		assertEquals(30, testResult.getSuccessful());
		assertEquals(30, testResult.getPassed());
		assertEquals(State.COMPLETED, build.getState());
		assertTrue(build.getRelatedIssueKeys().contains("ISSUE-1"));
		assertTrue(build.getRelatedIssueKeys().contains("ISSUE-2"));
		assertTrue(testResult.getFailedTests().contains("TestCase.java"));
	}

	/**
	 * Get the content of a file as String
	 * 
	 * @param filename the file name to get the content from
	 * @return the content
	 * @throws IOException in case the file cannot be found or read errors occur
	 */
	private String getXML(String filename) throws IOException {
		String xml = "";
		final String file = Thread.currentThread().getContextClassLoader().getResource(filename).getFile();
		final FileReader fileReader = new FileReader(file);
		final BufferedReader bufferReader = new BufferedReader(fileReader);
		String line;
		while ((line = bufferReader.readLine()) != null) {
			if (!"".equals(line)) {
				xml += ((!"".equals(xml)) ? '\n' : "") + line;
			}
		}
		fileReader.close();
		return xml;
	}

}
