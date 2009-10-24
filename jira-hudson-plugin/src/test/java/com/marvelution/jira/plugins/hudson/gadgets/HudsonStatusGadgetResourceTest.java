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
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.*;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.anyObject;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.atlassian.jira.rest.v1.model.errors.ErrorCollection;
import com.atlassian.jira.rest.v1.model.errors.ValidationError;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.user.util.UserUtil;
import com.atlassian.jira.util.I18nHelper;
import com.atlassian.jira.util.IOUtil;
import com.marvelution.jira.plugins.hudson.api.model.HudsonView;
import com.marvelution.jira.plugins.hudson.api.model.Job;
import com.marvelution.jira.plugins.hudson.api.model.JobsList;
import com.marvelution.jira.plugins.hudson.api.xstream.XStreamMarshaller;
import com.marvelution.jira.plugins.hudson.gadgets.HudsonStatusGadgetResource.HudsonStatusResource;
import com.marvelution.jira.plugins.hudson.gadgets.model.HudsonBuildResource;
import com.marvelution.jira.plugins.hudson.gadgets.model.HudsonProjectResource;
import com.marvelution.jira.plugins.hudson.service.HudsonServer;
import com.marvelution.jira.plugins.hudson.service.HudsonServerAccessDeniedException;
import com.marvelution.jira.plugins.hudson.service.HudsonServerAccessor;
import com.marvelution.jira.plugins.hudson.service.HudsonServerAccessorException;
import com.marvelution.jira.plugins.hudson.service.HudsonServerManager;

/**
 * Testcase for {@link HudsonStatusGadgetResource}
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class HudsonStatusGadgetResourceTest {

	private HudsonStatusGadgetResource gadgetResource;

	@Mock
	private HudsonServerManager serverManager;

	@Mock
	private HudsonServerAccessor serverAccessor;

	@Mock
	private HudsonServer server;

	@Mock
	private JiraAuthenticationContext authenticationContext;

	@Mock
	private UserUtil userUtil;

	@Mock
	private I18nHelper i18nHelper;

	private List<Job> jobs;

	/**
	 * Setup the test variables
	 * 
	 * @throws Exception in case of errors
	 */
	@Before
	public void before() throws Exception {
		MockitoAnnotations.initMocks(this);
		when(authenticationContext.getI18nHelper()).thenReturn(i18nHelper);
		final Answer<String> answer = new Answer<String>() {

			public String answer(InvocationOnMock invocation) throws Throwable {
				return invocation.getArguments()[0].toString();
			}

		};
		when(i18nHelper.getText(anyString())).thenAnswer(answer);
		when(i18nHelper.getText(anyString(), anyObject())).thenAnswer(answer);
		when(i18nHelper.getLocale()).thenReturn(Locale.ENGLISH);
		when(server.getName()).thenReturn("Hudson CI");
		when(server.getHost()).thenReturn("http://localhost:8080/");
		when(server.getSmallImageUrl()).thenReturn("images/");
		jobs = XStreamMarshaller.unmarshal(getXml("Jobs.xml"), JobsList.class).getJobs();
		gadgetResource =
			new HudsonStatusGadgetResource(authenticationContext, userUtil, serverManager, serverAccessor);
	}

	/**
	 * Test {@link HudsonStatusGadgetResource#validate(int, String)}
	 * 
	 * @throws Exception in case of errors
	 */
	@Test
	public void testValidate() throws Exception {
		when(serverManager.isHudsonConfigured()).thenReturn(true);
		when(serverManager.getServer(1)).thenReturn(server);
		final Response response = gadgetResource.validate(1, "");
		assertThat(response.getEntity(), nullValue());
		verify(serverManager, VerificationModeFactory.times(1)).getServer(1);
		verify(serverManager, VerificationModeFactory.times(1)).isHudsonConfigured();
	}

	/**
	 * Test {@link HudsonStatusGadgetResource#validate(int, String)}
	 * 
	 * @throws Exception in case of errors
	 */
	@Test
	public void testValidateView() throws Exception {
		when(serverManager.isHudsonConfigured()).thenReturn(true);
		when(serverManager.getServer(1)).thenReturn(server);
		when(serverAccessor.getViewsList(server)).thenAnswer(new Answer<List<HudsonView>>() {

			public List<HudsonView> answer(InvocationOnMock invocation) throws Throwable {
				final List<HudsonView> views = new ArrayList<HudsonView>();
				views.add(new HudsonView("All", ""));
				return views;
			}

		});
		final Response response = gadgetResource.validate(1, "All");
		assertThat(response.getEntity(), nullValue());
		verify(serverAccessor, VerificationModeFactory.times(1)).getViewsList(server);
		verify(serverManager, VerificationModeFactory.times(1)).getServer(1);
		verify(serverManager, VerificationModeFactory.times(1)).isHudsonConfigured();
	}

	/**
	 * Test {@link HudsonStatusGadgetResource#validate(int, String)}
	 * 
	 * @throws Exception in case of errors
	 */
	@Test
	public void testValidateNoHudsonConfigured() throws Exception {
		when(serverManager.isHudsonConfigured()).thenReturn(false);
		final Response response = gadgetResource.validate(1, "");
		assertThat(response.getEntity(), is(ErrorCollection.class));
		final ErrorCollection errorCollection = (ErrorCollection) response.getEntity();
		assertThat(errorCollection.getErrorMessages().isEmpty(), is(true));
		final List<ValidationError> errors = (List<ValidationError>) errorCollection.getErrors();
		assertThat(errors.isEmpty(), is(false));
		assertThat(errors.size(), is(1));
		assertThat(errors.get(0).getField(), is("serverId"));
		assertThat(errors.get(0).getError(), is("hudson.gadget.error.not.configured"));
		verify(serverManager, VerificationModeFactory.times(1)).isHudsonConfigured();
	}

	/**
	 * Test {@link HudsonStatusGadgetResource#validate(int, String)}
	 * 
	 * @throws Exception in case of errors
	 */
	@Test
	public void testValidateInvalidServerId() throws Exception {
		when(serverManager.isHudsonConfigured()).thenReturn(true);
		when(serverManager.getServer(1)).thenReturn(null);
		final Response response = gadgetResource.validate(1, "");
		assertThat(response.getEntity(), is(ErrorCollection.class));
		final ErrorCollection errorCollection = (ErrorCollection) response.getEntity();
		assertThat(errorCollection.getErrorMessages().isEmpty(), is(true));
		final List<ValidationError> errors = (List<ValidationError>) errorCollection.getErrors();
		assertThat(errors.isEmpty(), is(false));
		assertThat(errors.size(), is(1));
		assertThat(errors.get(0).getField(), is("serverId"));
		assertThat(errors.get(0).getError(), is("hudson.error.invalid.server.selected"));
		verify(serverManager, VerificationModeFactory.times(1)).getServer(1);
		verify(serverManager, VerificationModeFactory.times(1)).isHudsonConfigured();
	}

	/**
	 * Test {@link HudsonStatusGadgetResource#validate(int, String)}
	 * 
	 * @throws Exception in case of errors
	 */
	@Test
	public void testValidateInvalidViewName() throws Exception {
		when(serverManager.isHudsonConfigured()).thenReturn(true);
		when(serverManager.getServer(1)).thenReturn(server);
		when(serverAccessor.getViewsList(server)).thenAnswer(new Answer<List<HudsonView>>() {

			public List<HudsonView> answer(InvocationOnMock invocation) throws Throwable {
				final List<HudsonView> views = new ArrayList<HudsonView>();
				views.add(new HudsonView("All", ""));
				return views;
			}

		});
		final Response response = gadgetResource.validate(1, "Alles");
		assertThat(response.getEntity(), is(ErrorCollection.class));
		final ErrorCollection errorCollection = (ErrorCollection) response.getEntity();
		assertThat(errorCollection.getErrorMessages().isEmpty(), is(true));
		final List<ValidationError> errors = (List<ValidationError>) errorCollection.getErrors();
		assertThat(errors.isEmpty(), is(false));
		assertThat(errors.size(), is(1));
		assertThat(errors.get(0).getField(), is("view"));
		assertThat(errors.get(0).getError(), is("hudson.error.invalid.server.view.selected"));
		verify(serverAccessor, VerificationModeFactory.times(1)).getViewsList(server);
		verify(serverManager, VerificationModeFactory.times(1)).getServer(1);
		verify(serverManager, VerificationModeFactory.times(1)).isHudsonConfigured();
	}

	/**
	 * Test {@link HudsonStatusGadgetResource#validate(int, String)}
	 * 
	 * @throws Exception in case of errors
	 */
	@Test
	public void testValidateServerAccessorException() throws Exception {
		when(serverManager.isHudsonConfigured()).thenReturn(true);
		when(serverManager.getServer(1)).thenReturn(server);
		when(serverAccessor.getViewsList(server)).thenThrow(new HudsonServerAccessorException("Failure"));
		final Response response = gadgetResource.validate(1, "Alles");
		assertThat(response.getEntity(), is(ErrorCollection.class));
		final ErrorCollection errorCollection = (ErrorCollection) response.getEntity();
		assertThat(errorCollection.getErrorMessages().isEmpty(), is(true));
		final List<ValidationError> errors = (List<ValidationError>) errorCollection.getErrors();
		assertThat(errors.isEmpty(), is(false));
		assertThat(errors.size(), is(1));
		assertThat(errors.get(0).getField(), is("serverId"));
		assertThat(errors.get(0).getError(), is("hudson.error.cannot.connect"));
		verify(serverAccessor, VerificationModeFactory.times(1)).getViewsList(server);
		verify(serverManager, VerificationModeFactory.times(1)).getServer(1);
		verify(serverManager, VerificationModeFactory.times(1)).isHudsonConfigured();
	}

	/**
	 * Test {@link HudsonStatusGadgetResource#validate(int, String)}
	 * 
	 * @throws Exception in case of errors
	 */
	@Test
	public void testValidateServerAccessDeniedException() throws Exception {
		when(serverManager.isHudsonConfigured()).thenReturn(true);
		when(serverManager.getServer(1)).thenReturn(server);
		when(serverAccessor.getViewsList(server)).thenThrow(new HudsonServerAccessDeniedException("Failure"));
		final Response response = gadgetResource.validate(1, "Alles");
		assertThat(response.getEntity(), is(ErrorCollection.class));
		final ErrorCollection errorCollection = (ErrorCollection) response.getEntity();
		assertThat(errorCollection.getErrorMessages().isEmpty(), is(true));
		final List<ValidationError> errors = (List<ValidationError>) errorCollection.getErrors();
		assertThat(errors.isEmpty(), is(false));
		assertThat(errors.size(), is(1));
		assertThat(errors.get(0).getField(), is("serverId"));
		assertThat(errors.get(0).getError(), is("hudson.error.access.denied"));
		verify(serverAccessor, VerificationModeFactory.times(1)).getViewsList(server);
		verify(serverManager, VerificationModeFactory.times(1)).getServer(1);
		verify(serverManager, VerificationModeFactory.times(1)).isHudsonConfigured();
	}

	/**
	 * Test {@link HudsonStatusGadgetResource#generate(int, String)}
	 * 
	 * @throws Exception in case of errors
	 */
	@Test
	public void testGenerate() throws Exception {
		when(serverManager.isHudsonConfigured()).thenReturn(true);
		when(serverManager.getServer(1)).thenReturn(server);
		when(serverAccessor.getProjects(server)).thenReturn(jobs);
		final Response response = gadgetResource.generate(1, "");
		assertThat(response.getEntity(), is(HudsonStatusResource.class));
		final HudsonStatusResource statusResource = (HudsonStatusResource) response.getEntity();
		assertThat(statusResource.getServer().getName(), is("Hudson CI"));
		assertThat(statusResource.getServer().getUrl(), is("http://localhost:8080/"));
		assertThat(statusResource.getServer().getImageUrl(), is("images/"));
		assertThat(statusResource.getProjects().isEmpty(), is(false));
		assertThat(statusResource.getProjects().size(), is(1));
		final HudsonProjectResource projectResource =
			((List<HudsonProjectResource>) statusResource.getProjects()).get(0);
		assertThat(projectResource.getName(), is("Marvelution"));
		assertThat(projectResource.getUrl(), is("job/Marvelution/"));
		assertThat(projectResource.getDescription(), is("Marvelution organization Project Object Model"));
		assertThat(projectResource.getBuilds().isEmpty(), is(false));
		assertThat(projectResource.getBuilds().size(), is(1));
		assertThat(((List<HudsonBuildResource>) projectResource.getBuilds()).get(0).getNumber(), is(40));
		verify(serverAccessor, VerificationModeFactory.times(1)).getProjects(server);
		verify(serverManager, VerificationModeFactory.times(1)).getServer(1);
		verify(serverManager, VerificationModeFactory.times(0)).getDefaultServer();
		verify(serverManager, VerificationModeFactory.times(1)).isHudsonConfigured();
	}

	/**
	 * Test {@link HudsonStatusGadgetResource#generate(int, String)}
	 * 
	 * @throws Exception in case of errors
	 */
	@Test
	public void testGenerateView() throws Exception {
		when(serverManager.isHudsonConfigured()).thenReturn(true);
		when(serverManager.getServer(1)).thenReturn(server);
		when(serverAccessor.getView(server, "All")).thenAnswer(new Answer<HudsonView>() {

			public HudsonView answer(InvocationOnMock invocation) throws Throwable {
				final HudsonView view = new HudsonView("All", "");
				view.setJobs(jobs);
				return view;
			}

		});
		final Response response = gadgetResource.generate(1, "All");
		assertThat(response.getEntity(), is(HudsonStatusResource.class));
		final HudsonStatusResource statusResource = (HudsonStatusResource) response.getEntity();
		assertThat(statusResource.getServer().getName(), is("Hudson CI"));
		assertThat(statusResource.getServer().getUrl(), is("http://localhost:8080/"));
		assertThat(statusResource.getServer().getImageUrl(), is("images/"));
		assertThat(statusResource.getProjects().isEmpty(), is(false));
		assertThat(statusResource.getProjects().size(), is(1));
		final HudsonProjectResource projectResource =
			((List<HudsonProjectResource>) statusResource.getProjects()).get(0);
		assertThat(projectResource.getName(), is("Marvelution"));
		assertThat(projectResource.getUrl(), is("job/Marvelution/"));
		assertThat(projectResource.getDescription(), is("Marvelution organization Project Object Model"));
		assertThat(projectResource.getBuilds().isEmpty(), is(false));
		assertThat(projectResource.getBuilds().size(), is(1));
		assertThat(((List<HudsonBuildResource>) projectResource.getBuilds()).get(0).getNumber(), is(40));
		verify(serverAccessor, VerificationModeFactory.times(1)).getView(server, "All");
		verify(serverAccessor, VerificationModeFactory.times(0)).getProjects(server);
		verify(serverManager, VerificationModeFactory.times(1)).getServer(1);
		verify(serverManager, VerificationModeFactory.times(0)).getDefaultServer();
		verify(serverManager, VerificationModeFactory.times(1)).isHudsonConfigured();
	}

	/**
	 * Test {@link HudsonStatusGadgetResource#generate(int, String)}
	 * 
	 * @throws Exception in case of errors
	 */
	@Test
	public void testGenerateDefaultServer() throws Exception {
		when(serverManager.isHudsonConfigured()).thenReturn(true);
		when(serverManager.getServer(1)).thenReturn(null);
		when(serverManager.getDefaultServer()).thenReturn(server);
		when(serverAccessor.getProjects(server)).thenReturn(jobs);
		final Response response = gadgetResource.generate(1, "");
		assertThat(response.getEntity(), is(HudsonStatusResource.class));
		final HudsonStatusResource statusResource = (HudsonStatusResource) response.getEntity();
		assertThat(statusResource.getServer().getName(), is("Hudson CI"));
		assertThat(statusResource.getServer().getUrl(), is("http://localhost:8080/"));
		assertThat(statusResource.getServer().getImageUrl(), is("images/"));
		assertThat(statusResource.getProjects().isEmpty(), is(false));
		assertThat(statusResource.getProjects().size(), is(1));
		final HudsonProjectResource projectResource =
			((List<HudsonProjectResource>) statusResource.getProjects()).get(0);
		assertThat(projectResource.getName(), is("Marvelution"));
		assertThat(projectResource.getUrl(), is("job/Marvelution/"));
		assertThat(projectResource.getDescription(), is("Marvelution organization Project Object Model"));
		assertThat(projectResource.getBuilds().isEmpty(), is(false));
		assertThat(projectResource.getBuilds().size(), is(1));
		assertThat(((List<HudsonBuildResource>) projectResource.getBuilds()).get(0).getNumber(), is(40));
		verify(serverAccessor, VerificationModeFactory.times(1)).getProjects(server);
		verify(serverManager, VerificationModeFactory.times(1)).getServer(1);
		verify(serverManager, VerificationModeFactory.times(1)).getDefaultServer();
		verify(serverManager, VerificationModeFactory.times(1)).isHudsonConfigured();
	}

	/**
	 * Test {@link HudsonStatusGadgetResource#generate(int, String)}
	 * 
	 * @throws Exception in case of errors
	 */
	@Test
	public void testGenerateNoHudsonConfigured() throws Exception {
		when(serverManager.isHudsonConfigured()).thenReturn(false);
		final Response response = gadgetResource.generate(1, "");
		assertThat(response.getEntity(), is(HudsonStatusResource.class));
		final HudsonStatusResource statusResource = (HudsonStatusResource) response.getEntity();
		assertThat(statusResource.getServer(), nullValue());
		assertThat(statusResource.getProjects(), nullValue());
		final Collection<String> errors = statusResource.getErrors();
		assertThat(errors.isEmpty(), is(false));
		assertThat(errors.size(), is(1));
		assertThat(errors.contains("hudson.error.not.configured"), is(true));
		verify(serverManager, VerificationModeFactory.times(1)).isHudsonConfigured();
	}

	/**
	 * Test {@link HudsonStatusGadgetResource#generate(int, String)}
	 * 
	 * @throws Exception in case of errors
	 */
	@Test
	public void testGenerateServerAccessorException() throws Exception {
		when(serverManager.isHudsonConfigured()).thenReturn(true);
		when(serverManager.getServer(1)).thenReturn(server);
		when(serverAccessor.getProjects(server)).thenThrow(new HudsonServerAccessorException("Failure"));
		final Response response = gadgetResource.generate(1, "");
		assertThat(response.getEntity(), is(HudsonStatusResource.class));
		final HudsonStatusResource statusResource = (HudsonStatusResource) response.getEntity();
		assertThat(statusResource.getServer().getName(), is("Hudson CI"));
		assertThat(statusResource.getServer().getUrl(), is("http://localhost:8080/"));
		assertThat(statusResource.getServer().getImageUrl(), is("images/"));
		assertThat(statusResource.getProjects().isEmpty(), is(true));
		final Collection<String> errors = statusResource.getErrors();
		assertThat(errors.isEmpty(), is(false));
		assertThat(errors.size(), is(1));
		assertThat(errors.contains("hudson.error.cannot.connect"), is(true));
		verify(serverAccessor, VerificationModeFactory.times(0)).getView(server, "All");
		verify(serverAccessor, VerificationModeFactory.times(1)).getProjects(server);
		verify(serverManager, VerificationModeFactory.times(1)).getServer(1);
		verify(serverManager, VerificationModeFactory.times(0)).getDefaultServer();
		verify(serverManager, VerificationModeFactory.times(1)).isHudsonConfigured();
	}

	/**
	 * Test {@link HudsonStatusGadgetResource#generate(int, String)}
	 * 
	 * @throws Exception in case of errors
	 */
	@Test
	public void testGenerateServerAccessDeniedException() throws Exception {
		when(serverManager.isHudsonConfigured()).thenReturn(true);
		when(serverManager.getServer(1)).thenReturn(null);
		when(serverManager.getDefaultServer()).thenReturn(server);
		when(serverAccessor.getView(server, "All")).thenThrow(new HudsonServerAccessDeniedException("Failure"));
		final Response response = gadgetResource.generate(1, "All");
		assertThat(response.getEntity(), is(HudsonStatusResource.class));
		final HudsonStatusResource statusResource = (HudsonStatusResource) response.getEntity();
		assertThat(statusResource.getServer().getName(), is("Hudson CI"));
		assertThat(statusResource.getServer().getUrl(), is("http://localhost:8080/"));
		assertThat(statusResource.getServer().getImageUrl(), is("images/"));
		assertThat(statusResource.getProjects().isEmpty(), is(true));
		final Collection<String> errors = statusResource.getErrors();
		assertThat(errors.isEmpty(), is(false));
		assertThat(errors.size(), is(1));
		assertThat(errors.contains("hudson.error.access.denied"), is(true));
		verify(serverAccessor, VerificationModeFactory.times(1)).getView(server, "All");
		verify(serverAccessor, VerificationModeFactory.times(0)).getProjects(server);
		verify(serverManager, VerificationModeFactory.times(1)).getDefaultServer();
		verify(serverManager, VerificationModeFactory.times(1)).getServer(1);
		verify(serverManager, VerificationModeFactory.times(1)).isHudsonConfigured();
	}

	/**
	 * Get an {@link String} content for a classpath resource
	 * 
	 * @param filename the filename of the classpath resource
	 * @return the {@link String} content for the classpath resource
	 * @throws IOException in case of errors
	 */
	private String getXml(String filename) throws IOException {
		final InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream(filename);
		return IOUtil.toString(input);
	}

}
