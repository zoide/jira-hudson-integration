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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.Header;
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
import com.marvelution.jira.plugins.hudson.api.model.ApiImplementation;
import com.marvelution.jira.plugins.hudson.api.model.Build;
import com.marvelution.jira.plugins.hudson.api.model.Job;
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

	private DefaultHudsonServerAccessorImpl hudsonServerAccessor;

	@Mock
	private Project project;

	@Mock
	private Version version;

	@Mock
	private Version prevVersion;

	@Mock
	private Version firstVersion;

	@Mock
	private HudsonServerManager hudsonServerManager;

	@Mock
	private HttpClient httpClient;

	@Mock
	private PostMethod postMethod;

	@Mock
	private GetMethod getMethod;

	private HudsonServer hudsonServer;

	private Map<String, String> params = new HashMap<String, String>();

	/**
	 * Setup the variables for the tests
	 * 
	 * @throws Exception in case of failures
	 */
	@Before
	public void setUp() throws Exception {
		hudsonServer = new DefaultHudsonServerImpl();
		hudsonServer.setName("Hudson CI");
		hudsonServer.setHost("http://hudson.marvelution.com");
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
		when(hudsonServerManager.getServerByJiraProject(any(Project.class))).thenReturn(hudsonServer);
		when(hudsonServerManager.getServers()).thenReturn(Collections.singletonList(hudsonServer));
		hudsonServerAccessor = new TestDefaultHudsonServerAccessorImpl(hudsonServerManager);
	}

	/**
	 * Test get Crumb from Hudson
	 * 
	 * @throws Exception in case of test failures
	 */
	@Test
	public void testGetCrumb() throws Exception {
		when(getMethod.getStatusCode()).thenReturn(HttpStatus.SC_OK);
		when(getMethod.getResponseBodyAsStream()).thenReturn(new ByteArrayInputStream("Fake Response".getBytes()));
		when(getMethod.getResponseHeader("Crumb")).thenReturn(new Header("Crumb", "crumb-parameter"));
		when(getMethod.getResponseHeader("Crumb-Field")).thenReturn(new Header("Crumb-Field", ".crumb"));
		hudsonServerAccessor.getCrumb(hudsonServer);
		assertEquals("crumb-parameter", hudsonServer.getCrumb());
		assertEquals(".crumb", hudsonServer.getCrumbField());
		verify(httpClient, VerificationModeFactory.times(1)).executeMethod(getMethod);
		verify(getMethod, VerificationModeFactory.times(1)).getStatusCode();
		verify(getMethod, VerificationModeFactory.times(1)).getResponseBodyAsStream();
		verify(getMethod, VerificationModeFactory.times(1)).releaseConnection();
		verify(getMethod, VerificationModeFactory.times(2)).getResponseHeader("Crumb");
		verify(getMethod, VerificationModeFactory.times(2)).getResponseHeader("Crumb-Field");
	}

	/**
	 * Test get Crumb from Hudson
	 * 
	 * @throws Exception in case of test failures
	 */
	@Test
	public void testGetCrumbNoHeaders() throws Exception {
		when(getMethod.getStatusCode()).thenReturn(HttpStatus.SC_OK);
		when(getMethod.getResponseBodyAsStream()).thenReturn(new ByteArrayInputStream("Fake Response".getBytes()));
		when(getMethod.getResponseHeader("Crumb")).thenReturn(null);
		when(getMethod.getResponseHeader("Crumb-Field")).thenReturn(null);
		hudsonServerAccessor.getCrumb(hudsonServer);
		assertNull(hudsonServer.getCrumb());
		assertNull(hudsonServer.getCrumbField());
		verify(httpClient, VerificationModeFactory.times(1)).executeMethod(getMethod);
		verify(getMethod, VerificationModeFactory.times(1)).getStatusCode();
		verify(getMethod, VerificationModeFactory.times(1)).getResponseBodyAsStream();
		verify(getMethod, VerificationModeFactory.times(1)).releaseConnection();
		verify(getMethod, VerificationModeFactory.times(1)).getResponseHeader("Crumb");
		verify(getMethod, VerificationModeFactory.times(0)).getResponseHeader("Crumb-Field");
	}

	/**
	 * Test get ApiImplementation
	 * 
	 * @throws Exception in case of test failures
	 */
	@Test
	public void testGetApiImplementation() throws Exception {
		when(postMethod.getStatusCode()).thenReturn(HttpStatus.SC_OK);
		when(postMethod.getResponseBodyAsStream()).thenReturn(getXMLAsInputStream("JiraApi.xml"));
		final ApiImplementation api = hudsonServerAccessor.getApiImplementation(hudsonServer);
		assertEquals("1.0", api.getVersion());
		verify(httpClient, VerificationModeFactory.times(1)).executeMethod(postMethod);
		verify(postMethod, VerificationModeFactory.times(1)).getStatusCode();
		verify(postMethod, VerificationModeFactory.times(1)).getResponseBodyAsStream();
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
		when(postMethod.getResponseBodyAsStream()).thenReturn(new ByteArrayInputStream("OOPS!".getBytes()));
		try {
			hudsonServerAccessor.getApiImplementation(hudsonServer);
			fail("This test should fail");
		} catch (HudsonServerAccessorException e) {
			assertTrue(e.getMessage().startsWith(
				"Failed to unmarshal the Hudson server response to a API Implementation object. Reason:"));
		}
		verify(httpClient, VerificationModeFactory.times(1)).executeMethod(postMethod);
		verify(postMethod, VerificationModeFactory.times(1)).getStatusCode();
		verify(postMethod, VerificationModeFactory.times(1)).getResponseBodyAsStream();
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
		when(postMethod.getResponseBodyAsStream()).thenReturn(getXMLAsInputStream("ListAllProjects.xml"));
		final List<Job> jobs = hudsonServerAccessor.getProjectsList(hudsonServer);
		assertNotNull(jobs);
		assertFalse(jobs.isEmpty());
		assertEquals(3, jobs.size());
		verify(httpClient, VerificationModeFactory.times(1)).executeMethod(postMethod);
		verify(postMethod, VerificationModeFactory.times(1)).getStatusCode();
		verify(postMethod, VerificationModeFactory.times(1)).getResponseBodyAsStream();
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
		when(postMethod.getResponseBodyAsStream()).thenReturn(new ByteArrayInputStream("OOPS!".getBytes()));
		try {
			hudsonServerAccessor.getProjectsList(hudsonServer);
			fail("This test should fail");
		} catch (HudsonServerAccessorException e) {
			assertTrue(e.getMessage().startsWith(
				"Failed to unmarshal the Hudson server response to a Jobs object. Reason:"));
		}
		verify(httpClient, VerificationModeFactory.times(1)).executeMethod(postMethod);
		verify(postMethod, VerificationModeFactory.times(1)).getStatusCode();
		verify(postMethod, VerificationModeFactory.times(1)).getResponseBodyAsStream();
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
		when(postMethod.getResponseBodyAsStream()).thenReturn(getXMLAsInputStream("GetAllProjects.xml"));
		final List<Job> jobs = hudsonServerAccessor.getProjects(hudsonServer);
		assertNotNull(jobs);
		assertFalse(jobs.isEmpty());
		assertEquals(3, jobs.size());
		verify(httpClient, VerificationModeFactory.times(1)).executeMethod(postMethod);
		verify(postMethod, VerificationModeFactory.times(1)).getStatusCode();
		verify(postMethod, VerificationModeFactory.times(1)).getResponseBodyAsStream();
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
		when(postMethod.getResponseBodyAsStream()).thenReturn(new ByteArrayInputStream("OOPS!".getBytes()));
		try {
			hudsonServerAccessor.getProjects(hudsonServer);
			fail("This test should fail");
		} catch (HudsonServerAccessorException e) {
			assertTrue(e.getMessage().startsWith(
				"Failed to unmarshal the Hudson server response to a Jobs object. Reason:"));
		}
		verify(httpClient, VerificationModeFactory.times(1)).executeMethod(postMethod);
		verify(postMethod, VerificationModeFactory.times(1)).getStatusCode();
		verify(postMethod, VerificationModeFactory.times(1)).getResponseBodyAsStream();
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
		when(postMethod.getResponseBodyAsStream()).thenReturn(getXMLAsInputStream("GetProject.xml"));
		final Job job = hudsonServerAccessor.getProject(project);
		assertNotNull(job);
		assertEquals("MARVADMIN", job.getJiraKey());
		assertEquals("Marvelution", job.getName());
		verify(httpClient, VerificationModeFactory.times(1)).executeMethod(postMethod);
		verify(postMethod, VerificationModeFactory.times(1)).getStatusCode();
		verify(postMethod, VerificationModeFactory.times(1)).getResponseBodyAsStream();
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
		when(postMethod.getResponseBodyAsStream()).thenReturn(new ByteArrayInputStream("OOPS!".getBytes()));
		try {
			hudsonServerAccessor.getProject(project);
			fail("This test should fail");
		} catch (HudsonServerAccessorException e) {
			assertTrue(e.getMessage().startsWith(
				"Failed to unmarshal the Hudson server response to a Job object. Reason: "));
		}
		verify(httpClient, VerificationModeFactory.times(1)).executeMethod(postMethod);
		verify(postMethod, VerificationModeFactory.times(1)).getStatusCode();
		verify(postMethod, VerificationModeFactory.times(1)).getResponseBodyAsStream();
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
		when(postMethod.getResponseBodyAsStream()).thenReturn(getXMLAsInputStream("BuildsList.xml"));
		final List<Build> builds = hudsonServerAccessor.getBuilds(project);
		assertFalse(builds.isEmpty());
		assertEquals(1, builds.size());
		verify(httpClient, VerificationModeFactory.times(1)).executeMethod(postMethod);
		verify(postMethod, VerificationModeFactory.times(1)).getStatusCode();
		verify(postMethod, VerificationModeFactory.times(1)).getResponseBodyAsStream();
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
		when(postMethod.getResponseBodyAsStream()).thenReturn(new ByteArrayInputStream("OOPS!".getBytes()));
		try {
			hudsonServerAccessor.getBuilds(project);
			fail("This test should fail");
		} catch (HudsonServerAccessorException e) {
			assertTrue(e.getMessage().startsWith(
				"Failed to unmarshal the Hudson server response to a Builds object. Reason: "));
		}
		verify(httpClient, VerificationModeFactory.times(1)).executeMethod(postMethod);
		verify(postMethod, VerificationModeFactory.times(1)).getStatusCode();
		verify(postMethod, VerificationModeFactory.times(1)).getResponseBodyAsStream();
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
		when(postMethod.getResponseBodyAsStream()).thenReturn(getXMLAsInputStream("BuildsList.xml"));
		final List<Build> builds = hudsonServerAccessor.getBuilds(version);
		assertFalse(builds.isEmpty());
		assertEquals(1, builds.size());
		verify(httpClient, VerificationModeFactory.times(1)).executeMethod(postMethod);
		verify(postMethod, VerificationModeFactory.times(1)).getStatusCode();
		verify(postMethod, VerificationModeFactory.times(1)).getResponseBodyAsStream();
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
		when(postMethod.getResponseBodyAsStream()).thenReturn(new ByteArrayInputStream("OOPS!".getBytes()));
		try {
			hudsonServerAccessor.getBuilds(version);
			fail("This test should fail");
		} catch (HudsonServerAccessorException e) {
			assertTrue(e.getMessage().startsWith(
				"Failed to unmarshal the Hudson server response to a Builds object. Reason: "));
		}
		verify(httpClient, VerificationModeFactory.times(1)).executeMethod(postMethod);
		verify(postMethod, VerificationModeFactory.times(1)).getStatusCode();
		verify(postMethod, VerificationModeFactory.times(1)).getResponseBodyAsStream();
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
		when(postMethod.getResponseBodyAsStream()).thenReturn(getXMLAsInputStream("BuildsList.xml"));
		final List<Build> builds = hudsonServerAccessor.getBuilds(Collections.singletonList("MARVADMIN-1"));
		assertFalse(builds.isEmpty());
		assertEquals(1, builds.size());
		verify(httpClient, VerificationModeFactory.times(1)).executeMethod(postMethod);
		verify(postMethod, VerificationModeFactory.times(1)).getStatusCode();
		verify(postMethod, VerificationModeFactory.times(1)).getResponseBodyAsStream();
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
		when(postMethod.getResponseBodyAsStream()).thenReturn(new ByteArrayInputStream("OOPS!".getBytes()));
		try {
			hudsonServerAccessor.getBuilds(Collections.singletonList("MARVADMIN-1"));
			fail("This test should fail");
		} catch (HudsonServerAccessorException e) {
			assertTrue(e.getMessage().startsWith(
				"Failed to unmarshal the Hudson server response to a Builds object. Reason: "));
		}
		verify(httpClient, VerificationModeFactory.times(1)).executeMethod(postMethod);
		verify(postMethod, VerificationModeFactory.times(1)).getStatusCode();
		verify(postMethod, VerificationModeFactory.times(1)).getResponseBodyAsStream();
		verify(postMethod, VerificationModeFactory.times(1)).releaseConnection();
	}

	/**
	 * Test response form invalid Hudson hudsonServer
	 * 
	 * @throws Exception in case of test failures
	 */
	@Test
	public void testGetHudsonServerActionResponseInvalidHudsonServer() throws Exception {
		try {
			hudsonServerAccessor.getHudsonServerActionResponse(null, HudsonServerAccessor.GET_ALL_PROJECTS_ACTION,
				params);
		} catch (HudsonServerAccessorException e) {
			assertEquals("hudsonServer may not be null", e.getMessage());
		}
	}

	/**
	 * Test response form invalid Hudson hudsonServer
	 * 
	 * @throws Exception in case of test failures
	 */
	@Test
	public void testGetHudsonServerActionResponseFromHttpMethodInvalidHudsonServer() throws Exception {
		try {
			hudsonServerAccessor.getHudsonServerActionResponse(null, new GetMethod("/fake/url"));
		} catch (HudsonServerAccessorException e) {
			assertEquals("hudsonServer may not be null", e.getMessage());
		}
	}

	/**
	 * Test response form unsecured Hudson hudsonServer action
	 * 
	 * @throws Exception in case of test failures
	 */
	@Test
	public void testGetHudsonServerActionResponseUnsecuredHudsonServer() throws Exception {
		when(postMethod.getStatusCode()).thenReturn(HttpStatus.SC_OK);
		when(postMethod.getResponseBodyAsStream()).thenReturn(new ByteArrayInputStream("Fake response".getBytes()));
		final String response = hudsonServerAccessor.getHudsonServerActionResponse(hudsonServer, "/fake/url", params);
		assertEquals("Fake response" + '\n', response);
		verify(httpClient, VerificationModeFactory.times(1)).executeMethod(postMethod);
		verify(postMethod, VerificationModeFactory.times(1)).getStatusCode();
		verify(postMethod, VerificationModeFactory.times(1)).getResponseBodyAsStream();
		verify(postMethod, VerificationModeFactory.times(1)).releaseConnection();
	}

	/**
	 * Test response form unsecured Hudson hudsonServer action
	 * 
	 * @throws Exception in case of test failures
	 */
	@Test
	public void testGetHudsonServerActionResponseUnsecuredHudsonServerConnectionError() throws Exception {
		when(httpClient.executeMethod(postMethod)).thenThrow(new HttpException());
		when(postMethod.getResponseBodyAsStream()).thenReturn(new ByteArrayInputStream("Fake response".getBytes()));
		try {
			hudsonServerAccessor.getHudsonServerActionResponse(hudsonServer, "/fake/url", params);
		} catch (HudsonServerAccessorException e) {
			assertEquals("Failed to connect to the Hudson Server", e.getMessage());
		}
		verify(httpClient, VerificationModeFactory.times(1)).executeMethod(postMethod);
	}

	/**
	 * Test response form secured Hudson hudsonServer action
	 * 
	 * @throws Exception in case of test failures
	 */
	@Test
	public void testGetHudsonServerActionResponseSecuredHudsonServer() throws Exception {
		hudsonServer.setUsername("admin");
		hudsonServer.setPassword("admin");
		when(postMethod.getStatusCode()).thenReturn(HttpStatus.SC_OK);
		when(postMethod.getResponseBodyAsStream()).thenReturn(new ByteArrayInputStream("Fake response".getBytes()));
		final String response = hudsonServerAccessor.getHudsonServerActionResponse(hudsonServer, "/fake/url", params);
		assertEquals("Fake response" + '\n', response);
		verify(httpClient, VerificationModeFactory.times(2)).executeMethod(postMethod);
		verify(postMethod, VerificationModeFactory.times(1)).addParameter("j_username", hudsonServer.getUsername());
		verify(postMethod, VerificationModeFactory.times(1)).addParameter("j_password", hudsonServer.getPassword());
		verify(postMethod, VerificationModeFactory.times(1)).addParameter("action", "login");
		verify(postMethod, VerificationModeFactory.times(2)).getStatusCode();
		verify(postMethod, VerificationModeFactory.times(1)).getResponseBodyAsStream();
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
		 * @param hudsonServerManager the {@link HudsonServerManager}
		 */
		public TestDefaultHudsonServerAccessorImpl(HudsonServerManager hudsonServerManager) {
			super(hudsonServerManager);
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
		protected PostMethod createPostMethod(HudsonServer server, String url) {
			return postMethod;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		protected GetMethod createGetMethod(HudsonServer server, String url) {
			return getMethod;
		}

	}

	/**
	 * Get an {@link InputStream} for a classpath resource
	 * 
	 * @param filename the filename of the classpath resource
	 * @return the {@link InputStream} for the classpath resource
	 */
	private InputStream getXMLAsInputStream(String filename) {
		return Thread.currentThread().getContextClassLoader().getResourceAsStream(filename);
	}

}
