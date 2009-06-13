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

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.verification.VerificationModeFactory;

import com.atlassian.jira.project.Project;
import com.atlassian.jira.project.version.Version;
import com.marvelution.jira.plugins.hudson.model.ApiImplementation;
import com.marvelution.jira.plugins.hudson.model.Build;
import com.marvelution.jira.plugins.hudson.model.Job;
import com.marvelution.jira.plugins.hudson.service.HudsonServer;
import com.marvelution.jira.plugins.hudson.service.HudsonServerAccessor;
import com.marvelution.jira.plugins.hudson.service.HudsonServerAccessorException;
import com.marvelution.jira.plugins.hudson.service.HudsonServerManager;

/**
 * TestCase for {@link DefaultHudsonServerAccessorImpl}
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class DefaultHudsonServerAccessorImplTest {

	private DefaultHudsonServerAccessorImpl serverAccessor;

	@Mock
	private Project project;

	@Mock
	private Version version;

	@Mock
	private Version prevVersion;

	@Mock
	private Version firstVersion;

	@Mock
	private HudsonServerManager serverManager;

	@Mock
	private HttpClient httpClient;

	@Mock
	private PostMethod postMethod;

	@Mock
	private GetMethod getMethod;

	private HudsonServer server;

	private Map<String, String> params = new HashMap<String, String>();

	/**
	 * Setup the variables for the tests
	 * 
	 * @throws Exception in case of failures
	 */
	@Before
	public void setUp() throws Exception {
		server = new DefaultHudsonServerImpl();
		server.setName("Hudson CI");
		server.setHost("http://hudson.marvelution.com");
		MockitoAnnotations.initMocks(this);
		when(version.getProjectObject()).thenReturn(project);
		when(version.getSequence()).thenReturn(3L);
		when(version.isReleased()).thenReturn(true);
		when(version.getReleaseDate()).thenReturn(Calendar.getInstance().getTime());
		when(prevVersion.getSequence()).thenReturn(2L);
		when(version.isReleased()).thenReturn(false);
		when(firstVersion.getSequence()).thenReturn(1L);
		when(firstVersion.isReleased()).thenReturn(true);
		when(firstVersion.getReleaseDate()).thenReturn(Calendar.getInstance().getTime());
		final List<Version> versions = new ArrayList<Version>();
		versions.add(firstVersion);
		versions.add(prevVersion);
		versions.add(version);
		when(project.getKey()).thenReturn("MARVADMIN");
		when(project.getVersions()).thenReturn(versions);
		when(serverManager.getServerByJiraProject(any(Project.class))).thenReturn(server);
		when(serverManager.getServers()).thenReturn(Collections.singletonList(server));
		serverAccessor = new TestDefaultHudsonServerAccessorImpl(serverManager);
	}

	/**
	 * Test get ApiImplementation
	 * 
	 * @throws Exception in case of test failures
	 */
	@Test
	public void testGetApiImplementation() throws Exception {
		when(postMethod.getStatusCode()).thenReturn(HttpStatus.SC_OK);
		when(postMethod.getResponseBodyAsString()).thenReturn(getXML("JiraApi.xml"));
		final ApiImplementation api = serverAccessor.getApiImplementation(server);
		assertEquals("1.0", api.getVersion());
		verify(httpClient, VerificationModeFactory.times(1)).executeMethod(postMethod);
		verify(postMethod, VerificationModeFactory.times(1)).getStatusCode();
		verify(postMethod, VerificationModeFactory.times(1)).getResponseBodyAsString();
		verify(postMethod, VerificationModeFactory.times(1)).releaseConnection();
	}

	/**
	 * Test get ApiImplementation
	 * 
	 * @throws Exception in case of test failures
	 */
	@Test
	public void testGetApiImplementationUnmarshalException() throws Exception {
		when(postMethod.getStatusCode()).thenReturn(HttpStatus.SC_OK);
		when(postMethod.getResponseBodyAsString()).thenReturn("OOPS!");
		try {
			serverAccessor.getApiImplementation(server);
			fail("This test should fail");
		} catch (HudsonServerAccessorException e) {
			assertTrue(e.getMessage().startsWith(
				"Failed to unmarshal the Hudson server response to a API Implementation object. Reason: "));
		}
		verify(httpClient, VerificationModeFactory.times(1)).executeMethod(postMethod);
		verify(postMethod, VerificationModeFactory.times(1)).getStatusCode();
		verify(postMethod, VerificationModeFactory.times(1)).getResponseBodyAsString();
		verify(postMethod, VerificationModeFactory.times(1)).releaseConnection();
	}

	/**
	 * Test gets Projects List
	 * 
	 * @throws Exception in case of test failures
	 */
	@Test
	public void testGetProjectsList() throws Exception {
		when(postMethod.getStatusCode()).thenReturn(HttpStatus.SC_OK);
		when(postMethod.getResponseBodyAsString()).thenReturn(getXML("ListAllProjects.xml"));
		final List<Job> jobs = serverAccessor.getProjectsList(server);
		assertNotNull(jobs);
		assertFalse(jobs.isEmpty());
		assertEquals(3, jobs.size());
		verify(httpClient, VerificationModeFactory.times(1)).executeMethod(postMethod);
		verify(postMethod, VerificationModeFactory.times(1)).getStatusCode();
		verify(postMethod, VerificationModeFactory.times(1)).getResponseBodyAsString();
		verify(postMethod, VerificationModeFactory.times(1)).releaseConnection();
	}

	/**
	 * Test gets Projects List unmarshal exception
	 * 
	 * @throws Exception in case of test failures
	 */
	@Test
	public void testGetProjectsListUnmarshalException() throws Exception {
		when(postMethod.getStatusCode()).thenReturn(HttpStatus.SC_OK);
		when(postMethod.getResponseBodyAsString()).thenReturn("OOPS!");
		try {
			serverAccessor.getProjectsList(server);
			fail("This test should fail");
		} catch (HudsonServerAccessorException e) {
			assertTrue(e.getMessage().startsWith(
				"Failed to unmarshal the Hudson server response to a Jobs object. Reason: "));
		}
		verify(httpClient, VerificationModeFactory.times(1)).executeMethod(postMethod);
		verify(postMethod, VerificationModeFactory.times(1)).getStatusCode();
		verify(postMethod, VerificationModeFactory.times(1)).getResponseBodyAsString();
		verify(postMethod, VerificationModeFactory.times(1)).releaseConnection();
	}

	/**
	 * Test gets Projects
	 * 
	 * @throws Exception in case of test failures
	 */
	@Test
	public void testGetProjects() throws Exception {
		when(postMethod.getStatusCode()).thenReturn(HttpStatus.SC_OK);
		when(postMethod.getResponseBodyAsString()).thenReturn(getXML("GetAllProjects.xml"));
		final List<Job> jobs = serverAccessor.getProjects(server);
		assertNotNull(jobs);
		assertFalse(jobs.isEmpty());
		assertEquals(3, jobs.size());
		verify(httpClient, VerificationModeFactory.times(1)).executeMethod(postMethod);
		verify(postMethod, VerificationModeFactory.times(1)).getStatusCode();
		verify(postMethod, VerificationModeFactory.times(1)).getResponseBodyAsString();
		verify(postMethod, VerificationModeFactory.times(1)).releaseConnection();
	}

	/**
	 * Test gets Projects unmarshal exception
	 * 
	 * @throws Exception in case of test failures
	 */
	@Test
	public void testGetProjectsUnmarshalException() throws Exception {
		when(postMethod.getStatusCode()).thenReturn(HttpStatus.SC_OK);
		when(postMethod.getResponseBodyAsString()).thenReturn("OOPS!");
		try {
			serverAccessor.getProjects(server);
			fail("This test should fail");
		} catch (HudsonServerAccessorException e) {
			assertTrue(e.getMessage().startsWith(
				"Failed to unmarshal the Hudson server response to a Jobs object. Reason: "));
		}
		verify(httpClient, VerificationModeFactory.times(1)).executeMethod(postMethod);
		verify(postMethod, VerificationModeFactory.times(1)).getStatusCode();
		verify(postMethod, VerificationModeFactory.times(1)).getResponseBodyAsString();
		verify(postMethod, VerificationModeFactory.times(1)).releaseConnection();
	}

	/**
	 * Test gets Project
	 * 
	 * @throws Exception in case of test failures
	 */
	@Test
	public void testGetProject() throws Exception {
		when(postMethod.getStatusCode()).thenReturn(HttpStatus.SC_OK);
		when(postMethod.getResponseBodyAsString()).thenReturn(getXML("GetProject.xml"));
		final Job job = serverAccessor.getProject(project);
		assertNotNull(job);
		assertEquals("MARVADMIN", job.getJiraKey());
		assertEquals("Marvelution", job.getName());
		verify(httpClient, VerificationModeFactory.times(1)).executeMethod(postMethod);
		verify(postMethod, VerificationModeFactory.times(1)).getStatusCode();
		verify(postMethod, VerificationModeFactory.times(1)).getResponseBodyAsString();
		verify(postMethod, VerificationModeFactory.times(1)).releaseConnection();
	}

	/**
	 * Test gets Project unmarshal exception
	 * 
	 * @throws Exception in case of test failures
	 */
	@Test
	public void testGetProjectUnmarshalException() throws Exception {
		when(postMethod.getStatusCode()).thenReturn(HttpStatus.SC_OK);
		when(postMethod.getResponseBodyAsString()).thenReturn("OOPS!");
		try {
			serverAccessor.getProject(project);
			fail("This test should fail");
		} catch (HudsonServerAccessorException e) {
			assertTrue(e.getMessage().startsWith(
				"Failed to unmarshal the Hudson server response to a Job object. Reason: "));
		}
		verify(httpClient, VerificationModeFactory.times(1)).executeMethod(postMethod);
		verify(postMethod, VerificationModeFactory.times(1)).getStatusCode();
		verify(postMethod, VerificationModeFactory.times(1)).getResponseBodyAsString();
		verify(postMethod, VerificationModeFactory.times(1)).releaseConnection();
	}

	/**
	 * Test gets Builds for project
	 * 
	 * @throws Exception in case of test failures
	 */
	@Test
	public void testGetBuildsForProject() throws Exception {
		when(postMethod.getStatusCode()).thenReturn(HttpStatus.SC_OK);
		when(postMethod.getResponseBodyAsString()).thenReturn(getXML("BuildsList.xml"));
		final List<Build> builds = serverAccessor.getBuilds(project);
		assertFalse(builds.isEmpty());
		assertEquals(1, builds.size());
		verify(httpClient, VerificationModeFactory.times(1)).executeMethod(postMethod);
		verify(postMethod, VerificationModeFactory.times(1)).getStatusCode();
		verify(postMethod, VerificationModeFactory.times(1)).getResponseBodyAsString();
		verify(postMethod, VerificationModeFactory.times(1)).releaseConnection();
	}

	/**
	 * Test gets Builds for project unmarshal exception
	 * 
	 * @throws Exception in case of test failures
	 */
	@Test
	public void testGetBuildsForProjectUnmarshalException() throws Exception {
		when(postMethod.getStatusCode()).thenReturn(HttpStatus.SC_OK);
		when(postMethod.getResponseBodyAsString()).thenReturn("OOPS!");
		try {
			serverAccessor.getBuilds(project);
			fail("This test should fail");
		} catch (HudsonServerAccessorException e) {
			assertTrue(e.getMessage().startsWith(
				"Failed to unmarshal the Hudson server response to a Builds object. Reason: "));
		}
		verify(httpClient, VerificationModeFactory.times(1)).executeMethod(postMethod);
		verify(postMethod, VerificationModeFactory.times(1)).getStatusCode();
		verify(postMethod, VerificationModeFactory.times(1)).getResponseBodyAsString();
		verify(postMethod, VerificationModeFactory.times(1)).releaseConnection();
	}

	/**
	 * Test gets Builds for version
	 * 
	 * @throws Exception in case of test failures
	 */
	@Test
	public void testGetBuildsForVersion() throws Exception {
		when(postMethod.getStatusCode()).thenReturn(HttpStatus.SC_OK);
		when(postMethod.getResponseBodyAsString()).thenReturn(getXML("BuildsList.xml"));
		final List<Build> builds = serverAccessor.getBuilds(version);
		assertFalse(builds.isEmpty());
		assertEquals(1, builds.size());
		verify(httpClient, VerificationModeFactory.times(1)).executeMethod(postMethod);
		verify(postMethod, VerificationModeFactory.times(1)).getStatusCode();
		verify(postMethod, VerificationModeFactory.times(1)).getResponseBodyAsString();
		verify(postMethod, VerificationModeFactory.times(1)).releaseConnection();
	}

	/**
	 * Test gets Builds for version unmarshal exception
	 * 
	 * @throws Exception in case of test failures
	 */
	@Test
	public void testGetBuildsForVersionUnmarshalException() throws Exception {
		when(postMethod.getStatusCode()).thenReturn(HttpStatus.SC_OK);
		when(postMethod.getResponseBodyAsString()).thenReturn("OOPS!");
		try {
			serverAccessor.getBuilds(version);
			fail("This test should fail");
		} catch (HudsonServerAccessorException e) {
			assertTrue(e.getMessage().startsWith(
				"Failed to unmarshal the Hudson server response to a Builds object. Reason: "));
		}
		verify(httpClient, VerificationModeFactory.times(1)).executeMethod(postMethod);
		verify(postMethod, VerificationModeFactory.times(1)).getStatusCode();
		verify(postMethod, VerificationModeFactory.times(1)).getResponseBodyAsString();
		verify(postMethod, VerificationModeFactory.times(1)).releaseConnection();
	}

	/**
	 * Test gets Builds for issues keys
	 * 
	 * @throws Exception in case of test failures
	 */
	@Test
	public void testGetBuildsForIssueKeys() throws Exception {
		when(postMethod.getStatusCode()).thenReturn(HttpStatus.SC_OK);
		when(postMethod.getResponseBodyAsString()).thenReturn(getXML("BuildsList.xml"));
		final List<Build> builds = serverAccessor.getBuilds(Collections.singletonList("MARVADMIN-1"));
		assertFalse(builds.isEmpty());
		assertEquals(1, builds.size());
		verify(httpClient, VerificationModeFactory.times(1)).executeMethod(postMethod);
		verify(postMethod, VerificationModeFactory.times(1)).getStatusCode();
		verify(postMethod, VerificationModeFactory.times(1)).getResponseBodyAsString();
		verify(postMethod, VerificationModeFactory.times(1)).releaseConnection();
	}

	/**
	 * Test gets Builds for issues keys unmarshal exception
	 * 
	 * @throws Exception in case of test failures
	 */
	@Test
	public void testGetBuildsForIssueKeysUnmarshalException() throws Exception {
		when(postMethod.getStatusCode()).thenReturn(HttpStatus.SC_OK);
		when(postMethod.getResponseBodyAsString()).thenReturn("OOPS!");
		try {
			serverAccessor.getBuilds(Collections.singletonList("MARVADMIN-1"));
			fail("This test should fail");
		} catch (HudsonServerAccessorException e) {
			assertTrue(e.getMessage().startsWith(
				"Failed to unmarshal the Hudson server response to a Builds object. Reason: "));
		}
		verify(httpClient, VerificationModeFactory.times(1)).executeMethod(postMethod);
		verify(postMethod, VerificationModeFactory.times(1)).getStatusCode();
		verify(postMethod, VerificationModeFactory.times(1)).getResponseBodyAsString();
		verify(postMethod, VerificationModeFactory.times(1)).releaseConnection();
	}

	/**
	 * Test response form invalid Hudson server
	 * 
	 * @throws Exception in case of test failures
	 */
	@Test
	public void testGetHudsonServerActionResponseInvalidHudsonServer() throws Exception {
		try {
			serverAccessor.getHudsonServerActionResponse(null, HudsonServerAccessor.GET_ALL_PROJECTS_ACTION, params);
		} catch (HudsonServerAccessorException e) {
			assertEquals("hudsonServer may not be null", e.getMessage());
		}
	}

	/**
	 * Test response form unsecured Hudson server action
	 * 
	 * @throws Exception in case of test failures
	 */
	@Test
	public void testGetHudsonServerActionResponseUnsecuredHudsonServer() throws Exception {
		when(postMethod.getStatusCode()).thenReturn(HttpStatus.SC_OK);
		when(postMethod.getResponseBodyAsString()).thenReturn("Fake response");
		final String response = serverAccessor.getHudsonServerActionResponse(server, "/fake/url", params);
		assertEquals("Fake response", response);
		verify(httpClient, VerificationModeFactory.times(1)).executeMethod(postMethod);
		verify(postMethod, VerificationModeFactory.times(1)).getStatusCode();
		verify(postMethod, VerificationModeFactory.times(1)).getResponseBodyAsString();
		verify(postMethod, VerificationModeFactory.times(1)).releaseConnection();
	}

	/**
	 * Test response form unsecured Hudson server action
	 * 
	 * @throws Exception in case of test failures
	 */
	@Test
	public void testGetHudsonServerActionResponseUnsecuredHudsonServerConnectionError() throws Exception {
		when(httpClient.executeMethod(postMethod)).thenThrow(new HttpException());
		when(postMethod.getResponseBodyAsString()).thenReturn("Fake response");
		try {
			serverAccessor.getHudsonServerActionResponse(server, "/fake/url", params);
		} catch (HudsonServerAccessorException e) {
			assertEquals("Failed to connect to the Hudson Server", e.getMessage());
		}
		verify(httpClient, VerificationModeFactory.times(1)).executeMethod(postMethod);
	}

	/**
	 * Test response form secured Hudson server action
	 * 
	 * @throws Exception in case of test failures
	 */
	@Test
	public void testGetHudsonServerActionResponseSecuredHudsonServer() throws Exception {
		server.setUsername("admin");
		server.setPassword("admin");
		when(postMethod.getStatusCode()).thenReturn(HttpStatus.SC_OK);
		when(postMethod.getResponseBodyAsString()).thenReturn("Fake response");
		final String response = serverAccessor.getHudsonServerActionResponse(server, "/fake/url", params);
		assertEquals("Fake response", response);
		verify(httpClient, VerificationModeFactory.times(2)).executeMethod(postMethod);
		verify(postMethod, VerificationModeFactory.times(1)).addParameter("j_username", server.getUsername());
		verify(postMethod, VerificationModeFactory.times(1)).addParameter("j_password", server.getPassword());
		verify(postMethod, VerificationModeFactory.times(1)).addParameter("action", "login");
		verify(postMethod, VerificationModeFactory.times(2)).getStatusCode();
		verify(postMethod, VerificationModeFactory.times(1)).getResponseBodyAsString();
		verify(postMethod, VerificationModeFactory.times(2)).releaseConnection();
	}

	/**
	 * Test implementation for the {@link DefaultHudsonServerAccessorImpl}
	 * 
	 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
	 */
	private class TestDefaultHudsonServerAccessorImpl extends DefaultHudsonServerAccessorImpl {

		/**
		 * Constructor
		 * 
		 * @param serverManager the {@link HudsonServerManager}
		 */
		public TestDefaultHudsonServerAccessorImpl(HudsonServerManager serverManager) {
			super(serverManager);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		protected HttpClient getHttpClient() {
			return httpClient;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		protected PostMethod createPostMethod(String url) {
			return postMethod;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		protected GetMethod createGetMethod(String url) {
			return getMethod;
		}

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
